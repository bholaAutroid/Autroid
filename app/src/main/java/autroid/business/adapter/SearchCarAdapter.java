package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.SearchCarClickCallback;
import autroid.business.model.bean.SearchCarBE;

public class SearchCarAdapter extends RecyclerView.Adapter<SearchCarAdapter.AutomakerHolder> {

    Context context;
    ArrayList<SearchCarBE> mList;
    SearchCarClickCallback searchCarClickCallback;


    public SearchCarAdapter(Context context, ArrayList<SearchCarBE> mList,SearchCarClickCallback searchCarClickCallback){
        this.context=context;
        this.mList=mList;
        this.searchCarClickCallback=searchCarClickCallback;
    }

    @Override
    public AutomakerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_automaker, parent, false);

        return new AutomakerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AutomakerHolder holder, int position) {
        holder.mName.setText(mList.get(position).getVariant());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class AutomakerHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public AutomakerHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.automaker_name);

            mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchCarClickCallback.onCarClick(mList.get(getLayoutPosition()).getId(),mList.get(getLayoutPosition()).getVariant());

                }
            });

        }
    }

}