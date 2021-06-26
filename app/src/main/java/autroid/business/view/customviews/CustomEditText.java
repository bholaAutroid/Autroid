package autroid.business.view.customviews;

import android.content.Context;
import android.graphics.PorterDuff;

import com.google.android.material.textfield.TextInputEditText;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

import autroid.business.R;
import autroid.business.utils.FontUtils;


public class CustomEditText extends TextInputEditText {

    private Context context;
    private int mTextSizeInSp = 14;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());

        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSizeInSp);
        this.getBackground().mutate().setColorFilter(ContextCompat.getColor(context,R.color.lightgrey), PorterDuff.Mode.SRC_ATOP);
    }


}