package autroid.business.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
//import com.qiscus.nirmana.Nirmana;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.InspectionImageBE;
import autroid.business.model.response.InspectionImageResponse;
import autroid.business.presenter.CameraPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.jobcard.JobCardAddressFragment;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CameraFragment extends Fragment{

    //private static final String PERMISSIONS[] ={Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE} ;
    private static final int PERMISSIONS_MULTIPLE_REQUEST =102 ;
    private RequestBody requestBodyIndex;
    private RequestBody requestBodyBookingId;
    private MultipartBody.Part multipartFile;

    private CameraKitView cameraKitView;
    private ImageButton still_shot;
    private ImageView flash,proceed;
    private String bookingId="";
    private String userId="";
    private ImageView clicked_holder;
    private TextView directionHint;
    private boolean isFlash=false,mutex=false,isJobCard,isQualityCheck,isAdditionalPhotos;

    private int REQUEST_PERMISSION = 1;
    private int VALID_SIZE;
    private int index=0;

    //private static final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA};

    private ArrayList<InspectionImageBE> arrayList;
    private ArrayList<String> directions;
    private CameraPresenter cameraPresenter;
    private RelativeLayout mainLayout;


    private final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator;
    private final String suffix="picture";

    @Override
    public void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utility.hideSoftKeyboard(getActivity());

        //checkPermission();
        //cameraKitView.onStart();
        //cameraKitView.onResume();


        cameraKitView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraKitView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        cameraKitView.onStop();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        GlobalBus.getBus().register(this);
        Utility.hideSoftKeyboard(getActivity());

        arrayList=new ArrayList<>();
        proceed=view.findViewById(R.id.send);
        cameraKitView = view.findViewById(R.id.camera_view);
        still_shot = view.findViewById(R.id.stillshot);
        flash=view.findViewById(R.id.flash);
        clicked_holder=view.findViewById(R.id.clicked_holder);
        mainLayout=view.findViewById(R.id.relative_layout);
        directionHint=view.findViewById(R.id.hint);

        getBundleData();

        cameraPresenter=new CameraPresenter(this,mainLayout);

        still_shot.setOnClickListener(v -> {
            if (hasInternet()){
                still_shot.setEnabled(false);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(() -> {
                            still_shot.setEnabled(true);
                        });
                    }
                }, 1000);
                takePicture();
            }
        });

        flash.setOnClickListener(v->{
            if(!isFlash){
                isFlash=true;
                cameraKitView.setFlash(CameraKit.FLASH_ON);
//                Nirmana.getInstance().get().load(R.drawable.ic_flash_on).into(flash);
            }else{
                isFlash=false;
                cameraKitView.setFlash(CameraKit.FLASH_OFF);
//                Nirmana.getInstance().get().load(R.drawable.ic_flash_off).into(flash);
            }
        });

        clicked_holder.setOnClickListener(v->{
            Bundle bundle=new Bundle();
            bundle.putSerializable("inspection_list",arrayList);
            ((HomeScreen)getActivity()).addFragment(new DisplayGridFragment(),"DisplayGrid",true,false,bundle,((HomeScreen)getActivity()).currentFrameId);
        });

        proceed.setOnClickListener(v->{
            if (hasInternet()) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKING_ID, bookingId);
                bundle.putString(Constant.USER_ID, userId);
                JobCardAddressFragment jobCardAddressFragment = new JobCardAddressFragment();
                jobCardAddressFragment.setArguments(bundle);
                jobCardAddressFragment.show(getFragmentManager(), "AddressFragment");
            }
        });
    }


    private void takePicture() {

        cameraKitView.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitView, byte[] bytes) {
                Bundle bundle = new Bundle();
                bundle.putByteArray("byte_array",bytes);
                ((HomeScreen) getActivity()).addFragment(new DrawingFragment(),"DrawingFragment",true,false,bundle,((HomeScreen) getActivity()).currentFrameId);
            }
        });

    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_GET_IMAGE) {
            Bitmap bitmap = intent.getParcelableExtra("bitmap");
            glideImage(bitmap);

            if(arrayList.size()==0) arrayList.add(setUpInspectionImage());
            else if(mutex) arrayList.add(setUpInspectionImage());
            else {
                arrayList.remove(index);
                arrayList.add(index,setUpInspectionImage());
            }

            mutex=false;

            index=arrayList.size()-1;
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            File file =new File(file_path+suffix+index+".jpeg");
            if(file.exists()) file.delete();
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file.toString());
                fileOutputStream.write(stream.toByteArray());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            createRequestbody(index,bookingId,file);

            if(isJobCard)cameraPresenter.addInspectionImages(requestBodyIndex,requestBodyBookingId,multipartFile);
            else if(isQualityCheck)cameraPresenter.addApprovalImages(requestBodyIndex,requestBodyBookingId,multipartFile);
            else cameraPresenter.addAdditionalImages(requestBodyIndex,requestBodyBookingId,multipartFile);
        }
    }


    public void onSuccess(InspectionImageResponse inspectionImageResponse){
        mutex=true;
        int responseIndex=inspectionImageResponse.getInspectionDataBE().getIndex();
        InspectionImageBE inspectionImage=arrayList.get(responseIndex);
        inspectionImage.setIndex(responseIndex);
        inspectionImage.setImgId(inspectionImageResponse.getInspectionDataBE().getImageId());
        inspectionImage.setImgUrl(inspectionImageResponse.getInspectionDataBE().getImageAddress());

        File file=new File(file_path+suffix+responseIndex+".jpeg");
        if(file.exists())file.delete();

        if(!isAdditionalPhotos)directionHint.setText(directions.get(index+1));

        if(arrayList.size()==VALID_SIZE && isJobCard)nextScreen();
        else if(arrayList.size()==VALID_SIZE && isQualityCheck)backScreen();
    }

    public void createRequestbody(int index,String bookingId,File file){
        RequestBody filepart=null;
        requestBodyIndex=RequestBody.create(MultipartBody.FORM,String.valueOf(index));
        requestBodyBookingId=RequestBody.create(MultipartBody.FORM,bookingId);

        File imageFile = null;
        try {
            imageFile = new Compressor(getActivity()).compressToFile(file);
            String filenameArray[] = imageFile.getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            filepart=RequestBody.create(MediaType.parse("multipart/" + extension), imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        multipartFile=MultipartBody.Part.createFormData("media", imageFile.getName(),filepart);
    }

    public void glideImage(Bitmap bitmap){
//        Nirmana.getInstance().get().load(bitmap).into(clicked_holder);
    }

    public InspectionImageBE setUpInspectionImage(){
        InspectionImageBE inspectionImageBE=new InspectionImageBE();
        inspectionImageBE.setImgId(null);
        inspectionImageBE.setImgUrl(null);
        inspectionImageBE.setIndex(-1);
        return inspectionImageBE;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    private void initializeJobCardDirections() {
        directions=new ArrayList<>();
        directionHint.setText("Insurance");
        //directions.add("Front Side");
       /* directions.add("Insurance");
        directions.add("Registration");*/
        directions.add("Ideal RPM");
        directions.add("Dashboard");
        directions.add("Front Row");
        directions.add("Back Row");
        directions.add("Engine");
        directions.add("Front Side");
        directions.add("Right Side");
        directions.add("Rear Side");
        directions.add("Boot (Inside)");
        directions.add("Left Side");
        directions.add("Top Side");
        directions.add("DUMMY");
    }

    private void initializeApprovalDirections() {
        directions=new ArrayList<>();
        directionHint.setText("Ideal RPM");
        directions.add("Ideal RPM");
        directions.add("Dashboard");
        directions.add("Front Row");
        directions.add("Back Row");
        directions.add("Engine");
        directions.add("Front Side");
        directions.add("Right Side");
        directions.add("Rear Side");
        directions.add("Boot (Inside)");
        directions.add("Left Side");
        directions.add("Top Side");
        directions.add("DUMMY");
    }

    public void nextScreen(){
        Bundle bundle=new Bundle();
        bundle.putString(Constant.BOOKING_ID,bookingId);
        bundle.putString(Constant.USER_ID,userId);
        still_shot.setVisibility(View.GONE);
        directionHint.setVisibility(View.GONE);
        flash.setVisibility(View.GONE);
        proceed.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)clicked_holder.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,0);
        clicked_holder.setLayoutParams(params);

        JobCardAddressFragment jobCardAddressFragment=new JobCardAddressFragment();
        jobCardAddressFragment.setArguments(bundle);
        jobCardAddressFragment.show(getFragmentManager(),"AddressFragment");
    }

    public void backScreen(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_APPROVAL);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getActivity().onBackPressed();
    }

    private void getBundleData() {

        bookingId=getArguments().getString(Constant.BOOKING_ID);
        userId=getArguments().getString(Constant.USER_ID);

        if(getArguments().getBoolean(Constant.IS_JOBCARD))isJobCard=true;
        else if(getArguments().getBoolean(Constant.IS_QUALITY_CHECK))isQualityCheck=true;
        else isAdditionalPhotos=true;

        if(isJobCard){
            initializeJobCardDirections();
            VALID_SIZE=11;
            //VALID_SIZE=1;
        }else if(isQualityCheck) {
            initializeApprovalDirections();
            VALID_SIZE=11;
          /*  String jsonManifest= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_MANIFEST);
            try {
                JSONObject jsonObject = new JSONObject(jsonManifest);
                int picsLimit=jsonObject.getInt("job_inspection_pics_limit");
                if (picsLimit>0){

                }
            }
            catch (Exception e){

            }*/

        }else {
            directionHint.setText("");
        }
    }

//    private boolean checkPermissions() {
//        if(!hasPermissions(getActivity(),PERMISSIONS)){
//            requestPermissions(PERMISSIONS,100);
//            return false;
//        }else {
//            return true;
//        }
//    }



//    private boolean hasPermissions(Context context, String... perms) {
//        // Always return true for SDK < Marshmallow, let the system deal with the permissions
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            Log.w("TAG", "hasPermissions: API version < M, returning true by default");
//            return true;
//        }
//
//        for (String perm : perms) {
//            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
//            if (!hasPerm) {
//                return false;
//            }
//        }
//        return true;
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        boolean camera=false,read=false, write=false;
//
//        switch (requestCode){
//            case 1:
//                write=(grantResults[0]==PackageManager.PERMISSION_GRANTED);
//                read=(grantResults[1]==PackageManager.PERMISSION_GRANTED);
//                camera=(grantResults[2]==PackageManager.PERMISSION_GRANTED);
//                break;
//            case PERMISSIONS_MULTIPLE_REQUEST:
//                if (grantResults.length > 0) {
//                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//                    if(cameraPermission && readExternalFile)
//                    {
//                        cameraKitView.onStart();
//                        cameraKitView.onResume();
//                        // write your logic here
//                    } else {
//                        Snackbar.make(getActivity().findViewById(android.R.id.content),
//                                "Please Grant Permissions to upload profile photo",
//                                Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                                new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        requestPermissions(
//                                                new String[]{Manifest.permission
//                                                        .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                                                PERMISSIONS_MULTIPLE_REQUEST);
//                                    }
//                                }).show();
//                    }
//                }
//                break;
//       }
//
//       /*if(camera && read && write){
//           Toast.makeText(getActivity(),"Let's start clicking",Toast.LENGTH_SHORT).show();
//
//       }*/
//
//   }

    private boolean hasInternet(){
        ConnectivityManager check = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos=check.getAllNetworkInfo();

        for (NetworkInfo state:networkInfos) {
            if(state.getState()==NetworkInfo.State.CONNECTED){
                return true;
            }
        }

        Utility.showResponseMessage(mainLayout,"Please Check Internet Settings");
        return false;
    }

//    private void requestPermissions() {
//        requestPermissions(PERMISSIONS,REQUEST_PERMISSION);
//    }


//    private void checkPermission() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.READ_EXTERNAL_STORAGE) + ContextCompat
//                .checkSelfPermission(getActivity(),
//                        Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(
//                    new String[]{Manifest.permission
//                            .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                    PERMISSIONS_MULTIPLE_REQUEST);
//
//            /*if (ActivityCompat.shouldShowRequestPermissionRationale
//                    (getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ||
//                    ActivityCompat.shouldShowRequestPermissionRationale
//                            (getActivity(), Manifest.permission.CAMERA)) {
//
//                Snackbar.make(getActivity().findViewById(android.R.id.content),
//                        "Please Grant Permissions to take photo",
//                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
//                        new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                requestPermissions(
//                                        new String[]{Manifest.permission
//                                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                                        PERMISSIONS_MULTIPLE_REQUEST);
//                            }
//                        }).show();
//            } else {
//                requestPermissions(
//                        new String[]{Manifest.permission
//                                .READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
//                        PERMISSIONS_MULTIPLE_REQUEST);
//            }*/
//        } else {
//            cameraKitView.onResume();
//            // write your logic code if permission already granted
//        }
//    }


}