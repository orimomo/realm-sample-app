package com.example.realm_sample_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.realm_sample_app.databinding.ActivityMainBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }
    private val groupAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.adapter = groupAdapter

        val items = listOf("りんご🍎")
        items.forEach { item ->
            groupAdapter.add(ListItem(item))
        }

        binding.fab.setOnClickListener {
            supportFragmentManager.let { manager ->
                FormDialogFragment().show(manager, FormDialogFragment::class.simpleName)
            }
        }
    }
}
