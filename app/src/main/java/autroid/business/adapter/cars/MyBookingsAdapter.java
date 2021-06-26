package autroid.business.adapter.cars;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import autroid.business.R;
import autroid.business.interfaces.BookingsClickCallback;
import autroid.business.model.realm.BookingRealm;
import autroid.business.utils.Utility;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by pranav.mittal on 11/19/17.
 */

public class MyBookingsAdapter extends RealmRecyclerViewAdapter<BookingRealm, RecyclerView.ViewHolder> {

    Context context;
    private final int ORDER = 0, BOOKING = 1;

    private String bookingStatus[]={"Pending","Confirmed","Completed"};
    BookingsClickCallback mBookingsClickCallback;

    public MyBookingsAdapter(@Nullable OrderedRealmCollection<BookingRealm> data, boolean autoUpdate, Context context, BookingsClickCallback mBookingsClickCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingsClickCallback=mBookingsClickCallback;
    }

    @Override
    public int getItemViewType(int position) {
        //More to come
            if(getItem(position).getListing().equalsIgnoreCase("ORDER"))
                return ORDER;
            else
                return BOOKING;  // status or question

    }

  /*  public BookingsAdapter(ArrayList<BookingsBE> mList, Context context,BookingStatusCallback mBookingStatusCallback,String bookingStatus[]){
        this.mList=mList;

    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case BOOKING :
                View layoutViewOffer =inflater.inflate(R.layout.row_my_booking,parent, false);
                viewHolder = new BookingPendingHolder(layoutViewOffer);
                break;
            default:
                View layoutViewDef =inflater.inflate(R.layout.row_my_booking,parent, false);
                viewHolder = new BookingPendingHolder(layoutViewDef);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder , int position) {
        switch (viewHolder.getItemViewType()) {
            case BOOKING: {
                BookingPendingHolder holder = (BookingPendingHolder) viewHolder;
                BookingRealm bookingRealm = getItem(position);
                holder.bookingRealm=bookingRealm;
                holder.mCarMaker.setText(bookingRealm.getVehicleTitle());

                holder.mRegis.setText(Html.fromHtml("" +bookingRealm.getRegistrationNumber()));


                if(bookingRealm.getStatus()!=null)
                    if(bookingRealm.getStatus().equalsIgnoreCase("Completed")){
                        holder.btnInvoice.setEnabled(true);
                    }
                    else {
                        holder.btnInvoice.setEnabled(false);

                    }

                if(bookingRealm.getPrice()<=0) {
                    holder.mServicePrice.setVisibility(View.GONE);
                }
                else {
                    holder.mServicePrice.setVisibility(View.VISIBLE);
                    holder.mServicePrice.setText(Html.fromHtml("Total value <b>₹ " +Math.round(bookingRealm.getPrice())+ "</b>"));
                    }


                holder.mBookingDate.setText(Html.fromHtml("Scheduled for "+"<b>" + bookingRealm.getDated()+"</b>"+", "+bookingRealm.getTimeSlot()));

                holder.mStatus.setText(Utility.splitCamelCase(bookingRealm.getStatus()));
                holder.mConvenience.setText("Convenience: "+bookingRealm.getConvenience());

                if(bookingRealm.getJob_no()!=null && bookingRealm.getJob_no().length()>0){
                    holder.mBookingId.setText("JOB #" + bookingRealm.getShortId());

                }
                else
                holder.mBookingId.setText("Booking ID: " + bookingRealm.getShortId());

                if(!bookingRealm.getStatus().equalsIgnoreCase("cancelled") && bookingRealm.getDue()!=null) {
                    if (bookingRealm.getDue() > 0) {
                        holder.btnPayBill.setEnabled(false);
                        holder.mDuePrice.setVisibility(View.VISIBLE);
                        holder.mDuePrice.setText(Html.fromHtml("Due <b>₹ " + Math.round(bookingRealm.getDue()) + "</b>"));

                    } else {
                        holder.btnPayBill.setEnabled(false);
                    }
                }
                else {
                    holder.btnPayBill.setEnabled(false);
                }

                holder.btnDetail.setText("Details");

               // holder.btnPayBill.setEnabled(false);*/
                Picasso.with(context).load(bookingRealm.getCarImage()).placeholder(R.drawable.ic_placeholder_car).into(holder.mCarImage);
            }
            break;

        }
    }


    public  class BookingPendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mCarMaker,mServicePrice,mDuePrice,mBookingId,mConvenience,mBookingDate,mStatus,mRegis,mViewOption;
        ImageView mCarImage;
        LinearLayout mMainLayout;
        BookingRealm bookingRealm;
        Button btnPayBill, btnInvoice,btnDetail;

        public BookingPendingHolder(View itemView) {
            super(itemView);
            mCarMaker=itemView.findViewById(R.id.car_maker);
            mServicePrice=itemView.findViewById(R.id.price);
            mBookingId=itemView.findViewById(R.id.booking_id);
            mStatus=itemView.findViewById(R.id.status);
            mConvenience=itemView.findViewById(R.id.convenience);
            mBookingDate=itemView.findViewById(R.id.date_text);
            mCarImage=itemView.findViewById(R.id.car_image);
            mRegis = itemView.findViewById(R.id.regis_no);
            mMainLayout=itemView.findViewById(R.id.main_layout);

            mMainLayout.setOnClickListener(this);
            mViewOption=itemView.findViewById(R.id.textViewOptions);
            mViewOption.setOnClickListener(this);
            btnDetail=itemView.findViewById(R.id.btn_detail);
            btnDetail.setOnClickListener(this);
            mStatus.setOnClickListener(this);

            btnPayBill = itemView.findViewById(R.id.pay_bill);
            btnInvoice = itemView.findViewById(R.id.get_invoice);
            mDuePrice=itemView.findViewById(R.id.due_price);
            btnPayBill.setOnClickListener(this);
            btnInvoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.main_layout:
                    break;
                case R.id.provider_name:
                    mBookingsClickCallback.onBusinessClick(bookingRealm.getBusinessId());
                    break;
                case R.id.status:
                {
                    try {
                        PopupMenu popup = new PopupMenu(context, mViewOption);
                        //inflating menu from xml resource
                        if (bookingRealm.getStatus().equalsIgnoreCase("Pending") || bookingRealm.getStatus().equalsIgnoreCase("Confirmed") || bookingRealm.getStatus().equalsIgnoreCase("Approval"))
                            popup.inflate(R.menu.my_bookings_menu);
                        else
                            popup.inflate(R.menu.booking_completed_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.cancel_booking:
                                        //handle menu1 click
                                        mBookingsClickCallback.onCancelClick(bookingRealm.getBookingId(), bookingRealm.getStatus());
                                        return true;
                                    case R.id.re_schedule:
                                        //handle menu1 click
                                        mBookingsClickCallback.onRescheduleClick(bookingRealm.getBookingId(), bookingRealm.getStatus(), bookingRealm.getBusinessId());
                                        return true;
                                    case R.id.view_business:
                                        //handle menu1 click
                                        mBookingsClickCallback.onBusinessClick(bookingRealm.getBusinessId());
                                        return true;

                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }catch (NullPointerException e){

                    }
                    catch (Exception e){

                    }
                }
                    break;
                case R.id.textViewOptions:
                    PopupMenu popup = new PopupMenu(context, mViewOption);
                    //inflating menu from xml

                    if(bookingRealm.getStatus().equalsIgnoreCase("Pending") || bookingRealm.getStatus().equalsIgnoreCase("Confirmed") || bookingRealm.getStatus().equalsIgnoreCase("Approval"))
                        popup.inflate(R.menu.my_bookings_menu);
                    else
                        popup.inflate(R.menu.booking_completed_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.cancel_booking:
                                    //handle menu1 click
                                    mBookingsClickCallback.onCancelClick(bookingRealm.getBookingId(),bookingRealm.getStatus());
                                    return true;
                                case R.id.re_schedule:
                                    //handle menu1 click
                                    mBookingsClickCallback.onRescheduleClick(bookingRealm.getBookingId(),bookingRealm.getStatus(),bookingRealm.getBusinessId());
                                    return true;
                                case R.id.view_business:
                                    //handle menu1 click
                                    mBookingsClickCallback.onBusinessClick(bookingRealm.getBusinessId());
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                    break;
                case R.id.pay_bill:
                    mBookingsClickCallback.onPayDue(bookingRealm.getBookingId(),bookingRealm.getStatus(),bookingRealm.getDue(),bookingRealm.getListing());
                    break;
                case R.id.btn_detail:
                    mBookingsClickCallback.onDetailClick(bookingRealm.getBookingId());
                    break;
                case R.id.get_invoice:
                    mBookingsClickCallback.onInvoiceClick(bookingRealm.getBookingId());
                    break;
            }
        }
    }


}
