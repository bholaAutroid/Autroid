package autroid.business.view.fragment.carsales;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.carsales.ServiceDetailsPresenter;
import autroid.business.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceDetailsFragment extends BottomSheetDialogFragment {

    @BindView(R.id.service_detail)
    TextView mServiceDetail;
    @BindView(R.id.main_layout)
    NestedScrollView mMainLayout;


    ServiceDetailsPresenter mPresenter;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_package_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
       /* String text=getArguments().getString(Constant.Key_Body);
        mServiceDetail.setText(text); */

        mPresenter=new ServiceDetailsPresenter(this,mMainLayout);
        mPresenter.getServiceDetails(getArguments().getString(Constant.KEY_ID),getArguments().getString(Constant.KEY_TYPE));
    }

    public void onSuccess(BaseResponse baseResponse) {
        if(baseResponse.getResponseMessage().length()>0)
            mServiceDetail.setText(baseResponse.getResponseMessage());
        else {
           mServiceDetail.setText("No Details Available");
           mServiceDetail.setGravity(Gravity.CENTER);
        }
    }

}
