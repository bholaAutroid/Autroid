package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.esafirm.imagepicker.model.Image;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnImageDeleteCallback;

/**
 * Created by pranav.mittal on 06/17/17.
 */

public class SelectMultiImageAdapter extends RecyclerView.Adapter<SelectMultiImageAdapter.SelectMultiImageHolder> {

    public ArrayList<Image> images;
    Context context;
    OnImageDeleteCallback mOnImageDeleteCallback;

    public SelectMultiImageAdapter(Context context, ArrayList<Image> images, OnImageDeleteCallback mOnImageDeleteCallback){
        this.images=images;
        this.context=context;
        this.mOnImageDeleteCallback=mOnImageDeleteCallback;
    }
    @Override
    public SelectMultiImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_select_images, parent, false);

        return new SelectMultiImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectMultiImageHolder holder, int position) {
        Picasso.with(context).load(new File(images.get(position).getPath())).placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class SelectMultiImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView ivImage;
        LinearLayout llDelete;

        public SelectMultiImageHolder(View itemView) {
            super(itemView);

            ivImage= (ImageView) itemView.findViewById(R.id.row_selected_image);
            llDelete= (LinearLayout) itemView.findViewById(R.id.ll_img_delete);
            llDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_img_delete:
                   // mOnImageDeleteCallback.onDeleteClick(getLayoutPosition()+"");
                    break;
            }
        }
    }
}
