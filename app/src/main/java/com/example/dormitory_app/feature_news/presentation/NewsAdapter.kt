package com.example.dormitory_app.feature_news.presentation

import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dormitory_app.R
import com.example.dormitory_app.feature_news.external.messages.FileDto
import com.example.dormitory_app.feature_news.messages.dtos.NewsDtoWithFavoriteField
import com.example.dormitory_app.feature_news.messages.requests.SavedNewsRequest
import com.example.dormitory_app.feature_news.util.CurrentScreen
import com.example.dormitory_app.feature_news.util.HeaderCategory
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import dagger.hilt.android.AndroidEntryPoint


class NewsAdapter(
    private val viewModel: NewsViewModel
) : ListAdapter<Any, RecyclerView.ViewHolder> (DiffUtilNews()) {
    companion object {
        private const val HEADER_TYPE = 0
        private const val NEWS_TYPE = 1
    }
    class DiffUtilNews : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is NewsDtoWithFavoriteField && newItem is NewsDtoWithFavoriteField) {
                oldItem.id == newItem.id
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
            return if (oldItem is NewsDtoWithFavoriteField && newItem is NewsDtoWithFavoriteField) {
                oldItem.id == newItem.id && oldItem.title == newItem.title &&
                        oldItem.content == newItem.content && oldItem.favorite == newItem.favorite &&
                        oldItem.category == newItem.category && areListsEqual(oldItem.images, newItem.images) &&
                        areListsEqual(oldItem.documents, newItem.documents)
            }
            else false
        }

        private fun areListsEqual(list1: List<FileDto>, list2: List<FileDto>): Boolean {
            if (list1.size != list2.size) return false
            for (i in list1.indices) {
                if (list1[i] != list2[i]) return false
            }
            return true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_TYPE
        } else {
            NEWS_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> HeaderViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_news_header, parent, false)
            )
            NEWS_TYPE -> NewsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.news_item, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            HEADER_TYPE -> {
                (holder as HeaderViewHolder).bindData()
            }
            NEWS_TYPE -> {
                val newsItem = getItem(position) as NewsDtoWithFavoriteField
                (holder as NewsViewHolder).bindData(newsItem)
            }
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNewsHeader = itemView.findViewById<MaterialTextView>(R.id.tvNewsHeader)
        private val btnSearchNews = itemView.findViewById<ImageButton>(R.id.btnSearchNews)
        private val btnSavedNews = itemView.findViewById<ImageButton>(R.id.btnSavedNews)
        private val tvCategoryAll = itemView.findViewById<MaterialTextView>(R.id.tvCategoryAll)
        private val tvCategoryNews = itemView.findViewById<MaterialTextView>(R.id.tvCategoryNews)
        private val tvCategoryOrders = itemView.findViewById<MaterialTextView>(R.id.tvCategoryOrders)
        private val btnBackFromSearch = itemView.findViewById<ImageButton>(R.id.btnBackFromSearch)
        private val etSearchNews = itemView.findViewById<TextInputEditText>(R.id.etSearchNews)
        private val btnBackFromSavedNews = itemView.findViewById<ImageButton>(R.id.btnBackFromSavedNews)
        private val tvSavedNewsHeader = itemView.findViewById<MaterialTextView>(R.id.tvSavedNewsHeader)

        private val colorToSet = ContextCompat.getColor(itemView.context, R.color.blue)

        fun bindData() {
            if (viewModel.state.currentScreen == CurrentScreen.START_SCREEN) {
                tvSavedNewsHeader.visibility = View.GONE
                btnBackFromSavedNews.visibility = View.GONE
                etSearchNews.visibility = View.GONE
                etSearchNews.isEnabled = false
                btnBackFromSearch.visibility = View.GONE
                btnSavedNews.visibility = View.VISIBLE
                btnSearchNews.visibility = View.VISIBLE
                tvNewsHeader.visibility = View.VISIBLE
                tvCategoryOrders.visibility = View.VISIBLE
                tvCategoryNews.visibility = View.VISIBLE
                tvCategoryAll.visibility = View.VISIBLE
                when (viewModel.state.headerCategory) {
                    HeaderCategory.ALL -> tvCategoryAll.setTextColor(colorToSet)
                    HeaderCategory.NEWS -> tvCategoryNews.setTextColor(colorToSet)
                    HeaderCategory.ORDERS -> tvCategoryOrders.setTextColor(colorToSet)
                    HeaderCategory.NONE -> {}
                }
            }
            else if (viewModel.state.currentScreen == CurrentScreen.SAVED_SCREEN) {
                tvSavedNewsHeader.visibility = View.VISIBLE
                btnBackFromSavedNews.visibility = View.VISIBLE
                etSearchNews.visibility = View.GONE
                etSearchNews.isEnabled = false
                btnBackFromSearch.visibility = View.GONE
                btnSavedNews.visibility = View.GONE
                btnSearchNews.visibility = View.GONE
                tvNewsHeader.visibility = View.GONE
                tvCategoryOrders.visibility = View.GONE
                tvCategoryNews.visibility = View.GONE
                tvCategoryAll.visibility = View.GONE
            }
            else {
                tvSavedNewsHeader.visibility = View.GONE
                btnBackFromSavedNews.visibility = View.GONE
                etSearchNews.visibility = View.VISIBLE
                etSearchNews.isEnabled = true
                btnBackFromSearch.visibility = View.VISIBLE
                btnSavedNews.visibility = View.GONE
                btnSearchNews.visibility = View.GONE
                tvNewsHeader.visibility = View.GONE
                tvCategoryOrders.visibility = View.VISIBLE
                tvCategoryNews.visibility = View.VISIBLE
                tvCategoryAll.visibility = View.VISIBLE
                when (viewModel.state.headerCategory) {
                    HeaderCategory.ALL -> tvCategoryAll.setTextColor(colorToSet)
                    HeaderCategory.NEWS -> tvCategoryNews.setTextColor(colorToSet)
                    HeaderCategory.ORDERS -> tvCategoryOrders.setTextColor(colorToSet)
                    HeaderCategory.NONE -> {}
                }
            }
            //categoryAll
            tvCategoryAll.setOnClickListener {
                if (viewModel.state.headerCategory != HeaderCategory.ALL) {
                    if (viewModel.state.searchPattern.isBlank()) {
                        viewModel.onEvent(NewsUIEvent.GetAllNewsDesc())
                    }
                    else {
                        viewModel.onEvent(NewsUIEvent.GetAllNewsDesc(viewModel.state.searchPattern))
                    }
                    viewModel.onEvent(NewsUIEvent.HeaderCategoryChanged(HeaderCategory.ALL))
                }
            }

            tvCategoryNews.setOnClickListener {
                if (viewModel.state.headerCategory != HeaderCategory.NEWS) {
                    if (viewModel.state.searchPattern.isBlank()) {
                        viewModel.onEvent(NewsUIEvent.GetArticlesDesc())
                    }
                    else {
                        viewModel.onEvent(NewsUIEvent.GetArticlesDesc(viewModel.state.searchPattern))
                    }
                    viewModel.onEvent(NewsUIEvent.HeaderCategoryChanged(HeaderCategory.NEWS))
                }
            }

            tvCategoryOrders.setOnClickListener {
                if (viewModel.state.headerCategory != HeaderCategory.ORDERS) {
                    Log.println(Log.DEBUG, "WTFFFFFFFf", "Мы в приказе")
                    if (viewModel.state.searchPattern.isBlank()) {
                        viewModel.onEvent(NewsUIEvent.GetOrdersDesc())
                        Log.println(Log.DEBUG, "WTFFFFFFFf", "Мы в приказе1")
                    }
                    else {
                        viewModel.onEvent(NewsUIEvent.GetOrdersDesc(viewModel.state.searchPattern))
                        Log.println(Log.DEBUG, "WTFFFFFFFf", "Мы в приказе2")
                    }
                    viewModel.onEvent(NewsUIEvent.HeaderCategoryChanged(HeaderCategory.ORDERS))
                }

            }

            btnSavedNews.setOnClickListener {
                viewModel.onEvent(NewsUIEvent.CurrentScreenChanged(CurrentScreen.SAVED_SCREEN))
                viewModel.onEvent(NewsUIEvent.HeaderCategoryChanged(HeaderCategory.NONE))
                viewModel.onEvent(NewsUIEvent.GetSavedNews)
            }

            btnSearchNews.setOnClickListener {
                viewModel.onEvent(NewsUIEvent.CurrentScreenChanged(CurrentScreen.SEARCH_SCREEN))
                when (viewModel.state.headerCategory) {
                    HeaderCategory.ALL -> viewModel.onEvent(NewsUIEvent.GetAllNewsDesc())
                    HeaderCategory.NEWS -> viewModel.onEvent(NewsUIEvent.GetArticlesDesc())
                    HeaderCategory.ORDERS -> viewModel.onEvent(NewsUIEvent.GetOrdersDesc())
                    HeaderCategory.NONE -> {}
                }
            }
            btnBackFromSearch.setOnClickListener {
                viewModel.onEvent(NewsUIEvent.CurrentScreenChanged(CurrentScreen.START_SCREEN))
                viewModel.onEvent(NewsUIEvent.SearchPatternChanged(""))
                when (viewModel.state.headerCategory) {
                    HeaderCategory.ALL -> viewModel.onEvent(NewsUIEvent.GetAllNewsDesc())
                    HeaderCategory.NEWS -> viewModel.onEvent(NewsUIEvent.GetArticlesDesc())
                    HeaderCategory.ORDERS -> viewModel.onEvent(NewsUIEvent.GetOrdersDesc())
                    HeaderCategory.NONE -> {}
                }
            }

            btnBackFromSavedNews.setOnClickListener {
                viewModel.onEvent(NewsUIEvent.CurrentScreenChanged(CurrentScreen.START_SCREEN))
                viewModel.onEvent(NewsUIEvent.HeaderCategoryChanged(HeaderCategory.ALL))
                viewModel.onEvent(NewsUIEvent.GetAllNewsDesc())
            }
            etSearchNews.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.println(Log.DEBUG, "WTFFFF", "ПОИСК")
                    Log.println(Log.DEBUG, "WTFFFF", viewModel.state.searchPattern)
                    if (viewModel.state.headerCategory == HeaderCategory.ALL) {
                        Log.println(Log.DEBUG, "WTFFFF", viewModel.state.searchPattern)
                        viewModel.onEvent(NewsUIEvent.GetAllNewsDesc(viewModel.state.searchPattern))

                    }
                    else if (viewModel.state.headerCategory == HeaderCategory.NEWS) {
                        viewModel.onEvent(NewsUIEvent.GetArticlesDesc(viewModel.state.searchPattern))
                    }
                    else {
                        viewModel.onEvent(NewsUIEvent.GetOrdersDesc(viewModel.state.searchPattern))
                    }
                    true
                }
                else {
                    false
                }
            }
            etSearchNews.addTextChangedListener(
                object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        viewModel.onEvent(NewsUIEvent.SearchPatternChanged(s.toString()))
                        Log.println(Log.DEBUG, "WTFFFFF", s.toString())
                    }

                }
            )
        }
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNewsTitle = itemView.findViewById<TextView>(R.id.tvNewsTitle)
        private val tvNewsContent = itemView.findViewById<TextView>(R.id.tvNewsContent)
        private val tvNewsShowMore = itemView.findViewById<TextView>(R.id.tvShowMore)
        private val viewPager2 = itemView.findViewById<ViewPager2>(R.id.viewPager2)
        private val slideDotLL = itemView.findViewById<LinearLayout>(R.id.slideDotLL)
        private val btnSave = itemView.findViewById<ImageButton>(R.id.btnToSaveNews)
        private val filesLL = itemView.findViewById<LinearLayout>(R.id.filesLL)
        private val greyLine = itemView.findViewById<View>(R.id.greyLine)
        private val btnDeleteNews = itemView.findViewById<MaterialButton>(R.id.btnDeleteNews)
        private val btnChangeNews = itemView.findViewById<MaterialButton>(R.id.btnChangeNews)

        private var isExpanded = false
        private val imageAdapter = ImageAdapter()
        private var pageChangeListener: ViewPager2.OnPageChangeCallback? = null
        private val layoutParams = viewPager2.layoutParams as ConstraintLayout.LayoutParams
        init {
            viewPager2.adapter = imageAdapter
        }

        fun bindData(item: NewsDtoWithFavoriteField) {
            var saved = item.favorite
            tvNewsTitle.text = item.title
            tvNewsContent.text = item.content
            if (viewModel.state.userRole == "ADMIN") {
                greyLine.visibility = View.VISIBLE
                btnDeleteNews.visibility = View.VISIBLE
                btnChangeNews.visibility = View.VISIBLE
            }

            btnDeleteNews.setOnClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle("Удаление новости")
                    .setMessage("Вы уверены, что хотите удалить новость?")
                    .setPositiveButton("Да") {_, _ ->
                        viewModel.onEvent(NewsUIEvent.DeleteNews(item.id))
                        notifyItemRemoved(adapterPosition)
                        notifyItemRangeChanged(adapterPosition, itemCount)
                    }
                    .setNegativeButton("Нет", null)
                    .show()
            }

            if (tvNewsContent.lineCount <= 3) {
                tvNewsShowMore.visibility = View.GONE
            }
            tvNewsShowMore.setOnClickListener {
                isExpanded = !isExpanded
                tvNewsContent.maxLines = if (isExpanded) Int.MAX_VALUE else 3
                tvNewsShowMore.text = if (isExpanded) "Скрыть" else "Показать еще"
            }
            if (saved) {
                btnSave.setImageResource(R.drawable.baseline_bookmark_24)
            }
            btnSave.setOnClickListener {
                saved = !saved
                if (saved) {
                    viewModel.onEvent(NewsUIEvent.AddToSaveNews(
                        SavedNewsRequest(
                            newsId = item.id
                        )
                    ))
                }
                if (!saved) {
                    viewModel.onEvent(NewsUIEvent.DeleteFromSaved(item.id))
                }
                btnSave.setImageResource(
                    if (saved) {
                        R.drawable.baseline_bookmark_24
                    } else {
                        R.drawable.outline_bookmark_border_24
                    }
                )
            }

            val imageList = item.images
            imageAdapter.submitList(imageList)
            slideDotLL.removeAllViews()
            val dotsImage = Array(imageList.size) {
                ImageView(itemView.context)
            }
            dotsImage.forEachIndexed { index, imageView ->
                imageView.setImageResource(
                    if (index == viewPager2.currentItem) R.drawable.active_dot else R.drawable.non_active_dot
                )
                slideDotLL.addView(imageView)
            }
            pageChangeListener = object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    dotsImage.forEachIndexed { index, imageView ->
                        imageView.setImageResource(
                            if (position == index) R.drawable.active_dot else R.drawable.non_active_dot
                        )
                    }
                }
            }
            viewPager2.registerOnPageChangeCallback(pageChangeListener!!)

            if (imageList.isEmpty()) {
                viewPager2.visibility = View.GONE
                layoutParams.height = 0
            }
            else {
                viewPager2.visibility = View.VISIBLE
                layoutParams.height = 400.dpToPx() // высота в dp
            }
            if (imageList.size <= 1) {
                slideDotLL.removeAllViews()
            }
            viewPager2.layoutParams = layoutParams

            val fileList = item.documents
            val filesViews = fileList.map {
                val fileItemView = LayoutInflater.from(itemView.context).inflate(R.layout.file_item, null)
                val tvFileName = fileItemView.findViewById<MaterialTextView>(R.id.tvFileName)
                tvFileName.text = it.fileName

                fileItemView
            }
            filesLL.removeAllViews()
            filesViews.forEach {
                filesLL.addView(it)
            }
        }
        private fun Int.dpToPx(): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (this * scale + 0.5f).toInt()
        }
    }

}