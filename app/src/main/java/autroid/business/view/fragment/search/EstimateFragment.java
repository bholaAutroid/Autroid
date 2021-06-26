package autroid.business.view.fragment.search;


import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.estimate.EstimateCategoryAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.BookingCategoryCallback;
import autroid.business.model.realm.BookingCategoryRealm;
import autroid.business.model.response.BookingCategoryResponse;
import autroid.business.model.response.CarItemsResponse;
import autroid.business.presenter.EstimatePresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.activity.addCar.SelectAutomakerActivity;
import autroid.business.view.fragment.BookingPackagesFragment;
import autroid.business.view.fragment.BookingServicePackagesFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */

public class EstimateFragment extends Fragment  implements BookingCategoryCallback {

    RecyclerView recList;
    ConstraintLayout mMainLayout;
    TextView mCar;

    CarItemsResponse carItemsResponse;
    String modelID,modelName;
    String makerID,makerName;
    String variantID,variantName;

    EstimatePresenter mEstimatePresenter;

    private Realm realm;
    RealmController realmController;
    private Dialog dialog;


    public EstimateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_estimate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GlobalBus.getBus().register(this);

        this.realm = RealmController.with(getActivity()).getRealm();
        Application appCtx = (MyApplication) getActivity().getApplication();
        realmController=new RealmController(appCtx);
        realmController.clearBookingCategory();

        recList= (RecyclerView) view.findViewById(R.id.estimate_list);
        mMainLayout= view.findViewById(R.id.main_layout);
        mCar=view.findViewById(R.id.car);

        variantID=getArguments().getString(Constant.KEY_VARIANT_ID);
        mCar.setText(getArguments().getString(Constant.KEY_VARIANT_NAME));

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mEstimatePresenter=new EstimatePresenter(this,mMainLayout);
        mEstimatePresenter.bookingCategory(variantID);

       // setDetails();
    }

    private void setDetails() {

        Gson gson = new Gson();
        final PreferenceManager preferenceManager = PreferenceManager.getInstance();
        String carItems = preferenceManager.getStringPreference(getContext(), Constant.SP_CAR_ITEMS);
        carItemsResponse = gson.fromJson(carItems,
                CarItemsResponse.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_AUTOMAKER, carItemsResponse.getGetCarItems().getAutomaker());
        ((HomeScreen) getActivity()).makeDrawerVisible();
        ((HomeScreen) getActivity()).addFragment(new SelectAutomakerActivity(), "SelectAutomakerActivity", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // unregister the registered event.
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SELECT_CAR) {
            makerID=intent.getStringExtra(Constant.KEY_MAKER_ID);
            makerName=intent.getStringExtra(Constant.KEY_MAKER_NAME);
            modelID=intent.getStringExtra(Constant.KEY_MODEL_ID);
            modelName=intent.getStringExtra(Constant.KEY_MODEL_NAME);
            variantID=intent.getStringExtra(Constant.KEY_VARIANT_ID);
            variantName=intent.getStringExtra(Constant.KEY_VARIANT_NAME);

            //mEstimatePresenter.bookingCategory();

            //mAutomakerTitle.setText(makerName+", "+modelName+", "+variantName);
        }

    }

    public void onSuccess(BookingCategoryResponse bookingCategoryResponse) {
        for(int i=0;i<bookingCategoryResponse.getBookingCategoryBES().size();i++){
            BookingCategoryRealm bookingCategoryRealm=new BookingCategoryRealm();
            bookingCategoryRealm.setTitle(bookingCategoryResponse.getBookingCategoryBES().get(i).getTitle());
            bookingCategoryRealm.setTag(bookingCategoryResponse.getBookingCategoryBES().get(i).getTag());
            bookingCategoryRealm.setIcon(bookingCategoryResponse.getBookingCategoryBES().get(i).getIcon());
            bookingCategoryRealm.setNested(bookingCategoryResponse.getBookingCategoryBES().get(i).getNested());
            bookingCategoryRealm.setEnable(bookingCategoryResponse.getBookingCategoryBES().get(i).getEnable());
            bookingCategoryRealm.setSelected(Boolean.FALSE);

            realm.beginTransaction();
            realm.copyToRealm(bookingCategoryRealm);
            realm.commitTransaction();
        }
        EstimateCategoryAdapter mBookingCategoryAdapter = new EstimateCategoryAdapter(realmController.getBookingCategory(), true, getActivity(), this);
        recList.setAdapter(mBookingCategoryAdapter);
    }

    @Override
    public void onCategoryClick(String name, String tag,Boolean nested) {

        if(!nested){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,name);


            ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else {
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,name);
            ((HomeScreen) getActivity()).addFragment(new BookingServicePackagesFragment(), "BookingServicePackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
       /* if(tag.equalsIgnoreCase(Constant.bookingCategory[0])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");

            ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[1])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[2])){
            showDialog(name,tag);
            // showDialog();
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[4])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[3])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingServicePackagesFragment(), "BookingServicePackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[7])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingServicePackagesFragment(), "BookingServicePackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[6])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingServicePackagesFragment(), "BookingServicePackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else if(tag.equalsIgnoreCase(Constant.bookingCategory[5])){
            Bundle bundle=new Bundle();
            bundle.putString(Constant.KEY_ID,variantID);
            bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
            bundle.putString(Constant.KEY_TYPE,"");
            ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
        else {

        }*/
    }

    @Override
    public void onDisableCategoryClick(String name, String tag, Boolean nested) {

    }

    @Override
    public void onServicesSelected(String id, String name, String tag, String type, Float cost, Float mrp, int quantity, String unit, Boolean isDoorstep) {

    }

    @Override
    public void onServicesUnselect(String id) {

    }

    @Override
    public void updateQuantity(String id, int quantity) {

    }

    @Override
    public void onGalleryClick(String id, String type) {

    }

    @Override
    public void onRightSwipe() {

    }

    public void showDialog(final String name, final String tag){
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_collision_repair);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout mCross=dialog.findViewById(R.id.ll_cross);
        Button mMetallic =  dialog.findViewById(R.id.body_metallic);
        Button mAluminium = dialog.findViewById(R.id.body_aluminium);

        mMetallic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(Constant.KEY_ID,variantID);
                bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
                bundle.putString(Constant.KEY_TYPE,"general");
                ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                dialog.dismiss();
            }
        });
        mAluminium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(Constant.KEY_ID,variantID);
                bundle.putString(Constant.KEY_CATEGORY_NAME,tag);
                bundle.putString(Constant.KEY_TYPE,"aluminium");
                ((HomeScreen) getActivity()).addFragment(new BookingPackagesFragment(), "BookingPackagesFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                dialog.dismiss();
            }
        });

        mCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
