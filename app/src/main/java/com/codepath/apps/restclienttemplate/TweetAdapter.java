package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    private TwitterClient mClient;


    // pass in the tweets array into constructor
    public TweetAdapter(List<Tweet> tweets, TwitterClient client){
        mTweets = tweets;
        mClient = client;

    }


// recycler view adapter stuff
    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
//    public void addAll(List<Tweet> list) {
//        mTweets.addAll(list);
//        notifyDataSetChanged();
//    }

    // for each row, inflate the layout and cache references into ViewHolder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent,false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;

    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);

        // populate views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTime.setText(tweet.relativeTime);

        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);

        holder.tvLikes.setText(Integer.toString(tweet.numberLikes));
        holder.tvRetweets.setText(Integer.toString(tweet.numberRetweets));

        if (tweet.retweeted) {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
        } else {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
        }

        if (tweet.liked) {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_heart);
        } else {
            holder.ibRetweet.setImageResource(R.drawable.ic_vector_heart_stroke);
        }


        holder.ibLiked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.liked) {
                    mClient.unlikeTweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibLiked.setImageResource(R.drawable.ic_vector_heart_stroke);

                    tweet.numberLikes--;
                    holder.tvLikes.setText(Integer.toString(tweet.numberLikes));
                    tweet.liked = false;
                } else {
                    mClient.likeTweet(tweet.uid, new JsonHttpResponseHandler());
                    tweet.numberLikes++;

                    holder.ibLiked.setImageResource(R.drawable.ic_vector_heart);
                    holder.tvLikes.setText(Integer.toString(tweet.numberLikes));
                    tweet.liked = true;

                }
            }
        });


        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweet.retweeted) {
                    mClient.unRetweet(tweet.uid, new JsonHttpResponseHandler());
                    holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);

                    tweet.numberRetweets--;
                    holder.tvRetweets.setText(Integer.toString(tweet.numberRetweets));

                    tweet.retweeted = false;
                } else {
                    mClient.retweet(tweet.uid, new JsonHttpResponseHandler());
                    tweet.numberRetweets++;

                    holder.ibRetweet.setImageResource(R.drawable.ic_vector_retweet);
                    holder.tvRetweets.setText(Integer.toString(tweet.numberRetweets));

                    tweet.retweeted = true;
                }
            }
        });

    }


    // check if tweet liked, client unliked tweet
    // then change boolean tweet.liked and change count

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTime;
        public ImageButton ibLiked;
        public ImageButton ibRetweet;
        public ImageButton ibReply;
        public TextView tvLikes;
        public TextView tvRetweets;


        // buttons text view for count

        public ViewHolder(View itemView) {
            super (itemView);

            // perform findViewById lookups

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
            ibLiked = (ImageButton) itemView.findViewById(R.id.ibLiked);
            tvRetweets = (TextView) itemView.findViewById(R.id.tvRetweets);
            ibRetweet = (ImageButton) itemView.findViewById(R.id.ibRetweet);
            ibReply = (ImageButton) itemView.findViewById(R.id.ibReply);
        }
    }
}
