package com.atfotiad.superherosquadmaker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.atfotiad.superherosquadmaker.adapters.RecyclerAdapter
import com.atfotiad.superherosquadmaker.database.Repository
import com.atfotiad.superherosquadmaker.databinding.ActivityMainBinding
import com.atfotiad.superherosquadmaker.model.Superhero

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository = Repository(application)
    private val squadList : LiveData<List<Superhero?>?>? =repository.getSquad()


   fun loadJson(adapter: RecyclerAdapter, binding: ActivityMainBinding) {
        repository.loadJson(adapter,binding)
    }


    fun getSquad(): LiveData<List<Superhero?>?>? {
        return squadList
    }

    fun checkIFExists(id: String?): LiveData<Superhero?>?{
        return repository.checkIFExists(id)
    }

    fun insert(superhero: Superhero?) {repository.insert(superhero)}
    fun delete(superhero: Superhero?) {repository.delete(superhero)}

}