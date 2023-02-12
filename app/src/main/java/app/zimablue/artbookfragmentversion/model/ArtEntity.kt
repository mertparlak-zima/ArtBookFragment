package app.zimablue.artbookfragmentversion.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "art-table")
class ArtEntity(

    @ColumnInfo(name = "name")
    @NonNull var artName : String,

    @NonNull @ColumnInfo(name = "artistname")
    var artistName: String,

    @NonNull @ColumnInfo(name = "year")
    var year : String,

    @NonNull @ColumnInfo(name = "image")
    var image : ByteArray

) {
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}