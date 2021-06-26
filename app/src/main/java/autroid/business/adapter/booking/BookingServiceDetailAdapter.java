package autroid.business.adapter.booking;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import autroid.business.R;
import autroid.business.model.bean.PriceBreakupBE;

public class BookingServiceDetailAdapter extends RecyclerView.Adapter<BookingServiceDetailAdapter.MyViewHolder> {

    private ArrayList<PriceBreakupBE> arrayList;
    boolean isTechnician;

    public BookingServiceDetailAdapter(ArrayList<PriceBreakupBE> arrayList,boolean isTechnician){
        this.arrayList=arrayList;
        this.isTechnician=isTechnician;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mItem,mPrice,mQuantity;
        ImageView imgCheck;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mItem=itemView.findViewById(R.id.item_name);
            mPrice=itemView.findViewById(R.id.price);
            mQuantity=itemView.findViewById(R.id.quantity);
            imgCheck=itemView.findViewById(R.id.img_check);

            if(isTechnician){
                mPrice.setVisibility(View.GONE);
            }

        }

        public void onBind(int position){
            PriceBreakupBE data=arrayList.get(position);

            if(data.getSource()!=null){
                imgCheck.setVisibility(View.VISIBLE);
            }
            else {
                imgCheck.setVisibility(View.GONE);

            }

            mItem.setText(data.getItem());
            mPrice.setText("Price: ₹ "+data.getBase()+"\n"+data.getTax()+": ₹ "+data.getTax_amount()+"\n Total: ₹ "+data.getAmount());

            if(data.getQuantity()>1)
                mQuantity.setText("Quantity "+Math.round(data.getQuantity())+"");
            else
                mQuantity.setText("");

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_service_details, viewGroup, false);
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
