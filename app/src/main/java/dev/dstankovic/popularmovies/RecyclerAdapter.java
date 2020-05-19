package dev.dstankovic.popularmovies;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import dev.dstankovic.popularmovies.model.Genre;
import dev.dstankovic.popularmovies.model.Movie;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    public static final String TAG = RecyclerAdapter.class.getSimpleName();
    private List<Movie> moviesList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public void setMovies(List<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    public void updateMovie(Movie movie) {
        moviesList.set(moviesList.indexOf(movie), movie);
        notifyItemChanged(moviesList.indexOf(movie));
    }

    public List<Movie> getMovies(){
        return moviesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieReleaseYear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.iv_movie_poster);
            movieTitle = itemView.findViewById(R.id.tv_title);
            movieReleaseYear = itemView.findViewById(R.id.tv_year);
        }

        public void bind(Movie movie) {
            movieTitle.setText(movie.getTitle());
            movieReleaseYear.setText(movie.getReleaseDate().substring(0,4));

            // Picasso
            Picasso.get()
                    .load(Uri.parse("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher)
                    .into(moviePoster);
        }

    }
}
