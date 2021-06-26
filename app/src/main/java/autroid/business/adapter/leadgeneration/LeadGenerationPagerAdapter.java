package autroid.business.adapter.leadgeneration;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import autroid.business.view.fragment.leadgeneration.InsuranceDueFragment;
import autroid.business.view.fragment.leadgeneration.ServiceReminderFragment;

public class LeadGenerationPagerAdapter extends FragmentPagerAdapter {

    Context context;

    public LeadGenerationPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new InsuranceDueFragment();
            case 1:
                return new ServiceReminderFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
