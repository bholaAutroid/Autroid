package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.TimingsBE;
import autroid.business.model.response.TimingArrayResponse;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class SettingsBusinessTimingsAdapter extends RecyclerView.Adapter<SettingsBusinessTimingsAdapter.SettingsBusinessTimingsHolder> {

    Context context;
    public static ArrayList<TimingsBE> mList;
    TimingArrayResponse timingArrayResponse;
    public SettingsBusinessTimingsAdapter(Context context, ArrayList<TimingsBE> mList,TimingArrayResponse timingArrayResponse){
        this.context=context;
        this.mList=mList;
        this.timingArrayResponse=timingArrayResponse;
    }

    @Override
    public SettingsBusinessTimingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_business_timings, parent, false);

        return new SettingsBusinessTimingsHolder(itemView,mList);
    }

    @Override
    public void onBindViewHolder(SettingsBusinessTimingsHolder holder, int position) {
            holder.mDay.setText(mList.get(position).getDay());

        ArrayAdapter<String> adapterOpen = new ArrayAdapter<String>(context, R.layout.layout_spinner_remark,timingArrayResponse.getGetTimings().getOpen());
        holder.mOpenTiming.setAdapter(adapterOpen);
        if (!mList.get(position).getOpen().equals(null)) {
            int spinnerPosition = adapterOpen.getPosition(mList.get(position).getOpen());
            holder.mOpenTiming.setSelection(spinnerPosition);
        }
        else {
            int spinnerPosition = adapterOpen.getPosition("09:00 AM");
            holder.mOpenTiming.setSelection(spinnerPosition);
        }

        ArrayAdapter<String> adapterClosed = new ArrayAdapter<String>(context, R.layout.layout_spinner_remark,timingArrayResponse.getGetTimings().getClose());
        holder.mCloseTimimg.setAdapter(adapterClosed);


        if (!mList.get(position).getClose().equals(null)) {
            int spinnerPosition = adapterClosed.getPosition(mList.get(position).getClose());
            holder.mCloseTimimg.setSelection(spinnerPosition);
        }
        else {
            int spinnerPosition = adapterClosed.getPosition("07:00 PM");
            holder.mCloseTimimg.setSelection(spinnerPosition);
        }

        if(mList.get(position).isIs_closed()){
            holder.isChecked.setChecked(true);
        }
        else {
            holder.isChecked.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class SettingsBusinessTimingsHolder extends RecyclerView.ViewHolder{
        TextView mDay;
        Spinner mOpenTiming,mCloseTimimg;
        public  ArrayList<TimingsBE> mList;
        CheckBox isChecked;

        public SettingsBusinessTimingsHolder(View itemView, final ArrayList<TimingsBE> mList) {
            super(itemView);
            this.mList=mList;
            mDay= (TextView) itemView.findViewById(R.id.day_name);
            mOpenTiming= (Spinner) itemView.findViewById(R.id.open_timing);
            mCloseTimimg= (Spinner) itemView.findViewById(R.id.close_timing);
            isChecked= (CheckBox) itemView.findViewById(R.id.is_closed);

            isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        mList.get(getLayoutPosition()).setIs_closed(true);
                        mOpenTiming.setEnabled(false);
                        mCloseTimimg.setEnabled(false);
                        mOpenTiming.setAdapter(null);
                        mCloseTimimg.setAdapter(null);
                    }
                    else {
                        ArrayAdapter<String> adapterOpen = new ArrayAdapter<String>(context, R.layout.layout_spinner_remark,timingArrayResponse.getGetTimings().getOpen());
                        mOpenTiming.setAdapter(adapterOpen);

                        ArrayAdapter<String> adapterClosed = new ArrayAdapter<String>(context, R.layout.layout_spinner_remark,timingArrayResponse.getGetTimings().getClose());
                        mCloseTimimg.setAdapter(adapterClosed);

                        mOpenTiming.setEnabled(true);
                        mCloseTimimg.setEnabled(true);
                        mList.get(getLayoutPosition()).setIs_closed(false);
                    }
                }
            });

            mOpenTiming.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code herei
                    mList.get(getLayoutPosition()).setOpen(mOpenTiming.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            mCloseTimimg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    // your code herei
                    mList.get(getLayoutPosition()).setClose(mCloseTimimg.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

        }
    }
}
