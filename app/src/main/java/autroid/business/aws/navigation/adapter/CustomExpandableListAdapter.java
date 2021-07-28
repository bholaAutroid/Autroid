package autroid.business.aws.navigation.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;
import java.util.Map;

import autroid.business.R;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mExpandableListTitle;
    private Map<String, List<String>> mExpandableListDetail;
    private LayoutInflater mLayoutInflater;

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       Map<String, List<String>> expandableListDetail) {
        mContext = context;
        mExpandableListTitle = expandableListTitle;
        mExpandableListDetail = expandableListDetail;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mExpandableListDetail.get( mExpandableListTitle.get( listPosition ) ).get( expandedListPosition );
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild( listPosition, expandedListPosition );
        if (convertView == null) {
            convertView = mLayoutInflater.inflate( R.layout.list_item, null );
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById( R.id.expandedListItem );

        ImageView iv_items = convertView.findViewById( R.id.list_iv_item );
        expandedListTextView.setText( expandedListText );

        if (expandedListText.equals( "Business Overview" )) {
            iv_items.setImageResource( R.drawable.ic_business_overview );
        }
        if (expandedListText.equals( "Customer Overview" )) {
            iv_items.setImageResource( R.drawable.ic_business_overview );
        }
        if (expandedListText.equals( "Team Performance" )) {
            iv_items.setImageResource( R.drawable.ic_add_service );
        }
        if (expandedListText.equals( "Leads" )) {
            iv_items.setImageResource( R.drawable.ic_leads );
        }
        if (expandedListText.equals( "Leads Generation" )) {
            iv_items.setImageResource( R.drawable.ic_leads_generation );
        }
        if (expandedListText.equals( "Service" )) {
            iv_items.setImageResource( R.drawable.ic_book_service );
        }
        if (expandedListText.equals( "Parties" )) {
            iv_items.setImageResource( R.drawable.ic_drawer_leads );
        }
        if (expandedListText.equals( "Payment In" )) {
            iv_items.setImageResource( R.drawable.ic_rupee_indian );
        }
        if (expandedListText.equals( "Payment Out" )) {
            iv_items.setImageResource( R.drawable.ic_rupee_indian );
        }
        if (expandedListText.equals( "Purchase Orders" )) {
            iv_items.setImageResource( R.drawable.ic_home );
        }
        if (expandedListText.equals( "Bills" )) {
            iv_items.setImageResource( R.drawable.ic_invoice );
        }
        if (expandedListText.equals( "Sales Orders" )) {
            iv_items.setImageResource( R.drawable.ic_orders );
        }
        if (expandedListText.equals( "Invoices" )) {
            iv_items.setImageResource( R.drawable.ic_invoice );
        }
        if (expandedListText.equals( "Jobs" )) {
            iv_items.setImageResource( R.drawable.ic_jobs );
        }
        if (expandedListText.equals( "Booking" )) {
            iv_items.setImageResource( R.drawable.ic_booking );
        }
        if (expandedListText.equals( "Stock" )) {
            iv_items.setImageResource( R.drawable.ic_stock );
        }
        if (expandedListText.equals( "Business" )) {
            iv_items.setImageResource( R.drawable.ic_round_business_24 );
        }
        if (expandedListText.equals( "Integration" )) {
            iv_items.setImageResource( R.drawable.ic_book_service );
        }
        if (expandedListText.equals( "Offer & Coupons" )) {
            iv_items.setImageResource( R.drawable.ic_offers );
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return mExpandableListDetail.get( mExpandableListTitle.get( listPosition ) )
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return mExpandableListTitle.get( listPosition );
    }

    @Override
    public int getGroupCount() {
        return mExpandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup( listPosition );
        if (convertView == null) {
            convertView = mLayoutInflater.inflate( R.layout.list_group, null );
        }
        TextView listTitleTextView = (TextView) convertView.findViewById( R.id.listTitle );
        listTitleTextView.setTypeface( null, Typeface.NORMAL );
        listTitleTextView.setText( listTitle );
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
