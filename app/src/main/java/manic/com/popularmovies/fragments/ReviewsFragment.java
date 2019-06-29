package manic.com.popularmovies.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
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
import manic.com.popularmovies.adapters.ReviewAdapter;
import manic.com.popularmovies.database.AppExecutors;
import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.model.Review;
import manic.com.popularmovies.utils.JsonUtils;
import manic.com.popularmovies.utils.NetworkUtils;
import manic.com.popularmovies.utils.Utils;

public class ReviewsFragment extends Fragment implements ReviewAdapter.ReviewAdapterOnClickHandler {
    private static final String TAG = "ReviewsFragment";
    private static final int REVIEW_SEARCH_LOADER = 11;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    private RecyclerView recyclerView;
    private ReviewAdapter mAdapter;

    private Movie movie;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_fragment, container, false);

        recyclerView = view.findViewById(R.id.review_rv);

        mAdapter = new ReviewAdapter(getContext(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(Utils.getDividerItemDecoration(getContext(), mLayoutManager));

        listener.onFragmentCreated(this);
        loadReviews();

        return view;
    }

    public void loadReviews(){
        final List<Review>[] reviews = new List[]{new ArrayList<>()};
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL trailerRequestUrl = NetworkUtils.buildUrl(String.valueOf(movie.getId()), "reviews");
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                    reviews[0] = JsonUtils.parseReviewJson(jsonReviewResponse);
                    mAdapter.setReviews(reviews[0]);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(Review review) {

    }

    public void setMovie(Movie movie){
        this.movie = movie;
    }



    public interface OnFragmentInteractionListener {
        void onFragmentCreated(ReviewsFragment fragment);
    }

    private OnFragmentInteractionListener listener;

    public static Fragment newInstance(Movie movie) {
        ReviewsFragment instance;
        instance = new ReviewsFragment();

        return instance;
    }

    @Override
    public void onAttach(Context context) {try {
        listener = (ReviewsFragment.OnFragmentInteractionListener) getActivity();
    } catch (ClassCastException cce) {
        cce.printStackTrace();
    }
        super.onAttach(context);
    }
}
