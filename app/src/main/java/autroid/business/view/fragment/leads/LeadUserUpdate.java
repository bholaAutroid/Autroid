package autroid.business.view.fragment.leads;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.UserBE;
import autroid.business.model.request.LeadUserRequest;
import autroid.business.model.response.EditLeadResponse;
import autroid.business.presenter.LeadUserUpdatePresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class LeadUserUpdate extends DialogFragment {

    RelativeLayout mainLayout;

    TextView userName,userEmail,userContact;

    UserBE user;

    LeadUserUpdatePresenter presenter;

    Button update;

    String leadId="";

    public LeadUserUpdate(){}

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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_lead_user_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        userName=view.findViewById(R.id.name);
        userContact=view.findViewById(R.id.contact);
        userEmail=view.findViewById(R.id.email);
        update=view.findViewById(R.id.update);
        mainLayout=view.findViewById(R.id.main_layout);

        presenter=new LeadUserUpdatePresenter(this,mainLayout);

        user=(UserBE)getArguments().getSerializable(Constant.DETAILS_TYPE);
        leadId=getArguments().getString(Constant.KEY_ID);

        setPrimaryData();

        update.setOnClickListener(v->{
            if(validate()){
                LeadUserRequest request=new LeadUserRequest();
                request.setLead(leadId);
                request.setName(userName.getText().toString().trim());
                request.setContact_no(userContact.getText().toString().trim());
                request.setEmail(userEmail.getText().toString().trim());
                presenter.updateUserDetails(request);
            }
        });

    }

    private void setPrimaryData() {

        if(user.getName().equals("N/A"))userName.setText("");
        else {
            userName.setText(user.getName());
            userName.setEnabled(false);
        }

        if(user.getContact_no().equals("N/A"))userContact.setText("");
        else {
            userContact.setText(user.getContact_no());
            userContact.setEnabled(false);
        }

        if(user.getEmail().equals("N/A"))userEmail.setText("");
        else {
            userEmail.setText(user.getEmail());
            userEmail.setEnabled(false);
        }

    }

    private boolean validate(){

        if(userName.getText().toString().trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Invalid Name");
            return false;
        }else if(userContact.getText().toString().trim().length()!=10){
            Utility.showResponseMessage(mainLayout,"Invalid Contact");
            return false;
        }else if(userEmail.getText().toString().trim().length()>0 && !Utility.isEmailValid(mainLayout,userEmail.getText().toString().trim())){
            Utility.showResponseMessage(mainLayout,"Invalid Email");
            return false;
        }

        return true;
    }

    public void onSuccessUpdate(EditLeadResponse response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_USER_DETAILS_UPDATE);
        broadcastIntent.putExtra(Constant.KEY_ID,response.getUser().getId());

        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();

    }
}
