package com.example.virtualleaderboardsnow.presentation.leaderboarddetails.leaderboardannouncements.createannouncementdialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment

import android.widget.EditText

import androidx.annotation.Nullable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.virtualleaderboardsnow.R
import com.example.virtualleaderboardsnow.databinding.DialogCreateAnnouncementBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val TAG = "CreateAnnouncementDialo"

class CreateAnnouncementDialog : DialogFragment() {

    private lateinit var binding: DialogCreateAnnouncementBinding
    private val args by navArgs<CreateAnnouncementDialogArgs>()
    private val viewModel by viewModel<CreateAnnouncementViewModel> { parametersOf(args.boardId) }
    private var configsAdapter: AnnouncementConfigAdapter? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCreateAnnouncementBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ${args.boardId}")
        with(viewLifecycleOwner.lifecycleScope) {
            launch {
                viewModel.getAnnouncementConfigList().collect {
                    configsAdapter = AnnouncementConfigAdapter(it)
                    binding.boardHeroes.adapter = configsAdapter
                    binding.boardHeroes.layoutManager = LinearLayoutManager(requireContext())
                }
            }
            binding.announce.setOnClickListener {
                if (configsAdapter != null && configsAdapter?.getChangedList()
                        ?.isNotEmpty() == true
                ) viewModel.submitUpdates(
                    binding.nameEditText.text.toString(),
                    configsAdapter?.getChangedList()!!
                )
                dismiss()
            }
        }
    }
}