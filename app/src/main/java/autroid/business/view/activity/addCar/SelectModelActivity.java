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
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.R;
import autroid.business.adapter.AutomakerAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.response.CarModelsResponse;
import autroid.business.presenter.CarModelPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.RecyclerItemClickListener;
import autroid.business.view.activity.HomeScreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectModelActivity extends Fragment
{

    RecyclerView recList;
    String makerID,makerName;
    AutomakerAdapter automakerAdapter;
    LinearLayout mMainLayout;
    CarModelPresenter mPresenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_select_model, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recList= (RecyclerView)
                view.findViewById(R.id.model_list);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle bundle=getArguments();
        if(bundle!=null){
            makerID=bundle.getString(Constant.KEY_MAKER_ID);
            makerName=bundle.getString(Constant.KEY_MAKER_NAME);
        }

        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        mPresenter = new CarModelPresenter(this, mMainLayout);

        getCarModels();

        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Models");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
           // onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void getCarModels(){
        mPresenter.getCarModels(makerID);
    }

    public void onSuccess(final CarModelsResponse carModelsResponse){

        automakerAdapter=new AutomakerAdapter(getActivity(),carModelsResponse.getModels());
        recList.setAdapter(automakerAdapter);

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle=new Bundle();
                        bundle.putString(Constant.KEY_MAKER_ID,makerID);
                        bundle.putString(Constant.KEY_MAKER_NAME,makerName);
                        bundle.putString(Constant.KEY_MODEL_ID,carModelsResponse.getModels().get(position).getId());
                        bundle.putString(Constant.KEY_MODEL_NAME,carModelsResponse.getModels().get(position).getValue());

                        ((HomeScreen) getActivity()).addFragment(new SelectVariantActivity(), "SelectVariantActivity", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                      //  finish();
                    }

                }));

    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SELECT_CAR) {
            Intent intent1=new Intent();
            intent1.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SELECT_CAR1);

            Events.SendEvent sendEvent1 =
                    new Events.SendEvent(intent1);
            GlobalBus.getBus().post(sendEvent1);getActivity().onBackPressed();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
