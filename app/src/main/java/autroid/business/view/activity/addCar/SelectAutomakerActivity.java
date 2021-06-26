package autroid.business.view.activity.addCar;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.AutomakerAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CarItemsBE;
import autroid.business.utils.Constant;
import autroid.business.utils.RecyclerItemClickListener;
import autroid.business.view.activity.HomeScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectAutomakerActivity extends Fragment {

    RecyclerView recList;
    ArrayList<CarItemsBE> mList;
    AutomakerAdapter automakerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_select_automaker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    //    GlobalBus.getBus().register(this);

        recList= (RecyclerView) view.findViewById(R.id.automaker_list);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle intent=getArguments();
        if(intent!=null){
            mList= (ArrayList<CarItemsBE>) intent.getSerializable(Constant.KEY_AUTOMAKER);
        }

        automakerAdapter=new AutomakerAdapter(getActivity(),mList);
        recList.setAdapter(automakerAdapter);

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle=new Bundle();
                        bundle.putString(Constant.KEY_MAKER_ID,mList.get(position).getId());
                        bundle.putString(Constant.KEY_MAKER_NAME,mList.get(position).getValue());

                        ((HomeScreen) getActivity()).addFragment(new SelectModelActivity(), "SelectModelActivity", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                       // finish();


                    }

                }));

        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Automaker");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
         //   onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }


    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getDoubleExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SELECT_CAR1) {

            getActivity().onBackPressed();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
