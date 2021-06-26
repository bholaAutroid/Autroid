package autroid.business.adapter.jobcard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.ServicesDataBE;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {

    private ArrayList<ServicesDataBE> arrayList;

    public ServicesAdapter(ArrayList<ServicesDataBE> arrayList){
        this.arrayList=arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView services,cost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            services=itemView.findViewById(R.id.services);
            cost=itemView.findViewById(R.id.cost);
        }

        public void onBind(int position){
            services.setText(arrayList.get(position).getService());
            cost.setText("₹ "+arrayList.get(position).getCost());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.services_item, viewGroup, false);
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
