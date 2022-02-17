package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.ListItemAnnouncementModificationsBinding

private const val TAG = "AnnouncementConfigAdapt"
class AnnouncementConfigAdapter(private val announcementConfigs: List<AnnouncementConfig>) :
    RecyclerView.Adapter<AnnouncementConfigAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataBindingUtil.bind<ListItemAnnouncementModificationsBinding>(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_announcement_modifications, parent, false)
        )?.let { ViewHolder(it) }!!

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(announcementConfigs[position],position)

    override fun getItemCount() = announcementConfigs.size

    fun getChangedList() = announcementConfigs.filterNot { it.heroScore == 0 }

    inner class ViewHolder(private val binding: ListItemAnnouncementModificationsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(announcementConfig: AnnouncementConfig,position: Int) {
            with(binding) {
                heroName.text = announcementConfig.heroName
                inc.setOnClickListener { score += 1; announcementConfigs[position].heroScore += 1 }
                dec.setOnClickListener { score -= 1; announcementConfigs[position].heroScore -= 1 }
                score.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun afterTextChanged(p0: Editable?) {
                        if (p0.toString().isDigitsOnly() && p0.toString().isNotEmpty())
                            announcementConfigs[position].heroScore = p0.toString().toInt()
                    }
                })
            }

        }

    }

}


private operator fun EditText.plusAssign(i: Int) {
    if (text.toString().isNullOrEmpty()) setText("$i")
    else if (text.toString().removePrefix("-").isDigitsOnly()) {
        setText((text.toString().toInt() + i).toString())
    }
}

private operator fun EditText.minusAssign(i: Int) = this.plusAssign(i * -1)

