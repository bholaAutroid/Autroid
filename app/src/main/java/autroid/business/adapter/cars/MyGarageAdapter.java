package autroid.business.adapter.cars;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import autroid.business.R;
import autroid.business.interfaces.BookServiceClickCallback;
import autroid.business.interfaces.OnCarClickCallBack;
import autroid.business.model.realm.CarStockRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/*
 * Created by pranav.mittal on 09/14/17.
 */

public class MyGarageAdapter extends RealmRecyclerViewAdapter<CarStockRealm, MyGarageAdapter.CarStockHolder> {

    Context context;
    BookServiceClickCallback mBookServiceClickCallback;
    Activity activity;
    OnCarClickCallBack mOnClickCallBack;

    public MyGarageAdapter(@Nullable OrderedRealmCollection<CarStockRealm> data, boolean autoUpdate, BookServiceClickCallback mBookServiceClickCallback, Activity activity, OnCarClickCallBack mOnClickCallBack) {
        super(data, autoUpdate);
        this.mBookServiceClickCallback = mBookServiceClickCallback;
        this.activity = activity;
        this.mOnClickCallBack = mOnClickCallBack;
    }

    @Override
    public CarStockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_my_garage, parent, false);
        return new CarStockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarStockHolder holder, int position) {

        String details="";

        CarStockRealm carStockRealm = getItem(position);

        holder.objCarStockRealm = carStockRealm;

        holder.mCarName.setText(carStockRealm.getModelName());

//          holder.mCarSale.setChecked(carStockRealm.isPublish());
        if(carStockRealm.getYear()!=null)
            if (carStockRealm.getYear().trim().length() != 0)details=carStockRealm.getYear() + " / ";

        if (carStockRealm.getFuelType().trim().length() != 0)details+=carStockRealm.getFuelType();

        if (carStockRealm.getColor().trim().length() != 0)details+=" / "+carStockRealm.getColor();

        holder.mDetails.setText(details);

        if(carStockRealm.isPublish()){
            holder.mPrice.setVisibility(View.VISIBLE);
            holder.mPrice.setText("₹ " + carStockRealm.getPrice());
        }else holder.mPrice.setVisibility(View.GONE);


        if (carStockRealm.isPublish()) holder.mOnOff.setBackgroundResource(R.drawable.ic_on);
        else holder.mOnOff.setBackgroundResource(R.drawable.ic_off);

        if(!carStockRealm.isPublish())holder.mApproved.setVisibility(View.GONE);
        else if(carStockRealm.isPublish() && carStockRealm.isAdminApproved())holder.mApproved.setVisibility(View.GONE);
        else if(carStockRealm.isPublish() && !carStockRealm.isAdminApproved())holder.mApproved.setVisibility(View.VISIBLE);

        if (carStockRealm.getMedia() != null && carStockRealm.getMedia().size() > 0) Picasso.with(context).load(carStockRealm.getMedia().get(0).getPath()).placeholder(R.drawable.ic_placeholder_car).into(holder.mCarImage);
        else Picasso.with(context).load(R.drawable.ic_placeholder_car).placeholder(R.drawable.ic_placeholder_car).into(holder.mCarImage);

    }

    public class CarStockHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mCarName, mBookService,mDetails,mPrice,mViewOption,mApproved;
        ImageView mCarImage, mOnOff,mShare;
        CarStockRealm objCarStockRealm;
        PopupMenu popupMenu;
        ConstraintLayout mMainLayout;
        LinearLayout mSale;

        public CarStockHolder(View itemView) {
            super(itemView);
            mCarName = itemView.findViewById(R.id.car_name);
            mCarImage = itemView.findViewById(R.id.car_image);
            mOnOff = itemView.findViewById(R.id.on_off);
            mBookService = itemView.findViewById(R.id.book_service);
            mDetails = itemView.findViewById(R.id.details);
            mPrice = itemView.findViewById(R.id.price);
            mShare = itemView.findViewById(R.id.share_car);
            mViewOption = itemView.findViewById(R.id.textViewOptions);
            mMainLayout = itemView.findViewById(R.id.main_layout);
            mSale = itemView.findViewById(R.id.sale);
            mApproved= itemView.findViewById(R.id.approved);
            popupMenu = new PopupMenu(context, mViewOption);
            popupMenu.inflate(R.menu.car_sales_menu);

            popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {

                    case R.id.edit_car:
                        if (objCarStockRealm != null) mOnClickCallBack.onEditButtonClick(getLayoutPosition(), objCarStockRealm.getId());
                        break;

                    case R.id.delete_car:
                        if (objCarStockRealm != null) mOnClickCallBack.onDeleteClick(getLayoutPosition(), objCarStockRealm.getId());
                        break;
                }

                return false;
            });

            mViewOption.setOnClickListener(this);
            mSale.setOnClickListener(this);
            mBookService.setOnClickListener(this);
            mShare.setOnClickListener(this);
            mMainLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.book_service:
                    disableButton(mBookService);
                    if (objCarStockRealm != null) mBookServiceClickCallback.carId(objCarStockRealm.getId(), getLayoutPosition(), objCarStockRealm.getModelName());
                    break;

                case R.id.share_car:
                    String content;
                    if (objCarStockRealm != null && objCarStockRealm.getMedia() != null && objCarStockRealm.getMedia().size() > 0) {
                        content = objCarStockRealm.getModelName() + " - " + objCarStockRealm.getFuelType();
                        mOnClickCallBack.onShareButtonClick(getLayoutPosition(), objCarStockRealm.getMedia().get(0).getPath(), content,objCarStockRealm.isPublish());
                    } else if (objCarStockRealm != null && objCarStockRealm.getMedia() != null && objCarStockRealm.getMedia().size() == 0) {
                        content = objCarStockRealm.getModelName() +" - " + objCarStockRealm.getFuelType();
                        content=content+"\n"+" CarEager - Integrated Automotive Ecosystem. Download the App Now! https://goo.gl/fU5Upb";
                        mOnClickCallBack.onShareButtonClick(getLayoutPosition(), "", content,objCarStockRealm.isPublish());
                    }
                    break;

                case R.id.textViewOptions:
                    popupMenu.show();
                    break;

//                case R.id.ll_delete:
//

                case R.id.car_image:
                    if (objCarStockRealm != null) mOnClickCallBack.onImageClick(objCarStockRealm.getId());
                    break;

                case R.id.car_name:
                    if (objCarStockRealm != null) mOnClickCallBack.onTitleClick(getLayoutPosition(), objCarStockRealm.getPublisherName(), objCarStockRealm.getId());
                    break;

                case R.id.sale:
                    mOnClickCallBack.onOffClick(objCarStockRealm.getId(), objCarStockRealm.isPublish());
                    break;

                case R.id.main_layout:
                    if (objCarStockRealm != null) mOnClickCallBack.onTitleClick(getLayoutPosition(), objCarStockRealm.getPublisherName(), objCarStockRealm.getId());
                    break;
//
//                    case R.id.car_sale_switch:
//                        if(objCarStockRealm!=null)
//                            mOnClickCallBack.onSwitchClick(objCarStockRealm.getId(),objCarStockRealm.isPublish());
//                        break;

            }
        }
    }

    private void disableButton(TextView textView){
        textView.setEnabled(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(() -> {
                    textView.setEnabled(true);
                });
            }
        }, 2000);

    }

}
