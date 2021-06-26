package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.BookingRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by pranav.mittal on 11/22/17.
 */

public class BookingCompletedAdapter extends RealmRecyclerViewAdapter<BookingRealm,BookingCompletedAdapter.BookingCompletedHolder> {

    Context context;
    BookingStatusCallback mBookingStatusCallback;

    public BookingCompletedAdapter(@Nullable OrderedRealmCollection<BookingRealm> data, boolean autoUpdate, Context context,BookingStatusCallback mBookingStatusCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingStatusCallback=mBookingStatusCallback;
    }

  /*  public BookingCompletedAdapter(ArrayList<BookingsBE> mList, Context context,BookingStatusCallback mBookingStatusCallback){
        this.mList=mList;
        this.context=context;
        this.mBookingStatusCallback=mBookingStatusCallback;

    }*/

    @Override
    public BookingCompletedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_completed_booking, parent, false);

        return new BookingCompletedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingCompletedHolder holder, int position) {
        BookingRealm bookingRealm=getItem(position);
        holder.objBookingRealm=bookingRealm;
        holder.mCarMaker.setText(bookingRealm.getVehicleTitle());
        holder.mServicePrice.setText("₹ "+bookingRealm.getPrice());
        holder.mBookingDate.setText("Service Date: "+bookingRealm.getDated());
        holder.mServices.setText(bookingRealm.getServices());
        holder.mName.setText(bookingRealm.getProviderName());
        holder.mBookingId.setText("Booking ID: "+bookingRealm.getShortId());

        Picasso.with(context).load(bookingRealm.getCarImage()).placeholder(R.drawable.placeholder_thumbnail).into(holder.mCarImage);

    }


    public  class BookingCompletedHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mCarMaker,mServicePrice,mServices,mBookingId,mBookingDate,mName;
        ImageView mCarImage;
        LinearLayout mMainLayout;
        BookingRealm objBookingRealm;

        public BookingCompletedHolder(View itemView) {
            super(itemView);
            mCarMaker= (TextView) itemView.findViewById(R.id.car_maker);
            mServicePrice= (TextView) itemView.findViewById(R.id.price);
            mServices= (TextView) itemView.findViewById(R.id.services_list);
            mBookingId= (TextView) itemView.findViewById(R.id.booking_id);
            mBookingDate= (TextView) itemView.findViewById(R.id.date_text);
            mName= (TextView) itemView.findViewById(R.id.provider_name);
            mCarImage= (ImageView) itemView.findViewById(R.id.car_image);
            mMainLayout= (LinearLayout) itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.main_layout:
                    break;
            }
        }
    }
}
