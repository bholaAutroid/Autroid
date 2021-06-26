package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.realm.OffersRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class OfferAdapter extends RealmRecyclerViewAdapter<OffersRealm, OfferAdapter.OfferHolder> {

    Context context;

    OnClickCallBack mOnClickCallBack;


    public OfferAdapter(@Nullable OrderedRealmCollection<OffersRealm> data, boolean autoUpdate, OnClickCallBack mOfferClickCallback) {
        super(data, autoUpdate);
        this.mOnClickCallBack=mOfferClickCallback;
    }

    @Override
    public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_offers, parent, false);

        return new OfferHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferHolder holder, int position) {

        OffersRealm offersRealm=getItem(position);
        holder.offersRealm=offersRealm;
        holder.mOfferTitle.setText(offersRealm.getTitle());
        holder.mDate.setText("Validity "+offersRealm.getValidity());
        holder.mDescription.setText(offersRealm.getDescription());


        if(!offersRealm.isPublish()){
            holder.mUnpublish.setText("Publish");
        }
        else {
            holder.mUnpublish.setText("unPublish");
        }

        String imageURL=offersRealm.getOfferImg();
        if(imageURL!=null)
            if(imageURL.length()>0){
                Picasso.with(context).load(offersRealm.getOfferImg()).fit().placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
            }
    }



    class OfferHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mCarImage;
        TextView mOfferTitle,mDate,mDescription;
        LinearLayout llEdit;
        Button mUnpublish,mEdit,mShare;

        OffersRealm offersRealm;

        public OfferHolder(View itemView) {
            super(itemView);

            mCarImage= (ImageView) itemView.findViewById(R.id.offer_image);
            mOfferTitle= (TextView) itemView.findViewById(R.id.offer_title);
            mDate= (TextView) itemView.findViewById(R.id.validity_date);
            mDescription= (TextView) itemView.findViewById(R.id.offer_description);
            llEdit= (LinearLayout) itemView.findViewById(R.id.ll_edit);
            llEdit.setOnClickListener(this);
            mCarImage.setOnClickListener(this);
            mUnpublish= (Button) itemView.findViewById(R.id.offer_publish);
            mEdit= (Button) itemView.findViewById(R.id.offer_edit);
            mShare= (Button) itemView.findViewById(R.id.offer_share);

            mShare.setOnClickListener(this);
            mUnpublish.setOnClickListener(this);
            mEdit.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_edit:
                   // mOnClickCallBack.onEditButtonClick(getLayoutPosition());
                    break;
                case R.id.offer_image:
                    mOnClickCallBack.onImageClick(offersRealm.getOfferId());
                    break;
                case R.id.offer_publish:
                    if(offersRealm.isPublish()){
                        mUnpublish.setText(R.string.publish);
                    }
                    else {
                        mUnpublish.setText(R.string.unpublish);
                    }
                    mOnClickCallBack.onPublishUnPublishClick(offersRealm.getOfferId());
                    break;
                case R.id.offer_edit:
                    mOnClickCallBack.onEditButtonClick(offersRealm.getOfferId(),"");
                    break;
                case R.id.offer_share:
                    mOnClickCallBack.onShareButtonClick(offersRealm.getOfferId());
                    break;
            }
        }
    }
}
