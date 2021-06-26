package autroid.business.adapter.cars;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.PurchasedPackageCallback;
import autroid.business.model.bean.BookedPackagesBE;

public class SelectedPackagesAdapter extends RecyclerView.Adapter<SelectedPackagesAdapter.SelectedPackagesAdapterHolder> {

    public ArrayList<BookedPackagesBE> mList;
    Context context;
    PurchasedPackageCallback mBookingCategoryCallback;

    public SelectedPackagesAdapter(Context context, ArrayList<BookedPackagesBE> mList, PurchasedPackageCallback mBookingCategoryCallback) {
        this.mList = mList;
        this.context = context;
        this.mBookingCategoryCallback = mBookingCategoryCallback;
    }

    @Override
    public SelectedPackagesAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_purchased_packages, parent, false);

        return new SelectedPackagesAdapterHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectedPackagesAdapterHolder holder, int position) {
        holder.mName.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SelectedPackagesAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Button mName;

        public SelectedPackagesAdapterHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.package_name);
            mName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.package_name:
                    mBookingCategoryCallback.onPackageClick(mList.get(getLayoutPosition()).getPackages(),mList.get(getLayoutPosition()).getName());
                    break;
            }
        }
    }
}
