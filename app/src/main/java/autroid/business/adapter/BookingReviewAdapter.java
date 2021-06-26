package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import autroid.business.R;
import autroid.business.model.realm.SelectedBookingDataRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class BookingReviewAdapter extends RealmRecyclerViewAdapter<SelectedBookingDataRealm, BookingReviewAdapter.BookingReviewHolder> {

    Context context;

    public BookingReviewAdapter(@Nullable OrderedRealmCollection<SelectedBookingDataRealm> data, boolean autoUpdate, Context context) {
        super(data, autoUpdate);
        this.context=context;
    }


    @Override
    public BookingReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_review, parent, false);

        return new BookingReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingReviewHolder holder, final int position) {
        SelectedBookingDataRealm categoryRealm = getItem(position);
        holder.mName.setText(categoryRealm.getPackageName());
        holder.mCost.setText("₹ "+categoryRealm.getCost());
    }


    public class BookingReviewHolder extends RecyclerView.ViewHolder {

        TextView mName, mCost;
        public BookingReviewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.service_name);
            mCost = itemView.findViewById(R.id.service_price);

        }

    }
}
