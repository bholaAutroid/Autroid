package autroid.business.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.RatingReviewBE;

/**
 * Created by pranav.mittal on 06/14/17.
 */

public class ProfileReviewRatingAdapter extends RecyclerView.Adapter<ProfileReviewRatingAdapter.ProfileReviewHolder> {

    public ArrayList<RatingReviewBE> mList;
    Context context;
    Boolean isFullWidth;

    public ProfileReviewRatingAdapter(Context context,ArrayList<RatingReviewBE> mList,Boolean isFullWidth){
        this.context=context;
        this.mList=mList;
        this.isFullWidth=isFullWidth;
    }

    @Override
    public ProfileReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_profile_review_rating, parent, false);

        return new ProfileReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileReviewHolder holder, int position) {

            if(mList.get(position).getUser()!=null) {
                holder.tvName.setText(mList.get(position).getUser().getName());
                Picasso.with(context).load(mList.get(position).getUser().getCover_address()).placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);
            }
            holder.tvComment.setText(mList.get(position).getReview());
            holder.rbRating.setRating(Float.valueOf(mList.get(position).getRating()));

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class ProfileReviewHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvComment;
        ImageView ivImage;
        RatingBar rbRating;
        LinearLayout mMainLayout;


        public ProfileReviewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.review_name_one);
            tvComment = (TextView) itemView.findViewById(R.id.review_comment_one);
            ivImage = (ImageView) itemView.findViewById(R.id.review_image_one);
            rbRating = (RatingBar) itemView.findViewById(R.id.review_rating_one);

            mMainLayout = itemView.findViewById(R.id.main_layout);

            if (isFullWidth) {
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                mMainLayout.getLayoutParams().width = width;
                tvComment.setMaxLines(10);
                //mMainLayout.setBackgroundResource(R.drawable.selector_transparent);

              /*  mMainLayout.setPadding(0,5,0,0);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(0, 10, 0, 0);
                mMainLayout.setLayoutParams(layoutParams);
            }*/
            }
        }
    }
}
