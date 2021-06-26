package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarDetailBE;

/**
 * Created by pranav.mittal on 01/17/18.
 */

public class SelectCarBookingAdapter extends RecyclerView.Adapter<SelectCarBookingAdapter.BookingCompletedHolder> {

    Context context;
    private ArrayList<CarDetailBE> mList;

    @Override
    public BookingCompletedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_select_car, parent, false);

        return new BookingCompletedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingCompletedHolder holder, int position) {
        holder.mCarName.setText(mList.get(position).getModel());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class BookingCompletedHolder extends RecyclerView.ViewHolder{

       RadioButton mCarName;

        public BookingCompletedHolder(View itemView) {
            super(itemView);
            mCarName = (RadioButton) itemView.findViewById(R.id.car_name);

        }
    }
}
