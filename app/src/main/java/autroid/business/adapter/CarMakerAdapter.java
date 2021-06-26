package autroid.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarMakerBE;

/**
 * Created by pranav.mittal on 08/15/17.
 */

public class CarMakerAdapter extends ArrayAdapter<CarMakerBE> {

    private Context ctx;
    private String[] contentArray;
    private String[] imageArray;
    private ArrayList<CarMakerBE> base;

    public CarMakerAdapter(Context context, int resource, ArrayList<CarMakerBE> base, String defaulText) {
        super(context, R.layout.row_spinner_layout, R.id.spinnerTextView, base);
        this.ctx = context;
        this.base = base;
        CarMakerBE carItemsBE = new CarMakerBE();
        carItemsBE.setMaker(defaulText);
        base.add(0, carItemsBE);
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

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_spinner_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(base.get(position).getMaker());


        return row;

    }
}