package autroid.business.view.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.analytics.FirebaseAnalytics;

import autroid.business.R;
import autroid.business.adapter.CategorySpinnerAdapter;
import autroid.business.adapter.CompanySpinnerAdapter;
import autroid.business.adapter.CountryCodeSpinnerAdapter;
import autroid.business.model.request.AddBusinessRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.RegistrationDataResponse;
import autroid.business.presenter.AddNewBusinessPresenter;
import autroid.business.utils.Utility;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewBusinessFragment extends Fragment implements View.OnClickListener {

    AppCompatSpinner mCategory,mCompany,mMobileCode;
    RelativeLayout mMainLayout;
    AddNewBusinessPresenter mPresenter;
    EditText mBusinessName,mContactno,mEmail,mUsername;
    TextView mLocation;
    LatLng loc;

    String strBusiness,strContact,strEmail,strCategory,strCompany,strLocation,strCountryCode,strUsername;

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private String spinnerSelectText[]={ "Select Category", "Select Company","Select State"};

    private RegistrationDataResponse mRegistrationDataResponse;

    Button btnDone;

    Bitmap bitmap;
    String picturePath="";

    ImageView mCover;
    FloatingActionButton mEditButton;
    private TextView mTitle;

    private boolean isCompany=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static AddNewBusinessFragment newInstance() {
        AddNewBusinessFragment fragment = new AddNewBusinessFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_business, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Local Business",null);

        mCategory= (AppCompatSpinner) view.findViewById(R.id.business_category_list);
        mCompany= (AppCompatSpinner) view.findViewById(R.id.business_company_list);
        mMobileCode= (AppCompatSpinner) view.findViewById(R.id.business_mobilecode_list);


        mBusinessName= (EditText) view.findViewById(R.id.business_name);
        mContactno= (EditText) view.findViewById(R.id.business_mobile);
        mEmail= (EditText) view.findViewById(R.id.business_email);
        mLocation= (TextView) view.findViewById(R.id.business_address);
        btnDone= (Button) view.findViewById(R.id.business_done);
        btnDone.setOnClickListener(this);

        mUsername=view.findViewById(R.id.business_username);

        mCover= (ImageView) view.findViewById(R.id.business_cover);
        mEditButton= (FloatingActionButton) view.findViewById(R.id.fab_edit);
        mEditButton.setOnClickListener(this);

        mLocation.setOnClickListener(this);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new AddNewBusinessPresenter(this,mMainLayout);

        mTitle=view.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.drawer_add_business));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        getRegisData();

        mCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txt = view.findViewById(R.id.spinnerTextView);

                if(i > 0){
                   // txt.setTextColor(getResources().getColor(R.color.white_color));
                    isCompany=mRegistrationDataResponse.getGetRegistrationData().getCategory().get(i).isIs_company();
                    if(mRegistrationDataResponse.getGetRegistrationData().getCategory().get(i).isIs_company()){
                        mCompany.setVisibility(View.VISIBLE);
                        // mDivider.setVisibility(View.VISIBLE);

                    }
                    else {
                        mCompany.setVisibility(View.GONE);
                        // mDivider.setVisibility(View.GONE);
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


    }

    private void getRegisData(){
        mPresenter.getRegistrationsItems();
    }

    public void onItemSuccess(RegistrationDataResponse registrationDataResponse){

        this.mRegistrationDataResponse=registrationDataResponse;

        CategorySpinnerAdapter adapterCategory = new CategorySpinnerAdapter(getActivity(), R.layout.row_spinner_layout,registrationDataResponse.getGetRegistrationData().getCategory(),spinnerSelectText[0]);

        mCategory.setAdapter(adapterCategory);

        CompanySpinnerAdapter adapterCompany = new CompanySpinnerAdapter(getActivity(), R.layout.row_spinner_layout,registrationDataResponse.getGetRegistrationData().getAutomaker(),spinnerSelectText[1]);

        mCompany.setAdapter(adapterCompany);

        CountryCodeSpinnerAdapter adapterCountryCode = new CountryCodeSpinnerAdapter(getActivity(), R.layout.row_spinner_layout,registrationDataResponse.getGetRegistrationData().getCountry());
        mMobileCode.setAdapter(adapterCountryCode);

        for(int i=0;i<registrationDataResponse.getGetRegistrationData().getCountry().size();i++){
            if(registrationDataResponse.getGetRegistrationData().getCountry().get(i).getTelephoneCode().equals("+91")){
                mMobileCode.setSelection(i);
            }
        }

      /*  StateSpinnerAdapter adapterState = new StateSpinnerAdapter(getActivity(), R.layout.row_spinner_layout,registrationDataResponse.getGetRegistrationData().getState(),spinnerSelectText[2]);

        mState.setAdapter(adapterState);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.business_address:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
            case R.id.business_done:
                if(validate()){
                    addBusiness();
                }
                break;
            case R.id.fab_edit:
                checkReadPermission();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                mLocation.setText(place.getAddress().toString());
                loc=place.getLatLng();
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);
            mCover.setImageBitmap(bitmap);


        }
    }

    private boolean validate(){
        Boolean flag=true;

        strBusiness=mBusinessName.getText().toString();
        strContact=mContactno.getText().toString();
        strEmail=mEmail.getText().toString();
        strLocation=mLocation.getText().toString();
        strUsername=mUsername.getText().toString();

        strCategory=mRegistrationDataResponse.getGetRegistrationData().getCategory().get(mCategory.getSelectedItemPosition()).getCategory();
        strCompany=mRegistrationDataResponse.getGetRegistrationData().getAutomaker().get(mCompany.getSelectedItemPosition()).getMaker();
        strCountryCode=mRegistrationDataResponse.getGetRegistrationData().getCountry().get(mMobileCode.getSelectedItemPosition()).get_id();

        if(strBusiness.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter business name");
        }
        else if(strContact.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter contact no.");
        }
        else if(strUsername.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter username");
        }
        else if(strEmail.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter email");
        }
        else if(strLocation.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter business location");
        }
        else if(strCategory.trim().equalsIgnoreCase(spinnerSelectText[0])){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter business category");
        }
        else if(isCompany){
            if(strCompany.trim().equalsIgnoreCase(spinnerSelectText[1])){
                flag=false;
                Utility.showResponseMessage(mMainLayout,"Please enter business company");
            }
        }

        return flag;
    }

    private void addBusiness(){
        AddBusinessRequest addBusinessRequest=new AddBusinessRequest();
        addBusinessRequest.setLocation(strLocation);
        addBusinessRequest.setCategory(strCategory);
        addBusinessRequest.setCompany(strCompany);
        addBusinessRequest.setContact_no(strContact);
        addBusinessRequest.setEmail(strEmail);
        addBusinessRequest.setLatitude(loc.latitude);
        addBusinessRequest.setLongitude(loc.longitude);
        addBusinessRequest.setName(strBusiness);
        addBusinessRequest.setUsername(strUsername);
        addBusinessRequest.setCountry_code(strCountryCode);
       /* if(bitmap!=null) {
            addBusinessRequest.setMedia(ApiURL.PREFIX_IMAGES+Utility.convertToBase64(bitmap));
            addBusinessRequest.setFilename(strContact);
        }*/
        mPresenter.addLocalBussiness(picturePath,addBusinessRequest);
    }

    public void addBussSuccess(BaseResponse baseResponse){

        Bundle params = new Bundle();
        params.putString("business_name",strBusiness);
        mFirebaseAnalytics.logEvent("add_local_business", params);

        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
        getActivity().onBackPressed();
    }

    private void checkReadPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                return;
            }
            else{
                loadFromGallery();
            }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                    loadFromGallery();

                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    String permission=permissions[0];
                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
                    if (! showRationale) {
                        Toast.makeText(getContext(),permission,Toast.LENGTH_SHORT).show();
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }



    public void loadFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }


}
