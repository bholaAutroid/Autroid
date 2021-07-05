package autroid.business.view.fragment.profile.subscription;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import autroid.business.R;

public class SubscriptionFragment extends Fragment {

    private TabLayout tabLayoutSubs;
    private TabItem tabItem1,tabItem2,tabItem3,tabItem4;
    private ViewPager viewPagerSubs;
    private SubsTabAdapter subsTabAdapter;


    public SubscriptionFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_subscription, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        tabLayoutSubs = view.findViewById( R.id.tab_buttom );
        tabItem1 = view.findViewById( R.id.tab1 );
        tabItem2 = view.findViewById( R.id.tab2 );
        tabItem3 = view.findViewById( R.id.tab3 );
        tabItem4 = view.findViewById( R.id.tab4 );
        viewPagerSubs = view.findViewById( R.id.buttomTabViewpager );

        subsTabAdapter = new SubsTabAdapter(getChildFragmentManager(), tabLayoutSubs.getTabCount() );
        viewPagerSubs.setAdapter( subsTabAdapter );


        tabLayoutSubs.setOnTabSelectedListener( new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerSubs.setCurrentItem( tab.getPosition() );

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3) {
                    subsTabAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        } );

        viewPagerSubs.addOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener( tabLayoutSubs ) );

    }


}