package dev.dstankovic.popularmovies.network;

import dev.dstankovic.popularmovies.model.GenreObject;
import dev.dstankovic.popularmovies.model.MovieObject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("genre/movie/list")
    Observable<GenreObject> getGenres(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Observable<MovieObject> getPopularMovies(@Query("api_key") String apiKey);

}
