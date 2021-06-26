package autroid.business.adapter.leads;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.RemarkBE;

public class LeadRemarkAdapter extends RecyclerView.Adapter<LeadRemarkAdapter.LeadRemarkHolder> {

    Context context;

    public ArrayList<RemarkBE> mList;

    public LeadRemarkAdapter(Context context, ArrayList<RemarkBE> mList){
        this.mList=mList;
        this.context=context;
    }


    @NonNull
    @Override
    public LeadRemarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_lead_remaks, parent, false);
        return new LeadRemarkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadRemarkHolder holder, int position) {
        holder.mDate.setText(mList.get(position).getUpdated_at());
        holder.mStatus.setText(mList.get(position).getStatus());
        holder.mRemark.setText(mList.get(position).getAssignee_remark());
        holder.mAssignee.setText(mList.get(position).getAssignee().getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class LeadRemarkHolder extends RecyclerView.ViewHolder{

        TextView mDate,mStatus,mRemark,mAssignee;

        public LeadRemarkHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.date);
            mStatus = itemView.findViewById(R.id.status);
            mRemark=itemView.findViewById(R.id.remark);
            mAssignee=itemView.findViewById(R.id.assignee);
        }
    }
}
