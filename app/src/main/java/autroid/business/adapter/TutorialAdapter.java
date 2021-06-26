package autroid.business.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.interfaces.SearchBusinessCallback;
import autroid.business.model.bean.TutorialBE;

/**
 * Created by pranav.mittal on 06/30/17.
 */

public class TutorialAdapter extends PagerAdapter implements View.OnClickListener {

    Context context;
    ArrayList<TutorialBE> mList;
    SearchBusinessCallback mSearchBusinessCallback;
    LayoutInflater inflater;

    public  TutorialAdapter(Context context,ArrayList<TutorialBE> mList,SearchBusinessCallback mSearchBusinessCallback){
        this.context=context;
        this.mList=mList;
        this.mSearchBusinessCallback=mSearchBusinessCallback;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        TextView mHeading,mLineOne,mLineTwo,mSwipe;
        Button mGetStarted;

        ImageView mImgBg;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.row_tutorial, container, false);

        mHeading= (TextView) itemView.findViewById(R.id.tutorial_heading);
        mLineOne= (TextView) itemView.findViewById(R.id.tutorial_line_one);
        mLineTwo= (TextView) itemView.findViewById(R.id.tutorial_line_two);

        mImgBg= (ImageView) itemView.findViewById(R.id.tutorial_img);
        mGetStarted= (Button) itemView.findViewById(R.id.tutorial_get_started);
        mGetStarted.setOnClickListener(this);
        mSwipe= (TextView) itemView.findViewById(R.id.tutorial_swipe_to_know);

        mImgBg.setBackgroundResource(mList.get(position).getImgId());
        mHeading.setText(mList.get(position).getHeading());
        mLineOne.setText(mList.get(position).getLineOne());
        mLineTwo.setText(mList.get(position).getLineTwo());

        if(position==0){
            mSwipe.setVisibility(View.GONE);
        }
        else {
            mSwipe.setVisibility(View.GONE);
        }

        if(position==mList.size()-1){
            mGetStarted.setVisibility(View.GONE);
        }
        else {
            mGetStarted.setVisibility(View.GONE);
        }

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tutorial_get_started:
                mSearchBusinessCallback.clickGetStarted();
                break;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
