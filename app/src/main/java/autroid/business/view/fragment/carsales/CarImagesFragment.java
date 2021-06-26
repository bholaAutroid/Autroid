package autroid.business.view.fragment.carsales;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import autroid.business.R;
import autroid.business.adapter.EditCarGalleryAdapter;
import autroid.business.camera.RecyclerViewListener;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddImageResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.carsales.CarImagesPresenter;
import autroid.business.realm.RealmController;
import autroid.business.service.UploadMultipleImagesService;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.fragment.EditPicFragment;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class CarImagesFragment extends Fragment implements RecyclerViewListener {

    private static final int REQUEST_CODE_PICKER = 100;

    private static final int REQUEST_CAMERA_IMAGE = 101;

    String cameraPermissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private File file;

    ImageView mBackNavigation, mGallery, mCamera;

    String carId,imgId;

    EditCarGalleryAdapter editGalleryAdapter;

    RecyclerView recyclerView;

    FloatingActionButton fab;

    ConstraintLayout constraintLayout;

    Group group;

    CarImagesPresenter mPresenter;

    Realm realm;

    RealmController realmController;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_images, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);

        getBundleData();

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController=new RealmController(getActivity().getApplication());

        recyclerView = view.findViewById(R.id.grid_recycler);
        fab = view.findViewById(R.id.fab_add);
        constraintLayout =view.findViewById(R.id.main_layout);
        group = view.findViewById(R.id.attach_layout);
        mBackNavigation =view.findViewById(R.id.back_navigation);
        mGallery = view.findViewById(R.id.gallery);
        mCamera = view.findViewById(R.id.camera);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        try {
            if (realmController.getCarStockById(Constant.STOCK_TYPE_GARAGE, carId).getMedia() != null) {
                editGalleryAdapter = new EditCarGalleryAdapter(realmController.getCarStockById(Constant.STOCK_TYPE_GARAGE, carId).getMedia(), true, this);
                recyclerView.setAdapter(editGalleryAdapter);
            }
        }catch (NullPointerException e){

        }catch (Exception e){

        }

        mPresenter=new CarImagesPresenter(this, constraintLayout);

        mGallery.setOnClickListener(v -> {
            group.setVisibility(View.GONE);
            checkReadPermission();
        });

        mCamera.setOnClickListener(v -> {
            group.setVisibility(View.GONE);
            try {
                checkCameraPermission();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        fab.setOnClickListener(v->{
            if (group.getVisibility() == View.VISIBLE) group.setVisibility(View.GONE);
            else group.setVisibility(View.VISIBLE);
        });

        mBackNavigation.setOnClickListener(v-> getActivity().onBackPressed());
    }

    private void getBundleData() {
        carId = getArguments().getString(Constant.KEY_CAR_ID);
    }

    private void checkReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            } else selectImages();

        } else selectImages();

    }

    private void checkCameraPermission() throws IOException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> denied = new ArrayList<>();

            for (String perm : cameraPermissions) {
                if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED)
                    denied.add(perm);
            }

            if (denied.size() != 0) {
                requestPermissions(denied.toArray(new String[denied.size()]), REQUEST_CAMERA_IMAGE);
                return;
            } else
               dispatchCameraIntent();

        } else
           dispatchCameraIntent();

    }


    private void selectImages() {
        ImagePicker.create(this)
                // set whether pick or camera action should return immediate result or not. For pick image only work on single mode
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Select Image") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                // original selected images, used in multi mode
                .theme(R.style.ef_BaseTheme) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log// custom image loader, must be serializeable
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) selectImages();
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    String permission = permissions[0];
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) Toast.makeText(getActivity(), permission, Toast.LENGTH_SHORT).show();
                }
                return;
            }

            case REQUEST_CAMERA_IMAGE:
                for (int value : grantResults) if (value == -1) return;
                try {
                    dispatchCameraIntent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            UploadMultipleImagesService.startService(getActivity(), images, carId, Constant.VALUE_CAR, true);
        }else if(requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK){

            File oldFile=new File(file.getPath());

            File imageFile = null;

            try {
                imageFile = new Compressor(getActivity()).compressToFile(oldFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String filenameArray[] = imageFile.getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/" + extension), imageFile);
            MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media", imageFile.getName(), requestBody);
            RequestBody carID = RequestBody.create(MediaType.parse("text/plain"), carId);
            mPresenter.postImageCar(imageFileBody, carID);
        }
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
//        Intent intent = sendEvent.getEvent();
//        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_EDIT_CAR){
//
//            getActivity().runOnUiThread(()->{
//                ThumbnailBE data=new ThumbnailBE();
//                data.setId(intent.getStringExtra(Constant.KEY_ID));
//                data.setFile_address(intent.getStringExtra(Constant.LINK));
//            });
//        }
    }

    @Override
    public void onItemImage(String url, int index) {
        group.setVisibility(View.GONE);
        EditPicFragment imageZoomFragment = new EditPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_IMAGES, url);
        bundle.putString(Constant.KEY_TYPE, "cover");
        imageZoomFragment.setArguments(bundle);
        imageZoomFragment.show(getChildFragmentManager(), "ImageZoomFragment");
    }

    @Override
    public void onImageDelete(String id,int position) {

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation Message")
                .setMessage("Do you want to remove this image")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    group.setVisibility(View.GONE);
                    imgId=id;
                    PublishUnpublishRequest publishUnpublishRequest = new PublishUnpublishRequest();
                    publishUnpublishRequest.setStock_id(id);
                    mPresenter.removeCarPic(publishUnpublishRequest, id);
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void onDocumentClick(String id, int position, String documentType) {

    }

    @Override
    public void onDocumentDelete(String id, int position) {

    }

    public void onDeleteSuccess(BaseResponse response) {
        Utility.showResponseMessage(constraintLayout,response.getResponseMessage());
        realmController.deleteCarImage(carId,imgId);
    }

    public void onSuccessImageUpload(AddImageResponse response) throws IOException {
        realmController.addGarageCarImage(carId,response.getImageUploaded().getItem().getId(),response.getImageUploaded().getItem().getFile_address());
        checkCameraPermission();
    }

    private void dispatchCameraIntent() throws IOException {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            file = File.createTempFile("JPEG_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),".jpeg",getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES));

            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.careager.suite.fileprovider", file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_IMAGE);
            }
        }
    }

}
