package autroid.business.view.customviews;

import android.content.Context;
import android.util.AttributeSet;

import autroid.business.R;


/**
 * Created by pranav.mittal on 4/4/2017.
 */

public class CustomCheckbox extends androidx.appcompat.widget.AppCompatCheckBox {



    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }
    @Override
    public void setChecked(boolean t){
        if(t)
        {

            this.setBackgroundResource(R.drawable.background_transparent);
        }
        else
        {
            this.setBackgroundResource(R.drawable.background_transparent);
        }
        super.setChecked(t);
    }
}
