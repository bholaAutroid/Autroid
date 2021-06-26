package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnCheckedChangeCallback;
import autroid.business.model.bean.ServiceTypeBE;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class SettingsServiceTypeAdapter extends RecyclerView.Adapter<SettingsServiceTypeAdapter.SettingsServiceTypeHolder> {

    Context context;
    ArrayList<ServiceTypeBE> mList;
    private OnCheckedChangeCallback mOnCheckedChangeCallback;

    public SettingsServiceTypeAdapter(Context context, ArrayList<ServiceTypeBE> serviceTypeBE,OnCheckedChangeCallback mOnCheckedChangeCallback){
        this.context=context;
        this.mList=serviceTypeBE;
        this.mOnCheckedChangeCallback=mOnCheckedChangeCallback;
    }

    @Override
    public SettingsServiceTypeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_service_type, parent, false);

        return new SettingsServiceTypeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SettingsServiceTypeHolder holder, int position) {

        holder.mName.setText(mList.get(position).getBusiness());

        if(mList.get(position).getIs_added()==0){
            holder.mIsServiceType.setChecked(false);
        }
        else {
            holder.mIsServiceType.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class SettingsServiceTypeHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView mName;
        SwitchCompat mIsServiceType;
        public SettingsServiceTypeHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.service_type_name);
            mIsServiceType= (SwitchCompat) itemView.findViewById(R.id.service_type_switch);

            mIsServiceType.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mOnCheckedChangeCallback.onCheckedChange(getLayoutPosition());
        }
    }

}
