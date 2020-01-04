package com.example.realm_sample_app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.realm_sample_app.databinding.DialogFormBinding

class FormDialogFragment : DialogFragment() {
    private lateinit var viewModel: ViewModel
    private var binding: DialogFormBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFormBinding.inflate(LayoutInflater.from(activity), null, false)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[ViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        binding?.viewModel = viewModel

        binding?.button?.setOnClickListener {
            viewModel.memo.value?.let { memo ->
                viewModel.updateItem.value?.let {
                    // 更新だった場合
                    viewModel.updateMemo.value = memo
                } ?: run {
                    // 新規作成だった場合
                    viewModel.newMemo.value = memo
                }
                viewModel.clearMemo()
                dismiss()
            }
        }

        return super.onCreateDialog(savedInstanceState).apply {
            binding?.let {
                setContentView(it.root)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}