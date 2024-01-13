package com.example.javamoviesratingapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.javamoviesratingapp.model.Movie;
import com.example.javamoviesratingapp.R; // Import the R class from your app's package
import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {

    private Context movieContext;
    private List<Movie> movieData;
    private static final String IMAGE_URL = "https://image.tmdb.org/t/p/w500/";
    // https://image.tmdb.org/t/p/w600_and_h900_bestv2/

    public Adaptery(Context movieContext, List<Movie> movieData) {
        this.movieContext = movieContext;
        this.movieData = movieData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Adaptery", "onCreateViewHolder called");
        LayoutInflater inflater = LayoutInflater.from(movieContext);
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("Adaptery", "onBindViewHolder called for position: " + position);
        holder.id.setText(movieData.get(position).getId());
        holder.name.setText(movieData.get(position).getName());

        // Use the Glide library to load the image from the URL into the ImageView
        // add the image link  to the image name

        Glide.with(movieContext)
                .load(IMAGE_URL + movieData.get(position).getImage())
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return movieData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView name;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_text);
            name = itemView.findViewById(R.id.name_text);
            image = itemView.findViewById(R.id.image);
        }

    }
}
