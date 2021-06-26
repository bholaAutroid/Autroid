package autroid.business.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.BookingPackagesBE;


/**
 * Created by pranav.mittal on 02/10/18.
 */

public class ServicesListAdapter extends BaseExpandableListAdapter {
    private Context context;

    private String coinType[] = {"Coin Earned", "Coin Used"};

    public ArrayList<BookingPackagesBE> mList;
    OnRealmImageClickCallback onRealmImageClickCallback;

    public ServicesListAdapter(Context context, ArrayList<BookingPackagesBE> mList, OnRealmImageClickCallback onRealmImageClickCallback) {
        this.mList = mList;
        this.context = context;
        this.onRealmImageClickCallback=onRealmImageClickCallback;
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
            mDropArrow.setBackgroundResource(R.drawable.up_arrow_show);

        } else {
            mDropArrow.setBackgroundResource(R.drawable.down_arrow_hide);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_service, null);
        }


        TextView mServiceName,mServiceDetail,mServicePrice,mServiceCost,mDoorStepService,mServiceLabour,mServicePart;
        RelativeLayout mMainLayout;

        mServiceName=convertView.findViewById(R.id.service_name);
        mServiceDetail=convertView.findViewById(R.id.service_detail);
        mServiceCost=convertView.findViewById(R.id.service_cost);
        mServicePrice=convertView.findViewById(R.id.service_price);
        mDoorStepService=convertView.findViewById(R.id.doorstep);

        mServiceLabour=convertView.findViewById(R.id.labour_price);
        mServicePart=convertView.findViewById(R.id.part_cost);

        mMainLayout=convertView.findViewById(R.id.main_layout);


        mServiceName.setText(mList.get(i).getServices().get(i1).getService());
       // mServiceDetail.setText(mList.get(i).getServices().get(i1).getDescription());
        mServicePrice.setText("₹ "+mList.get(i).getServices().get(i1).getCost());
        mServiceCost.setText("₹ "+mList.get(i).getServices().get(i1).getMrp());

        mServiceLabour.setText("Labour ₹"+mList.get(i).getServices().get(i1).getLabour_cost());
        mServicePart.setText("Part ₹"+mList.get(i).getServices().get(i1).getPart_cost());
        mServiceCost.setPaintFlags(mServiceCost.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(mList.get(i).getServices().get(i1).getDoorstep()){
            mDoorStepService.setText(Html.fromHtml("<b>(Doorstep Available)</b>"));
        }
        else {
            mDoorStepService.setText("");
        }


        mServiceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRealmImageClickCallback.onDetailClick(i,mList.get(i).getServices().get(i1).getDescription());
            }
        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
