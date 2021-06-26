package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import autroid.business.R;
import autroid.business.camera.RecyclerViewListener;
import autroid.business.model.realm.MediaRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by pranav.mittal on 06/18/17.
 */

public class EditCarGalleryAdapter extends /*RecyclerView.Adapter<EditCarGalleryAdapter.MyViewHolder>*/RealmRecyclerViewAdapter<MediaRealm, RecyclerView.ViewHolder> {

    Context context;
    RecyclerViewListener recyclerViewListener;

    public EditCarGalleryAdapter(@Nullable OrderedRealmCollection<MediaRealm> data, boolean autoUpdate, RecyclerViewListener recyclerViewListener) {
       super(data,autoUpdate);
        this.recyclerViewListener=recyclerViewListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            delete=itemView.findViewById(R.id.delete);
            delete.setVisibility(View.VISIBLE);
            image.setOnClickListener(v-> recyclerViewListener.onItemImage(getItem(getAdapterPosition()).getPath(),getAdapterPosition()));
            delete.setOnClickListener(v->recyclerViewListener.onImageDelete(getItem(getAdapterPosition()).getId(),getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_image, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        MyViewHolder myViewHolder=(MyViewHolder) viewHolder;

        MediaRealm mediaRealm=getItem(position);

//        Nirmana.getInstance().get()
//                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.placeholder_big))
//                .load(mediaRealm.getPath())
//                .into(myViewHolder.image);

    }

    @Override
    public int getItemCount() {
        return getData().size();
    }
}


//
//    ArrayList<ThumbnailBE> arrayList=null;
//    RecyclerViewListener recyclerViewListener=null;
//
//    public EditCarGalleryAdapter(ArrayList<ThumbnailBE> arrayList, RecyclerViewListener recyclerViewListener){
//        this.arrayList=arrayList;
//        this.recyclerViewListener=recyclerViewListener;
//    }
//


//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM) {
//            //Inflating recycle view item layout
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_edit_gallery, parent, false);
//            return new ProfileGalleryHolder(itemView);
//        }
//        else if (viewType == TYPE_FOOTER) {
//            //Inflating footer view
//            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_footer_add_image, parent, false);
//            return new FooterHolder(itemView);
//        } else return null;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        //More to come
//        //Log.d("Position",getItemCount()+"");
//        if (position ==getData().size()) {
//            return TYPE_FOOTER;
//        }
//        return TYPE_ITEM;
//    }
//
//    @Override
//    public int getItemCount() {
//        return getData().size()+1;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
////       if (holder instanceof FooterHolder) {
////           FooterHolder footerHolder = (FooterHolder) holder;
////
////        } else if (holder instanceof ProfileGalleryHolder) {
////           ProfileGalleryHolder itemViewHolder = (ProfileGalleryHolder) holder;
////           MediaRealm mediaRealm=getItem(position);
////           itemViewHolder.mediaRealm=mediaRealm;
////           Picasso.with(context).load(mediaRealm.getPath()).placeholder(R.drawable.placeholder_thumbnail).into(((ProfileGalleryHolder) holder).ivImage);
////        }
//    }

//    public  class ProfileGalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        ImageView ivImage;
//        CardView llDelete;
//        MediaRealm mediaRealm;
//
//        public ProfileGalleryHolder(View itemView) {
//            super(itemView);
//
//            ivImage= (ImageView) itemView.findViewById(R.id.img_gallery);
//            llDelete= (CardView) itemView.findViewById(R.id.ll_img_delete);
//            llDelete.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.ll_img_delete:
//                    mImageDeleteCallback.onDeleteImageClickId(mediaRealm.getId());
//                    break;
//            }
//        }
//    }
//
//    public  class FooterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        RelativeLayout mAddImage;
//
//        public FooterHolder(View itemView) {
//            super(itemView);
//
//            mAddImage=itemView.findViewById(R.id.ll_img_add);
//            mAddImage.setOnClickListener(this);
//
//
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case R.id.ll_img_add:
//                   mImageDeleteCallback.onAddImageClick(getLayoutPosition());
//                    break;
//            }
//        }
//    }


