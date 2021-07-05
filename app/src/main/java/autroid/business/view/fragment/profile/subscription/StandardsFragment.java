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

public class StandardsFragment extends Fragment {
    TextView tvAmount;
    Button btnUpgrade;

    public StandardsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_standards, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        tvAmount=view.findViewById( R.id.tv_amount_standards );
        btnUpgrade=view.findViewById( R.id.btnUpgradeStandards );
    }
}