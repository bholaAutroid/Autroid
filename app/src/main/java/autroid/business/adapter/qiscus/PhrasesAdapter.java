package autroid.business.adapter.qiscus;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import autroid.business.R;
import autroid.business.interfaces.RecyclerViewListener;

public class PhrasesAdapter extends RecyclerView.Adapter<PhrasesAdapter.MyViewHolder> {

    private List<String> list;
    private RecyclerViewListener recyclerViewListener;

    public PhrasesAdapter(List<String> arrayList, RecyclerViewListener recyclerViewListener){
        this.list=arrayList;
        this.recyclerViewListener=recyclerViewListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.phrases);
            textView.setOnClickListener(v->{
                recyclerViewListener.onPhrasesClick(getAdapterPosition());
            });
        }

        public void bind(int index){
            textView.setText(list.get(index));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phrases, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
