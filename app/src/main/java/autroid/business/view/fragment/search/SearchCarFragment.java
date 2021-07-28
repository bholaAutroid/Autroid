package autroid.business.view.fragment.search;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.SearchCarAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.SearchCarClickCallback;
import autroid.business.model.response.SearchCarResponse;
import autroid.business.presenter.SearchCarPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCarFragment extends Fragment implements SearchCarClickCallback {

    RecyclerView recList;

    @BindView(R.id.car)
    AutoCompleteTextView mCar;

    @BindView(R.id.main_layout)
    ConstraintLayout mMainLayout;

    SearchCarPresenter mPresenter;

    InputMethodManager inputMethodManager;

    public SearchCarFragment() {
        // Required empty public constructor
    }


    boolean isCarDetails=false,isManualDetails=false,isCarGarage=false,isLeadInsurance=false,isCarUpdate=false;
    Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_car, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        bundle=getArguments();
        if (bundle!=null && bundle.getBoolean(Constant.CAR_DETAILS)) isCarDetails=true;
        else if(bundle!=null && bundle.getBoolean(Constant.IS_MANUAL))isManualDetails=true;
        else if(bundle!=null && bundle.getBoolean(Constant.IS_GARAGE_CAR))isCarGarage=true;
        else if(bundle!=null && bundle.getBoolean(Constant.VALUE))isLeadInsurance=true;
        else if(bundle!=null && bundle.getBoolean(Constant.IS_CAR_UPDATE))isCarUpdate=true;

        recList= (RecyclerView) view.findViewById(R.id.variant_list);
        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mPresenter=new SearchCarPresenter(this,mMainLayout);

        mCar.setThreshold(1);  //to start working from first character

        mCar.requestFocus();
        Utility.showSoftKeyboard(getActivity(),mCar);

        mCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>=3){
                    mPresenter.searchCar(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onSuccess(SearchCarResponse searchCarResponse) {
        SearchCarAdapter searchCarAdapter=new SearchCarAdapter(getActivity(),searchCarResponse.getSearchCarBE(),this);
        recList.setAdapter(searchCarAdapter);
    }


    @Override
    public void onCarClick(String id, String car) {

        if(isCarDetails){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_POST_CAR);
            broadcastIntent.putExtra("variant_id",id);
            broadcastIntent.putExtra("variant_name",car);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            removeFragment();
            GlobalBus.getBus().post(sendEvent);
            getActivity().onBackPressed();//.getSupportFragmentManager().beginTransaction().remove(this).commit();
        }else if(isManualDetails){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_POST_MANUAL_CAR);
            broadcastIntent.putExtra("variant_id",id);
            broadcastIntent.putExtra("variant_name",car);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            removeFragment();
            GlobalBus.getBus().post(sendEvent);
            getActivity().onBackPressed();
        }else if(isCarGarage){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_ADD_CAR);
            broadcastIntent.putExtra("variantid",id);
            broadcastIntent.putExtra("variantname",car);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            //removeFragment();
            GlobalBus.getBus().post(sendEvent);
            getActivity().onBackPressed();
        }else if(isLeadInsurance){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SEND_INSURANCE_CAR);
            broadcastIntent.putExtra("variant_id",id);
            broadcastIntent.putExtra("variant_name",car);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            removeFragment();
            GlobalBus.getBus().post(sendEvent);

        }else if(isCarUpdate){
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_SEND_CAR_UPDATE);
            broadcastIntent.putExtra("variant_id",id);
            broadcastIntent.putExtra("variant_name",car);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            removeFragment();
            GlobalBus.getBus().post(sendEvent);
        } else{
            Bundle broadcastIntent = new Bundle();
            broadcastIntent.putString(Constant.KEY_VARIANT_ID, id);
            broadcastIntent.putString(Constant.KEY_VARIANT_NAME,car);
            ((AwsHomeActivity) getActivity()).addFragment(new EstimateFragment(), "EstimateFragment", true, false, broadcastIntent, ((AwsHomeActivity) getActivity()).currentFrameId);
        }
    }

    public void removeFragment(){
        FragmentManager fm=getFragmentManager();
        int curentIndex=fm.getBackStackEntryCount()-1;
        if(fm.getBackStackEntryAt(curentIndex-1).getName().equalsIgnoreCase("SelectionFragment")){
            fm.popBackStack();
        }
    }
}
