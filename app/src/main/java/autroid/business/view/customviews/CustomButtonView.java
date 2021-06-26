package autroid.business.view.customviews;

import android.content.Context;
import android.os.Build;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;

import autroid.business.R;
import autroid.business.utils.FontUtils;
import autroid.business.utils.Utility;


public class CustomButtonView extends AppCompatButton {

    private Context context;
    private int textsize=14;
    public CustomButtonView(Context context) {
        super(context);
        FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());
        this.context = context;
    }

    public CustomButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextColor(getResources().getColor(R.color.selector_white_button_text_color));
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP,textsize);
        this.setBackground(ContextCompat.getDrawable(context, R.drawable.white_button_bg));
        //this.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_btnbackground));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setStateListAnimator(null);
            this.setElevation(3);
           /* ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    // Or read size directly from the view's width/height
                    int size = getResources().getDimensionPixelSize(R.dimen.dimen_10dp);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        outline.setRoundRect(size, size, size, size, 5);
                }
            };
            setOutlineProvider(viewOutlineProvider);*/
        }
        int dp=(int) Utility.convertDpToPixel(4, this.getContext());
        this.setPadding(dp, dp,dp,dp);

                FontUtils.setFontFace(context, this, FontUtils.FontType.RobotoMedium.toString());

    }

}
