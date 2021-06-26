package autroid.business.adapter.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.SearchUserDetailCallback;
import autroid.business.model.bean.BookingsBE;

public class MainSearchBookingAdapter extends RecyclerView.Adapter<MainSearchBookingAdapter.MyViewHolder> {

    private ArrayList<BookingsBE> arrayList;
    SearchUserDetailCallback mainSearchListener;

    public MainSearchBookingAdapter(ArrayList<BookingsBE> arrayList, SearchUserDetailCallback mainSearchListener){
        this.arrayList=arrayList;
        this.mainSearchListener=mainSearchListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView booking,status,date_time,title,reg_no,name,phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v-> {
                if(mainSearchListener!=null)mainSearchListener.onBookingClick(arrayList.get(getAdapterPosition()).getId());
            });
            booking=itemView.findViewById(R.id.booking);
            title=itemView.findViewById(R.id.title);
            reg_no=itemView.findViewById(R.id.reg_no);
            date_time=itemView.findViewById(R.id.date_time);
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
            status=itemView.findViewById(R.id.status);
        }

        public void bind(int position){
            booking.setText("#"+arrayList.get(position).getBooking_no());
            status.setText(arrayList.get(position).getStatus());
            date_time.setText(arrayList.get(position).getDate()+" | "+arrayList.get(position).getTime_slot());
            title.setText(arrayList.get(position).getCar().getTitle());
            reg_no.setText(arrayList.get(position).getCar().getRegistration_no());
            name.setText(arrayList.get(position).getUser().getName());
            phone.setText(arrayList.get(position).getUser().getContact_no());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_search_booking_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
