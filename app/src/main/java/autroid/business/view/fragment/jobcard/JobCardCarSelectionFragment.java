package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.jobcard.SelectCarAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.presenter.CarSelectionPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.fragment.search.SearchCarFragment;

public class JobCardCarSelectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private SelectCarAdapter selectCarAdapter;
    private ArrayList<CarDetailBE> arrayList;
    private Button carSelected,new_car;
    private TextView textView;
    private boolean isManual,isLeadInsurance;
    private RelativeLayout mainLayout;
    private CarSelectionPresenter presenter;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_card_car_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        textView=view.findViewById(R.id.toolbar_title);
        textView.setText("Select Car");
        mainLayout=view.findViewById(R.id.main_layout);

        presenter=new CarSelectionPresenter(this,mainLayout);

        Utility.hideSoftKeyboard(getActivity());

        if(getArguments()!=null && getArguments().getBoolean(Constant.IS_MANUAL))isManual=true;

        if (getArguments()!=null && getArguments().getBoolean(Constant.VALUE)){
            userId=getArguments().getString(Constant.KEY_ID);
            presenter.getCarList(userId);
            isLeadInsurance=true;
            arrayList=new ArrayList<>();
        }else {
            arrayList = (ArrayList<CarDetailBE>) getArguments().getSerializable("car_details");
        }

        selectCarAdapter=new SelectCarAdapter(arrayList,getActivity());

        carSelected=view.findViewById(R.id.car_selected);
        recyclerView=view.findViewById(R.id.cars_list);
        new_car=view.findViewById(R.id.add_new_car);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(selectCarAdapter);

        carSelected.setOnClickListener(v->{
            if(selectCarAdapter.carId!=null){
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra("car_id",selectCarAdapter.carId);
                broadcastIntent.putExtra("variant_id",selectCarAdapter.variantId);
                broadcastIntent.putExtra("variant_name",selectCarAdapter.carName);
                broadcastIntent.putExtra("reg_no",selectCarAdapter.regNo);
                broadcastIntent.putExtra("vin_no",selectCarAdapter.vinNo);
                broadcastIntent.putExtra("eng_no",selectCarAdapter.engNo);
                broadcastIntent.putExtra("policy_holder",selectCarAdapter.policyHolder);
                broadcastIntent.putExtra("company",selectCarAdapter.company);
                broadcastIntent.putExtra("policy_no",selectCarAdapter.policy);
                broadcastIntent.putExtra("expire",selectCarAdapter.expire);
                broadcastIntent.putExtra("premium",Integer.toString(selectCarAdapter.premium));

                if(isManual){
                    broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SEND_MANUAL_CAR);
                    Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                    GlobalBus.getBus().post(sendEvent);
                    getActivity().onBackPressed();
                } else if(isLeadInsurance){
                    broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SEND_CAR_INSURANCE);
                    Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                    GlobalBus.getBus().post(sendEvent);
                }
                else{
                    broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SEND_CAR);
                    Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                    GlobalBus.getBus().post(sendEvent);
                    getActivity().onBackPressed();
                }
            }
        });

        new_car.setOnClickListener(v->{

            Bundle bundle = new Bundle();

            if(isLeadInsurance)bundle.putBoolean(Constant.VALUE,true);
            else {
                if(isManual)bundle.putBoolean(Constant.IS_MANUAL, true);
                else bundle.putBoolean(Constant.CAR_DETAILS, true);
            }

            ((HomeScreen)getActivity()).addFragment(new SearchCarFragment(),"SearchFragment",true,false,bundle,((HomeScreen)getActivity()).currentFrameId);
        });

    }

    public void onListSuccess(GetUserCarResponse response) {
        arrayList.addAll(response.getGetCarDetails());
        selectCarAdapter.notifyDataSetChanged();
    }
}
