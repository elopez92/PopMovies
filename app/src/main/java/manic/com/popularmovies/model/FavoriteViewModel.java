package manic.com.popularmovies.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import manic.com.popularmovies.database.AppDatabase;

public class FavoriteViewModel extends ViewModel {

    private LiveData<List<Favorite>> favoritesL;
    private List<Favorite> favorites;
    private Favorite favorite;
    private AppDatabase database;

    public FavoriteViewModel(AppDatabase database) {
        favoritesL = database.favoriteDao().loadAllFavorites();
        this.database = database;
    }

    public LiveData<List<Favorite>> getFavoritesL() {
        return favoritesL;
    }

    public List<Favorite> getFavorites() {
        try {
            favorites = new GetFavorites().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return favorites; }

    public Favorite getFavorite(final int mFavoriteId){
        try {
            favorite = new GetFavorite().execute(mFavoriteId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    private class GetFavorite extends AsyncTask<Integer, Void, Favorite>{

        @Override
        protected Favorite doInBackground(Integer... integers) {
            return database.favoriteDao().loadFavoriteById(integers[0]);
        }
    }

    private class GetFavorites extends AsyncTask<Void, Void, List<Favorite>>{

        @Override
        protected List<Favorite> doInBackground(Void... voids) {
            return database.favoriteDao().loadFavorites();
        }
    }
}
