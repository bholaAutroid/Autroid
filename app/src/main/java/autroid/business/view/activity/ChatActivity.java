package autroid.business.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.support.v4.os.ResultReceiver;
import androidx.core.util.Pair;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.bumptech.glide.request.RequestOptions;
//import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResult;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.google.android.gms.maps.model.LatLng;
//import com.qiscus.nirmana.Nirmana;
//import com.qiscus.sdk.chat.core.QiscusCore;
//import com.qiscus.sdk.chat.core.data.model.QiscusChatRoom;
//import com.qiscus.sdk.chat.core.data.model.QiscusComment;
//import com.qiscus.sdk.chat.core.data.model.QiscusLocation;
//import com.qiscus.sdk.chat.core.data.model.QiscusRoomMember;
//import com.qiscus.sdk.chat.core.data.remote.QiscusApi;
//import com.qiscus.sdk.chat.core.data.remote.QiscusPusherApi;
//import com.qiscus.sdk.chat.core.event.QiscusCommentReceivedEvent;
//import com.qiscus.sdk.chat.core.event.QiscusUserStatusEvent;
//import com.qiscus.sdk.chat.core.util.QiscusAndroidUtil;
//import com.qiscus.sdk.chat.core.util.QiscusDateUtil;
//import com.qiscus.sdk.ui.view.QiscusChatScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import autroid.business.BuildConfig;
import autroid.business.R;
import autroid.business.adapter.qiscus.PhrasesAdapter;
import autroid.business.compression.FileCompression;
import autroid.business.fcmservices.PushNotificationUtil;
import autroid.business.interfaces.RecyclerViewListener;
import autroid.business.presenter.ChatActivityPresenter;
import autroid.business.service.FetchAddressIntentService;
import autroid.business.utils.Constant;
import autroid.business.view.activity.map_activity.MapsActivity;
import autroid.business.view.fragment.EditPicFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity{


//        implements QiscusChatScrollListener.Listener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, RecyclerViewListener {
//
//    private static String USER = QiscusCore.getQiscusAccount().getEmail();

    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int REQUEST_IMAGE_PERMISSION = 2;
    private static final int REQUEST_LOCATION_PERMISSION = 3;

    private static final int TEXT = 4;
    private static final int LOCATION = 5;
    private static final int REQUEST_LOCATION = 6;

    private static final int REQUEST_PICK_FILE = 7;
    private static final int REQUEST_FILE_PERMISSION = 8;

    private String download_file_path = "/storage/emulated/0/CarEager Suite/Files/";

    private static final String[] FILE_IMAGE_PERMISSION = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };

    private static final String[] LOCATION_PERMISSION = {
            "android.permission.ACCESS_FINE_LOCATION"
    };

    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;

    Location location = null;

    Geocoder geocoder;
    List<Address> addresses;

    ImageView send, back_navigation, attachment_icon, call_btn, avatar, gallery, loc_pin, document;
    EditText editText;
    Toolbar toolbar;
    TextView userName, status;

    String message = null, retrieved = "", semaphore = "", outer_uri = null;

    private boolean isLoading = false, isActivity = false;

    public static boolean isActivityOpened = false;

    File file;

//    QiscusComment qiscusComment;
//    QiscusChatRoom qiscusChatRoom;
//
//    ChatAdapter chatAdapter;
//
//    PhrasesAdapter phrasesAdapter;
//
//    List<QiscusRoomMember> roomMembersList;
//    List<QiscusComment> loadedArrayList;
    List<String> opponentData;
    List<String> phrasesList;

    RecyclerView recyclerView;
    RecyclerView phrases_recyclerView;

    LinearLayoutManager layoutManager;
    LinearLayoutManager phrases_layoutManager;
    LinearLayout attachment_menu;

    NotificationManager notificationManager;

    ChatActivityPresenter chatActivityPresenter;

    FloatingActionButton fab;

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        Log.i("None", "I am onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        QiscusPusherApi.getInstance().unListenUserStatus(getOpponentData().get(2));
    }

    protected void onResume() {
        super.onResume();
        isActivityOpened = true;
//        notificationManager.cancel(PushNotificationUtil.notificationId);
        EventBus.getDefault().register(this);
    }

    protected void onPause() {
        super.onPause();
        isActivityOpened = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {

        if (phrases_recyclerView.getVisibility() == View.VISIBLE) {
            phrases_recyclerView.setVisibility(View.GONE);
        } else if (attachment_menu.getVisibility() == View.VISIBLE) {
            attachment_menu.setVisibility(View.GONE);
        } else if (isActivity) {
            finish();
        } else if (!isActivity) {
            startActivity(new Intent(this, HomeScreen.class));
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatActivityPresenter = new ChatActivityPresenter(this);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        setSupportActionBar(toolbar);
//        qiscusChatRoom = getIntent().getParcelableExtra("chat_room");
//        isActivity = getIntent().getExtras().getBoolean("is_activity");
//        outer_uri = getIntent().getExtras().getString("outer_uri");
//
//        if (qiscusChatRoom == null) {
//            finish();
//            return;
//        }

        if (outer_uri != null) {
            try {
                file = FileCompression.from(Uri.parse(outer_uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
            File compressedFile = FileCompression.Compress(file);
            sendMessage(compressedFile);
            outer_uri = null;
        }

//        loadedArrayList = new ArrayList<>();
//        roomMembersList = new ArrayList<>();
        addresses = new ArrayList<>();
        opponentData = new ArrayList<>();
        phrasesList = new ArrayList<>();

//        addPhrases();
        initializeViews();

        geocoder = new Geocoder(this);
//
//        chatAdapter = new ChatAdapter(loadedArrayList, this);
//        phrasesAdapter = new PhrasesAdapter(phrasesList, this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        phrases_layoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(chatAdapter);
//        recyclerView.addOnScrollListener(new QiscusChatScrollListener(layoutManager, this));
//
//        phrases_recyclerView = findViewById(R.id.phrases_recycler_view);
//        phrases_recyclerView.setLayoutManager(phrases_layoutManager);
//        phrases_recyclerView.setAdapter(phrasesAdapter);
//
//        userName.setText(getOpponentData().get(0));
//
//        Nirmana.getInstance().get()
//                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_qiscus_avatar))
//                .load(getOpponentData().get(1))
//                .into(avatar);

//        googleClientSetup();
//        loadComments();

        call_btn.setOnClickListener(v -> {
            chatActivityPresenter.getContact(opponentData.get(2));
        });

        back_navigation.setOnClickListener(v -> {

            if (isActivity) {
                finish();
            } else if (!isActivity) {
                startActivity(new Intent(this, HomeScreen.class));
                finish();
            }
        });

        send.setOnClickListener(view -> {
            int text = 4;
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                message = editText.getText().toString().trim();
                sendMessage(text);
            }
        });

        attachment_icon.setOnClickListener(view -> {

            if (attachment_menu.getVisibility() == View.VISIBLE) {
                attachment_menu.setVisibility(View.GONE);
            } else {
                attachment_menu.setVisibility(View.VISIBLE);
            }

            gallery.setOnClickListener(v -> {
                attachment_menu.setVisibility(View.GONE);
                if (hasPermissions(this, FILE_IMAGE_PERMISSION)) {
                    pickImage();
                } else {
                    ActivityCompat.requestPermissions(this, FILE_IMAGE_PERMISSION, REQUEST_IMAGE_PERMISSION);
                }
            });

            loc_pin.setOnClickListener(v -> {
                attachment_menu.setVisibility(View.GONE);
//                locationVerification();
            });

            document.setOnClickListener(v -> {
                attachment_menu.setVisibility(View.GONE);
                if (hasPermissions(this, FILE_IMAGE_PERMISSION)) {
                    pickFile();
                } else {
                    ActivityCompat.requestPermissions(this, FILE_IMAGE_PERMISSION, REQUEST_FILE_PERMISSION);
                }
            });
        });

        fab.setOnLongClickListener(v -> {
            phrases_recyclerView.setVisibility(View.VISIBLE);
            attachment_menu.setVisibility(View.GONE);
            return true;
        });

//        QiscusPusherApi.getInstance().listenUserStatus(getOpponentData().get(2));

    }

    public void initializeViews() {
        send = findViewById(R.id.send);
        fab = findViewById(R.id.fab);
        back_navigation = findViewById(R.id.back_navigation);
        editText = findViewById(R.id.editText);
        toolbar =  findViewById(R.id.toolbar);
        userName = findViewById(R.id.user_name);
        attachment_icon = findViewById(R.id.attachment);
        call_btn = findViewById(R.id.call_btn);
        avatar = findViewById(R.id.avatar);
        status = findViewById(R.id.status);
        attachment_menu = findViewById(R.id.attach_layout);
        gallery = findViewById(R.id.gallery);
        loc_pin = findViewById(R.id.location);
        document = findViewById(R.id.document);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
    }

    private boolean hasPermissions(Context context, String... perms) {
        // Always return true for SDK < Marshmallow, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.w("TAG", "hasPermissions: API version < M, returning true by default");
            return true;
        }

        for (String perm : perms) {

            boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);

            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    private void pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimetypes = {"application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/msword"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_IMAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }

        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (gpsEnabled()) {
//                int LOCATION = 5;
//                sendMessage(LOCATION);
//            } else {
////                enableGps();
//            }
        }

        if (requestCode == REQUEST_FILE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickFile();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            try {
                file = FileCompression.from(data.getData());
                File compressedFile = FileCompression.Compress(file);
                sendMessage(compressedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_LOCATION && resultCode == RESULT_OK) {
//            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (mLastLocation != null) FetchAddressIntentService.startService(getApplicationContext(), mLastLocation, new AddressResultReceiver(new Handler()));
        } else if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK) {
            try {
                file = FileCompression.from(data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendMessage(file);
        }
    }

    private List<String> getOpponentData() {

//        roomMembersList = qiscusChatRoom.getMember();
//
//        for (QiscusRoomMember member : roomMembersList) {
//            if (member.getEmail().equalsIgnoreCase(USER))
//                continue;
//            else
//                opponentData.add(member.getUsername());
//            opponentData.add(member.getAvatar());
//            opponentData.add(member.getEmail());
//
//            return opponentData;
//        }

        return null;
    }

    private void sendMessage(int type) {

        switch (type) {

            case TEXT:
//                qiscusComment = QiscusComment.generateMessage(qiscusChatRoom.getId(), message);
//                QiscusApi.getInstance().postComment(qiscusComment)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(comment -> {
//                            editText.setText("");
//                            loadedArrayList.add(0, comment);
//                            chatAdapter.notifyDataSetChanged();
//                            recyclerView.smoothScrollToPosition(0);
//                        }, throwable -> {
//                            Log.d("ChatActivity", "Error");
//                        });
                break;

            case LOCATION:
                //All 4 mandatory on qiscuslocation

                String address = generateAddress(this.location);
//
//                QiscusLocation location = new QiscusLocation();
//                location.setName("Location");
//                location.setAddress(address);
//                location.setLatitude(this.location.getLatitude());
//                location.setLongitude(this.location.getLongitude());
//
//                LatLng latLng = new LatLng(this.location.getLatitude(), this.location.getLongitude());
//
//                startActivity(new Intent(this, MapsActivity.class).putExtra("lat_lng", latLng)
//                        .putExtra("address", address)
//                        .putExtra("chat_room", qiscusChatRoom)
//                        .putExtra("location", location));

                recyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    private String generateAddress(Location location) {

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (geocoder.isPresent() && addresses.size() != 0) {
            StringBuilder sb = new StringBuilder();
            Address s = addresses.get(0);
            sb.append(s.getAddressLine(0) + "\n");
            return sb.toString();
        } else {
            Toast.makeText(this, "Address not found...", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void sendMessage(File compressedFile) {

        progressDialog.show();

//        QiscusComment qiscusComment = QiscusComment.generateFileAttachmentMessage(qiscusChatRoom.getId(), compressedFile.getPath(), null, file.getName());
//
//        QiscusApi.getInstance()
//                .uploadFile(compressedFile, progress -> qiscusComment.setProgress((int) progress))
//                .flatMap(uri -> {
//                    qiscusComment.updateAttachmentUrl(uri.toString());
//                    return QiscusApi.getInstance().postComment(qiscusComment);
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnCompleted(()->{
//                    progressDialog.dismiss();
//                })
//                .subscribe(comment -> {
//                    // here can be a bug, because the attachment_icon view appears in the chat before the
//                    //document is downloaded. If the user clicks the view before download then there will be toast " File not found..."
//
//                    if (!comment.isImage()) downloadDocument(comment.getAttachmentUri(), comment.getAttachmentName());
//                    loadedArrayList.add(0, comment);
//                    chatAdapter.notifyDataSetChanged();
//                    recyclerView.smoothScrollToPosition(0);
//                    Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
//                });
//    }
//
//    private void loadComments() {
//        Observable.merge(getInitRoomData(), getLocalComments(0, true)
//                .map(comments -> Pair.create(qiscusChatRoom, comments)))
//                .filter(qiscusChatRoomListPair -> qiscusChatRoomListPair != null)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(roomData -> {
//                    QiscusAndroidUtil.runOnUIThread(() -> {
//                        loadedArrayList.addAll(roomData.second);
//                        chatAdapter.notifyDataSetChanged();
//                    });
//                }, throwable -> {
//                    throwable.printStackTrace();
//                });
//    }
//
//    private Observable<Pair<QiscusChatRoom, List<QiscusComment>>> getInitRoomData() {
//        return QiscusApi.getInstance().getChatRoomComments(qiscusChatRoom.getId())
//                .doOnError(throwable -> throwable.printStackTrace())
//                .doOnNext(roomData -> {})
//                .subscribeOn(Schedulers.io())
//                .onErrorReturn(throwable -> null);
//    }
//
//    // making no use
//    private Observable<List<QiscusComment>> getLocalComments(int count, boolean forceFailedSendingComment) {
//        return QiscusCore.getDataStore().getObservableComments(qiscusChatRoom.getId(), 2 * count)
//                .flatMap(Observable::from)
//                .toSortedList()
//                .map(comments -> {
//                    if (comments.size() > count) {
//                        return comments.subList(0, count);
//                    }
//                    return comments;
//                })
//                .doOnNext(comments -> {
//                })
//                .subscribeOn(Schedulers.io());
//    }
//
//    //triggered whenever a comment is attached in the chatroom
//    //good logic yaaaxi :)
//    //special quality of OR used, if isMap is true it won't check the next statement
//    @Subscribe
//    public void onCommentReceivedEvent(QiscusCommentReceivedEvent event) {
//
//        boolean isMap = false;
//        QiscusComment receivedQiscusComment = event.getQiscusComment();
//        retrieved = event.getQiscusComment().getUniqueId();
//
//        if (receivedQiscusComment.getLocation() != null) isMap = true;
//
//        if ((receivedQiscusComment.getRoomId() == qiscusChatRoom.getId()) && !(semaphore.equals(retrieved)) && (isMap || !(receivedQiscusComment.getSenderEmail().equals(USER)))) {
//            semaphore = retrieved;
//            loadedArrayList.add(0, event.getQiscusComment());
//            chatAdapter.notifyDataSetChanged();
//            recyclerView.smoothScrollToPosition(0);
//        }
//    }
//
//    @Subscribe
//    public void onUserStatusChanged(QiscusUserStatusEvent event) {
//        if (event.isOnline()) {
//            status.setText("Online");
//        } else {
//            status.setText("last seen " + QiscusDateUtil.getRelativeTimeDiff(event.getLastActive()));
//        }
//    }
//
//    @Override
//    public void onTopOffListMessage() {
//        if (!isLoading) {
//            isLoading = true;
//            loadPreviousComments();
//        }
//    }
//
//    @Override
//    public void onMiddleOffListMessage() {
//    }
//
//    @Override
//    public void onBottomOffListMessage() {
//
//    }
//
//    private void loadPreviousComments() {
//
//        long lastCommentId = (loadedArrayList.get(loadedArrayList.size() - 1)).getId();
//        QiscusApi.getInstance().getComments(qiscusChatRoom.getId(), lastCommentId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnCompleted(() -> {
//                    isLoading = false;
//                })
//                .subscribe(comment -> {
//                    loadedArrayList.add(comment);
//                    chatAdapter.notifyDataSetChanged();
//                }, throwable -> {
//                    Log.d("ChatActivity", "Previous comment not loaded");
//                });
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//        Log.i("None", "I am onConnected");
//
//        locationRequest = LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(0);
//        locationRequest.setFastestInterval(0);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        // PendingResult<Status> reslut = LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this::onLocationChanged);
//
//    }
//
//    private void locationVerification() {
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, LOCATION_PERMISSION, REQUEST_LOCATION_PERMISSION);
//        } else if (!gpsEnabled()) {
//            enableGps();
//        } else {
//            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//            if (mLastLocation != null) FetchAddressIntentService.startService(getApplicationContext(), mLastLocation, new AddressResultReceiver(new Handler()));
//        }
//    }
//
//    private boolean gpsEnabled() {
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        return statusOfGPS;
//    }
//
//    private void enableGps() {
//        if (googleApiClient != null && googleApiClient.isConnected()) {
//
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(10000);
//
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//
//            builder.setAlwaysShow(true);
//
//            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//
//            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
//                @Override
//                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//                    final Status status = locationSettingsResult.getStatus();
//                    switch (status.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            try {
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                status.startResolutionForResult(
//                                        ChatActivity.this, REQUEST_LOCATION);
//                            } catch (IntentSender.SendIntentException e) {
//                                // Ignore the error.
//                                e.printStackTrace();
//                            }
//                            break;
//                    }
//                }
//            });
//
//        }
//    }
//
//    private void googleClientSetup() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//    }
//
//    private void addPhrases() {
//        phrasesList.add(getString(R.string.line1));
//        phrasesList.add(getString(R.string.line2));
//        phrasesList.add(getString(R.string.line3));
//        phrasesList.add(getString(R.string.line4));
//        phrasesList.add(getString(R.string.line5));
//        phrasesList.add(getString(R.string.line6));
//        phrasesList.add(getString(R.string.line7));
//        phrasesList.add(getString(R.string.line8));
//    }
//
//    private void downloadDocument(Uri downloadUri, String attachmentName) {
//
//        File f = new File(download_file_path + attachmentName);
//
//        if (!f.exists()) {
//            QiscusApi.getInstance()
//                    .downloadFile(downloadUri.toString(), attachmentName, total -> {
//                        // here you can get the progress total downloaded
//                    })
//                    .doOnNext(document -> {
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(document -> {
//                        Toast.makeText(this, "Saved...", Toast.LENGTH_LONG).show();
//                    }, throwable -> {
//                        //failed
//                    });
//        }
//    }
//
//    @Override
//    public void onImageClick(Uri uri) {
//
//        EditPicFragment editPicFragment = new EditPicFragment();
//        Bundle bundle1 = new Bundle();
//        bundle1.putString(Constant.KEY_IMAGES, String.valueOf(uri));
//        bundle1.putString(Constant.KEY_TYPE, "cover");
//        editPicFragment.setArguments(bundle1);
//        editPicFragment.show(getSupportFragmentManager(), "EditPicFragment");
//    }
//
//    @Override
//    public void onDocumentClick(Uri downloadUri, String sender, String attachmentName) {
//
//        File f = new File(String.valueOf(download_file_path + attachmentName));
//        Log.e("Environment", f.toString());
//
//        if (f.exists()) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", f));
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Intent intentChoosing = Intent.createChooser(intent, "Open File");
//            try {
//                startActivity(intentChoosing);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(this, "No application found to open file...", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//
//            if (sender.equalsIgnoreCase(USER)) {
//                Toast.makeText(this, "File not found...", Toast.LENGTH_SHORT).show();
//            } else {
//                downloadDocument(downloadUri, attachmentName);
//            }
//        }
//    }
//
//    @Override
//    public void onMapClick(String address) {
//        String locationToSet = "http://maps.google.co.in/maps?q=" + address;
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationToSet));
//
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "No suitable application available...", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onLinkClick(String url) {
//
//        if (!url.startsWith("http") && !url.startsWith("https")) {
//            url = "http://" + url;
//        }
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Can't find suitable application", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onPhrasesClick(int position) {
//        editText.setText(phrasesList.get(position));
//        phrases_recyclerView.setVisibility(View.GONE);
//    }
//
//    public class AddressResultReceiver extends ResultReceiver {
//
//        @SuppressLint("RestrictedApi")
//        public AddressResultReceiver(Handler handler) {
//            super(handler);
//        }
//
//        @Override
//        protected void onReceiveResult(int resultCode, final Bundle resultData) {
//            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
//                location = resultData.getParcelable(FetchAddressIntentService.RESULT_LOCATION);
//                int LOCATION = 5;
//                sendMessage(LOCATION);
//            } else if (resultCode == FetchAddressIntentService.FAILURE_RESULT) {
//
//            }
//
//        }
    }
}

