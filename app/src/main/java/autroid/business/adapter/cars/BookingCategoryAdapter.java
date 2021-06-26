package autroid.business.adapter.cars;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import autroid.business.R;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.model.realm.BookingCategoryRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class BookingCategoryAdapter extends RealmRecyclerViewAdapter<BookingCategoryRealm, BookingCategoryAdapter.BookingCategoryHolder> {

    Context context;
    BookingCategoryCallback mBookingCategoryCallback;
    ImageView selectedView;
    Boolean isExpanded;
    static int viewExpanded=1;
    static int viewCollapsed=2;

    public BookingCategoryAdapter(@Nullable OrderedRealmCollection<BookingCategoryRealm> data, boolean autoUpdate, Context context, BookingCategoryCallback mBookingCategoryCallback, Boolean isExpanded) {
        super(data, autoUpdate);
        this.context=context;
        this.mBookingCategoryCallback=mBookingCategoryCallback;
        this.isExpanded=isExpanded;
    }

    @Override
    public int getItemViewType(int position) {
        if(isExpanded)
            return viewExpanded;
        else return viewCollapsed;
    }

    @Override
    public BookingCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=null;
        switch (viewType) {
            case 1:
                itemView = LayoutInflater.
                    from(context).
                    inflate(R.layout.row_booking_category, parent, false);
            break;
            case 2:
                itemView = LayoutInflater.from(context).inflate(R.layout.row_new_category, parent, false);
                break;
                default:
                    itemView = LayoutInflater.
                            from(context).
                            inflate(R.layout.row_new_category, parent, false);
                    break;

        }


        return new BookingCategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookingCategoryHolder holder, final int position) {
        BookingCategoryRealm categoryRealm=getItem(position);
        holder.bookingCategoryRealm=categoryRealm;
        holder.mName.setText(categoryRealm.getTitle());

        if(categoryRealm.getIcon()!=null && categoryRealm.getIcon().length()>0)
            Picasso.with(context).load(categoryRealm.getIcon()).placeholder(R.drawable.placeholder_thumbnail).into(holder.ivImage);

        if(categoryRealm.getEnabled()) {
            if (categoryRealm.getSelected()) {
                ImageViewCompat.setImageTintList(holder.ivImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                holder.llView.setBackgroundColor(context.getResources().getColor(R.color.red_color));
                holder.mName.setTextColor(context.getResources().getColor(R.color.white_color));
            } else {
                ImageViewCompat.setImageTintList(holder.ivImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_color_button)));
                holder.llView.setBackgroundColor(context.getResources().getColor(R.color.gray_color_button));
                holder.mName.setTextColor(context.getResources().getColor(R.color.white_dark));
                }
        }
        else {
            ImageViewCompat.setImageTintList(holder.ivImage, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
            holder.llView.setBackgroundColor(context.getResources().getColor(R.color.black));
            holder.mName.setTextColor(context.getResources().getColor(R.color.black ));
        }
    }


    public class BookingCategoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mName;
        LinearLayout mMainlayout;
        ImageView ivImage;
        View llView;
        BookingCategoryRealm bookingCategoryRealm;

        public BookingCategoryHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.category_name);
            mMainlayout=itemView.findViewById(R.id.main_layout);
            ivImage= (ImageView) itemView.findViewById(R.id.icon_category);
            mMainlayout.setOnClickListener(this);
            llView=itemView.findViewById(R.id.ll_view);

           /* mMainlayout.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onSwipeTop() {
                }
                public void onSwipeRight() {
                    mBookingCategoryCallback.onRightSwipe();

                }
                public void onSwipeLeft() {
                }
                public void onSwipeBottom() {
                }

            });
*/

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                   /* if(selectedView==null){

                        selectedView=ivImage;
                       // selectedView.setColorFilter(ContextCompat.getColor(context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
                        ImageViewCompat.setImageTintList(selectedView, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));


                        //selectedView.setBackgroundColor(context.getResources().getColor(R.color.card_color));

                    }
                    else {
                        ImageViewCompat.setImageTintList(selectedView, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray_color_button)));
                        selectedView=ivImage;
                        ImageViewCompat.setImageTintList(selectedView, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));

                    }*/

                    if(bookingCategoryRealm.getEnabled())
                        mBookingCategoryCallback.onCategoryClick(bookingCategoryRealm.getTitle(),bookingCategoryRealm.getTag(),bookingCategoryRealm.getNested());
                    else
                        mBookingCategoryCallback.onDisableCategoryClick(bookingCategoryRealm.getTitle(),bookingCategoryRealm.getTag(),bookingCategoryRealm.getNested());

                    break;
            }
        }
    }
}
