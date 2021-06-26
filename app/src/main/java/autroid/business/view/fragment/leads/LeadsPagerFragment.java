package autroid.business.view.fragment.leads;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import autroid.business.R;
import autroid.business.adapter.leads.LeadsPagerAdapter;
import autroid.business.utils.Constant;
import me.relex.circleindicator.CircleIndicator;

public class LeadsPagerFragment extends Fragment {

    private ViewPager viewPager;

    public LeadsPagerFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_leads_pager,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        viewPager=view.findViewById(R.id.leads_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new LeadsPagerAdapter(getChildFragmentManager(),getActivity()));
        viewPager.setClipToPadding(false);
        viewPager.setPadding(40, 0, 40, 0);
        viewPager.setOffscreenPageLimit(3);

        if(getArguments()!=null && getArguments().containsKey(Constant.KEY_ID))
            viewPager.setCurrentItem(getArguments().getInt(Constant.KEY_ID));

        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }
}
