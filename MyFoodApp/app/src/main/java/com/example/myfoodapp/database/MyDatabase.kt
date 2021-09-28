package com.example.myfoodapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myfoodapp.model.Cart

@Database(entities = arrayOf(Cart::class),version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun cartDao():CartDao

    companion object{
        private var myDatabase:MyDatabase ? =null
        @Synchronized
        fun getDatabase(context: Context):MyDatabase{
            if (myDatabase==null) {
                myDatabase = Room.databaseBuilder(context.applicationContext,MyDatabase::class.java,"ResturantDB.db").fallbackToDestructiveMigration().build()
            }
            return myDatabase!!
        }
    }

}