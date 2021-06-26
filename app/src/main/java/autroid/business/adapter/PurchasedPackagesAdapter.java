package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.bean.PurchasedPackagesBE;


public class PurchasedPackagesAdapter extends RecyclerView.Adapter<PurchasedPackagesAdapter.PurchasedPackagesHolder> {


    public ArrayList<PurchasedPackagesBE> mList;
    Context context;

    LeadsCallback mLeadsCallback;


    public PurchasedPackagesAdapter(Context context, ArrayList<PurchasedPackagesBE> mList,LeadsCallback mLeadsCallback) {

        this.context = context;

        this.mLeadsCallback=mLeadsCallback;
        this.mList=mList;


    }

    @NonNull
    @Override
    public PurchasedPackagesHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_purchased_packages_list, viewGroup, false);

        return new PurchasedPackagesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasedPackagesHolder bookingPackageseHolder, int i) {

        bookingPackageseHolder.mServiceName.setText(mList.get(i).getName());
        bookingPackageseHolder.mPurchase.setText(Html.fromHtml("Purchased on <b>"+mList.get(i).getCreated_at()));


        bookingPackageseHolder.mCarName.setText(mList.get(i).getCar().getTitle());
        bookingPackageseHolder.mCarRegistration.setText(mList.get(i).getCar().getRegistration_no());

        bookingPackageseHolder.mUserName.setText(mList.get(i).getUser().getName());
        bookingPackageseHolder.mUserNumber.setText(mList.get(i).getUser().getContact_no()+" / "+mList.get(i).getUser().getEmail());

        if(mList.get(i).getPayment().getPaid_total()<=0) {
            bookingPackageseHolder.mPrice.setVisibility(View.GONE);
            bookingPackageseHolder.mPaidPrice.setVisibility(View.GONE);
        }
        else {
            bookingPackageseHolder.mPrice.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mPrice.setText(Html.fromHtml("Value <b>₹ " + Math.round(mList.get(i).getPayment().getTotal() )+ "</b>"));
            bookingPackageseHolder.mPaidPrice.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mPaidPrice.setText(Html.fromHtml("Amount Paid <b>₹ " + Math.round(mList.get(i).getPayment().getPaid_total()) + "</b>"));
        }



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class PurchasedPackagesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mServiceName,mServiceInfo,mExpire,mPurchase,mCarName,mCarRegistration,mPrice,mPaidPrice,mUserName,mUserNumber;

        LinearLayout mMainLayout;
        ImageButton btnChat,btnCall;
        ImageButton btnStatus;
        public PurchasedPackagesHolder(View itemView) {
            super(itemView);
            mServiceName = itemView.findViewById(R.id.package_name);
            mExpire = itemView.findViewById(R.id.package_expire);
            mPurchase=itemView.findViewById(R.id.package_purchase);
            mCarName=itemView.findViewById(R.id.car_name);
            mPrice=itemView.findViewById(R.id.price);
            mCarRegistration=itemView.findViewById(R.id.car_registration);
            mPaidPrice=itemView.findViewById(R.id.paid_price);
            mUserName=itemView.findViewById(R.id.user_name);
            mUserNumber=itemView.findViewById(R.id.user_number);
            btnChat=itemView.findViewById(R.id.chat_btn);
            btnChat.setOnClickListener(this);
            btnCall=itemView.findViewById(R.id.call_btn);
            btnCall.setOnClickListener(this);
            btnStatus=itemView.findViewById(R.id.update_status);
            btnStatus.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.update_status:
                    mLeadsCallback.onConvertLead(mList.get(getLayoutPosition()).getUser().getName(),mList.get(getLayoutPosition()).getUser().getEmail(),mList.get(getLayoutPosition()).getUser().getContact_no(),mList.get(getLayoutPosition()).getName());
                    break;
                case R.id.chat_btn:
                    mLeadsCallback.onChatClick(mList.get(getLayoutPosition()).getUser().getId());
                    break;
                case R.id.call_btn:
                    mLeadsCallback.onCallClick(mList.get(getLayoutPosition()).getUser().getContact_no());
                    break;
            }
        }
    }
}
