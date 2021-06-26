package autroid.business.adapter;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.bean.BookingServicesBE;


public class BookingPackagesAdapter extends RecyclerView.Adapter<BookingPackagesAdapter.BookingPackageseHolder> {


    public ArrayList<BookingServicesBE> mList;
    Context context;
    OnClickBusinessCallback mOnClickBusinessCallback;

    public BookingPackagesAdapter(Context context, ArrayList<BookingServicesBE> mList,OnClickBusinessCallback mOnClickBusinessCallback) {
        this.mList = mList;
        this.context = context;
        this.mOnClickBusinessCallback=mOnClickBusinessCallback;
    }

    @NonNull
    @Override
    public BookingPackageseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_service, viewGroup, false);

        return new BookingPackageseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingPackageseHolder bookingPackageseHolder, int i) {

        bookingPackageseHolder.mServiceName.setText(mList.get(i).getService());

        if(mList.get(i).getDescription()!=null){
            if(mList.get(i).getDescription().length()>0){
                bookingPackageseHolder.mServiceDetail.setVisibility(View.VISIBLE);
               // bookingPackageseHolder.mServiceDetail.setText(mList.get(i).getDescription());
            }
            else {
                bookingPackageseHolder.mServiceDetail.setVisibility(View.GONE);
            }
        }

        if(mList.get(i).getDoorstep()!=null)
        if(mList.get(i).getDoorstep()){
            bookingPackageseHolder.mDoorStepService.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mDoorStepService.setText(Html.fromHtml("<b>(Doorstep Available)</b>"));
        }
        else {
            bookingPackageseHolder.mDoorStepService.setVisibility(View.GONE);
        }

        if(mList.get(i).getCost()>0){
            bookingPackageseHolder.llPrice.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mServicePrice.setText("₹ "+mList.get(i).getCost());

            if(mList.get(i).getMrp()>0) {
                bookingPackageseHolder.mServiceCost.setText("₹ " + mList.get(i).getMrp());
                bookingPackageseHolder.mServiceCost.setPaintFlags(bookingPackageseHolder.mServiceCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }

           bookingPackageseHolder.mServiceLabour.setText("Labour: ₹"+mList.get(i).getLabour_cost());
            bookingPackageseHolder.mServicePart.setText("Part: ₹"+mList.get(i).getPart_cost());
        }
        else {
            bookingPackageseHolder.llPrice.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class BookingPackageseHolder extends RecyclerView.ViewHolder{

        TextView mServiceName,mServiceDetail,mServicePrice,mServiceCost,mDoorStepService,mServiceLabour,mServicePart;
        RelativeLayout mMainLayout,llPrice;
        public BookingPackageseHolder(View itemView) {
            super(itemView);
            mServiceName=itemView.findViewById(R.id.service_name);
            mServiceDetail=itemView.findViewById(R.id.service_detail);
            mServicePrice=itemView.findViewById(R.id.service_price);
            mServiceCost=itemView.findViewById(R.id.service_cost);

            mServiceLabour=itemView.findViewById(R.id.labour_price);
            mServicePart=itemView.findViewById(R.id.part_cost);

            mDoorStepService=itemView.findViewById(R.id.doorstep);
            llPrice=itemView.findViewById(R.id.llservice);
            mMainLayout=itemView.findViewById(R.id.main_layout);


            mServiceDetail.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mOnClickBusinessCallback.onBusinessClick(mList.get(getLayoutPosition()).getDescription());
                       //onRealmImageClickCallback.onDetailClick(getLayoutPosition(),mList.get(getLayoutPosition()).getDescription());
                   }
               });



            /*mService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mList.get(getLayoutPosition()).getChecked()){
                        mService.setChecked(false);
                        mList.get(getLayoutPosition()).setChecked(false);
                    }
                    else {
                        mService.setChecked(true);
                        mList.get(getLayoutPosition()).setChecked(true);
                    }
                }
            });
*/

        }

    }
}
