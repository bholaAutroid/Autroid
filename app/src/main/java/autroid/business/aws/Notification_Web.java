package autroid.business.aws;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import autroid.business.R;
import autroid.business.aws.navigation.adapter.WebNotificationPresenter;
import autroid.business.aws.navigation.adapter.WebNotificationResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.presenter.LoginPresenter;
import autroid.business.utils.Constant;
import autroid.business.view.activity.OTPActivity;
import butterknife.BindView;

public class Notification_Web extends AppCompatActivity {

    WebNotificationPresenter mPresenter;
    ConstraintLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification_web );

        mLayout=findViewById( R.id.main_layout_wenNoti );


        mPresenter = new WebNotificationPresenter( this,mLayout);
        mPresenter.getWebNotifications();

    }

    public void onWebNotificationSuccess(WebNotificationResponse webNotificationResponse) {

        if (webNotificationResponse!=null) {

            Toast.makeText( this, "i am running..", Toast.LENGTH_SHORT ).show();

            Toast.makeText( this, ""+webNotificationResponse.getResponseMessage(), Toast.LENGTH_SHORT ).show();
//            Toast.makeText( this, ""+webNotificationResponse.getTitle(), Toast.LENGTH_SHORT ).show();
//            Toast.makeText( this, ""+webNotificationResponse.getBody(), Toast.LENGTH_SHORT ).show();
//            Toast.makeText( this, ""+webNotificationResponse.getBody(), Toast.LENGTH_SHORT ).show();

        }

    }
}