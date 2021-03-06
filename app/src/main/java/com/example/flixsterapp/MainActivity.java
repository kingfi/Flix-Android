package com.example.flixsterapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixsterapp.adapters.MovieAdapter;
import com.example.flixsterapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
    public static final String TAG = "MainActivity";

    List<Movie> movies;
    Switch darkModeSwitch;
    ConstraintLayout conLayout;
    String backgroundCol;
    int txtColor;
    RecyclerView rvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conLayout = findViewById(R.id.conLayout);
        backgroundCol = "black";
        txtColor = getResources().getColor(R.color.white);


        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

        //hide action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // create and display recycler view
        adapt(txtColor);

        // Set up switch
        darkModeSwitch = findViewById(R.id.darkModeSwitch);
        darkModeSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    // The toggle is enabled
                    // Light Mode
                    conLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    backgroundCol = "white";
                    txtColor = getResources().getColor(R.color.black);
                    adapt(txtColor);
                    Log.i(TAG, Integer.toString(txtColor));



                } else {
                    // The toggle is disabled
                    //Dark Mode
                    conLayout.setBackgroundColor(getResources().getColor(R.color.black));
                    backgroundCol = "black";
                    txtColor = getResources().getColor(R.color.white);
                    adapt(txtColor);
                }
            }
        });

    }

    // Function to set up the Recycler View
    public void adapt (int txtColor){
        Toast.makeText(this,"ADAPT",Toast.LENGTH_SHORT).show();
        // Create the adapter
        final MovieAdapter movieAdapter = new MovieAdapter(this, movies, backgroundCol, txtColor);

        rvMovies = findViewById(R.id.rvMovies);

        // Set the adapter on the Recycler View
        rvMovies.setAdapter(movieAdapter);

        //Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        String url = NOW_PLAYING_URL + getString(R.string.tmdbNowPlayingAPIKey);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());
                    if (movies.size() == 0) {
                        movies.addAll(Movie.fromJsonArray(results));
                        movieAdapter.notifyDataSetChanged();
                    }
                    Log.i(TAG, "Movies: " + movies.size());


                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                    e.printStackTrace();

                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");

            }
        });

    };

}