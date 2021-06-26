package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.ThumbnailBE;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryHolder> {

    private final Context mContext;

    ArrayList<ThumbnailBE> mImages;
    OnRealmImageClickCallback onRealmImageClickCallback;

    public GalleryAdapter(Context c, ArrayList<ThumbnailBE> mImages,OnRealmImageClickCallback onRealmImageClickCallback) {

        this.mImages = mImages;
        mContext = c;
        this.onRealmImageClickCallback=onRealmImageClickCallback;

    }


    @NonNull
    @Override
    public GalleryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(mContext).
                inflate(R.layout.row_gallery, parent, false);

        return new GalleryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHolder holder, int position) {

        String imageURL=mImages.get(position).getFile_address();
        if(imageURL!=null)
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.placeholder_big).into(holder.ivImage);

        holder.mCount.setText(position+1+"/"+mImages.size());

    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public  class GalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivImage;
        TextView mCount;


        public GalleryHolder(View itemView) {
            super(itemView);

            ivImage= (ImageView) itemView.findViewById(R.id.img_gallery);

            ivImage.setOnClickListener(this);
            mCount=itemView.findViewById(R.id.img_count);

        }

        @Override
        public void onClick(View view) {
            onRealmImageClickCallback.onImageClick(0,mImages.get(getLayoutPosition()).getFile_address());
        }
    }

}
