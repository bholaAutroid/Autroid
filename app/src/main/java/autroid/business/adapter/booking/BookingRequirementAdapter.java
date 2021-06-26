package autroid.business.adapter.booking;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import autroid.business.R;
import autroid.business.model.bean.CustomerRequirementsBE;

public class BookingRequirementAdapter extends RecyclerView.Adapter<BookingRequirementAdapter.MyViewHolder> {

    private ArrayList<CustomerRequirementsBE> arrayList;

    public BookingRequirementAdapter(ArrayList<CustomerRequirementsBE> arrayList){
        this.arrayList=arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mRemark,mDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mRemark=itemView.findViewById(R.id.remark);
            mDate=itemView.findViewById(R.id.date);
            mDate.setVisibility(View.GONE);
        }

        public void onBind(int position){
            CustomerRequirementsBE data=arrayList.get(position);
            mRemark.setText(data.getRequirement());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_booking_remark, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingRequirementAdapter.MyViewHolder myViewHolder, int position) {
        myViewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

