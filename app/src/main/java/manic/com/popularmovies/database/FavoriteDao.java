package manic.com.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import manic.com.popularmovies.model.Favorite;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    LiveData<List<Favorite>> loadAllFavorites();

    @Query("SELECT * FROM favorite")
    List<Favorite> loadFavorites();

    @Insert
    void insertFavorie(Favorite favorite);

    @Delete
    void deleteFavorite(Favorite favorite);

    @Query("DELETE FROM favorite WHERE id = :id")
    void deleteFavorite(int id);

    @Query("SELECT * FROM favorite WHERE id = :id")
    Favorite loadFavoriteById(int id);


}
