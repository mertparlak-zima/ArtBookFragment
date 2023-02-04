package app.zimablue.artbookfragmentversion.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import app.zimablue.artbookfragmentversion.model.ArtEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtDao {

    @Insert
    suspend fun insert(artEntity: ArtEntity)

    @Delete
    suspend fun delete(artEntity: ArtEntity)

    @Query("SELECT name, id FROM `art-table`")
    fun getArtWithNameAndId():Flow<List<ArtEntity>>

    @Query("SELECT * FROM `art-table` WHERE id = :id")
    fun getArtById(id: Int): Flow<ArtEntity>




}