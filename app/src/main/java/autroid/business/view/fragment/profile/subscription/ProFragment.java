package autroid.business.view.fragment.profile.subscription;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.utils.Constant;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.payment.PaytmPaymentFragment;
import autroid.business.view.fragment.profile.ShowroomReviewsFragment;

public class ProFragment extends Fragment implements View.OnClickListener {

    TextView tvAmount;
    Button btnUpgrade;

    public ProFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_pro, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        tvAmount = view.findViewById( R.id.tv_amount_pro );
        btnUpgrade = view.findViewById( R.id.btnUpgradeProSubs );

        btnUpgrade.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpgradeProSubs:

                ((AwsHomeActivity) getActivity()).makeDrawerVisible();
                Bundle bundle1 = new Bundle();
                bundle1.putString( Constant.KEY_ID, "87387");
                ((AwsHomeActivity) getActivity()).addFragment( new PaytmPaymentFragment(), "ShowroomReviewsFragment", true, false, bundle1, ((AwsHomeActivity) getActivity()).currentFrameId );
                break;

        }
    }
}