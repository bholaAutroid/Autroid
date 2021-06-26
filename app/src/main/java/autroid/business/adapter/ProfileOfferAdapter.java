package autroid.business.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.bean.OfferBE;
import autroid.business.utils.Utility;

/**
 * Created by pranav.mittal on 11/25/17.
 */

public class ProfileOfferAdapter extends RecyclerView.Adapter<ProfileOfferAdapter.OfferHolder> {

    Context context;
    public ArrayList<OfferBE> mList;
    OnClickCallBack mOnClickCallBack;
    Activity activity;
    Boolean isFullWidth;

    public ProfileOfferAdapter(Context context, ArrayList<OfferBE> mList,Boolean isFullWidth){
        this.context=context;
        this.mList=mList;
        this.mOnClickCallBack=mOnClickCallBack;
        this.activity=activity;
        this.isFullWidth=isFullWidth;
    }


    @Override
    public OfferHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_profile_offers, parent, false);

        return new OfferHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OfferHolder holder, int position) {

        holder.mOfferTitle.setText(mList.get(position).getOffer());
        holder.mOfferDescription.setText(mList.get(position).getDescription());
       // holder.mDate.setText("Expired on: "+mList.get(position).getValid_till());

        String imageURL=mList.get(position).getFile_address();
        if(imageURL!=null)
            if(imageURL.length()>0){
                //String cloudUrl = ApiURL.CLOUD_BASE_URL + Utility.getWindowWidth(activity)+"x"+Utility.dpToPx(Math.round(holder.height),context) + "/none/" + imageURL;
                Picasso.with(context).load(imageURL).fit().placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
            }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class OfferHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView mCarImage;
        TextView mOfferTitle,mDate,mOfferDescription;
        LinearLayout llEdit,mMainLayout;
        float width,height;

        public OfferHolder(View itemView) {
            super(itemView);

            mCarImage= (ImageView) itemView.findViewById(R.id.offer_image);
            mOfferTitle= (TextView) itemView.findViewById(R.id.offer_title);
            mOfferDescription= (TextView) itemView.findViewById(R.id.offer_description);
            mDate= (TextView) itemView.findViewById(R.id.validity_date);
            llEdit= (LinearLayout) itemView.findViewById(R.id.ll_edit);
            llEdit.setOnClickListener(this);
            mMainLayout=itemView.findViewById(R.id.main_layout);

            if(isFullWidth){
                int width= Resources.getSystem().getDisplayMetrics().widthPixels;
                mMainLayout.getLayoutParams().width=width;
            }


            mCarImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    //now we can retrieve the width and height
                    width = Utility.convertPixelsToDp(mCarImage.getWidth(),context);
                    height = Utility.convertPixelsToDp(mCarImage.getHeight(), context);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN)
                        mCarImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else
                        mCarImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);              }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_edit:
                   // mOnClickCallBack.onEditButtonClick(getLayoutPosition());
                    break;
            }
        }
    }
}
