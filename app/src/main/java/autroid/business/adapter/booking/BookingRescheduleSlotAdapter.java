package autroid.business.adapter.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.BookingSlotBE;


public class BookingRescheduleSlotAdapter extends ArrayAdapter<BookingSlotBE>
{

        private Context ctx;
        private ArrayList<BookingSlotBE> base;

    public BookingRescheduleSlotAdapter(Context context, int resource, ArrayList<BookingSlotBE> base, String defaulText) {
        super(context,  R.layout.row_spinner_slot, R.id.spinnerTextView, base);
        this.ctx = context;
        this.base = base;
        BookingSlotBE carItemsBE=new BookingSlotBE();
        carItemsBE.setSlot(defaulText);
        base.add(0,carItemsBE);
        //base.get(0).setValue(defaulText);
    }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }



        public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_spinner_slot, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(base.get(position).getSlot());
        textView.setEnabled(base.get(position).getStatus());
        if(position!=0) {
            if (!base.get(position).getStatus()) {
                textView.setOnClickListener(null);
                textView.setText(base.get(position).getSlot()+" (Full)");
            }
            else {

            }
        }
        return row;

    }
}
