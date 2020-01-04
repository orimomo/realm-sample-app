package com.example.realm_sample_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.realm_sample_app.databinding.FragmentListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

class ListFragment : Fragment() {
    private lateinit var viewModel: ViewModel
    private var binding: FragmentListBinding? = null
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private lateinit var realm: Realm
    private var usedRealm = false

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初回読み込み
        readRealm()

        // 新規作成した後の読み込み
        viewModel.list.observe(viewLifecycleOwner, Observer { all ->
            groupAdapter.clear()
            addSortedItems(all)
        })

        // 削除する
        viewModel.deleteId.observe(viewLifecycleOwner, Observer { id ->
            deleteRealm(id)
            groupAdapter.clear()
            readRealm()
        })
    }

    override fun onDestroyView() {
        binding = null
        if (usedRealm) realm.close()
        super.onDestroyView()
    }

    private fun readRealm() {
        realm = Realm.getDefaultInstance()
        val all = realm.where(ListObject::class.java).findAll()
        addSortedItems(all)
    }

    private fun addSortedItems(list: RealmResults<ListObject>) {
        val sortedAll = list.sort("id", Sort.DESCENDING)
        sortedAll.forEach { obj ->
            groupAdapter.add(ListItem(obj, viewModel))
        }
    }

    private fun deleteRealm(id: Int) {
        // 削除対象を取得
        val target = realm.where(ListObject::class.java)
            .equalTo("id",id)
            .findAll()
        // 削除
        realm.executeTransaction {
            target.deleteFromRealm(0)
        }
        usedRealm = true
    }
}