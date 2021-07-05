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

public class UltimateFragment extends Fragment {
    TextView tvAmount;
    Button btnUpgrade;

    public UltimateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_ultimate, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        btnUpgrade=view.findViewById( R.id.btnUpgradeUltimate );
        tvAmount=view.findViewById( R.id.tv_amount_ultimate );
    }
}