package com.example.sqlitedemo

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.sqlitedemo.databinding.ItemsRowBinding

class ItemAdapter(val context: Context, val items: ArrayList<EmpModelClass>) :
        RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemsRowBinding = ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemsRowBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, position)

    }

    // viewbinding을 ViewHolder에서 처리
    inner class ViewHolder(private val binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind(empModelClass: EmpModelClass, position: Int){
            binding.apply {
                tvName.text = empModelClass.name
                tvEmailId.text = empModelClass.email
                if( position % 2 == 0){
                    llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLightGray))
                }else{
                    llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
                }
                ivEdit.setOnClickListener {
                    if(context is MainActivity){
                        context.updateRecordDialog(items[position])
                    }
                }
                ivDelete.setOnClickListener {
                    if(context is MainActivity){
                        context.deleteRecordAlertDialog(items[position])
                    }
                }
            }
        }
    }
}