package autroid.business.aws;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import autroid.business.BuildConfig;
import autroid.business.R;
import autroid.business.aws.crm.aws_leads.LeadsFragment;
import autroid.business.aws.navigation.adapter.CustomExpandableListAdapter;
import autroid.business.aws.navigation.adapter.ExpandableListDataSource;
import autroid.business.aws.navigation.adapter.SettingsActivity;
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
import autroid.business.view.activity.HelpPageActivity;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.LoginActivity;
import autroid.business.view.fragment.AnalyticsFragment;
import autroid.business.view.fragment.BusinessOverviewFragment;
import autroid.business.view.fragment.ChatConverationFragment;
import autroid.business.view.fragment.HomeFragment;
import autroid.business.view.fragment.NotificationListFragment;
import autroid.business.view.fragment.RequestLeadsFragment;
import autroid.business.view.fragment.booking.BookingsPagerFragment;
import autroid.business.view.fragment.carsales.BookingCategoryFragment;
import autroid.business.view.fragment.jobcard.JobCardDetailFragment;
import autroid.business.view.fragment.jobcard.JobsPagerFragment;
import autroid.business.view.fragment.leads.LeadsPagerFragment;
import autroid.business.view.fragment.profile.MyReferralsFragment;
import autroid.business.view.fragment.profile.MyWalletFragment;
import autroid.business.view.fragment.profile.ProfilePagerFragment;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.PrefrenceMangerProfile;
import autroid.business.view.fragment.profile.subscription.SubscriptionFragment;
import autroid.business.view.fragment.search.MainSearchFragment;
import autroid.business.view.fragment.search.SearchCarFragment;
import io.realm.Realm;
import io.realm.RealmResults;

import static autroid.business.utils.Utility.REQUEST_CAMERA_IMAGE;
import static autroid.business.utils.Utility.cameraPermissions;

public class AwsHomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, FragmentManager.OnBackStackChangedListener {

    private GoogleApiClient mGoogleApiClient;
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private AddressCallback mCallback;

    final static int REQUEST_LOCATION = 199;

    private static final int APP_UPDATE_REQUEST = 99;
    public int currentFrameId;

    private DrawerLayout mDrawerLayout;
    private FrameLayout frameLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    private String currentTab;
    private String tab[] = {"home", "drawer"};

    private ExtendedFloatingActionButton btnNew;

    private FrameLayout mHomeLayout;

    RealmController mRealmController;
    Realm mRealm;
    HomeScreenPresenter mPresenter;
    boolean isSpinnerTouched = false;
    Spinner mSpnUser;

    ImageView mImage, mChat, mNotification, mSearch, mResend;
    String fcmToken;
    TextView mOptionView;
    PopupMenu popup;
    private HashMap<String, Integer> hashMap;
    private static final java.lang.String TAG = AwsHomeActivity.class.getSimpleName();
    ProgressDialog mProgressBar;
    private FirebaseAnalytics mFirebaseAnalytics;
    boolean isTechnician = false;

    Toolbar comman_tool;
    private TextView tv_headerName;
    private ImageView iv_headerPic;

    TextView footer_subs, footer_referEarn, footerSettings, footerHelp;

    private ExpandableListView mExpandableListView;
    private ExpandableListAdapter mExpandableListAdapter;
    private List<String> mExpandableListTitle;

    ImageView img;
    private Map<String, List<String>> mExpandableListData;


    @Override
    protected void onResume() {
        super.onResume();

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create( this );

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener( appUpdateInfo->{
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate( appUpdateManager );
            }
        } );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_aws_home );

        img = (ImageView) findViewById( R.id.imageView );
        mDrawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );

        mHomeLayout = findViewById( R.id.layout_home );
        mActivityTitle = getTitle().toString();
//        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        comman_tool = findViewById( R.id.toolbar1 );
        setSupportActionBar( comman_tool );
//        TextView tvTool= findViewById(R.id.toolbar1).findViewById(R.id.toolbar_title);
        ImageView imageNotification = findViewById( R.id.toolbar1 ).findViewById( R.id.img_notification );



        btnNew=findViewById( R.id.extendedNewFab );
        mFirebaseAnalytics = FirebaseAnalytics.getInstance( this );
        mProgressBar = new ProgressDialog( AwsHomeActivity.this );

        PreferenceManager preferenceManager = PreferenceManager.getInstance();

        fcmToken = preferenceManager.getStringPreference( this, Constant.SP_FCM_TOKEN );
        if (fcmToken != null) {
            try {
                Intent intent = new Intent( this, RefreshFcmTokenService.class );
                intent.putExtra( Constant.KEY_FCM_TOKEN, fcmToken );
                startService( intent );
            } catch (IllegalStateException e) {

            } catch (Exception e) {

            }
        }


        mExpandableListView = (ExpandableListView) findViewById( R.id.navList );
        mExpandableListView.setBackgroundResource( R.color.card_color );
        mExpandableListView.setGroupIndicator( this.getResources().getDrawable( R.drawable.coustom_expendable_aerrow ) );
        initItems();
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate( R.layout.aws_nav_header, null, false );
        mExpandableListView.addHeaderView( listHeaderView );

        View listFooterView = inflater.inflate( R.layout.nav_aws_footer, null, false );
        mExpandableListView.addFooterView( listFooterView );

         tv_headerName = listHeaderView.findViewById( R.id.userName );
        TextView tv_home = listHeaderView.findViewById( R.id.home_nav_tv );
        iv_headerPic = listHeaderView.findViewById( R.id.nav_heder_iv );


        iv_headerPic.setOnClickListener( v->{

            mDrawerLayout.closeDrawers();
            makeDrawerVisible();
            Bundle bundle = new Bundle();
            String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
            bundle.putString( Constant.KEY_ID, businessData );
            addFragment( new ProfilePagerFragment(), "ProfilePagerFragment", true, false, bundle, currentFrameId );

        } );


        tv_home.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDrawerLayout.closeDrawers();

            }
        } );

        tv_headerName.setOnClickListener( v->{
            mDrawerLayout.closeDrawers();
            makeDrawerVisible();
            Bundle bundle = new Bundle();
            String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
            bundle.putString( Constant.KEY_ID, businessData );
            addFragment( new ProfilePagerFragment(), "ProfilePagerFragment", true, false, bundle, currentFrameId );
        } );

        imageNotification.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity( new Intent( getApplicationContext(), Notification_Web.class ) );
//                mDrawerLayout.closeDrawers();

                makeDrawerVisible();
                addFragment( new NotificationListFragment(), "NotificationListFragment", true, false, null, currentFrameId );
            }
        } );


        TextView tvVersion = listFooterView.findViewById( R.id.tv_nav_app_version );
        footer_referEarn = listFooterView.findViewById( R.id.footer_tv_referEarn );
        footer_subs = listFooterView.findViewById( R.id.footer_tv_subscription );
        footerHelp = listFooterView.findViewById( R.id.footer_tv_help );
        footerSettings = listFooterView.findViewById( R.id.footer_tv_settings );

        tvVersion.setText( "Version: " + versionName );
        mExpandableListData = ExpandableListDataSource.getData( this );
        mExpandableListTitle = new ArrayList( mExpandableListData.keySet() );

        addDrawerItems();
        setupDrawer();

        footerHelp.setOnClickListener( v->{
            mDrawerLayout.closeDrawers();
            startActivity( new Intent( getApplicationContext(), HelpPageActivity.class ) );

        } );

        footer_subs.setOnClickListener( v->{

            mDrawerLayout.closeDrawers();
            makeDrawerVisible();
            Bundle bundle = new Bundle();
            String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
            bundle.putString( Constant.KEY_ID, businessData );
            addFragment( new SubscriptionFragment(), "SubscriptionFragment", true, false, bundle, currentFrameId );

        } );


        footer_referEarn.setOnClickListener( v->{

            mDrawerLayout.closeDrawers();
            makeDrawerVisible();
            Bundle bundle = new Bundle();
            String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
            bundle.putString( Constant.KEY_ID, businessData );
            addFragment( new MyWalletFragment(), "MyWalletFragment", true, false, bundle, currentFrameId );

        } );


        footerSettings.setOnClickListener( v->{

            mDrawerLayout.closeDrawers();
            Intent intent = new Intent( getApplicationContext(), SettingsActivity.class );
            startActivity( intent );

        } );


        btnNew.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AwsHomeActivity.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_new_aws, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } );

        mFirebaseAnalytics = FirebaseAnalytics.getInstance( this );
        mProgressBar = new ProgressDialog( AwsHomeActivity.this );


        PrefrenceMangerProfile profile1 = new PrefrenceMangerProfile( getApplicationContext() );
        PreferenceManager preferenceManager1 = PreferenceManager.getInstance();

        fcmToken = preferenceManager.getStringPreference( this, Constant.SP_FCM_TOKEN );
        if (fcmToken != null) {
            try {
                Intent intent = new Intent( this, RefreshFcmTokenService.class );
                intent.putExtra( Constant.KEY_FCM_TOKEN, fcmToken );
                startService( intent );
            } catch (IllegalStateException e) {

            } catch (Exception e) {

            }
        }


        initialize();
//        navigationWork();

        getSupportFragmentManager().addOnBackStackChangedListener( this );
//        makeHomeVisible();
        //addFragment(new OffersFragment(), "OffersFragment", true, false, null, currentFrameId);

        Intent intent = getIntent();

        if (intent != null && Intent.ACTION_SEND.equals( intent.getAction() ) && intent.getType().startsWith( "image/" )) {
            Bundle bundle = new Bundle();
            Uri uri = intent.getParcelableExtra( Intent.EXTRA_STREAM );
            bundle.putString( Constant.KEY_TYPE, String.valueOf( uri ) );
            makeDrawerVisible();
            addFragment( new ChatConverationFragment(), "ChatConverationFragment", true, false, bundle, currentFrameId );
        } else if (intent != null) {
            if (intent.hasExtra( "isNotification" )) {
                if (intent.getBooleanExtra( "isNotification", false )) {

                    if (intent.getStringExtra( "activity" ).equals( "lead" )) {
                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        addFragment( new LeadsPagerFragment(), "LeadsPagerFragment", true, false, bundle, currentFrameId );
                    } else if (intent.getStringExtra( "activity" ).equals( "booking" )) {
                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        bundle.putString( Constant.KEY_ID, intent.getStringExtra( "source" ) );
                        addFragment( new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle, currentFrameId );
                        /*if (!PreferenceManager.getInstance().getStringPreference(getApplicationContext(), Constant.SP_CRE).equalsIgnoreCase("CRE")) {
                            Bundle bundle = new Bundle();
                            addFragment(new BookingsPagerFragment(), "BookingsPagerFragment", true, false, bundle, currentFrameId);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constant.KEY_ID, 2);
                            addFragment(new LeadsPagerFragment(), "LeadsPagerFragment", true, false, bundle, currentFrameId);
                        }*/

                    } else if (intent.getStringExtra( "activity" ).equals( "jobcard" )) {

                        makeDrawerVisible();
                        Bundle bundle = new Bundle();
                        bundle.putString( Constant.KEY_ID, intent.getStringExtra( "source" ) );
                        addFragment( new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle, currentFrameId );
                    }

                }
            }
        }


        checkForAppUpdate();
        checkCameraPermission();

    }

    private void initItems() {
        items = getResources().getStringArray( R.array.enginee );
    }

    private void addDrawerItems() {
        mExpandableListAdapter = new CustomExpandableListAdapter( this, mExpandableListTitle, mExpandableListData );
        mExpandableListView.setAdapter( mExpandableListAdapter );
        mExpandableListView.setOnGroupExpandListener( new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle( mExpandableListTitle.get( groupPosition ).toString() );
            }
        } );

        mExpandableListView.setOnGroupCollapseListener( new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                // getSupportActionBar().setTitle(R.string.film_genres);
            }
        } );


        //------------------  Navigation Child Click listener  ---------------------------------------//

        mExpandableListView.setOnChildClickListener( (parent, v, groupPosition, childPosition, id)->{

            //---------------- Accounts--------------------------

            if (groupPosition == 0 && childPosition == 0) {

            }
            if (groupPosition == 0 && childPosition == 1) {

            }
            if (groupPosition == 0 && childPosition == 2) {

            }

            //----------------CRM------------------------------------

            if (groupPosition == 1 && childPosition == 0) {
                mDrawerLayout.closeDrawers();
                makeDrawerVisible();
                Bundle bundle = new Bundle();
                String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
                bundle.putString( Constant.KEY_ID, businessData );
                addFragment( new LeadsFragment(), "LeadsFragment", true, false, bundle, currentFrameId );
            }

            if (groupPosition == 1 && childPosition == 1) {

            }
            if (groupPosition == 1 && childPosition == 2) {

            }
            if (groupPosition == 1 && childPosition == 3) {

            }


            //--------------- MANAGEMENT-----------------------------

            if (groupPosition == 2 && childPosition == 0) {

                mDrawerLayout.closeDrawers();
                makeDrawerVisible();
                Bundle bundle = new Bundle();
                String businessData = PreferenceManager.getInstance().getStringPreference( getApplicationContext(), Constant.SP_BUSINESS );
                bundle.putString( Constant.KEY_ID, businessData );
                addFragment( new BusinessOverviewFragment(), "BusinessOverviewFragment", true, false, bundle, currentFrameId );

            }

            // Customer OverView
            if (groupPosition == 2 && childPosition == 1) {
                startActivity( new Intent( getApplicationContext(), LeadFunnelActivity.class ) );
            }

            // Team performance
            if (groupPosition == 2 && childPosition == 2) {
                addFragment(new AnalyticsFragment(), "AnalyticsFragment", true, false, null, currentFrameId);
            }

            //---------------------Master-------------------------------

            if (groupPosition == 3 && childPosition == 0) {

            }

            if (groupPosition == 3 && childPosition == 1) {

            }

            if (groupPosition == 3 && childPosition == 2) {

            }

            //---------------------- Purchase --------------------------

            if (groupPosition == 4 && childPosition == 0) {

            }
            if (groupPosition == 4 && childPosition == 1) {

            }


            //-------------------------Sales----------------------------

            if (groupPosition == 5 && childPosition == 0) {

            }
            if (groupPosition == 5 && childPosition == 1) {

            }

            //--------------------------- WMS ---------------------------

            if (groupPosition == 6 && childPosition == 0) {
                addFragment(new BookingsPagerFragment(), "BookingsPagerFragment", true, false, null, currentFrameId);
            }
            if (groupPosition == 6 && childPosition == 1) {
                addFragment(new JobsPagerFragment(), "JobsPagerFragment", true, false, null, currentFrameId);
            }


            mDrawerLayout.closeDrawer( GravityCompat.START );
            return false;
        } );
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle( this, mDrawerLayout, comman_tool, R.string.drawer_open, R.string.drawer_close ) {


            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened( drawerView );
//                getSupportActionBar().setTitle(R.string.film_genres);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed( view );
                getSupportActionBar().setTitle( mActivityTitle );
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled( true );
        mDrawerToggle.getDrawerArrowDrawable().setColor( getResources().getColor( R.color.white ) );
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener( mDrawerToggle );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate( savedInstanceState );
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged( newConfig );
        mDrawerToggle.onConfigurationChanged( newConfig );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected( item )) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    //--------------------------------- Home Screen Added Code ---------------------------------------//
    private void checkCameraPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> denied = new ArrayList<>();

            for (String perm : cameraPermissions) {
                if (ContextCompat.checkSelfPermission( this, perm ) != PackageManager.PERMISSION_GRANTED)
                    denied.add( perm );
            }

            if (denied.size() != 0) {
                requestPermissions( denied.toArray( new String[denied.size()] ), REQUEST_CAMERA_IMAGE );
                return;
            }

        }
    }

    private void checkForAppUpdate() {

        // Creates instance of the manager.
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create( this );

        // Checks that the platform will allow the specified type of update.
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener( appUpdateInfo->{

            //  Toast.makeText(this, appUpdateInfo.updateAvailability()+"", Toast.LENGTH_SHORT).show();

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {

                try {
                    appUpdateManager.startUpdateFlowForResult( appUpdateInfo, AppUpdateType.FLEXIBLE, this, APP_UPDATE_REQUEST );
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

                appUpdateManager.registerListener( installState->{
                    if (installState.installStatus() == InstallStatus.DOWNLOADED)
                        popupSnackbarForCompleteUpdate( appUpdateManager );
                } );
            }
        } );
    }

    private void otpReceiver() {

        SmsRetrieverClient mClient = null;
        Task<Void> task = null;

        mClient = SmsRetriever.getClient( this );
        task = mClient.startSmsRetriever();

        task.addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e( "HomeScreen", "Success" );
            }
        } );

        task.addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e( "HomeScreen", "Failure" );
            }
        } );

    }

    private void popupSnackbarForCompleteUpdate(AppUpdateManager appUpdateManager) {
        Snackbar snackbar = Snackbar.make( mHomeLayout, "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE );
        snackbar.setAction( "RESTART", view->appUpdateManager.completeUpdate() );
        snackbar.setActionTextColor( getResources().getColor( R.color.red_color ) );
        snackbar.show();
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_UPDATE_COVER) {
            String url = intent.getStringExtra( Constant.KEY_IMAGES );
            // Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.placeholder_thumbnail).into(mImage);
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_COVER_IMAGE) {
            String url = intent.getStringExtra( Constant.KEY_IMAGES );
            //Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.placeholder_thumbnail).into(mImage);
        } else if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_NOTIFICATION) {
            Utility.showResponseMessage( mHomeLayout, intent.getStringExtra( Constant.KEY_TYPE ) );

        }
        if (intent.getIntExtra( Constant.KEY_EVENT_ID, -1 ) == Constant.EVENT_NOTIFICATION_COUNT) {

        }
    }


    private void initialize() {
        this.mRealm = RealmController.with( this ).getRealm();
        mRealmController = RealmController.getInstance();
        popup = new PopupMenu( this, mOptionView );
        popup.inflate( R.menu.homescreen_menu );
        popup.setOnMenuItemClickListener( item->{
            switch (item.getItemId()) {
                case R.id.log_out:
                    dialogLogout();
                    break;
            }
            return false;
        } );
        mHomeLayout = (FrameLayout) findViewById( R.id.layout_home );
        frameLayout = (FrameLayout) findViewById( R.id.layout_drawer );
        mPresenter = new HomeScreenPresenter( this, mHomeLayout );
        mPresenter.getCarItems();
        if (checkGooglePlayServices()) {
            buildGoogleApiClient();
        }

    }


    public void setHeaderSpinner() {
        RealmResults<UserRealm> userRealmRealmResults = mRealmController.getAllNotLoggedInUsers();
        final String strUsers[] = new String[userRealmRealmResults.size() + 2];
        strUsers[0] = "Switch Account";
        for (int i = 1; i <= userRealmRealmResults.size(); i++) {
            strUsers[i] = userRealmRealmResults.get( i - 1 ).getContactNo();
        }
        strUsers[userRealmRealmResults.size() + 1] = "Add Account";
        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>( this, R.layout.layout_spinner_text_white, strUsers );
        adapterBrand.setDropDownViewResource( R.layout.layout_spinner_text );
        mSpnUser.setAdapter( adapterBrand );
        mSpnUser.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
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
                    Intent homescreen = new Intent( getApplicationContext(), LoginActivity.class );
                    homescreen.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity( homescreen );
                    finish();
                } else {
                    UserRealm userRealm = mRealmController.getUserId( parentView.getSelectedItem().toString() );
                    refreshToken( userRealm.getId() );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        } );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_btn:
                makeDrawerVisible();
                addFragment( new MainSearchFragment(), "MainSearchFragment", true, false, null, currentFrameId );
                break;
            case R.id.notification_btn:
                makeDrawerVisible();
                addFragment( new NotificationListFragment(), "NotificationListFragment", true, false, null, currentFrameId );
                break;
            case R.id.textViewOptions:
                popup.show();
                break;

        }
    }

    private void updateFCM() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener( new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w( TAG, "getInstanceId failed", task.getException() );
                    return;
                }
                String token = task.getResult().getToken();
                mPresenter.sendRegistrationToServer( token );
            }
        } );
    }

    private void makeHomeVisible() {
        currentFrameId = R.id.layout_home;
        currentTab = tab[0];
        clearStackLocal();
    }

    public void makeDrawerVisible() {
        currentFrameId = R.id.layout_drawer;
        currentTab = tab[1];
        frameLayout = findViewById( R.id.layout_drawer );
        mDrawerLayout.setVisibility( View.VISIBLE );

    }


    @Override
    public void onBackStackChanged() {

        if (currentTab == tab[0]) {
            Fragment fragment = getSupportFragmentManager().findFragmentById( R.id.layout_home );
            if (fragment instanceof HomeFragment) {
//                tvTitle.setText("Business Overview");
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById( R.id.layout_drawer );

            if (fragment instanceof BookingCategoryFragment) {
                Intent data = new Intent();
                data.putExtra( Constant.KEY_EVENT_ID, Constant.EVENT_BOOKING_CATEGORY_VISIBLE );
                Events.SendEvent sendEvent =
                        new Events.SendEvent( data );
                GlobalBus.getBus().post( sendEvent );
                //Toast.makeText(this, "BookingCategoryFragment", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        Utility.hideSoftKeyboard( this );
        FragmentManager mManager = getSupportFragmentManager();

        if (mManager.getBackStackEntryCount() > 1) {
            mManager.popBackStackImmediate();
        } else {
            super.onBackPressed();

//            finish();
        }

        if (mManager.getBackStackEntryCount() == 1) {
            if (!isTechnician) {
                currentTab = tab[0];
                makeHomeVisible();
            } else {
                currentTab = tab[1];
                makeDrawerVisible();
            }
        }
    }

    public void addFragment(Fragment fragment, String tag, boolean addtobackstack, boolean isTransaction, Bundle bundle) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments( bundle );
        transaction.add( R.id.layout_home, fragment, tag );
        if (addtobackstack)
            transaction.addToBackStack( tag );
        else
            transaction.disallowAddToBackStack();
        transaction.commit();
    }

    public void addFragment(Fragment fragment, String tag, boolean addtobackstack, boolean isTransaction, Bundle bundle, int frameLayout) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments( bundle );
        transaction.add( frameLayout, fragment, tag );
        if (addtobackstack)
            transaction.addToBackStack( tag );
        else
            transaction.disallowAddToBackStack();
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister( this );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        GlobalBus.getBus().register( this );
    }

    private boolean checkGooglePlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable( this );
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {

            GooglePlayServicesUtil.getErrorDialog( checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES ).show();

            return false;
        }

        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
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
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    String permission = permissions[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        boolean showRationale = shouldShowRequestPermissionRationale( permission );
                        if (!showRationale) {
                            Toast.makeText( getApplicationContext(), permission, Toast.LENGTH_SHORT ).show();
                        }
                    }
                }
                return;
            }
        }
    }

    public void onItemSuccess(CarItemsResponse carItemsResponse) {

        if (carItemsResponse != null) {

            Gson gson = new Gson();

            //System.out.println(gson.toJson(carItemsResponse));

            PreferenceManager preferenceManager = PreferenceManager.getInstance();
            preferenceManager.putStringPreference( getApplicationContext(), Constant.SP_CAR_ITEMS, gson.toJson( carItemsResponse ) );
        }
    }

    public void onLogoutSuccess(BaseResponse baseResponse) {

        Bundle params = new Bundle();
        mFirebaseAnalytics.logEvent( "logout", params );

        mRealmController.deleteLoggedInUsers();
        callLogOut( baseResponse.getResponseMessage() );
    }

    public void callLogOut(String logout) {
//        Qiscus.clearUser();
        Toast.makeText( getApplicationContext(), logout, Toast.LENGTH_LONG ).show();
        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.putStringPreference( getApplicationContext(), Constant.SP_TOKEN, "" );
        Intent intent1 = new Intent( this, LoginActivity.class );
        intent1.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity( intent1 );
        finish();
    }

    public void onSuccess(BusinessPlanResponse showroomProfileResponse) {
        Gson gson = new Gson();
        String jsonString = gson.toJson( showroomProfileResponse.getGetData().getManifest() );
        PreferenceManager.getInstance().putStringPreference( getApplicationContext(), Constant.SP_MANIFEST, jsonString );
        //Log.d("Json",jsonString);

        if (PreferenceManager.getInstance().getStringPreference( this, Constant.SP_ROLE ).equalsIgnoreCase( "Technician" )) {
            makeDrawerVisible();
            isTechnician = true;
            addFragment( new JobsPagerFragment(), "JobsPagerFragment", true, false, null, currentFrameId );
//            mChat.setVisibility(View.GONE);
            mSearch.setVisibility( View.GONE );

        } else {
            isTechnician = false;
            addFragment( new BusinessOverviewFragment(), "BusinessOverviewFragment", true, false, null, currentFrameId );
            setNavigation( showroomProfileResponse.getGetData().getNavigation() );
        }
    }


    public void goawsHome(View view) {
        startActivity( new Intent( getApplicationContext(), LeadFunnelActivity.class ) );
    }

    @SuppressLint("RestrictedApi")
    public class AddressResultReceiver extends ResultReceiver {

        @SuppressLint("RestrictedApi")
        public AddressResultReceiver(Handler handler) {
            super( handler );
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                String address = resultData.getString( FetchAddressIntentService.RESULT_ADDRESS );
                Location location = resultData.getParcelable( FetchAddressIntentService.RESULT_LOCATION );
                setAddress( address, location );
                Log.e( "address receive", address );
            } else if (resultCode == FetchAddressIntentService.FAILURE_RESULT) {

            }

        }
    }

    public interface AddressCallback {
        void getAddress(String address, Location location);
    }

    public void setAddress(String address, Location location) {
        mCallback.getAddress( address, location );
    }

    private void refreshToken(String id) {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setId( id );
        mPresenter.validateUser( refreshTokenRequest );
    }


    public void onLoginSuccess(LoginResponse loginResponse) {
        Utility.setAuthToken( loginResponse.getGetLoginData().getToken() );

        UserRealm userRealm = mRealmController.checkUser( loginResponse.getGetLoginData().getUser().getId() );

        if (userRealm == null) {
            UserRealm mUserRealm = new UserRealm();
            mUserRealm.setId( loginResponse.getGetLoginData().getUser().getId() );
            mUserRealm.setAvatar( loginResponse.getGetLoginData().getUser().getAvatar() );
            mUserRealm.setAvatar_url( loginResponse.getGetLoginData().getUser().getAvatar() );
            mUserRealm.setContactNo( loginResponse.getGetLoginData().getUser().getContact_no() );
            mUserRealm.setCover( loginResponse.getGetLoginData().getUser().getCover() );
            mUserRealm.setEmail( loginResponse.getGetLoginData().getUser().getEmail() );
            mUserRealm.setIsVerify( loginResponse.getGetLoginData().getUser().getVerified_account() );
            mUserRealm.setLoggedIn( Boolean.TRUE );
            mUserRealm.setName( loginResponse.getGetLoginData().getUser().getName() );
            mUserRealm.setUsername( loginResponse.getGetLoginData().getUser().getUsername() );

            mRealm.beginTransaction();
            mRealm.copyToRealm( mUserRealm );
            mRealm.commitTransaction();

        } else {

            UserRealm mUserRealm = new UserRealm();
            mUserRealm.setAvatar( loginResponse.getGetLoginData().getUser().getAvatar() );
            mUserRealm.setAvatar_url( loginResponse.getGetLoginData().getUser().getAvatar() );
            mUserRealm.setContactNo( loginResponse.getGetLoginData().getUser().getContact_no() );
            mUserRealm.setCover( loginResponse.getGetLoginData().getUser().getCover() );
            mUserRealm.setEmail( loginResponse.getGetLoginData().getUser().getEmail() );
            mUserRealm.setIsVerify( loginResponse.getGetLoginData().getUser().getVerified_account() );
            mUserRealm.setLoggedIn( Boolean.TRUE );
            mUserRealm.setName( loginResponse.getGetLoginData().getUser().getName() );
            mUserRealm.setUsername( loginResponse.getGetLoginData().getUser().getUsername() );


            mRealmController.updateRecordUser( loginResponse.getGetLoginData().getUser().getId(), mUserRealm );

            mRealmController.updateRecordUserLoggidIn( loginResponse.getGetLoginData().getUser().getId() );
        }

        //     setQiscusData(loginUser);


        mRealmController.updateRecordUserNotLoggidIn( loginResponse.getGetLoginData().getUser().getId() );

        PreferenceManager preferenceManager = PreferenceManager.getInstance();
        preferenceManager.putStringPreference( getApplicationContext(), Constant.SP_USERID, loginResponse.getGetLoginData().getUser().getId() );

        RealmResults<UserRealm> userRealmRealmResults = mRealmController.getAllNotLoggedInUsers();
        String strUsers[] = new String[userRealmRealmResults.size() + 2];
        strUsers[0] = "Switch Account";
        for (int i = 1; i <= userRealmRealmResults.size(); i++) {
            strUsers[i] = userRealmRealmResults.get( i - 1 ).getContactNo();
        }

        strUsers[userRealmRealmResults.size() + 1] = "Add Account";

        UserRealm loginUser = mRealmController.getAllLoggedInUsers();
        if (loginUser != null) {
            Picasso.with(getApplicationContext()).load(loginUser.getAvatar()).placeholder(R.drawable.placeholder_big).into(iv_headerPic);
            tv_headerName.setText( loginUser.getName() );
//            mMobile.setText(loginUser.getContactNo());
//            setQiscusData(loginUser);
        }

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>( this, R.layout.layout_spinner_text_white, strUsers );
        adapterBrand.setDropDownViewResource( R.layout.layout_spinner_text );
        mSpnUser.setAdapter( adapterBrand );

        makeHomeVisible();

        Intent intent = new Intent();
        intent.putExtra( Constant.KEY_EVENT_ID, Constant.EVENT_CHANGE_ACCOUNT );
        Events.SendEvent sendEvent = new Events.SendEvent( intent );
        GlobalBus.getBus().post( sendEvent );

    }

    public void getCurrentLocation(AddressCallback mCallback) {
        this.mCallback = mCallback;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1 );

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

            } else {
                onLocationAvailable();
            }


        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    private void onLocationAvailable() {
        try {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient );
            if (mLastLocation != null) {
                FetchAddressIntentService.startService( getApplicationContext(), mLastLocation, new AwsHomeActivity.AddressResultReceiver( new Handler() ) );
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {
            //  buildAlertMessageNoGps();

            enableLoc();
            return true;
        }
        return false;
    }

    private void enableLoc() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder( this )
                    .addApi( LocationServices.API )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            // Timber.v("Location error " + connectionResult.getErrorCode());
                        }
                    } ).build();
            mGoogleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        locationRequest.setInterval( 30 * 1000 );
        locationRequest.setFastestInterval( 5 * 1000 );
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest( locationRequest );

        builder.setAlwaysShow( true );

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings( mGoogleApiClient, builder.build() );
        result.setResultCallback( new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show th  e dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    AwsHomeActivity.this, REQUEST_LOCATION );
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        } );

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

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

    public void clearStackLocal() {
        try {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = (count - 1); i >= 1; i--) {
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt( i );
                String tag = entry.getName();
                FragmentManager mManager = getSupportFragmentManager();
                Fragment fragment = mManager.findFragmentByTag( tag );
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.remove( fragment );
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
                FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt( i );
                String tag = entry.getName();
                FragmentManager mManager = getSupportFragmentManager();
                Fragment fragment = mManager.findFragmentByTag( tag );
                FragmentTransaction transaction = mManager.beginTransaction();
                transaction.remove( fragment );
                transaction.commit();
                mManager.popBackStack();

            }

            makeDrawerVisible();
            addFragment( new LeadsPagerFragment(), "LeadsPagerFragment", true, false, null, currentFrameId );


        } catch (IllegalStateException e) {

        } catch (Exception e) {

        }
    }


    public void disableFabButton(FloatingActionButton button) {
        button.setEnabled( false );
        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                runOnUiThread( ()->{
                    button.setEnabled( true );
                } );
            }
        }, 2000 );
    }

    public void disableButton(Button button) {
        button.setEnabled( false );
        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                runOnUiThread( ()->{
                    button.setEnabled( true );
                } );
            }
        }, 2000 );
    }


    public void disableTextview(TextView textView) {
        textView.setEnabled( false );
        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                runOnUiThread( ()->{
                    textView.setEnabled( true );
                } );
            }
        }, 2000 );
    }

    private void dialogLogout() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder( this );
        builder1.setMessage( "Are you sure, you want to logout?" );
        builder1.setCancelable( true );

        builder1.setPositiveButton( "Yes", (dialog, id)->{
            mPresenter.logout();
            dialog.cancel();
        } );

        builder1.setNegativeButton( "No", (dialog, id)->dialog.cancel() );

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void setNavigation(ArrayList<NavigationGroupBE> navigation) {
        for (int i = 0; i < navigation.size(); i++) {
            for (int j = 0; j < navigation.get( i ).getGroup().size(); j++) {
            }
        }
    }

}
