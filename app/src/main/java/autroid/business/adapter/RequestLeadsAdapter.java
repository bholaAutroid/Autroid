package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.RequestLeadBE;

/**
 * Created by pranav.mittal on 08/10/17.
 */

public class RequestLeadsAdapter extends RecyclerView.Adapter<RequestLeadsAdapter.RequestLeadsHolder> {

    private ArrayList<RequestLeadBE> mList;
    Context context;
    public RequestLeadsAdapter(ArrayList<RequestLeadBE> mList){
        this.mList=mList;
    }

    @Override
    public RequestLeadsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_leads, parent, false);

        return new RequestLeadsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestLeadsHolder holder, int position) {
        holder.tvName.setText(mList.get(position).getName());
        holder.tvDate.setText(mList.get(position).getCreated_at());
        holder.tvSubject.setText(mList.get(position).getSubject());
        holder.tvContent.setText(mList.get(position).getQuote());

        Picasso.with(context).load(mList.get(position).getAvatar_url()).fit().placeholder(R.drawable.placeholder_big).into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class RequestLeadsHolder extends RecyclerView.ViewHolder{
        TextView tvName,tvDate,tvSubject,tvContent;
        ImageView ivImage;


        public RequestLeadsHolder(View itemView) {
            super(itemView);
            tvName= (TextView) itemView.findViewById(R.id.txt_name);
            tvDate= (TextView) itemView.findViewById(R.id.txt_date);
            tvSubject= (TextView) itemView.findViewById(R.id.txt_subject);
            tvContent= (TextView) itemView.findViewById(R.id.txt_content);
            ivImage= (ImageView) itemView.findViewById(R.id.user_image);
        }
    }
}
