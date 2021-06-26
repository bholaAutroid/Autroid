package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.UserBE;
import autroid.business.model.request.UpdateCustomerDetailsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobCustomerUpdatePresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class JobCustomerUpdateFragment extends DialogFragment {

    private ConstraintLayout mainLayout;

    private JobCustomerUpdatePresenter presenter;

    private EditText name,phone,email,companyName,gstIn;

    private Button update;

    private UserBE userBE;

    public JobCustomerUpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_jobs_customer_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mainLayout=view.findViewById(R.id.main_layout);

        presenter=new JobCustomerUpdatePresenter(this,mainLayout);

        userBE = (UserBE) getArguments().getSerializable(Constant.USER_DATA);

        name=view.findViewById(R.id.name);
        phone=view.findViewById(R.id.contact);
        email=view.findViewById(R.id.email);
        companyName=view.findViewById(R.id.company_name);
        gstIn=view.findViewById(R.id.gst_number);
        update=view.findViewById(R.id.update);

        name.setText(userBE.getName());
        phone.setText(userBE.getContact_no());
        email.setText(userBE.getEmail());
        companyName.setText(userBE.getBusiness_info().getCompany_name());
        gstIn.setText(userBE.getBusiness_info().getGstin());

        update.setOnClickListener(v->{

            if(validate()){

                UpdateCustomerDetailsRequest request=new UpdateCustomerDetailsRequest();
                request.setUser(userBE.getId());
                request.setName(name.getText().toString().trim());
                request.setContact_no(phone.getText().toString().trim());
                request.setEmail(email.getText().toString().trim());
                request.setCompany_name(companyName.getText().toString().trim());
                request.setGstin(gstIn.getText().toString().trim());

                presenter.updateDetails(request);
            }
        });
    }

    private boolean validate(){

        if(name.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Name");
            return false;
        }else if(phone.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Contact");
            return false;
        }
        else if(phone.getText().toString().trim().length()!=10){
            Utility.showResponseMessage(mainLayout,"Invalid mobile number");
            return false;
        }else if(email.getText().toString().trim().length()!=0 && !Utility.isEmailValid(mainLayout,email.getText().toString().trim())){
            Utility.showResponseMessage(mainLayout,"Invalid Email");
            return false;
        }

        return true;
    }

    public void onSuccess(BaseResponse response) {
       Toast.makeText(getActivity(),response.getResponseMessage(),Toast.LENGTH_LONG).show();
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }
}
