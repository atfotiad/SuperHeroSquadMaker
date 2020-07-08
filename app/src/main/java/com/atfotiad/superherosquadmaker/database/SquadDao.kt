package com.atfotiad.superherosquadmaker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.atfotiad.superherosquadmaker.model.Superhero

@Dao
interface SquadDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(superhero: Superhero?)

    @Delete
    fun delete(superhero: Superhero?)

    @get:Query("select * from squad_table ")
    val squad: LiveData<List<Superhero?>?>?

    @Query("select *from squad_table where id=:id")
    fun checkIFExists(id: String?): LiveData<Superhero?>?
}