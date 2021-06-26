package autroid.business.view.fragment.jobcard;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.bean.AddressBE;
import autroid.business.model.bean.UserBE;
import autroid.business.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class JobsUserDetails extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.mobile)
    TextView mMobile;
    @BindView(R.id.email)
    TextView mEmail;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.details_type)
    TextView detailType;


    UserBE userBE;
    AddressBE addressBE;

    public JobsUserDetails() {
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
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_jobs_user_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        userBE= (UserBE) getArguments().getSerializable(Constant.Key_Business_Name);
        addressBE= (AddressBE) getArguments().getSerializable(Constant.Key_Business_address);

        detailType.setText(getArguments().getString(Constant.DETAILS_TYPE));

        if(userBE!=null) {
            mName.setText(userBE.getName());
            mMobile.setText(userBE.getContact_no());

            if(!userBE.getEmail().equals(""))
                mEmail.setText(userBE.getEmail());
            else
                mEmail.setVisibility(View.GONE);
        }

        mMobile.setOnClickListener(this::onClick);

        if(addressBE!=null) mAddress.setText(addressBE.getAddress() + " , " + addressBE.getCity() + " , " + addressBE.getZip());
        else mAddress.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.mobile:
                    try {
                        if ( userBE!= null && userBE.getContact_no().length() == 10){
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", userBE.getContact_no(), null));
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getLocalizedMessage();
                    }
                    break;
            }
    }
}
