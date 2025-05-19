package com.example.appfilmes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appfilmes.R
import com.example.appfilmes.Filme

class FilmeAdapter(
    private var filmes: List<Filme>,
    private val onItemClick: (Filme) -> Unit
) : RecyclerView.Adapter<FilmeAdapter.FilmeViewHolder>() {

    inner class FilmeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.tvTitulo)
        init {
            view.setOnClickListener {
                onItemClick(filmes[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filme, parent, false)
        return FilmeViewHolder(view)
    }

    override fun getItemCount(): Int = filmes.size

    override fun onBindViewHolder(holder: FilmeViewHolder, position: Int) {
        holder.tituloTextView.text = filmes[position].titulo
    }

    fun updateList(newList: List<Filme>) {
        filmes = newList
        notifyDataSetChanged()
    }
}