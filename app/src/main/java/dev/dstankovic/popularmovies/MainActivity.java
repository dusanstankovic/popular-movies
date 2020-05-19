package dev.dstankovic.popularmovies;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import dev.dstankovic.popularmovies.BuildConfig;
import dev.dstankovic.popularmovies.model.GenreObject;
import dev.dstankovic.popularmovies.model.Movie;
import dev.dstankovic.popularmovies.model.MovieObject;
import dev.dstankovic.popularmovies.network.ApiClient;
import dev.dstankovic.popularmovies.network.ApiInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;
    private CompositeDisposable disposables = new CompositeDisposable();
    private ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ApiClient.getClient().create(ApiInterface.class);

        getMoviesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Movie, ObservableSource<Movie>>() {
                    @Override
                    public ObservableSource<Movie> apply(Movie movie) throws Exception {
                        return getGenresObservable(movie);
                    }
                })
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Movie movie) {
                        Log.d(TAG, "We are inside getMoviesObservable(): " + movie.getTitle());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private Observable<Movie> getGenresObservable(final Movie movie) {
        Log.d(TAG, "Inside getGenresObservable(): " + movie.getGenreIds());
        return api.getGenres(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<GenreObject, Movie>() {
            @Override
            public Movie apply(GenreObject genreObject) throws Exception {
                movie.setGenres(movie.getGenreIds());
                return movie;
            }
        });
    }

    private Observable<Movie> getMoviesObservable() {
        return api.getPopularMovies(API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<MovieObject, ObservableSource<Movie>>() {
                    @Override
                    public ObservableSource<Movie> apply(MovieObject movieObject) throws Exception {
                        // this is where you can add movies to RecyclerView
                        Log.d(TAG, "Inside getMoviesObservable(): " + movieObject.getMovies().size());
                        return Observable.fromIterable(movieObject.getMovies())
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
