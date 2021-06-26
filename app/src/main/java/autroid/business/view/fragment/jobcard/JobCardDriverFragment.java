package autroid.business.view.fragment.jobcard;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.response.UserDataResponse;
//import careager.business.presenter.jobcard.JobCardDriverPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobCardDriverFragment extends Fragment {

    @BindView(R.id.mobile)
    EditText mMobile;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    //JobCardDriverPresenter mPresenter;

    public JobCardDriverFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_card_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        //mPresenter=new JobCardDriverPresenter(this,mMainLayout);

        mMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()==10){
                   // mPresenter.verifyUser(s.toString(),"driver");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void onSuccess(UserDataResponse userDataResponse) {

    }

    public void onError() {

    }
}
