package manic.com.popularmovies.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import manic.com.popularmovies.R;
import manic.com.popularmovies.adapters.TrailerAdapter;
import manic.com.popularmovies.database.AppExecutors;
import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.model.Trailer;
import manic.com.popularmovies.utils.JsonUtils;
import manic.com.popularmovies.utils.NetworkUtils;
import manic.com.popularmovies.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailerFragment extends Fragment implements TrailerAdapter.TrailerAdapterOnClickHandler{
    private static final String TAG = "TrailerFragment";
    private static final int REVIEW_SEARCH_LOADER = 12;

    private RecyclerView trailer_rv;
    private TrailerAdapter mTrailerAdapter;
    Movie movie;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //startLoader();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentCreated(TrailerFragment fragment);
    }

    private OnFragmentInteractionListener listener;

    public static Fragment newInstance(Movie movie) {
        TrailerFragment instance;
        instance = new TrailerFragment();

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trailer_fragment, container, false);
        mTrailerAdapter = new TrailerAdapter(getContext(), this);
        trailer_rv = view.findViewById(R.id.trailer_rv);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        trailer_rv.setAdapter(mTrailerAdapter);
        trailer_rv.setLayoutManager(mLayoutManager);
        trailer_rv.addItemDecoration(Utils.getDividerItemDecoration(getContext(), mLayoutManager));
        listener.onFragmentCreated(this);
        trailer_rv.setItemViewCacheSize(0);

        getTrailers();

        return view;
    }

    public void getTrailers(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL trailerRequestUrl = NetworkUtils.buildUrl(String.valueOf(movie.getId()), "videos");
                    String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                    List<Trailer> trailers = JsonUtils.parseTrailerJson(jsonTrailerResponse);
                    mTrailerAdapter.setTrailerData(trailers);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }



    @Override
    public void onAttach(Context context) {
        try {
            listener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(Trailer trailer) {
        if (trailer != null) {
            String videoId = trailer.getKey();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_video, videoId)));
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_video, videoId)));
            try {
                getContext().startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                getContext().startActivity(webIntent);
            }
        }
    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }

    public TrailerAdapter getAdapter(){
        return mTrailerAdapter;
    }
}
