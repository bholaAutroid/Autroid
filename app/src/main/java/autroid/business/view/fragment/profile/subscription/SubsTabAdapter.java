package autroid.business.view.fragment.profile.subscription;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

public class SubsTabAdapter extends FragmentPagerAdapter {
    int tabcount;


    public SubsTabAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super( fm, behavior );
        tabcount = behavior;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FreeFragment();
            case 1:
                return new StandardsFragment();
            case 2:
                return new ProFragment();
            case 3:
                return new UltimateFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
