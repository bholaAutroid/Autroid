package autroid.business.view.fragment.profile.subscription;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import autroid.business.R;

public class ProFragment extends Fragment {

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

        tvAmount=view.findViewById( R.id.tv_amount_pro );
        btnUpgrade=view.findViewById( R.id.btnUpgradeProSubs );

    }
}