package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.BookingRealm;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by pranav.mittal on 11/19/17.
 */

public class BookingPendingAdapter extends RealmRecyclerViewAdapter<BookingRealm, BookingPendingAdapter.BookingPendingHolder> {

    Context context;
    BookingStatusCallback mBookingStatusCallback;


    public BookingPendingAdapter(@Nullable OrderedRealmCollection<BookingRealm> data, boolean autoUpdate,Context context,BookingStatusCallback mBookingStatusCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingStatusCallback=mBookingStatusCallback;

    }

  /*  public BookingPendingAdapter(ArrayList<BookingsBE> mList, Context context,BookingStatusCallback mBookingStatusCallback,String bookingStatus[]){
        this.mList=mList;

    }*/

    @Override
    public BookingPendingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.row_pending_booking, parent, false);
        return new BookingPendingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingPendingHolder holder, int position) {
        BookingRealm bookingRealm = getItem(position);
        holder.objBookingRealm = bookingRealm;
        holder.mCarMaker.setText(bookingRealm.getRegistrationNumber() + " / " + bookingRealm.getVehicleTitle());
        if(bookingRealm.getDated()!=null)
            if(!bookingRealm.getDated().equalsIgnoreCase("Invalid date")) {
                holder.mBookingDate.setVisibility(View.VISIBLE);
                holder.mBookingDate.setText(Html.fromHtml("<b>" + bookingRealm.getDated() + ", " + bookingRealm.getTimeSlot()));
            }else {
                holder.mBookingDate.setVisibility(View.GONE);
            }

        // holder.mConvenience.setText("Convenience: "+bookingRealm.getConvenience());
        holder.mBookingId.setText("ID #" + bookingRealm.getShortId());
        holder.mStatus.setText(Utility.splitCamelCase(bookingRealm.getStatus()));

        if(bookingRealm.getAddress()!=null) {
            holder.mUserName.setText(bookingRealm.getmUserName() + " / ");
            holder.mUserNumber.setText(bookingRealm.getConvenience());
        } else {
            holder.mUserNumber.setBackgroundColor(context.getResources().getColor(R.color.card_color));
            holder.mUserNumber.setTextColor(context.getResources().getColor(R.color.white_dark));
            holder.mUserName.setText(bookingRealm.getmUserName());

            if (bookingRealm.getConvenience() != null) {
                if (bookingRealm.getConvenience().length() > 0) holder.mUserNumber.setText(" / " + bookingRealm.getConvenience());
                else holder.mUserNumber.setText("");
            }
        }

        if(bookingRealm.getStatus().equals(Constant.CONFIRMED))holder.mStatus.setVisibility(View.GONE);

        if(bookingRealm.getCareager_cash()!=null)
            if(bookingRealm.getCareager_cash()>0){
                holder.mCarEagerCash.setVisibility(View.VISIBLE);
                holder.mCarEagerCash.setText("CarEager Cash: "+Math.round(bookingRealm.getCareager_cash()));
            }
            else {
                holder.mCarEagerCash.setVisibility(View.GONE);
            }

       /* if(bookingRealm.getPrice()<=0) {
            holder.mServicePrice.setVisibility(View.GONE);
        }
        else {
            holder.mServicePrice.setVisibility(View.VISIBLE);
            holder.mServicePrice.setText(Html.fromHtml("Value <b>₹ " + Math.round(bookingRealm.getPrice() )+ "</b>"));
        }

        if(bookingRealm.getPaidTotal()<=0){
            holder.mPaidPrice.setVisibility(View.GONE);
        }
        else {
            holder.mPaidPrice.setVisibility(View.VISIBLE);
            holder.mPaidPrice.setText(Html.fromHtml("Amount Paid <b>₹ " + Math.round(bookingRealm.getPaidTotal()) + "</b>"));
        }

        if(bookingRealm.getDiscount_type()!=null)
            if(bookingRealm.getDiscount_type().equalsIgnoreCase("coins")){
                holder.mDiscountType.setVisibility(View.VISIBLE);
                holder.mDiscountType.setText(Html.fromHtml("Coins Used: <b>"+Math.round(bookingRealm.getDiscount())+"</b>"));
            }
            else if(bookingRealm.getDiscount_type().equalsIgnoreCase("coupon")){
                holder.mDiscountType.setVisibility(View.VISIBLE);
                holder.mDiscountType.setText(Html.fromHtml("Coupon Applied/<b>"+bookingRealm.getCoupon()+"</b>"));
            }

           // Picasso.with(context).load(bookingRealm.getCarImage()).placeholder(R.drawable.placeholder_thumbnail).into(holder.mCarImage);
    }*/
    }


    public class BookingPendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mCarEagerCash,mCarMaker,mServicePrice,mBookingId,mConvenience,mBookingDate,mStatus,mRegistration,mPaidPrice,mDiscountType,mUserName,mUserNumber;
        ConstraintLayout mMainLayout;
        BookingRealm objBookingRealm;

        public BookingPendingHolder(View itemView) {
            super(itemView);
            mBookingId= (TextView) itemView.findViewById(R.id.booking_id);
            mStatus=itemView.findViewById(R.id.status);
            mBookingDate= (TextView) itemView.findViewById(R.id.date_text);
            mCarMaker= (TextView) itemView.findViewById(R.id.car_maker);
            mUserName=itemView.findViewById(R.id.user_name);
            mUserNumber=itemView.findViewById(R.id.user_number);
            mCarEagerCash=itemView.findViewById(R.id.careager_cash);
            mMainLayout= itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(this);

//            mServicePrice= (TextView) itemView.findViewById(R.id.price);
//            mConvenience= (TextView) itemView.findViewById(R.id.convenience);
//            mRegistration=itemView.findViewById(R.id.car_registration);
//            mPaidPrice=itemView.findViewById(R.id.pricePaid);
//            mDiscountType=itemView.findViewById(R.id.discount_type);
//
//
//            mStatus.setOnClickListener(this);
//            mUserNumber.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.main_layout:
                    mBookingStatusCallback.onDetailClick(objBookingRealm.getBookingId(),objBookingRealm.getStatus());
                    break;
//                case R.id.btn_detail:
//                    break;
//                case R.id.chat_btn:
//                    mBookingStatusCallback.onChatClick(objBookingRealm.getmUserId());
//                    break;
//                case R.id.call_btn:
//                    mBookingStatusCallback.onCallClick(objBookingRealm.getmUserNumber());
//                    break;
//                case R.id.user_number:
//                    if(objBookingRealm.getAddress()!=null)
//                      mBookingStatusCallback.onAddressClick(objBookingRealm.getAddress(),objBookingRealm.getConvenience());
//                    break;
//                case R.id.status: {
//                    PopupMenu popup = new PopupMenu(context, mViewOption);
//                    //inflating menu from xml resource
//                    popup.inflate(R.menu.bookings_menu);
//                    //adding click listener
//                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.confirm_booking:
//                                    mBookingStatusCallback.confirmBooking(getLayoutPosition(), objBookingRealm.getBookingId());
//                                    return true;
//                                case R.id.completed_booking:
//                                    mBookingStatusCallback.completeBooking(getLayoutPosition(), objBookingRealm.getBookingId());
//                                    return true;
//                                case R.id.reject_booking:
//                                    mBookingStatusCallback.rejectBooking(getLayoutPosition(), objBookingRealm.getBookingId());
//                                    return true;
//                                case R.id.reschedule_booking:
//                                    mBookingStatusCallback.onRescheduleClick(objBookingRealm.getBookingId(), objBookingRealm.getStatus());
//                                    return true;
//                                case R.id.create_lead:
//                                    mBookingStatusCallback.createLead(objBookingRealm.getmUserName(),objBookingRealm.getmUserNumber(),"",objBookingRealm.getShortId());
//                                    return true;
//                                default:
//                                    return false;
//                            }
//                        }
//                    });
//                    //displaying the popup
//                    popup.show();
//                }
//                    break;
            }
        }
    }
}
