package autroid.business.view.fragment;


import android.Manifest;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.realm.OffersRealm;
import autroid.business.model.request.AddOfferRequest;
import autroid.business.model.request.UploadMultipleImagesRequest;
import autroid.business.model.response.AddOfferResponse;
import autroid.business.presenter.AddOfferPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOfferFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    ImageView mOfferImage;
    Bitmap bitmap;
    EditText mOfferTitle,mOfferDescription;
    TextView mOfferDate;
    Button mAddOffer;
    RelativeLayout mMainLayout;
    AddOfferPresenter mPresenter;

    String strTitle="",strDescription="",strDate="",strOfferId;

    boolean isEditable=false;

    private Realm realm;
    RealmController realmController;

    String picturePath="";
    private TextView mTitle;

    private FirebaseAnalytics mFirebaseAnalytics;

    public AddOfferFragment() {
        // Required empty public constructor
    }

    public static AddOfferFragment newInstance() {
        AddOfferFragment fragment = new AddOfferFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_offer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Add Offer",null);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        mOfferImage= (ImageView) view.findViewById(R.id.offer_image);
        mOfferImage.setOnClickListener(this);

        mOfferTitle= (EditText) view.findViewById(R.id.offer_title);
        mOfferDescription= (EditText) view.findViewById(R.id.offer_description);
        mAddOffer= (Button) view.findViewById(R.id.add_offer);
        mAddOffer.setOnClickListener(this);

        mOfferDate= (TextView) view.findViewById(R.id.offer_date);
        mOfferDate.setOnClickListener(this);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new AddOfferPresenter(this,mMainLayout);

        mTitle=view.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.add_offer));
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        Bundle bundle=getArguments();
        if(bundle!=null) {
            if (bundle.containsKey(Constant.KEY_IS_OFFER_EDITABLE)) {
                mTitle.setText(getString(R.string.edit_offer));
                isEditable = bundle.getBoolean(Constant.KEY_IS_OFFER_EDITABLE);
                strOfferId=bundle.getString(Constant.KEY_ID);
                OffersRealm offersRealm=realmController.getOffer(strOfferId);
                mOfferTitle.setText(offersRealm.getTitle());
                mOfferDescription.setText(offersRealm.getDescription());
                mOfferDate.setText(offersRealm.getValidity());
                String imageURL = offersRealm.getOfferImg();
                if (imageURL != null) {
                    if (imageURL.length() > 0) {
                        Picasso.with(getActivity()).load(imageURL).placeholder(R.drawable.placeholder_big).into(mOfferImage);
                        // Picasso.with(getActivity()).load(offerBE.getFile_address()).into(mOfferImage);
                    }
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.offer_image:
             checkReadPermission();
             break;
         case R.id.add_offer:
             if(!isEditable) {
                 if (validate()) {
                     addOffer();
                 }
             }
             else {
                 if(validateEditableText()){
                     editOffer();
                 }
             }
             break;
         case R.id.offer_date:
             setDate();
             break;
     }
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
        /*Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();

            //cropImage(selectedImage,3);
           /* String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
             picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);*/
            customDialog(selectedImage);



        }
        else  if (requestCode == 3 && resultCode == RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

            bitmap = BitmapFactory.decodeFile(picturePath);
            mOfferImage.setImageBitmap(bitmap);

            if (isEditable) {
                if (bitmap != null) {
                    editOfferImage();
                }
            }
        }

    }

    public void cropImage(Uri uri, int action_code) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 16);
            intent.putExtra("aspectY", 9);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 200);
            intent.putExtra("scale", true);
            startActivityForResult(intent, action_code);
        }catch (ActivityNotFoundException e) {
            String errorMessage = "your device doesn't support the crop action!";
            e.printStackTrace();
            //Toast toast = Toast.makeText(getActivity(), e.pri()+"", Toast.LENGTH_SHORT);
            //toast.show();
        }

    }

    private boolean validate(){
        boolean flag=true;
        strTitle=mOfferTitle.getText().toString();
        strDescription=mOfferDescription.getText().toString();
        strDate=mOfferDate.getText().toString();

        if(bitmap==null){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please add image");
        }
        else if(strDate.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select validity date");
        }

        return flag;

    }

    private boolean validateEditableText(){
        boolean flag=true;
        strTitle=mOfferTitle.getText().toString();
        strDescription=mOfferDescription.getText().toString();
        strDate=mOfferDate.getText().toString();

       if(strDate.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please select validity date");
        }

        return flag;

    }

    private void  addOffer(){
        AddOfferRequest addOfferRequest=new AddOfferRequest();
        addOfferRequest.setDescription(strDescription);
        addOfferRequest.setFilename(strTitle);
        addOfferRequest.setOffer(strTitle);
        addOfferRequest.setValid_till(strDate);
        mPresenter.postOffer(addOfferRequest,picturePath);
    }

    private void  editOffer(){
        AddOfferRequest addOfferRequest=new AddOfferRequest();
        addOfferRequest.setDescription(strDescription);
        addOfferRequest.setOffer(strTitle);
        addOfferRequest.setValid_till(strDate);
        addOfferRequest.setId(strOfferId);
        mPresenter.editOffer(addOfferRequest);
    }

    private void  editOfferImage(){
        UploadMultipleImagesRequest uploadMultipleImagesRequest=new UploadMultipleImagesRequest();
        uploadMultipleImagesRequest.setFilename("offer_"+strOfferId);
        uploadMultipleImagesRequest.setId(strOfferId);
        mPresenter.editOfferImage(picturePath,uploadMultipleImagesRequest);
    }

    public void onSuccessImage(AddOfferResponse addOfferResponse){
        Utility.showResponseMessage(mMainLayout,addOfferResponse.getResponseMessage());

        OffersRealm offersRealm=new OffersRealm();
        offersRealm.setOfferImg(addOfferResponse.getOfferResponse().getItem().getFile_address());
        realmController.updateOfferImage(strOfferId,offersRealm);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_REFRESH_OFFER);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);
    }

    public void onSuccess(AddOfferResponse addOfferResponse){
        Utility.showResponseMessage(mMainLayout,addOfferResponse.getResponseMessage());

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_ID,addOfferResponse.getOfferResponse().getItem().getId());
        mFirebaseAnalytics.logEvent("add_offer", params);

        OffersRealm offersRealm=new OffersRealm();

        offersRealm.setDescription(addOfferResponse.getOfferResponse().getItem().getDescription());
        offersRealm.setOfferId(addOfferResponse.getOfferResponse().getItem().getId());
        offersRealm.setTitle(addOfferResponse.getOfferResponse().getItem().getOffer());
        offersRealm.setOfferImg(addOfferResponse.getOfferResponse().getItem().getFile_address());
        offersRealm.setValidity(addOfferResponse.getOfferResponse().getItem().getValid_till());
        offersRealm.setPublish(addOfferResponse.getOfferResponse().getItem().isPublish());
        realm.beginTransaction();
        realm.copyToRealm(offersRealm);
        realm.commitTransaction();

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_REFRESH_OFFER);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);

        getActivity().onBackPressed();
    }

    public void setDate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        final Calendar later = Calendar.getInstance();
        later.add(Calendar.MONTH, 1);

        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }});

        datePickerDialog.show();
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mOfferDate.setText(zeroPrefix(dayOfMonth) + "-" + zeroPrefix(month+1) + "-" + year);
    }



    private void customDialog(Uri imagepath){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_image_crop);
        dialog.setTitle("Title...");
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // set the custom dialog components - text, image and button
        final CropImageView mCropImageView;
        Button cropImageButton,cropImageCancel;
        mCropImageView = (CropImageView)  dialog.findViewById(R.id.CropImageView);
        mCropImageView.setAspectRatio(1600,900);
        mCropImageView.setFixedAspectRatio(false);
        //mCropImageView.setCropShape(CropImageView.CropShape.OVAL);


        mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        mCropImageView.dispatchSetActivated(true);
        cropImageButton= (Button) dialog.findViewById(R.id.cropImageButton);
        cropImageCancel= (Button) dialog.findViewById(R.id.cropImageCancel);
       /* File imgFile = new  File(imagepath);
        if(imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mCropImageView.setImageBitmap(myBitmap);
        }*/
        try {

            //mCropImageView.setImageBitmap(imagepath);
            mCropImageView.setImageUriAsync(imagepath);


            //Log.d("Base 64-->",Utility.convertToBase64(bitmap));
        }catch (Exception e){
            e.printStackTrace();
        }
        //   Picasso.with(getActivity()).load(new File(imagepath)).into(mCropImageView);

        cropImageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cropImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    bitmap=mCropImageView.getCroppedImage();


                    mOfferImage.setImageBitmap(bitmap);

                    Uri uri= getImageUri(getActivity(),bitmap);
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    if(isEditable){
                        if(bitmap!=null){
                            editOfferImage();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                // Bitmap cropped =  mCropImageView.getCroppedImage(500,500);
               /* if (uri != null)
                   Picasso.with(getActivity()).load(uri).into(answer_selected_image);*/

                /*Picasso.Builder builder = new Picasso.Builder(getActivity());
                builder.listener(new Picasso.Listener()
                {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
                    {
                        exception.printStackTrace();
                    }
                });
                builder.build().load(uri).into(answer_selected_image);*/
                // answer_selected_image.setImageURI(uri);

                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog

        dialog.show();
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_UPDATE_PROFILE) {

        }
    }

    public void onEditSuccess(AddOfferResponse addOfferResponse) {
        Utility.showResponseMessage(mMainLayout,addOfferResponse.getResponseMessage());

        OffersRealm offersRealm=new OffersRealm();
        offersRealm.setDescription(addOfferResponse.getOfferResponse().getItem().getDescription());
        offersRealm.setTitle(addOfferResponse.getOfferResponse().getItem().getOffer());
        offersRealm.setValidity(addOfferResponse.getOfferResponse().getItem().getValid_till());

        realmController.updateOffer(strOfferId,offersRealm);

        Intent intent=new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_REFRESH_OFFER);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);

        //getActivity().onBackPressed();
    }
}
