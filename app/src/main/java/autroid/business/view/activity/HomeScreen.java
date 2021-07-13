package autroid.business.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

//import com.qiscus.sdk.Qiscus;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import autroid.business.NotificationCounter;
import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.BusinessPlanResponse;
import autroid.business.model.bean.NavigationGroupBE;
import autroid.business.model.realm.UserRealm;
import autroid.business.model.request.RefreshTokenRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.model.response.LoginResponse;
import autroid.business.presenter.HomeScreenPresenter;
import autroid.business.realm.RealmController;
import autroid.business.service.FetchAddressIntentService;
import autroid.business.service.RefreshFcmTokenService;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.customviews.CircularTextView;
import autroid.business.view.fragment.BusinessOverviewFragment;
import autroid.business.view.fragment.AnalyticsFragment;
import autroid.business.view.fragment.ChatConverationFragment;
import autroid.business.view.fragment.HomeFragment;
import autroid.business.view.fragment.booking.BookingsPagerFragment;
import autroid.business.view.fragment.carpurchase.PurchaseCarFragment;
import autroid.business.view.fragment.carsales.BookingCategoryFragment;
import autroid.business.view.fragment.carsales.MyBookingsFragment;
import autroid.business.view.fragment.carsales.MyGarageFragment;
import autroid.business.view.fragment.jobcard.JobCardDetailFragment;
import autroid.business.view.fragment.jobcard.JobsPagerFragment;
import autroid.business.view.fragment.NotificationListFragment;
import autroid.business.view.fragment.leadgeneration.LeadGenerationPagerFragment;
import autroid.business.view.fragment.leads.LeadsPagerFragment;
import autroid.business.view.fragment.profile.ProfilePagerFragment;
import autroid.business.view.fragment.search.MainSearchFragment;
import autroid.business.view.fragment.search.SearchCarFragment;
import io.realm.Realm;
import io.realm.RealmResults;

import static autroid.business.utils.Utility.REQUEST_CAMERA_IMAGE;
import static autroid.business.utils.Utility.cameraPermissions;


public class HomeScreen extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FragmentManager.OnBackStackChangedListener {

    public NavigationView navigationView;

    private DrawerLayout drawerLayout;

    public int currentFrameId;

    RelativeLayout mTabHome;
    Toolbar toolbar;

    private String tab[] = {"home", "drawer"};
    private String currentTab;

    LinearLayout  btnSearch;
    RelativeLayout btnNotification;

    private GoogleApiClient mGoogleApiClient;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private AddressCallback mCallback;

    final static int REQUEST_LOCATION = 199;

    private static final int APP_UPDATE_REQUEST = 99;

    TextView tvTitle;

    CircularTextView mNotificationCount;

    private FrameLayout mHomeLayout, mDrawerLayout;

    RealmController mRealmController;

    Realm mRealm;

    HomeScreenPresenter mPresenter;

    boolean isSpinnerTouched = false;

    Spinner mSpnUser;
    TextView mName, mMobile, mOptionView;
    ImageView mImage, mChat, mNotification, mSearch, mResend;

    String fcmToken;

    PopupMenu popup;

    private HashMap<String, Integer> hashMap;

    private static final java.lang.String TAG = HomeScreen.class.getSimpleName();

    ProgressDialog mProgressBar;

    private FirebaseAnalytics mFirebaseAnalytics;

    boolean isTechnician=false;


    TextView tv_notificationCount;
    NotificationCounter notificationCounter;

    @Override
    protected void onResume() {
        super.onResume();

        AppUpdateManager appUpdateManager=AppUpdateManagerFactory.create(this);

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if(appUpdateInfo.installStatus()==InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate(appUpdateManager);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        //otpReceiver();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
//
//        mResend=(ImageView)initializeViews(R.id.img_estimate);
//        mResend.setOnClickListener(v->otpReceiver());

        mProgressBar = new ProgressDialog(HomeScreen.this);

        tv_notificationCount=findViewById( R.id.tv_countNotification );

        notificationCounter=new NotificationCounter(findViewById( R.id.bell ) );

        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        fcmToken = preferenceManager.getStringPreference(this, Constant.SP_FCM_TOKEN);
        if (fcmToken != null) {
            try {
                Intent intent = new Intent(this, RefreshFcmTokenService.class);
                intent.putExtra(Constant.KEY_FCM_TOKEN, fcmToken);
                startService(intent);
            } catch (IllegalStateException e) {

            } catch (Exception e) {

            }
        }

        initialize();
        navigationWork();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        makeHomeVisible();
        //addFragment(new OffersFragment(), "OffersFragment", true, false, null, currentFrameId);

        Intent intent = getIntent();

        if (intent != null && Intent.ACTION_SEND.equals(intent.getAction()) && intent.getType().startsWith("image/")) {
            Bundle bundle = new Bundle();
            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            bundle.putString(Constant.KEY_TYPE, String.valueOf(uri));
            makeDrawerVisible();
            addFragment(new ChatConverationFragment(), "ChatConverationFragment", true, false, bundle, currentFrameId);
        } else if (intent != null) {
            if (intent.hasExtra("isNotification")) {
                if (intent.getBooleanExtra("isNotification", false)) {

                    if (intent.getStringExtra("activity").equals("lead")) {
                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, bundle, currentFrameId);
                    }
                    else if (intent.getStringExtra("activity").equals("booking")) {
                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_ID, intent.getStringExtra("source"));
                        addFragment(new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle,currentFrameId);
                        /*if (!PreferenceManager.getInstance().getStringPreference(getApplicationContext(), Constant.SP_CRE).equalsIgnoreCase("CRE")) {
                            Bundle bundle = new Bundle();
                            addFragment(new BookingsPagerFragment(), "BookingsPagerFragment", true, false, bundle, currentFrameId);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.KEY_ID, 2);
                            addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, bundle, currentFrameId);
                        }*/

                    } else if (intent.getStringExtra("activity").equals("jobcard")) {

                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_ID, intent.getStringExtra("source"));
                        addFragment(new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle,currentFrameId);
                    }

                }
            }
        }

        String businessData = PreferenceManager.getInstance().getStringPreference(getApplicationContext(), Constant.SP_BUSINESS);
        mPresenter.getShowroom();


        updateFCM();


        checkForAppUpdate();
        checkCameraPermission();


        /*if(!PreferenceManager.getInstance().getStringPreference(getApplicationContext(),Constant.SP_CRE).equalsIgnoreCase("CRE")) {
            makeDrawerVisible();
            addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null,currentFrameId);
        }
        else {
            makeDrawerVisible();
            addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, null,currentFrameId);
        }*/
    }

    private void checkCameraPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> denied = new ArrayList<>();

            for (String perm : cameraPermissions) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) denied.add(perm);
            }

            if (denied.size() != 0) {
                requestPermissions(denied.toArray(new String[denied.size()]), REQUEST_CAMERA_IMAGE);
                return;
            }

        }
    }

    private void checkForAppUpdate() {

        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);

        // Checks that the platform will allow the specified type of update.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

          //  Toast.makeText(this, appUpdateInfo.updateAvailability()+"", Toast.LENGTH_SHORT).show();

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ) {

                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, APP_UPDATE_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

                appUpdateManager.registerListener(installState -> {
                    if (installState.installStatus() == InstallStatus.DOWNLOADED) popupSnackbarForCompleteUpdate(appUpdateManager);
                });
            }
        });
    }

    private void otpReceiver() {

        SmsRetrieverClient mClient = null;
        Task<Void> task = null;

        mClient = SmsRetriever.getClient(this);
        task = mClient.startSmsRetriever();

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("HomeScreen", "Success");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("HomeScreen", "Failure");
            }
        });

    }

    private void popupSnackbarForCompleteUpdate(AppUpdateManager appUpdateManager) {
        Snackbar snackbar = Snackbar.make(mHomeLayout,"An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(getResources().getColor(R.color.red_color));
        snackbar.show();
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_UPDATE_COVER) {
            String url = intent.getStringExtra(Constant.KEY_IMAGES);
            // Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.placeholder_thumbnail).into(mImage);
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_COVER_IMAGE) {
            String url = intent.getStringExtra(Constant.KEY_IMAGES);
            //Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.placeholder_thumbnail).into(mImage);
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_NOTIFICATION) {
            Utility.showResponseMessage(mHomeLayout, intent.getStringExtra(Constant.KEY_TYPE));
            setNotificationCount();
        }
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_NOTIFICATION_COUNT) {
            setNotificationCount();
        }
    }

    private void setNotificationCount() {
        int count = PreferenceManager.getInstance().getIntegerPreference(getApplicationContext(), Constant.SP_NOTIFICATION_COUNT);
        if (count == 0) {
            mNotificationCount.setVisibility(View.GONE);
        } else {
            mNotificationCount.setVisibility(View.VISIBLE);
            mNotificationCount.setText(count + "");

            notificationCounter.increaseNumber();
        }
    }


    private void initialize() {

        this.mRealm = RealmController.with(this).getRealm();
        mRealmController = RealmController.getInstance();

        mTabHome = (RelativeLayout) findViewById(R.id.tab_home);
        mTabHome.setOnClickListener(this);


        mSearch = (ImageView) findViewById(R.id.img_search);
        // mSearch.setOnClickListener(this);

        mNotification = (ImageView) findViewById(R.id.img_notification);
        //  mNotification.setOnClickListener(this);

        mOptionView = findViewById(R.id.textViewOptions);
        mOptionView.setOnClickListener(this);

        popup = new PopupMenu(this, mOptionView);
        popup.inflate(R.menu.homescreen_menu);

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.log_out:
                    dialogLogout();
                    //mPresenter.logout();
                    break;
            }

            return false;
        });

        mHomeLayout = (FrameLayout) findViewById(R.id.layout_home);

        mDrawerLayout = (FrameLayout) findViewById(R.id.layout_drawer);


        btnSearch = findViewById(R.id.search_btn);
        btnSearch.setOnClickListener(this);

        btnNotification = (RelativeLayout) findViewById(R.id.notification_btn);
        btnNotification.setOnClickListener(this);

        mNotificationCount = findViewById(R.id.notification_count);
        mNotificationCount.setSolidColor("#ff0000");
        mNotificationCount.setStrokeColor("#666666");

        setNotificationCount();


        mPresenter = new HomeScreenPresenter(this, mHomeLayout);
        mPresenter.getCarItems();

        tvTitle = (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("HOME");


        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
        }

    }


    private void navigationWork() {

        toolbar = findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigation_view);


        View header = LayoutInflater.from(this).inflate(R.layout.layout_drawer_header, null);
        navigationView.addHeaderView(header);

        mSpnUser = (Spinner) header.findViewById(R.id.spn_users);
        mName = (TextView) header.findViewById(R.id.user_name);
        mMobile = (TextView) header.findViewById(R.id.user_number);
        mImage = (ImageView) header.findViewById(R.id.user_img);

        UserRealm loginUser = mRealmController.getAllLoggedInUsers();

        if (loginUser != null) {
            // Picasso.with(getApplicationContext()).load(loginUser.getCover()).placeholder(R.drawable.placeholder_big).into(mImage);
            mName.setText(loginUser.getName());
            // mMobile.setText(loginUser.getContactNo());

            // setQiscusData(loginUser);
        }

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();

                makeDrawerVisible();
                Bundle bundle = new Bundle();
                String businessData = PreferenceManager.getInstance().getStringPreference(getApplicationContext(), Constant.SP_BUSINESS);
                bundle.putString(Constant.KEY_ID, businessData);
                addFragment(new ProfilePagerFragment(), "ProfilePagerFragment", true, false, bundle, currentFrameId);
            }
        });

        setHeaderSpinner();

        mSpnUser.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
       /* View headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        navigationView.addHeaderView(headerView);*/

        actionBarDrawerToggle.syncState();

        /*------------------------------------------------------------------------------------------------*/


    }

    public void setHeaderSpinner() {

        RealmResults<UserRealm> userRealmRealmResults = mRealmController.getAllNotLoggedInUsers();
        final String strUsers[] = new String[userRealmRealmResults.size() + 2];
        strUsers[0] = "Switch Account";
        for (int i = 1; i <= userRealmRealmResults.size(); i++) {
            strUsers[i] = userRealmRealmResults.get(i - 1).getContactNo();
        }

        strUsers[userRealmRealmResults.size() + 1] = "Add Account";

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(this, R.layout.layout_spinner_text_white, strUsers);
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mSpnUser.setAdapter(adapterBrand);

        mSpnUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (!isSpinnerTouched) return;
                // do what you want

                if (position == 0) {
                   /* Intent homescreen=new Intent(getApplicationContext(),LoginActivity.class);
                    homescreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homescreen);
                    finish();*/
                } else if (position == strUsers.length - 1) {
                    Intent homescreen = new Intent(getApplicationContext(), LoginActivity.class);
                    homescreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homescreen);
                    finish();

                } else {
                    UserRealm userRealm = mRealmController.getUserId(parentView.getSelectedItem().toString());
                    refreshToken(userRealm.getId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void callLogOut() {
        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_TOKEN, "");
        Intent intent1 = new Intent(this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                makeDrawerVisible();
                addFragment(new MainSearchFragment(), "MainSearchFragment", true, false, null, currentFrameId);
                break;
            case R.id.notification_btn:
                makeDrawerVisible();
                addFragment(new NotificationListFragment(), "NotificationListFragment", true, false, null, currentFrameId);
                break;
                case R.id.textViewOptions:
                popup.show();
                break;

        }
    }

    private void updateFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        mPresenter.sendRegistrationToServer(token);

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        //Toast.makeText(HomeScreenActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void makeHomeVisible() {
        currentFrameId = R.id.layout_home;
        currentTab = tab[0];
        mHomeLayout.setVisibility(View.VISIBLE);
        mDrawerLayout.setVisibility(View.GONE);
        selectedDivider(R.id.selected_home);
        clearStackLocal();
    }

    public void makeDrawerVisible() {
       /* mHomeLayout.setVisibility(View.GONE);
        mCarLayout.setVisibility(View.GONE);
        mProductLayout.setVisibility(View.GONE);
        mServiceLayout.setVisibility(View.GONE);
        mOfferLayout.setVisibility(View.GONE);*/
        currentFrameId = R.id.layout_drawer;
        currentTab = tab[1];
        mDrawerLayout = (FrameLayout) findViewById(R.id.layout_drawer);
        mDrawerLayout.setVisibility(View.VISIBLE);
        // selectedDivider(R.id.icon_only);
    }

    private void selectedDivider(int icon) {
        int imageArry[] = {R.id.selected_home, R.id.selected_car, R.id.selected_product, R.id.selected_service, R.id.selected_offer};
        for (int i = 0; i < imageArry.length; i++) {
            if (icon == imageArry[i]) {
                findViewById(icon).setVisibility(View.VISIBLE);
            } else {
                findViewById(imageArry[i]).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBackStackChanged() {

        if (currentTab == tab[0]) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_home);
            if (fragment instanceof HomeFragment) {
                tvTitle.setText("Business Overview");
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_drawer);

            if (fragment instanceof BookingCategoryFragment) {
                Intent data = new Intent();
                data.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_BOOKING_CATEGORY_VISIBLE);
                Events.SendEvent sendEvent =
                        new Events.SendEvent(data);
                GlobalBus.getBus().post(sendEvent);
                //Toast.makeText(this, "BookingCategoryFragment", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        Utility.hideSoftKeyboard(this);
        FragmentManager mManager = getSupportFragmentManager();

        if (mManager.getBackStackEntryCount() > 1) {
            mManager.popBackStackImmediate();
        } else {
            // super.onBackPressed();

            finish();
        }

        if (mManager.getBackStackEntryCount() == 1) {
                if(!isTechnician) {
                    currentTab = tab[0];
                    makeHomeVisible();
                }
                else {
                    currentTab = tab[1];
                    makeDrawerVisible();
                }
        }
    }

    /**
     * Method will add fragment on empty layout
     *
     * @param fragment
     * @param tag
     * @param addtobackstack
     * @param bundle
     */
    public void addFragment(Fragment fragment, String tag, boolean addtobackstack, boolean isTransaction, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(R.id.layout_home, fragment, tag);
        if (addtobackstack)
            transaction.addToBackStack(tag);
        else
            transaction.disallowAddToBackStack();
        transaction.commit();
    }

    public void addFragment(Fragment fragment, String tag, boolean addtobackstack, boolean isTransaction, Bundle bundle, int frameLayout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        transaction.add(frameLayout, fragment, tag);
        if (addtobackstack)
            transaction.addToBackStack(tag);
        else
            transaction.disallowAddToBackStack();
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        GlobalBus.getBus().register(this);
    }

    private boolean checkGooglePlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {

            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }

        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                    getLocation();


                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    String permission = permissions[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            Toast.makeText(getApplicationContext(), permission, Toast.LENGTH_SHORT).show();
                        }
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


   /* private void getLocation() {
        try {
            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(this, "Enable GPS for accurate data", Toast.LENGTH_SHORT).show();
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                FetchAddressIntentService.startService(getApplicationContext(),mLastLocation,new AddressResultReceiver(new Handler()));
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }*/

    public void onItemSuccess(CarItemsResponse carItemsResponse) {

        if (carItemsResponse != null) {

            Gson gson = new Gson();

            //System.out.println(gson.toJson(carItemsResponse));

            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_CAR_ITEMS, gson.toJson(carItemsResponse));
        }
    }

    public void onLogoutSuccess(BaseResponse baseResponse) {

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent("logout", params);

        mRealmController.deleteLoggedInUsers();
        callLogOut(baseResponse.getResponseMessage());
    }

    public void callLogOut(String logout) {
//        Qiscus.clearUser();
        Toast.makeText(getApplicationContext(), logout, Toast.LENGTH_LONG).show();
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_TOKEN, "");
        Intent intent1 = new Intent(this, LoginActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
        finish();
    }

    public void onSuccess(BusinessPlanResponse showroomProfileResponse) {

       /* if (showroomProfileResponse.getGetData().getChat()) {
            btnChat.setVisibility(View.GONE);

           // if (!BuildConfig.DEBUG) {
                Qiscus.setUser(showroomProfileResponse.getGetData().getBusiness().getId(), showroomProfileResponse.getGetData().getBusiness().getUuid())
                        .withUsername(showroomProfileResponse.getGetData().getBusiness().getName())
                        .withAvatarUrl(showroomProfileResponse.getGetData().getBusiness().getAvatar_url())
                        .save(new QiscusCore.SetUserListener() {
                            @Override
                            public void onSuccess(QiscusAccount qiscusAccount) {
                                //on success followup
                                Log.e("Qiscus", qiscusAccount.getUsername());
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                //do anything if error occurs
                                throwable.printStackTrace();
                            }
                        });

                Qiscus.setProviderAuthorities("careager.business.provider");
          //  }
        } else {
            btnChat.setVisibility(View.GONE);
        }*/



        Gson gson = new Gson();
        String jsonString = gson.toJson(showroomProfileResponse.getGetData().getManifest());
        PreferenceManager.getInstance().putStringPreference(getApplicationContext(),Constant.SP_MANIFEST,jsonString);
        //Log.d("Json",jsonString);

        if(PreferenceManager.getInstance().getStringPreference(this,Constant.SP_ROLE).equalsIgnoreCase("Technician")){
            makeDrawerVisible();
            isTechnician=true;
            addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null, currentFrameId);
//            mChat.setVisibility(View.GONE);
            mSearch.setVisibility(View.GONE);

        }else {
            isTechnician=false;
            addFragment(new BusinessOverviewFragment(), "BusinessOverviewFragment", true, false, null, currentFrameId);
            setNavigation(showroomProfileResponse.getGetData().getNavigation());

        }





    }

    public void goawsHome(View view) {
        startActivity( new Intent(getApplicationContext(), AwsHomeActivity.class ) );
    }

    @SuppressLint("RestrictedApi")
    public class AddressResultReceiver extends ResultReceiver {

        @SuppressLint("RestrictedApi")
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                String address = resultData.getString(FetchAddressIntentService.RESULT_ADDRESS);
                Location location = resultData.getParcelable(FetchAddressIntentService.RESULT_LOCATION);
                setAddress(address, location);
                Log.e("address receive", address);
            } else if (resultCode == FetchAddressIntentService.FAILURE_RESULT) {

            }

        }
    }

    public interface AddressCallback {
        void getAddress(String address, Location location);
    }

    public void setAddress(String address, Location location) {
        mCallback.getAddress(address, location);
    }

    private void refreshToken(String id) {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setId(id);
        mPresenter.validateUser(refreshTokenRequest);
    }


    public void onLoginSuccess(LoginResponse loginResponse) {
        if (drawerLayout != null) {
            drawerLayout.closeDrawers();
        }
        Utility.setAuthToken(loginResponse.getGetLoginData().getToken());

        UserRealm userRealm = mRealmController.checkUser(loginResponse.getGetLoginData().getUser().getId());

        if (userRealm == null) {
            UserRealm mUserRealm = new UserRealm();
            mUserRealm.setId(loginResponse.getGetLoginData().getUser().getId());
            mUserRealm.setAvatar(loginResponse.getGetLoginData().getUser().getAvatar());
            mUserRealm.setAvatar_url(loginResponse.getGetLoginData().getUser().getAvatar());
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

            mRealmController.updateRecordUserLoggidIn(loginResponse.getGetLoginData().getUser().getId());
        }

        //     setQiscusData(loginUser);


        mRealmController.updateRecordUserNotLoggidIn(loginResponse.getGetLoginData().getUser().getId());

        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.putStringPreference(getApplicationContext(), Constant.SP_USERID, loginResponse.getGetLoginData().getUser().getId());

        RealmResults<UserRealm> userRealmRealmResults = mRealmController.getAllNotLoggedInUsers();
        String strUsers[] = new String[userRealmRealmResults.size() + 2];
        strUsers[0] = "Switch Account";
        for (int i = 1; i <= userRealmRealmResults.size(); i++) {
            strUsers[i] = userRealmRealmResults.get(i - 1).getContactNo();
        }

        strUsers[userRealmRealmResults.size() + 1] = "Add Account";

        UserRealm loginUser = mRealmController.getAllLoggedInUsers();
        if (loginUser != null) {
            //Picasso.with(getApplicationContext()).load(loginUser.getAvatar()).placeholder(R.drawable.placeholder_big).into(mImage);
            mName.setText(loginUser.getName());
            //mMobile.setText(loginUser.getContactNo());
            //setQiscusData(loginUser);
        }

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(this, R.layout.layout_spinner_text_white, strUsers);
        adapterBrand.setDropDownViewResource(R.layout.layout_spinner_text);
        mSpnUser.setAdapter(adapterBrand);

        makeHomeVisible();

        Intent intent = new Intent();
        intent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_CHANGE_ACCOUNT);
        Events.SendEvent sendEvent =
                new Events.SendEvent(intent);
        GlobalBus.getBus().post(sendEvent);

    }

    public void getCurrentLocation(AddressCallback mCallback) {
        this.mCallback = mCallback;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant

                return;
            } else {
                getLocation();
            }
    }

    private void getLocation() {
        try {
            if (noLocation()) {
               /* LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Toast.makeText(this, "Enable GPS for accurate data", Toast.LENGTH_SHORT).show();
                    noLocation();

                    return;
                }
*/
              /*  Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    FetchAddressIntentService.startService(getApplicationContext(),mLastLocation,new AddressResultReceiver(new Handler()));
                }*/
            } else {
                onLocationAvailable();
            }


        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void onLocationAvailable() {
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                FetchAddressIntentService.startService(getApplicationContext(), mLastLocation, new AddressResultReceiver(new Handler()));
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();

            enableLoc();
            return true;
        }
        return false;
    }

    private void enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            // Timber.v("Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            mGoogleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show th  e dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    HomeScreen.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        //finish();
                        break;
                    }
                    default: {
                        onLocationAvailable();
                        break;
                    }
                }
                break;
        }
    }

   /* private void setQiscusData(UserRealm loginUser){

    }*/

    public void clearStackLocal() {
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = (count - 1); i >= 1; i--) {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
                String tag = entry.getName();
                FragmentManager mManager = getSupportFragmentManager();
                Fragment fragment = mManager.findFragmentByTag(tag);
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
                mManager.popBackStack();

            }
        } catch (IllegalStateException e) {

        } catch (Exception e) {

        }
    }

    public void clearStackLocalAndShowLeads() {
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = (count - 1); i >= 1; i--) {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
                String tag = entry.getName();
                FragmentManager mManager = getSupportFragmentManager();
                Fragment fragment = mManager.findFragmentByTag(tag);
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
                mManager.popBackStack();

            }

            makeDrawerVisible();
            addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, null, currentFrameId);


        } catch (IllegalStateException e) {

        } catch (Exception e) {

        }
    }

    public void disableFabButton(FloatingActionButton button) {
        button.setEnabled(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    button.setEnabled(true);
                });
            }
        }, 2000);
    }

    public void disableButton(Button button) {
        button.setEnabled(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    button.setEnabled(true);
                });
            }
        }, 2000);
    }


    public void disableTextview(TextView textView) {
        textView.setEnabled(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    textView.setEnabled(true);
                });
            }
        }, 2000);
    }

    private void setNavigation(ArrayList<NavigationGroupBE> navigation) {

        Menu m = navigationView.getMenu();
        setHashMap();

        for (int i = 0; i < navigation.size(); i++) {

            SubMenu topChannelMenu = m.addSubMenu(navigation.get(i).getModule());
            topChannelMenu.setIcon(R.drawable.ic_drawer_home); // add icon with drawable resource

            for (int j = 0; j < navigation.get(i).getGroup().size(); j++) {

                //  Log.d("TAG",""+hashMap.get(navigation.get(i).getGroup().get(j).getTag()));
                if (hashMap.containsKey(navigation.get(i).getGroup().get(j).getTag())) {
                    topChannelMenu.add(i, j, j, navigation.get(i).getGroup().get(j).getAction()).setIcon(hashMap.get(navigation.get(i).getGroup().get(j).getTag()));
                } else {
                    topChannelMenu.add(i, j, j, navigation.get(i).getGroup().get(j).getAction()).setIcon(R.drawable.ic_drawer_home);

                }

                //int resID = getResources().getIdentifier("ic_drawer_aboutus" , "drawable", getPackageName());
                //topChannelMenu.add(resID);
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();
                Bundle bundle = new Bundle();
                //Check to see which item was being clicked and perform appropriate action
                //Toast.makeText(HomeScreen.this, menuItem.getGroupId()+" group "+menuItem.getItemId()+"id", Toast.LENGTH_SHORT).show();
                if (navigation.get(menuItem.getGroupId()).getGroup().get(menuItem.getItemId()).getEnable()) {
                    switch (navigation.get(menuItem.getGroupId()).getGroup().get(menuItem.getItemId()).getTag()) {
                        case "leads":
                            if (!checkVisibility("LeadsPagerFragment")) {
                                makeDrawerVisible();
                                addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, null, currentFrameId);
                            }
                            break;

                        case "leads-generation":
                            if (!checkVisibility("LeadGenerationPagerFragment")) {
                                makeDrawerVisible();
                                addFragment(new LeadGenerationPagerFragment(), "LeadGenerationPagerFragment", true, false, null, currentFrameId);
                            }
                            break;
                        case "my-booking":
                            if (!checkVisibility("MyBookingsFragment")) {
                                makeDrawerVisible();
                                addFragment(new MyBookingsFragment(), "MyBookingsFragment", true, false, null, currentFrameId);
                            }
                            break;

                        case "cars":
                            if (!checkVisibility("MyGarageFragment")) {
                                makeDrawerVisible();
                                addFragment(new MyGarageFragment(), "MyGarageFragment", true, false, null, currentFrameId);
                            }
                            break;

                        case "orders":
                            showToast("This module is available in web only.");
                           // Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();

                            break;

                        case "cars_purchase":
                            if (!checkVisibility("PurchaseCarFragment")) {
                                makeDrawerVisible();
                                addFragment(new PurchaseCarFragment(), "PurchaseCarFragment", true, false, null, currentFrameId);
                                //Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case "stock":
                            showToast("This module is available in web only.");

                           // Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();
                            break;
                        case "suppliers":
                            showToast("This module is available in web only.");

                            //Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();
                            break;

                        case "booking":
                            if (!checkVisibility("BookingsPagerFragment")) {
                                makeDrawerVisible();
                                addFragment(new BookingsPagerFragment(), "BookingsPagerFragment", true, false, null, currentFrameId);
                            }
                            break;

                        case "jobs":
                            if (!checkVisibility("JobsPagerFragment")) {
                                makeDrawerVisible();
                                addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null, currentFrameId);
                            }
                            break;

                        case "business-overview":
                            makeHomeVisible();
                            // addFragment(new BusinessOverviewFragment(), "BusinessOverviewFragment", true, false, null, currentFrameId);
                            // if(!checkVisibility()) addFragment(new BusinessOverviewFragment(), "BusinessOverviewFragment", true, false, null, currentFrameId);
                            break;

                        case "analytic":
                            if (!checkVisibility("AnalyticsFragment")) {
                                makeDrawerVisible();
                                addFragment(new AnalyticsFragment(), "AnalyticsFragment", true, false, null, currentFrameId);
                            }
                            break;
                        case "labour":
                            makeDrawerVisible();
                            addFragment(new SearchCarFragment(), "SearchCarFragment", true, false, null, currentFrameId);
                            break;
                        case "expense":
                            showToast("This module is available in web only.");

                            //Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();

                            break;
                        case "invoices":
                            showToast("This module is available in web only.");

                          //  Toast.makeText(HomeScreen.this, "Coming Soon...", Toast.LENGTH_LONG).show();
                            break;
                    }
                } else {
                    Toast.makeText(HomeScreen.this, "Upgrade your pack", Toast.LENGTH_SHORT).show();
                }
//                switch (menuItem.getItemId()) {
//
//
////                    case R.id.drawer_home:
////                        makeHomeVisible();
////                        break;
////                    case R.id.drawer_location:
////                        makeDrawerVisible();
////                        addFragment(UpdateBusinessLocationFragment.newInstance(), FragmentTags.FRAGMENT_UPDATE_BUSINESS_LOCATION,true,false,null,currentFrameId);
////                        break;
////                    case R.id.drawer_businesses:
////                        makeDrawerVisible();
////                        addFragment(new SavedBusinessesFragment(),"SavedBusinessesFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.drawer_settings:
////                        makeDrawerVisible();
////                        addFragment(new SettingsFragment(),"SettingsFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.offers_list:
////                        makeDrawerVisible();
////                        addFragment(OffersFragment.newInstance(),FragmentTags.FRAGMENT_OFFERS,true,false,null,currentFrameId);
////                        break;
////                    case R.id.packages_list:
////                        makeDrawerVisible();
////                        addFragment(new PurchasedPackagesFragment(),"PurchasedPackagesFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.inactive:
////                        makeDrawerVisible();
////                        addFragment(new InactiveBookingFragment(),"InactiveBookingFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.jobcard:
////                        makeDrawerVisible();
////                        //addFragment(new JobCardRequirementFragment(),"RequirementFragment",true,false,null,currentFrameId);
////                        addFragment(new JobCardUserFragment(),"JobCardUserFragment",true,false,null,currentFrameId);
////                        //addFragment(new CameraFragment(),"CameraFragment",true,false,null,currentFrameId);
////                        //addFragment(new JobCardCarFragment(),"CarFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.orderlist:
////                        makeDrawerVisible();
////                        addFragment(new OrdersFragment(),"OrdersFragment",true,false,null,currentFrameId);
////                        break;
////                    case R.id.drawer_add_business:
////                        makeDrawerVisible();
////                        addFragment(AddNewBusinessFragment.newInstance(), FragmentTags.FRAGMENT_ADD_BUSINESS_,true,false,null,currentFrameId);
////                        break;
////                    /*case R.id.drawer_leads:
////                        makeDrawerVisible();
////                        addFragment(RequestLeadsFragment.newInstance(), FragmentTags.FRAGMENT_LEADS,true,false,null,R.id.layout_drawer);*/
////                      //  break;
////                    case R.id.drawer_aboutus: {
////                        makeDrawerVisible();
////                        Bundle bundle1 = new Bundle();
////                        bundle1.putString(Constant.KEY_TYPE, ApiURL.ABOUT_US_URL);
////                        addFragment(WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1,currentFrameId);
////                    }
////                        break;
////                    case R.id.drawer_terms: {
////                        makeDrawerVisible();
////                        Bundle bundle1 = new Bundle();
////                        bundle1.putString(Constant.KEY_TYPE, ApiURL.TERMS_CONDITION_URL);
////                        addFragment(WebViewFragment.newInstance(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle1,currentFrameId);
////                    }
////                    break;
////                    case R.id.drawer_logout:
////
////                        mPresenter.logout();
////                        //callLogOut();
////                        break;
//
//                   /* case R.id.jobs:
//                        makeDrawerVisible();
//                        addFragment(new JobCardUserFragment(), "UserFragment", true, false, null,currentFrameId);
//                        break;*/
//
//                    case R.id.leads:
//                        makeDrawerVisible();
//                        addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, null, currentFrameId);
//                        break;
//                    case R.id.bookings:
//                        makeDrawerVisible();
//                        addFragment(new BookingsPagerFragment(), "BookingsPagerFragment", true, false, null, currentFrameId);
//                        break;
//                    case R.id.jobs:
//                        makeDrawerVisible();
//                        addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null, currentFrameId);
//                        break;
//                    case R.id.orders:
//                        makeDrawerVisible();
//                        addFragment(new OrdersFragment(), "OrdersFragment", true, false, null, currentFrameId);
//                        break;
//                    case R.id.car_sales:
//                        makeDrawerVisible();
//                        addFragment(new CarStockFragment(), "CarStockFragment", true, false, null,currentFrameId);
//                        break;
//                    case R.id.lead_generation:
////                        makeDrawerVisible();
////                        addFragment(new JobCardUserFragment(), "JobCardUserFragment", true, false, null,currentFrameId);
////                        break;
//
////                      addFragment(new OffersFragment(),"OffersFragment",true,false,null,currentFrameId);
//
//                }

                return false;
            }
        });
    }

    private boolean checkVisibility(String tag) {

        int topOfStack = (getSupportFragmentManager().getBackStackEntryCount() - 1);

        if (getSupportFragmentManager().getBackStackEntryAt(topOfStack).getName().equalsIgnoreCase(tag))
            return true;

        return false;
    }

    private void dialogLogout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure, you want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("Yes", (dialog, id) -> {
            mPresenter.logout();
            dialog.cancel();
        });

        builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void setHashMap() {

        hashMap = new HashMap<>();
        hashMap.put("business-overview", R.drawable.ic_business_overview);
        hashMap.put("analytic", R.drawable.ic_analytic);
        hashMap.put("leads", R.drawable.ic_leads);
        hashMap.put("leads-generation", R.drawable.ic_leads);
        hashMap.put("stock", R.drawable.ic_stock);
        hashMap.put("suppliers", R.drawable.ic_suppliers);
        hashMap.put("labour", R.drawable.ic_labour);
        hashMap.put("my-booking", R.drawable.ic_my_booking);
        hashMap.put("cars", R.drawable.ic_cars);
        hashMap.put("cars_purchase", R.drawable.ic_cars);
        hashMap.put("orders", R.drawable.ic_orders);
        hashMap.put("booking", R.drawable.ic_booking);
        hashMap.put("jobs", R.drawable.ic_jobs);
        hashMap.put("expense", R.drawable.ic_expense);
        hashMap.put("invoices", R.drawable.ic_invoice);

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

