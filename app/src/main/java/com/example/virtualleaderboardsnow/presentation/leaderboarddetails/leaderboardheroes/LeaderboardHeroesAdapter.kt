package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardheroes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.ListItemHeroesBinding

class LeaderboardHeroesAdapter(private val list: List<Hero>) :
    RecyclerView.Adapter<LeaderboardHeroesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.bind<ListItemHeroesBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_heroes, parent, false)
        )?.let {
            ViewHolder(it)
        }!!


    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(list[position], position + 1)

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ListItemHeroesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Hero, position: Int) {
            with(binding) {
                val frameColor: Int = when (position) {
                    1 -> R.color.gold
                    2 -> R.color.silver
                    3 -> R.color.bronze
                    else -> R.color.froly
                }
                heroName.text = hero.name
                heroScore.text = hero.score.toString()
                rank.text = position.toString()
                trophyFrame.setColorFilter(
                    ContextCompat.getColor(
                        itemView.context,
                        frameColor
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }
}