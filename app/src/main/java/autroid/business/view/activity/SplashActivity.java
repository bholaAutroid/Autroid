package autroid.business.view.activity;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import autroid.business.R;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        final String token=preferenceManager.getStringPreference(getApplicationContext(), Constant.SP_TOKEN);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                //AppSignatureHelper appSignatureHelper=new AppSignatureHelper(SplashActivity.this);

                if(token!=null && token.length()>0){
                    Utility.setAuthToken(token);
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();
                }
                else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }
}
