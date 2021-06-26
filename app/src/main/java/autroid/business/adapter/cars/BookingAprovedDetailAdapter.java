package autroid.business.adapter.cars;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.ServiceBE;

public class BookingAprovedDetailAdapter extends RecyclerView.Adapter<BookingAprovedDetailAdapter.BookingReviewHolder> {

    Context context;
    public ArrayList<ServiceBE> arrayList;

    public BookingAprovedDetailAdapter(ArrayList<ServiceBE> arrayList, Context context){
        this.arrayList=arrayList;
        this.context=context;
    }

    @Override
    public BookingReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout. row_approved_services, parent, false);

        return new BookingReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingReviewHolder holder, final int position) {

        holder.mName.setText(arrayList.get(position).getService());
        holder.mCost.setText("₹ "+arrayList.get(position).getCost());
        holder.mDescription.setText(arrayList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class BookingReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName, mCost, mDescription;

        public BookingReviewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.service_name);
            mCost = itemView.findViewById(R.id.service_price);
            mDescription = itemView.findViewById(R.id.service_detail);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    }
}
