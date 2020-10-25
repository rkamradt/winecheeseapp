package net.kamradtfamily.winecheeseapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class WineCheesePostViewHolder (view: View)
    : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.wine)
    private val subtitle: TextView = view.findViewById(R.id.cheese)
    private val score: TextView = view.findViewById(R.id.pairing)
     private var post : WineCheesePost? = null

    fun bind(post: WineCheesePost?) {
        this.post = post
        title.text = post?.wine ?: "loading"
        subtitle.text = itemView.context.resources.getString(R.string.post_subtitle,
            post?.cheese ?: "unknown")
        score.text = "${post?.pairing ?: 0}"
    }

    companion object {
        fun create(parent: ViewGroup): WineCheesePostViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.wine_cheese_post_item, parent, false)
            return WineCheesePostViewHolder(view)
        }
    }

    fun updateScore(item: WineCheesePost?) {
        post = item
        score.text = "${item?.pairing ?: 0}"
    }
}
