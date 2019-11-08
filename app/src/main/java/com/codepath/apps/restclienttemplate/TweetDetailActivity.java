package com.codepath.apps.restclienttemplate;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.List;

import okhttp3.Headers;

public class TweetDetailActivity extends AppCompatActivity {
    public static final String TAG = "TweetDetailActivity";

    ImageView ivProfileImage;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTimeAndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweet_details);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvTimeAndDate = findViewById(R.id.tvTimeAndDate);

        Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvTimeAndDate.setText(tweet.createdAt);
        Glide.with(this).load(tweet.user.profileImageURL).into(ivProfileImage);
        Log.i("Detail Tweet", "created");
    }
}
