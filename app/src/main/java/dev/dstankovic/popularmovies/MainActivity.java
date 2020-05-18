package dev.dstankovic.popularmovies;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dev.dstankovic.popularmovies.model.Genre;
import dev.dstankovic.popularmovies.model.Movie;
import dev.dstankovic.popularmovies.network.TheMovieDbApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView tvApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvApiCall = findViewById(R.id.tv_api_call);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TheMovieDbApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheMovieDbApi api = retrofit.create(TheMovieDbApi.class);

        Call<List<Genre>> call = api.getGenres();

        call.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                List<Genre> genres = response.body();

                for (Genre genre : genres) {
                    tvApiCall.append(genre.getName() + "\n\n");
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
