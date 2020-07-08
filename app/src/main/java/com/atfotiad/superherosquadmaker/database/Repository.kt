package com.atfotiad.superherosquadmaker.database

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import com.atfotiad.superherosquadmaker.adapters.RecyclerAdapter
import com.atfotiad.superherosquadmaker.database.SquadDatabase.Companion.databaseWriteExecutor
import com.atfotiad.superherosquadmaker.database.SquadDatabase.Companion.getDatabase
import com.atfotiad.superherosquadmaker.databinding.ActivityMainBinding
import com.atfotiad.superherosquadmaker.model.MarvelResponse
import com.atfotiad.superherosquadmaker.model.Result
import com.atfotiad.superherosquadmaker.model.Superhero
import com.atfotiad.superherosquadmaker.retrofit.ApiClient
import com.atfotiad.superherosquadmaker.retrofit.ApiClient.API_KEY
import com.atfotiad.superherosquadmaker.retrofit.ApiClient.LIMIT
import com.atfotiad.superherosquadmaker.retrofit.ApiClient.OFFSET
import com.atfotiad.superherosquadmaker.retrofit.ApiClient.ts
import com.atfotiad.superherosquadmaker.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Repository(application: Application?) {
    private val squadDao: SquadDao
    private val mySquad: LiveData<List<Superhero?>?>?
    private var results = arrayListOf<Result>()
    private val apiService: ApiInterface


    fun insert(superhero: Superhero?) {
        databaseWriteExecutor.execute { squadDao.insert(superhero) }
    }

    fun delete(superhero: Superhero?) {
        databaseWriteExecutor.execute { squadDao.delete(superhero) }
    }

    fun loadJson(adapter: RecyclerAdapter, binding: ActivityMainBinding) {
        val apiCall = apiService.getResults(API_KEY, ts,ApiClient.md5hash, LIMIT, OFFSET)
        apiCall!!.enqueue(object : Callback<MarvelResponse?> {
            override fun onResponse(call: Call<MarvelResponse?>, response: Response<MarvelResponse?>) {
                assert(response.body() != null)
                results = Objects.requireNonNull(response.body()!!.data)!!.results as ArrayList<Result>
                adapter.addCharacters(results)
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<MarvelResponse?>, t: Throwable) {
                Log.e("From Repo", "onFailure: " + t.message)
            }
        })
    }


    fun getSquad() : LiveData<List<Superhero?>?>? {
        return squadDao.squad
    }

    fun checkIFExists(id: String?): LiveData<Superhero?>?{
        return squadDao.checkIFExists(id)
    }

    init {
        val squadDatabase = getDatabase(application!!)!!
        squadDao = squadDatabase.dao()
        mySquad = squadDao.squad
        apiService = ApiClient.getClient()
    }
}