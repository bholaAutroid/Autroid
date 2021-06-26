package autroid.business.adapter;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.bean.CarDetailBE;

/**
 * Created by pranav.mittal on 06/14/17.
 */

public class ProfileCarStockAdapter extends RecyclerView.Adapter<ProfileCarStockAdapter.ProfileCarStockHolder> {

    public ArrayList<CarDetailBE> mList;
    Context context;
    Boolean isFullWidth;
    OnClickCallBack mOnClickCallBack;

    public ProfileCarStockAdapter(Context context,ArrayList<CarDetailBE> mList,Boolean isFullWidth,OnClickCallBack mOnClickCallBack){
        this.context=context;
        this.mList=mList;
        this.isFullWidth=isFullWidth;
        this.mOnClickCallBack=mOnClickCallBack;
    }
    @Override
    public ProfileCarStockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_profile_car_stock, parent, false);

        return new ProfileCarStockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileCarStockHolder holder, int position) {

        holder.mCarName.setText(mList.get(position).getTitle());

        if(mList.get(position).getPrice()!=null)
        holder.mCarPrice.setText("₹ "+mList.get(position).getPrice());

        holder.mCarType.setText(" ● "+ mList.get(position).getFuel_type());

            if(mList.get(position).getManufacture_year()!=null)
            holder.mCarYear.setText(" ● "+ mList.get(position).getManufacture_year());

            if(mList.get(position).getDriven()!=null)
            holder.mCarKm.setText(" ● "+ mList.get(position).getDriven()+" KM");


        if(mList.get(position).getPublish().equals("0")){

        }

        if(mList.get(position).getThumbnails().size()>0)
            Picasso.with(context).load(mList.get(position).getThumbnails().get(0).getFile_address()).placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
        else
            Picasso.with(context).load(R.drawable.placeholder_big).fit().placeholder(R.drawable.placeholder_big).into(holder.mCarImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class ProfileCarStockHolder extends RecyclerView.ViewHolder{
        TextView mCarPrice,mCarName,mCarYear,mCarKm,mCarType;

        ImageView mCarImage;
        LinearLayout mMainLayout;

        public ProfileCarStockHolder(View itemView) {
            super(itemView);
            mCarPrice= (TextView) itemView.findViewById(R.id.car_price);
            mCarName= (TextView) itemView.findViewById(R.id.car_name);

            mCarYear= (TextView) itemView.findViewById(R.id.car_year);
            mCarKm= (TextView) itemView.findViewById(R.id.car_km);
            mCarType= (TextView) itemView.findViewById(R.id.car_type);
            mMainLayout=itemView.findViewById(R.id.main_layout);

            mCarImage= (ImageView) itemView.findViewById(R.id.car_image);


            if(isFullWidth){
                int width= Resources.getSystem().getDisplayMetrics().widthPixels;
                mMainLayout.getLayoutParams().width=width;
            }

            mMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickCallBack.onImageClick(mList.get(getLayoutPosition()).getId());
                }
            });

        }
    }
}
