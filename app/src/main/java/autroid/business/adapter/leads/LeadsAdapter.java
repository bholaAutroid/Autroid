package autroid.business.adapter.leads;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.realm.LeadsRealm;
import autroid.business.utils.Constant;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class LeadsAdapter extends RealmRecyclerViewAdapter<LeadsRealm, LeadsAdapter.LeadsHolder> {

    Context context;
    LeadsCallback leadsCallback;

    public LeadsAdapter(@Nullable OrderedRealmCollection<LeadsRealm> data, boolean autoUpdate,LeadsCallback leadsCallback) {
        super(data, autoUpdate);
        this.leadsCallback=leadsCallback;
    }

    @NonNull
    @Override
    public LeadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_all_lead, parent, false);
        return new LeadsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadsHolder holder, int i) {

        LeadsRealm obj=getItem(i);
        holder.leadsRealm=obj;
        holder.mDate.setText(obj.getCreatedAt());
        holder.mSource.setText(obj.getSource());
        holder.mStatus.setText(obj.getStatus());

        if(obj.getStatus().equals(Constant.OPEN))holder.mStatus.setVisibility(View.GONE);

        if(!obj.getName().equals(""))holder.mName.setText(obj.getName());
        else holder.mName.setText("Name Not Assigned");

        if(!obj.getContactNo().equals(""))holder.mContact.setText(obj.getContactNo());
        else holder.mContact.setVisibility(View.GONE);

        holder.mRemark.setText("Remark: "+obj.getAssignee_remark());

//        holder.mContact.setVisibility(View.VISIBLE);


//
//        if(!obj.getAssignee_remark().trim().equals("")) {
//            holder.mAssigneeRemark.setVisibility(View.VISIBLE);
//            holder.mAssigneeRemark.setText("Assignee Remark: " + obj.getAssignee_remark());
//        } else holder.mAssigneeRemark.setVisibility(View.GONE);
      //  holder.mEmail.setText("Email: "+obj.getEmail());

//        if(obj.getContactNo().equals(""))holder.btnCall.setVisibility(View.GONE);
//
//        if(obj.getUserId()!=null) holder.btnChat.setVisibility(View.VISIBLE);
//        else holder.btnChat.setVisibility(View.GONE);

//        if(obj.getImportant()){
//           // holder.btnImportant.setBackgroundResource(R.drawable.ic_star_small);
//            holder.btnImportant.setColorFilter(ContextCompat.getColor(context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
//        }
//        else {
//            holder.btnImportant.setColorFilter(ContextCompat.getColor(context, R.color.gray_color), android.graphics.PorterDuff.Mode.SRC_IN);
//        }
    }

   /* @Override
    public void onBindViewHolder(@NonNull LeadsHolder holder, int position) {


        holder.mDate.setText("Date: "+mList.get(position).getDate());
        holder.mName.setText("Name: "+mList.get(position).getVendorInfoBE().getName());
        holder.mContact.setText("Contact No: "+mList.get(position).getVendorInfoBE().getContact_no());
        holder.mStatus.setText("Status: "+mList.get(position).getStatus());
        holder.mSource.setText("Source: "+mList.get(position).getLabel());

    }
*/

    public class LeadsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mDate,mName,mContact,mStatus,mSource,mStatusColor,mRemark,mAssigneeRemark,mEmail;
        AppCompatImageView  btnImportant;
        ConstraintLayout mainLayout;

        LeadsRealm leadsRealm;
        public LeadsHolder(View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.date);
            mName = itemView.findViewById(R.id.name);
            mContact = itemView.findViewById(R.id.contact);
            mEmail=itemView.findViewById(R.id.email);
            mEmail.setVisibility(View.GONE);
            mSource = itemView.findViewById(R.id.source);
            mStatus = itemView.findViewById(R.id.status);
            mRemark=itemView.findViewById(R.id.remark);
            mainLayout=itemView.findViewById(R.id.main_layout);
            mainLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                    leadsCallback.onStatusClick(leadsRealm.getId());
                    break;
            }
        }
    }
}
