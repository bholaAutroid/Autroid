package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.realm.MediaRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by pranav.mittal on 09/12/17.
 */

public class ShowroomGalleryAdapter extends RealmRecyclerViewAdapter<MediaRealm, ShowroomGalleryAdapter.FeedsImagesHolder> {

    Context context;
    RealmList<MediaRealm> mediaRealms;

    String id;
    OnClickBusinessCallback onRealmImageClickCallback;

    public ShowroomGalleryAdapter(@Nullable OrderedRealmCollection<MediaRealm> data, boolean autoUpdate, String id, OnClickBusinessCallback onRealmImageClickCallback) {
        super(data, autoUpdate);
        this.onRealmImageClickCallback=onRealmImageClickCallback;
        this.id=id;
    }

    @Override
    public int getItemCount() {
        int size=getData().size();
        if(size>4)
            return 4;
        else
            return size;

    }

    @Override
    public FeedsImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_showroom_images, parent, false);
        FeedsImagesHolder holder = new FeedsImagesHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(FeedsImagesHolder holder, int position) {
        MediaRealm mediaRealm=getItem(position);
        Picasso.with(context).load(mediaRealm.getPath()).placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);

        if(getData().size()>4){
            if(position==3){
                holder.mOverlay.setVisibility(View.VISIBLE);
                holder.mCount.setVisibility(View.VISIBLE);
                int remaining=getData().size()-4;
                holder.mCount.setText("+"+remaining);
            }
            else {
                holder.mCount.setVisibility(View.GONE);
                holder.mOverlay.setVisibility(View.GONE);
            }
        }
        else if(getData().size()==3){
            if(position==0) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                layoutParams.setFullSpan(true);

            }
        }
        else if(getData().size()==1){
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setFullSpan(true);
            final float scale = context.getResources().getDisplayMetrics().density;
            int px = (int) (200 * scale + 0.5f);
            holder.mLayout.getLayoutParams().height=px;
        }
    }

    public  class FeedsImagesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivImage;
        View mOverlay;
        TextView mCount;
        RelativeLayout mLayout;
        public FeedsImagesHolder(View itemView) {
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
                    onRealmImageClickCallback.onBusinessClick(id);
                    break;
            }
        }
    }
}
