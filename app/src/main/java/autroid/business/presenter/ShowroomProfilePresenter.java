package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.SaveBusinessRequest;
import autroid.business.model.response.BusinessProfileResponse;
import autroid.business.model.response.SaveBusinessResponse;
import autroid.business.model.response.ShowroomCarsResponse;
import autroid.business.model.response.ShowroomOfferResponse;
import autroid.business.model.response.ShowroomProductResponse;
import autroid.business.model.response.ShowroomReviewResponse;
import autroid.business.view.fragment.profile.ShowroomFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/13/17.
 */

public class ShowroomProfilePresenter {

    private ShowroomFragment mFragment;
    private ViewGroup mMainLayout;

    public ShowroomProfilePresenter(ShowroomFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getShowroom(String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<BusinessProfileResponse> myCall = apiRequest.getShowroomProfile(id);
        myCall.enqueue(new ApiCallback.MyCallback<BusinessProfileResponse>() {
            @Override
            public void success(final Response<BusinessProfileResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BusinessProfileResponse showroomProfileResponse = response.body();
                            mFragment.onSuccess(showroomProfileResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void saveShowroom(SaveBusinessRequest saveBusinessRequest) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<SaveBusinessResponse> myCall = apiRequest.saveBusiness(saveBusinessRequest);
        myCall.enqueue(new ApiCallback.MyCallback<SaveBusinessResponse>() {
            @Override
            public void success(final Response<SaveBusinessResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            SaveBusinessResponse saveBusinessResponse = response.body();
                            mFragment.onSaveSuccess(saveBusinessResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.TRUE);
    }

    public void getAllCars(String showroomID) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomCarsResponse> myCall = apiRequest.getShowroomCars(showroomID,0);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomCarsResponse>() {
            @Override
            public void success(final Response<ShowroomCarsResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomCarsResponse showroomCarsResponse = response.body();
                            mFragment.onCarSuccess(showroomCarsResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }

    public void getAllOffers(String showroomID) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomOfferResponse> myCall = apiRequest.getShowroomOffers(showroomID,0);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomOfferResponse>() {
            @Override
            public void success(final Response<ShowroomOfferResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomOfferResponse showroomOfferResponse = response.body();
                            mFragment.onOfferSuccess(showroomOfferResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }

    public void getAllProducts(String showroomID) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomProductResponse> myCall = apiRequest.getShowroomProducts(showroomID,0);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomProductResponse>() {
            @Override
            public void success(final Response<ShowroomProductResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomProductResponse showroomProductResponse = response.body();
                            mFragment.onProductSuccess(showroomProductResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }

    public void getAllReviews(String showroomID) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<ShowroomReviewResponse> myCall = apiRequest.getShowroomReviews(showroomID,0);
        myCall.enqueue(new ApiCallback.MyCallback<ShowroomReviewResponse>() {
            @Override
            public void success(final Response<ShowroomReviewResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            ShowroomReviewResponse showroomReviewResponse = response.body();
                            mFragment.onReviewsSuccess(showroomReviewResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout,Boolean.FALSE);
    }
}
