package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarItemsBE;

/**
 * Created by pranav.mittal on 06/01/17.
 */

public class AutomakerAdapter extends RecyclerView.Adapter<AutomakerAdapter.AutomakerHolder> {

    Context context;
    ArrayList<CarItemsBE> mList;

    public AutomakerAdapter(Context context, ArrayList<CarItemsBE> mList){
        this.context=context;
        this.mList=mList;
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
        holder.mName.setText(mList.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AutomakerHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public AutomakerHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.automaker_name);

        }
    }

}
