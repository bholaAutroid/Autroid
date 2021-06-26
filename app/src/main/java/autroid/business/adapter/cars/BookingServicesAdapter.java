package autroid.business.adapter.cars;

import android.content.Context;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.BookingServicesBE;


public class BookingServicesAdapter extends RecyclerView.Adapter<BookingServicesAdapter.BookingPackageseHolder> {


    public ArrayList<BookingServicesBE> mList;
    Context context;
    OnRealmImageClickCallback onRealmImageClickCallback;
    BookingCategoryCallback mBookingCategoryCallback;
    String tag;

    public BookingServicesAdapter(Context context, ArrayList<BookingServicesBE> mList, OnRealmImageClickCallback onRealmImageClickCallback, BookingCategoryCallback mBookingCategoryCallback, String tag) {
        this.mList = mList;
        this.context = context;
        this.onRealmImageClickCallback=onRealmImageClickCallback;
        this.mBookingCategoryCallback=mBookingCategoryCallback;
        this.tag=tag;
    }

    @NonNull
    @Override
    public BookingPackageseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_booking_service, viewGroup, false);

        return new BookingPackageseHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingPackageseHolder bookingPackageseHolder, int i) {

        bookingPackageseHolder.mService.setText(mList.get(i).getService());
        bookingPackageseHolder.mServiceDetail.setVisibility(View.VISIBLE);

       /* if(mList.get(i).getDoorstep()!=null)
        if(mList.get(i).getDoorstep()){
            bookingPackageseHolder.mDoorStepService.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mDoorStepService.setText(Html.fromHtml("<b>(Doorstep Available)</b>"));
        }
        else {
            bookingPackageseHolder.mDoorStepService.setVisibility(View.GONE);
        }*/

        if(mList.get(i).getCost()>0){
            bookingPackageseHolder.llPrice.setVisibility(View.VISIBLE);
            bookingPackageseHolder.mServicePrice.setText("₹ "+mList.get(i).getCost());

            if(mList.get(i).getMrp()>0) {
                bookingPackageseHolder.mServiceCost.setText("₹ " + mList.get(i).getMrp());
                bookingPackageseHolder.mServiceCost.setPaintFlags(bookingPackageseHolder.mServiceCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        }
        else {
            bookingPackageseHolder.llPrice.setVisibility(View.GONE);
        }


        if(mList.get(i).getGallery()>0){
            bookingPackageseHolder.mServiceGallery.setVisibility(View.VISIBLE);
        }
        else {
            bookingPackageseHolder.mServiceGallery.setVisibility(View.GONE);

        }


      //  bookingPackageseHolder.mService.setChecked(mList.get(i).getChecked());

        if(mList.get(i).getChecked()){
            bookingPackageseHolder.llCBServices.setChecked(true);

            // bookingPackageseHolder.mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.gray_color));

            //   viewGroup.setBackgroundColor(context.getResources().getColor(R.color.red_color));
            if(mList.get(i).getUnit()!=null)
                if(mList.get(i).getUnit().trim().length()>0){
                    bookingPackageseHolder.mLLQuantity.setVisibility(View.VISIBLE);
                    bookingPackageseHolder.mUnit.setText(mList.get(i).getQuantity()+"");
                }
                else {
                    bookingPackageseHolder.mLLQuantity.setVisibility(View.GONE);
                }

        }
        else {
            //bookingPackageseHolder.mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

            bookingPackageseHolder.mLLQuantity.setVisibility(View.GONE);
            bookingPackageseHolder.llCBServices.setChecked(false);
        }

        if(mList.get(i).getIcon()!=null && mList.get(i).getIcon().length()>0) {
            bookingPackageseHolder.mImage.setVisibility(View.VISIBLE);

            Picasso.with(context).load(mList.get(i).getIcon()).placeholder(R.drawable.placeholder_thumbnail).into(bookingPackageseHolder.mImage);
        }
        else {
           // bookingPackageseHolder.llPrice.setPadding(70,0,0,0);
            bookingPackageseHolder.mImage.setVisibility(View.GONE);
          //  bookingPackageseHolder.mServiceDetail.setPadding(100,0,0,0);

        }


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public  class BookingPackageseHolder extends RecyclerView.ViewHolder{
        TextView mService;
        ImageView mImage;
        ImageButton mPlus,mMinus;
        TextView mServiceDetail,mServiceGallery;
        TextView mUnit,mCoupon;
        LinearLayout mLLQuantity;
        AppCompatCheckBox llCBServices;
        TextView mServiceName,mServicePrice,mServiceCost,mDoorStepService;
        RelativeLayout mMainLayout,llPrice;

        public BookingPackageseHolder(View itemView) {
            super(itemView);
            mService=itemView.findViewById(R.id.check_service);
            mServiceName=itemView.findViewById(R.id.service_name);
            mServiceDetail=itemView.findViewById(R.id.service_detail);
            mServicePrice=itemView.findViewById(R.id.service_price);
            mServiceCost=itemView.findViewById(R.id.service_cost);
            mImage=itemView.findViewById(R.id.part_image);
            mDoorStepService=itemView.findViewById(R.id.doorstep);
            llPrice=itemView.findViewById(R.id.llservice);
            llCBServices=itemView.findViewById(R.id.services_selected);
            mCoupon=itemView.findViewById(R.id.coupon);

            mPlus=itemView.findViewById(R.id.add);
            mMinus=itemView.findViewById(R.id.minus);
            mUnit=itemView.findViewById(R.id.unit);
            mLLQuantity=itemView.findViewById(R.id.ll_quantity);
            mServiceGallery=itemView.findViewById(R.id.service_gallery);



            mPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity=mList.get(getLayoutPosition()).getQuantity();
                    quantity=quantity+1;
                    mList.get(getLayoutPosition()).setQuantity(quantity);
                    mUnit.setText(mList.get(getLayoutPosition()).getQuantity()+"");
                }
            });

            mMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity=mList.get(getLayoutPosition()).getQuantity();
                    if(quantity>1) {
                        quantity = quantity - 1;
                        mList.get(getLayoutPosition()).setQuantity(quantity);
                        mUnit.setText(mList.get(getLayoutPosition()).getQuantity() + "");
                    }
                    else {
                        llCBServices.setChecked(false);
                        mList.get(getLayoutPosition()).setChecked(false);
                        mLLQuantity.setVisibility(View.GONE);
                        mBookingCategoryCallback.onServicesUnselect(mList.get(getLayoutPosition()).getId());
                    }
                }
            });

            mMainLayout=itemView.findViewById(R.id.main_layout);
            mMainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(getLayoutPosition()).getChecked()){
                        llCBServices.setChecked(false);
                     //   mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                        mList.get(getLayoutPosition()).setChecked(false);
                        mLLQuantity.setVisibility(View.GONE);
                        mBookingCategoryCallback.onServicesUnselect(mList.get(getLayoutPosition()).getId());

                    }
                    else {
                      //  mMainLayout.setBackgroundColor(context.getResources().getColor(R.color.gray_color));
                        mBookingCategoryCallback.onServicesSelected(mList.get(getLayoutPosition()).getId(),mList.get(getLayoutPosition()).getService(),tag,mList.get(getLayoutPosition()).getType(),mList.get(getLayoutPosition()).getCost(),mList.get(getLayoutPosition()).getMrp(),mList.get(getLayoutPosition()).getQuantity(),mList.get(getLayoutPosition()).getUnit(),mList.get(getLayoutPosition()).getDoorstep());
                        llCBServices.setChecked(true);
                        mList.get(getLayoutPosition()).setChecked(true);
                        if(mList.get(getLayoutPosition()).getUnit()!=null)
                            if(mList.get(getLayoutPosition()).getUnit().trim().length()>0){
                                mLLQuantity.setVisibility(View.VISIBLE);
                                mUnit.setText(mList.get(getLayoutPosition()).getQuantity()+"");
                            }
                    }
                }
            });


            llCBServices.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(getLayoutPosition()).getChecked()){
                        llCBServices.setChecked(false);
                        mList.get(getLayoutPosition()).setChecked(false);
                        mLLQuantity.setVisibility(View.GONE);
                        mBookingCategoryCallback.onServicesUnselect(mList.get(getLayoutPosition()).getId());


                    }
                    else {
                        llCBServices.setChecked(true);
                        mList.get(getLayoutPosition()).setChecked(true);
                        mBookingCategoryCallback.onServicesSelected(mList.get(getLayoutPosition()).getId(),mList.get(getLayoutPosition()).getService(),tag,mList.get(getLayoutPosition()).getType(),mList.get(getLayoutPosition()).getCost(),mList.get(getLayoutPosition()).getMrp(),mList.get(getLayoutPosition()).getQuantity(),mList.get(getLayoutPosition()).getUnit(),mList.get(getLayoutPosition()).getDoorstep());

                        if(mList.get(getLayoutPosition()).getUnit()!=null)
                            if(mList.get(getLayoutPosition()).getUnit().trim().length()>0){
                                mLLQuantity.setVisibility(View.VISIBLE);
                                mUnit.setText(mList.get(getLayoutPosition()).getQuantity()+"");
                            }
                    }
                }
            });

           mServiceDetail.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   onRealmImageClickCallback.onDetailClick(mList.get(getLayoutPosition()).getId(),mList.get(getLayoutPosition()).getType());
               }
           });

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRealmImageClickCallback.onImageClick(getLayoutPosition(),mList.get(getLayoutPosition()).getIcon());
                }
            });

            mServiceGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBookingCategoryCallback.onGalleryClick(mList.get(getLayoutPosition()).getId(),mList.get(getLayoutPosition()).getType());
                }
            });

            /*
            mService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mList.get(getLayoutPosition()).getChecked()){
                        mService.setChecked(false);
                        mList.get(getLayoutPosition()).setChecked(false);
                    }
                    else {
                        mService.setChecked(true);
                        mList.get(getLayoutPosition()).setChecked(true);
                    }
                }
            });
*/

        }

    }
}
