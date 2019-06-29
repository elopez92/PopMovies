package manic.com.popularmovies.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import manic.com.popularmovies.adapters.MovieAdapter;
import manic.com.popularmovies.database.AppDatabase;

public class MovieViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MovieAdapter mAdapter;
    private final String sortBy;

    public MovieViewModelFactory(MovieAdapter mAdapter, String sortBy) {
        this.mAdapter = mAdapter;
        this.sortBy = sortBy;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(mAdapter, sortBy);
    }
}
