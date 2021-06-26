package autroid.business.adapter.search;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.bean.UserBE;

public class MainSearchUserAdapter extends RecyclerView.Adapter<MainSearchUserAdapter.MyViewHolder> {

    private ArrayList<UserBE> arrayList;
    private MainSearchListener mainSearchListener;

    public MainSearchUserAdapter(ArrayList<UserBE> arrayList,MainSearchListener mainSearchListener){
        this.arrayList=arrayList;
        this.mainSearchListener=mainSearchListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,phone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v->{
                mainSearchListener.onClickSearchItem(arrayList.get(getAdapterPosition()).getId(),null,null);
            });
            name=itemView.findViewById(R.id.name);
            phone=itemView.findViewById(R.id.phone);
        }

        public void bind(int position) {
            name.setText(arrayList.get(position).getName());
            phone.setText(arrayList.get(position).getContact_no());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_search_user_item,viewGroup,false);
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
