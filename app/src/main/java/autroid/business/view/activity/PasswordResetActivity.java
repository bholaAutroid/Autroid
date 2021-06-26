package autroid.business.view.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.realm.UserRealm;
import autroid.business.model.request.ResetPasswordRequest;
import autroid.business.model.response.LoginResponse;
import autroid.business.presenter.ResetPasswordPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;

    @BindView(R.id.btn_done)
    Button mDone;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    ResetPasswordPresenter mPresenter;

    String strPassword,strConfirmPassword,strId,strOtp;

    RealmController mRealmController;
    Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        mDone.setOnClickListener(this);

        this.mRealm = RealmController.with(this).getRealm();
        mRealmController=RealmController.getInstance();

        mPresenter=new ResetPasswordPresenter(this,mMainLayout);

        Intent intent=getIntent();
        strId=intent.getStringExtra(Constant.KEY_ID);
        strOtp=intent.getStringExtra(Constant.KEY_TYPE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_done:
                if(validate()){
                    ResetPasswordRequest resetPasswordRequest=new ResetPasswordRequest();
                    resetPasswordRequest.setId(strId);
                    resetPasswordRequest.setOtp(strOtp);
                    resetPasswordRequest.setPassword(strPassword);
                    resetPasswordRequest.setType("Business");
                    mPresenter.resetPassword(resetPasswordRequest);
                }
                break;
        }
    }

    private boolean validate(){
        boolean flag=true;

        strPassword=mPassword.getText().toString();
        strConfirmPassword=mConfirmPassword.getText().toString();

        if(strPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter password");
        }
        else if(strConfirmPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter confirm password");
        }
        else if(!strPassword.equals(strConfirmPassword)){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Confirm password mismatch");
        }
        return flag;

    }

    public void onLoginSuccess(LoginResponse loginResponse) {

        login(loginResponse);

    }

    private void login(LoginResponse loginResponse) {
        Utility.setAuthToken(loginResponse.getGetLoginData().getToken());

        UserRealm userRealm=mRealmController.checkUser(loginResponse.getGetLoginData().getUser().getId());

        if(userRealm==null){
            UserRealm mUserRealm=new UserRealm();
            mUserRealm.setId(loginResponse.getGetLoginData().getUser().getId());
            mUserRealm.setAvatar(loginResponse.getGetLoginData().getUser().getCover_address());
            mUserRealm.setAvatar_url(loginResponse.getGetLoginData().getUser().getCover_address());
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

            //mRealmController.updateRecordUserLoggidIn(loginResponse.getGetLoginData().getUser().getId());
            mRealmController.updateRecordUserNotLoggidIn(loginResponse.getGetLoginData().getUser().getId());
        }

        PreferenceManager preferenceManager=PreferenceManager.getInstance();
        preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_USERID,loginResponse.getGetLoginData().getUser().getId());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_TOKEN,loginResponse.getGetLoginData().getToken());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_BUSINESS,loginResponse.getGetLoginData().getManagement().get(0).getBusiness());
        preferenceManager.putStringPreference(getApplicationContext(),Constant.SP_ROLE,loginResponse.getGetLoginData().getManagement().get(0).getRole());
        Intent intent=new Intent(this, HomeScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
