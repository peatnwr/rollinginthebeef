package com.example.rollinginthebeef.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rollinginthebeef.dataclass.Category
import com.example.rollinginthebeef.databinding.CategoryItemLayoutBinding

class CategoryAdapter (var categoryList: ArrayList<Category>?, val context: Context)
    :RecyclerView.Adapter<CategoryAdapter.ViewHolder>(){
    inner class ViewHolder(view: View, val binding: CategoryItemLayoutBinding)
        :RecyclerView.ViewHolder(view){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        binding.title.text = categoryList!![position].category_name
        binding.title.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return categoryList!!.size
    }
}