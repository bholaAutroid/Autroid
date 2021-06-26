package autroid.business.view.fragment;


import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.BookingPackagesAdapter;
import autroid.business.interfaces.OnClickBusinessCallback;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.model.request.ServiceRequest;
import autroid.business.model.response.BookingServicesResponse;
import autroid.business.presenter.BookingPackagesPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingPackagesFragment extends Fragment implements View.OnClickListener,OnClickBusinessCallback {

    @BindView(R.id.pacakges_list)
    RecyclerView mList;
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.service_name)
    TextView mServiceName;

    BookingPackagesPresenter mPresenter;
    BookingPackagesAdapter mBookingPackagesAdapter;



    private Realm realm;
    RealmController realmController;

    String tag;
    String carId,vendorId,packageId="";

    public BookingPackagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);


        tag=getArguments().getString(Constant.KEY_CATEGORY_NAME);
        carId=getArguments().getString(Constant.KEY_ID);
        mServiceName.setText(getArguments().getString(Constant.KEY_TYPE));


        mPresenter=new BookingPackagesPresenter(this,mMainLayout);

        ServiceRequest serviceRequest=new ServiceRequest();
        serviceRequest.setCar(carId);
        serviceRequest.setType(tag);

        mPresenter.bookingPackages(carId,tag);

}

    public void onSuccess(BookingServicesResponse bookingServicesResponse) {

        RealmResults<SelectedBookingDataRealm> selectedBookingDataRealms=realmController.getSelectedBookingData(tag);
        for(int j=0;j<selectedBookingDataRealms.size();j++){
            for(int i=0;i<bookingServicesResponse.getServices().size();i++){
                if(bookingServicesResponse.getServices().get(i).getId().equals(selectedBookingDataRealms.get(j).getId())){
                        bookingServicesResponse.getServices().get(i).setChecked(true);                   }
                }
            }

        mBookingPackagesAdapter=new BookingPackagesAdapter(getContext(),bookingServicesResponse.getServices(),this);
        mList.setAdapter(mBookingPackagesAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

        }
    }


    @Override
    public void onBusinessClick(String id) {
        BookingPackageDetailFragment bookingDetailFragment=new BookingPackageDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constant.KEY_TYPE,id);
        bookingDetailFragment.setArguments(bundle);
        bookingDetailFragment.show(getChildFragmentManager(),"BookingPackageDetailFragment");
    }
}
