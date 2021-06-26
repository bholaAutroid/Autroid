package autroid.business.adapter.booking;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.BookingStatusCallback;
import autroid.business.model.realm.LeadBookingRealm;
import autroid.business.utils.Utility;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class LeadBookingAdapter extends RealmRecyclerViewAdapter<LeadBookingRealm, LeadBookingAdapter.MyViewHolder> {

    Context context;
    BookingStatusCallback clickCallback;


    public LeadBookingAdapter(@Nullable OrderedRealmCollection<LeadBookingRealm> data, boolean autoUpdate, BookingStatusCallback clickCallback) {
        super(data, autoUpdate);
        this.clickCallback=clickCallback;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView bookingId, date, carInfo, name, status;

        Button detailButton,callButton,chatButton;
        ConstraintLayout mMainLayout;

        LeadBookingRealm leadBookingRealm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingId = itemView.findViewById(R.id.booking_id);
            date = itemView.findViewById(R.id.date_text);
            carInfo = itemView.findViewById(R.id.car_maker);
            name = itemView.findViewById(R.id.user_name);
//            detailButton = itemView.findViewById(R.id.btn_detail);
//            callButton = itemView.findViewById(R.id.call_btn);
//            chatButton = itemView.findViewById(R.id.chat_btn);
            status = itemView.findViewById(R.id.status);
            mMainLayout=itemView.findViewById(R.id.main_layout);

//            detailButton.setOnClickListener(this);
//            callButton.setOnClickListener(this);
//            chatButton.setOnClickListener(this);
            mMainLayout.setOnClickListener(this);
        }

        public void bind(int position) {
            leadBookingRealm = getItem(position);
            carInfo.setText(leadBookingRealm.getRegistrationNo() + " / " + leadBookingRealm.getCarName());

            if(leadBookingRealm.getBookingDate().equalsIgnoreCase("Invalid date"))date.setVisibility(View.GONE);
            else date.setText(Html.fromHtml("<b>" + leadBookingRealm.getBookingDate() + ", " + leadBookingRealm.getBookingTime()));

            bookingId.setText("ID #" + leadBookingRealm.getBookingNo());

            if(leadBookingRealm.getConvenience().length()>0) name.setText(leadBookingRealm.getName() + " / " + leadBookingRealm.getConvenience());
            else name.setText(leadBookingRealm.getName());

            status.setText(Utility.splitCamelCase(leadBookingRealm.getStatus()));
        }

        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.main_layout:
                    clickCallback.onDetailClick(leadBookingRealm.getId(),leadBookingRealm.getStatus());
                    break;
//                case R.id.chat_btn:
//                    //clickCallback.onChatClick(leadBookingRealm);
//                    break;
//                case R.id.call_btn:
//                    clickCallback.onCallClick(leadBookingRealm.getContactNo());
//                    break;
            }
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_pending_booking,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }
}
