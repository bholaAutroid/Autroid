package autroid.business.view.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Locale;

import autroid.business.R;
import autroid.business.utils.Constant;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePagerFragment extends Fragment {

    ViewPager mViewPager;
    TabLayout tabs;

    public ProfilePagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.pager_explore);
        tabs = (TabLayout) view.findViewById(R.id.pager_tab_strip);

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(),getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        tabs.setupWithViewPager(mViewPager);

        if(getArguments()!=null){
            if(getArguments().containsKey(Constant.KEY_ID)){
                mViewPager.setCurrentItem(getArguments().getInt(Constant.KEY_ID));
            }
        }
       // mViewPager.setOffscreenPageLimit(2);
    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ShowroomFragment();
                case 1:
                    return new SubscriptionFragment();
                case 2:
                    return new MyWalletFragment();
            }
            return null;
        }


        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.fragment_myprofile);
                case 1:
                   return getString( R.string.fragment_careager_subscription );
                case 2:
                    return getString(R.string.fragment_careager_cash);

            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    }

