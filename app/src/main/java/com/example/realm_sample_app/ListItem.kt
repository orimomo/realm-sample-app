package com.example.realm_sample_app

import androidx.appcompat.app.AlertDialog
import com.example.realm_sample_app.databinding.ItemListBinding
import com.xwray.groupie.databinding.BindableItem

class ListItem(private val obj: ListObject) : BindableItem<ItemListBinding>() {
    override fun getLayout(): Int = R.layout.item_list

    override fun bind(viewBinding: ItemListBinding, position: Int) {
        viewBinding.text = obj.title

        viewBinding.deleteButton.setOnClickListener { view ->
            AlertDialog.Builder(view.context)
                .setMessage("「${obj.title}」のメモを本当に削除しますか？")
                .setPositiveButton("削除") { _, _ ->
                    // 処理
                }
                .setNegativeButton("キャンセル", null)
                .create().show()
        }
    }
}