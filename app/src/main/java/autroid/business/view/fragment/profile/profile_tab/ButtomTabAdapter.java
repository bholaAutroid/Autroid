package autroid.business.view.fragment.profile.profile_tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import autroid.business.view.fragment.profile.ShowroomCarsFragment;
import autroid.business.view.fragment.profile.ShowroomOffersFragment;
import autroid.business.view.fragment.profile.ShowroomProductsFragment;
import autroid.business.view.fragment.profile.ShowroomReviewsFragment;
import autroid.business.view.fragment.profile.profile_tab.profileutilities.GallaryFragment;

public class ButtomTabAdapter extends FragmentPagerAdapter {
    int tabcount;


    public ButtomTabAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super( fm, behavior );
        tabcount = behavior;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GallaryFragment();
            case 1:
                return new ShowroomOffersFragment();
            case 2:
                return new ShowroomProductsFragment();
            case 3:
                return new ShowroomReviewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
