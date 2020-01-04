package com.example.realm_sample_app

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.realm_sample_app.databinding.FragmentListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.realm.Realm
import io.realm.Sort

class ListFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private var binding: FragmentListBinding? = null
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var realm: Realm
    private var usedRealm = false
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProviders.of(this)[ViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        binding = FragmentListBinding.inflate(inflater, container, false).also {
            it.recyclerView.adapter = groupAdapter
            it.recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

            it.fab.setOnClickListener {
                activity?.supportFragmentManager?.let { manager ->
                    FormDialogFragment().show(manager, FormDialogFragment::class.simpleName)
                }
            }
        }

        realm = Realm.getDefaultInstance()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readRealm()

        viewModel.newMemo.observe(viewLifecycleOwner, Observer { memo ->
            createRealm(memo)
        })

        viewModel.deleteId.observe(viewLifecycleOwner, Observer { id ->
            deleteRealm(id)
        })
    }

    override fun onDestroyView() {
        binding = null
        if (usedRealm) realm.close()
        super.onDestroyView()
    }

    private fun readRealm() {
        val all = realm.where(ListObject::class.java).findAll()
        val sortedAll = all.sort("id", Sort.DESCENDING)
        sortedAll.forEach { obj ->
            groupAdapter.add(ListItem(obj, viewModel))
        }
    }

    private fun createRealm(title: String) {
        val savedId = sharedPreferences.getInt(ViewModel.KEY.REALM_ID.name, id)
        val id = savedId + 1
        realm.executeTransaction { realm ->
            val obj = realm.createObject(ListObject::class.java, id)
            obj.title = title
        }
        // 再描画
        groupAdapter.clear()
        readRealm()

        usedRealm = true
        sharedPreferences.edit().putInt(ViewModel.KEY.REALM_ID.name, id).apply()
    }

    private fun deleteRealm(id: Int) {
        val target = realm.where(ListObject::class.java)
            .equalTo("id",id)
            .findAll()
        realm.executeTransaction {
            target.deleteFromRealm(0)
        }
        // 再描画
        groupAdapter.clear()
        readRealm()

        usedRealm = true
    }
}