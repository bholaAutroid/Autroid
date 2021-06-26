package autroid.business.adapter.booking;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;

public class ReviewPointsAdapter extends RecyclerView.Adapter<ReviewPointsAdapter.MyViewHolder>{

    private ArrayList<String> arrayList;

    public ArrayList<Boolean> trueList;

    public ReviewPointsAdapter(ArrayList<String> arrayList){

        this.arrayList=arrayList;
        trueList=new ArrayList<>();
        for(int i=0;i<arrayList.size();i++){
            trueList.add(false);
        }


    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        AppCompatCheckBox checkBox;
        TextView points;
        EditText remark;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            points=itemView.findViewById(R.id.point);
            checkBox=itemView.findViewById(R.id.cb_status);
            remark=itemView.findViewById(R.id.remark);
            remark.setVisibility(View.GONE);


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    trueList.set(getLayoutPosition(),b);

                }
            });
        }

        public void onBind(int position){
            points.setText(arrayList.get(position));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_qc_status, viewGroup, false);
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
