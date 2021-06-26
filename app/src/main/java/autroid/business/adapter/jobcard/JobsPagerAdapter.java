package autroid.business.adapter.jobcard;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import autroid.business.utils.Constant;
import autroid.business.view.fragment.jobcard.JobsListFragment;

public class JobsPagerAdapter extends FragmentPagerAdapter {

    private String stages[] = {"NewJob", "In-Process", "QC", "StoreApproval","Ready"};

    Context mContext;

    public JobsPagerAdapter(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int i) {

        Bundle bundle = new Bundle();
        JobsListFragment jobsListFragment = new JobsListFragment();

        switch (i) {

            case 0:

            case 1:

            case 2:

            case 3:

            case 4:
                bundle.putString(Constant.KEY_TYPE, stages[i]);
                jobsListFragment.setArguments(bundle);
                return jobsListFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
