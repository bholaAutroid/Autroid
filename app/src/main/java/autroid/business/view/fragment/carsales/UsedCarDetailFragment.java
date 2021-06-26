package autroid.business.view.fragment.carsales;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.qiscus.sdk.Qiscus;
import com.squareup.picasso.Picasso;


import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.cars.CarSalePackageInclusionAdapter;
import autroid.business.adapter.jobcard.MultipleUserAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.MultipleUserSelectCallback;
import autroid.business.model.request.AddOwnerRequest;
import autroid.business.model.request.CarLeadRequest;
import autroid.business.model.request.CarSoldRequest;
import autroid.business.model.response.AddOwnerResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarLeadResponse;
import autroid.business.model.response.CarSoldResponse;
import autroid.business.model.response.CarStockResponse;
import autroid.business.model.response.GetUserResponse;
import autroid.business.model.response.UserResponseData;
import autroid.business.presenter.CarDetailPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.GalleryActivity;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.carpurchase.UserProfileFragment;
import autroid.business.view.fragment.payment.PaytmPaymentFragment;
import autroid.business.view.fragment.profile.ShowroomFragment;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsedCarDetailFragment extends Fragment implements View.OnClickListener, MultipleUserSelectCallback {

    TextView mLocation, mYear, mFuelType, mKM, mTransmission, mColour, mOwnership, mBodyStyle, mCollision, mInsurance, mPrice, carID, mBusiness, mModel, mVariant, mPackageDescription, mRegistration, mMileage, mStatus, mImagesCount, mViewOption,mCarDescription;
    RelativeLayout mMainLayout;
    CarDetailPresenter mPresenter;
    String carId,modelName,userId;
    ImageView mCarImage,mBackNavigation;
    PopupMenu popupMenu;
    CardView packageDescription,carDescription;

    @BindView(R.id.package_detail_list)
    RecyclerView mList;

    float price, refurbishmentCost, purchasedCost;

    private Realm realm;
    RealmController realmController;
    private CarStockResponse carItemsResponse;
    private Dialog dialogSold;

    LinearLayout details;
    Button proceed_btn, mSold, mBookService,mEditCar;
    EditText mobile_no, user_name, user_email;
    private Dialog dialogUsers;
    private EditText soldPrice;
    private Dialog dialogOTP;
    private Dialog dialogMembership;
    private Dialog dialogMessage;

    LinearLayout mLLEditable;
    LinearLayout mLLSale;
    private Boolean isView;
    Button btnSeller,btnChat,btnCall;


    public UsedCarDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_old_car_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        GlobalBus.getBus().register(this);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());


        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);
        mList.setNestedScrollingEnabled(false);

        mBackNavigation=view.findViewById(R.id.back_navigation);
        mYear = view.findViewById(R.id.car_year);
        mFuelType = view.findViewById(R.id.fuel_type);
        mKM = view.findViewById(R.id.car_km);
        mTransmission = view.findViewById(R.id.car_transmission);
        mColour = view.findViewById(R.id.car_color);
        mOwnership = view.findViewById(R.id.car_ownership);

        mSold = view.findViewById(R.id.sold);
        mEditCar = view.findViewById(R.id.edit_car);
        mBookService = view.findViewById(R.id.book_service);
        mBodyStyle = view.findViewById(R.id.body_style);
        mCollision = view.findViewById(R.id.car_collision);
        mInsurance = view.findViewById(R.id.car_insurance);
        mPrice = view.findViewById(R.id.car_price);
        mModel = view.findViewById(R.id.car_model);
        mVariant = view.findViewById(R.id.car_variant);
        mMileage = view.findViewById(R.id.car_mileage);
        mFuelType = view.findViewById(R.id.fuel_type);
        mKM = view.findViewById(R.id.car_km);
        mTransmission = view.findViewById(R.id.car_transmission);
        mColour = view.findViewById(R.id.car_color);
        mOwnership = view.findViewById(R.id.car_ownership);
        mBodyStyle = view.findViewById(R.id.body_style);
        mCollision = view.findViewById(R.id.car_collision);
        mInsurance = view.findViewById(R.id.car_insurance);
        mPrice = view.findViewById(R.id.price);
        carID = view.findViewById(R.id.car_id);
        mBusiness = view.findViewById(R.id.business);
        mModel = view.findViewById(R.id.car_model);
        mVariant = view.findViewById(R.id.car_variant);
        mRegistration = view.findViewById(R.id.car_registration);
        mMileage = view.findViewById(R.id.car_mileage);
        mLocation = view.findViewById(R.id.location);
        mPackageDescription=view.findViewById(R.id.package_text);
        mCarDescription=view.findViewById(R.id.description);
        mStatus = view.findViewById(R.id.car_status);
        mCarImage = view.findViewById(R.id.car_image);
        packageDescription = view.findViewById(R.id.package_description);
        carDescription = view.findViewById(R.id.car_description);
        mCarImage.setOnClickListener(this);
        mSold.setOnClickListener(this);
        mEditCar.setOnClickListener(this);
        mBookService.setOnClickListener(this);

        btnSeller=view.findViewById(R.id.btn_seller);
        btnChat=view.findViewById(R.id.chat);
        btnCall=view.findViewById(R.id.call);

        btnSeller.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        mImagesCount = view.findViewById(R.id.images_count);
        mViewOption = view.findViewById(R.id.textViewOptions);
        mViewOption.setOnClickListener(this);
        mMainLayout = view.findViewById(R.id.main_layout);
        mPresenter = new CarDetailPresenter(this, mMainLayout);

        isView=getArguments().getBoolean(Constant.KEY_IS_VIEW);

        mLLEditable=view.findViewById(R.id.layout_editable);
        mLLSale=view.findViewById(R.id.layout_sale);

        if(isView){
            mLLSale.setVisibility(View.VISIBLE);
            mLLEditable.setVisibility(View.GONE);
            mViewOption.setVisibility(View.GONE);
        }
        else {
            mLLEditable.setVisibility(View.VISIBLE);
            mLLSale.setVisibility(View.GONE);
            mViewOption.setVisibility(View.VISIBLE);
        }


        Bundle bundle = getArguments();
        if (bundle != null) {
            carId = bundle.getString(Constant.KEY_ID);
            // carRealm=realmController.getCar(carId);
            //onDetailSet(carRealm);
            mPresenter.getCarDetails(carId);
        }


        popupMenu = new PopupMenu(getActivity(), mViewOption);

        popupMenu.inflate(R.menu.more_details_menu);

        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.more_details:
                    Bundle dataBundle = new Bundle();
                    dataBundle.putFloat(Constant.SELLING_PRICE, price);
                    dataBundle.putFloat(Constant.REFURBISHMENT_PRICE, refurbishmentCost);
                    dataBundle.putFloat(Constant.PURCHASE_PRICE, purchasedCost);
                    dataBundle.putString(Constant.VALUE,mRegistration.getText().toString().trim());
                    ((HomeScreen) getActivity()).addFragment(new AdditionalCarDetailFragment(), "AdditionalCardDetailFragment", true, false, dataBundle, ((HomeScreen) getActivity()).currentFrameId);
                    break;
            }

            return false;
        });

        mBackNavigation.setOnClickListener(v->getActivity().onBackPressed());

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_image:
                if (null != carItemsResponse)
                    if (carItemsResponse.getGetCarDetails().getThumbnails().size() > 0) {
                        Intent intent = new Intent(getActivity(), GalleryActivity.class);
                        intent.putExtra(Constant.KEY_IMAGES, carItemsResponse.getGetCarDetails().getThumbnails());
                        startActivity(intent);
                    }
                break;

            case R.id.book_service:
                ((HomeScreen)getActivity()).disableButton(mBookService);
                Bundle bundle=new Bundle();
                bundle.putString(Constant.KEY_CAR_ID, carId);
                bundle.putString(Constant.KEY_MODEL_NAME, modelName);
                ((HomeScreen) getActivity()).addFragment(new BookingCategoryFragment(), "BookingCategoryFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                break;

            case R.id.edit_car:
                Bundle dataBundle=new Bundle();
                dataBundle.putString(Constant.KEY_CAR_ID, carId);
                dataBundle.putString(Constant.KEY_TYPE, Constant.EDIT_CAR);
                dataBundle.putBoolean(Constant.IS_GARAGE_CAR,false);
                ((HomeScreen) getActivity()).addFragment(new EditCarFragment(), "BookingCategoryFragment", true, false, dataBundle, ((HomeScreen) getActivity()).currentFrameId);
                break;

            case R.id.textViewOptions:
                popupMenu.show();
                break;

            case R.id.sold:
                showDialogSold();
                break;
            case R.id.btn_seller: {
                CarLeadRequest carLeadRequest = new CarLeadRequest();
                carLeadRequest.setCar(carId);
                mPresenter.carLead(carLeadRequest, carItemsResponse.getGetCarDetails().getUser().getId(), carItemsResponse.getGetCarDetails().getUser().getAccount_info().getType(), "seller");
                break;
            }
            case R.id.call:

                CarLeadRequest carLeadRequest=new CarLeadRequest();
                carLeadRequest.setCar(carId);
                mPresenter.carLead(carLeadRequest,carItemsResponse.getGetCarDetails().getUser().getId(),carItemsResponse.getGetCarDetails().getUser().getAccount_info().getType(),"call");

                break;
            case R.id.chat:
                final ProgressDialog progressDialog=new ProgressDialog(getActivity());
                progressDialog.show();//
                try {
//                    Qiscus.buildChatWith(carItemsResponse.getGetCarDetails().getUser().getId())//here we use email as userID. But you can make it whatever you want.
//                            .build(getActivity(), new Qiscus.ChatActivityBuilderListener() {
//                                @Override
//                                public void onSuccess(Intent intent) {
//                                    progressDialog.dismiss();
//                                    startActivity(intent);
//                                }
//
//                                @Override
//                                public void onError(Throwable throwable) {
//                                    //do anything if error occurs
//                                    Toast.makeText(getContext(), "Chat facility is not available for this seller.", Toast.LENGTH_SHORT).show();
//
//                                    progressDialog.dismiss();
//                                    throwable.printStackTrace();
//                                    throwable.getLocalizedMessage();
//                                }
//                            });
                }catch (NullPointerException e){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                }catch (Exception e){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                }
                break;
        }
    }

    public void onDetailSuccess(CarStockResponse carItemsResponse) {
        this.carItemsResponse = carItemsResponse;

        //mViewOption.setVisibility(View.VISIBLE);
        modelName=carItemsResponse.getGetCarDetails().getModelName();

        price = carItemsResponse.getGetCarDetails().getNumericPrice();
        refurbishmentCost = carItemsResponse.getGetCarDetails().getRefurbishment_cost();
        purchasedCost = carItemsResponse.getGetCarDetails().getPurchase_price();

        mPrice.setText("₹ " + carItemsResponse.getGetCarDetails().getPrice());
        carID.setText("Car ID : " + carItemsResponse.getGetCarDetails().getCarId());
        mYear.setText(carItemsResponse.getGetCarDetails().getManufacture_year());
        mKM.setText(carItemsResponse.getGetCarDetails().getOdometer());
        mBusiness.setText(carItemsResponse.getGetCarDetails().getUser().getName());
        mFuelType.setText(carItemsResponse.getGetCarDetails().getFuel_type());
        mTransmission.setText(carItemsResponse.getGetCarDetails().getTransmission());
        mColour.setText(carItemsResponse.getGetCarDetails().getVehicle_color());
        mOwnership.setText(carItemsResponse.getGetCarDetails().getOwner());
        mBodyStyle.setText(carItemsResponse.getGetCarDetails().getBody_style());
        mCollision.setText(carItemsResponse.getGetCarDetails().getAccidental());
        mInsurance.setText(carItemsResponse.getGetCarDetails().getInsurance());
        mModel.setText(carItemsResponse.getGetCarDetails().getTitle());
        mMileage.setText(carItemsResponse.getGetCarDetails().getMileage());
        mRegistration.setText(carItemsResponse.getGetCarDetails().getRegistration_no());
        mLocation.setText(carItemsResponse.getGetCarDetails().getLocation());
        mStatus.setText(carItemsResponse.getGetCarDetails().getVehicle_status());
        // mVariant.setText(carRealm.getVariant());


        //btnChat.setEnabled(carItemsResponse.getGetCarDetails().isChatEnable());

        if(isView) {
            if (carItemsResponse.getGetCarDetails().isPublish() && carItemsResponse.getGetCarDetails().isAdmin_approved()) {
               // if(carItemsResponse.getGetCarDetails().getUser().getAccount_info().getType().equalsIgnoreCase("business"))
               /*     btnChat.setEnabled(carItemsResponse.getGetCarDetails().isChatEnable());
                else
                    btnChat.setEnabled(true);*/

                // if(carItemsResponse.getGetCarDetails().getChatEnable())
            }
            else {
                mLLSale.setVisibility(View.GONE);

            }
        }

        if(carItemsResponse.getGetCarDetails().getDescription().trim().length()!=0){
            carDescription.setVisibility(View.VISIBLE);
            mCarDescription.setText(carItemsResponse.getGetCarDetails().getDescription());
        }

        if(carItemsResponse.getGetCarDetails().getPackageData()!=null){
            packageDescription.setVisibility(View.VISIBLE);
            mPackageDescription.setText(carItemsResponse.getGetCarDetails().getPackageData().getDescription());

            CarSalePackageInclusionAdapter purchasedPackagesDetailAdapter=new CarSalePackageInclusionAdapter(getActivity(),carItemsResponse.getGetCarDetails().getPackageData().getDiscount());
            mList.setAdapter(purchasedPackagesDetailAdapter);
        }

        if(carItemsResponse.getGetCarDetails().isPublish() && carItemsResponse.getGetCarDetails().isAdmin_approved())
            mSold.setVisibility(View.VISIBLE);

        if (carItemsResponse.getGetCarDetails().getThumbnails().size() > 0) {
            mImagesCount.setText("1/" + carItemsResponse.getGetCarDetails().getThumbnails().size());
            Picasso.with(getActivity()).load(carItemsResponse.getGetCarDetails().getThumbnails().get(0).getFile_address()).placeholder(R.drawable.placeholder_big).into(mCarImage);
        } else {
            mImagesCount.setText("NA");
            Picasso.with(getActivity()).load(R.drawable.placeholder_big).fit().placeholder(R.drawable.placeholder_big).into(mCarImage);
        }
    }

    public void showDialogSold() {
        dialogSold = new Dialog(getActivity());
        dialogSold.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSold.setCancelable(true);
        dialogSold.setContentView(R.layout.layout_change_car_ownership);
        dialogSold.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_opacity80)));
        Window window = dialogSold.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        proceed_btn = dialogSold.findViewById(R.id.jobcard_proceed);
        mobile_no = dialogSold.findViewById(R.id.jobcard_mob_no);
        details = dialogSold.findViewById(R.id.details);
        user_name = dialogSold.findViewById(R.id.jobcard_user_name);
        user_email = dialogSold.findViewById(R.id.jobcard_user_email);
        soldPrice = dialogSold.findViewById(R.id.sold_price);

        proceed_btn.setOnClickListener(v -> {
            ((HomeScreen) getActivity()).disableButton(proceed_btn);
            if (validateSelling(soldPrice.getText().toString())) {
                if (validateNumber(mobile_no.getText().toString().trim()) && details.getVisibility() == View.GONE) {
                    mPresenter.getUser(mobile_no.getText().toString().trim());
                } else if (details.getVisibility() == View.VISIBLE && validateName(user_name.getText().toString().trim())) {
                    if (user_email.getText().toString().trim().equals(""))
                        mPresenter.addOwner(addOwnerRequest());
                    else if (Utility.isEmailValid(mMainLayout, user_email.getText().toString().trim()))
                        mPresenter.addOwner(addOwnerRequest());
                }
            }
        });

        dialogSold.show();
    }

    private AddOwnerRequest addOwnerRequest() {
        AddOwnerRequest addOwnerRequest = new AddOwnerRequest();
        addOwnerRequest.setUserId(userId);
        addOwnerRequest.setName(user_name.getText().toString().trim());
        addOwnerRequest.setContact_no(mobile_no.getText().toString().trim());
        addOwnerRequest.setEmail(user_email.getText().toString().trim());

        return addOwnerRequest;
    }

    private boolean validateNumber(String contact_no) {
        if (contact_no.length() == 0 || contact_no.length() != 10) {
            Utility.showResponseMessage(mMainLayout, "Please enter a valid number");
            return false;
        }
        return true;
    }

    private boolean validateSelling(String price) {
        if (price.length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter sold price");
            return false;
        }
        return true;
    }

    private boolean validateName(String name) {
        if (name.length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Please enter a valid name");
            return false;
        }
        return true;
    }


    public void onSuccessGetUser(GetUserResponse userResponse) {
        if (userResponse.getResponseData().size() > 1) {
            showDialog(userResponse.getResponseData());
        } else if (userResponse.getResponseData().size() == 1) {
            setSelectedUser(userResponse.getResponseData().get(0));
        }
    }

    private void setSelectedUser(UserResponseData userResponse) {
        userId = userResponse.getId();
        CarSoldRequest carSoldRequest = new CarSoldRequest();
        carSoldRequest.setBuyer(userId);
        carSoldRequest.setCar(carId);
        carSoldRequest.setPrice(soldPrice.getText().toString());
        mPresenter.soldCar(carSoldRequest);
    }

    public void showDialog(ArrayList<UserResponseData> bookedPackagesBES) {
        dialogUsers = new Dialog(getActivity());
        dialogUsers.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUsers.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));

        dialogUsers.setCancelable(true);
        dialogUsers.setContentView(R.layout.layout_multiple_user);
        Window window = dialogUsers.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout mCross = dialogUsers.findViewById(R.id.ll_cross);
        RecyclerView mList = dialogUsers.findViewById(R.id.package_list);
        TextView mHeading = dialogUsers.findViewById(R.id.heading);
        mHeading.setText("Please select the account linked with entered number");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);

        MultipleUserAdapter selectedPackagesAdapter = new MultipleUserAdapter(getActivity(), bookedPackagesBES, this);
        mList.setAdapter(selectedPackagesAdapter);

        mCross.setOnClickListener(v-> dialogUsers.dismiss());

        dialogUsers.show();

    }

    public void notFound() {
        userId = "";
        Utility.showResponseMessage(mMainLayout, "User not found");
        details.setVisibility(View.VISIBLE);
        mobile_no.setFocusable(false);
    }

    @Override
    public void onUserClick(UserResponseData userResponseData) {
        if (dialogUsers != null && dialogUsers.isShowing()) {
            dialogUsers.dismiss();
            setSelectedUser(userResponseData);
        }
    }

    public void OnSuccessAddOwner(AddOwnerResponse ownerResponse) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, ownerResponse.getResponseData().getId());
        bundle.putString(Constant.USER_NAME, ownerResponse.getResponseData().getName());
        details.setVisibility(View.GONE);
        mobile_no.setFocusable(true);
        mobile_no.setFocusableInTouchMode(true);
        Utility.showSoftKeyboard(getActivity(), mobile_no);
        user_name.setText("");
        user_email.setText("");

        setSelectedUser(ownerResponse.getResponseData());
    }

    public void onCarSold(BaseResponse body) {

        if (dialogSold != null && dialogSold.isShowing())
            dialogSold.dismiss();

        showDialogOTP();
    }

    public void showDialogOTP() {
        dialogOTP = new Dialog(getActivity());
        dialogOTP.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOTP.setCancelable(true);
        dialogOTP.setContentView(R.layout.layout_car_sold_otp);
        Window window = dialogOTP.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button mVerify,mResend,mCancel;
        EditText mOtp;

        mVerify = dialogOTP.findViewById(R.id.verify);
        mResend = dialogOTP.findViewById(R.id.resend);
        mCancel = dialogOTP.findViewById(R.id.cancel);
        mOtp = dialogOTP.findViewById(R.id.otp);

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOtp.getText().toString().length() > 0) {
                    CarSoldRequest carSoldRequest = new CarSoldRequest();
                    carSoldRequest.setCar(carId);
                    carSoldRequest.setOtp(mOtp.getText().toString());
                    mPresenter.soldCarOTP(carSoldRequest);
                }
            }
        });

        mResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOTP.dismiss();
                CarSoldRequest carSoldRequest = new CarSoldRequest();
                carSoldRequest.setBuyer(userId);
                carSoldRequest.setCar(carId);
                carSoldRequest.setPrice(soldPrice.getText().toString());
                mPresenter.soldCar(carSoldRequest);
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialogOTP.dismiss();
            }
        });

        dialogOTP.show();
    }

    public void onCarSoldOTP(CarSoldResponse body) {

        if (dialogOTP != null && dialogOTP.isShowing())
            dialogOTP.dismiss();


        Toast.makeText(getActivity(), body.getResponseMessage(), Toast.LENGTH_LONG).show();
        if(carItemsResponse.getGetCarDetails().getPackageData()!=null) {
            showDialogMembership(body.getGetSoldData().getId());
        }
        else {
            showDialogMessage();
        }

    }

    public void showDialogMembership(String id) {
        dialogMembership = new Dialog(getActivity());
        dialogMembership.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMembership.setCancelable(false);
        dialogMembership.setContentView(R.layout.layout_buy_membership);
        Window window = dialogMembership.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button mBuyNow, mBuyLater;

        mBuyNow = dialogMembership.findViewById(R.id.buy_now);
        mBuyLater = dialogMembership.findViewById(R.id.buy_later);

        mBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeScreen) getActivity()).clearStackLocal();

                if (dialogMembership.isShowing())
                    dialogMembership.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, id);
                ((HomeScreen) getActivity()).addFragment(new PaytmPaymentFragment(), "PaytmPaymentFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

            }
        });

        mBuyLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogMembership.isShowing())
                    dialogMembership.dismiss();

                ((HomeScreen) getActivity()).clearStackLocal();
                ((HomeScreen) getActivity()).addFragment(new MyGarageFragment(), "MyGarageFragment", true, false, null, ((HomeScreen) getActivity()).currentFrameId);

            }
        });
        dialogMembership.show();
    }

    public void showDialogMessage() {
        dialogMessage = new Dialog(getActivity());
        dialogMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMessage.setCancelable(false);
        dialogMessage.setContentView(R.layout.layout_dialog_message);
        Window window = dialogMessage.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button mOK;
        TextView mTitle, mDescription;

        mOK = dialogMessage.findViewById(R.id.ok);
        mTitle = dialogMessage.findViewById(R.id.title);
        mDescription = dialogMessage.findViewById(R.id.description);

        mTitle.setText("Car Successfully Sold");
        mDescription.setText("Car Ownership has been Changed.\nBuyer can see the car in his garage into the CarEager App.");


        mOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
                ((HomeScreen) getActivity()).clearStackLocal();
                ((HomeScreen) getActivity()).addFragment(new MyGarageFragment(), "MyGarageFragment", true, false, null, ((HomeScreen) getActivity()).currentFrameId);

            }
        });
        dialogMessage.show();
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {

        Intent intent = sendEvent.getEvent();

        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_REFRESH_CAR_DETAIL) {
                mPresenter.getCarDetails(carId);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }


    public void onLeadSuccess(CarLeadResponse carItemsResponse, final String userId, final String type, String action) {

            if(action.equalsIgnoreCase("Seller")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_ID, userId);
                ((HomeScreen) getActivity()).makeDrawerVisible();
                if (type.equalsIgnoreCase("Business"))
                    ((HomeScreen) getActivity()).addFragment(new ShowroomFragment(), "ShowroomFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                else
                    ((HomeScreen) getActivity()).addFragment(new UserProfileFragment(), "UserProfileFragment", true, false, bundle, ((HomeScreen
                            ) getActivity()).currentFrameId);
            }
            else {
                try {
                    String number=this.carItemsResponse.getGetCarDetails().getUser().getContact_no();
                    if(number!=null) {
                        if(number.length()>0) {
                            String phone = number;
                            Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                            startActivity(intent1);
                        }
                    }
                }
                catch (SecurityException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

    }
}
