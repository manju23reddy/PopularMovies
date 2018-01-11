package com.manju23reddy.popularmovies.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.manju23reddy.popularmovies.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MReddy3 on 1/6/2018.
 */

public class MovieTrailersAdapter  extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailersHolder>{

    private ArrayList<JSONObject> mMovieTrailers = null;

    public interface PlayTrailerCallBack {
        public void onPlayButtonClicked(int pos);
    }

    PlayTrailerCallBack mPlayBackListener;

    Context mContext;

    public class MovieTrailersHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView trailerTxtV;
        private ImageButton playTrailerImgBtn;

        public MovieTrailersHolder(View view){
            super(view);

            trailerTxtV = view.findViewById(R.id.txtv_trailer_number);
            playTrailerImgBtn = view.findViewById(R.id.imgbtn_play_button);
            playTrailerImgBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mPlayBackListener.onPlayButtonClicked(getAdapterPosition());
        }
    }

    public MovieTrailersAdapter(Context context, PlayTrailerCallBack listener){
        mContext = context;
        mPlayBackListener = listener;

        mMovieTrailers = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(MovieTrailersHolder holder, int position) {
        JSONObject movieTrailer = mMovieTrailers.get(position);
        int id = position+1;
        holder.trailerTxtV.setText(holder.trailerTxtV.getText().toString()+" "+id);
    }

    @Override
    public MovieTrailersHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int thumbnailLayout = R.layout.trailer_list_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatly = false;

        View view = inflater.inflate(thumbnailLayout, parent, shouldAttachToParentImmediatly);
        return new MovieTrailersHolder(view);
    }

    @Override
    public int getItemCount() {
        if (null != mMovieTrailers){
            return mMovieTrailers.size();
        }
        return 0;
    }

    public JSONObject getTrailerObject(int pos){
        if (mMovieTrailers != null){
            return mMovieTrailers.get(pos);
        }
        return null;
    }

    public void addTrailer(JSONObject trailer){
        mMovieTrailers.add(trailer);
        notifyDataSetChanged();
    }

    public void resetAdapter(){
        mMovieTrailers.clear();
    }


}
