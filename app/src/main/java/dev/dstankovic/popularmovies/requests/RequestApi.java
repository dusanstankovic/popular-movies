package dev.dstankovic.popularmovies.requests;

import dev.dstankovic.popularmovies.models.GenreObject;
import dev.dstankovic.popularmovies.models.MovieDetails;
import dev.dstankovic.popularmovies.models.MovieObject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestApi {

    @GET("genre/movie/list")
    Observable<GenreObject> getGenres(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieObject> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Observable<MovieDetails> getMovieDetails(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
