package autroid.business.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.appcompat.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.androidadvance.topsnackbar.TSnackbar;
//import com.qiscus.sdk.Qiscus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autroid.business.R;
import autroid.business.storage.PreferenceManager;

import static android.util.DisplayMetrics.DENSITY_DEFAULT;


public class Utility {


    public static final String EMPTY_STRING = "";
    public static int MAX_WIDTH = 220;
    public static int MAX_HEIGHT = 158;


    public static final String HOCKEY_APP_ID = "259c1d2715394654a13957b13f35d94d";

    public static String STREMAILADDREGEX = "^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; //EMAIL REGEX

    public static String cameraPermissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private static String AuthToken;
    private static String mPushwooshRegisterId = "";
    public static final int REQUEST_CAMERA_IMAGE = 100;

    public static String getDeviceId() {
        String deviceId = null;
        if (deviceId == null) {
            // If all else fails, if the user does have lower than API 9 (lower
            // than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
            // returns 'null', then simply the ID returned will be solely based
            // off their Android device information. This is where the collisions
            // can happen.
            // Thanks http://www.pocketmagic.net/?p=1662!
            // Try not to use DISPLAY, HOST or ID - these items could change.
            // If there are collisions, there will be overlapping data
            String m_szDevIDShort = "35" +
                    (Build.BOARD.length() % 10)
                    + (Build.BRAND.length() % 10)
                    + (Build.CPU_ABI.length() % 10)
                    + (Build.DEVICE.length() % 10)
                    + (Build.MANUFACTURER.length() % 10)
                    + (Build.MODEL.length() % 10)
                    + (Build.PRODUCT.length() % 10);

            // Thanks to @Roman SL!
            // http://stackoverflow.com/a/4789483/950427
            // Only devices with API >= 9 have android.os.Build.SERIAL
            // http://developer.android.com/reference/android/os/Build.html#SERIAL
            // If a user upgrades software or roots their phone, there will be a duplicate entry
            String serial = null;
            try {
                serial = Build.class.getField("SERIAL").get(null).toString();
                // Go ahead and return the serial for api => 9
                deviceId = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
                return deviceId;
            } catch (Exception e) {
                // String needs to be initialized
                serial = "icreon"; // some value
            }
            // Thanks @Joe!
            // http://stackoverflow.com/a/2853253/950427
            // Finally, combine the values we have found by using the UUID class to create a unique identifier
            deviceId = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            return deviceId;
        } else {
            return deviceId;
        }
    }

    public static String getDeviceModelName() {
        String deviceModelName;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            deviceModelName = capitalize(model);
        } else {
            deviceModelName = capitalize(manufacturer) + " " + model;
        }
        return deviceModelName;
    }

    public static String getDeviceName() {
        String deviceName;
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            deviceName = myDevice.getName();
            if (deviceName == null || deviceName.equals("")) {
                deviceName = getDeviceModelName();
            }
        } catch (Exception e) {
            deviceName = getDeviceModelName();
        }
        return deviceName;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static String getDeviceManufacturerName() {
        return Build.MANUFACTURER.toLowerCase(Locale.getDefault());
    }

    public static String getAppVersion(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (Exception e) {
            return "1.0";
        }
    }

    /**
     * Method will convert the string to base64 format
     *
     * @param txtString, needs to be base64 encoded
     * @return
     */

    public static String convertBase64(String txtString) {
        try {
            return Base64.encodeToString(txtString.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (Exception e) {
            return "";
        }
    }

    public static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static String getAuthToken(Context appContext) {
        if (AuthToken == null || AuthToken.equals("")) {
            AuthToken = PreferenceManager.getInstance()
                    .getStringPreference(appContext,
                            PreferenceManager.AUTHORIZATION);
        }
        return AuthToken;
    }

    public static void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

   /* public static String getDeviceToken(Context context) {
        if (mPushwooshRegisterId.equals(""))
            mPushwooshRegisterId = PreferenceManager.getInstance().getStringPreference(context.getApplicationContext(), Consts.KEY_GCM_PUSH_DEVICE_TOKEN);//"";//GCMRegistrar.getRegistrationId(context);
        return mPushwooshRegisterId;

    }*/

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity activity, View view) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getWindowWidth(Activity mcontext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mcontext.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    /**
     * Method will set the tint color on imageview with colorid
     *
     * @param context
     * @param resourceId
     * @param colourId
     * @return
     */
    public static Drawable setTintOnDrawable(Context context, int resourceId, int colourId) {
        Drawable drawable = AppCompatResources.getDrawable(context, resourceId);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable.mutate(), ContextCompat.getColor(context, colourId));
        return drawable;
    }


    public static boolean validatePassword(String pswd) {
        //Password  contain atleast one number or symbol, must contain both lower and upper case letters
        //

        // ^(?=.*[a-z])(?=.*[A-Z])(?=.*[$@$!%*?&\d])$
        // String pswdRegex="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]$";

        String pswdRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=0-9]).{8,}$";


        Pattern pattern = Pattern.compile(pswdRegex);
        Matcher matcher = pattern.matcher(pswd);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }

        //everything has passed so far, lets return valid

    }

    public static boolean isEmailValid(View view, String email) {

        Pattern pattern = Pattern.compile(STREMAILADDREGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        } else {
            showResponseMessage(view, "Please enter a valid mail");
        }

        return false;
    }

    /**
     * Method will show the snackbar for response received from server
     *
     * @param view    instance of view on which snackbar will display
     * @param message message to be display in snackbar
     */
    public static void showResponseMessage(View view, String message) {
        try {
            TSnackbar snackbar = TSnackbar.make(view, message, TSnackbar.LENGTH_SHORT);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.rgb(255, 0, 0));
            TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();
        } catch (NullPointerException e) {

        } catch (Exception e) {

        }
    }

    public static String convertTimeStampTodate(String timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(timeStamp) * 1000));
        return dateString;
    }

    public static void share(Context context, String sharingData) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingData);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.app_name)));
    }

    public static void shareImage(String url, final Context context, final String sharingData) {
        try {
            Picasso.with(context).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, context));
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    i.putExtra(android.content.Intent.EXTRA_TEXT, sharingData);
                    context.startActivity(Intent.createChooser(i, "Share Image"));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Uri getLocalBitmapUri(Bitmap bmp, final Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            // bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(context, "com.careager.suite.fileprovider", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


    public static void onChatClick(String id, Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        try {
//            Qiscus.buildChatWith(id) //here we use email as userID. But you can make it whatever you want.
//                    .build(context, new Qiscus.ChatActivityBuilderListener() {
//                        @Override
//                        public void onSuccess(Intent intent) {
//                            progressDialog.dismiss();
//                            context.startActivity(intent);
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            //do anything if error occurs
//                            progressDialog.dismiss();
//                            throwable.printStackTrace();
//                            throwable.getLocalizedMessage();
//                        }
//                    });
        } catch (Exception e) {
        }
    }

    public static void onCallClick(String contact, Context context) {
        try {
            if (contact != null && contact.length() == 10) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", contact, null));
                context.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getLocalizedMessage();
        }
    }

    public static String splitCamelCase(String s) {
        return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }


    public static String getYouTubeId(String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "error";
        }
    }

    public static String periodCalculator(Integer integer) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -integer);
        String date = DateFormat.getDateInstance().format(calendar.getTime()) + " To ";
        calendar.add(Calendar.DAY_OF_YEAR, +integer);
        date += DateFormat.getDateInstance().format(calendar.getTime());

        return date;
    }

    public static String getSpecificDate(int year, int month, int day) {

        String minDate = year + "-" + month + "-" + day;

        Date dateFormat = null;

        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat);
        return DateFormat.getDateInstance().format(calendar.getTime());
    }

    public static void whatsAppChat(String phone,Context context){
        try {
            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=+91"+phone);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);

        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
