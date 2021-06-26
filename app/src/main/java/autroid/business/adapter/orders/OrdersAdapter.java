package autroid.business.adapter.orders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import autroid.business.R;
import autroid.business.interfaces.OrdersListener;
import autroid.business.model.realm.OrdersRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class OrdersAdapter extends RealmRecyclerViewAdapter<OrdersRealm, OrdersAdapter.MyViewHolder> {

    private OrdersListener ordersListener;
    private String checker;

    public OrdersAdapter(@Nullable OrderedRealmCollection<OrdersRealm> data, boolean autoUpdate, OrdersListener ordersListener,String checker) {
        super(data, autoUpdate);
        this.ordersListener=ordersListener;
        this.checker=checker;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView order,delivered_by,time_slot,in_stock,name,time_left;
        ImageView btn_call,btn_chat;
        OrdersRealm ordersRealm;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            order=itemView.findViewById(R.id.order);
            in_stock=itemView.findViewById(R.id.in_stock);
            delivered_by=itemView.findViewById(R.id.delivered_by);
            time_slot=itemView.findViewById(R.id.time_slot);
            name=itemView.findViewById(R.id.name);
            time_left=itemView.findViewById(R.id.time_left);
            btn_call=itemView.findViewById(R.id.btn_call);
            if(!checker.equalsIgnoreCase("listOrder"))btn_call.setVisibility(View.GONE);
            btn_call.setOnClickListener(v->{
                ordersListener.onCallClick(ordersRealm.getContact_no());
            });
            cardView=itemView.findViewById(R.id.main_layout);
            cardView.setOnClickListener(v->{
                ordersListener.onCardClick(ordersRealm.getId());
            });
            btn_chat=itemView.findViewById(R.id.btn_chat);
            if(!checker.equalsIgnoreCase("listOrder"))btn_chat.setVisibility(View.GONE);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        OrdersRealm obj=getItem(position);
        myViewHolder.ordersRealm=obj;
        myViewHolder.order.setText("#"+myViewHolder.ordersRealm.getOrderNumber());
        myViewHolder.delivered_by.setText(myViewHolder.ordersRealm.getDeliveredBy());
        myViewHolder.time_slot.setText(myViewHolder.ordersRealm.getTimeSlot());
        myViewHolder.in_stock.setText(myViewHolder.ordersRealm.getStatus());
        myViewHolder.name.setText(myViewHolder.ordersRealm.getName());
    }
}
