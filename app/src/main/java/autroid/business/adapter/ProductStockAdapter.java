package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import autroid.business.R;
import autroid.business.interfaces.OnClickCallBack;
import autroid.business.model.realm.ProductRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


/**
 * Created by pranav.mittal on 06/11/17.
 */

public class ProductStockAdapter extends RealmRecyclerViewAdapter<ProductRealm, ProductStockAdapter.ProductStockHolder> {

    Context context;
    private static OnClickCallBack mOnClickCallBack;


    public ProductStockAdapter(@Nullable OrderedRealmCollection<ProductRealm> data, boolean autoUpdate, OnClickCallBack mOfferClickCallback) {
        super(data, autoUpdate);
        this.mOnClickCallBack=mOfferClickCallback;
    }

    @Override
    public ProductStockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_product_stock, parent, false);

        return new ProductStockHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductStockHolder holder, int position) {
        ProductRealm productRealm=getItem(position);
        holder.productRealm=productRealm;

        holder.mProductName.setText(productRealm.getTitle());
        holder.mProductPrice.setText("₹ "+productRealm.getPrice());
        holder.mPublisherName.setText(productRealm.getBusinessName());
        holder.mProductCode.setText(productRealm.getDescription());
        holder.mPublisherAddress.setText(productRealm.getCategory());
        holder.mDate.setText("Added on: "+productRealm.getCreated_at());


        if(!productRealm.isPublish()){
            holder.mUnpublish.setText("Publish");
        }
        else {
            holder.mUnpublish.setText("Unpublish");
        }

        if(productRealm.getMedia().size()>0) {
            holder.mImagesCount.setText("1/" + productRealm.getMedia().size());
            Picasso.with(context).load(productRealm.getMedia().get(0).getPath()).placeholder(R.drawable.placeholder_big).into(holder.mProductImage);

        }else {
            holder.mImagesCount.setText("NA");
            Picasso.with(context).load(R.drawable.placeholder_big).fit().placeholder(R.drawable.placeholder_big).into(holder.mProductImage);
        }
    }


    public  class ProductStockHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mProductPrice,mProductName,mPublisherName,mPublisherAddress,mProductCode,mDate,mImagesCount;
        Button mUnpublish,mEdit,mShare;
        ImageView mProductImage;
        LinearLayout llImgNxt;
        ProductRealm productRealm;

        public ProductStockHolder(View itemView) {
            super(itemView);
            mProductPrice= (TextView) itemView.findViewById(R.id.product_price);
            mProductName= (TextView) itemView.findViewById(R.id.product_name);
            mPublisherName= (TextView) itemView.findViewById(R.id.publisher_name);
            mPublisherAddress= (TextView) itemView.findViewById(R.id.publisher_address);
            mProductCode= (TextView) itemView.findViewById(R.id.product_code);
            mDate= (TextView) itemView.findViewById(R.id.added_date);
            mImagesCount=itemView.findViewById(R.id.images_count);

            mUnpublish= (Button) itemView.findViewById(R.id.product_unpublish);
            mEdit= (Button) itemView.findViewById(R.id.product_edit);
            mShare= (Button) itemView.findViewById(R.id.product_share);
            mProductImage= (ImageView) itemView.findViewById(R.id.product_image);
            llImgNxt= (LinearLayout) itemView.findViewById(R.id.ll_img_nxt);

            mProductImage.setOnClickListener(this);
            mUnpublish.setOnClickListener(this);
            mShare.setOnClickListener(this);
            mEdit.setOnClickListener(this);
            llImgNxt.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
            case R.id.product_image:
             mOnClickCallBack.onImageClick(productRealm.getId());
            break;
            case R.id.ll_img_nxt:
            //mOnClickCallBack.onImageClick(getLayoutPosition());
            break;
            case R.id.product_unpublish:
            if(!productRealm.isPublish()){
                this.mPublisherAddress.setText("Publish");
            }
            else {
                this.mPublisherAddress.setText("Unpublish");
            }
            mOnClickCallBack.onPublishUnPublishClick(productRealm.getId());
            break;
            case R.id.product_edit:
                mOnClickCallBack.onEditButtonClick(productRealm.getId(),"");
                break;
            case R.id.product_share:
                 mOnClickCallBack.onShareButtonClick(productRealm.getId());
                break;

        }

        }
    }
}
