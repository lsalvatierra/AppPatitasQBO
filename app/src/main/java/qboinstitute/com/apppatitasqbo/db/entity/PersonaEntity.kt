package qboinstitute.com.apppatitasqbo.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona")
data class PersonaEntity(
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    val id: Int,
    //@ColumnInfo(name = "word")
    //val word: String
    val nombres: String,
    val apellidos: String,
    val email: String,
    val celular: String,
    val usuario: String,
    val password: String,
    val esvoluntario: String
)
