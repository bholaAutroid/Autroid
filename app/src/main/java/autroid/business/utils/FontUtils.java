package autroid.business.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import java.util.EnumMap;
import java.util.Map;

public final class FontUtils {
    private FontUtils() {
    }

    public enum FontType {
        RobotoMedium("fonts/Roboto-Regular.ttf");

        private final String path;

        FontType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * Method will cache the loaded typeface
     */
    private static final Map<FontType, Typeface> typefaceCache = new EnumMap<>(
            FontType.class);


    /**
     * Method creates the typeface and put it into the cache
     *
     * @param context
     * @param fontType type of font to be add into the cache
     * @return
     */
    public static Typeface getTypeface(Context context, FontType fontType) {
        String fontPath = fontType.getPath();

        if (!typefaceCache.containsKey(fontType)) {
            typefaceCache.put(fontType,
                    Typeface.createFromAsset(context.getAssets(), fontPath));
        }

        return typefaceCache.get(fontType);
    }

    /**
     * method to set the defined font to a specific view
     * @param context
     * @param view
     */
    public static void setFontFace(Context context, View view, String typeface) {
        ((TextView) view).setTypeface(getTypeface(context, FontType.valueOf(typeface)));
    }

}
