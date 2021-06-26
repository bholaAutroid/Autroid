package autroid.business.adapter.cars;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.PackagesDiscountBE;


public class CarSalePackageInclusionAdapter extends RecyclerView.Adapter<CarSalePackageInclusionAdapter.PurchasedPackagesHolder> {


    public ArrayList<PackagesDiscountBE> mList;
    Context context;

    public CarSalePackageInclusionAdapter(Context context, ArrayList<PackagesDiscountBE> mList) {
        this.mList = mList;
        this.context = context;

    }

    @NonNull
    @Override
    public PurchasedPackagesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_purchased_packages_detail, viewGroup, false);

        return new PurchasedPackagesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedPackagesHolder bookingPackageseHolder, int i) {

        bookingPackageseHolder.mServiceName.setText(mList.get(i).getTitle());


        if(mList.get(i).getForDiscount().equalsIgnoreCase("specific")) {
            bookingPackageseHolder.mLLSpecific.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mServiceLimit.setText("" + mList.get(i).getLimit());
        }
        else {

            if(mList.get(i).getDiscount()>0) {
                bookingPackageseHolder.imgCheck.setVisibility(View.GONE);
                if (mList.get(i).getType().equalsIgnoreCase("percent")) {
                    bookingPackageseHolder.mServiceDiscount.setText("" + mList.get(i).getDiscount() + "%");
                } else {
                    bookingPackageseHolder.mServiceDiscount.setText("" + mList.get(i).getDiscount());
                }
            }
            else {
                bookingPackageseHolder.imgCheck.setVisibility(View.VISIBLE);
            }

            bookingPackageseHolder.mLLSpecific.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class PurchasedPackagesHolder extends RecyclerView.ViewHolder{

        TextView mServiceName,mServiceDiscount,mServiceLimit;
        LinearLayout mLLSpecific;
        ImageView imgCheck;

        public PurchasedPackagesHolder(View itemView) {
            super(itemView);
            mServiceName = itemView.findViewById(R.id.service_name);
            mServiceDiscount = itemView.findViewById(R.id.discount_type);
            mServiceLimit = itemView.findViewById(R.id.limit);
            mLLSpecific=itemView.findViewById(R.id.ll_specific);
            imgCheck=itemView.findViewById(R.id.img_check);



        }

    }
}
