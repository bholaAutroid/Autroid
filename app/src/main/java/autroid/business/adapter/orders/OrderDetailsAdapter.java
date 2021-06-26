package autroid.business.adapter.orders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.ItemDataBE;
import autroid.business.model.bean.ServiceBE;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    private ArrayList<ItemDataBE> arrayListItems;
    private ArrayList<ServiceBE> servicesData;

    public OrderDetailsAdapter(ArrayList<ItemDataBE> arrayList) {
        arrayListItems = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, qty, total, service_name, service_taken;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.order_title);
            qty = itemView.findViewById(R.id.qty);
            total = itemView.findViewById(R.id.total);
            service_name = itemView.findViewById(R.id.service_name);
            service_taken = itemView.findViewById(R.id.services_taken);
        }

        public void bind(int position) {

            ItemDataBE data = arrayListItems.get(position);
            servicesData = data.getServices();
            title.setText(data.getTitle());
            qty.setText("Qty : " + String.valueOf(data.getQuantity()));
            total.setText("Total : ₹ " + String.valueOf(data.getTotal()));
//            if (servicesData.size() != 0) {
//                for (ServiceBE service : servicesData)
//                    service_name.append("\u2022 " + service.getService() + "\n");
//                service_name.setText(service_name.getText().toString().trim());
//            } else {
//                service_taken.setVisibility(View.GONE);
//                service_name.setVisibility(View.GONE);
//            }
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orderdetails_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }


    @Override
    public int getItemCount() {
        return arrayListItems.size();
    }
}
