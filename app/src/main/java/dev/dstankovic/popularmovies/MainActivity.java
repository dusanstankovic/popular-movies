package dev.dstankovic.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.reactivestreams.Subscription;

import dev.dstankovic.popularmovies.models.Movie;
import dev.dstankovic.popularmovies.models.MovieDetails;
import dev.dstankovic.popularmovies.models.MovieObject;
import dev.dstankovic.popularmovies.requests.ServiceGenerator;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_KEY = BuildConfig.THEMOVIEDB_API_KEY;

    private CompositeDisposable disposables = new CompositeDisposable();

    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        getMoviesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Movie movie) {
                        adapter.updateMovie(movie);
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

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(adapter);
    }

    private Observable<MovieDetails> getMovieDetailsObservable(int id) {
        return ServiceGenerator.getRequestApi()
                .getMovieDetails(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Movie> getMoviesObservable() {
        return ServiceGenerator.getRequestApi()
                .getPopularMovies(API_KEY)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<MovieObject, ObservableSource<Movie>>() {
                    @Override
                    public ObservableSource<Movie> apply(MovieObject movieObject) throws Exception {
                        adapter.setMovies(movieObject.getMovies());
                        return Observable.fromIterable(movieObject.getMovies())
                                .subscribeOn(Schedulers.io());
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
