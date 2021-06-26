package autroid.business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.TelephoneCodeBE;

/**
 * Created by pranav.mittal on 02/09/18.
 */

public class CountryCodeSpinnerAdapter extends ArrayAdapter<TelephoneCodeBE> {

    private Context ctx;
    private String[] contentArray;
    private String[] imageArray;
    private ArrayList<TelephoneCodeBE> base;
    private int resource;

    public CountryCodeSpinnerAdapter(Context context, int resource, ArrayList<TelephoneCodeBE> base) {
        super(context, R.layout.row_spinner_layout, R.id.spinnerTextView, base);
        this.ctx = context;
        this.base = base;
        this.resource = resource;

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
        View row = inflater.inflate(resource, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinnerTextView);
        textView.setText(base.get(position).getIsoAlpha2()+"("+base.get(position).getTelephoneCode()+")");


        return row;

    }
}

