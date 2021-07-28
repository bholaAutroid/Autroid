package autroid.business.aws.navigation.adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.aws.AwsHomeActivity;

public class SettingsActivity extends AppCompatActivity {

    Toolbar comman_tool;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_settings );


        comman_tool=findViewById( R.id.setting_tool );
        setSupportActionBar( comman_tool );

       ImageView imageBack= findViewById(R.id.setting_tool).findViewById(R.id.iv_bacAerro);

       imageBack.setOnClickListener( v->{
           Intent intent = new Intent(getApplicationContext(), AwsHomeActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);
           finish();
       } );
    }
}