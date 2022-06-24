package com.example.a10.dars.sodda.passportgeneration.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a10.dars.sodda.passportgeneration.databinding.RvItemBinding
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport

class MyRecycklerViewAdapter(val list: ArrayList<Passport>, val onRootClickListener: OnRootClickListener) :
    RecyclerView.Adapter<MyRecycklerViewAdapter.Vh>() {
    inner class Vh(val rvItemBinding: RvItemBinding) : RecyclerView.ViewHolder(rvItemBinding.root) {
        fun onBind(model: Passport) {
            rvItemBinding.nameTv.text = "${position+1}. ${model.name} ${model.surName}"
            rvItemBinding.passportTv.text = model.passportNumber
            rvItemBinding.root.setOnClickListener{
                onRootClickListener.onRootClickListener(model,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val model = list[position]
        holder.onBind(model)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnRootClickListener {
        fun onRootClickListener(passport: Passport, position: Int)
    }

}
