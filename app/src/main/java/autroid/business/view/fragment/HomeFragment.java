package autroid.business.view.fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

import autroid.business.R;
import autroid.business.view.fragment.profile.ShowroomFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ViewPager mViewPager;
    TabLayout tabs;

    BarChart barChart;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (TabLayout) view.findViewById(R.id.pager_tab_strip);

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getActivity().getSupportFragmentManager(),getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        tabs.setupWithViewPager(mViewPager);

         barChart = (BarChart) view.findViewById(R.id.chart1);

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("2016");
        labels.add("2015");
        labels.add("2014");
        labels.add("2013");
        labels.add("2012");
        labels.add("2011");

        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
        bargroup1.add(new BarEntry(8f, 0));
        bargroup1.add(new BarEntry(2f, 1));
        bargroup1.add(new BarEntry(5f, 2));
        bargroup1.add(new BarEntry(20f, 3));
        bargroup1.add(new BarEntry(15f, 4));
        bargroup1.add(new BarEntry(19f, 5));

        BarDataSet bardataset = new BarDataSet(bargroup1, "Bar Group 1");

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(bardataset);

        BarData data = new BarData(bardataset);
        barChart.setData(data);

    }


    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {

            // Create fragment object
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new ShowroomFragment();
                case 1:
                    // Movies fragment activity
                    return new AnalyticsFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.fragment_showroom);
                case 1:
                    return getString(R.string.fragment_analytic);
            }

            return null;
        }
    }

}
