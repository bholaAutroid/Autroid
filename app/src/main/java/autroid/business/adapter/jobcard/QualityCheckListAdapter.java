package autroid.business.adapter.jobcard;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.JobsQCBE;

public class QualityCheckListAdapter extends RecyclerView.Adapter<QualityCheckListAdapter.MyViewHolder> {

    private ArrayList<JobsQCBE> arrayList;
    private boolean disable;

    public QualityCheckListAdapter(ArrayList<JobsQCBE> arrayList, boolean disable) {
        this.arrayList = arrayList;
        this.disable= disable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        EditText etRemarK;
        AppCompatCheckBox mCBStatus;
        TextView mPoints;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etRemarK = itemView.findViewById(R.id.remark);
            mCBStatus = itemView.findViewById(R.id.cb_status);
            mPoints = itemView.findViewById(R.id.point);

            mCBStatus.setOnClickListener(v -> {
                if (arrayList.get(getLayoutPosition()).getStatus())
                    arrayList.get(getLayoutPosition()).setStatus(false);
                else arrayList.get(getLayoutPosition()).setStatus(true);
            });

            etRemarK.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arrayList.get(getLayoutPosition()).setRemark(s.toString());
                }
            });

            if(disable){
                mCBStatus.setEnabled(false);
                etRemarK.setEnabled(false);
            }
        }

        public void onBind(int position) {
            mPoints.setText(arrayList.get(position).getPoint());
            mCBStatus.setChecked(arrayList.get(position).getStatus());
            etRemarK.setText(arrayList.get(position).getRemark());
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_qc_status, viewGroup, false);
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
