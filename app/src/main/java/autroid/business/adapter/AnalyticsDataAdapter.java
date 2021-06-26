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
import autroid.business.model.bean.ValuesBE;

public class AnalyticsDataAdapter extends RecyclerView.Adapter<AnalyticsDataAdapter.AnalyticsDataHolder> {

    Context context;

    ArrayList<ValuesBE> dataList;

    public AnalyticsDataAdapter(Context context,ArrayList<ValuesBE> dataList){
        this.context=context;
        this.dataList=dataList;
    }

    public class AnalyticsDataHolder extends RecyclerView.ViewHolder{

        TextView title,count;

        public AnalyticsDataHolder(View itemView) {
            super(itemView);
            count=itemView.findViewById(R.id.count);
            title=itemView.findViewById(R.id.title);
        }

        public void bind(int position){
            title.setText(dataList.get(position).getTitle());
            count.setText(String.valueOf(dataList.get(position).getCount()));
        }
    }

    @NonNull
    @Override
    public AnalyticsDataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_item_analytics_data, viewGroup, false);
        return new AnalyticsDataHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsDataHolder analyticsDataHolder, int position) {
        analyticsDataHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
