package dev.dstankovic.popularmovies.network;

import java.util.List;

import dev.dstankovic.popularmovies.BuildConfig;
import dev.dstankovic.popularmovies.model.Genre;
import dev.dstankovic.popularmovies.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TheMovieDbApi {

    String BASE_URL = "https://api.themoviedb.org/3/";
    String API_KEY =  BuildConfig.THEMOVIEDB_API_KEY;

    @GET("genre/movie/list?api_key=" + API_KEY)
    Call<List<Genre>> getGenres();

    @GET("movie/popular?api_key=" + API_KEY)
    Call<List<Movie>> getMovies();

}
