package com.example.javamoviesratingapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.javamoviesratingapp.adapter.Adaptery;
import com.example.javamoviesratingapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    // Link for popular movies https://api.themoviedb.org/3/movie/top_rated?api_key=96e65c274f8988d468a2f9b8e76f54a3
    private static final String JSON_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=96e65c274f8988d468a2f9b8e76f54a3";

    private List<Movie> movieList;
    private RecyclerView recyclerView;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man);

        movieList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        ImageView header = findViewById(R.id.imageView);

        Glide.with(this)
                .load(R.drawable.header)
                .into(header);

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> {
            // Clear the movie list
            movieList.clear();

            // Execute the background task using ExecutorService
            executorService.execute(() -> {
                String result = fetchDataFromUrl();

                // Post the results back to the UI thread
                handler.post(() -> handleResult(result));
            });
        });

        // how to check the movie list in logcat
        System.out.println("Movie List: " + movieList);
    }

    private String fetchDataFromUrl() {
        StringBuilder current = new StringBuilder();
        try {
            URL apiUrl = new URL(MainActivity.JSON_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();

            try {
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();
                while (data != -1) {
                    current.append((char) data);
                    data = inputStreamReader.read();
                }

                return current.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return current.toString();
    }

    private void handleResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                Movie model = new Movie();
                model.setId(jsonObject1.getString("vote_average"));
                model.setName(jsonObject1.getString("title"));
                model.setImage(jsonObject1.getString("poster_path"));

                movieList.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("MainActivity", "Error occurred during JSON parsing");
        }

        putDataIntoRecyclerView(movieList);
    }

    private void putDataIntoRecyclerView(List<Movie> movieList) {
        Adaptery adaptery = new Adaptery(this, movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adaptery);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}