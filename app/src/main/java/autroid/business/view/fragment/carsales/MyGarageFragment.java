package autroid.business.view.fragment.carsales;

import autroid.business.R;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import autroid.business.adapter.cars.MyGarageAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.interfaces.BookServiceClickCallback;
import autroid.business.interfaces.OnCarClickCallBack;
import autroid.business.model.bean.CarDetailBE;
import autroid.business.model.bean.ThumbnailBE;
import autroid.business.model.realm.CarStockRealm;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.request.AddCarRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.CarListResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.presenter.carsales.CarStockPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;
import io.realm.RealmList;

public class MyGarageFragment extends Fragment implements BookServiceClickCallback, OnCarClickCallBack {

    RecyclerView recList;

    ConstraintLayout mMainLayout;

    CarStockPresenter mPresenter;

    MyGarageAdapter carStockAdapter;

    FloatingActionButton mAddButton;

    TextView mTotal;

    ImageView backNavigation;

    String currentDeletionId = "";

    Realm realm;

    RealmController realmController;

    SwipeRefreshLayout mSwipeRefreshLayout;

    EndlessScrollListener mScrollListener = null;

    LinearLayoutManager llm;

    public MyGarageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_garage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());
        realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);

        llm = new LinearLayoutManager(getActivity());
        mPresenter = new CarStockPresenter(this, mMainLayout);

        mMainLayout = view.findViewById(R.id.main_layout);
        recList = view.findViewById(R.id.car_stock_list);
        recList.setLayoutManager(llm);
        mAddButton = view.findViewById(R.id.fab_add);
        mAddButton.hide();
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        backNavigation = view.findViewById(R.id.back_navigation);
        mTotal = view.findViewById(R.id.car_total);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            // Refresh items
            realmController.clearCarStock(Constant.STOCK_TYPE_GARAGE);
            getCarStock(0);
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                getCarStock(page);
            }
        };

        recList.addOnScrollListener(mScrollListener);

        getCarStock(0);

        mAddButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_CAR_TYPE, Constant.CAR_TYPES[0]);
            ((AwsHomeActivity) getActivity()).makeDrawerVisible();
            ((AwsHomeActivity) getActivity()).addFragment(new AddCarFragment(), "EditCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
        });

        backNavigation.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void getCarStock(int page) {
        mPresenter.getCarStock(page);
    }

    public void onSuccessCars(CarListResponse carStockResponse, int page) {

        mSwipeRefreshLayout.setRefreshing(false);

        mTotal.setText("All Cars (" + carStockResponse.getGetCarStock().getTotal() + ")");

        mAddButton.show();

        if (carStockResponse.getGetCarStock().getStocks().size() == 0 && page == 0) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_CAR_TYPE, Constant.CAR_TYPES[0]);
            ((AwsHomeActivity) getActivity()).makeDrawerVisible();
            ((AwsHomeActivity) getActivity()).addFragment(new AddCarFragment(), "EditCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);

            Toast.makeText(getActivity(), "Let's add your first car...", Toast.LENGTH_LONG).show();

        } else {

            for (CarDetailBE data : carStockResponse.getGetCarStock().getStocks()) {

                CarStockRealm carRealm = new CarStockRealm();
                carRealm.setId(data.getId());
                carRealm.setCreatedDate(data.getCreated_at());
                carRealm.setFuelType(data.getFuel_type());
                carRealm.setYear(data.getManufacture_year());
                carRealm.setColor(data.getVehicle_color());
                carRealm.setPrice(data.getPrice());
                carRealm.setRegistrationNo(data.getRegistration_no());
                carRealm.setModelName(data.getModelName());
                carRealm.setVariant(data.getVariant());
                carRealm.setStatus(data.getVehicle_status());
                carRealm.setPublish(data.getPublish());
                carRealm.setAdminApproved(data.getAdmin_approved());
                carRealm.setTitle(data.getTitle());
                carRealm.setCarId(data.getCarId()); //It is basically carNo like bookingNo

                carRealm.setInternalType(Constant.STOCK_TYPE_GARAGE);

                if (data.getThumbnails().size() > 0) {

                    RealmList<MediaRealm> realmListMedia = new RealmList<>();

                    for (ThumbnailBE thumbnail : data.getThumbnails()) {

                        MediaRealm mediaRealm = new MediaRealm();
                        mediaRealm.setId(thumbnail.getId());
                        mediaRealm.setPath(thumbnail.getFile_address());
                        realmListMedia.add(mediaRealm);
                    }

                    carRealm.setMedia(realmListMedia);
                }

                realm.beginTransaction();
                realm.copyToRealm(carRealm);
                realm.commitTransaction();
            }
        }

        if (page == 0) {
            carStockAdapter = new MyGarageAdapter(realmController.getCarStock(Constant.STOCK_TYPE_GARAGE), true, this, getActivity(), this);
            recList.setAdapter(carStockAdapter);
        }
    }

    public void onSuccessDelete(BaseResponse response) {
        Utility.showResponseMessage(mMainLayout, response.getResponseMessage());
        realmController.removeCar(currentDeletionId);
    }

    public void unPublishResponse(PublishResponse baseResponse, String id) {
        Utility.showResponseMessage(mMainLayout, baseResponse.getResponseMessage());
        realmController.updateCarsStatus(id, false, false);
    }

    public void unPublishAlert(String id) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation Message")
                .setMessage("Do you want to remove this car from sale listing ?")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    PublishUnpublishRequest publishUnpublishRequest = new PublishUnpublishRequest();
                    publishUnpublishRequest.setCar(id);
                    mPresenter.UnPublishCar(publishUnpublishRequest);
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void carId(String carId, int pos, String modelName) {

        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_CAR_ID, carId);
        bundle.putString(Constant.KEY_MODEL_NAME, modelName);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new BookingCategoryFragment(), "BookingCategoryFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }


    @Override
    public void onImageClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putString(Constant.KEY_TYPE, Constant.STOCK_TYPE_GARAGE);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new UsedCarDetailFragment(), "UsedCarDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onOffClick(String id, Boolean isPublish) {
        if (!isPublish) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_CAR_ID, id);
            bundle.putString(Constant.KEY_TYPE, Constant.PUBLISH_CAR);
            ((AwsHomeActivity) getActivity()).makeDrawerVisible();
            ((AwsHomeActivity) getActivity()).addFragment(new EditCarFragment(), "EditCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
            Toast.makeText(getActivity(), "Verify details and list the car", Toast.LENGTH_LONG).show();
        } else {
            unPublishAlert(id);
        }
    }

    @Override
    public void onEditButtonClick(int position, String carId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_CAR_ID, carId);
        bundle.putString(Constant.KEY_TYPE, Constant.EDIT_CAR);
        bundle.putBoolean(Constant.IS_GARAGE_CAR, true);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new EditCarFragment(), "EditCarFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onShareButtonClick(int position, String imagePath, String content, boolean publish) {

        if (publish) {
            if (imagePath.trim().length() != 0)
                Utility.shareImage(imagePath, getActivity(), content);
            else Utility.share(getActivity(), content);
        } else Utility.showResponseMessage(mMainLayout, "This car is not listed for sale");

    }

    @Override
    public void onTitleClick(int position, String postedBy, String carId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, carId);
        bundle.putString(Constant.KEY_TYPE, Constant.STOCK_TYPE_GARAGE);
        ((AwsHomeActivity) getActivity()).makeDrawerVisible();
        ((AwsHomeActivity) getActivity()).addFragment(new UsedCarDetailFragment(), "UsedCarDetailFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    @Override
    public void onDeleteClick(int position, String carId) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation Message")
                .setMessage("Do you want to delete the car ?")
                .setPositiveButton("Delete", (dialogInterface, which) -> {
                    currentDeletionId = carId;
                    AddCarRequest addCarRequest = new AddCarRequest();
                    addCarRequest.setCar(carId);
                    mPresenter.deleteCar(addCarRequest);
                })
                .setNegativeButton("Cancel", (dialogInterface, which) -> {
                    dialogInterface.dismiss();
                }).show();
    }

    @Override
    public void onLikeClick(int position, String carId) {

    }

    @Override
    public void onNavigateClick(int position, String carId) {
    }
}
