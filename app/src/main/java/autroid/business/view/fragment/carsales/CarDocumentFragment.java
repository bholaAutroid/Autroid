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
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import autroid.business.R;
import autroid.business.adapter.cars.CarDocumentAdapter;
import autroid.business.camera.DrawingFragment;
import autroid.business.camera.RecyclerViewListener;
import autroid.business.compression.FileCompression;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CarDocumentBE;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.AddImageResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarDocumentResponse;
import autroid.business.model.response.DocumentResponse;
import autroid.business.presenter.carsales.CarDocumentPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.EditPicFragment;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class CarDocumentFragment extends Fragment implements RecyclerViewListener {

    String icUrl, rcUrl, carId;

    boolean isRcSelected, isInsuranceSelected;

    private static final int REQUEST_PICK_FILE = 1;

    private static final int GALLERY = 2;

    private static final int CAMERA = 3;

    private static final int DOCUMENT = 4;

    private static final int REQUEST_CAMERA_IMAGE = 5;

    String cameraPermissions[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private File file;

    Intent data;

    int latestPosition, selection;

    TextView emptyDocument;

    ArrayList<CarDocumentBE> arrayList;

    ImageView mBackNavigation, mGallery, mDocument, mCamera;

    FloatingActionButton fab;

    ConstraintLayout mainLayout;

    Group group;

    CarDocumentPresenter mPresenter;

    RecyclerView recyclerView;

    CarDocumentAdapter carDocumentAdapter;

    public CarDocumentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_car_documents, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);

        getBundleData();

        mainLayout = view.findViewById(R.id.main_layout);
        group = view.findViewById(R.id.attach_layout);
        mBackNavigation = view.findViewById(R.id.back_navigation);
        fab = view.findViewById(R.id.fab_add);
        recyclerView = view.findViewById(R.id.recyclerView);
        mGallery = view.findViewById(R.id.gallery);
        mDocument = view.findViewById(R.id.document);
        mCamera = view.findViewById(R.id.camera);
        emptyDocument = view.findViewById(R.id.default_document);

        arrayList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        carDocumentAdapter = new CarDocumentAdapter(arrayList, this);
        recyclerView.setAdapter(carDocumentAdapter);


        mPresenter = new CarDocumentPresenter(this, mainLayout);
        mPresenter.getCarDocuments(carId);

        mGallery.setOnClickListener(v -> {
            selection = GALLERY;
            group.setVisibility(View.GONE);
            checkReadPermission();
        });

        mDocument.setOnClickListener(v -> {
            selection = DOCUMENT;
            group.setVisibility(View.GONE);
            checkReadPermission();
        });

        mCamera.setOnClickListener(v -> {
            selection = CAMERA;
            group.setVisibility(View.GONE);
            try {
                checkCameraPermission();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        mBackNavigation.setOnClickListener(v -> getActivity().onBackPressed());

        fab.setOnClickListener(v -> {
            if (group.getVisibility() == View.VISIBLE) group.setVisibility(View.GONE);
            else group.setVisibility(View.VISIBLE);
        });

    }

    private void getBundleData() {
        icUrl = getArguments().getString(Constant.IC_URL);
        rcUrl = getArguments().getString(Constant.RC_URL);
        carId = getArguments().getString(Constant.KEY_CAR_ID);
    }

    private void checkReadPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return;
            } else pickFile();

        } else pickFile();

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

    private void pickFile() {

        Intent intent = new Intent();
        intent.setType("*/*");

        if (selection == GALLERY) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_PICK_FILE);
        } else if (selection == DOCUMENT) {
            String[] mimetypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_PICK_FILE);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) pickFile();
                else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    String permission = permissions[0];
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale)
                        Toast.makeText(getActivity(), permission, Toast.LENGTH_SHORT).show();
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

        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK /*&& data != null*/) {

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constant.IS_IMAGE_CAPTURED_CAR_SALES, true);
            bundle.putString(Constant.VALUE, file.getAbsolutePath());
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new DrawingFragment(), "DrawingFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

        } else if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK && data != null) {

            this.data = data;
            Bundle bundle = new Bundle();
            bundle.putString(Constant.IMAGE_URI, data.getData().toString());
            bundle.putBoolean(Constant.IS_IMAGE_SELECTED_CAR_SALES, true);

            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new DrawingFragment(), "DrawingFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

//            if (isRcSelected) mPresenter.postRc(imageFileBody, carID);
//            else if (isInsuranceSelected) mPresenter.postInsurance(imageFileBody, carID);
        }
    }

    public void onRCSuccess(AddImageResponse addImageResponse) {
        rcUrl = addImageResponse.getImageUploaded().getFile_address();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_RC_UPDATE);
        broadcastIntent.putExtra(Constant.LINK, rcUrl);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
    }

    public void onInsuranceSuccess(AddImageResponse addImageResponse) {
        icUrl = addImageResponse.getImageUploaded().getFile_address();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_IC_UPDATE);
        broadcastIntent.putExtra(Constant.LINK, icUrl);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
    }

    public void onSuccessDocument(DocumentResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
        arrayList.add(response.getCarDocument());
        carDocumentAdapter.notifyDataSetChanged();
        if(arrayList.size()==0)emptyDocument.setVisibility(View.VISIBLE);
        else emptyDocument.setVisibility(View.GONE);
    }

    public void onSuccessDocumentDelete(BaseResponse response) {
        arrayList.remove(latestPosition);
        carDocumentAdapter.notifyDataSetChanged();
        Utility.showResponseMessage(mainLayout, response.getResponseMessage());
        if(arrayList.size()==0)emptyDocument.setVisibility(View.VISIBLE);
        else emptyDocument.setVisibility(View.GONE);
    }

    public void onSuccessDocuments(CarDocumentResponse response) {
        arrayList.addAll(response.getCarDocuments());
        carDocumentAdapter.notifyDataSetChanged();
        if(arrayList.size()==0)emptyDocument.setVisibility(View.VISIBLE);
        else emptyDocument.setVisibility(View.GONE);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) throws IOException {

        Intent intent = sendEvent.getEvent();

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_IMAGE_SELECTED_CARSALE) {
            File file = FileCompression.from(data.getData());
            prepareFile(carId, file, intent.getStringExtra(Constant.VALUE));
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_IMAGE_CAPTURED_CARSALE) {
            prepareFile(carId, file, intent.getStringExtra(Constant.VALUE));
        }
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
    public void onImageDelete(String id, int position) {
        confirmationDialog(id, position);
    }

    @Override
    public void onDocumentClick(String fileUrl, int position, String documentType) {
        group.setVisibility(View.GONE);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        startActivity(browserIntent);
    }

    @Override
    public void onDocumentDelete(String id, int position) {
        confirmationDialog(id, position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    private void prepareFile(String carId, File file, String fileCaption) {
        String filenameArray[] = file.getName().split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/" + extension), file);
        MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media", file.getName(), requestBody);
        RequestBody carID = RequestBody.create(MediaType.parse("text/plain"), carId);
        RequestBody caption = RequestBody.create(MediaType.parse("text/plain"), fileCaption);
        mPresenter.postDocuments(imageFileBody, carID, caption);
    }

    private void confirmationDialog(String id, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation Message")
                .setMessage("Do you want to remove this file")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    group.setVisibility(View.GONE);
                    latestPosition = position;
                    PublishUnpublishRequest publishUnpublishRequest = new PublishUnpublishRequest();
                    publishUnpublishRequest.setStock_id(id);
                    mPresenter.deleteDocument(publishUnpublishRequest);
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                }).show();
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
