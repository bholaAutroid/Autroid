package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.MultipleUserSelectCallback;
import autroid.business.model.response.UserResponseData;


public class MultipleUserAdapter extends RecyclerView.Adapter<MultipleUserAdapter.SelectedPackagesAdapterHolder> {

    public ArrayList<UserResponseData> mList;
    Context context;
    MultipleUserSelectCallback mBookingCategoryCallback;

    public MultipleUserAdapter(Context context, ArrayList<UserResponseData> mList, MultipleUserSelectCallback mBookingCategoryCallback) {
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
        holder.mName.setText(mList.get(position).getName()+" ("+mList.get(position).getAccount_info().getType()+")");
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
                    mBookingCategoryCallback.onUserClick(mList.get(getLayoutPosition()));
                    break;
            }
        }
    }
}
