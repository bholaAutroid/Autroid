package autroid.business.presenter.jobcard;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.JobCardBookingRequest;
import autroid.business.model.response.GetCompanyResponse;
import autroid.business.model.response.GetUserBookingResponse;
import autroid.business.model.response.GetUserCarResponse;
import autroid.business.view.fragment.jobcard.JobCardCarFragment;
import retrofit2.Response;

public class JobCardCarPresenter {

    private JobCardCarFragment mFragment;
    private ViewGroup mMainLayout;

    public JobCardCarPresenter(JobCardCarFragment jobCardCarFragment,ViewGroup mainLayout ){
        mFragment=jobCardCarFragment;
        mMainLayout=mainLayout;
    }

    public void getCompanyName() {
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(),ApiRequest.class);
        ApiCallback.MyCall<GetCompanyResponse> myCall=apiRequest.getCompanyName();
        myCall.enqueue(new ApiCallback.MyCallback<GetCompanyResponse>() {
            @Override
            public void success(Response<GetCompanyResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessCompanyName(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {
              //  mFragment.onError(errorMessage);
            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }

    public void getUserCars(String userId){
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserCarResponse> myCall=apiRequest.getUserCar(userId);
        myCall.enqueue(new ApiCallback.MyCallback<GetUserCarResponse>() {
            @Override
            public void success(Response<GetUserCarResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200 && response.body().getGetCarDetails().size()!=0){
                        mFragment.onSuccessCarData(response.body());
                    }else if(response.body().getResponseCode()==200 && response.body().getGetCarDetails().size()==0){
                        mFragment.onSuccessEmptyCarData();
                    }
                });
            }

            @Override
            public void error(String errorMessage) {}
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }

    public void addBooking(JobCardBookingRequest jobCardBookingRequest){
       ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(),ApiRequest.class);
       ApiCallback.MyCall<GetUserBookingResponse> myCall=apiRequest.addBooking(jobCardBookingRequest);
       myCall.enqueue(new ApiCallback.MyCallback<GetUserBookingResponse>() {
           @Override
           public void success(Response<GetUserBookingResponse> response) {
               mFragment.getActivity().runOnUiThread(()->{
                   if(response.body().getResponseCode()==200){
                       mFragment.onSuccessAddBooking(response.body());
                   }
               });
           }

           @Override
           public void error(String errorMessage) {
            mFragment.onError(errorMessage);
           }
       },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }

    public void getBookingCar(String bookingId){
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserCarResponse> myCall=apiRequest.getBookingCar(bookingId);
        myCall.enqueue(new ApiCallback.MyCallback<GetUserCarResponse>() {
            @Override
            public void success(Response<GetUserCarResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessBookingCar(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);
    }

    public void addBookingJob(JobCardBookingRequest jobCardBookingRequest){
        ApiRequest apiRequest=ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<GetUserBookingResponse> myCall=apiRequest.getExistingBookingResponse(jobCardBookingRequest);
        myCall.enqueue(new ApiCallback.MyCallback<GetUserBookingResponse>() {
            @Override
            public void success(Response<GetUserBookingResponse> response) {
                mFragment.getActivity().runOnUiThread(()->{
                    if(response.body().getResponseCode()==200){
                        mFragment.onSuccessBookingJob(response.body());
                    }
                });
            }

            @Override
            public void error(String errorMessage) {

            }
        },mFragment.getActivity(),mMainLayout,Boolean.TRUE);

    }
}
