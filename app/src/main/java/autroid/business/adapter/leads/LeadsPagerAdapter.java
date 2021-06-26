package autroid.business.adapter.leads;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import autroid.business.utils.Constant;
import autroid.business.view.fragment.leads.LeadBookingFragment;
import autroid.business.view.fragment.leads.LeadsListFragment;

public class LeadsPagerAdapter extends FragmentPagerAdapter {

    Context context;

    private String stages[] = {"Open", "Follow-Up", "Lead Booking"};

    public LeadsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

    }

    @Override
    public Fragment getItem(int i) {
        Bundle bundle = new Bundle();
        LeadsListFragment leadsListFragment = new LeadsListFragment();

        switch (i) {
            case 0:
            case 1:
                bundle.putString(Constant.KEY_TYPE, stages[i]);
                leadsListFragment.setArguments(bundle);
                return leadsListFragment;
            case 2:
                LeadBookingFragment leadBookingFragment=new LeadBookingFragment();
                bundle.putString(Constant.KEY_TYPE, stages[i]);
                leadBookingFragment.setArguments(bundle);
                return leadBookingFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
