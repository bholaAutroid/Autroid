package autroid.business.adapter.jobcard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.PaymentLogBE;

public class PaymentLogAdapter extends RecyclerView.Adapter<PaymentLogAdapter.MyViewHolder> {

    private ArrayList<PaymentLogBE> arrayList;

    public PaymentLogAdapter(ArrayList<PaymentLogBE> arrayList){
        this.arrayList=arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView transactionId, paidTotal, paymentMode, transactionDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionId=itemView.findViewById(R.id.transaction_id);
            paidTotal=itemView.findViewById(R.id.paid_total);
            paymentMode=itemView.findViewById(R.id.payment_mode);
            transactionDate=itemView.findViewById(R.id.paid_on);
        }

        public void onBind(int position){
            transactionId.setText(arrayList.get(position).getTransactionId());
            paidTotal.setText("₹ "+arrayList.get(position).getPaidTotal());
            paymentMode.setText(arrayList.get(position).getPaymentMode());
            transactionDate.setText(arrayList.get(position).getUpdated_at());
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_log_item, viewGroup, false);
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
