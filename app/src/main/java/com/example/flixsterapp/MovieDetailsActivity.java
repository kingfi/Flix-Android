package com.example.flixsterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixsterapp.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView tvTitle;
    TextView tvOverview;
    TextView popularity;
    TextView ratingTxt;
    RatingBar rbVoteAverage;
    ImageView moviePoster;
    ConstraintLayout constraintLayout;

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

        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // get colors
        int backgroundCol = getIntent().getIntExtra("backgroundCol", 0);
        int txtCol = getIntent().getIntExtra("txtColor", 0);

        // set the title, overview, and background color
        tvTitle.setText(movie.getTitle());
        tvTitle.setTextColor(txtCol);
        tvOverview.setText(movie.getOverview());
        tvOverview.setTextColor(txtCol);
        popularity.setText(String.format("Popularity: %f", movie.getPopularity()));
        popularity.setTextColor(txtCol);
        ratingTxt.setTextColor(txtCol);
        constraintLayout.setBackgroundColor(getResources().getColor(backgroundCol));


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