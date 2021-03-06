package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        Log.i("Tweets Adapter: ", "constructor called");
    }

    // for each row inflate a layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        Log.i("onCreateViewHolder: ", "TweetsAdapter: called");
        return new ViewHolder(view);
    }

    // bind values based on the position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the data
        Tweet tweet = tweets.get(position);
        // find the tweet with the viewholder
        holder.bind(tweet);
        Log.i("onBindViewHolder: ","TweetsAdapter: called");
    }

    // pass in the context and list of tweets
    @Override
    public int getItemCount() {
        Log.i("Tweets Adapter: ", "getItemCount() called");
        return tweets.size();
    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
        Log.i("Tweets Adapter: ", "clear() called");
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> tweetList) {
        tweets.addAll(tweetList);
        notifyDataSetChanged();
        Log.i("Tweets Adapter: ", "addAll() called");
    }

    // define a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            container = itemView.findViewById(R.id.container);
            Log.i("ViewHolder: ", "TweetsAdapter: called");
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.profileImageURL).into(ivProfileImage);
            Log.i("bind:", "Tweets Adapter: called");


            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "Single Tweet", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, TweetDetailActivity.class);

                    i.putExtra("tweet", Parcels.wrap(tweet));
                    context.startActivity(i);
                }
            });
        }
    }
}
