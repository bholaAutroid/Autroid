package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.bean.AnalyticsBE;

public class AnalyticsAdapter extends PagerAdapter {

    Context context;

    RecyclerView recyclerView;

    ArrayList<AnalyticsBE> mList;

    AnalyticsDataAdapter analyticsDataAdapter;

    PopupMenu popup;

    MainSearchListener onMenuClickListner; // MainSearchListner is used because it has a single method

    public AnalyticsAdapter(Context context, ArrayList<AnalyticsBE> mList, MainSearchListener onMenuClickListner) {
        this.mList = mList;
        this.context = context;
        this.onMenuClickListner = onMenuClickListner;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView name, role, mViewOption;

        ImageView imageView;

        View itemView = LayoutInflater.from(context).inflate(R.layout.fragment_item_analytics, container, false);

        name = itemView.findViewById(R.id.name);
        role = itemView.findViewById(R.id.role);
        imageView = itemView.findViewById(R.id.individual_picture);
       /* mViewOption = itemView.findViewById(R.id.textViewOptions);

        if (PreferenceManager.getInstance().getStringPreference(context, Constant.SP_CRE).equalsIgnoreCase("admin"))
            mViewOption.setVisibility(View.VISIBLE);
        else mViewOption.setVisibility(View.GONE);

        popup = new PopupMenu(context, mViewOption);
        popup.inflate(R.menu.in_process_menu);
        popup.getMenu().findItem(R.id.quality_check).setTitle("Remove member");

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.quality_check:
                    new AlertDialog.Builder(context)
                            .setTitle("Confirmation Message")
                            .setMessage("Do you want to remove this member")
                            .setPositiveButton("Yes", (dialogInterface, which) -> {
                                onMenuClickListner.onClickSearchItem(mList.get(position).getId(),String.valueOf(position),null);
                            })
                            .setNegativeButton("Cancel", (dialogInterface, which) -> {
                                dialogInterface.dismiss();
                            }).show();
                    break;
            }

            return false;

        });

        mViewOption.setOnClickListener(v -> popup.show());
*/
        recyclerView = itemView.findViewById(R.id.data_list);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));


        name.setText(mList.get(position).getName());
        role.setText("" + mList.get(position).getRole() + "");

        Picasso.with(context).load(mList.get(position).getAvatar_address()).placeholder(R.drawable.ic_user).into(imageView);

        analyticsDataAdapter = new AnalyticsDataAdapter(context, mList.get(position).getAnalytics());
        recyclerView.setAdapter(analyticsDataAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View itemView, @NonNull Object o) {

        return itemView == o;
    }


    /*public class AnalyticsHolder extends RecyclerView.ViewHolder{

        TextView name,role;

        QiscusCircularImageView imageView;

        public AnalyticsHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            imageView = itemView.findViewById(R.id.individual_picture);
            recyclerView=itemView.findViewById(R.id.data_list);
            recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        }

        public void bind(int position){
            name.setText(mList.get(position).getName());
            role.setText(""+mList.get(position).getRole()+"");

            Picasso.with(context).load(mList.get(position).getAvatar_address()).fit().into(imageView);

            analyticsDataAdapter=new AnalyticsDataAdapter(context,mList.get(position).getAnalytics());
            recyclerView.setAdapter(analyticsDataAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }
*/


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ConstraintLayout) object);

    }

}
