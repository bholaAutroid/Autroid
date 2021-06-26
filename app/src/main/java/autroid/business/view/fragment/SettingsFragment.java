package autroid.business.view.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.system.ErrnoException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.io.InputStream;

import autroid.business.R;
import autroid.business.adapter.AccountsListAdapter;
import autroid.business.adapter.EditGalleryAdapter;
import autroid.business.adapter.SettingsBusinessTimingsAdapter;
import autroid.business.adapter.SettingsServiceTypeAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.OnAccountCallback;
import autroid.business.interfaces.OnCheckedChangeCallback;
import autroid.business.interfaces.OnImageDeleteCallback;
import autroid.business.model.bean.SocialiteBE;
import autroid.business.model.realm.UserRealm;
import autroid.business.model.request.AddTimingsRequest;
import autroid.business.model.request.ChangePasswordRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.request.UpdateProfileRequest;
import autroid.business.model.request.UploadMultipleImagesRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.BusinessProfileResponse;
import autroid.business.model.response.ServiceTypeResponse;
import autroid.business.model.response.TimingArrayResponse;
import autroid.business.model.response.UploadCoverResponse;
import autroid.business.presenter.SettingsPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.AddMultipleImagesActivity;
import autroid.business.view.activity.HomeScreen;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;


public class SettingsFragment extends Fragment implements View.OnClickListener,OnImageDeleteCallback,OnCheckedChangeCallback,CropImageView.OnCropImageCompleteListener,OnAccountCallback {

    RecyclerView recListTypes,recListTimings,recListAccounts;
    RelativeLayout mMainLayout;
    SettingsPresenter mPresenter;
    SettingsServiceTypeAdapter mSettingsServiceTypeAdapter;
    SettingsBusinessTimingsAdapter mSettingsBusinessTimingsAdapter;
    String userId;
    TimingArrayResponse timingArrayResponse;
    Button btnUpdateTimings,btnChangePassword,btnUpdateChanges,btnSaveLinks;
    RecyclerView mGallery;

    EditText mBusinessName,mBusinessEmail,mBusinessMobile,mBusinessSecondaryMobile,mBusinessSecondaryEmail,mBusinessAddress,mOverview;
    EditText mOldPassword,mNewPassword,mConfirmPassword;
    String strOldPassword,strNewPassword,strConfirmPassword;
    TextView mLocation;

    BusinessProfileResponse mShowroomProfileResponse;
    ServiceTypeResponse mServiceTypeResponse;

    private boolean isSwitchAllowed=false;
    FloatingActionButton mEditButton;

    Bitmap bitmap;
    String picturePath="";

    ImageView mCover;
    RealmController mRealmController;
    Realm mRealm;

    AddTimingsRequest addTimingsRequest;
    EditGalleryAdapter editGalleryAdapter;
    EditText mYoutube,mTwitter,mLinkedin,mInstagram,mGoogle,mFacebook,mWeb;
    String strYoutube="",strTwitter="",strLinkedin="",strInstagram="",strGoogle="",strFacebook="",strWeb="";
    private TextView mTitle;

    private Dialog dialog;
    private Uri mCropImageUri;
    private CropImageView mCropImageView;

    private FirebaseAnalytics mFirebaseAnalytics;

    AccountsListAdapter mAccountsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Settings",null);

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();
        mEditButton= (FloatingActionButton) view.findViewById(R.id.fab_edit);
        mEditButton.setOnClickListener(this);

        recListTypes= (RecyclerView) view.findViewById(R.id.services_types_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recListTypes.setLayoutManager(llm);
        recListTypes.setNestedScrollingEnabled(false);

        recListTimings= (RecyclerView) view.findViewById(R.id.business_timings_list);
        LinearLayoutManager llmTimings;
        llmTimings = new LinearLayoutManager(getActivity());
        llmTimings.setOrientation(LinearLayoutManager.VERTICAL);
        recListTimings.setLayoutManager(llmTimings);
        recListTimings.setNestedScrollingEnabled(false);

        recListAccounts= (RecyclerView) view.findViewById(R.id.accounts_list);
        LinearLayoutManager llmAccounts;
        llmAccounts = new LinearLayoutManager(getActivity());
        llmAccounts.setOrientation(LinearLayoutManager.VERTICAL);
        recListAccounts.setLayoutManager(llmAccounts);
        recListAccounts.setNestedScrollingEnabled(false);

        mBusinessName= (EditText) view.findViewById(R.id.business_name);
        mBusinessEmail= (EditText) view.findViewById(R.id.business_email);
        mBusinessMobile= (EditText) view.findViewById(R.id.business_mobile);
        mBusinessSecondaryMobile= (EditText) view.findViewById(R.id.business_secondary_mobile);
        mBusinessAddress= (EditText) view.findViewById(R.id.business_address);
        mBusinessSecondaryEmail= (EditText) view.findViewById(R.id.business_secondary_email);
        mOverview= (EditText) view.findViewById(R.id.business_overview);
        btnUpdateChanges= (Button) view.findViewById(R.id.update_changes);
        btnUpdateChanges.setOnClickListener(this);

        mCover= (ImageView) view.findViewById(R.id.cover_image);
        mOldPassword= (EditText) view.findViewById(R.id.old_password);
        mNewPassword= (EditText) view.findViewById(R.id.new_password);
        mConfirmPassword= (EditText) view.findViewById(R.id.confirm_password);
        btnChangePassword= (Button) view.findViewById(R.id.change_password);
        btnChangePassword.setOnClickListener(this);

        mLocation=view.findViewById(R.id.update_location);
        mLocation.setOnClickListener(this);

        mYoutube=view.findViewById(R.id.youtube_link);
        mTwitter=view.findViewById(R.id.twitter_link);
        mLinkedin=view.findViewById(R.id.linkedin_link);
        mInstagram=view.findViewById(R.id.instagram_link);
        mGoogle=view.findViewById(R.id.google_link);
        mFacebook=view.findViewById(R.id.facebook_link);
        mWeb=view.findViewById(R.id.web_link);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new SettingsPresenter(this,mMainLayout);
        PreferenceManager preferenceManager= PreferenceManager.getInstance();
        userId=preferenceManager.getStringPreference(getActivity(), Constant.SP_USERID);

        btnUpdateTimings= (Button) view.findViewById(R.id.update_timings);
        btnUpdateTimings.setOnClickListener(this);

        mGallery=view.findViewById(R.id.gallery);
        LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getActivity());
        llmGallery.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGallery.setLayoutManager(llmGallery);

        btnSaveLinks= (Button) view.findViewById(R.id.save_links);
        btnSaveLinks.setOnClickListener(this);

        // getServiceTypes();
        getTimingsArray();
        mTitle=view.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.drawer_settings));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        setAccounts();
        }


    void setAccounts(){
        RealmResults<UserRealm> userRealms= mRealmController.getAllUsers();
        mAccountsListAdapter=new AccountsListAdapter(userRealms,true,getActivity(),this);
        recListAccounts.setAdapter(mAccountsListAdapter);

    }


    @Subscribe
    public void getEvent(Events.SendEvent fragmentActivityMessage) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobalBus.getBus().unregister(this);
    }


    private void getServiceTypes(){
        mPresenter.getServiceType();
    }

    public void onServiceTypeSuccess(ServiceTypeResponse serviceTypeResponse){
        mServiceTypeResponse=serviceTypeResponse;
        mSettingsServiceTypeAdapter=new SettingsServiceTypeAdapter(getActivity(),serviceTypeResponse.getGetServiceType(),this);
        recListTypes.setAdapter(mSettingsServiceTypeAdapter);
    }

    private void getShowroom(){
        mPresenter.getShowroom(userId);
    }



    public void onSuccess(BusinessProfileResponse showroomProfileResponse){
        isSwitchAllowed=true;
        mShowroomProfileResponse=showroomProfileResponse;
        mSettingsBusinessTimingsAdapter=new SettingsBusinessTimingsAdapter(getActivity(),showroomProfileResponse.getResponseData().getTiming(),timingArrayResponse);
        recListTimings.setAdapter(mSettingsBusinessTimingsAdapter);

        mBusinessName.setText(showroomProfileResponse.getResponseData().getName());
        mBusinessEmail.setText(showroomProfileResponse.getResponseData().getEmail());
        mBusinessEmail.setEnabled(false);
        mBusinessMobile.setText(showroomProfileResponse.getResponseData().getContact_no());
        mBusinessMobile.setEnabled(false);
        mBusinessSecondaryMobile.setText(showroomProfileResponse.getResponseData().getOptional_info().getContact_no());
        mBusinessAddress.setText(showroomProfileResponse.getResponseData().getAddress().getLocation());
        mBusinessAddress.setEnabled(false);

        mYoutube.setText(showroomProfileResponse.getResponseData().getSocialite().getYoutube());
        mTwitter.setText(showroomProfileResponse.getResponseData().getSocialite().getTwitter());
        mLinkedin.setText(showroomProfileResponse.getResponseData().getSocialite().getLinkedin());
        mInstagram.setText(showroomProfileResponse.getResponseData().getSocialite().getInstagram());
        mFacebook.setText(showroomProfileResponse.getResponseData().getSocialite().getFacebook());
        mGoogle.setText(showroomProfileResponse.getResponseData().getSocialite().getGoogleplus());
        mWeb.setText(showroomProfileResponse.getResponseData().getSocialite().getWebsite());

        Picasso.with(getActivity()).load(showroomProfileResponse.getResponseData().getAvatar_address()).placeholder(R.drawable.placeholder_big).into(mCover);

        mBusinessSecondaryEmail.setText(showroomProfileResponse.getResponseData().getOptional_info().getEmail());
        mOverview.setText(showroomProfileResponse.getResponseData().getOptional_info().getOverview());


        editGalleryAdapter=new EditGalleryAdapter(mRealmController.getShowroomData().getMedia(),true,this);
        mGallery.setAdapter(editGalleryAdapter);
    }

    private void getTimingsArray(){
        mPresenter.getTimingsArray();
    }

    public void onTimingsArraySuccess(TimingArrayResponse timingArrayResponse){
        this.timingArrayResponse=timingArrayResponse;
        getShowroom();
    }

    public void onTimingsSendSuccess(BaseResponse baseResponse){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_timings", params);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE_TIMING);
        intent.putExtra(Constant.KEY_TYPE,addTimingsRequest.getTiming());
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_timings:
                addTimingsRequest=new AddTimingsRequest();
                addTimingsRequest.setTiming(SettingsBusinessTimingsAdapter.mList);
                mPresenter.addTimingsArray(addTimingsRequest);

                break;
            case R.id.change_password:
                if(validateChangePassword())
                    changePassword();
                break;
            case R.id.save_links:
               strYoutube=mYoutube.getText().toString();
               strFacebook=mFacebook.getText().toString();
               strGoogle=mGoogle.getText().toString();
               strLinkedin=mLinkedin.getText().toString();
               strInstagram=mInstagram.getText().toString();
               strTwitter=mTwitter.getText().toString();
               strWeb=mWeb.getText().toString();
                updateSocialLink();
                break;
            case R.id.update_changes:
                if(validateUpdateChanges())updateProfile();
                break;
            case R.id.fab_edit:
                checkReadPermission();
                break;
            case R.id.btn_cancel:
                if(dialog!=null){
                    dialog.dismiss();
                }
                break;
            case R.id.btn_done:
                mCropImageView.getCroppedImageAsync();
                break;
            case R.id.update_location:
                ((HomeScreen) getActivity()).addFragment(new UpdateBusinessLocationFragment(), "UpdateBusinessLocationFragment",true,false,null,((HomeScreen) getActivity()).currentFrameId);

                break;
        }
    }

    private boolean validateUpdateChanges() {

        if(mBusinessName.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mMainLayout,"Please enter business name");
            return false;
        } else if(mBusinessEmail.getText().toString().trim().length()==0 || !Utility.isEmailValid(mMainLayout,mBusinessEmail.getText().toString().trim())){
            Utility.showResponseMessage(mMainLayout,"Please enter valid email");
            return false;
        }else if(mBusinessMobile.getText().toString().trim().length()!=10){
            Utility.showResponseMessage(mMainLayout,"Please enter valid mobile number");
            return false;
        } else if(mBusinessSecondaryEmail.getText().toString().trim().length()!=0 && !Utility.isEmailValid(mMainLayout,mBusinessSecondaryEmail.getText().toString().trim())){
            Utility.showResponseMessage(mMainLayout,"Enter valid secondary email");
            return false;
        }else if(mBusinessSecondaryMobile.getText().toString().trim().length()!=0 && mBusinessSecondaryMobile.getText().toString().trim().length()!=10){
            Utility.showResponseMessage(mMainLayout,"Enter valid secondary mobile number");
            return false;
        }

        return true;
    }

    @Override
    public void onDeleteClick(String id) {
        PublishUnpublishRequest publishUnpublishRequest=new PublishUnpublishRequest();
        publishUnpublishRequest.setStock_id(id);
        mPresenter.removeShowroomPic(publishUnpublishRequest,id);
    }

    @Override
    public void onDeleteImageClickId(String id) {

    }

    @Override
    public void onAddImageClick(int position) {
        Intent intent=new Intent(getActivity(), AddMultipleImagesActivity.class);
        intent.putExtra(Constant.KEY_ID,userId);
        intent.putExtra(Constant.KEY_TYPE,Constant.VALUE_SHOWROOM);
        startActivity(intent);
    }

    private boolean validateChangePassword(){
        boolean flag=true;
        strOldPassword=mOldPassword.getText().toString();
        strNewPassword=mNewPassword.getText().toString();
        strConfirmPassword=mConfirmPassword.getText().toString();

        if(strOldPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Enter Old Password");
        }else if(strNewPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Enter New Password");
        }else if(strConfirmPassword.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Enter Confirm Password");
        }else if(!strNewPassword.trim().equals(strConfirmPassword)){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Confirm Password Mismatch");
        }

        return flag;
    }

    private void changePassword(){
        ChangePasswordRequest changePasswordRequest=new ChangePasswordRequest();
        changePasswordRequest.setOld_password(strOldPassword);
        changePasswordRequest.setPassword(strNewPassword);
        mPresenter.changePassword(changePasswordRequest);
    }

    public void onChnagePasswordSuccess(BaseResponse baseResponse){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_password", params);
        mOldPassword.setText("");
        mNewPassword.setText("");
        mConfirmPassword.setText("");
    }

    public void onDeleteSuccess(BaseResponse baseResponse,String imageId){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());

        mRealmController.deleteShowRoomImage(userId,imageId);

        editGalleryAdapter.notifyDataSetChanged();

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE_GALLERY);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
    }

    private void updateProfile(){
        UpdateProfileRequest updateProfileRequest=new UpdateProfileRequest();
        updateProfileRequest.setName(mBusinessName.getText().toString());
        updateProfileRequest.setSecondary_contact_no(mBusinessSecondaryMobile.getText().toString());
        updateProfileRequest.setSecondary_email(mBusinessSecondaryEmail.getText().toString());
        updateProfileRequest.setOverview(mOverview.getText().toString());
        mPresenter.updateProfile(updateProfileRequest);


    }

    private void updateSocialLink(){
        SocialiteBE socialiteBE=new SocialiteBE();
        socialiteBE.setFacebook(strFacebook);
        socialiteBE.setGoogleplus(strGoogle);
        socialiteBE.setInstagram(strInstagram);
        socialiteBE.setLinkedin(strLinkedin);
        socialiteBE.setYoutube(strYoutube);
        socialiteBE.setTwitter(strTwitter);
        socialiteBE.setWebsite(strWeb);
        mPresenter.updateSocialLinks(socialiteBE);
    }
    public void updateProfileSuccess(BaseResponse baseResponse){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_profile_info", params);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE_PROFILE);
        intent.putExtra(Constant.Key_Business_Name,mBusinessName.getText().toString());
        intent.putExtra(Constant.Key_Business_overview,mOverview.getText().toString());
        Events.SendEvent sendEvent = new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);

    }

    @Override
    public void onCheckedChange(int pos) {
        if(isSwitchAllowed) {
            PublishUnpublishRequest publishUnpublishRequest = new PublishUnpublishRequest();
            publishUnpublishRequest.setStock_id(mServiceTypeResponse.getGetServiceType().get(pos).getId());
            mPresenter.addServiceType(publishUnpublishRequest);
        }
    }

    private void checkReadPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                return;
            } else {
                loadFromGallery();
            }

        }else loadFromGallery();
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showDialog(mCropImageUri);
        } else {
            Toast.makeText(getActivity(), "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }



    public void loadFromGallery() {
        /*Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

           Uri selectedImage = data.getData();

           boolean requirePermissions = false;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                   getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                   isUriRequiresPermissions(selectedImage)) {

               // request permissions and handle the result in onRequestPermissionsResult()
               requirePermissions = true;
               mCropImageUri = selectedImage;
               requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
           }

           if (!requirePermissions) {
               showDialog(selectedImage);
           }


       }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getActivity().getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public void onUploadSuccess(UploadCoverResponse uploadCoverResponse){

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_cover", params);


        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE_COVER);
        intent.putExtra(Constant.KEY_IMAGES,uploadCoverResponse.getResponseData().getAvatar_address());
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
        mRealmController.updateShowroomCover(uploadCoverResponse.getResponseData().getAvatar_address());

        /*try {
            Qiscus.updateUser(PreferenceManager.getInstance().getStringPreference(getActivity(), Constant.SP_USERID), uploadCoverResponse.getResponseData().getAvatar_address(), new QiscusCore().SetUserListener() {
                @Override
                public void onSuccessGetUser(QiscusAccount qiscusAccount) {

                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }
        catch (NullPointerException e){

        }catch (Exception e){

        }*/
    }

    public void updateSocialSuccess(BaseResponse baseResponse) {
        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("update_social_links", params);
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
    }

    public void showDialog(Uri imageUri){
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_crop);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mCropImageView = (CropImageView) dialog.findViewById(R.id.CropImageView);
        mCropImageView.setImageUriAsync(imageUri);
        mCropImageView.setOnCropImageCompleteListener(this);
        mCropImageView.setAspectRatio(150,100);
        mCropImageView.setFixedAspectRatio(true);

        TextView mCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView mDone = (TextView) dialog.findViewById(R.id.btn_done);
        mCancel.setOnClickListener(this);
        mDone.setOnClickListener(this);

        dialog.show();

    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        mCropImageView.setImageUriAsync(result.getUri());

        //  result.getBitmap();
        Bitmap bitmap =  result.getBitmap();

        mCover.setImageBitmap(bitmap);

        if(dialog!=null){
            dialog.dismiss();
        }

        UploadMultipleImagesRequest uploadMultipleImagesRequest = new UploadMultipleImagesRequest();
        uploadMultipleImagesRequest.setMedia("data:image/jpeg;base64,"+Utility.convertToBase64(bitmap));
        mPresenter.updateCover(uploadMultipleImagesRequest);
    }

    @Override
    public void onAccountClick(String id, Boolean isLoggedin) {

        deletePostDialog(id,isLoggedin);
    }

    void deletePostDialog(final String userId, final Boolean isLoggedin){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure, You want to remove this account?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(isLoggedin){
                            //((HomeScreen)getActivity()).callLogOut("Account removed");
                            mRealmController.deleteLoggedInUsers();
                            ((HomeScreen)getActivity()).callLogOut("Account removed");
                        }
                        else {
                            mRealmController.deleteUsersById(userId);
                            ((HomeScreen)getActivity()).setHeaderSpinner();

                        }

                        Bundle params = new Bundle();
                        mFirebaseAnalytics.logEvent("remove_multiple_account", params);

                    }
                });

        builder1.setNegativeButton(
                "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
