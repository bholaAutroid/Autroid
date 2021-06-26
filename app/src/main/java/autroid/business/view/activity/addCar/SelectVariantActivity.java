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
import autroid.business.model.response.CarVariantResponse;
import autroid.business.presenter.CarModelPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectVariantActivity extends Fragment {

    RecyclerView recList;
    String modelID,modelName;
    String makerID,makerName;
    AutomakerAdapter automakerAdapter;
    LinearLayout mMainLayout;
    CarModelPresenter mPresenter;

    public SelectVariantActivity() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_select_variant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recList= (RecyclerView) view.findViewById(R.id.variant_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        Bundle bundle=getArguments();
        if(bundle!=null){
            makerID=bundle.getString(Constant.KEY_MAKER_ID);
            makerName=bundle.getString(Constant.KEY_MAKER_NAME);
            modelID=bundle.getString(Constant.KEY_MODEL_ID);
            modelName=bundle.getString(Constant.KEY_MODEL_NAME);
        }

        mMainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        mPresenter = new CarModelPresenter(this, mMainLayout);

        getCarModels();
        TextView tvTitle= (TextView) view.findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Variants");


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
        mPresenter.getCarVariant(modelID);
    }

    public void onSuccess(final CarVariantResponse carModelsResponse){

        automakerAdapter=new AutomakerAdapter(getActivity(),carModelsResponse.getVaiants());
        recList.setAdapter(automakerAdapter);

        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        getActivity().onBackPressed();
                        Intent broadcastIntent = new Intent();
                        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SELECT_CAR);
                        broadcastIntent.putExtra(Constant.KEY_VARIANT_ID, carModelsResponse.getVaiants().get(position).getId());
                        broadcastIntent.putExtra(Constant.KEY_VARIANT_NAME, carModelsResponse.getVaiants().get(position).getValue());
                        broadcastIntent.putExtra(Constant.KEY_MODEL_ID, modelID);
                        broadcastIntent.putExtra(Constant.KEY_MODEL_NAME, modelName);
                        broadcastIntent.putExtra(Constant.KEY_MAKER_ID, makerID);
                        broadcastIntent.putExtra(Constant.KEY_MAKER_NAME,makerName);
                        Events.SendEvent sendEvent =
                                new Events.SendEvent(broadcastIntent);
                        GlobalBus.getBus().post(sendEvent);
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
    public void getEvent(Events.SendEvent fragmentActivityMessage) {

    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }
}
