package com.atfotiad.superherosquadmaker.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.atfotiad.superherosquadmaker.R
import com.atfotiad.superherosquadmaker.adapters.RecyclerAdapter
import com.atfotiad.superherosquadmaker.adapters.SquadAdapter
import com.atfotiad.superherosquadmaker.databinding.ActivityMainBinding
import com.atfotiad.superherosquadmaker.model.MarvelResponse
import com.atfotiad.superherosquadmaker.model.Result
import com.atfotiad.superherosquadmaker.model.Superhero
import com.atfotiad.superherosquadmaker.retrofit.ApiClient
import com.atfotiad.superherosquadmaker.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    // binding var for inflating  views in main activity
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var squadAdapter: SquadAdapter
    private lateinit var superheroAdapter: RecyclerAdapter

    private var superHeroes: List<Result> = ArrayList() //heroList from the api response
    private var mySquad:List<Superhero>? = null // heroList that persists in our database
    private var offset = 0
    private val limit = 20
    private var page = 1

    //variable that checks if it's the first time app is opened
    private var isFirstTime =true

    //Variables for Pagination
    private var isLoading = true
    private var pastVisibleItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var previousTotal = 0
    private val viewThreshold = 20

    //get client
    private val apiService: ApiInterface = ApiClient.getClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //exclude action bar ,status bar and navigation bar from the animation for better UX
        val fade = android.transition.Fade()
        fade.excludeTarget(R.id.action_bar_container, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
        window.exitTransition = fade

        val layoutManager = LinearLayoutManager(this)
        val squadLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        //get viewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        superheroAdapter = RecyclerAdapter(superHeroes as MutableList<Result>, this)
        squadAdapter = SquadAdapter(mySquad,this)

        binding.allSuperheroes.layoutManager = layoutManager
        binding.allSuperheroes.adapter =superheroAdapter
        binding.allSuperheroes.setHasFixedSize(true)

        binding.mySquadRecyclerView.layoutManager = squadLayoutManager
        binding.mySquadRecyclerView.adapter = squadAdapter
        binding.progressBar.visibility = View.VISIBLE

        // fetch data and Observe Database changes
        mainViewModel.loadJson(superheroAdapter,binding)
        mainViewModel.getSquad()?.observe(this, Observer { t ->
            @Suppress("UNCHECKED_CAST")
            squadAdapter.setSuperHeroes(t as List<Superhero>?)
            if (t != null && t.isNotEmpty()) {
                binding.mySquad.visibility = View.VISIBLE ; binding.mySquadRecyclerView.visibility = View.VISIBLE
                if (isFirstTime){
                    isFirstTime = false

                }else{
                    squadLayoutManager.smoothScrollToPosition(binding.mySquadRecyclerView,null,t.lastIndex)
                    Handler().postDelayed({ squadLayoutManager.smoothScrollToPosition(binding.mySquadRecyclerView,null,t.indexOfFirst { true }) },1500)
                }
            }else if (t!= null && t.isEmpty()) {binding.mySquad.visibility = View.GONE ; binding.mySquadRecyclerView.visibility = View.GONE}
        })

        //logic for pagination

        binding.allSuperheroes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = layoutManager.childCount
                totalItemCount = layoutManager.itemCount
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                if (dy > 0) {
                    if (isLoading) {
                        if (totalItemCount > previousTotal) {
                            isLoading = false
                            previousTotal = totalItemCount
                        }
                    }
                    if (!isLoading && totalItemCount - visibleItemCount <= pastVisibleItems + viewThreshold) {
                        //perform pagination
                        offset += 20
                        page++
                        performPagination()
                        isLoading = true
                    }
                }
            }
        })
    }

    private fun performPagination() {
        binding.progressBar.visibility = View.VISIBLE
        apiService.getResults("fd8305a90f6de37776a56429d280578c",
                "1", "dfabe3f090fc7c3c0c13b118438345a7", limit, offset)?.enqueue(object : Callback<MarvelResponse?> {
            override fun onResponse(call: Call<MarvelResponse?>, response: Response<MarvelResponse?>) {
                assert(response.body() != null)
                if (response.body()!!.data?.offset?.toInt() ?: 0 < response.body()!!.data?.total?.toInt() ?: 0) {
                    val characters = response.body()!!.data?.results
                    superheroAdapter.addCharacters(characters)
                    Toast.makeText(this@MainActivity, " Page $page is loaded.. ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, " No more characters available", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<MarvelResponse?>, t: Throwable) {
                Log.e("FAILED ", "onFailure: " + t.message)
            }
        })
    }

}






