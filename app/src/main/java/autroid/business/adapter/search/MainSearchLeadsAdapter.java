package autroid.business.adapter.search;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.SearchUserDetailCallback;
import autroid.business.model.bean.LeadsBE;
import autroid.business.utils.Constant;

public class MainSearchLeadsAdapter extends RecyclerView.Adapter<MainSearchLeadsAdapter.MyViewHolder> {

    Context context;
    private ArrayList<LeadsBE> arrayList;
    private SearchUserDetailCallback mainSearchListener;


    public MainSearchLeadsAdapter(ArrayList<LeadsBE> arrayList, SearchUserDetailCallback mainSearchListener) {
        this.arrayList=arrayList;
        this.mainSearchListener=mainSearchListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView date,name,contact,source,status,remark,assigneeRemark,email;
        ImageButton btnChat,btnCall;
/*
        AppCompatImageView btnImportant;
*/

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v->{
               if(mainSearchListener!=null){
                   Bundle bundle=new Bundle();
                   bundle.putString(Constant.STATUS,arrayList.get(getAdapterPosition()).getRemark().getStatus());
                   mainSearchListener.onLeadClick(arrayList.get(getAdapterPosition()).getId());
               }
            });
            date = itemView.findViewById(R.id.date);
            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
            email=itemView.findViewById(R.id.email);
            status = itemView.findViewById(R.id.status);
            source = itemView.findViewById(R.id.source);
            remark=itemView.findViewById(R.id.remark);
            //assigneeRemark=itemView.findViewById(R.id.assignee_remark);
//            statusColor=itemView.findViewById(R.id.status_color);
          //  btnChat=itemView.findViewById(R.id.chat_btn);
            //btnChat.setVisibility(View.GONE);
          //  btnCall=itemView.findViewById(R.id.call_btn);
            //btnCall.setVisibility(View.GONE);
           // btnImportant=itemView.findViewById(R.id.img_important);
           // btnImportant.setVisibility(View.GONE);
        }

        public void bind(int position){
            name.setText(arrayList.get(position).getName());
            date.setText(arrayList.get(position).getUpdated_at());
            contact.setText(arrayList.get(position).getContact_no());
            email.setText(arrayList.get(position).getEmail());
            status.setText(arrayList.get(position).getRemark().getStatus());
           // source.setText("Source: "+arrayList.get(position).getType());
            remark.setText("Remark: "+arrayList.get(position).getRemark().getAssignee_remark());
//            if(!arrayList.get(position).getRemark().getAssignee_remark().trim().equals(""))assigneeRemark.setText("Assignee Remark: "+arrayList.get(position).getRemark().getAssignee_remark());
//            else assigneeRemark.setVisibility(View.GONE);

//            if(arrayList.get(position).getI!=null) btnChat.setVisibility(View.VISIBLE);
//            else btnChat.setVisibility(View.GONE);
//

         /*   if(arrayList.get(position).getImportant()) btnImportant.setColorFilter(ContextCompat.getColor(context, R.color.yellow), android.graphics.PorterDuff.Mode.SRC_IN);
            else btnImportant.setColorFilter(ContextCompat.getColor(context, R.color.gray_color), android.graphics.PorterDuff.Mode.SRC_IN);*/


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        context=viewGroup.getContext();
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_all_lead,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
