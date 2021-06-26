package autroid.business.adapter.leads;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.LeadsCallback;
import autroid.business.model.realm.LeadsAssignedRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class LeadsAssignedAdapter extends RealmRecyclerViewAdapter<LeadsAssignedRealm, LeadsAssignedAdapter.LeadsHolder> {


    Context context;
    LeadsCallback leadsCallback;

    public LeadsAssignedAdapter(@Nullable OrderedRealmCollection<LeadsAssignedRealm> data, boolean autoUpdate, LeadsCallback leadsCallback) {
        super(data, autoUpdate);
        this.leadsCallback=leadsCallback;
    }


    @NonNull
    @Override
    public LeadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context=parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_all_lead, parent, false);
        return new LeadsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeadsHolder holder, int i) {
        LeadsAssignedRealm obj=getItem(i);
        holder.leadsAssignedRealm=obj;
        holder.mDate.setText(obj.getCreatedAt());
        holder.mSource.setText(obj.getSource());
        holder.mStatus.setText(obj.getStatus());

        if(!obj.getName().equals(""))holder.mName.setText(obj.getName());
        else holder.mName.setText("Name Not Assigned");

        if(!obj.getContactNo().equals(""))holder.mContact.setText(obj.getContactNo());
        else holder.mContact.setVisibility(View.GONE);

        holder.mRemark.setText("Remark: "+obj.getAssignee_remark());
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
        AppCompatImageView btnImportant;
        CardView cardView;

        LeadsAssignedRealm leadsAssignedRealm;

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
            cardView=itemView.findViewById(R.id.main_layout);
            cardView.setOnClickListener(this);
//            btnImportant=itemView.findViewById(R.id.img_important);
            btnImportant.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                    leadsCallback.onStatusClick(leadsAssignedRealm.getId());
                    break;
//                case R.id.img_important:
//                    leadsCallback.onImportantClick(leadsAssignedRealm.getId());
//                    break;
            }
        }
    }

}
