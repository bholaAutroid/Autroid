package autroid.business.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import autroid.business.R;

public class HelpPageActivity extends AppCompatActivity {

    YouTubePlayer tubePlayer1,tubePlayer2,tubePlayer3;

    private static final int REQUEST_CALL=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);


//        tubePlayer1=findViewById( R.id.youtube_leads );
//        tubePlayer2=findViewById( R.id.youtube_sales );
//        tubePlayer3=findViewById( R.id.youtubePlay );


    }

    public void OpenWhatsApp(View view) {
        callWhatsApp();
    }

    public void openWhatsApp(View view) {
        callWhatsApp();
    }


    public void openEmail(View view) {
        openEmail();
    }

    public void openEmailChat(View view) {
        openEmail();
    }

    public void opnePhone(View view) {
        callUs();
    }

    public void opnePhoneCall(View view) {
        callUs();
    }

    public void openEmail() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "care@autroid.com", null));
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));

        } catch (Exception exception) {
            Toast.makeText(this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void callWhatsApp() {
        boolean installed = appInstalledorNot("com.whatsapp");
        if (installed) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + "+91" + "8710006161"
                    + "&text=" + ""));
            startActivity(intent);
        } else {
            Toast.makeText(this, "WhatsApp not installed on your Phone", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean appInstalledorNot(String uri) {
        PackageManager packageManager = getPackageManager();
        boolean appInstalled;
        try {
            packageManager.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
            e.printStackTrace();
        }
        return appInstalled;
    }

    public void callUs() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(HelpPageActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            String s = "tel:" + "8710006161";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(s));
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_CALL){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                callUs();
            }else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}