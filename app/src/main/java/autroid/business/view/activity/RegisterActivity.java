package autroid.business.view.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import autroid.business.R;
import autroid.business.adapter.CategorySpinnerAdapter;
import autroid.business.adapter.CompanySpinnerAdapter;
import autroid.business.adapter.CountryCodeSpinnerAdapter;
import autroid.business.model.request.AddBusinessRequest;
import autroid.business.model.response.LoginResponse;
import autroid.business.model.response.RegistrationDataResponse;
import autroid.business.presenter.RegisterPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatSpinner mCategory,mCompany,mMobileCode;
    RegisterPresenter mPresenter;
    RelativeLayout mMainLayout;
    private String spinnerSelectText[]={ "Select Category", "Select Company","Select State"};
    EditText mName,mEmail,mPassword,mConfirmPassword,mMobile,mUsername;
    TextView mAddress;
    RegistrationDataResponse mRegistrationDataResponse;
    Button mRegister;
    View mDivider;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    LatLng loc;
    RealmController mRealmController;
    Realm mRealm;

    String  strName,strEmail,strPassword,strConfirmPassword,strMobile,strAddress,strCategory,strCompany,strTelephoneCode,strCountryCode,strUsername;
    CheckBox cbTermPolicy;

    private boolean isCompany=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }


    private void init(){
        mCategory= (AppCompatSpinner) findViewById(R.id.business_category_list);
        mCompany= (AppCompatSpinner) findViewById(R.id.business_company_list);
        mMobileCode= (AppCompatSpinner) findViewById(R.id.business_mobilecode_list);
        mDivider=findViewById(R.id.business_company_divider);

        mRegister= (Button) findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        mName= (EditText) findViewById(R.id.business_name);
        mEmail= (EditText) findViewById(R.id.business_email);
        mPassword= (EditText) findViewById(R.id.password);
        mConfirmPassword= (EditText) findViewById(R.id.confirm_password);
        mMobile= (EditText) findViewById(R.id.business_mobile);
        mAddress= (TextView) findViewById(R.id.business_address);
        mUsername=findViewById(R.id.business_username);
        mAddress.setOnClickListener(this);

        cbTermPolicy= (CheckBox) findViewById(R.id.txt_signup);

        this.mRealm = RealmController.with(this).getRealm();
        mRealmController=RealmController.getInstance();


    mMainLayout= (RelativeLayout) findViewById(R.id.main_layout);
    mPresenter=new RegisterPresenter(this,mMainLayout);

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = view.findViewById(R.id.spinnerTextView);

                if(i > 0){
                    txt.setTextColor(getResources().getColor(R.color.white_color));
                    isCompany=mRegistrationDataResponse.getGetRegistrationData().getCategory().get(i).isIs_company();
                    if(mRegistrationDataResponse.getGetRegistrationData().getCategory().get(i).isIs_company()){
                        mCompany.setVisibility(View.VISIBLE);
                        mDivider.setVisibility(View.VISIBLE);

                    }
                    else {
                        mCompany.setVisibility(View.GONE);
                        mDivider.setVisibility(View.GONE);
                    }
                }
                if(i == 0){
                    txt.setTextColor(getResources().getColor(R.color.gray_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mMobileCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = view.findViewById(R.id.spinnerTextView);

                if(i > 0){
                    txt.setTextColor(getResources().getColor(R.color.white_color));
                }
                if(i == 0){
                    txt.setTextColor(getResources().getColor(R.color.gray_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = view.findViewById(R.id.spinnerTextView);

                if(i > 0){
                    txt.setTextColor(getResources().getColor(R.color.white_color));
                }
                if(i == 0){
                    txt.setTextColor(getResources().getColor(R.color.gray_color));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    getRegisData();


}

    private void getRegisData(){
        mPresenter.getRegistrationsItems();
    }



    public void onItemSuccess(RegistrationDataResponse registrationDataResponse){
        this.mRegistrationDataResponse=registrationDataResponse;

        CategorySpinnerAdapter adapterCategory = new CategorySpinnerAdapter(this, R.layout.row_spinner_layout_white,registrationDataResponse.getGetRegistrationData().getCategory(),spinnerSelectText[0]);
        mCategory.setAdapter(adapterCategory);

        CompanySpinnerAdapter adapterCompany = new CompanySpinnerAdapter(this, R.layout.row_spinner_layout_white,registrationDataResponse.getGetRegistrationData().getAutomaker(),spinnerSelectText[1]);
        mCompany.setAdapter(adapterCompany);

        CountryCodeSpinnerAdapter adapterCountryCode = new CountryCodeSpinnerAdapter(this, R.layout.row_spinner_layout_white,registrationDataResponse.getGetRegistrationData().getCountry());
        mMobileCode.setAdapter(adapterCountryCode);

        for(int i=0;i<registrationDataResponse.getGetRegistrationData().getCountry().size();i++){
            if(registrationDataResponse.getGetRegistrationData().getCountry().get(i).getTelephoneCode().equals("+91")){
                mMobileCode.setSelection(i);
            }
        }

      /*    StateSpinnerAdapter adapterState = new StateSpinnerAdapter(this, R.layout.row_spinner_layout_white,registrationDataResponse.getGetRegistrationData().getState(),spinnerSelectText[2]);
            mState.setAdapter(adapterState);
*/
        /*ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(this, R.layout.layout_spinner_text_white, getResources().getStringArray(R.array.mobile_code));
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mMobileCode.setAdapter(adapterBrand);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_register:
                if(validate()){
                    if(cbTermPolicy.isChecked())
                        sendRegister();
                }
                break;
            case R.id.business_address:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
        }
    }


    private boolean validate(){
        Boolean flag=true;

        strName=mName.getText().toString();
        strMobile=mMobile.getText().toString();
        strEmail=mEmail.getText().toString();
        strAddress=mAddress.getText().toString();
        strPassword=mPassword.getText().toString();
        strUsername=mUsername.getText().toString();
        strConfirmPassword=mConfirmPassword.getText().toString();
      //  strCountryCode=mMobileCode.getSelectedItem().toString();


        strCategory=mRegistrationDataResponse.getGetRegistrationData().getCategory().get(mCategory.getSelectedItemPosition()).getCategory();
        strCompany=mRegistrationDataResponse.getGetRegistrationData().getAutomaker().get(mCompany.getSelectedItemPosition()).getMaker();
        strCountryCode=mRegistrationDataResponse.getGetRegistrationData().getCountry().get(mMobileCode.getSelectedItemPosition()).get_id();
        strTelephoneCode=mRegistrationDataResponse.getGetRegistrationData().getCountry().get(mMobileCode.getSelectedItemPosition()).getTelephoneCode();


        if(strName.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter business name");
        }
        else if(strCategory.trim().equalsIgnoreCase(spinnerSelectText[0])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select business category");
            }
        else if(isCompany){
            if(strCategory.trim().equalsIgnoreCase(spinnerSelectText[1])){
                flag=false;
                Utility.showResponseMessage(mMainLayout,"Please select company");
                }
        }
        else if(strMobile.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter mobile no.");
        }
        else if(strUsername.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter username");
        }
        else if(strAddress.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter business location");
        }
        else if(strPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter password");
        }
        else if(strConfirmPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter confirm password");
        }
        else if(!strConfirmPassword.equals(strPassword)){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Confirm password mismatch");
        }
        return flag;
    }

    private void sendRegister(){
        AddBusinessRequest addBusinessRequest=new AddBusinessRequest();
        addBusinessRequest.setLocation(strAddress);
        addBusinessRequest.setCategory(strCategory);
        addBusinessRequest.setCompany(strCompany);
        addBusinessRequest.setContact_no(strMobile);
        addBusinessRequest.setEmail(strEmail);
        addBusinessRequest.setLatitude(loc.latitude);
        addBusinessRequest.setLongitude(loc.longitude);
        addBusinessRequest.setName(strName);
        addBusinessRequest.setCountry_code(strCountryCode);
        addBusinessRequest.setPassword(strPassword);
        addBusinessRequest.setUsername(strUsername);
        mPresenter.addLocalBussiness(addBusinessRequest);

    }


    public void addBussSuccess(LoginResponse loginResponse){
        Utility.showResponseMessage(mMainLayout,loginResponse.getResponseMessage());

        startActivity(new Intent(this, OTPActivity.class).putExtra(Constant.Key_Mobile,strTelephoneCode+"-"+strMobile).putExtra(Constant.KEY_ID,loginResponse.getGetLoginData().getUser().getId()));
        finish();
       /* startActivity(new Intent(this, HomeScreen.class));
        finish();*/


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mAddress.setText(place.getAddress().toString());
                loc = place.getLatLng();
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void goToLogingPage(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}
