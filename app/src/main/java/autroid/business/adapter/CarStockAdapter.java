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
import autroid.business.interfaces.PurchaseCarCallback;
import autroid.business.model.realm.CarStockRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by pranav.mittal on 06/10/17.
 */

public class CarStockAdapter extends RealmRecyclerViewAdapter<CarStockRealm, CarStockAdapter.CarStockHolder> {


    Context context;
    private  PurchaseCarCallback mOnClickCallBack;

    public CarStockAdapter(@Nullable OrderedRealmCollection<CarStockRealm> data, boolean autoUpdate, PurchaseCarCallback mOfferClickCallback) {
        super(data, autoUpdate);
        this.mOnClickCallBack=mOfferClickCallback;
    }

    @Override
    public CarStockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_car_stock, parent, false);

        return new CarStockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarStockHolder holder, int position) {
        CarStockRealm carStockRealm=getItem(position);
        holder.carStockRealm=carStockRealm;

        holder.mCarName.setText(carStockRealm.getTitle());
        holder.mCarPrice.setText("₹ "+carStockRealm.getPrice());

        holder.mCarYear.setText(""+carStockRealm.getYear()+" | "+carStockRealm.getOdometer()+" km"+" | "+carStockRealm.getFuelType());



        if(carStockRealm.isPartner()){
            holder.mPartnerImage.setVisibility(View.VISIBLE);
        }
        else
            holder.mPartnerImage.setVisibility(View.GONE);


      /*  if(carStockRealm.isBookmarked()){
            holder.mCarFav.setColorFilter(ContextCompat.getColor(context, R.color.red_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        else {
            holder.mCarFav.setColorFilter(ContextCompat.getColor(context, R.color.gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
        }
*/


        if(carStockRealm.getFeatureImg()!=null) {
            Picasso.with(context).load(carStockRealm.getFeatureImg()).placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
        }else {
            Picasso.with(context).load(R.drawable.placeholder_big).fit().placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
        }

        if(carStockRealm.isMembership()){
            holder.mCarEagerMembership.setVisibility(View.VISIBLE);
        }
        else {
            holder.mCarEagerMembership.setVisibility(View.GONE);
        }

        if(carStockRealm.getUserType().equalsIgnoreCase("business")){
            /*if(carStockRealm.getChat()){
                holder.btnChat.setBackgroundResource(R.drawable.selector_gray);

            }
            else {
                holder.btnChat.setBackgroundResource(R.drawable.selector_transparent);
            }*/


            holder.btnSeller.setText("Dealer");
        }
        else{
            holder.btnSeller.setText("Seller");
            //holder.btnChat.setEnabled(true);
            }

    }


    public  class CarStockHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mCarPrice,mCarName,mCarYear;
        ImageView mCarImage,mPartnerImage,mCarEagerMembership;
        LinearLayout mMainLayout;
        LinearLayout mCarShare;
        CarStockRealm carStockRealm;
        Button btnSeller,btnChat;

        public CarStockHolder(View itemView) {
            super(itemView);

            mPartnerImage=itemView.findViewById(R.id.img_partner);
            mCarEagerMembership=itemView.findViewById(R.id.ic_membership);
            mCarPrice= (TextView) itemView.findViewById(R.id.car_price);
            mCarName= (TextView) itemView.findViewById(R.id.car_name);
            mCarYear= (TextView) itemView.findViewById(R.id.car_year);
            btnSeller=itemView.findViewById(R.id.btn_seller);
            btnChat=itemView.findViewById(R.id.chat);
            mMainLayout=itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(this);


            mCarImage= (ImageView) itemView.findViewById(R.id.car_image);


            mCarShare=itemView.findViewById(R.id.share_car);
            mCarShare.setOnClickListener(this);

            btnSeller.setOnClickListener(this);
            btnChat.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_layout:
                    try {
                        mOnClickCallBack.onTitleClick(getLayoutPosition(),carStockRealm.getCreatedDate(),carStockRealm.getId());
                    }catch (IllegalStateException e){

                    }catch (Exception e){

                    }
                    break;
                case R.id.share_car:
                    /*if(carStockRealm.getMedia().size()>0) {
                        mOnClickCallBack.onShareButtonClick(carStockRealm.getTitle(),carStockRealm.getId(),carStockRealm.getMedia().get(0).getPath());
                    }else {
                    }*/

                    mOnClickCallBack.onShareButtonClick("Hey, I found "+carStockRealm.getTitle()+" | "+carStockRealm.getYear()+" year | "+carStockRealm.getOdometer()+" KM driven | "+carStockRealm.getFuelType(),carStockRealm.getId(),null);

                    break;
                case R.id.btn_seller:
                    try {
                        mOnClickCallBack.onPublisherClick(carStockRealm.getId(),carStockRealm.getPublisherId(),carStockRealm.getUserType());

                    }catch (IllegalStateException e){

                    }catch (Exception e){

                    }
                    break;
                case R.id.chat:
                    try {
                        mOnClickCallBack.onChatClick(carStockRealm.getPublisherId());

                    }catch (IllegalStateException e){

                    }catch (Exception e){

                    }
                    break;
            }
        }
    }
}
