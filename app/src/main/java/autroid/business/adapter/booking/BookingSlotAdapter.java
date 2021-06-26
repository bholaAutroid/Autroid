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
import autroid.business.model.bean.BookingSlotBE;


public class BookingSlotAdapter extends RecyclerView.Adapter<BookingSlotAdapter.BookingSlotHolder> {

    Context context;
    ArrayList<BookingSlotBE> mList;
    TextView selectedView;
    BookingSlotCallback mBookingSlotCallback;

    public BookingSlotAdapter(Context context, ArrayList<BookingSlotBE> mList, BookingSlotCallback mBookingSlotCallback){
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

        if(mList.get(0).getStatus()) {
            if (selectedView == null) {
                selectedView = (TextView) holder.mSlot;
                selectedView.setBackgroundResource(R.drawable.rectangle_red_color);
                selectedView.setTextColor(context.getResources().getColor(R.color.white_color));
                mBookingSlotCallback.onSlotClick(mList.get(0).getStatus(),mList.get(0).getSlot());

            }
        }

        if(mList.get(position).getStatus()){
            holder.mSlot.setText(mList.get(position).getSlot());


        }
        else {
            holder.mSlot.setText(mList.get(position).getSlot());
            holder.mSlot.setTextColor(context.getResources().getColor(R.color.lightgrey));
        }
       // holder.mSlot.setEnabled(mList.get(position).getStatus());


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
                    if(mList.get(getLayoutPosition()).getStatus()){
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
                        mBookingSlotCallback.onSlotClick(mList.get(getLayoutPosition()).getStatus(),mList.get(getLayoutPosition()).getSlot());
                    }
                    else {
                        mBookingSlotCallback.onSlotClick(mList.get(getLayoutPosition()).getStatus(),mList.get(getLayoutPosition()).getSlot());
                    }
                    break;
            }
        }
    }
}
