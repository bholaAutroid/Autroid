//package careager.business.presenter.jobcard;
//
//import android.view.ViewGroup;
//
//import careager.business.api.ApiCallback;
//import careager.business.api.ApiFactory;
//import careager.business.api.ApiRequest;
//import careager.business.model.request.AddUserRequest;
//import careager.business.model.response.UserDataResponse;
//import careager.business.view.fragment.jobcard.JobCardDriverFragment;
//import careager.business.view.fragment.jobcard.JobCardUserFragment;
//import retrofit2.Response;
//
//public class JobCardDriverPresenter {
//
//    private JobCardDriverFragment mFragment;
//    private ViewGroup mMainLayout;
//
//    public JobCardDriverPresenter(JobCardDriverFragment frag, ViewGroup mainLayout) {
//        mFragment = frag;
//        mMainLayout = mainLayout;
//    }
//
//    public void verifyUser(String mobile,String type) {
//        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
//        ApiCallback.MyCall<UserDataResponse> myCall = apiRequest.verifyUser(mobile,"contact_no",type);
//
//        myCall.enqueue(new ApiCallback.MyCallback<UserDataResponse>() {
//            @Override
//            public void success(final Response<UserDataResponse> response) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (response.body().getResponseCode() == 200) {
//                            UserDataResponse baseResponse = response.body();
//                            mFragment.onSuccess(baseResponse);
//                        } else {
//                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
//                        }
//                    }
//                });
//            }
//            @Override
//            public void error(String errorMessage) {
//                mFragment.getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mFragment.onError();
//                    }
//                });
//            }
//        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
//    }
//
//
//}
