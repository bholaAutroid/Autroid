package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.model.realm.MediaRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by pranav.mittal on 03/03/18.
 */

public class RealmGalleryAdapter extends RealmRecyclerViewAdapter<MediaRealm,RealmGalleryAdapter.GalleryHolder> {

    Context context;
    public RealmGalleryAdapter(@Nullable OrderedRealmCollection<MediaRealm> data, boolean autoUpdate) {
        super(data, autoUpdate);
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_gallery, parent, false);

        return new GalleryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        MediaRealm mediaRealm=getItem(position);
        Picasso.with(context).load(mediaRealm.getPath()).placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);
    }

    public  class GalleryHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;

        public GalleryHolder(View itemView) {
            super(itemView);

            ivImage= (ImageView) itemView.findViewById(R.id.img_gallery);

        }

    }

}
