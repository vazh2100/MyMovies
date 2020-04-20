package com.vazheninapps.mymovies2.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vazheninapps.mymovies2.R;
import com.vazheninapps.mymovies2.adapters.ReviewAdapter;
import com.vazheninapps.mymovies2.adapters.TrailerAdapter;
import com.vazheninapps.mymovies2.data.FavouriteMovie;
import com.vazheninapps.mymovies2.data.Movie;
import com.vazheninapps.mymovies2.data.Review;
import com.vazheninapps.mymovies2.data.Trailer;
import com.vazheninapps.mymovies2.modules.FromJSONGetter;
import com.vazheninapps.mymovies2.modules.FromNetworkDownloader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class DetailedActivity extends AppCompatActivity {

    private ImageView imageViewAddToFavourite;
    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;

    private ScrollView scrollViewInfo;
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    private Intent intent;

    private TrailerAdapter trailerAdapter;
    private ArrayList<Trailer> trailers;

    private ReviewAdapter reviewAdapter;
    private ArrayList<Review> reviews;

    private MainViewModel viewModel;

    private Movie movie;
    private FavouriteMovie favouriteMovie;
    private int movieId;

    private static String lang;


    private void initItems() {
        setContentView(R.layout.activity_detailed);
        intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            movieId = intent.getIntExtra("id", -1);
        } else {
            finish();
        }
        movie = viewModel.getMovieById(movieId);
        if (movie == null) {
            movie = viewModel.getFavouriteMovieById(movieId);
        }

        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavourite);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        scrollViewInfo = findViewById(R.id.scrollViewInfo);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        trailerAdapter = new TrailerAdapter();
        reviewAdapter = new ReviewAdapter();

        lang = Locale.getDefault().getLanguage();
    }

    private void setItems() {
        scrollViewInfo.smoothScrollTo(0, 0);
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setAdapter(trailerAdapter);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToYoutube);
            }
        });
    }

    private void setMovie() {
        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.replace).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewRating.setText(String.format("%s", movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
        checkMovieIsFavouriteAndSetButtonImage();
    }

    private void checkMovieIsFavouriteAndSetButtonImage() {
        favouriteMovie = viewModel.getFavouriteMovieById(movieId);
        if (favouriteMovie == null) {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_add_to);
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.favourite_remove);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initItems();
        setItems();
        setMovie();
        loadTrailers();
        showTrailers();
        loadReviews();
        showReviews();
    }


    private void loadTrailers() {
        JSONObject jsonObjectTrailers = FromNetworkDownloader.getJSONForVideos(movie.getId(), lang);
        trailers = FromJSONGetter.getTrailersFromJSON(jsonObjectTrailers);
    }

    private void showTrailers() {
        trailerAdapter.setTrailers(trailers);
    }

    private void loadReviews() {
        JSONObject jsonObjectReviews = FromNetworkDownloader.getJSONForReviews(movie.getId(), lang);
        reviews = FromJSONGetter.getReviewsFromJSON(jsonObjectReviews);
    }

    private void showReviews() {
        reviewAdapter.setReviews(reviews);
    }


    public void onClickChangeFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.added_to_favourite_toast, Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.removed_from_favourite_toast, Toast.LENGTH_SHORT).show();
        }
        checkMovieIsFavouriteAndSetButtonImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intent1 = new Intent(this, FavouriteActivity.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
