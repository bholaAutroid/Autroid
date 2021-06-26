package autroid.business.view.fragment.booking;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import autroid.business.R;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingsPagerFragment extends Fragment {

    ViewPager mViewPager;


    RealmController realmController;

    public static final String stages[] = {"Approval","Confirmed","Missed","Pending","Assigned"};
    public static final String soryBy[] = {"", "", "", "date", ""};

    public BookingsPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookings_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.pager_careager);

        realmController=new RealmController(getActivity().getApplication());
        realmController.clearBookings();

        CustomPageAdapter mCustomPagerAdapter = new CustomPageAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(50, 0, 50, 0);
        mViewPager.setPageMargin(20);
        mViewPager.setOffscreenPageLimit(5);

    }

    public class CustomPageAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPageAdapter(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();

            BookingsFragment bookingsFragment = new BookingsFragment();
            switch (i) {
                case 0:
                    bundle.putString(Constant.KEY_TYPE, stages[i]);
                    bundle.putString(Constant.Key_Source, soryBy[i]);
                    bookingsFragment.setArguments(bundle);
                    return bookingsFragment;
                case 1:
                    bundle.putString(Constant.KEY_TYPE, stages[i]);
                    bundle.putString(Constant.Key_Source, soryBy[i]);
                    bookingsFragment.setArguments(bundle);
                    return bookingsFragment;
                case 2:
                    bundle.putString(Constant.KEY_TYPE, stages[i]);
                    bundle.putString(Constant.Key_Source, soryBy[i]);
                    bookingsFragment.setArguments(bundle);
                    return bookingsFragment;
                /*case 3:
                    bundle.putString(Constant.KEY_TYPE, stages[i]);
                    bundle.putString(Constant.Key_Source, soryBy[i]);
                    bookingsFragment.setArguments(bundle);
                    return bookingsFragment;
                case 4:
                    LeadAssignedFragment leadAssignedFragment=new LeadAssignedFragment();
                    bundle.putString(Constant.KEY_TYPE, stages[i]);
                    bundle.putString(Constant.Key_Source, soryBy[i]);
                    leadAssignedFragment.setArguments(bundle);
                    return leadAssignedFragment;*/
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
