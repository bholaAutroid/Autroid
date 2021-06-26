package autroid.business.adapter.purchase;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarItemsBE;


public class ModelFilterAdapter extends RecyclerView.Adapter<ModelFilterAdapter.AutomakerHolder> implements Filterable {

    Context context;
    public ArrayList<CarItemsBE> mList;
    public ArrayList<CarItemsBE> mFilteredList;

    public ModelFilterAdapter(Context context, ArrayList<CarItemsBE> mList) {
        this.context = context;
        this.mList = mList;
        this.mFilteredList=mList;
    }

    @Override
    public AutomakerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_model_filter, parent, false);

        return new AutomakerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AutomakerHolder holder, int position) {
        holder.mName.setText(mFilteredList.get(position).getValue());

        if(mFilteredList.get(position).getChecked()){
            holder.mName.setChecked(true);
        }
        else {
            holder.mName.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mList;
                } else {

                    ArrayList<CarItemsBE> filteredList = new ArrayList<>();

                    for (CarItemsBE androidVersion : mList) {

                        if (androidVersion.getValue().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    mFilteredList = (ArrayList<CarItemsBE>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
    }

    public class AutomakerHolder extends RecyclerView.ViewHolder {
        CheckBox mName;

        public AutomakerHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.cb_model);

            mName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mFilteredList.get(getLayoutPosition()).setChecked(isChecked);

                }
            });

        }
    }
}