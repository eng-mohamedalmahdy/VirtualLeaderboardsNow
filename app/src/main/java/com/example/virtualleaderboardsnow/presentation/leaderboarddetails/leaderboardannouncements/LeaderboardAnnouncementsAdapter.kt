package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.ListItemAnnouncementBinding
import com.example.virtualleaderboardsnow.domain.themeColor
import com.google.android.material.chip.Chip


class LeaderboardAnnouncementsAdapter(private val announcements: List<Announcement>) :
    RecyclerView.Adapter<LeaderboardAnnouncementsAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ListItemAnnouncementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(announcementModel: Announcement) {
            with(binding) {
                announcement.text = announcementModel.title
                announcementModel.changes.forEach {
                    val chip = Chip(itemView.context)
                    val backgroundColor = ColorStateList.valueOf(
                        itemView.context.themeColor(R.attr.colorAccent)
                    )
                    chip.chipBackgroundColor = backgroundColor
                    chip.text = it
                    announcementHeroes.addView(chip)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        DataBindingUtil.bind<ListItemAnnouncementBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_announcement, parent, false)
        )?.let {
            ViewHolder(it)
        }!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(announcements[position])

    override fun getItemCount(): Int = announcements.size
}