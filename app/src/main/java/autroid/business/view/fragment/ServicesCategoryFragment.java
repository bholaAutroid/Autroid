package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import autroid.business.R;
import autroid.business.adapter.ServiceAdapter;
import autroid.business.adapter.ServiceCategoryAdapter;
import autroid.business.interfaces.AddServicePriceCallback;
import autroid.business.interfaces.CategoryClickCallback;
import autroid.business.model.request.AddServiceRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.ServicesPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.FragmentTags;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesCategoryFragment extends Fragment implements AddServicePriceCallback,CategoryClickCallback {

    ExpandableListView lvServices;
    RelativeLayout mMainLayout;
    ServicesPresenter mPresenter;
    ServiceAdapter mServiceAdapter;

    ServiceCategoryAdapter mServiceCategoryAdapter;
    RecyclerView mList;

    private String categoryId[]={"1","2","3","4","5","6","7","8","9","10"};
    private String category[]={"General Maintenance","Scheduled Maintenance","Brakes","Diagnosis","Electrical & Lights","Engine","Exhaust & Emissions","Heating & Air Conditioning","Suspension & Steering","Body Repair"};
    private int categoryIcon[]={R.drawable.ic_general_maintenance,R.drawable.ic_scheduled_maintenance,R.drawable.ic_brakes,R.drawable.ic_diagnosis,R.drawable.ic_electrical_lights,R.drawable.ic_engine,R.drawable.ic_exhaust_emission,R.drawable.ic_heating_conditioning,R.drawable.ic_suspension_repairing,R.drawable.ic_body_repair};

    public ServicesCategoryFragment() {
        // Required empty public constructor
    }

    public static ServicesCategoryFragment newInstance() {
        ServicesCategoryFragment fragment = new ServicesCategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lvServices= (ExpandableListView) view.findViewById(R.id.expandable_list);

        mList= (RecyclerView) view.findViewById(R.id.services_list);
        GridLayoutManager mGridLayoutManager=new GridLayoutManager(getActivity(),2);
        mList.setLayoutManager(mGridLayoutManager);

        mServiceCategoryAdapter=new ServiceCategoryAdapter(getActivity(),category,categoryIcon,this);
        mList.setAdapter(mServiceCategoryAdapter);


       /* mMainLayout= (RelativeLayout) view.initializeViews(R.id.main_layout);
        mPresenter=new ServicesPresenter(this,mMainLayout);*/

        //getAllServices();
    }

   /* private void getAllServices(){
        mPresenter.getAllServices(categoryId);
    }

    public void onSuccessGetUser(VendorServicesResponse vendorServicesResponse){
        mServiceAdapter=new ServiceAdapter(getActivity(),vendorServicesResponse.getGetServices(),this);
        lvServices.setAdapter(mServiceAdapter);

    }*/

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

    @Override
    public void categoryPos(int pos) {
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_ID,categoryId[pos]);
        ((AwsHomeActivity) getActivity()).addFragment(ServicesLIstFragment.newInstance(), FragmentTags.FRAGMENT_SERVICES_LIST,true,false,bundle,R.id.layout_services);
    }
}
