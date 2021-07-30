package autroid.business.aws.crm.aws_leads;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import autroid.business.R;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.profile.subscription.SubsTabAdapter;

public class LeadsFragment extends Fragment {
    private TabLayout tabLayoutSubs;
    private TabItem tabItem1, tabItem2, tabItem3, tabItem4, tabItem5, tabItem6;
    private ViewPager viewPagerSubs;
    private LeadsTabAdapter leadsTabAdapter;

    TextView toolTitle;

    public LeadsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_new_leads, container, false );
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        tabLayoutSubs = view.findViewById( R.id.tab_buttom );
        tabItem1 = view.findViewById( R.id.tab1 );
        tabItem2 = view.findViewById( R.id.tab2 );
        tabItem3 = view.findViewById( R.id.tab3 );
        tabItem4 = view.findViewById( R.id.tab4 );
        tabItem5 = view.findViewById( R.id.tab5 );
        tabItem6 = view.findViewById( R.id.tab6 );

        toolTitle=view.findViewById( R.id.toolbar ).findViewById( R.id.toolbar_title );
        toolTitle.setText( "Leads" );

        ImageView back= view.findViewById( R.id.toolbar ).findViewById( R.id.tool_back );

        back.setOnClickListener( (View v)->{

            Intent intent=new Intent( getActivity(), AwsHomeActivity.class );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity( intent );

        } );

        viewPagerSubs = view.findViewById( R.id.buttomTabViewpager );

        leadsTabAdapter = new LeadsTabAdapter( getChildFragmentManager(), tabLayoutSubs.getTabCount() );
        viewPagerSubs.setAdapter( leadsTabAdapter );

        tabLayoutSubs.setOnTabSelectedListener( new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerSubs.setCurrentItem( tab.getPosition() );

                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3 || tab.getPosition() == 4 || tab.getPosition() == 5) {
                    leadsTabAdapter.notifyDataSetChanged();
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