package com.atfotiad.superherosquadmaker.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.atfotiad.superherosquadmaker.ui.HeroDetails
import com.atfotiad.superherosquadmaker.databinding.AllHeroesItemBinding
import com.atfotiad.superherosquadmaker.model.Result
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val characterList: MutableList<Result>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AllHeroesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentResult = characterList[position]
        holder.binding.name.text = currentResult.name
        val image = currentResult.thumbnail?.path + "." + (currentResult.thumbnail?.extension ?: "")
        Picasso.get().load(image).resize(150, 150).into(holder.binding.thumbnail)
        holder.binding.heroCard.setOnClickListener {
            val intent = Intent(context, HeroDetails::class.java)
            intent.putExtra("id", currentResult.id)
            intent.putExtra("heroName", currentResult.name)
            intent.putExtra("image", image)
            if (currentResult.description?.isEmpty()!!) {
                intent.putExtra("description", "No Available Description")
            } else intent.putExtra("description", currentResult.description)
            val optionsCompat : ActivityOptionsCompat? = ViewCompat.getTransitionName(holder.binding.thumbnail)?.let { it1 ->
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,holder.binding.thumbnail,
                        it1)
            }
            if (optionsCompat != null) {
                context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    fun addCharacters(characters: List<Result>?) {
        characterList.addAll(characters!!)
        notifyDataSetChanged()
    }

    class MyViewHolder(var binding: AllHeroesItemBinding) : RecyclerView.ViewHolder(binding.root)

}