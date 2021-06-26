package autroid.business.view.fragment.jobcard;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import autroid.business.R;
import autroid.business.adapter.jobcard.JobsPagerAdapter;
import autroid.business.realm.RealmController;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsPagerFragment extends Fragment {

    ViewPager mViewPager;
    RealmController realmController;

    public JobsPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_card_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        realmController=new RealmController(getActivity().getApplication());
        realmController.clearAllJobs();


        mViewPager=view.findViewById(R.id.pager_careager);
        mViewPager.setOffscreenPageLimit(7);
        mViewPager.setAdapter(new JobsPagerAdapter(getChildFragmentManager(), getActivity()));
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(40, 0, 40, 0);

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);

    }
}
