package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import autroid.business.R;
import autroid.business.model.bean.OverviewBE;

public class BusinessOverviewAdapter extends RecyclerView.Adapter<BusinessOverviewAdapter.OverViewHolder>   {

    Context context;

    ArrayList<OverviewBE> mList;

    BusinessOverviewDataAdapter businessOverviewDataAdapter;

    public BusinessOverviewAdapter(Context context,ArrayList<OverviewBE> mList){
        this.context=context;
        this.mList=mList;
    }

    public class OverViewHolder extends RecyclerView.ViewHolder{

        private TextView group;
        private RecyclerView recyclerView;


        public OverViewHolder(View itemView) {
            super(itemView);
            group=itemView.findViewById(R.id.group);
            recyclerView=itemView.findViewById(R.id.overview_data_recycler);
            recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        }

        public void bind(int position){
            group.setText(mList.get(position).getGroup());
           businessOverviewDataAdapter=new BusinessOverviewDataAdapter(context,mList.get(position).getOverviewData());
           recyclerView.setAdapter(businessOverviewDataAdapter);
        }
    }

    @NonNull
    @Override
    public OverViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item_overview, viewGroup, false);
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
