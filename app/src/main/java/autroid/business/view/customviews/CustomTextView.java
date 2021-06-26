package autroid.business.view.customviews;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import autroid.business.utils.FontUtils;


public class CustomTextView extends AppCompatTextView {

    private Context context;

    public CustomTextView(Context context) {
        super(context);
        FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());
        this.context = context;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

                FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());

    }

    public void setFontFace(FontUtils.FontType fontFace) {
        FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());
    }

}
