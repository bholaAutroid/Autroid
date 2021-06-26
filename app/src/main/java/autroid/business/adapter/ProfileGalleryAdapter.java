package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.GalleryImageClickCallback;
import autroid.business.model.bean.ThumbnailBE;

/**
 * Created by pranav.mittal on 06/14/17.
 */

public class ProfileGalleryAdapter extends RecyclerView.Adapter<ProfileGalleryAdapter.ProfileGalleryHolder> {

    ArrayList<ThumbnailBE> mList;
    Context context;
    GalleryImageClickCallback mGalleryImageClickCallback;

    public ProfileGalleryAdapter(Context context,ArrayList<ThumbnailBE> mList,GalleryImageClickCallback mGalleryImageClickCallback){
        this.context=context;
        this.mList=mList;
        this.mGalleryImageClickCallback=mGalleryImageClickCallback;
    }

    @Override
    public ProfileGalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_profile_gallery, parent, false);

        return new ProfileGalleryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileGalleryHolder holder, int position) {
        Picasso.with(context).load(mList.get(position).getFile_address()).fit().placeholder(R.drawable.placeholder_big).into(holder.ivImage);

        if(mList.size()>4){
            if(position==3){
                holder.mOverlay.setVisibility(View.VISIBLE);
                holder.mCount.setVisibility(View.VISIBLE);
                int remaining=mList.size()-4;
                holder.mCount.setText("+"+remaining);
            }
            else {
                holder.mCount.setVisibility(View.GONE);
                holder.mOverlay.setVisibility(View.GONE);
            }
        }
        else if(mList.size()==3){
            if(position==0) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);

            }
        }
        else if(mList.size()==1){
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            final float scale = context.getResources().getDisplayMetrics().density;
            int px = (int) (200 * scale + 0.5f);
            holder.mLayout.getLayoutParams().height=px;
        }
    }

    @Override
    public int getItemCount() {

        int size=mList.size();
        if(size>4)
            return 4;
        else
            return size;
    }

    public  class ProfileGalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivImage;
        View mOverlay;
        TextView mCount;
        RelativeLayout mLayout;
        public ProfileGalleryHolder(View itemView) {
            super(itemView);

            ivImage= (ImageView) itemView.findViewById(R.id.row_selected_image);
            mOverlay=itemView.findViewById(R.id.image_overlay);
            mCount=itemView.findViewById(R.id.txt_count);
            mLayout=itemView.findViewById(R.id.main_layout);
            ivImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.row_selected_image:
                    mGalleryImageClickCallback.onGalleryClick(mList);
                    break;
            }
        }
    }
}
