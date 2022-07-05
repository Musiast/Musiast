package com.example.musiast.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.musiast.R
import com.example.musiast.databinding.ItemArticleBinding
import com.example.musiast.model.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {
    companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }
            override fun areContentsTheSame(oldItem: Article, newItem: Article) : Boolean {
                return newItem.title == oldItem.title
            }
        }
    }
    class ArticleViewHolder(var view: ItemArticleBinding) : RecyclerView.ViewHolder(view.root)
    val differ = AsyncListDiffer(this, diffUtilCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ArticleViewHolder {
        val  inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemArticleBinding>(inflater, R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }
    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        holder.view.article = article
        // Item CLick Listener
        //Bind these click listeners later
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                article.let { article ->
                    it(article)
                }
            }
        }
    }
    override fun getItemId(position: Int) = position.toLong()
    private var onItemClickListener: ((Article) -> Unit)? = null

}