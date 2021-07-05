package autroid.business.view.fragment.profile.profile_tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

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
                return new ImageFragment1();
            case 1:
                return new OfferFragment2();
            case 2:
                return new ServiceFragment3();
            case 3:
                return new ReviewFragment4();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
