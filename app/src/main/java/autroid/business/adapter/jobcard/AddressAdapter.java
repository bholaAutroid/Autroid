package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import autroid.business.R;
import autroid.business.model.bean.BookingAddressBE;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private List<BookingAddressBE> arrayList;

    private RadioButton lastCheckedRB=null;

    public String addressId=null;

    Context context;

    public AddressAdapter(ArrayList<BookingAddressBE> arrayList,Context context){
        this.arrayList=arrayList;
        this.context=context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        RadioButton radio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            radio=itemView.findViewById(R.id.rb_car_name);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.jobcard_select_car, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        BookingAddressBE data=arrayList.get(position);
        myViewHolder.radio.setText(data.getAddress()+", "+data.getCity()+", "+data.getZip()+", "+data.getState());
        myViewHolder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton checked_rb = (RadioButton) view;
                if(lastCheckedRB != null && lastCheckedRB!=checked_rb){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
                addressId=data.getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
