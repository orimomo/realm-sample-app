package com.example.realm_sample_app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.realm_sample_app.databinding.DialogFormBinding
import io.realm.Realm

class FormDialogFragment : DialogFragment() {
    private var binding: DialogFormBinding? = null
    lateinit var realm: Realm

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogFormBinding.inflate(LayoutInflater.from(activity), null, false)

        binding?.button?.setOnClickListener {
            //keyを設定して
            val id = 1
            //initしたインスタンスをとってきて、トランザクションで書き込み
            realm = Realm.getDefaultInstance()
            realm.executeTransaction { realm ->
                val obj = realm.createObject(ListObject::class.java, id)
                obj.title = "test1"
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
        realm.close()
        super.onDestroyView()
    }
}