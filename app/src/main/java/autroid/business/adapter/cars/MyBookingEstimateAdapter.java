package autroid.business.adapter.cars;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.booking.BookingServiceDetailAdapter;
import autroid.business.model.bean.ServiceBE;


public class MyBookingEstimateAdapter extends RecyclerView.Adapter<MyBookingEstimateAdapter.BookingReviewHolder> {

    Context context;
    public ArrayList<ServiceBE> arrayList;
    public Boolean isView;

    public MyBookingEstimateAdapter(ArrayList<ServiceBE> arrayList, Context context,Boolean isView){
        this.arrayList=arrayList;
        this.context=context;
        this.isView=isView;
    }

    @Override
    public BookingReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_my_booking_services, parent, false);

        return new BookingReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingReviewHolder holder, final int position) {

        holder.mName.setText(arrayList.get(position).getService());
        holder.mCost.setText("₹ "+arrayList.get(position).getCost());

        if(arrayList.get(position).getParts().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailPart = new BookingServiceDetailAdapter(arrayList.get(position).getParts(),false);
            holder.mPartsList.setAdapter(bookingServiceDetailPart);
        }else {
            holder.mPartsLayout.setVisibility(View.GONE);
        }

        if(arrayList.get(position).getLabour().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailLabour=new BookingServiceDetailAdapter(arrayList.get(position).getLabour(),false);
            holder.mLabourList.setAdapter(bookingServiceDetailLabour);
        }else {
            holder.mLabourLayout.setVisibility(View.GONE);
        }
        if(arrayList.get(position).getOpening_fitting().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailFitting=new BookingServiceDetailAdapter(arrayList.get(position).getOpening_fitting(),false);
            holder.mFittingList.setAdapter(bookingServiceDetailFitting);
        }else {
            holder.mFittingLayout.setVisibility(View.GONE);
        }

        if(arrayList.get(position).isClaim() && !arrayList.get(position).isSurveyor_approval()){
            holder.mServiceCheck.setEnabled(false);
            holder.mSurveyorApproval.setVisibility(View.VISIBLE);
        }
        else {
            holder.mServiceCheck.setEnabled(true);
            holder.mSurveyorApproval.setVisibility(View.GONE);
        }

       /* BookingDetailRealm categoryRealm = getItem(position);

        if(categoryRealm.getQuantity()>1) {
            holder.mName.setText(Html.fromHtml(categoryRealm.getPackageName()+" <B><big>x "+categoryRealm.getQuantity()+"</big></B>"));
        }
        else
        {
            holder.mName.setText(categoryRealm.getPackageName());
        }

       // holder.mName.setText(categoryRealm.getPackageName());

        if(categoryRealm.getType()!=null){
            if(categoryRealm.getType().equalsIgnoreCase("Custom")){

                    holder.mCost.setText("Labour ₹ " + categoryRealm.getLabour_cost()+"\nParts ₹ "+categoryRealm.getPart_cost()+"\n(Advanced Payment)");
                    }
            else {

                    holder.mCost.setText("₹ " + categoryRealm.getCost());

            }
        }

        if (categoryRealm.getDetails() != null) {
            holder.mDescription.setVisibility(View.VISIBLE);
            holder.mDescription.setText(categoryRealm.getDetails());
        } else {
            holder.mDescription.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class BookingReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName, mCost, mDescription;

        //surveyor_approval

        @BindView(R.id.check_service)
        AppCompatCheckBox mServiceCheck;

        @BindView(R.id.parts_list)
        RecyclerView mPartsList;
        @BindView(R.id.labour_list)
        RecyclerView mLabourList;
        @BindView(R.id.fitting_list)
        RecyclerView mFittingList;

        @BindView(R.id.surveyor_approval)
        TextView mSurveyorApproval;

        @BindView(R.id.ll_parts)
        LinearLayout mPartsLayout;
        @BindView(R.id.ll_labour)
        LinearLayout mLabourLayout;
        @BindView(R.id.ll_fitting)
        LinearLayout mFittingLayout;
        @BindView(R.id.ic_dropdown)
        ImageView mImgDropdown;

        @BindView(R.id.layout)
        RelativeLayout mMainLayout;
        @BindView(R.id.layout_details)
        LinearLayout mLayoutDetails;

        public BookingReviewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            mName = itemView.findViewById(R.id.service_name);
            mCost = itemView.findViewById(R.id.service_price);
            mDescription = itemView.findViewById(R.id.service_detail);

            mPartsList.setLayoutManager(new LinearLayoutManager(context));
            mLabourList.setLayoutManager(new LinearLayoutManager(context));
            mFittingList.setLayoutManager(new LinearLayoutManager(context));

            mMainLayout.setOnClickListener(this);

            if(isView){
                mServiceCheck.setVisibility(View.GONE);
            }
            else {
                mServiceCheck.setVisibility(View.VISIBLE);

            }

            mServiceCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!arrayList.get(getLayoutPosition()).isSelected()) {
                        arrayList.get(getLayoutPosition()).setSelected(true);
                        arrayList.get(getLayoutPosition()).setCustomer_approval(true);
                    }
                    else {
                        arrayList.get(getLayoutPosition()).setSelected(false);
                        arrayList.get(getLayoutPosition()).setCustomer_approval(false);

                    }

                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.layout:
                    if(!arrayList.get(getLayoutPosition()).isExpanded()) {
                        mLayoutDetails.setVisibility(View.VISIBLE);
                        arrayList.get(getLayoutPosition()).setExpanded(true);
                        mImgDropdown.setBackgroundResource(R.drawable.ic_arrow_drop_down);
                    }
                    else {
                        mLayoutDetails.setVisibility(View.GONE);
                        arrayList.get(getLayoutPosition()).setExpanded(false);
                        mImgDropdown.setBackgroundResource(R.drawable.ic_arrow_drop_down);

                    }
                    break;
            }
        }
    }
}