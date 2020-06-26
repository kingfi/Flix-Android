package com.example.flixsterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixsterapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    TextView popularity;
    TextView ratingTxt;
    RatingBar rbVoteAverage;
    ImageView moviePoster;
    ConstraintLayout constraintLayout;
    TextView trailerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        popularity = (TextView) findViewById(R.id.popularitytxt);
        ratingTxt = (TextView) findViewById(R.id.ratingTxt);
        moviePoster = (ImageView) findViewById(R.id.moviePoster);
        constraintLayout = findViewById(R.id.conDetailsLayout);
        trailerTxt = findViewById(R.id.textView);

        trailerTxt.setAlpha(1);

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // get colors
        String backgroundCol = getIntent().getStringExtra("backgroundCol");
        int txtCol = getIntent().getIntExtra("txtColor", getResources().getColor(R.color.white));



        // Connect to Youtube API and move to MovieTrailerActivity Page
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://api.themoviedb.org/3/movie/" + movie.getId()+ "/videos?api_key=566c61f7c87655018b6ff91b149a463c", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                trailerTxt.setAlpha(1);

                try{
                    final String ytKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");
                    moviePoster.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MovieDetailsActivity.this , MovieTrailerActivity.class);
                            i.putExtra("youtubekey", ytKey);
                            startActivity(i);
                        }
                    });
                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });



        // set the title, overview, and background color
        tvTitle.setText(movie.getTitle());
        tvTitle.setTextColor(txtCol);
        tvOverview.setText(movie.getOverview());
        tvOverview.setTextColor(txtCol);
        trailerTxt.setTextColor(txtCol);
        popularity.setText(String.format("Popularity: %f", movie.getPopularity()));
        popularity.setTextColor(txtCol);
        ratingTxt.setTextColor(txtCol);

        if (backgroundCol.equals("black")){
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.black));
        } else{
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }



        // set image to backdrop image
        String imageURL = movie.getBackdropPath();
        int placeholder = R.drawable.flicks_backdrop_placeholder;
        Glide.with(this).load(imageURL)
                                .placeholder(placeholder)
                                .fitCenter()
                                .into(moviePoster);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


    }
}