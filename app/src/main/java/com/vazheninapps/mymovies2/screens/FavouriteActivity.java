package com.vazheninapps.mymovies2.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.vazheninapps.mymovies2.R;
import com.vazheninapps.mymovies2.adapters.MovieAdapter;
import com.vazheninapps.mymovies2.data.FavouriteMovie;
import com.vazheninapps.mymovies2.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavourite;

    private MovieAdapter movieAdapter;

    private MainViewModel viewModel;

    private LiveData<List<FavouriteMovie>> favouriteMovies;

    private void initItems() {
        setContentView(R.layout.activity_favourite);
        recyclerViewFavourite = findViewById(R.id.recyclerViewFavouriteMovies);

        movieAdapter = new MovieAdapter();

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        favouriteMovies = viewModel.getFavouriteMovies();
    }

    private void setItems() {
        recyclerViewFavourite.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewFavourite.setAdapter(movieAdapter);

        movieAdapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                Movie movie = movieAdapter.getMovies().get(position);
                Intent intent = new Intent(FavouriteActivity.this, DetailedActivity.class);
                intent.putExtra("id", movie.getId());
                startActivity(intent);
            }
        });

        favouriteMovies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> favouriteMovies) {
                List<Movie> movies = new ArrayList<>();
                if (favouriteMovies != null) {
                    movies.addAll(favouriteMovies);
                    movieAdapter.setMovies(movies);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initItems();
        setItems();
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
