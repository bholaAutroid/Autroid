package autroid.business.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import autroid.business.R;
import autroid.business.model.request.ClaimBusinessRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.ClaimBusinessPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClaimBusinessFragment extends DialogFragment implements View.OnClickListener {

    private EditText mContact,mEmail,mDescription;
    private TextView mName;
    private Button mDone;
    RelativeLayout mMainLayout;
    ClaimBusinessPresenter mPresenter;

    String strContact,strEmail,strDescription;
    String strName,strId;

    public ClaimBusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_claim_business, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContact= (EditText) view.findViewById(R.id.business_mobile);
        mEmail= (EditText) view.findViewById(R.id.business_email);
        mDescription= (EditText) view.findViewById(R.id.business_description);
        mName= (TextView) view.findViewById(R.id.business_name);
        mDone= (Button) view.findViewById(R.id.business_done);
        mDone.setOnClickListener(this);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new ClaimBusinessPresenter(this,mMainLayout);

        mMainLayout.setOnClickListener(this);

        Bundle bundle=getArguments();
        if(bundle!=null){
            strName=bundle.getString(Constant.KEY_CATEGORY_NAME);
            strId=bundle.getString(Constant.KEY_ID);
            mName.setText(strName);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.business_done:
                if(validate()){
                    ClaimBusinessRequest claimBusinessRequest=new ClaimBusinessRequest();
                    claimBusinessRequest.setBusiness(strId);
                    claimBusinessRequest.setDescription(strDescription);
                    claimBusinessRequest.setEmail(strEmail);
                    claimBusinessRequest.setPhone(strContact);
                    mPresenter.claimBussiness(claimBusinessRequest);
                }
                break;
            case R.id.main_layout:
                Utility.hideSoftKeyboard(getActivity());
            break;
        }
    }

    private boolean validate(){
        boolean flag=true;

        strContact=mContact.getText().toString();
        strEmail=mEmail.getText().toString();
        strDescription=mDescription.getText().toString();

        if(strContact.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter contact number");

        }
        else if(strEmail.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter email-id");

        }
        else if(strDescription.trim().length()==0){
            flag=false;
            Utility.showResponseMessage(mMainLayout,"Please enter description");

        }

        return flag;
    }

    public void claimBussSuccess(BaseResponse baseResponse){
        Toast.makeText(getActivity(),baseResponse.getResponseMessage(),Toast.LENGTH_LONG).show();
       // Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
        getDialog().dismiss();
    }
}
