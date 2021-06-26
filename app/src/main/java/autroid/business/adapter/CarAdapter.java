package autroid.business.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import autroid.business.R;
import autroid.business.interfaces.SearchUserDetailCallback;
import autroid.business.model.bean.CarDetailBE;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private ArrayList<CarDetailBE> arrayList;
    SearchUserDetailCallback mSearchUserDetailCallback;

    public CarAdapter(ArrayList<CarDetailBE> arrayList,SearchUserDetailCallback mSearchUserDetailCallback){
        this.arrayList=arrayList;
        this.mSearchUserDetailCallback=mSearchUserDetailCallback;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView carName,reg_no;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            carName=itemView.findViewById(R.id.name);
            reg_no=itemView.findViewById(R.id.phone);

            itemView.setOnClickListener(v->{
                if(mSearchUserDetailCallback!=null){

                    mSearchUserDetailCallback.onCarClick(arrayList.get(getAdapterPosition()).getId());
                }
            });
        }

        public void bind(int position) {
            carName.setText(arrayList.get(position).getTitle());
            reg_no.setText(arrayList.get(position).getRegistration_no());
        }
    }

    @NonNull
    @Override
    public CarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_search_user_item,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
