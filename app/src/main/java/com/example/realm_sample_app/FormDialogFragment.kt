package com.example.realm_sample_app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.realm_sample_app.databinding.DialogFormBinding
import io.realm.Realm

class FormDialogFragment : DialogFragment() {
    private lateinit var viewModel: FormViewModel
    private var binding: DialogFormBinding? = null
    private lateinit var realm: Realm

    private var usedRealm = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFormBinding.inflate(LayoutInflater.from(activity), null, false)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[FormViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        binding?.viewModel = viewModel

        binding?.button?.setOnClickListener {
            viewModel.memo.value?.let { memo ->
                // 書き込み
                val id = 10
                realm = Realm.getDefaultInstance()
                realm.executeTransaction { realm ->
                    val obj = realm.createObject(ListObject::class.java, id)
                    obj.title = memo
                }

                // 読み込み
                val all = realm.where(ListObject::class.java).findAll()
                val titleList = mutableListOf<String>()
                all.forEach { item ->
                    titleList.add(item.title)
                }
                viewModel.list.value = titleList

                usedRealm = true
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
        if (usedRealm) realm.close()
        super.onDestroyView()
    }
}