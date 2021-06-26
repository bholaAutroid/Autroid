package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.booking.BookingServiceDetailAdapter;
import autroid.business.interfaces.BookingDetailCallback;
import autroid.business.model.bean.ServiceBE;

public class BookingDetailsAdapter extends RecyclerView.Adapter<BookingDetailsAdapter.MyViewHolder> {

    private ArrayList<ServiceBE> arrayList;
    BookingDetailCallback mBookingDetailCallback;
    Context context;
    boolean isClaim;
    boolean isTechnician;

    public BookingDetailsAdapter(ArrayList<ServiceBE> arrayList,BookingDetailCallback mBookingDetailCallback,Context context,boolean isClaim,boolean isTechnician){
        this.arrayList=arrayList;
        this.mBookingDetailCallback=mBookingDetailCallback;
        this.context=context;
        this.isClaim=isClaim;
        this.isTechnician=isTechnician;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView serviceTitle,labourCost,partCost,qty,total,mSurveyorApproval,mCustomerApproval;
        LinearLayout mMainLayout;

        @BindView(R.id.parts_list)
        RecyclerView mPartsList;
        @BindView(R.id.labour_list)
        RecyclerView mLabourList;
        @BindView(R.id.fitting_list)
        RecyclerView mFittingList;

        @BindView(R.id.ll_parts)
        LinearLayout mPartsLayout;
        @BindView(R.id.ll_labour)
        LinearLayout mLabourLayout;
        @BindView(R.id.ll_fitting)
        LinearLayout mFittingLayout;

        @BindView(R.id.layout_details)
        LinearLayout mLayoutDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            serviceTitle=itemView.findViewById(R.id.service_title);
            labourCost=itemView.findViewById(R.id.labour_cost);
            partCost=itemView.findViewById(R.id.part_cost);
            qty=itemView.findViewById(R.id.qty);
            total=itemView.findViewById(R.id.total);
            mMainLayout=itemView.findViewById(R.id.main_layout);
            mSurveyorApproval=itemView.findViewById(R.id.surveyor_approval);
            mCustomerApproval=itemView.findViewById(R.id.customer_approval);
            mMainLayout.setOnClickListener(this);

            mPartsList.setLayoutManager(new LinearLayoutManager(context));
            mLabourList.setLayoutManager(new LinearLayoutManager(context));
            mFittingList.setLayoutManager(new LinearLayoutManager(context));

            if(isTechnician){
                labourCost.setVisibility(View.GONE);
                partCost.setVisibility(View.GONE);
                total.setVisibility(View.GONE);
            }

        }

        public void onBind(int position){
            ServiceBE data=arrayList.get(position);
            serviceTitle.setText(data.getService());
            labourCost.setText("Labour: ₹ "+data.getLabour_cost()+"\nPart: ₹ "+data.getPart_cost());
            partCost.setText("");
            if(data.getQuantity()>1)
                qty.setText("Quantity : "+data.getQuantity());
            else
                qty.setText("");

            total.setText("₹ "+data.getCost());

            if(isClaim){
                if(data.isClaim())
                if(data.isSurveyor_approval()){
                    mSurveyorApproval.setText("Surveyor Approval: YES");
                    mSurveyorApproval.setTextColor(context.getResources().getColor(R.color.green));
                } else {
                    mSurveyorApproval.setText("Surveyor Approval: NO");
                    mSurveyorApproval.setTextColor(context.getResources().getColor(R.color.red_color));
                }
            } else mSurveyorApproval.setVisibility(View.GONE);

            if(data.isCustomer_approval()){
                mCustomerApproval.setText("Customer Approval: YES");
                mCustomerApproval.setTextColor(context.getResources().getColor(R.color.green));
            }
            else {
                mCustomerApproval.setText("Customer Approval: NO");
                mCustomerApproval.setTextColor(context.getResources().getColor(R.color.red_color));
            }

            if(data.getParts().size()>0) {
                BookingServiceDetailAdapter bookingServiceDetailPart = new BookingServiceDetailAdapter(data.getParts(),isTechnician);
                mPartsList.setAdapter(bookingServiceDetailPart);
            }else {
                mPartsLayout.setVisibility(View.GONE);
            }

            if(data.getLabour().size()>0) {
                BookingServiceDetailAdapter bookingServiceDetailLabour=new BookingServiceDetailAdapter(data.getLabour(),isTechnician);
                mLabourList.setAdapter(bookingServiceDetailLabour);
            }else mLabourLayout.setVisibility(View.GONE);

            if(data.getOpening_fitting().size()>0) {
                BookingServiceDetailAdapter bookingServiceDetailFitting=new BookingServiceDetailAdapter(data.getOpening_fitting(),isTechnician);
                mFittingList.setAdapter(bookingServiceDetailFitting);
            }else mFittingLayout.setVisibility(View.GONE);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                    if(!arrayList.get(getLayoutPosition()).isExpanded()) {
                        mLayoutDetails.setVisibility(View.VISIBLE);
                        arrayList.get(getLayoutPosition()).setExpanded(true);
                    }
                    else {
                        mLayoutDetails.setVisibility(View.GONE);
                        arrayList.get(getLayoutPosition()).setExpanded(false);
                    }
                      //  mBookingDetailCallback.onServiceClick(arrayList.get(getLayoutPosition()));
                    break;
            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookingdetails_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
