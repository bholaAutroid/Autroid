package autroid.business.adapter.cars;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.response.CarEagerCoinsResponse;


/**
 * Created by pranav.mittal on 12/28/17.
 */

public class TransactionAdapter extends BaseExpandableListAdapter {
    private Context context;

    private String coinType[]={"Earned","Used"};

    ArrayList<CarEagerCoinsResponse.GetCoinsData> mList;

    public TransactionAdapter(Context context, ArrayList<CarEagerCoinsResponse.GetCoinsData> mList){
        this.mList=mList;
        this.context=context;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mList.get(i).getTransactions().size();
    }

    @Override
    public Object getGroup(int i) {
        return mList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mList.get(i).getTransactions().get(i1);
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
            convertView = infalInflater.inflate(R.layout.row_transactions, null);
        }

        TextView mMonthName=convertView.findViewById(R.id.month_name);
        ImageView mDropArrow=convertView.findViewById(R.id.arrow_down_up);
        mMonthName.setText(mList.get(i).getMonth());

        if(b){
            mDropArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);

        }
        else {
            mDropArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down);
        }
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_transaction_child, null);
        }

        TextView mActivity=convertView.findViewById(R.id.activity_text);
        TextView mDate=convertView.findViewById(R.id.date_text);
        TextView mAmount=convertView.findViewById(R.id.amount_text);
        TextView mTransaction=convertView.findViewById(R.id.transaction_type);
        mActivity.setText(mList.get(i).getTransactions().get(i1).getTag());

        if(mList.get(i).getTransactions().get(i1).getType().equals("credit")){
            mAmount.setText("+ ₹"+mList.get(i).getTransactions().get(i1).getPoints());
            mTransaction.setText(coinType[0]);
            mAmount.setTextColor(Color.rgb(50,205,50));
            mTransaction.setTextColor(Color.rgb(50,205,50));
        }
        else {
            mAmount.setText("- ₹"+mList.get(i).getTransactions().get(i1).getPoints());
            mTransaction.setText(coinType[1]);
            mAmount.setTextColor(Color.RED);
            mTransaction.setTextColor(Color.RED);
        }

        try {
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            Date date = simpleDateFormat.parse(mList.get(i).getTransactions().get(i1).getUpdated_at());
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);*/
            mDate.setText(mList.get(i).getTransactions().get(i1).getCreated_at()+"");

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
