package autroid.business.service;

import android.app.Application;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.esafirm.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;

import autroid.business.MyApplication;

import autroid.business.R;
import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.AddImageResponse;
import autroid.business.model.request.UploadMultipleImagesRequest;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import id.zelory.compressor.Compressor;
import io.realm.Realm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/17/17.
 */

public class UploadMultipleImagesService extends IntentService {

    String id,keyValue;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    int notId = 1;
    int serverCall=0;
    ArrayList<Image> images;
    boolean fromCarSales;

    private Realm realm;
    RealmController realmController;

    public UploadMultipleImagesService() {
        super("image");
    }

    public static void startService(Context context, ArrayList<Image> images, String id, String keyValue,boolean fromCarSales) {

        Intent intent = new Intent(context, UploadMultipleImagesService.class);
        intent.putExtra(Constant.KEY_IMAGES,images);
        intent.putExtra(Constant.KEY_ID,id);
        intent.putExtra(Constant.KEY_TYPE,keyValue);
        intent.putExtra(Constant.FROM_CAR_SALES,fromCarSales);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent!=null) {
            images = intent.getParcelableArrayListExtra(Constant.KEY_IMAGES);
            fromCarSales=intent.getBooleanExtra(Constant.FROM_CAR_SALES,false);
            id=intent.getStringExtra(Constant.KEY_ID);
            keyValue=intent.getStringExtra(Constant.KEY_TYPE);

            if(images.size()>0){
                generateNotification();
                sendFile();
            }
        }
    }

    private void sendFile(){
        if(serverCall==0){
            build.setProgress(images.size(),serverCall, false);
            mNotifyManager.notify(notId, build.build());
        }

        try {
            UploadMultipleImagesRequest uploadMultipleImagesRequest = new UploadMultipleImagesRequest();
            uploadMultipleImagesRequest.setId(id);
            uploadMultipleImagesRequest.setFilename(images.get(serverCall).getName());

            /* change image path to base64 */
            File imageFileOld = new File(images.get(serverCall).getPath());
            File imageFile = new Compressor(this).compressToFile(imageFileOld);
            String filenameArray[] = imageFile.getName().split("\\.");
            String extension = filenameArray[filenameArray.length - 1];
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/" + extension), imageFile);

            MultipartBody.Part imageFileBody = MultipartBody.Part.createFormData("media", imageFile.getName(), requestBody);

            if (keyValue.equalsIgnoreCase(Constant.VALUE_CAR)) {
                RequestBody carID = RequestBody.create(MediaType.parse("text/plain"), uploadMultipleImagesRequest.getCar_id());
                postImageCar(imageFileBody, carID, uploadMultipleImagesRequest.getCar_id());
            } else if (keyValue.equalsIgnoreCase(Constant.VALUE_PRODUCT)) {
                RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), uploadMultipleImagesRequest.getCar_id());
                postImageProduct(imageFileBody, productId, uploadMultipleImagesRequest.getCar_id());
            } else if (keyValue.equalsIgnoreCase(Constant.VALUE_SHOWROOM)) {
                RequestBody showroomID = RequestBody.create(MediaType.parse("text/plain"), uploadMultipleImagesRequest.getCar_id());
                postImageShowroom(imageFileBody, showroomID, uploadMultipleImagesRequest.getCar_id());
            }
        }catch (Exception e){

        }
    }

    public void postImageCar(MultipartBody.Part imageFileBody,RequestBody carId, final String id) {
        ApiRequest apiRequest = ApiFactory.createService(getApplicationContext(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postImageCar(imageFileBody,carId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {
                if (response.body().getResponseCode() >= 200 && response.body().getResponseCode() < 300) {
                    serverCall++;

                    realm = RealmController.with(getApplication()).getRealm();
                    Application appCtx = (MyApplication) getApplication();
                    realmController=new RealmController(appCtx);

                    if(!fromCarSales)realmController.addCarImage(id,response.body().getImageUploaded().getItem().getId(),response.body().getImageUploaded().getItem().getFile_address());
                    else realmController.addGarageCarImage(id,response.body().getImageUploaded().getItem().getId(),response.body().getImageUploaded().getItem().getFile_address());

                    if(serverCall==images.size()) {
                        build.setProgress(100, 100, false);
                        build.setOngoing(false);
                        build.setContentText("Uploaded Successfully");
                        mNotifyManager.notify(notId, build.build());

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(Constant.BROADCAST_ADD_IMAGES);
                        sendBroadcast(broadcastIntent);
                    }
                    else {
                        build.setProgress(images.size(),serverCall, false);
                        mNotifyManager.notify(notId, build.build());
                        sendFile();
                    }

                } else {


                }
            }
            @Override
            public void error(String errorMessage) {
                build.setProgress(images.size(), serverCall, false);
                build.setOngoing(false);
                build.setContentText("Error in uploading try again");
                mNotifyManager.notify(notId, build.build());
            }
        }, getApplicationContext(), null,Boolean.FALSE);


    }

    public void postImageProduct(MultipartBody.Part imageFileBody, final RequestBody productId, final String id) {
        ApiRequest apiRequest = ApiFactory.createService(getApplicationContext(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postImageProduct(imageFileBody,productId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {
                if (response.body().getResponseCode() >= 200 && response.body().getResponseCode() < 300) {
                    serverCall++;

                    realm = RealmController.with(getApplication()).getRealm();
                    Application appCtx = (MyApplication) getApplication();
                    realmController=new RealmController(appCtx);
                    realmController.addProductImage(id,response.body().getImageUploaded().getItem().getId(),response.body().getImageUploaded().getItem().getFile_address());
                    if(serverCall==images.size()) {
                        build.setProgress(100, 100, false);
                        build.setOngoing(false);
                        build.setContentText("Uploaded Successfully");
                        mNotifyManager.notify(notId, build.build());

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(Constant.BROADCAST_ADD_IMAGES);
                        sendBroadcast(broadcastIntent);
                    }
                    else {
                        build.setProgress(images.size(),serverCall, false);
                        mNotifyManager.notify(notId, build.build());
                        sendFile();
                    }

                } else {

                }
            }
            @Override
            public void error(String errorMessage) {
                build.setProgress(images.size(), serverCall, false);
                build.setOngoing(false);
                build.setContentText("Error in uploading try again");
                mNotifyManager.notify(notId, build.build());

            }
        }, getApplicationContext(), null,Boolean.FALSE);


    }
    public void postImageShowroom(MultipartBody.Part imageFileBody,RequestBody showroomId, final String id) {
        ApiRequest apiRequest = ApiFactory.createService(getApplicationContext(), ApiRequest.class);
        ApiCallback.MyCall<AddImageResponse> myCall = apiRequest.postImageShowroom(imageFileBody,showroomId);
        myCall.enqueue(new ApiCallback.MyCallback<AddImageResponse>() {
            @Override
            public void success(final Response<AddImageResponse> response) {
                if (response.body().getResponseCode() >= 200 && response.body().getResponseCode() < 300) {

                    serverCall++;
                    realm = RealmController.with(getApplication()).getRealm();
                    Application appCtx = (MyApplication) getApplication();
                    realmController=new RealmController(appCtx);
                    realmController.addShowroomImage(id,response.body().getImageUploaded().getItem().getId(),response.body().getImageUploaded().getItem().getFile_address());
                    if(serverCall==images.size()) {
                        build.setProgress(100, 100, false);
                        build.setOngoing(false);
                        build.setContentText("Uploaded Successfully");
                        mNotifyManager.notify(notId, build.build());

                        Intent broadcastIntent = new Intent();
                        broadcastIntent.setAction(Constant.BROADCAST_ADD_IMAGES);
                        sendBroadcast(broadcastIntent);
                    }
                    else {
                        build.setProgress(images.size(),serverCall, false);
                        mNotifyManager.notify(notId, build.build());
                        sendFile();
                    }

                } else {

                }
            }
            @Override
            public void error(String errorMessage) {
                build.setProgress(images.size(), serverCall, false);
                build.setOngoing(false);
                build.setContentText("Error in uploading try again!");
                mNotifyManager.notify(notId, build.build());

            }
        }, getApplicationContext(), null,Boolean.FALSE);


    }

    private void generateNotification(){
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        build = new NotificationCompat.Builder(this)
                .setContentTitle("Careager")
                .setContentText("Upload in progress")
                .setOngoing(false)
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);
    }
}
