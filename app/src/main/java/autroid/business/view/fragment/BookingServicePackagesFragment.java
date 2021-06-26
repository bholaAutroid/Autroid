package autroid.business.view.fragment;

import android.app.Application;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.ServicesListAdapter;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.ServiceRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.VendorServicesResponse;
import autroid.business.presenter.ServicesPackagePresenter;
import autroid.business.realm.RealmController;

import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import io.realm.Realm;
import io.realm.RealmResults;

public class BookingServicePackagesFragment extends Fragment implements View.OnClickListener,OnRealmImageClickCallback {

    ServicesPackagePresenter mPresenter;
    LinearLayout mMainLayout;

    ServicesListAdapter mServiceListAdapter;
    String carId;

    @BindView(R.id.services_list)
    ExpandableListView mList;
    @BindView(R.id.service_name)
    TextView mServiceName;

    String tag,packageId;

    private Realm realm;
    RealmController realmController;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.activity_services_list, container, false);
    }





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);


        Bundle bundle=getArguments();
        if(bundle!=null){
            carId=bundle.getString(Constant.KEY_ID);
            tag=getArguments().getString(Constant.KEY_CATEGORY_NAME);
            mServiceName.setText(getArguments().getString(Constant.KEY_TYPE));


        }


      /*  Intent intent=getIntent();
        if(intent!=null){
            objAddBookingRequest= (AddBookingRequest) getIntent().getSerializableExtra("AddBookingRequest");
        }*/

        mMainLayout= view.findViewById(R.id.main_layout);
        mPresenter=new ServicesPackagePresenter(this,mMainLayout);

        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setCar(carId);
        serviceRequest.setType(tag);
        mPresenter.getAllServices(carId,tag);

    }

    public void onSuccess(VendorServicesResponse vendorServicesResponse){
        RealmResults<SelectedBookingDataRealm> selectedBookingDataRealms=realmController.getSelectedBookingData(tag);
        for(int j=0;j<selectedBookingDataRealms.size();j++) {
            for(int i=0;i<vendorServicesResponse.getGetServices().size();i++){
                for(int k=0;k<vendorServicesResponse.getGetServices().get(i).getServices().size();k++){
                    if(vendorServicesResponse.getGetServices().get(i).getServices().get(k).getId().equals(selectedBookingDataRealms.get(j).getId())){
                        vendorServicesResponse.getGetServices().get(i).getServices().get(k).setChecked(true);
                    }
                }
            }
        }

        mServiceListAdapter=new ServicesListAdapter(getActivity(),vendorServicesResponse.getGetServices(),this);
        mList.setAdapter(mServiceListAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){


        }
    }

    public void onServicesSuccess(BaseResponse loginPostResponse) {
        Utility.showResponseMessage(mMainLayout,loginPostResponse.getResponseMessage());
    }



    @Override
    public void onImageClick(int pos, String id) {

    }

    @Override
    public void onTraveloguePagerClick(int pos, String id) {

    }

    @Override
    public void onDetailClick(int pos, String id) {
        BookingPackageDetailFragment bookingDetailFragment=new BookingPackageDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_TYPE,id);
        bookingDetailFragment.setArguments(bundle);
        bookingDetailFragment.show(getChildFragmentManager(),"BookingPackageDetailFragment");

    }

    @Override
    public void onDetailClick(String id, String type) {

    }
}
