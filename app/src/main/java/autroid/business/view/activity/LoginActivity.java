package autroid.business.view.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import autroid.business.R;
import autroid.business.model.realm.UserRealm;
import autroid.business.model.request.ForgotPasswordRequest;
import autroid.business.model.request.LoginRequest;
import autroid.business.model.response.ForgotPasswordResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.presenter.LoginPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mUsername, mPassword;
    String strUsername, strPassword;
    RelativeLayout mMainLayout;
    ImageView btnLogin;

    LoginPresenter mPresenter;

    RealmController mRealmController;
    Realm mRealm;
    TextView mRegister,tv_loginText;
    TextView mForgotPassword;
    LinearLayout llPassword;


    private boolean isLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mRealm = RealmController.with(this).getRealm();
        mRealmController = RealmController.getInstance();

        mUsername = (EditText) findViewById(R.id.et_username);
        mPassword = (EditText) findViewById(R.id.et_password);
        mMainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mPresenter = new LoginPresenter(this, mMainLayout);
        mForgotPassword = findViewById(R.id.txt_forgot_Password);
        mForgotPassword.setOnClickListener(this);
        llPassword = findViewById(R.id.ll_password);
        tv_loginText=findViewById(R.id.tv_login_login);


        btnLogin = (ImageView) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        mRegister = findViewById(R.id.ll_register);
        mRegister.setOnClickListener(this);

        tv_loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    postLoginRequest();
                }
            }
        });
    }

    private boolean validate() {
        boolean flag = true;

        strUsername = mUsername.getText().toString();
        strPassword = mPassword.getText().toString();

        if (strUsername.trim().length() == 0) {
            flag = false;
            Utility.showResponseMessage(mMainLayout, getString(R.string.empty_username));
        } else if (strPassword.trim().length() == 0) {
            if (isLogin) {
                flag = false;
                Utility.showResponseMessage(mMainLayout, getString(R.string.empty_password));
            }

        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if (validate()) {
                    postLoginRequest();
                }
                break;
            case R.id.ll_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.txt_forgot_Password:
                strUsername = mUsername.getText().toString();

                if (strUsername.trim().length() == 0) {
                    Utility.showResponseMessage(mMainLayout, getString(R.string.empty_username));
                } else {
                    ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
                    forgotPasswordRequest.setContact_no(strUsername);
                    forgotPasswordRequest.setType("business");
                    mPresenter.forgotPassword(forgotPasswordRequest);
                }
                break;
        }
    }

    private void postLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setContact_no(strUsername);
        loginRequest.setPassword(strPassword);
        loginRequest.setType("business");
        if (isLogin)
            mPresenter.login(loginRequest);
        else
            mPresenter.validateUser(loginRequest);
    }

    public void onLoginSuccess(LoginResponse loginResponse) {

        if (loginResponse.getGetLoginData().getStatus().equalsIgnoreCase(getString(R.string.status_new))) {
            Toast.makeText(this, "Mobile number not registered. Please register", Toast.LENGTH_LONG).show();
            /*Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtra(Constant.Key_Mobile, strUsername);
            startActivity(intent);
            finish();*/
        } else if (loginResponse.getGetLoginData().getStatus().equalsIgnoreCase(getString(R.string.status_active))) {
            login(loginResponse);
        } else if (loginResponse.getGetLoginData().getStatus().equalsIgnoreCase(getString(R.string.status_complete))) {
            startActivity(new Intent(this, OTPActivity.class).putExtra(Constant.Key_Mobile, strUsername).putExtra(Constant.KEY_ID, loginResponse.getGetLoginData().getUser().getId()));
            finish();
        }

    }

    private void login(LoginResponse loginResponse) {
        Utility.setAuthToken(loginResponse.getGetLoginData().getToken());

        UserRealm userRealm = mRealmController.checkUser(loginResponse.getGetLoginData().getUser().getId());

        if (userRealm == null) {
            UserRealm mUserRealm = new UserRealm();
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

        } else {
            UserRealm mUserRealm = new UserRealm();
            mUserRealm.setAvatar(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setAvatar_url(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setContactNo(loginResponse.getGetLoginData().getUser().getContact_no());
            mUserRealm.setCover(loginResponse.getGetLoginData().getUser().getCover());
            mUserRealm.setEmail(loginResponse.getGetLoginData().getUser().getEmail());
            mUserRealm.setIsVerify(loginResponse.getGetLoginData().getUser().getVerified_account());
            mUserRealm.setLoggedIn(Boolean.TRUE);
            mUserRealm.setName(loginResponse.getGetLoginData().getUser().getName());
            mUserRealm.setUsername(loginResponse.getGetLoginData().getUser().getUsername());
            mRealmController.updateRecordUser(loginResponse.getGetLoginData().getUser().getId(), mUserRealm);

            //mRealmController.updateRecordUserLoggidIn(loginResponse.getGetLoginData().getUser().getId());
            mRealmController.updateRecordUserNotLoggidIn(loginResponse.getGetLoginData().getUser().getId());
        }

        if (!isLogin) {
            if (loginResponse.getGetLoginData().getStatus().equals(getString(R.string.status_new))) {
                Toast.makeText(this, "Mobile number not registered. Please register", Toast.LENGTH_LONG).show();

            } else if (loginResponse.getGetLoginData().getStatus().equals(getString(R.string.status_active))) {
                llPassword.setVisibility(View.VISIBLE);
                isLogin = true;
                Utility.showResponseMessage(mMainLayout, "Please enter password");
            /*  Utility.setAuthToken(loginResponse.getGetLoginData().getToken());
            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_USERID, loginResponse.getGetLoginData().getUser().get_id());
            finish(); */
            } else if (loginResponse.getGetLoginData().getStatus().equals(getString(R.string.status_complete))) {
                startActivity(new Intent(this, OTPActivity.class).putExtra(Constant.Key_Mobile, strUsername).putExtra(Constant.KEY_ID, loginResponse.getGetLoginData().getUser().getId()));
            }
        } else {
            if (loginResponse.getGetLoginData().getStatus().equalsIgnoreCase(getString(R.string.status_active))) {
                PreferenceManager preferenceManager = PreferenceManager.getInstance();
                preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_USERID, loginResponse.getGetLoginData().getUser().getId());
                preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_TOKEN, loginResponse.getGetLoginData().getToken());
                preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_TOKEN, loginResponse.getGetLoginData().getToken());
                preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_BUSINESS, loginResponse.getGetLoginData().getManagement().get(0).getBusiness());
                preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_ROLE, loginResponse.getGetLoginData().getManagement().get(0).getRole());

                Intent intent = new Intent(this, HomeScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (loginResponse.getGetLoginData().getStatus().equalsIgnoreCase(getString(R.string.status_complete))) {
                startActivity(new Intent(this, OTPActivity.class).putExtra(Constant.Key_Mobile, strUsername).putExtra(Constant.KEY_ID, loginResponse.getGetLoginData().getUser().getId()));
            }
        }
    }

    public void onForgotSuccess(ForgotPasswordResponse baseResponse) {
        Utility.showResponseMessage(mMainLayout, baseResponse.getResponseMessage());
        startActivity(new Intent(this, OTPActivity.class).putExtra(Constant.Key_Mobile, strUsername).putExtra(Constant.KEY_FORGOT, true).putExtra(Constant.KEY_ID, baseResponse.getGetId().getId()));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    public void goToHelpPage(View view) {
        startActivity(new Intent(getApplicationContext(),HelpPageActivity.class));
    }
}
