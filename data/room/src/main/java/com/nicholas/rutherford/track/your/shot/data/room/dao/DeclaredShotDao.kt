package com.nicholas.rutherford.track.your.shot.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.nicholas.rutherford.track.your.shot.data.room.entities.DeclaredShotEntity

@Dao
interface DeclaredShotDao {

    @Insert
    suspend fun insert(declaredShotEntity: DeclaredShotEntity)

    @Insert
    suspend fun insert(declaredShotEntities: List<DeclaredShotEntity>)

    @Update
    suspend fun update(declaredShotEntity: DeclaredShotEntity)

    @Query("DELETE FROM declaredShots")
    suspend fun deleteAll()

    @Query("DELETE FROM declaredShots WHERE id = :id")
    suspend fun deleteDeclaredShotById(id: Int)

    @Query("SELECT * FROM declaredShots ORDER BY title ASC")
    suspend fun getAllDeclaredShots(): List<DeclaredShotEntity>

    @Query("SELECT * FROM declaredShots WHERE id = :id")
    suspend fun getDeclaredShotFromId(id: Int): DeclaredShotEntity?

    @Query("SELECT * FROM declaredShots WHERE REPLACE(title, ' ', '') LIKE '%' || REPLACE(:searchQuery, ' ', '') || '%' ORDER BY title ASC")
    suspend fun getDeclaredShotsBySearchQuery(searchQuery: String): List<DeclaredShotEntity>

    @Query("SELECT COALESCE(MAX(id), 1) FROM declaredShots")
    suspend fun getMaxIdOrDefault(): Int
}
