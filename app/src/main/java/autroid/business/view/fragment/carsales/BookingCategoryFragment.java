package autroid.business.view.fragment.carsales;


import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.cars.BookingCategoryAdapter;
import autroid.business.adapter.cars.BookingNestedServicesAdapter;
import autroid.business.adapter.cars.BookingServicesAdapter;
import autroid.business.adapter.cars.SelectedPackagesAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.interfaces.PurchasedPackageCallback;
import autroid.business.model.bean.BookedPackagesBE;
import autroid.business.model.bean.ServiceBE;
import autroid.business.model.realm.BookingCategoryRealm;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.AddBookingRequest;
import autroid.business.model.request.ServiceRequest;
import autroid.business.model.response.AddBookingResponse;
import autroid.business.model.response.BookedPackageResponse;
import autroid.business.model.response.BookingCategoryResponse;
import autroid.business.model.response.BookingServicesResponse;
import autroid.business.model.response.CarListResponse;
import autroid.business.model.response.ShowroomProfileResponse;
import autroid.business.model.response.VendorServicesResponse;
import autroid.business.presenter.bookings.BookingNewCategoryPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.booking.BookingScheduleFragment;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingCategoryFragment extends Fragment implements View.OnClickListener, BookingCategoryCallback, PurchasedPackageCallback, OnRealmImageClickCallback,View.OnTouchListener {

    @BindView(R.id.booking_category_new)
    RecyclerView mListNew;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.services_list)
    RecyclerView mServiceList;

    BookingCategoryAdapter mBookingCategoryAdapter;
    BookingNestedServicesAdapter mServiceListAdapter;
    private BookingServicesAdapter mServicesAdapter;


    BookingNewCategoryPresenter mPresenter;

    private Realm realm;
    RealmController realmController;

    String carId, businessId;

    @BindView(R.id.packages)
    TextView mPackages;

    @BindView(R.id.car_title)
    TextView mCarName;

    @BindView(R.id.business_title)
    TextView mBusinessName;

    @BindView(R.id.price)
    TextView mPrice;

    @BindView(R.id.savings)
    TextView mSavings;

    @BindView(R.id.name)
    TextView mSelectedService;

    @BindView(R.id.nested_name)
    TextView mNestedSelectedService;

    @BindView(R.id.nested_services_list)
    ExpandableListView mNestedServicesList;

    @BindView(R.id.car_package)
    TextView mCarPackage;

    @BindView(R.id.ll_package)
    LinearLayout mLLPackage;

    @BindView(R.id.default_message)
    TextView mDefMessage;

    @BindView(R.id.btn_proceed)
    LinearLayout mDone;

    @BindView(R.id.ll_bottom)
    RelativeLayout mBottom;



    @BindView(R.id.ll_complete_view)
    LinearLayout mLLCompleteView;

    @BindView(R.id.ll_services_list)
    NestedScrollView mLLServicesList;

    @BindView(R.id.ll_transparent)
    View mLLTransparent;

    @BindView(R.id.ll_nested_services_list)
    LinearLayout mLLNestedServicesList;

    private Dialog dialog;

    String packageId = "", packageName = "";

    private FirebaseAnalytics mFirebaseAnalytics;

    ServiceRequest serviceRequest;
    private Boolean nested=false;

    public BookingCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_new_category2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        //GlobalBus.getBus().register(this);
        mPresenter = new BookingNewCategoryPresenter(this, mMainLayout);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Booking Category", null);

        GlobalBus.getBus().register(this);

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(Constant.KEY_CAR_ID)) {
                carId = bundle.getString(Constant.KEY_CAR_ID);
                mCarName.setText(bundle.getString(Constant.KEY_MODEL_NAME));
                mPresenter.bookingCategory(carId);

                if (bundle.containsKey(Constant.KEY_PACKAGE_ID)) {

                    packageId = bundle.getString(Constant.KEY_PACKAGE_ID);
                    packageName = bundle.getString(Constant.KEY_PACKAGE_NAME);
                    mCarPackage.setText(packageName);
                    mLLPackage.setVisibility(View.GONE);
                    if(packageName!=null && packageName.length()>0)
                    Toast.makeText(getActivity(),"The package " +packageName+" is applied. The prices of the applicable services are updated.", Toast.LENGTH_LONG).show();

                } else {
                    AddBookingRequest addBookingRequest = new AddBookingRequest();
                    addBookingRequest.setCar(carId);
                    mPresenter.getPackages(addBookingRequest);
                }
            } else {
                mPresenter.getCarStock();
            }

            if (bundle.containsKey(Constant.KEY_VENDOR_ID)) {
                businessId = bundle.getString(Constant.KEY_VENDOR_ID);
                mBusinessName.setText(bundle.getString(Constant.Key_Name));
            } else {
                mPresenter.getVendors();
            }
        }

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController = new RealmController(appCtx);
        realmController.clearBookingCategory();
        realmController.clearBookingData();

        LinearLayoutManager llmNew = new LinearLayoutManager(getActivity());
        llmNew.setOrientation(LinearLayoutManager.VERTICAL);
        mListNew.setLayoutManager(llmNew);

        LinearLayoutManager llServicem = new LinearLayoutManager(getActivity());
        llServicem.setOrientation(LinearLayoutManager.VERTICAL);
        mServiceList.setLayoutManager(llServicem);

        mDone.setOnClickListener(this);
        //mBusinessName.setOnClickListener(this);
        //mCarName.setOnClickListener(this);
        mCarPackage.setOnClickListener(this);
        mBottom.setOnClickListener(this);
        mBottom.setOnTouchListener(this);

        /*   ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(-1, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                drawerLayout.openDrawer(Gravity.START);

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
               return;


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mListNew);*/
    }

    public void onSuccess(BookingCategoryResponse bookingCategoryResponse) {
        for (int i = 0; i < bookingCategoryResponse.getBookingCategoryBES().size(); i++) {
            BookingCategoryRealm bookingCategoryRealm = new BookingCategoryRealm();
            bookingCategoryRealm.setTitle(bookingCategoryResponse.getBookingCategoryBES().get(i).getTitle());
            bookingCategoryRealm.setTag(bookingCategoryResponse.getBookingCategoryBES().get(i).getTag());
            bookingCategoryRealm.setIcon(bookingCategoryResponse.getBookingCategoryBES().get(i).getIcon());
            bookingCategoryRealm.setNested(bookingCategoryResponse.getBookingCategoryBES().get(i).getNested());
            bookingCategoryRealm.setSelected(Boolean.FALSE);
            bookingCategoryRealm.setEnabled(true);
            realm.beginTransaction();
            realm.copyToRealm(bookingCategoryRealm);
            realm.commitTransaction();
        }

        BookingCategoryAdapter mBookingCategoryAdapterNew = new BookingCategoryAdapter(realmController.getBookingCategory(), true, getActivity(), this, false);
        mListNew.setAdapter(mBookingCategoryAdapterNew);
    }

    public void onBusinessSuccess(ShowroomProfileResponse showroomProfileResponse) {
        mBusinessName.setText(showroomProfileResponse.getResponseData().get(0).getName());
        businessId = showroomProfileResponse.getResponseData().get(0).getId();
    }

    public void onCarSuccess(CarListResponse carStockResponse) {
        if (carStockResponse.getGetCarStock().getStocks().size() > 0) {
            carId = carStockResponse.getGetCarStock().getStocks().get(0).getId();
            mCarName.setText(carStockResponse.getGetCarStock().getStocks().get(0).getModelName());
            mPresenter.bookingCategory(carId);
            AddBookingRequest addBookingRequest = new AddBookingRequest();
            addBookingRequest.setCar(carId);
            mPresenter.getPackages(addBookingRequest);
        } else {
            Toast.makeText(getActivity(), "Add car to book service.", Toast.LENGTH_SHORT).show();
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_CAR_TYPE, Constant.CAR_TYPES[1]);
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).clearStackLocal();
            ((HomeScreen) getActivity()).addFragment(new AddCarFragment(), "EditCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
    }

    public void onPackageSuccess(BookedPackageResponse baseResponse) {
        if (baseResponse.getBookedPackagesBES().size() == 0) {
            packageId = "";
            packageName = "";
            mLLPackage.setVisibility(View.GONE);
        } else {
            showDialog(baseResponse.getBookedPackagesBES());
        }
       /* else if (baseResponse.getBookedPackagesBES().size() == 1) {
            packageId = baseResponse.getBookedPackagesBES().get(0).getPackages();
            packageName = baseResponse.getBookedPackagesBES().get(0).getName();
            mCarPackage.setText(packageName);
            mLLPackage.setVisibility(View.VISIBLE);

            Toast.makeText(getActivity(),"The package " +packageName+" is applied. The prices of the applicable services are updated.", Toast.LENGTH_LONG).show();


        }*/

    }


    public void onBookingSuccess(AddBookingResponse addBookingResponse) {
        Bundle params = new Bundle();
        params.putString("booking_number", addBookingResponse.getmGetBookingData().getBooking_no());
        mFirebaseAnalytics.logEvent("add_booking", params);

        /*Intent bundle=new Intent(getActivity(),PaymentFragment.class);
        bundle.putExtra(Constant.KEY_ID,addBookingResponse.getmGetBookingData().getUser());
        bundle.putExtra(Constant.Key_Name,addBookingResponse.getmGetBookingData().getFirstname());
        bundle.putExtra(Constant.Key_Email,addBookingResponse.getmGetBookingData().getEmail());
        bundle.putExtra(Constant.Key_Mobile,addBookingResponse.getmGetBookingData().getPhone());
        bundle.putExtra(Constant.KEY_TYPE,addBookingResponse.getmGetBookingData().getProductinfo());
        bundle.putExtra(Constant.KEY_TXN_ID,addBookingResponse.getmGetBookingData().getTxnid());
        bundle.putExtra(Constant.KEY_AMOUNT,addBookingResponse.getmGetBookingData().getAmount());
        startActivity(bundle);*/


        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, addBookingResponse.getmGetBookingData().getId());
      //  ((HomeScreen) getActivity()).addFragment(new B(), "BookingCheckoutFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bottom:
               /* BookingCartFragment bookingDetailFragment = new BookingCartFragment();
                bookingDetailFragment.show(getChildFragmentManager(), "BookingCartFragment");*/

                break;
            case R.id.btn_proceed:

                try {
                    RealmResults<BookingCategoryRealm> bookingCategoryRealms = realmController.getBookingSelectedCategory();
                    if (bookingCategoryRealms.size() > 0) {
                        AddBookingRequest objAddBookingRequest = new AddBookingRequest();
                        objAddBookingRequest.setCar(carId);
                        objAddBookingRequest.setBusiness(businessId);
                        objAddBookingRequest.setPackages(packageId);
                        ArrayList<ServiceBE> serviceBES = new ArrayList<>();
                        RealmResults<SelectedBookingDataRealm> selectedBookingDataRealm = realmController.getBookingData();
                        if (selectedBookingDataRealm != null) {
                            boolean isPackage = true;
                            for (int i = 0; i < selectedBookingDataRealm.size(); i++) {
                                ServiceBE serviceBE = new ServiceBE();
                                serviceBE.setId(selectedBookingDataRealm.get(i).getId());
                                serviceBE.setType(selectedBookingDataRealm.get(i).getType());
                                serviceBE.setQuantity(selectedBookingDataRealm.get(i).getQuantity());
                                if (null != selectedBookingDataRealm.get(i).getType())
                                    if (!selectedBookingDataRealm.get(i).getType().equals("package")) {
                                        isPackage = false;
                                    }
                                serviceBES.add(serviceBE);
                            }

                            objAddBookingRequest.setServices(serviceBES);
                            objAddBookingRequest.setIs_services(Boolean.TRUE);
                                // BookingScheduleFragment dateTimeSlotActivity=new BookingScheduleFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("AddBookingRequest", objAddBookingRequest);
                                bundle.putBoolean(Constant.Is_Category, true);
                                bundle.putString(Constant.KEY_VENDOR_ID, businessId);
                                ((HomeScreen) getActivity()).addFragment(new BookingScheduleFragment(), "BookingScheduleFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

                        }
                    }
                } catch (Exception e) {

                }
                break;
            case R.id.car_title: {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_VENDOR_ID, businessId);
                bundle.putString(Constant.Key_Name, mBusinessName.getText().toString());
                bundle.putString(Constant.KEY_PACKAGE_ID, packageId);
                bundle.putString(Constant.KEY_PACKAGE_NAME, packageName);
                BookingCarFragment bookingCartFragment=new BookingCarFragment();
                bookingCartFragment.setArguments(bundle);
                bookingCartFragment.show(getChildFragmentManager(),"BookingCarFragment");
               // ((HomeScreen) getActivity()).addFragment(new BookingCarFragment(), "BookingCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                break;
            }
            case R.id.business_title:
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_CAR_ID, carId);
                bundle.putString(Constant.KEY_MODEL_NAME, mCarName.getText().toString());
                bundle.putString(Constant.KEY_PACKAGE_ID, packageId);
                bundle.putString(Constant.KEY_PACKAGE_NAME, packageName);
               /* BookingBusinessFragment bookingCartFragment=new BookingBusinessFragment();
                bookingCartFragment.setArguments(bundle);
                bookingCartFragment.show(getChildFragmentManager(),"BookingBusinessFragment");*/

               // ((HomeScreenActivity) getActivity()).addFragment(new BookingBusinessFragment(), "BookingBusinessFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
                break;
            case R.id.car_package:
                AddBookingRequest addBookingRequest = new AddBookingRequest();
                addBookingRequest.setCar(carId);
                mPresenter.getPackages(addBookingRequest);
                break;
        }
    }

    @Override
    public void onCategoryClick(String name, String tag, Boolean nested) {

       /* ResizeWidthAnimation anim = new ResizeWidthAnimation(mList, 100);
        anim.setDuration(500);
        mList.startAnimation(anim);*/

       /* float offset = .15f * getResources().getDisplayMetrics().widthPixels ;
        float width = getResources().getDisplayMetrics().widthPixels - offset;
        DrawerLayout.LayoutParams params = (android.support.v4.widget.DrawerLayout.LayoutParams) mNavigationView.getLayoutParams();
        params.width = (int) width;
        mNavigationView.setLayoutParams(params);*/


        mNestedServicesList.setVisibility(View.GONE);
        mServiceList.setVisibility(View.GONE);
        mDefMessage.setVisibility(View.GONE);


        //mSelectedService.setText(name);
        realmController.updateBookingCategorySelected(tag, true, 0.0f);

        this.nested=nested;
        //  mNavigationView.setTranslationX(100);

        if (nested) {
            mNestedSelectedService.setText(name);

            serviceRequest = new ServiceRequest();
            serviceRequest.setCar(carId);
            serviceRequest.setBusiness(businessId);
            serviceRequest.setType(tag);
            serviceRequest.setPackages(packageId);
            mPresenter.getNestedServices(serviceRequest, tag);
        } else if (!nested) {
            mSelectedService.setText(name);

            serviceRequest = new ServiceRequest();
            serviceRequest.setCar(carId);
            serviceRequest.setBusiness(businessId);
            serviceRequest.setType(tag);
            serviceRequest.setPackages(packageId);
            mPresenter.getServices(serviceRequest, tag);
            // ((HomeScreenActivity) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
        }
    }

    @Override
    public void onDisableCategoryClick(String name, String tag, Boolean nested) {
        if (nested) {
            mNestedSelectedService.setText(name);

            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setCar(carId);
            serviceRequest.setBusiness(businessId);
            serviceRequest.setType(tag);
            serviceRequest.setPackages(packageId);
            mPresenter.getNestedServices(serviceRequest, tag);
        } else if (!nested) {
            mSelectedService.setText(name);

            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setCar(carId);
            serviceRequest.setBusiness(businessId);
            serviceRequest.setType(tag);
            serviceRequest.setPackages(packageId);
            mPresenter.getServices(serviceRequest, tag);
            // ((HomeScreenActivity) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
        }
    }

    @Override
    public void onServicesSelected(String id, String name, String tag, String type, Float cost, Float mrp, int quantity,String unit,Boolean isDoorstep) {

        SelectedBookingDataRealm selectedBookingDataRealm = new SelectedBookingDataRealm();
        selectedBookingDataRealm.setTag(tag);
        selectedBookingDataRealm.setType(type);
        selectedBookingDataRealm.setId(id);
        selectedBookingDataRealm.setPackageName(name);
        selectedBookingDataRealm.setCost(cost);
        selectedBookingDataRealm.setMrp(mrp);
        selectedBookingDataRealm.setQuantity(quantity);
        selectedBookingDataRealm.setDoorstep(isDoorstep);
        selectedBookingDataRealm.setUnit(unit);

        realm.beginTransaction();
        realm.copyToRealm(selectedBookingDataRealm);
        realm.commitTransaction();

        calculatePrice();

        /*new CartAnimationUtil().attachActivity(getActivity()).setTargetView(mBusinessName).setMoveDuration(1000).setDestView(mCarName).setAnimationListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //  addItemToCart();
                Toast.makeText(getActivity(), "Continue Shopping...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).startAnimation();
        */
    }

    @Override
    public void onServicesUnselect(String id) {
        realmController.removeSelectedData(id);
        calculatePrice();
    }


    @Override
    public void updateQuantity(String id, int quantity) {
        realmController.updateQuantity(id, quantity);
        calculatePrice();
    }

    @Override
    public void onGalleryClick(String id,String type) {
        ((HomeScreen) getActivity()).makeDrawerVisible();
        ServiceGalleryFragment serviceGalleryFragment = new ServiceGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putString(Constant.KEY_TYPE, type);
        serviceGalleryFragment.setArguments(bundle);
        serviceGalleryFragment.show(getChildFragmentManager(), "ServiceGalleryFragment");
    }





    @Override
    public void onRightSwipe(){
    }

    public void showDialog(ArrayList<BookedPackagesBE> bookedPackagesBES) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_purchased_packages);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout mCross = dialog.findViewById(R.id.ll_cross);
        RecyclerView mList = dialog.findViewById(R.id.package_list);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);

        SelectedPackagesAdapter selectedPackagesAdapter = new SelectedPackagesAdapter(getActivity(), bookedPackagesBES, this);
        mList.setAdapter(selectedPackagesAdapter);
        mCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onPackageClick(String packageId, String packageName) {

        if (dialog.isShowing())
            dialog.dismiss();
        this.packageId = packageId;
        this.packageName = packageName;
        mCarPackage.setText(packageName);
        mLLPackage.setVisibility(View.GONE);

        Toast.makeText(getActivity(),"The package " +packageName+" is applied. The prices of the applicable services are updated.", Toast.LENGTH_LONG).show();

        try {
            if(null!=serviceRequest) {
                serviceRequest.setPackages(packageId);
                realmController.clearBookingData();
                mBottom.setVisibility(View.GONE);
                if (nested) {

                    mPresenter.getNestedServices(serviceRequest, serviceRequest.getType());
                } else if (!nested) {
                    mPresenter.getServices(serviceRequest, serviceRequest.getType());
                    // ((HomeScreenActivity) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
                }
            }

            //Toast.makeText(getActivity(), "Cart Dismiss", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }

    }

    public void onNestedServicesSuccess(VendorServicesResponse vendorServicesResponse, String tag) {


        if (vendorServicesResponse.getGetServices().size() > 0) {
            mNestedServicesList.setVisibility(View.VISIBLE);
            mServiceList.setVisibility(View.GONE);
            mLLNestedServicesList.setVisibility(View.VISIBLE);
            mLLServicesList.setVisibility(View.GONE);
            RealmResults<SelectedBookingDataRealm> selectedBookingDataRealms = realmController.getSelectedBookingData(tag);
            for (int j = 0; j < selectedBookingDataRealms.size(); j++) {
                for (int i = 0; i < vendorServicesResponse.getGetServices().size(); i++) {
                    for (int k = 0; k < vendorServicesResponse.getGetServices().get(i).getServices().size(); k++) {
                        if (vendorServicesResponse.getGetServices().get(i).getServices().get(k).getId().equals(selectedBookingDataRealms.get(j).getId())) {
                            vendorServicesResponse.getGetServices().get(i).getServices().get(k).setChecked(true);
                            vendorServicesResponse.getGetServices().get(i).getServices().get(k).setQuantity(selectedBookingDataRealms.get(j).getQuantity());
                        }
                    }
                }
            }

            mServiceListAdapter = new BookingNestedServicesAdapter(getActivity(), vendorServicesResponse.getGetServices(), this, this, tag);
            mNestedServicesList.setAdapter(mServiceListAdapter);


       //     setListViewHeight(mNestedServicesList,0);
            if(vendorServicesResponse.getGetServices().size()==1) {
                mNestedServicesList.expandGroup(0,true);
               // setExpandableListViewHeight(mNestedServicesList);
            }

          //

          //

            runLayoutAnimationExpandable(mNestedServicesList);

        } else {
            showAlertDialog(vendorServicesResponse.getResponseMessage());
           // showDialogStatus(vendorServicesResponse.getResponseMessage());
        }

    }

    @Override
    public void onImageClick(int pos, String id) {

    }

    @Override
    public void onDetailClick(int pos, String des) {

    }

    @Override
    public void onDetailClick(String id,String type) {
        ServiceDetailsFragment bookingDetailFragment = new ServiceDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putString(Constant.KEY_TYPE, type);
        bookingDetailFragment.setArguments(bundle);
        bookingDetailFragment.show(getChildFragmentManager(), "ServiceDetailsFragment");
    }

    @Override
    public void onTraveloguePagerClick(int pos, String id) {

    }


    private void showAlertDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void onServicesSuccess(BookingServicesResponse bookingServicesResponse, String tag) {

        if (bookingServicesResponse.getServices().size() > 0) {

            mNestedServicesList.setVisibility(View.GONE);
            mServiceList.setVisibility(View.VISIBLE);
            mLLServicesList.setVisibility(View.VISIBLE);
            mLLNestedServicesList.setVisibility(View.GONE);


            RealmResults<SelectedBookingDataRealm> selectedBookingDataRealms = realmController.getSelectedBookingData(tag);
            for (int j = 0; j < selectedBookingDataRealms.size(); j++) {
                for (int i = 0; i < bookingServicesResponse.getServices().size(); i++) {
                    if (bookingServicesResponse.getServices().get(i).getId().equals(selectedBookingDataRealms.get(j).getId())) {
                        bookingServicesResponse.getServices().get(i).setChecked(true);
                    }
                }
            }


            mServicesAdapter = new BookingServicesAdapter(getContext(), bookingServicesResponse.getServices(), this, this, tag);
            mServiceList.setAdapter(mServicesAdapter);
            runLayoutAnimation(mServiceList);
        } else {
            showAlertDialog(bookingServicesResponse.getResponseMessage());
          //  showDialogStatus(bookingServicesResponse.getResponseMessage());
        }
    }

    private void calculatePrice() {
        RealmResults<SelectedBookingDataRealm> selectedBookingDataRealm = realmController.getBookingData();

        if (selectedBookingDataRealm != null) {
            Float totalPrice = 0.0f;
            Float totalMrp = 0.0f;
            Float savedTotal = 0.0f;
            if (selectedBookingDataRealm.size() > 0) {
                mBottom.setVisibility(View.VISIBLE);
                for (int i = 0; i < selectedBookingDataRealm.size(); i++) {
                    if (selectedBookingDataRealm.get(i).getQuantity() > 0) {
                        if (selectedBookingDataRealm.get(i).getMrp() > 0)
                            totalMrp = totalMrp + (selectedBookingDataRealm.get(i).getMrp() * selectedBookingDataRealm.get(i).getQuantity());
                        else
                            totalMrp = totalMrp + (selectedBookingDataRealm.get(i).getCost() * selectedBookingDataRealm.get(i).getQuantity());


                        totalPrice = totalPrice + (selectedBookingDataRealm.get(i).getCost() * selectedBookingDataRealm.get(i).getQuantity());

                    } else {
                        if (selectedBookingDataRealm.get(i).getMrp() > 0)
                            totalMrp = totalMrp + selectedBookingDataRealm.get(i).getMrp();
                        else
                            totalMrp = totalMrp + selectedBookingDataRealm.get(i).getCost();

                        totalPrice = totalPrice + selectedBookingDataRealm.get(i).getCost();

                    }
                }

                // mPrice.setText("Total Amount\n₹ "+totalPrice);
                savedTotal = totalMrp - totalPrice;
                if(savedTotal>0){
                    mPrice.setVisibility(View.VISIBLE);
                    mSavings.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.item_animation_from_right);
                    mSavings.startAnimation(anim);

                    mSavings.setText("₹ " + savedTotal);
                }
                else {
                    mPrice.setVisibility(View.GONE);
                    mSavings.setVisibility(View.GONE);
                }

            } else {
                mBottom.setVisibility(View.GONE);
            }

        }

    }


    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, 0) == Constant.EVENT_SELECT_PACKAGE) {
            //  setBottomBar();
        }
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, 0) == Constant.EVENT_BOOKING_CATEGORY_VISIBLE) {
            try {
                getView().setFocusableInTouchMode(true);
                getView().requestFocus();
                getView().setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        //Log.i(tag, "keyCode: " + keyCode);
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                            RealmResults<BookingCategoryRealm> bookingCategoryRealms = realmController.getBookingSelectedCategory();
                            if (bookingCategoryRealms.size() > 0) {
                                showBackDialog();
                                return true;
                            } else {
                                //getActivity().onBackPressed();
                            }
                        }
                        return false;
                    }
                });
            } catch (Exception e) {

            }
        }

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, 0) == Constant.EVENT_EDIT_CART) {
            try {
              calculatePrice();
            } catch (Exception e) {

            }
        }

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, 0) == Constant.EVENT_DISMISS_CART_EDIT) {
            try {
                mLLTransparent.setVisibility(View.GONE);

                if(null!=serviceRequest) {
                    if (nested) {

                        mPresenter.getNestedServices(serviceRequest, serviceRequest.getType());
                    } else if (!nested) {
                        mPresenter.getServices(serviceRequest, serviceRequest.getType());
                        // ((HomeScreenActivity) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreenActivity) getActivity()).currentFrameId);
                    }
                }

                //Toast.makeText(getActivity(), "Cart Dismiss", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, 0) == Constant.EVENT_DISMISS_CART) {
            try {
                mLLTransparent.setVisibility(View.GONE);


                //Toast.makeText(getActivity(), "Cart Dismiss", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    private void showBackDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure, you want to discard all selection?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().onBackPressed();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void runLayoutAnimationExpandable(final ExpandableListView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

        recyclerView.setLayoutAnimation(controller);
       // recyclerView.getAdapter().notify();
        recyclerView.scheduleLayoutAnimation();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                mLLTransparent.setVisibility(View.GONE);
              /*  BookingCartFragment bookingDetailFragment = new BookingCartFragment();
                bookingDetailFragment.show(getChildFragmentManager(), "BookingCartFragment");*/

                break;
        }
        return false;
    }


    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setExpandableListViewHeight(ExpandableListView listView) {
        try {
            ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View listItem = listAdapter.getGroupView(i, false, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = totalHeight + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            if (height < 10) height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();
            /*scrollBody.post(new Runnable() {
                public void run() {
                    scrollBody.fullScroll(ScrollView.FOCUS_UP);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
