package autroid.business.view.fragment.leads;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import org.json.JSONObject;

import autroid.business.R;
import autroid.business.api.NetworkTask;
import autroid.business.model.request.CarInsuranceRequest;
import autroid.business.model.response.PoliciesInsuranceTokenResponse;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class LeadInsuranceCustomerFragment extends Fragment implements NetworkTask.Result {

    private RelativeLayout mainLayout;

    private CarInsuranceRequest request;

    private EditText first_name, last_name, mobile, email;

    private Button next;

    private String url = "http://uatservices.policies365.com/cxf/authrestservices/integrate/invoke";

    private String token;

    private String messageId;

    private ImageView backNavigation;

    public LeadInsuranceCustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        request = (CarInsuranceRequest) getArguments().getSerializable(Constant.VALUE);

        first_name = view.findViewById(R.id.first_name);
        last_name = view.findViewById(R.id.last_name);
        mobile = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        next = view.findViewById(R.id.btn_next);
        mainLayout = view.findViewById(R.id.main_layout);
        backNavigation=view.findViewById(R.id.back_navigation);

        first_name.setText(request.getFirstName());
        mobile.setText(request.getMobileNumber());
        email.setText(request.getEmailId());

        backNavigation.setOnClickListener(v->getActivity().onBackPressed());

        next.setOnClickListener(v -> {

            if (validate()) {

                request.setFirstName(first_name.getText().toString().trim());
                request.setLastName(last_name.getText().toString().trim());
                request.setMobileNumber(mobile.getText().toString().trim());
                request.setEmailId(email.getText().toString().trim());

                callWS();
            }
        });
    }

    private boolean validate() {

        if (first_name.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mainLayout, "Invalid Name");
            return false;
        } else if (mobile.getText().toString().trim().length() != 10) {
            Utility.showResponseMessage(mainLayout, "Invalid Mobile");
            return false;
        } else if (email.getText().toString().trim().length() != 0 && !Utility.isEmailValid(mainLayout, email.getText().toString().trim())) {
            Utility.showResponseMessage(mainLayout, "Invalid Email");
            return false;
        }/*else if (address.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Address");
            return false;
        }else if (city.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid City");
            return false;
        }else if (state.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid State");
            return false;
        }else if (pin.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Pin");
            return false;
        }*/

        return true;
    }

    private void callWS() {

        try {

            JSONObject json = new JSONObject();
            JSONObject jsonObjectHeader = new JSONObject();
            jsonObjectHeader.put("source", "careager");
            jsonObjectHeader.put("deviceId", "agency");
            jsonObjectHeader.put("transactionName", "getToken");
            json.put("header", jsonObjectHeader);
            //json.put("id","CarEagerTest");

            JSONObject jsonObjectBody = new JSONObject();
            jsonObjectBody.put("id", "careager");
            json.put("body", jsonObjectBody);

            NetworkTask networkTask = new NetworkTask(getActivity(), 1, json.toString());
            networkTask.setDialogMessage("Loading...");
            networkTask.exposePostExecute(this);
            networkTask.execute(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void resultfromNetwork(String object, int id, int arg1, String arg2) {
        switch (id) {
            case 1:
                try {

                    Gson gson = new Gson();
                    PoliciesInsuranceTokenResponse base = gson.fromJson(object, PoliciesInsuranceTokenResponse.class);

                    if (base.getResponseCode() == 1000) {
                        token = base.getData().getToken();
                        WSCreationRequest();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {

                    Gson gson = new Gson();
                    PoliciesInsuranceTokenResponse base = gson.fromJson(object, PoliciesInsuranceTokenResponse.class);

                    if (base.getResponseCode() == 1000) {
                        messageId = base.getData().getMessageId();
                        callWebView();
                        // WSCreationRequest();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private void WSCreationRequest() {

        try {

            JSONObject json = new JSONObject();

            /*HEADER*/
            JSONObject jsonObjectHeader = new JSONObject();
            jsonObjectHeader.put("source", "careager");
            jsonObjectHeader.put("deviceId", "agency");
            jsonObjectHeader.put("transactionName", "createLead");
            jsonObjectHeader.put("browser", "Chrome V - 77.0.3865.92");
            json.put("header", jsonObjectHeader); /* END HEADER*/

            JSONObject jsonObjectBody = new JSONObject();
            jsonObjectBody.put("userName", "careager");

            /* body >> quoteParam*/
            JSONObject jsonQuoteParam = new JSONObject();
            jsonQuoteParam.put("quoteType", "3");
            jsonObjectBody.put("quoteParam", jsonQuoteParam);

            jsonObjectBody.put("calcQuote", true);
            jsonObjectBody.put("campaign_id", "73e06d6e-0eda-099d-1459-5cd976db53b9");
            jsonObjectBody.put("requestSource", "agency");
            jsonObjectBody.put("agencyId", "careager");
            jsonObjectBody.put("userName", "careager");

            /* body >> contactInfo*/
            JSONObject jsonContactInfo = new JSONObject();
            jsonContactInfo.put("messageId", "");
            jsonContactInfo.put("termsCondition", true);
            jsonContactInfo.put("firstName", request.getFirstName());
            jsonContactInfo.put("lastName", request.getLastName());
            jsonContactInfo.put("emailId", request.getEmailId());
            jsonContactInfo.put("mobileNumber", request.getMobileNumber());
            jsonContactInfo.put("createLeadStatus", true);
            jsonObjectBody.put("contactInfo", jsonContactInfo);

            /* body >> commonInfo*/
            JSONObject jsonCommonInfo = new JSONObject();
            /* body >> commonInfo >> address*/
            JSONObject jsonAddress = new JSONObject();
            jsonAddress.put("streetDetails", "");
            jsonAddress.put("city", "");
            jsonAddress.put("state", "");
            jsonAddress.put("pincode", "");
            jsonAddress.put("displayArea", "");
            jsonAddress.put("address", "");
            jsonCommonInfo.put("address", jsonAddress);
            jsonObjectBody.put("commonInfo", jsonCommonInfo);

            /* body >> carInfo*/
            JSONObject jsonCarInfo = new JSONObject();
            jsonCarInfo.put("PreviousPolicyExpiryDate", request.getPrevPolicyEndDate());
            jsonCarInfo.put("registrationYear", request.getRegistrationYear());
            jsonCarInfo.put("city", "");
            jsonCarInfo.put("state", "");
            jsonCarInfo.put("registrationPlace", "");
            jsonCarInfo.put("make", request.getMake());
            jsonCarInfo.put("model", request.getModel());
            jsonCarInfo.put("variant", request.getVariant());
            jsonCarInfo.put("dateOfRegistration", "");
            jsonCarInfo.put("registrationNumber", request.getRegistrationNumber());
            jsonCarInfo.put("chasisNumber", "");
            jsonCarInfo.put("engineNumber", "");
            jsonObjectBody.put("carInfo", jsonCarInfo);

            /* body >> insuranceDetails*/
            JSONObject jsonInsuranceDetails = new JSONObject();
            jsonInsuranceDetails.put("isPrevPolicy", false);
            jsonInsuranceDetails.put("prevPolicyStartDate", "");
            jsonInsuranceDetails.put("policyNumber", "");
            jsonInsuranceDetails.put("insuranceType", request.getInsuranceType());
            jsonInsuranceDetails.put("prevPolicyEndDate", request.getPrevPolicyEndDate());
            jsonInsuranceDetails.put("ncb", request.getNcb());//20
            jsonInsuranceDetails.put("insurerName", "");//Avinay Sharma
            jsonInsuranceDetails.put("insurerId", "");//NIAAG00050453
            jsonInsuranceDetails.put("prevPolicyType", "");
            jsonInsuranceDetails.put("previousPolicyExpired", "");
            jsonObjectBody.put("insuranceDetails", jsonInsuranceDetails);

            json.put("body", jsonObjectBody);/* END BODY*/

            Log.d("JSON", jsonObjectBody.toString());

            NetworkTask networkTask = new NetworkTask(getActivity(), 2, json.toString());
            networkTask.setDialogMessage("Loading...");
            networkTask.exposePostExecute(this);
            networkTask.execute(url);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void callWebView() {
        Bundle bundle = new Bundle();
        String url = "http://uagency.policies365.com/dcpa/#/iquoteNavigation?messageId=" + messageId + "&lob=3&P365Token=" + token + "&source=CarEager&UniqueId=12000&url=http://13.233.36.16:4000/api/v3.4/loggedIn/details";
        bundle.putString(Constant.keyUrl, url);
        //((HomeScreen) getActivity()).makeDrawerVisible();
        //((HomeScreen) getActivity()).addFragment(new WebViewFragment(), FragmentTags.FRAGMENT_WEB_VIEW, true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
//
        Log.d("URL", url);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //  intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null);
            startActivity(intent);
        }
    }
}
