package app.zimablue.artbookfragmentversion.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.zimablue.artbookfragmentversion.model.ArtEntity

@Database(entities = [ArtEntity::class], version = 1)
abstract class ArtDatabase: RoomDatabase() {

    abstract fun artDao(): ArtDao

//    companion object{
//        private var INSTANCE: ArtDatabase? = null
//
//        fun getInstance(context: Context):ArtDatabase{
//            synchronized(this){
//                var instance = INSTANCE
//
//                if (instance == null ){
//                    instance = Room.databaseBuilder(context.applicationContext,ArtDatabase::class.java,"art_database").fallbackToDestructiveMigration().build()
//
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//
//    }
}