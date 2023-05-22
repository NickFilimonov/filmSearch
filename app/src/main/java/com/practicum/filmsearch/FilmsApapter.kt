package com.practicum.filmsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FilmsApapter(): RecyclerView.Adapter<FilmViewHolder> () {

    var films = ArrayList<Film>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder = FilmViewHolder(parent)

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        holder.bind(films[position])
    }

    override fun getItemCount(): Int =  films.size
}