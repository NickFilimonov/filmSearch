package com.practicum.filmsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FilmViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)) {

        var title: TextView = itemView.findViewById(R.id.film_name)
        var description: TextView = itemView.findViewById(R.id.film_released)
        var poster: ImageView = itemView.findViewById(R.id.film_poster)

    fun  bind(film: Film) {
        title.text = film.title
        description.text = film.description
        Glide.with(itemView)
            .load(film.image)
            .centerCrop()
            .into(poster)


    }

}

