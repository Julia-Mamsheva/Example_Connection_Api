package com.example.myapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Model.Result
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemObjectBinding


class AdapterCharacter(private val listener: Listener): RecyclerView.Adapter<AdapterCharacter.CharacterHolder>()
{
    private val characterList = ArrayList<Result>()
    class CharacterHolder (item: View):RecyclerView.ViewHolder(item) {
        private val binding = ItemObjectBinding.bind(item)
        fun bind(result: Result,listener: Listener) = with(binding)
        {
            nameCharacter.text = result.name
            speciesCharacter.text = result.species
            Glide.with(binding.root).load(result.image).into(imageCharacter)
            objectItem.setOnClickListener()
            {
                listener.OnClick(result)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_object,parent,false)
        return  CharacterHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        holder.bind(characterList[position],listener)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    fun addResult(character: Result)
    {
        characterList.add(character)
        notifyDataSetChanged()
    }
    interface Listener
    {        fun OnClick(result: Result)

    }
}