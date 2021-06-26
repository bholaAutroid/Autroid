package autroid.business.adapter.purchase;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.tokenautocomplete.TokenCompleteTextView;

import autroid.business.R;
import autroid.business.model.bean.CarItemsBE;


/**
 * Created by pranav.mittal on 01/18/18.
 */

public class SearchModelAdapter extends TokenCompleteTextView<CarItemsBE> {


    Context context;

    public SearchModelAdapter(Context context) {
        super(context);
        this.context = context;
    }
    public SearchModelAdapter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(CarItemsBE object) {
        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.row_search_model, (ViewGroup) getParent(), false);
        view.setText(object.getValue());

        return view;
    }

    @Override
    public void addObject(CarItemsBE object) {
        super.addObject(object);
    }

    @Override
    protected CarItemsBE defaultObject(String completionText) {
        int index = completionText.indexOf('@');
        if (index == -1) {
            return new CarItemsBE(completionText, completionText.replace(" ", "") + "@example.com");
        } else {
            return new CarItemsBE(completionText.substring(0, index), completionText);
        }
    }
}
