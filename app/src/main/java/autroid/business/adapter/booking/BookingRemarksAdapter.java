package autroid.business.adapter.booking;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.RemarkBE;

public class BookingRemarksAdapter extends RecyclerView.Adapter<BookingRemarksAdapter.MyViewHolder> {

    private ArrayList<RemarkBE> arrayList;

    public BookingRemarksAdapter(ArrayList<RemarkBE> arrayList){
        this.arrayList=arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mRemark,mDate,mRemarkBy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mRemark=itemView.findViewById(R.id.remark);
            mDate=itemView.findViewById(R.id.date);
            mRemarkBy=itemView.findViewById(R.id.remark_by);
        }

        public void onBind(int position){
            RemarkBE data=arrayList.get(position);
            mRemark.setText(data.getRemark());
            mDate.setText(data.getUpdated_at().substring(0,12));
            mRemarkBy.setText(data.getName());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_booking_remark, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
