package com.example.realm_sample_app

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.example.realm_sample_app.databinding.DialogFormBinding
import io.realm.Realm

class FormDialogFragment : DialogFragment() {
    private lateinit var viewModel: ViewModel
    private var binding: DialogFormBinding? = null
    private lateinit var realm: Realm
    private var usedRealm = false
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFormBinding.inflate(LayoutInflater.from(activity), null, false)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[ViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        binding?.viewModel = viewModel

        binding?.button?.setOnClickListener {
            viewModel.memo.value?.let { memo ->
                // 書き込み
                val savedId = sharedPreferences.getInt(KEY.REALM_ID.name, id)
                val id = savedId + 1
                realm = Realm.getDefaultInstance()
                realm.executeTransaction { realm ->
                    val obj = realm.createObject(ListObject::class.java, id)
                    obj.title = memo
                }

                // 読み込み
                val all = realm.where(ListObject::class.java).findAll()
                viewModel.list.value = all

                // 後処理
                sharedPreferences.edit().putInt(KEY.REALM_ID.name, id).apply()
                viewModel.clearMemo()
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

    enum class KEY {
        REALM_ID
    }
}