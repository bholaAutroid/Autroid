package autroid.business.adapter.jobcard;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import autroid.business.R;
import autroid.business.interfaces.JobParticularsCallback;
import autroid.business.model.bean.ParticularsDataBE;

public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder> implements Filterable {

    private List<ParticularsDataBE> list;
    private final List<ParticularsDataBE> filteredList;

    JobParticularsCallback mJobParticularsCallback;

    private List<ParticularsDataBE> mDictionary;
    private CustomFilter mFilter;


    public AssetAdapter(ArrayList<ParticularsDataBE> arrayList, JobParticularsCallback mJobParticularsCallback) {
        this.list = arrayList;
        this.mJobParticularsCallback = mJobParticularsCallback;
        this.mDictionary = arrayList;
        filteredList = new ArrayList<ParticularsDataBE>();
        mFilter = new CustomFilter(AssetAdapter.this);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(v -> {

                if (list.get(getLayoutPosition()).getChecked()) {
                    checkBox.setChecked(false);
                    list.get(getLayoutPosition()).setChecked(false);
                } else {
                    checkBox.setChecked(true);
                    list.get(getLayoutPosition()).setChecked(true);
                }

                if (list.get(getLayoutPosition()).getValue().equalsIgnoreCase("Others")) mJobParticularsCallback.onCheckChange(list.get(getLayoutPosition()).getChecked());
            });
        }

        public void onBind(int position) {
            checkBox.setText(list.get(position).getValue());
            checkBox.setChecked(list.get(position).getChecked());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_particulars, viewGroup, false);
        return new AssetAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.onBind(i);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class CustomFilter extends Filter {
        private AssetAdapter mAdapter;

        private CustomFilter(AssetAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(mDictionary);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final ParticularsDataBE mWords : mDictionary) {
                    if (mWords.getValue().toLowerCase().contains(filterPattern)) {
                        filteredList.add(mWords);
                    }
                }
            }

            System.out.println("Count Number" + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number " + ((List<ParticularsDataBE>) results.values).size());
            list = (ArrayList<ParticularsDataBE>) results.values;
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
