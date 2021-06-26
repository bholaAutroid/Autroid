package autroid.business.adapter.cars;

import android.content.Context;
import android.graphics.Paint;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.BookingPackagesBE;


/**
 * Created by pranav.mittal on 02/10/18.
 */

public class BookingNestedServicesAdapter extends BaseExpandableListAdapter {
    private Context context;

    private String coinType[] = {"Coin Earned", "Coin Used"};

    public ArrayList<BookingPackagesBE> mList;
    OnRealmImageClickCallback onRealmImageClickCallback;
    BookingCategoryCallback mBookingCategoryCallback;
    String tag;

    public BookingNestedServicesAdapter(Context context, ArrayList<BookingPackagesBE> mList, OnRealmImageClickCallback onRealmImageClickCallback, BookingCategoryCallback mBookingCategoryCallback, String tag) {
        this.mList = mList;
        this.context = context;
        this.onRealmImageClickCallback=onRealmImageClickCallback;
        this.mBookingCategoryCallback=mBookingCategoryCallback;
        this.tag=tag;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }


    @Override
    public int getChildrenCount(int i) {
        return mList.get(i).getServices().size();
    }

    @Override
    public Object getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mList.get(i).getServices().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_service_category, null);
        }

        TextView mMonthName = convertView.findViewById(R.id.month_name);
        ImageView mDropArrow = convertView.findViewById(R.id.arrow_down_up);
        mMonthName.setText(mList.get(i).getPackages());

        if (b) {
            mDropArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);

        } else {
            mDropArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_booking_service, null);
        }

        final TextView mService;
        ImageButton mPlus,mMinus;
        TextView mServiceName,mServicePrice,mServiceCost,mDoorStepService,mCoupon;
        final TextView mUnit;
        final RelativeLayout mMainLayout,mLLPrice;
        final LinearLayout mLLQuantity;
        TextView mServiceDetail,mServiceGallery;
        final AppCompatCheckBox llCBServices;

        ImageView mImage;
        mService=convertView.findViewById(R.id.check_service);
        mServiceName=convertView.findViewById(R.id.service_name);
        mServiceDetail=convertView.findViewById(R.id.service_detail);
        mServiceCost=convertView.findViewById(R.id.service_cost);
        mServicePrice=convertView.findViewById(R.id.service_price);
        mDoorStepService=convertView.findViewById(R.id.doorstep);
        mPlus=convertView.findViewById(R.id.add);
        mMinus=convertView.findViewById(R.id.minus);
        mUnit=convertView.findViewById(R.id.unit);
        mLLQuantity=convertView.findViewById(R.id.ll_quantity);
        llCBServices=convertView.findViewById(R.id.services_selected);
        mServiceGallery=convertView.findViewById(R.id.service_gallery);
        mLLPrice=convertView.findViewById(R.id.llservice);
        mCoupon=convertView.findViewById(R.id.coupon);

        if(mList.get(i).getServices().get(i1).getGallery()>0){
            mServiceGallery.setVisibility(View.VISIBLE);
        }
        else {
            mServiceGallery.setVisibility(View.GONE);

        }

        mPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=mList.get(i).getServices().get(i1).getQuantity();
                quantity=quantity+1;
                mList.get(i).getServices().get(i1).setQuantity(quantity);
                mUnit.setText(mList.get(i).getServices().get(i1).getQuantity()+"");
                mBookingCategoryCallback.updateQuantity(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getQuantity());
            }
        });

        mMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=mList.get(i).getServices().get(i1).getQuantity();
                if(quantity>1) {
                    quantity = quantity - 1;
                    mList.get(i).getServices().get(i1).setQuantity(quantity);
                    mUnit.setText(mList.get(i).getServices().get(i1).getQuantity() + "");
                    mBookingCategoryCallback.updateQuantity(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getQuantity());

                }
                else {
                    mList.get(i).getServices().get(i1).setChecked(false);
                    llCBServices.setChecked(false);
                    mLLQuantity.setVisibility(View.GONE);
                    mBookingCategoryCallback.onServicesUnselect( mList.get(i).getServices().get(i1).getId());
                }
            }
        });



        mMainLayout=convertView.findViewById(R.id.main_layout);
        mImage=convertView.findViewById(R.id.part_image);
        mImage.setVisibility(View.GONE);

        mService.setText(mList.get(i).getServices().get(i1).getService());


       // mServiceDetail.setText(mList.get(i).getServices().get(i1).getDescription());

        if(mList.get(i).getServices().get(i1).getCost()>0) {
            mLLPrice.setVisibility(View.VISIBLE);
            mServicePrice.setText("₹ " + mList.get(i).getServices().get(i1).getCost());

            if(mList.get(i).getServices().get(i1).getMrp()>0) {
                mServiceCost.setText("₹ " + mList.get(i).getServices().get(i1).getMrp());
                mServiceCost.setPaintFlags(mServiceCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }
        else {
            mLLPrice.setVisibility(View.GONE);
            }



        if(mList.get(i).getServices().get(i1).getDoorstep()){

        }
        else {
            mDoorStepService.setText("");
        }

        if(mList.get(i).getServices().get(i1).getChecked()){
            llCBServices.setChecked(true);
         //   viewGroup.setBackgroundColor(context.getResources().getColor(R.color.red_color));
           // mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));

            if(mList.get(i).getServices().get(i1).getUnit()!=null)
                if(mList.get(i).getServices().get(i1).getUnit().trim().length()>0){
                    mLLQuantity.setVisibility(View.VISIBLE);
                    mUnit.setText(mList.get(i).getServices().get(i1).getQuantity()+"");
                }

        }
        else {
          //  mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

            mLLQuantity.setVisibility(View.GONE);
            llCBServices.setChecked(false);
        }

        mServiceDetail.setVisibility(View.VISIBLE);

        mServiceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRealmImageClickCallback.onDetailClick(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getType());
            }
        });


        llCBServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.get(i).getServices().get(i1).getChecked()){
                    mList.get(i).getServices().get(i1).setChecked(false);
                    llCBServices.setChecked(false);
                    mLLQuantity.setVisibility(View.GONE);

                    mBookingCategoryCallback.onServicesUnselect( mList.get(i).getServices().get(i1).getId());
                }
                else {
                    mList.get(i).getServices().get(i1).setChecked(true);
                    llCBServices.setChecked(true);
                    mBookingCategoryCallback.onServicesSelected(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getService(),tag,mList.get(i).getServices().get(i1).getType(),mList.get(i).getServices().get(i1).getCost(),mList.get(i).getServices().get(i1).getMrp(),mList.get(i).getServices().get(i1).getQuantity(),mList.get(i).getServices().get(i1).getUnit(),mList.get(i).getServices().get(i1).getDoorstep());

                    if(mList.get(i).getServices().get(i1).getUnit()!=null)
                        if(mList.get(i).getServices().get(i1).getUnit().trim().length()>0){
                            mLLQuantity.setVisibility(View.VISIBLE);
                            mUnit.setText(mList.get(i).getServices().get(i1).getQuantity()+"");
                        }
                }
            }
        });

        mMainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mList.get(i).getServices().get(i1).getChecked()){
                    mList.get(i).getServices().get(i1).setChecked(false);
                    llCBServices.setChecked(false);
                    mLLQuantity.setVisibility(View.GONE);
                  //  mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    mBookingCategoryCallback.onServicesUnselect( mList.get(i).getServices().get(i1).getId());

                    }
                else {
                    mList.get(i).getServices().get(i1).setChecked(true);
                   // mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
                   mBookingCategoryCallback.onServicesSelected(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getService(),tag,mList.get(i).getServices().get(i1).getType(),mList.get(i).getServices().get(i1).getCost(),mList.get(i).getServices().get(i1).getMrp(),mList.get(i).getServices().get(i1).getQuantity(),mList.get(i).getServices().get(i1).getUnit(),mList.get(i).getServices().get(i1).getDoorstep());


                    llCBServices.setChecked(true);
                    if(mList.get(i).getServices().get(i1).getUnit()!=null)
                        if(mList.get(i).getServices().get(i1).getUnit().trim().length()>0){
                            mLLQuantity.setVisibility(View.VISIBLE);
                            mUnit.setText(mList.get(i).getServices().get(i1).getQuantity()+"");
                        }
                }
            }
        });

        mServiceGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookingCategoryCallback.onGalleryClick(mList.get(i).getServices().get(i1).getId(),mList.get(i).getServices().get(i1).getType());
            }
        });

       /* if(mList.get(i).getServices().get(i1).getIcon()!=null &&mList.get(i).getServices().get(i1).getIcon().length()>0) {
            mImage.setVisibility(View.VISIBLE);

            Picasso.with(context).load(mList.get(i).getServices().get(i1).getIcon()).placeholder(R.mipmap.ic_def_booking).into(mImage);
        }
        else {

            mImage.setVisibility(View.GONE);
            //  bookingPackageseHolder.mServiceDetail.setPadding(100,0,0,0);

        }*/



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
