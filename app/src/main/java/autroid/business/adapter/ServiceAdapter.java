package autroid.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.AddServicePriceCallback;
import autroid.business.model.bean.ServiceCategoryBE;

/**
 * Created by pranav.mittal on 1/26/2017.
 */

public class ServiceAdapter extends BaseExpandableListAdapter {

    Context _context;
    ArrayList<ServiceCategoryBE> base;
    AddServicePriceCallback mAddServicePriceCallback;

    public ServiceAdapter(Context context, ArrayList<ServiceCategoryBE> base,AddServicePriceCallback mAddServicePriceCallback){
        _context=context;
        this.base=base;
        this.mAddServicePriceCallback=mAddServicePriceCallback;
    }

    @Override
    public int getGroupCount() {
        return base.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
         return base.get(groupPosition).getServices().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return base.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return base.get(groupPosition).getServices().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_services_group, null);

        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.service_category);


        lblListHeader.setText(base.get(groupPosition).getCategory());

        ImageView lblImage= (ImageView) convertView.findViewById(R.id.group_indicater);


        if(groupPosition != -1) {
            int imageResourceId = isExpanded ? R.drawable.ic_expand_minus : R.drawable.ic_expandable_plus;
            lblImage.setImageResource(imageResourceId);
        }




        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_service_child, null);


        }

        TextView service_sub_category = (TextView) convertView
                .findViewById(R.id.service_sub_category);
        final EditText service_price = (EditText) convertView
                .findViewById(R.id.service_price);
        ImageButton btn_add_service= (ImageButton) convertView.findViewById(R.id.btn_add_service);

        btn_add_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price=service_price.getText().toString();
                if(price!=null)
                {
                    if(price.trim().length()>0){
                     //   base.get(groupPosition).getServices().get(childPosition).setPrice(price);
                       // mAddServicePriceCallback.addServicePrice(base.get(groupPosition).getServices().get(childPosition).getId(),price);
                        //callWS(base.get(groupPosition).getServices().get(childPosition).getId()+"",price);
                    }
                }

            }
        });


        //service_sub_category.setText(base.get(groupPosition).getServices().get(childPosition).getName());
       // service_price.setText(base.get(groupPosition).getServices().get(childPosition).getPrice());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



}
