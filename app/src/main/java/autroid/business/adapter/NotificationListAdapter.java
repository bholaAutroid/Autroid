package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.NotificationListBE;
import autroid.business.view.customviews.CircleImageView;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationListHolder> {

    Context context;
    public ArrayList<NotificationListBE> mList;

    public NotificationListAdapter(Context context, ArrayList<NotificationListBE> mList){
        this.mList=mList;
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_notification_list, parent, false);

        return new NotificationListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListHolder holder, int position) {

        if(mList.get(position).getSender()!=null){
            Picasso.with(context).load(mList.get(position).getSender().getAvatar_address()).placeholder(R.drawable.placeholder_thumbnail).into(holder.mUserImage);
        }

        holder.mTitle.setText(mList.get(position).getTitle());
        holder.mBody.setText(mList.get(position).getBody());

        holder.mDate.setText(mList.get(position).getCreated_at());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class NotificationListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView mUserImage;
        TextView mTitle,mDate,mBody;
        LinearLayout mMainLayout;

        public NotificationListHolder(View itemView) {
            super(itemView);
            mUserImage=itemView.findViewById(R.id.user_img);
            mTitle=itemView.findViewById(R.id.title);
            mBody=itemView.findViewById(R.id.body);
            mDate=itemView.findViewById(R.id.date_text);
            mMainLayout=itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.main_layout:
                    break;
            }

        }
    }
}
