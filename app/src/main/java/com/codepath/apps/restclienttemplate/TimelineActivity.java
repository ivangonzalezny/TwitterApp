package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout swipeContainer;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApp.getRestClient(this);

        swipeContainer = findViewById(R.id.swipeContainer);

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "setOnRefreshListener(): Fetching new data");
                populateHomeTimeline();
            }
        });

        // find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        // init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // recycler view setup: layout manager and the adapter
        //rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setLayoutManager(layoutManager);
        Log.i(TAG, "setLayoutManager");
        rvTweets.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore: " + page);
                loadMoreData();
            }
        };
        // adds the scroll listener to the recycle view
        rvTweets.addOnScrollListener(scrollListener);
        Log.i(TAG, "addOnScrollListener() called");

        populateHomeTimeline();
        Log.i(TAG, "populateHomeTimeline() called");

//        Intent i = new Intent(this, TweetDetailActivity.class);
//        startActivity(i);
    }

    // this is where we will make another API call to get the next page of tweets and add the objects to our current list of tweets
    private void loadMoreData() {
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            // 1. Send an API request to retrieve appropriate paginated data

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess on: loadMoreData(): " + json.toString());
                // 2. Deserialize and construct new model objects from the API response
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    // 3. Append the new data objects to the existing set of items inside the array of items
                    // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    Log.i(TAG, "onSuccess on load More Data(): " + json.toString());
                    adapter.addAll(tweets);
                } catch (JSONException e) {
                    Log.i(TAG, "JSON Exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure on: loadMoreData()", throwable);
            }
        }, tweets.get(tweets.size() - 1).id);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess: " + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    adapter.addAll(Tweet.fromJsonArray(jsonArray));
//                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
//                    adapter.notifyDataSetChanged();
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                    Log.i(TAG, "onSuccess: adapter called successfully");
                } catch (JSONException e) {
                    Log.i(TAG, "JSON Exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure: " + response, throwable);
            }
        });
    }
}
