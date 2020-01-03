package com.example.realm_sample_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realm_sample_app.databinding.FragmentListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.realm.Realm

class ListFragment : Fragment() {
    private lateinit var viewModel: FormViewModel
    private var binding: FragmentListBinding? = null
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this)[FormViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

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

        // 初回読み込み
        realm = Realm.getDefaultInstance()
        val all = realm.where(ListObject::class.java).findAll()
        all.forEach { item ->
            groupAdapter.add(ListItem(item.title))
        }

        viewModel.list.observe(viewLifecycleOwner, Observer { list ->
            groupAdapter.clear()
            list.forEach { title ->
                groupAdapter.add(ListItem(title))
            }
        })
    }
}