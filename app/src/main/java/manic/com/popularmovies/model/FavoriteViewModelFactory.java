package manic.com.popularmovies.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import manic.com.popularmovies.database.AppDatabase;

public class FavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mFavoriteId;

    public FavoriteViewModelFactory(AppDatabase database, int mFavoriteId){
        mDb = database;
        this.mFavoriteId = mFavoriteId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FavoriteViewModel(mDb);
    }
}
