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
import autroid.business.model.bean.ProductDetailBE;

/**
 * Created by pranav.mittal on 06/14/17.
 */

public class ProfileProductStockAdapter extends RecyclerView.Adapter<ProfileProductStockAdapter.ProfileProductStockHolder> {

    Context context;
    public ArrayList<ProductDetailBE> mList;
    Boolean isFullWidth;
    public ProfileProductStockAdapter(Context context,ArrayList<ProductDetailBE> mList,Boolean isFullWidth){
        this.context=context;
        this.mList=mList;
        this.isFullWidth=isFullWidth;
    }
    @Override
    public ProfileProductStockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_profile_product_stock, parent, false);

        return new ProfileProductStockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProfileProductStockHolder holder, int position) {
        holder.mProductName.setText(mList.get(position).getTitle());
        holder.mProductPrice.setText("₹ "+mList.get(position).getPrice());
//        holder.mProductCode.setText(mList.get(position).getCategory().getCategory());


        if(mList.get(position).getThumbnails().size()>0)
            Picasso.with(context).load(mList.get(position).getThumbnails().get(0).getFile_address()).placeholder(R.drawable.placeholder_big).into(holder.mProductImage);
        else
            Picasso.with(context).load(R.drawable.placeholder_big).fit().placeholder(R.drawable.placeholder_big).into(holder.mProductImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class ProfileProductStockHolder extends RecyclerView.ViewHolder{
        TextView mProductPrice,mProductName,mProductCode;
        LinearLayout mMainLayout;
        ImageView mProductImage;

        public ProfileProductStockHolder(View itemView) {
            super(itemView);
            mProductPrice= (TextView) itemView.findViewById(R.id.product_price);
            mProductName= (TextView) itemView.findViewById(R.id.product_name);

            mProductCode= (TextView) itemView.findViewById(R.id.product_code);
            mMainLayout=itemView.findViewById(R.id.main_layout);


            mProductImage= (ImageView) itemView.findViewById(R.id.product_image);

            if(isFullWidth){
                int width= Resources.getSystem().getDisplayMetrics().widthPixels;
                mMainLayout.getLayoutParams().width=width;
            }

        }
    }
}
