package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.OverviewDataBE;

public class BusinessOverviewDataAdapter extends RecyclerView.Adapter<BusinessOverviewDataAdapter.OverViewHolder> {

    private ArrayList<OverviewDataBE> mList;

    public BusinessOverviewDataAdapter(Context context,ArrayList<OverviewDataBE> mList) {
        this.mList = mList;
    }

    public class OverViewHolder extends RecyclerView.ViewHolder {

        private TextView title,count;

        public OverViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            count=itemView.findViewById(R.id.count);
        }

        public void bind(int position) {
            title.setText(mList.get(position).getTitle());

            if(mList.get(position).getGroup()!=null)
                if(mList.get(position).getGroup().equalsIgnoreCase("Income Overview")){
                    count.setText("₹ "+mList.get(position).getCount());
                }else {
                    count.setText(mList.get(position).getCount());
                }
        }
    }

    @NonNull
    @Override
    public OverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item_data_overview, viewGroup, false);
        return new OverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OverViewHolder overViewHolder, int position) {
        overViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

