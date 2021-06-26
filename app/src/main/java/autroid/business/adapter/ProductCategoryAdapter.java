package autroid.business.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.ProductCatSubcatBE;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryHolder> {

    Context context;
    ArrayList<ProductCatSubcatBE> mList;

    public ProductCategoryAdapter(Context context,ArrayList<ProductCatSubcatBE> mList){
        this.context=context;
        this.mList=mList;
    }

    @Override
    public ProductCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_automaker, parent, false);

        return new ProductCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductCategoryHolder holder, int position) {

        holder.mName.setText(mList.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ProductCategoryHolder extends RecyclerView.ViewHolder{
        TextView mName;
        public ProductCategoryHolder(View itemView) {
            super(itemView);
            mName= (TextView) itemView.findViewById(R.id.automaker_name);

        }
    }
}
