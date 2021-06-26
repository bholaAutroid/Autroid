package autroid.business.view.activity;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.TutorialAdapter;
import autroid.business.interfaces.SearchBusinessCallback;
import autroid.business.model.bean.TutorialBE;
import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener,SearchBusinessCallback {

    ArrayList<TutorialBE> mList;
    RecyclerView recList;
    TutorialAdapter mTutorialAdapter;
    TextView mLogin,mRegister;

    ViewPager mListPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mList=new ArrayList<TutorialBE>();
        setData();

        mLogin= (TextView) findViewById(R.id.login);
        mRegister= (TextView) findViewById(R.id.register);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        recList= (RecyclerView) findViewById(R.id.tutorial_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recList.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recList);

        mListPager = (ViewPager) findViewById(R.id.tutorial_list_pager);

        mTutorialAdapter=new TutorialAdapter(getApplicationContext(),mList,this);
        mListPager.setAdapter(mTutorialAdapter);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mListPager);
    }

    private void setData(){
        mList.add(new TutorialBE("Hi!","WELCOME TO\n'CAREAGER FOR BUSINESS'","",R.drawable.tutorial_one));
        mList.add(new TutorialBE("Show","Show your car sale, service, or repair related business to car owners for free","",R.drawable.tutorial_two));
        mList.add(new TutorialBE("Manage","Manage the online presence of\nyour business","",R.drawable.tutorial_three));
        mList.add(new TutorialBE("Earn","Earn by selling your cars and products for free!","",R.drawable.tutorial_four));
        mList.add(new TutorialBE("Grow","Grow by connecting and communicating with customers and other businesses!","",R.drawable.tutorial_five));
        mList.add(new TutorialBE("Analyze","Analyze your business using Views, Reviews, Ratings, Leads, Messages, and so on","",R.drawable.tutorial_six));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
            case R.id.register:
                startActivity(new Intent(getApplicationContext(),SearchClaimBusinessActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void clickGetStarted() {
        startActivity(new Intent(getApplicationContext(),SearchClaimBusinessActivity.class));
    }
}
