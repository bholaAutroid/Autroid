package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.SearchUserBE;
import autroid.business.view.customviews.CircleImageView;

public class SearchUserAdapter  extends RecyclerView.Adapter<SearchUserAdapter.SearchUserHolder> {

    Context context;
    ArrayList<SearchUserBE> mList;

    public SearchUserAdapter(Context mContext, ArrayList<SearchUserBE> mList){
        context=mContext;
        this.mList=mList;
    }

    @Override
    public SearchUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_search_user, parent, false);
        return new SearchUserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchUserHolder holder, int position) {
        holder.mName.setText(mList.get(position).getName());
        holder.mMobile.setText(mList.get(position).getContact_no());
        Picasso.with(context).load(mList.get(position).getAvatar_address()).placeholder(R.drawable.placeholder_thumbnail).into(holder.mImg);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class SearchUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName,mMobile;
        CircleImageView mImg;

        public SearchUserHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.user_info);
            mMobile= (TextView) itemView.findViewById(R.id.user_mobile);
            mImg = (CircleImageView) itemView.findViewById(R.id.review_image_one);


            mName.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.user_info:
                        //mLeadsCallback.onStatusClick(mList.get(getLayoutPosition()).getId());
                        break;
                }

        }
    }


}
