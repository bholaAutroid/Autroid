package autroid.business.adapter.cars;

import android.content.Context;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.BookingSlotCallback;
import autroid.business.model.bean.JobsLogBE;
import autroid.business.utils.Utility;


public class BookingTrackingAdapter extends RecyclerView.Adapter<BookingTrackingAdapter.ViewHolder> {

    Context context;
    String stages[];
    BookingSlotCallback mBookingSlotCallback;
    private ArrayList<JobsLogBE> logs;
    private String deliveryDate;

    public BookingTrackingAdapter(Context context, String stages[], ArrayList<JobsLogBE> logs, String deliveryDate){
        this.context=context;
        this.stages=stages;
        this.logs=logs;
        this.deliveryDate=deliveryDate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_tracking, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mStatusName.setText(Utility.splitCamelCase(stages[position]));

        if(position==stages.length-1){
            holder.mStatusDate.setText(deliveryDate);
        }
        else {
            holder.mStatusDate.setText("");

        }


        if(isStatus(stages[position])){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mTrackingLine.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
                holder.mTrackingCircle.setBackgroundTintList(context.getResources().getColorStateList(R.color.green));
            }

            holder.mStatusName.setTextColor(context.getResources().getColor(R.color.white_dark));
            holder.mStatusDate.setText(findDate(stages[position]));

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mTrackingLine.setBackgroundTintList(context.getResources().getColorStateList(R.color.black_opacity60));
                holder.mTrackingCircle.setBackgroundTintList(context.getResources().getColorStateList(R.color.black_opacity60));
            }
            holder.mStatusName.setTextColor(context.getResources().getColor(R.color.black_opacity60));
        }

        if(position==stages.length-1){
            holder.mTrackingLine.setVisibility(View.GONE);
        }
        else {
            holder.mTrackingLine.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return stages.length;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mTrackingCircle,mTrackingLine;
        TextView mStatusName,mStatusDate;

        public ViewHolder(View itemView) {
            super(itemView);
            mTrackingCircle=itemView.findViewById(R.id.track_image);
            mTrackingLine=itemView.findViewById(R.id.track_line);
            mStatusName=itemView.findViewById(R.id.title);
            mStatusDate=itemView.findViewById(R.id.date);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    }

    private boolean isStatus(String stage){

        for(int i=0;i<logs.size();i++){
            if(logs.get(i).getStage().equalsIgnoreCase(stage)){
                return true;
            }
        }

        return false;
    }

    private String findDate(String stage){
        String date="";

        for(int i=0;i<logs.size();i++){
            if(logs.get(i).getStage().equalsIgnoreCase(stage)){
                if(logs.get(i).getList()!=null)
                date=logs.get(i).getList().get(0).getUpdated_at();
            }
        }

        return date;
    }
}
