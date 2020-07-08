package com.atfotiad.superherosquadmaker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.atfotiad.superherosquadmaker.model.Superhero
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@Database(entities = [Superhero::class],version = 1,exportSchema = false)
abstract class SquadDatabase : RoomDatabase() {
    abstract fun dao(): SquadDao

    companion object {
        @Volatile
        private var INSTANCE: SquadDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)
        fun getDatabase(context: Context): SquadDatabase? {
            if (INSTANCE == null) {
                synchronized(SquadDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                SquadDatabase::class.java, "squad_database")
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}