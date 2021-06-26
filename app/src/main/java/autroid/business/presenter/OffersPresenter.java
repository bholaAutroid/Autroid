package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.PublishUnpublishRequest;
import autroid.business.model.response.OffersResponse;
import autroid.business.model.response.PublishResponse;
import autroid.business.view.fragment.OffersFragment;
import retrofit2.Response;

/**
 * Created by pranav.mittal on 06/11/17.
 */

public class OffersPresenter {

    private OffersFragment mFragment;
    private ViewGroup mMainLayout;

    public OffersPresenter(OffersFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getOffers(int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<OffersResponse> myCall = apiRequest.getOffers(page);
        myCall.enqueue(new ApiCallback.MyCallback<OffersResponse>() {
            @Override
            public void success(final Response<OffersResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            OffersResponse offersResponse = response.body();
                            mFragment.onSuccess(offersResponse);
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


    public void publishOffer(PublishUnpublishRequest publishUnpublishRequest, final String id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<PublishResponse> myCall = apiRequest.putPublishOfferRequest(publishUnpublishRequest);
        myCall.enqueue(new ApiCallback.MyCallback<PublishResponse>() {
            @Override
            public void success(final Response<PublishResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            PublishResponse publishResponse = response.body();
                            mFragment.publishUnpublishResponse(publishResponse,id);
                        } else {

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
