package autroid.business.view.fragment.jobcard;


import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

import autroid.business.storage.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.jobcard.JobsListAdapter;
import autroid.business.camera.CameraFragment;
import autroid.business.interfaces.JobCardCallback;
import autroid.business.model.response.JobsResponse;
import autroid.business.presenter.jobcard.JobsListPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.EndlessScrollListener;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import io.realm.Realm;

import static autroid.business.utils.Utility.REQUEST_CAMERA_IMAGE;
import static autroid.business.utils.Utility.cameraPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobsListFragment extends Fragment implements JobCardCallback {

    private EndlessScrollListener mScrollListener = null;

    private Realm realm;

    private RealmController realmController;

    private String status = "";

    @BindView(R.id.jobs_list)
    RecyclerView recList;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.main_layout)
    ConstraintLayout mMainLayout;

    JobsListPresenter mPresenter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fab_add)
    FloatingActionButton fab;

    @BindView(R.id.progressBar1)
    ProgressBar mProgressBar;

    private Boolean isTechnician=false;



    public JobsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_jobs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        getBundleData();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        mPresenter = new JobsListPresenter(this, mMainLayout);

        if(PreferenceManager.getInstance().getStringPreference(getActivity(),Constant.SP_ROLE).equalsIgnoreCase("Technician")) {
            isTechnician = Boolean.TRUE;
        }


        mTitle.setText(status);
        fab.hide();

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());
        realmController.deleteJobs(status);

        getList(0, status);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            realmController.deleteJobs(status);
            getList(0, status);
        });

        mScrollListener = new EndlessScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mProgressBar.setVisibility(View.VISIBLE);
                getList(page, status);
            }
        };

        recList.addOnScrollListener(mScrollListener);

        fab.setOnClickListener(v -> checkCameraPermission());

    }

    private void getList(int page, String status) {
        mPresenter.getJobs(status, page);
    }

    public void onSuccessList(JobsResponse jobsResponse, int page) {

        mSwipeRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);

        mTitle.setText(status + " (" + jobsResponse.getTotalResponse().getTotalResult() + ")");

        if (status.equals(Constant.NEW_JOB)) fab.show();


        if (page == 0) {
            JobsListAdapter jobsListAdapter = new JobsListAdapter(realmController.getJobs(status), true, this,isTechnician);
            recList.setAdapter(jobsListAdapter);
        }
    }

    @Override
    public void onJobClick(String id) {
        ((HomeScreen) getActivity()).makeDrawerVisible();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_ID, id);
        bundle.putBoolean(Constant.KEY_MY_BOOKING, false);
        ((HomeScreen) getActivity()).addFragment(new JobCardDetailFragment(), "JobCardDetailFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }

    @Override
    public void onChatClick(String id) {
        Utility.whatsAppChat(id, getActivity());
    }

    @Override
    public void onCallClick(String contact) {
        Utility.onCallClick(contact, getActivity());
    }

    @Override
    public void onCameraClick(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.BOOKING_ID, id);
        bundle.putString(Constant.USER_ID, "");
        bundle.putBoolean(Constant.IS_ADDITIONAL_PHOTOS, true);
        ((HomeScreen) getActivity()).addFragment(new CameraFragment(), "CameraFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }


    private void getBundleData() {
        status = getArguments().getString(Constant.KEY_TYPE);
    }

    private void checkCameraPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> denied = new ArrayList<>();

            for (String perm : cameraPermissions) {
                if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) denied.add(perm);
            }

            if (denied.size() != 0) {
                requestPermissions(denied.toArray(new String[denied.size()]), REQUEST_CAMERA_IMAGE);
                return;
            } else startProcess();

        } else startProcess();

    }

    private void startProcess() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constant.IS_MANUAL, false);
        ((HomeScreen) getActivity()).addFragment(new JobCardUserFragment(), "JobCardUserFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case REQUEST_CAMERA_IMAGE :
                for (int value : grantResults) if (value == -1) return;
                startProcess();
            break;
        }
    }

}
