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
    lateinit var realm: Realm

    private var usedRealm = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFormBinding.inflate(LayoutInflater.from(activity), null, false)
        viewModel = ViewModelProviders.of(this).get(FormViewModel::class.java)
        binding?.viewModel = viewModel

        binding?.button?.setOnClickListener {
            viewModel.memo.value?.let { memo ->
                //initしたインスタンスをとってきて、トランザクションで書き込み
                val id = 2
                realm = Realm.getDefaultInstance()
                realm.executeTransaction { realm ->
                    val obj = realm.createObject(ListObject::class.java, id)
                    obj.title = memo
                }
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