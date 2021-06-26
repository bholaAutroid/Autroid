package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.bean.ShowroomProfileBE;


/**
 * Created by pranav.mittal on 12/24/17.
 */

public class SearchBusinessAdapter extends RecyclerView.Adapter<SearchBusinessAdapter.ServiceProviderOthersHolder> {

    private ArrayList<ShowroomProfileBE> mList;
    Context context;
    private OnClickBusinessCallback mUserClickCallback;
    public SearchBusinessAdapter(ArrayList<ShowroomProfileBE> mList, Context context, OnClickBusinessCallback mUserClickCallback){
        this.mList=mList;
        this.context=context;
        this.mUserClickCallback=mUserClickCallback;
    }

    @Override
    public ServiceProviderOthersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_businesses, parent, false);

        return new ServiceProviderOthersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ServiceProviderOthersHolder holder, int position) {
        holder.mShowroom.setText(mList.get(position).getName());
        holder.mLocation.setText(mList.get(position).getAddress().getLocation());
        holder.mCategory.setText(mList.get(position).getBusiness_info().getCategory());

        Picasso.with(context).load(mList.get(position).getAvatar_address()).placeholder(R.drawable.placeholder_big).into(holder.mCover);

       /* if(mList.get(position).getTotal_rating()!=null) {
            holder.mRatings.setRating(mList.get(position).getTotal_rating());
        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public  class ServiceProviderOthersHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mShowroom,mLocation,mCategory;
        LinearLayout mLayout;
        ImageView mCover;


        public ServiceProviderOthersHolder(View itemView) {
            super(itemView);
            mShowroom=itemView.findViewById(R.id.showroom_name);
            mCategory=itemView.findViewById(R.id.showroom_category);
            mLocation=itemView.findViewById(R.id.showroom_location);
            mLayout=itemView.findViewById(R.id.main_layout);
            mLayout.setOnClickListener(this);
            mCover=itemView.findViewById(R.id.showroom_cover);

        }

        @Override
        public void onClick(View view) {
            mUserClickCallback.onBusinessClick(mList.get(getLayoutPosition()).getId());
        }
    }
}
