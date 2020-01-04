package com.example.realm_sample_app

import com.example.realm_sample_app.databinding.ItemListBinding
import com.xwray.groupie.databinding.BindableItem

class ListItem(private val obj: ListObject) : BindableItem<ItemListBinding>() {
    override fun getLayout(): Int = R.layout.item_list

    override fun bind(viewBinding: ItemListBinding, position: Int) {
        viewBinding.text = obj.title

        viewBinding.deleteButton.setOnClickListener {
            // 確認ダイアログを表示
        }
    }
}