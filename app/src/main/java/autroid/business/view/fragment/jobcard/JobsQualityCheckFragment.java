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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.jobcard.QualityCheckListAdapter;
import autroid.business.camera.CameraFragment;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.JobsQCBE;
import autroid.business.model.request.PointsRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.JobsQCResponse;
import autroid.business.presenter.JobsQualityCheckPresenter;
import autroid.business.realm.RealmController;
import autroid.business.storage.PreferenceManager;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsQualityCheckFragment extends Fragment {

    JobsQualityCheckPresenter mPresenter;

    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;

    @BindView(R.id.points_list)
    RecyclerView mList;

    @BindView(R.id.re_work)
    Button reWork;

    @BindView(R.id.approval)
    Button approval;

    String bookingId;

    ArrayList<JobsQCBE> arrayList;

    boolean isApproval;

    private Realm realm;

    private RealmController realmController;


    public JobsQualityCheckFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quality_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        GlobalBus.getBus().register(this);

        arrayList = new ArrayList<>();

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());

        getBundleData();

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setNestedScrollingEnabled(true);

        reWork.setOnClickListener(v -> {
            mPresenter.setPoints(createReworkPointsRequest());
        });

        approval.setOnClickListener(v -> {
            if(!isApproval && validate()){
                Bundle bundle = new Bundle();
                bundle.putString(Constant.BOOKING_ID, bookingId);
                bundle.putString(Constant.USER_ID, "");
                bundle.putBoolean(Constant.IS_QUALITY_CHECK, true);

                String jsonManifest= PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_MANIFEST);
                try {
                    JSONObject jsonObject = new JSONObject(jsonManifest);
                    int picLimit=jsonObject.getInt("qc_inspection_limit");
                    if(picLimit>0){
                        ((AwsHomeActivity) getActivity()).addFragment(new CameraFragment(), "CameraFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);

                    }
                    else {

                        mPresenter.setPoints(createApprovalPointsRequest());

                    }

                }catch (Exception e){

                }
            }else if(validate()){
                mPresenter.setPoints(createApprovalPointsRequest());
            }

        });
    }

    private PointsRequest createReworkPointsRequest() {
        PointsRequest pointsRequest = new PointsRequest();
        pointsRequest.setBooking(bookingId);
        pointsRequest.setStage(Constant.IN_PROCESS);
        pointsRequest.setStatus(Constant.REWORK);
        pointsRequest.setArrayList(arrayList);
        return pointsRequest;
    }

    private PointsRequest createApprovalPointsRequest() {
        PointsRequest pointsRequest = new PointsRequest();
        pointsRequest.setBooking(bookingId);
        pointsRequest.setStage(Constant.STORE_APPROVAL);
        pointsRequest.setStatus(Constant.STORE_APPROVAL);
        pointsRequest.setArrayList(arrayList);
        return pointsRequest;
    }

    public void onSuccess(JobsQCResponse response) {
        arrayList = response.getGetJobsQC();
        QualityCheckListAdapter qualityCheckListAdapter = new QualityCheckListAdapter(arrayList,false);
        mList.setAdapter(qualityCheckListAdapter);
        reWork.setVisibility(View.VISIBLE);
        approval.setVisibility(View.VISIBLE);
    }

    public void onSuccessRework(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();

        if(isApproval)realmController.updateJobsStatus(bookingId, Constant.STORE_APPROVAL);
        else realmController.updateJobsStatus(bookingId, Constant.IN_PROCESS);

        Fragment qualityCheck = getFragmentManager().findFragmentByTag("QualityCheckFragment");
        if (qualityCheck!=null)qualityCheck.getFragmentManager().beginTransaction().remove(qualityCheck).commit();

        Fragment jobCardDetailFragment = getFragmentManager().findFragmentByTag("JobCardDetailFragment");
        if (jobCardDetailFragment!=null)jobCardDetailFragment.getFragmentManager().beginTransaction().remove(jobCardDetailFragment).commit();

    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_APPROVAL) {
            isApproval=true;
            mPresenter.setPoints(createApprovalPointsRequest());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    private void getBundleData(){
        if(!getArguments().getBoolean(Constant.IS_JOBCARD_DETAILS)){
            bookingId = getArguments().getString(Constant.BOOKING_ID);
            mPresenter = new JobsQualityCheckPresenter(this, mMainLayout);
            mPresenter.getQC(bookingId);
        }else {
            arrayList= (ArrayList<JobsQCBE>) getArguments().getSerializable(Constant.QC_LIST);
            QualityCheckListAdapter qualityCheckListAdapter = new QualityCheckListAdapter(arrayList,true);
            mList.setAdapter(qualityCheckListAdapter);
            reWork.setVisibility(View.GONE);
            approval.setVisibility(View.GONE);
        }
    }

    private boolean validate() {

        for (JobsQCBE data:arrayList)if(!data.getStatus()){
            Utility.showResponseMessage(mMainLayout,"Check points not checked");
            return false;
        }

        return true;
    }
}
