package com.atfotiad.superherosquadmaker.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.atfotiad.superherosquadmaker.databinding.MySquadItemBinding
import com.atfotiad.superherosquadmaker.model.Superhero
import com.atfotiad.superherosquadmaker.ui.HeroDetails
import com.squareup.picasso.Picasso

class SquadAdapter(private var superHeroes: List<Superhero>?, private val context: Context) : RecyclerView.Adapter<SquadAdapter.SquadHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadHolder {
        return SquadHolder(MySquadItemBinding.inflate(LayoutInflater.from(parent.context)
                , parent, false))
    }

    override fun onBindViewHolder(holder: SquadHolder, position: Int) {
        val currentSuperHero = superHeroes!![position]
        val imagePath = currentSuperHero.imageUri
        holder.itemBinding.name.text = currentSuperHero.name
        Picasso.get().load(imagePath).resize(200, 200).into(holder.itemBinding.thumbnail)
        holder.itemBinding.heroCard.setOnClickListener {
            val intent = Intent(context, HeroDetails::class.java)
            intent.putExtra("id", currentSuperHero.id)
            intent.putExtra("heroName", currentSuperHero.name)
            intent.putExtra("image", currentSuperHero.imageUri)
            if (currentSuperHero.description.isEmpty()) {
                intent.putExtra("description", "No Available Description")
            } else intent.putExtra("description", currentSuperHero.description)
            val optionsCompat : ActivityOptionsCompat? = ViewCompat.getTransitionName(holder.itemBinding.heroCard)?.let { it1 ->
                ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity,holder.itemBinding.heroCard,
                        it1)
            }
            if (optionsCompat != null) {
                context.startActivity(intent,optionsCompat.toBundle())
            }
        }
    }

    fun setSuperHeroes(superHeroes: List<Superhero>?) {
        this.superHeroes = superHeroes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (superHeroes != null) {
            superHeroes!!.size
        } else 0
    }

    class SquadHolder(val itemBinding: MySquadItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

}