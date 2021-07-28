package autroid.business.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;


import autroid.business.aws.AwsHomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.realm.UserRealm;
import autroid.business.model.request.PhoneVerificationRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.presenter.OTPActivityPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    private static OTPActivity inst;
    @BindView(R.id.et_otp)
    EditText mOtp;
    @BindView(R.id.btn_verify)
    Button mVerify;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.ll_resend_otp)
    LinearLayout mResend;

    OTPActivityPresenter mPresenter;


    @BindView(R.id.otp_msg)
    TextView mMsg;

    String otpMsg="OTP sent to: ";
    String mobileNumber="",userId;

    RealmController mRealmController;
    Realm mRealm;

    private FirebaseAnalytics mFirebaseAnalytics;


    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private Boolean isForgot=false;
    public static OTPActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        ButterKnife.bind(this);

        this.mRealm = RealmController.with(this).getRealm();
        mRealmController=RealmController.getInstance();


        mVerify.setOnClickListener(this);
        mPresenter=new OTPActivityPresenter(this,mMainLayout);
      //  startCountDownTimer(timerTime);
        mResend.setOnClickListener(this);
        mResend.setVisibility(View.GONE);

        Intent intent=getIntent();
        if(intent!=null){
            mobileNumber=intent.getStringExtra(Constant.Key_Mobile);
            mMsg.setText(otpMsg+mobileNumber);
            userId=intent.getStringExtra(Constant.KEY_ID);

            if(intent.hasExtra(Constant.KEY_FORGOT))
            isForgot=intent.getBooleanExtra(Constant.KEY_FORGOT,false);
        }


        if (checkAndRequestPermissions()) {
            // carry on the normal flow, as the case of  permissions  granted.

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    public void smsReceived(final String smsMessage) {
        if(smsMessage!=null){
            if(smsMessage.length()>0){
                mOtp.setText(smsMessage);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verify:
                if(mOtp.getText().toString().length()>0){
                    if(isForgot){

                        PhoneVerificationRequest phoneVerificationRequest = new PhoneVerificationRequest();
                        phoneVerificationRequest.setOtp(mOtp.getText().toString());
                        phoneVerificationRequest.setId(userId);
                        mPresenter.resetPhone(phoneVerificationRequest);
                      /*  Intent intent=new Intent(this,PasswordResetActivity.class);
                        intent.putExtra(Constant.KEY_ID,userId);
                        intent.putExtra(Constant.KEY_TYPE,mOtp.getText().toString());
                        startActivity(intent);*/
                    }
                    else {
                        PhoneVerificationRequest phoneVerificationRequest = new PhoneVerificationRequest();
                        phoneVerificationRequest.setOtp(mOtp.getText().toString().trim());
                        phoneVerificationRequest.setId(userId);
                        mPresenter.validatePhone(phoneVerificationRequest);
                    }
                }
                break;
            case R.id.ll_resend_otp:
                PhoneVerificationRequest phoneVerificationRequest=new PhoneVerificationRequest();
                phoneVerificationRequest.setId(userId);
                mPresenter.resendOtp(phoneVerificationRequest);
                break;
        }
    }


    public void onVerifySuccess(LoginResponse loginResponse) {
        Toast.makeText(getApplicationContext(),loginResponse.getResponseMessage(),Toast.LENGTH_LONG).show();
        Utility.setAuthToken(loginResponse.getGetLoginData().getToken());

        UserRealm userRealm=mRealmController.checkUser(loginResponse.getGetLoginData().getUser().getId());

        if(userRealm==null){
            UserRealm mUserRealm=new UserRealm();
            mUserRealm.setId(loginResponse.getGetLoginData().getUser().getId());
            mUserRealm.setAvatar(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setAvatar_url(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setContactNo(loginResponse.getGetLoginData().getUser().getContact_no());
            mUserRealm.setCover(loginResponse.getGetLoginData().getUser().getCover());
            mUserRealm.setEmail(loginResponse.getGetLoginData().getUser().getEmail());
            mUserRealm.setIsVerify(loginResponse.getGetLoginData().getUser().getVerified_account());
            mUserRealm.setLoggedIn(Boolean.TRUE);
            mUserRealm.setName(loginResponse.getGetLoginData().getUser().getName());
            mUserRealm.setUsername(loginResponse.getGetLoginData().getUser().getUsername());

            mRealm.beginTransaction();
            mRealm.copyToRealm(mUserRealm);
            mRealm.commitTransaction();

            mRealmController.updateRecordUserNotLoggidIn(loginResponse.getGetLoginData().getUser().getId());

        }
        else {
            UserRealm mUserRealm=new UserRealm();
            mUserRealm.setAvatar(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setAvatar_url(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setContactNo(loginResponse.getGetLoginData().getUser().getContact_no());
            mUserRealm.setCover(loginResponse.getGetLoginData().getUser().getCover());
            mUserRealm.setEmail(loginResponse.getGetLoginData().getUser().getEmail());
            mUserRealm.setIsVerify(loginResponse.getGetLoginData().getUser().getVerified_account());
            mUserRealm.setLoggedIn(Boolean.TRUE);
            mUserRealm.setName(loginResponse.getGetLoginData().getUser().getName());
            mUserRealm.setUsername(loginResponse.getGetLoginData().getUser().getUsername());
            mRealmController.updateRecordUser(loginResponse.getGetLoginData().getUser().getId(),mUserRealm);

            mRealmController.updateRecordUserLoggidIn(loginResponse.getGetLoginData().getUser().getId());
            mRealmController.updateRecordUserNotLoggidIn(loginResponse.getGetLoginData().getUser().getId());
        }

        PreferenceManager preferenceManager=PreferenceManager.getInstance();
        preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_USERID,loginResponse.getGetLoginData().getUser().getId());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_TOKEN,loginResponse.getGetLoginData().getToken());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_TOKEN,loginResponse.getGetLoginData().getToken());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_BUSINESS,loginResponse.getGetLoginData().getManagement().get(0).getBusiness());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_ROLE,loginResponse.getGetLoginData().getManagement().get(0).getRole());

        Intent intent=new Intent(this, AwsHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }




    public void onResendSuccess(BaseResponse baseResponse) {
        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("resend_otp", params);
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
    }

    public void onResetSuccess(BaseResponse baseResponse) {

        Toast.makeText(getApplicationContext(),baseResponse.getResponseMessage(),Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,PasswordResetActivity.class);
        intent.putExtra(Constant.KEY_ID,userId);
        intent.putExtra(Constant.KEY_TYPE,mOtp.getText().toString());
        startActivity(intent);
    }
}
