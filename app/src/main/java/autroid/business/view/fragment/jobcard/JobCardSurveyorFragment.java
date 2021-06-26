package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.UserBE;
import autroid.business.model.request.SurveyorRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.GetDriverResponse;
import autroid.business.presenter.jobcard.JobCardSurveyorPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;

public class JobCardSurveyorFragment extends Fragment {

    TextView title;
    Button save;
    LinearLayout mainLayout,otherDetails;


    String surveyorId = "",bookingId="";

    EditText surveyorPhone, surveyorName, surveyorEmail;

    private JobCardSurveyorPresenter presenter;

    public JobCardSurveyorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_card_claim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        title = view.findViewById(R.id.toolbar_title);
        title.setText("Surveyor Details");

        bookingId = getArguments().getString(Constant.KEY_ID);

        mainLayout = view.findViewById(R.id.main_layout);
        otherDetails = view.findViewById(R.id.other_details);
        save = view.findViewById(R.id.save);
        surveyorPhone = view.findViewById(R.id.jobcard_surveyor_contact);
        surveyorName = view.findViewById(R.id.jobcard_surveyor_name);
        surveyorEmail = view.findViewById(R.id.jobcard_surveyor_email);
        presenter = new JobCardSurveyorPresenter(this, mainLayout);

        //presenter.

        surveyorPhone.requestFocus();
        Utility.showSoftKeyboard(getActivity(), surveyorPhone);

        if(getArguments().getBoolean(Constant.VALUE)){
            UserBE surveyor= (UserBE) getArguments().getSerializable(Constant.DETAILS_TYPE);
            surveyorPhone.setText(surveyor.getContact_no());
            presenter.getSurveyorDetails(surveyor.getContact_no());
        }

        surveyorPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 10) {
                    presenter.getSurveyorDetails(surveyorPhone.getText().toString().trim());
                    surveyorPhone.clearFocus();
                    surveyorName.setText("");
                    surveyorEmail.setText("");
                } else if (charSequence.toString().length() < 10 && surveyorName.getVisibility() == View.VISIBLE) {
                    otherDetails.setVisibility(View.GONE);
                    surveyorName.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged (Editable editable){

            }
    });


        save.setOnClickListener(v->{
            ((HomeScreen) getActivity()).disableButton(save);
            if(validate())presenter.setSurveyorDetails(createSurveyorRequest());
        });

}

    private SurveyorRequest createSurveyorRequest() {

        SurveyorRequest surveyorRequest=new SurveyorRequest();
        surveyorRequest.setBooking(bookingId);
        surveyorRequest.setSurveyorId(surveyorId);
        surveyorRequest.setSurveyorContact(surveyorPhone.getText().toString().trim());
        surveyorRequest.setSurveyorName(surveyorName.getText().toString().trim());
        surveyorRequest.setEmail(surveyorEmail.getText().toString().trim());
        return surveyorRequest;
    }

    private boolean validate() {

        if(surveyorPhone.getText().toString().trim().length()!=10) {
            Utility.showResponseMessage(mainLayout, "Invalid Contact Number");
            return false;
        }else if(surveyorName.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout, "Invalid Name");
            return false;
        }

        return true;
    }

    public void onSuccessSurveyor(GetDriverResponse response) {// it is surveyor response but the reponse type is same as driver
        otherDetails.setVisibility(View.VISIBLE);

        if (response.getDriverDataBE().getName() == null) {
            surveyorId = "";
            surveyorName.setEnabled(true);
            surveyorName.requestFocus();
            Utility.showSoftKeyboard(getActivity(), surveyorName);
        }else {
            surveyorName.setEnabled(false);
            surveyorId = response.getDriverDataBE().getDriverId();
            surveyorName.setText(response.getDriverDataBE().getName());
            surveyorEmail.setText(response.getDriverDataBE().getEmail());
        }
    }

    public void onSuccessSurveyorDetails(BaseResponse response) {
        Toast.makeText(getActivity(),response.getResponseMessage(),Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getActivity().onBackPressed();
    }
}
