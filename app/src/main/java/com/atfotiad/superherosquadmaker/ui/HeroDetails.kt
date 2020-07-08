package com.atfotiad.superherosquadmaker.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atfotiad.superherosquadmaker.R
import com.atfotiad.superherosquadmaker.databinding.ActivityHeroDetailsBinding
import com.atfotiad.superherosquadmaker.databinding.CustomDialogLayoutBinding
import com.atfotiad.superherosquadmaker.model.Superhero
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hero_details.*

class HeroDetails : AppCompatActivity() {
    private lateinit var binding: ActivityHeroDetailsBinding
    private lateinit var dialogLayoutBinding: CustomDialogLayoutBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var superhero: Superhero

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailsBinding.inflate(layoutInflater)
        dialogLayoutBinding= CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fade = android.transition.Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        incomingIntent
        binding.fabHire.setOnClickListener { customDialog("Are you sure you want to hire this character ?",fab_hire)}
        binding.fabFire.setOnClickListener {customDialog("Are you sure you want to fire this character from the squad ?",fab_fire)}

    }


    private val incomingIntent: Unit
        get() {
            val id = intent.getStringExtra("id")
            val name = intent.getStringExtra("heroName")
            val description = intent.getStringExtra("description")
            val image = intent.getStringExtra("image")
            binding.heroName.text = name
            supportActionBar!!.title = "Hero Details"
            Picasso.get().load(image).into(binding.heroImage)
            binding.heroDescription.text = description
            superhero = Superhero(id,name,description,image)
            viewModel.checkIFExists(id)?.observe(this, Observer { t ->
                if (t!=null) {
                    binding.fabHire.visibility = View.GONE
                    binding.fabFire.visibility = View.VISIBLE
                }else{
                    binding.fabFire.visibility = View.GONE
                    binding.fabHire.visibility = View.VISIBLE
                }
            })
        }

    private fun customDialog (content: String, fab: FloatingActionButton) {
        val hireDialog =AlertDialog.Builder(this)
        dialogLayoutBinding.tvDialog.text = content
        if (dialogLayoutBinding.dialogCard.parent!=null)
            (dialogLayoutBinding.dialogCard.parent as ViewGroup).removeView(dialogLayoutBinding.dialogCard)
        hireDialog.setView(dialogLayoutBinding.dialogCard).setCancelable(false).setPositiveButton("yes") { _, _ -> if (fab == fab_hire) viewModel.insert(superhero)
            else viewModel.delete(superhero);finish() }
        hireDialog.setNegativeButton("no") { _,_  -> }.create()
        hireDialog.show()

    }
    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }

}