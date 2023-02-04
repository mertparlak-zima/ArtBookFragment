package app.zimablue.artbookfragmentversion.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "art-table")
class ArtEntity(

    @ColumnInfo(name = "name")
    var artName : String,

    @ColumnInfo(name = "artistname")
    var artistName: String?,

    @ColumnInfo(name = "year")
    var year : String?,

    @ColumnInfo(name = "image")
    var image : ByteArray?

) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}