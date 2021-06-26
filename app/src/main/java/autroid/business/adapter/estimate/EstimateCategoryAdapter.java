package autroid.business.adapter.estimate;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.model.realm.BookingCategoryRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class EstimateCategoryAdapter extends RealmRecyclerViewAdapter<BookingCategoryRealm, EstimateCategoryAdapter.BookingCategoryHolder> {

    Context context;
    BookingCategoryCallback mBookingCategoryCallback;

    public EstimateCategoryAdapter(@Nullable OrderedRealmCollection<BookingCategoryRealm> data, boolean autoUpdate, Context context, BookingCategoryCallback mBookingCategoryCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingCategoryCallback=mBookingCategoryCallback;
    }


    @Override
    public BookingCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_category, parent, false);

        return new BookingCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingCategoryHolder holder, final int position) {
        BookingCategoryRealm categoryRealm=getItem(position);
        holder.bookingCategoryRealm=categoryRealm;
        holder.mName.setText(categoryRealm.getTitle());

        holder.mMainlayout.setEnabled(categoryRealm.getEnable());
        holder.mName.setEnabled(categoryRealm.getEnable());
    }


    public class BookingCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mName;
        LinearLayout mMainlayout;

        BookingCategoryRealm bookingCategoryRealm;

        public BookingCategoryHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.category_name);

            mMainlayout=itemView.findViewById(R.id.main_layout);

            mMainlayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                    mBookingCategoryCallback.onCategoryClick(bookingCategoryRealm.getTitle(),bookingCategoryRealm.getTag(),bookingCategoryRealm.getNested());
                    break;
            }
        }
    }
}
