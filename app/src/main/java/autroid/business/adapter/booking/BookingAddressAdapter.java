package autroid.business.adapter.booking;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.BookingAddressCallback;
import autroid.business.model.bean.BookingAddressBE;


public class BookingAddressAdapter extends RecyclerView.Adapter<BookingAddressAdapter.BookingAddressHolder> {


    public ArrayList<BookingAddressBE> mList;
    Context context;
    private RadioButton lastCheckedRB = null;
    public String addressId=null;
    BookingAddressCallback bookingAddressCallback;

    public BookingAddressAdapter(Context context, ArrayList<BookingAddressBE> mList, BookingAddressCallback bookingAddressCallback) {
        this.mList = mList;
        this.context = context;
        this.bookingAddressCallback=bookingAddressCallback;
    }

    @NonNull
    @Override
    public BookingAddressHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_address, viewGroup, false);

        return new BookingAddressHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookingAddressHolder bookingPackageseHolder, final int i) {

        String address=mList.get(i).getAddress();
        if(mList.get(i).getArea().length()>0)
            address=address+", "+mList.get(i).getArea();

        if(mList.get(i).getLandmark()!=null && mList.get(i).getLandmark().length()>0){
            address=address+", "+mList.get(i).getLandmark();
        }

        if(mList.get(i).getCity()!=null && mList.get(i).getCity().length()>0){
            address=address+", "+mList.get(i).getCity();
        }

        if(mList.get(i).getState()!=null && mList.get(i).getState().length()>0){
            address=address+", "+mList.get(i).getState();
        }

        if(mList.get(i).getZip()!=null && mList.get(i).getZip().length()>0){
            address=address+" "+mList.get(i).getZip();
        }

        bookingPackageseHolder.mRb.setText(address);

        bookingPackageseHolder.mRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null && lastCheckedRB!=checked_rb){
                    lastCheckedRB.setChecked(false);
                }

                lastCheckedRB = checked_rb;
                addressId=mList.get(i).getId();


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BookingAddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatRadioButton mRb;
        TextView mAddress,mDelete;
        LinearLayout mMainLayout;


        public BookingAddressHolder(View itemView) {
            super(itemView);

            mRb=itemView.findViewById(R.id.rb_address);
            mAddress=itemView.findViewById(R.id.address);
            mMainLayout=itemView.findViewById(R.id.main_layout);
            mDelete=itemView.findViewById(R.id.delete_address);
            mDelete.setOnClickListener(this);

          mMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton checked_rb =  mRb;
                    if(lastCheckedRB != null){
                        lastCheckedRB.setChecked(false);
                    }

                    lastCheckedRB = checked_rb;
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.delete_address:
                    bookingAddressCallback.onRemoveAddress(getLayoutPosition(),mList.get(getLayoutPosition()).getId());
                    break;
            }
        }
    }
}
