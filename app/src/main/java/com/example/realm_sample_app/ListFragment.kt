package com.example.realm_sample_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.realm_sample_app.databinding.FragmentListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.realm.Realm

class ListFragment : Fragment() {
    private var binding: FragmentListBinding? = null
    private val groupAdapter = GroupAdapter<ViewHolder>()
    lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false).also {
            it.recyclerView.adapter = groupAdapter

            it.fab.setOnClickListener {
                activity?.supportFragmentManager?.let { manager ->
                    FormDialogFragment().show(manager, FormDialogFragment::class.simpleName)
                }
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 読み込み
        realm = Realm.getDefaultInstance()
        val all = realm.where(ListObject::class.java).findAll()
        all.forEach { item ->
            groupAdapter.add(ListItem(item.title))
        }
    }
}