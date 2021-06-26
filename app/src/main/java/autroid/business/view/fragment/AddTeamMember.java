package autroid.business.view.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.RoleBE;
import autroid.business.model.request.MemberRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.RolesResponse;
import autroid.business.presenter.TeamMemberPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class AddTeamMember extends DialogFragment {

    AppCompatSpinner rolesSpinner;

    RelativeLayout relativeLayout;

    Button addMember;

    TeamMemberPresenter teamMemberPresenter;

    ArrayList<RoleBE> arrayList;

    EditText name,contactNumber,email;

    public AddTeamMember(){}

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
        return inflater.inflate(R.layout.fragment_add_member, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        relativeLayout=view.findViewById(R.id.main_layout);
        rolesSpinner=view.findViewById(R.id.spinner_roles);
        name=view.findViewById(R.id.member_name);
        contactNumber=view.findViewById(R.id.mobile);
        email=view.findViewById(R.id.email);
        addMember=view.findViewById(R.id.add_member);

        teamMemberPresenter=new TeamMemberPresenter(this,relativeLayout);

        teamMemberPresenter.getRoles();

        addMember.setOnClickListener(v->{
            if(validate())teamMemberPresenter.setTeamMember(createMemberRequest());
        });

    }

    private boolean validate(){

        if(name.getText().toString().trim().length()==0){
            Utility.showResponseMessage(relativeLayout,"Please enter a valid name");
            return false;
        }else if(contactNumber.getText().toString().trim().length()!=10){
            Utility.showResponseMessage(relativeLayout,"Please enter a valid number");
            return false;
        }else if(email.getText().toString().trim().length()!=0 && !Utility.isEmailValid(relativeLayout,email.getText().toString().trim())){
            Utility.showResponseMessage(relativeLayout,"Please enter a valid email");
            return false;
        }else if(rolesSpinner.getSelectedItem()==null){
            Utility.showResponseMessage(relativeLayout,"Please refresh the screen");
            return false;
        } else if(rolesSpinner.getSelectedItemPosition()==0){
            Utility.showResponseMessage(relativeLayout,"Please select a role");
            return false;
        }

        return true;
    }

    public MemberRequest createMemberRequest(){

        MemberRequest memberRequest=new MemberRequest();
        memberRequest.setName(name.getText().toString().trim());
        memberRequest.setContact_no(contactNumber.getText().toString().trim());
        memberRequest.setEmail(email.getText().toString().trim());
        memberRequest.setRole(rolesSpinner.getSelectedItem().toString().trim());
        memberRequest.setDepartment(arrayList.get(rolesSpinner.getSelectedItemPosition()-1).getDepartment());

        return memberRequest;
    }

    public void onSuccessRoles(RolesResponse response){

        arrayList=new ArrayList<>();
        arrayList.addAll(response.getRoleData());

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Role");

        for (RoleBE data: response.getRoleData())arrayList.add(data.getRole());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.layout_spinner_remark, arrayList);
        rolesSpinner.setAdapter(arrayAdapter);
    }

    public void onSuccessMemberAdded(BaseResponse response) {

        Toast.makeText(getActivity(),response.getResponseMessage(),Toast.LENGTH_LONG).show();
        getDialog().dismiss();

        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_MEMBER_ADDED);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
    }
}
