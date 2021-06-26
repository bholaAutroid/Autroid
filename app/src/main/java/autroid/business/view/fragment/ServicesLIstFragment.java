package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import autroid.business.R;
import autroid.business.adapter.ServicesListAdapter;
import autroid.business.interfaces.AddServicePriceCallback;
import autroid.business.model.request.AddServiceRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.VendorServicesResponse;
import autroid.business.presenter.ServicesPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesLIstFragment extends Fragment implements AddServicePriceCallback {

    RecyclerView mList;
    String serviceId;
    ServicesListAdapter mServiceListAdapter;

    RelativeLayout mMainLayout;
    ServicesPresenter mPresenter;
    public ServicesLIstFragment() {
        // Required empty public constructor
    }

    public static ServicesLIstFragment newInstance() {
        ServicesLIstFragment fragment = new ServicesLIstFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services_list, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList= (RecyclerView) view.findViewById(R.id.sub_services_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);

        mMainLayout= (RelativeLayout) view.findViewById(R.id.main_layout);
        mPresenter=new ServicesPresenter(this,mMainLayout);


        Bundle bundle=getArguments();

        if(bundle!=null){
            serviceId=bundle.getString(Constant.KEY_ID);
        }

        getAllServices();

    }

    private void getAllServices(){}/*{
        mPresenter.getAllServices(serviceId);*/


    public void onSuccess(VendorServicesResponse vendorServicesResponse){


    }

    public void onAddSuccess(BaseResponse baseResponse){
        Utility.showResponseMessage(mMainLayout,baseResponse.getResponseMessage());
    }

    @Override
    public void addServicePrice(String serviceId, String price) {
        AddServiceRequest addServiceRequest=new AddServiceRequest();
        addServiceRequest.setPrice(price);
        addServiceRequest.setService_id(serviceId);
        mPresenter.addService(addServiceRequest);
    }
}
