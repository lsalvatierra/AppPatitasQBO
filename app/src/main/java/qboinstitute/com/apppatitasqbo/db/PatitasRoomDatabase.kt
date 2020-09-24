package qboinstitute.com.apppatitasqbo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import qboinstitute.com.apppatitasqbo.db.dao.PersonaDao
import qboinstitute.com.apppatitasqbo.db.entity.PersonaEntity

@Database(entities = [PersonaEntity::class], version = 1)
//@Database(entities = [PersonaEntity::class], version = 2, exportSchema = true)
public abstract class PatitasRoomDatabase : RoomDatabase() {
    //android:allowBackup="false"

    abstract fun personaDao(): PersonaDao
    //Agrupa todos las variables y métodos que están definidos como
    //estáticos
    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        //Indicamos que el cambio que pueda tener esta variable
        //se vuelve inmediata para otros subprocesos
        @Volatile
        private var INSTANCE: PatitasRoomDatabase? = null

        fun getDatabase(context: Context): PatitasRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            //este método es utilizado sólo por un hilo de ejecución
            //haciendo que los demás hilos esperen.
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatitasRoomDatabase::class.java,
                    "patitasdb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}