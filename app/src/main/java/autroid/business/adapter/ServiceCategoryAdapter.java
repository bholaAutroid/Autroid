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

import autroid.business.R;
import autroid.business.interfaces.CategoryClickCallback;

/**
 * Created by pranav.mittal on 08/05/17.
 */

public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.ServiceCategoryHolder> {

    Context context;
    String categoryName[];
    int categoryIcon[];
    CategoryClickCallback mCategoryClickCallback;

    public ServiceCategoryAdapter(Context mContext,String categoryName[],int categoryIcon[],CategoryClickCallback mCategoryClickCallback){
            context=mContext;
        this.categoryName=categoryName;
        this.categoryIcon=categoryIcon;
        this.mCategoryClickCallback=mCategoryClickCallback;
    }

    @Override
    public ServiceCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_service_category, parent, false);

        return new ServiceCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ServiceCategoryHolder holder, int position) {
        holder.mName.setText(categoryName[position]);
        Picasso.with(context).load(categoryIcon[position]).placeholder(R.drawable.placeholder_thumbnail).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return categoryIcon.length;
    }

    public  class ServiceCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName;
        ImageView mImage;
        LinearLayout llCategory;
        public ServiceCategoryHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.category_name);
          //  mImage= (ImageView) itemView.initializeViews(R.id.category_icon);
            //llCategory= (LinearLayout) itemView.initializeViews(R.id.category_layout);
            llCategory.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
              /*  case R.id.category_layout:
                    mCategoryClickCallback.categoryPos(getLayoutPosition());
                    break;*/
            }
        }
    }
}
