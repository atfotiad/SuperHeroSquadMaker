package com.atfotiad.superherosquadmaker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//This is the superhero model that is going to be persisted in the database
@Entity(tableName = "squad_table")
class Superhero(
        @PrimaryKey
        var id: String,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "description")
        var description: String,
        @ColumnInfo(name = "image")
        var imageUri: String)