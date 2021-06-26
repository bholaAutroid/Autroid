package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.bean.JobsBE;
import autroid.business.model.realm.JobsRealm;
import autroid.business.model.response.JobsResponse;
import autroid.business.view.fragment.jobcard.JobsListFragment;
import io.realm.Realm;
import retrofit2.Response;

public class JobsListPresenter {

    private JobsListFragment mFragment;
    private ViewGroup mMainLayout;

    public JobsListPresenter(JobsListFragment jobCardCarFragment,ViewGroup mainLayout ){
        mFragment=jobCardCarFragment;
        mMainLayout=mainLayout;
    }

    public void getJobs(String status,final int page){
        ApiRequest apiRequest= ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<JobsResponse> myCall=apiRequest.jobsList(status,page);
        myCall.enqueue(new ApiCallback.MyCallback<JobsResponse>() {
            @Override
            public void success(Response<JobsResponse> response) {

                Realm realm;
                realm = Realm.getDefaultInstance();
                for (JobsBE data : response.body().getGetJobs()) {
                    JobsRealm jobsRealm = new JobsRealm();
                    jobsRealm.setUserId(data.getId());
                    jobsRealm.setJobNo(data.getJob_no());
                    jobsRealm.setBookingNo(data.getBooking_no());
                    jobsRealm.setJobOpenDate(data.getCreated_at());
                    jobsRealm.setUpdatedAt(data.getUpdated_at());
                    jobsRealm.setTimeLeft(data.getTime_left());
                    jobsRealm.setDeliveryDate(data.getDelivery_date());
                    jobsRealm.setId(data.getId());
                    jobsRealm.setTitle(data.getCar().getTitle());
                    jobsRealm.setRegistrationNo(data.getCar().getRegistration_no());
                    jobsRealm.setPrimaryStatus(status); //stage
                    jobsRealm.setSecondaryStatus(data.getStatus()); //status
                    jobsRealm.setUserName(data.getUser().getName());
                    jobsRealm.setUserMobile(data.getUser().getContact_no());

                    realm.beginTransaction();
                    realm.copyToRealm(jobsRealm);
                    realm.commitTransaction();
                }

                realm.close();

                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessList(response.body(),page);
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },mFragment.getActivity(),mMainLayout,Boolean.FALSE);
    }
}
