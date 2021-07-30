package autroid.business.aws.crm.aws_leads;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import autroid.business.view.fragment.profile.subscription.FreeFragment;
import autroid.business.view.fragment.profile.subscription.ProFragment;
import autroid.business.view.fragment.profile.subscription.StandardsFragment;
import autroid.business.view.fragment.profile.subscription.UltimateFragment;

public class LeadsTabAdapter extends FragmentPagerAdapter {


    int tabCount;

    public LeadsTabAdapter(@NonNull @NotNull FragmentManager fm, int tabCount) {
        super( fm, tabCount );
        this.tabCount = tabCount;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LeadsOpenFragment();
            case 1:
                return new LeadsFallowUpFragment();
            case 2:
                return new LeadsEstimateFragment();
            case 3:
                return new LeadsPsfFragment();
            case 4:
                return new LeadsApprovalFragment();
            case 5:
                return new LeadsConvertedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
