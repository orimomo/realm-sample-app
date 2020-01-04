package com.example.realm_sample_app

import androidx.appcompat.app.AlertDialog
import com.example.realm_sample_app.databinding.ItemListBinding
import com.xwray.groupie.databinding.BindableItem

class ListItem(private val obj: ListObject, private val viewModel: ViewModel) : BindableItem<ItemListBinding>() {
    override fun getLayout(): Int = R.layout.item_list

    override fun bind(viewBinding: ItemListBinding, position: Int) {
        viewBinding.text = obj.title

        viewBinding.deleteButton.setOnClickListener { view ->
            AlertDialog.Builder(view.context)
                .setMessage("「${obj.title}」のメモを削除しますか？")
                .setPositiveButton("削除") { _, _ ->
                    viewModel.deleteId.value = obj.id
                }
                .setNegativeButton("キャンセル", null)
                .create().show()
        }

        viewBinding.root.setOnLongClickListener {
            viewModel.updateItem.value = obj
            true
        }
    }
}