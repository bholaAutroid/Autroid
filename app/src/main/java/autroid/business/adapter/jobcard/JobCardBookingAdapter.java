package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.BookingRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class JobCardBookingAdapter extends RealmRecyclerViewAdapter<BookingRealm, JobCardBookingAdapter.BookingPendingHolder> {

    Context context;
    BookingStatusCallback mBookingStatusCallback;


    public JobCardBookingAdapter(@Nullable OrderedRealmCollection<BookingRealm> data, boolean autoUpdate, Context context, BookingStatusCallback mBookingStatusCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingStatusCallback=mBookingStatusCallback;

    }

  /*  public BookingPendingAdapter(ArrayList<BookingsBE> mList, Context context,BookingStatusCallback mBookingStatusCallback,String bookingStatus[]){
        this.mList=mList;

    }*/

    @Override
    public BookingPendingHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.row_job_bookings, parent, false);

        return new BookingPendingHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingPendingHolder holder, int position) {
        BookingRealm bookingRealm = getItem(position);
        holder.objBookingRealm = bookingRealm;
        holder.mCarMaker.setText(bookingRealm.getRegistrationNumber() + " / " + bookingRealm.getVehicleTitle());
        holder.mBookingDate.setText(Html.fromHtml("<b>" + bookingRealm.getDated() + ", " + bookingRealm.getTimeSlot()));
        // holder.mConvenience.setText("Convenience: "+bookingRealm.getConvenience());
        holder.mBookingId.setText("Booking ID: " + bookingRealm.getShortId());
        holder.mStatus.setText(bookingRealm.getStatus());

        if(bookingRealm.getAddress()!=null) {
            holder.mUserName.setText(bookingRealm.getmUserName() + " / ");
            holder.mUserNumber.setText(bookingRealm.getConvenience());
        }
        else {
            holder.mUserNumber.setBackgroundColor(context.getResources().getColor(R.color.card_color));
            holder.mUserNumber.setTextColor(context.getResources().getColor(R.color.white_dark));
            holder.mUserName.setText(bookingRealm.getmUserName() + " / ");
            holder.mUserNumber.setText(bookingRealm.getConvenience());
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


    public  class BookingPendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mCarMaker,mServicePrice,mBookingId,mConvenience,mBookingDate,mStatus,mRegistration,mPaidPrice,mDiscountType,mUserName,mUserNumber;
        ImageView mCarImage;
        LinearLayout mMainLayout;
        BookingRealm objBookingRealm;
        Button btnDetail,btnJobCard;

        public BookingPendingHolder(View itemView) {
            super(itemView);
            mCarMaker= (TextView) itemView.findViewById(R.id.car_maker);
            mServicePrice= (TextView) itemView.findViewById(R.id.price);
            mBookingId= (TextView) itemView.findViewById(R.id.booking_id);
            mConvenience= (TextView) itemView.findViewById(R.id.convenience);
            mBookingDate= (TextView) itemView.findViewById(R.id.date_text);
            mCarImage= (ImageView) itemView.findViewById(R.id.car_image);
            mRegistration=itemView.findViewById(R.id.car_registration);
            mPaidPrice=itemView.findViewById(R.id.pricePaid);
            mDiscountType=itemView.findViewById(R.id.discount_type);
            mStatus=itemView.findViewById(R.id.status);
            mMainLayout= (LinearLayout) itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(this);
            mUserName=itemView.findViewById(R.id.user_name);
            mUserNumber=itemView.findViewById(R.id.user_number);
            btnDetail=itemView.findViewById(R.id.btn_detail);
            btnJobCard=itemView.findViewById(R.id.open_jobcard);
            btnDetail.setOnClickListener(this);
            btnJobCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.main_layout:
                    break;
                case R.id.btn_detail:
                    mBookingStatusCallback.onDetailClick(objBookingRealm.getBookingId(),null);
                    break;
                case R.id.open_jobcard:
                    mBookingStatusCallback.createJobCard(objBookingRealm.getmUserId(),objBookingRealm.getBookingId());
                    break;
            }
        }
    }
}
