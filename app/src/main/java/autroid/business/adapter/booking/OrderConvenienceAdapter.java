package autroid.business.adapter.booking;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.BookingSlotCallback;
import autroid.business.model.bean.OrderConvenienceBE;


public class OrderConvenienceAdapter extends RecyclerView.Adapter<OrderConvenienceAdapter.BookingSlotHolder> {

    Context context;
    ArrayList<OrderConvenienceBE> mList;
    TextView selectedView;
    BookingSlotCallback mBookingSlotCallback;

    public OrderConvenienceAdapter(Context context, ArrayList<OrderConvenienceBE> mList, BookingSlotCallback mBookingSlotCallback){
        this.context=context;
        this.mList=mList;
        this.mBookingSlotCallback=mBookingSlotCallback;
    }

    @Override
    public BookingSlotHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_slot, parent, false);

        return new BookingSlotHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingSlotHolder holder, int position) {

        if(position==0){
            if(selectedView==null){
                selectedView= (TextView) holder.mSlot;
                selectedView.setBackgroundResource(R.drawable.rectangle_red_color);
                selectedView.setTextColor(context.getResources().getColor(R.color.white_color));
                mBookingSlotCallback.onConvenienceClick(mList.get(position).getConvenience(),mList.get(position).getCharges());
            }
        }

        holder.mSlot.setText(mList.get(position).getConvenience());

        if(mList.get(position).getEnabled()!=null)
            holder.mSlot.setEnabled(mList.get(position).getEnabled());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class BookingSlotHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mSlot;


        public BookingSlotHolder(View itemView) {
            super(itemView);
            mSlot= (TextView) itemView.findViewById(R.id.slot);
            mSlot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.slot:
                        if(selectedView==null){
                            selectedView= (TextView) v;
                            selectedView.setBackgroundResource(R.drawable.rectangle_red_color);
                            selectedView.setTextColor(context.getResources().getColor(R.color.white_color));
                        }
                        else {
                            selectedView.setBackgroundResource(R.drawable.rectangle_white_color);
                            selectedView.setTextColor(context.getResources().getColor(R.color.selector_white_button_text_color));
                            selectedView= (TextView) v;
                            selectedView.setBackgroundResource(R.drawable.rectangle_red_color);
                            selectedView.setTextColor(context.getResources().getColor(R.color.white_color));

                        }

                        mBookingSlotCallback.onConvenienceClick(mList.get(getLayoutPosition()).getConvenience(),mList.get(getLayoutPosition()).getCharges());


                    break;
            }
        }
    }
}
