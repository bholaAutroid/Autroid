package autroid.business.adapter.cars;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarDetailBE;


/**
 * Created by pranav.mittal on 01/17/18.
 */

public class SelectCarBookingAdapter extends RecyclerView.Adapter<SelectCarBookingAdapter.BookingCompletedHolder> {

    Context context;
    private ArrayList<CarDetailBE> mList;
    private RadioButton lastCheckedRB = null;
    public String carId=null,carName=null;

    public SelectCarBookingAdapter(ArrayList<CarDetailBE> mList, Context context){
        this.mList=mList;
        this.context=context;
    }

    @Override
    public BookingCompletedHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_select_car, parent, false);

        return new BookingCompletedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingCompletedHolder holder,final int position) {
        holder.mRbCarName.setText(mList.get(position).getTitle());

        holder.mRbCarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null && lastCheckedRB!=checked_rb){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
                carId=mList.get(position).getId();
                carName=mList.get(position).getModelName();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class BookingCompletedHolder extends RecyclerView.ViewHolder{

       RadioButton mRbCarName;
       TextView mName;

        public BookingCompletedHolder(View itemView) {
            super(itemView);
            mRbCarName = (RadioButton) itemView.findViewById(R.id.rb_car_name);
            mName=itemView.findViewById(R.id.car_name);

        }
    }
}
