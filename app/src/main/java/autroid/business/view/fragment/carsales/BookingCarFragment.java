package autroid.business.view.fragment.carsales;


import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.cars.SelectCarBookingAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.response.CarListResponse;
import autroid.business.presenter.carsales.SelectCarBookingPresenter;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingCarFragment extends DialogFragment implements View.OnClickListener {


    SelectCarBookingAdapter mSelectCarBookingAdapter;

    @BindView(R.id.cars_list)
    RecyclerView mList;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;
    @BindView(R.id.car_selected)
    Button mCarSelected;

    @BindView(R.id.add_new_car)
    Button mNewCar;

    @BindView(R.id.default_garage)
    TextView mEmptyGarage;

    SelectCarBookingPresenter mPresenter;

    String strSelectionType;

    String vendorName,vendorId;
    private String packageId,packageName;

    public BookingCarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogOfferAnimation;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity80)));
        GlobalBus.getBus().register(this);
        return inflater.inflate(R.layout.fragment_select_car_booking, container, false);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();

       /* if (intent.getDoubleExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_BOOKING2) {
            getActivity().onBackPressed();

        }*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
      //  GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mCarSelected.setOnClickListener(this);
        mNewCar.setOnClickListener(this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mList.setLayoutManager(llm);
        mPresenter = new SelectCarBookingPresenter(this, mMainLayout);
        mPresenter.getCarStock();

        Bundle bundle = getArguments();
        if (bundle != null) {
            vendorId=getArguments().getString(Constant.KEY_VENDOR_ID);
            vendorName=getArguments().getString(Constant.Key_Name);
            packageId=bundle.getString(Constant.KEY_PACKAGE_ID);
            packageName=bundle.getString(Constant.KEY_PACKAGE_NAME);
        }

        TextView tvTitle = (TextView) view.findViewById(R.id.toolbar_title);
        tvTitle.setText(getString(R.string.select_car));

    }

    public void onSuccess(CarListResponse carStockResponse) {
        if(carStockResponse.getGetCarStock().getStocks().size()>0){

            mEmptyGarage.setVisibility(View.GONE);
            mCarSelected.setEnabled(Boolean.TRUE);
            mSelectCarBookingAdapter=new SelectCarBookingAdapter(carStockResponse.getGetCarStock().getStocks(),getActivity());
            mList.setAdapter(mSelectCarBookingAdapter);
        }
        else {
            mCarSelected.setEnabled(Boolean.FALSE);
            mEmptyGarage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.car_selected: {
                try {
                    if (null != mSelectCarBookingAdapter) {
                        if (null != mSelectCarBookingAdapter.carId) {
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.KEY_CAR_ID, mSelectCarBookingAdapter.carId);
                            bundle.putString(Constant.KEY_MODEL_NAME, mSelectCarBookingAdapter.carName);
                            bundle.putString(Constant.KEY_VENDOR_ID, vendorId);
                            bundle.putString(Constant.Key_Name, vendorName);
                            ((HomeScreen) getActivity()).clearStackLocal();
                            ((HomeScreen) getActivity()).addFragment(new BookingCategoryFragment(), "BookingCategoryFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                        }
                    }
                }catch (Exception e){

                }
                break;
            }
            case R.id.add_new_car:
                getDialog().dismiss();
                Bundle bundle=new Bundle();
                bundle.putString(Constant.KEY_CAR_TYPE,Constant.CAR_TYPES[1]);
                ((HomeScreen) getActivity()).makeDrawerVisible();
                ((HomeScreen) getActivity()).addFragment(new AddCarFragment(), "EditCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                break;
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        try{
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //Log.i(tag, "keyCode: " + keyCode);
                    if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                        getActivity().onBackPressed();
                        return true;
                    }
                    return false;
                }
            });
        }catch (Exception e){

        }
    }*/
}
