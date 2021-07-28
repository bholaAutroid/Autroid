package autroid.business.view.fragment.jobcard;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.jobcard.MultipleUserAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.MultipleUserSelectCallback;
import autroid.business.model.realm.BookingRealm;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.AddOwnerRequest;
import autroid.business.model.response.AddOwnerResponse;
import autroid.business.model.response.GetUserResponse;
import autroid.business.model.response.UserResponseData;
import autroid.business.presenter.jobcard.JobCardUserPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.booking.ManualBookingFragment;
import io.realm.Realm;
import io.realm.RealmList;

public class JobCardUserFragment extends Fragment implements MultipleUserSelectCallback {

    TextView title;

    Button proceed_btn;

    RelativeLayout main_layout;

    LinearLayout details;

    EditText mobile_no, user_name, user_email;

    JobCardUserPresenter jobCardUserPresenter;

    String userId = "";

    boolean isManual;

    RealmController mRealmController;

    Realm mRealm;

    Dialog dialog;


    public JobCardUserFragment() {}


    @Override
    public void onResume() {
        super.onResume();
        Utility.hideSoftKeyboard(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_card_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utility.hideSoftKeyboard(getActivity());
        title = view.findViewById(R.id.toolbar_title);
        title.setText("Customer Details");

        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController = RealmController.getInstance();

        isManual = getArguments().getBoolean(Constant.IS_MANUAL);

        proceed_btn = view.findViewById(R.id.jobcard_proceed);
        mobile_no = view.findViewById(R.id.jobcard_mob_no);
        main_layout = view.findViewById(R.id.main_layout);
        details = view.findViewById(R.id.details);
        user_name = view.findViewById(R.id.jobcard_user_name);
        user_email = view.findViewById(R.id.jobcard_user_email);

        jobCardUserPresenter = new JobCardUserPresenter(this, main_layout);

        proceed_btn.setOnClickListener(v -> {
            ((AwsHomeActivity) getActivity()).disableButton(proceed_btn);
            if (validateNumber(mobile_no.getText().toString().trim()) && details.getVisibility() == View.GONE) {
                jobCardUserPresenter.getUser(mobile_no.getText().toString().trim());
            } else if (details.getVisibility() == View.VISIBLE && validateName(user_name.getText().toString().trim())) {
                if (user_email.getText().toString().trim().equals(""))
                    jobCardUserPresenter.addOwner(addOwnerRequest());
                else if (Utility.isEmailValid(main_layout, user_email.getText().toString().trim()))
                    jobCardUserPresenter.addOwner(addOwnerRequest());
            }
        });
    }

    public void onSuccessGetUser(GetUserResponse userResponse) {
       if(userResponse.getResponseData().size()>1){
           showDialog(userResponse.getResponseData());
       } else if(userResponse.getResponseData().size()==1){
           setSelectedUser(userResponse.getResponseData().get(0));
       }
    }
    
    private void setSelectedUser(UserResponseData userResponse){
        userId = userResponse.getId();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.USER_ID, userResponse.getId());
        bundle.putString(Constant.USER_NAME, userResponse.getName());

        if (!isManual && userResponse.getBookings().size() > 0) {

            mRealmController.clearBookingsByStage("Jobcard");

            for (int i = 0; i < userResponse.getBookings().size(); i++) {

                BookingRealm bookingRealm = new BookingRealm();
                bookingRealm.setBookingId(userResponse.getBookings().get(i).getId());
                bookingRealm.setVehicleTitle(userResponse.getBookings().get(i).getCar().getTitle());
                bookingRealm.setConvenience(userResponse.getBookings().get(i).getConvenience());
                bookingRealm.setDated(userResponse.getBookings().get(i).getDate());
                bookingRealm.setPrice(userResponse.getBookings().get(i).getPayment().getTotal());
                bookingRealm.setPaidTotal(userResponse.getBookings().get(i).getPayment().getPaid_total());
                bookingRealm.setCoupon(userResponse.getBookings().get(i).getPayment().getCoupon());
                bookingRealm.setDiscount_type(userResponse.getBookings().get(i).getPayment().getDiscount_type());
                bookingRealm.setDiscount(userResponse.getBookings().get(i).getPayment().getDiscount());
                bookingRealm.setProviderName(userResponse.getBookings().get(i).getUser().getName());
                bookingRealm.setShortId(userResponse.getBookings().get(i).getBooking_no());
                bookingRealm.setTimeSlot(userResponse.getBookings().get(i).getTime_slot());
                bookingRealm.setRegistrationNumber(userResponse.getBookings().get(i).getCar().getRegistration_no());
                bookingRealm.setmUserName(userResponse.getBookings().get(i).getUser().getName());
                bookingRealm.setmUserNumber(userResponse.getBookings().get(i).getUser().getContact_no());
                bookingRealm.setmUserId(userResponse.getBookings().get(i).getUser().getId());
                bookingRealm.setPrimaryStatus("Jobcard");
                bookingRealm.setStatus(userResponse.getBookings().get(i).getStatus());

                if (userResponse.getBookings().get(i).getAddress() != null) {
                    String address = userResponse.getBookings().get(i).getAddress().getAddress();
                    if (userResponse.getBookings().get(i).getAddress().getArea().length() > 0)
                        address = address + ", " + userResponse.getBookings().get(i).getAddress().getArea();

                    if (userResponse.getBookings().get(i).getAddress().getLandmark().length() > 0) {
                        address = address + ", " + userResponse.getBookings().get(i).getAddress().getLandmark();
                    }

                    if (userResponse.getBookings().get(i).getAddress().getCity().length() > 0) {
                        address = address + ", " + userResponse.getBookings().get(i).getAddress().getCity();
                    }

                    if (userResponse.getBookings().get(i).getAddress().getState().length() > 0) {
                        address = address + ", " + userResponse.getBookings().get(i).getAddress().getState();
                    }

                    if (userResponse.getBookings().get(i).getAddress().getZip().length() > 0) {
                        address = address + " " + userResponse.getBookings().get(i).getAddress().getZip();
                    }

                    bookingRealm.setAddress(address);

                }

                //if(userResponse.getBookings().get(i).getCar().getThumbnails().size()>0)
                //  bookingRealm.setCarImage(userResponse.getBookings().get(i).getCar().getThumbnails().get(0).getFile_address());
                if (userResponse.getBookings().get(i).getServices().size() > 0) {
                    RealmList<SelectedBookingDataRealm> realmListMedia = new RealmList<>();

                    for (int j = 0; j < userResponse.getBookings().get(i).getServices().size(); j++) {
                        SelectedBookingDataRealm selectedBookingDataRealm = new SelectedBookingDataRealm();
                        selectedBookingDataRealm.setPackageName(userResponse.getBookings().get(i).getServices().get(j).getService());
                        selectedBookingDataRealm.setCost(userResponse.getBookings().get(i).getServices().get(j).getCost());
                        realmListMedia.add(selectedBookingDataRealm);
                    }

                    bookingRealm.setSelectedBookingDataRealms(realmListMedia);
                }

                mRealm.beginTransaction();
                mRealm.copyToRealm(bookingRealm);
                mRealm.commitTransaction();
            }

            //bundle.putBoolean(Constant.IS_JOBCARD, true);
            ((AwsHomeActivity) getActivity()).addFragment(new JobCardBookingsFragment(), "JobCardBookingsFragment", true, false,bundle /*bundle*/, ((AwsHomeActivity) getActivity()).currentFrameId);

        } else {
            if (isManual) ((AwsHomeActivity) getActivity()).addFragment(new ManualBookingFragment(), "ManualBookingFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
            else ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarFragment(), "JobCardCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
        }
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
            Utility.showResponseMessage(main_layout, "Please enter a valid number");
            return false;
        }
        return true;
    }

    private boolean validateName(String name) {
        if (name.length() == 0) {
            Utility.showResponseMessage(main_layout, "Please enter a valid name");
            return false;
        }
        return true;
    }

    public void notFound() {
        userId = "";
        Utility.showResponseMessage(main_layout, "User not found");
        details.setVisibility(View.VISIBLE);
        mobile_no.setFocusable(false);
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

        if (isManual) ((AwsHomeActivity) getActivity()).addFragment(new ManualBookingFragment(), "ManualBookingFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
        else ((AwsHomeActivity) getActivity()).addFragment(new JobCardCarFragment(), "JobCardCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public void showDialog(ArrayList<UserResponseData> bookedPackagesBES) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_purchased_packages);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout mCross = dialog.findViewById(R.id.ll_cross);
        RecyclerView mList = dialog.findViewById(R.id.package_list);
        TextView mHeading=dialog.findViewById(R.id.heading);
        mHeading.setText("Please select the account linked with entered number");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);

        MultipleUserAdapter selectedPackagesAdapter = new MultipleUserAdapter(getActivity(), bookedPackagesBES, this);
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
    public void onUserClick(UserResponseData userResponseData) {
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            setSelectedUser(userResponseData);
        }
    }
}

