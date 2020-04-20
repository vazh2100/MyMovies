package com.vazheninapps.mymovies2.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movies")
public class FavouriteMovie extends Movie {
    public FavouriteMovie(int uniqueId, int id, int voteCount, String title, String originalTitle, String overview, String smallPosterPath, String bigPosterPath, String backDropPath, double voteAverage, String releaseDate) {
        super(uniqueId, id, voteCount, title, originalTitle, overview, smallPosterPath, bigPosterPath, backDropPath, voteAverage, releaseDate);
    }
    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getVoteCount(), movie.getTitle(), movie.getOriginalTitle(), movie.getOverview(), movie.getSmallPosterPath(), movie.getBigPosterPath(), movie.getBackDropPath(),movie.getVoteAverage(), movie.getReleaseDate());
    }
}
