package autroid.business.presenter;

import android.view.ViewGroup;


import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.NotificationsListResponse;
import autroid.business.view.fragment.NotificationListFragment;
import retrofit2.Response;

public class NotificationListPresenter {

    private NotificationListFragment mFragment;
    private ViewGroup mMainLayout;

    public NotificationListPresenter(NotificationListFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getNotifications(int page) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<NotificationsListResponse> myCall = apiRequest.getNotifications(page);
        myCall.enqueue(new ApiCallback.MyCallback<NotificationsListResponse>() {
            @Override
            public void success(final Response<NotificationsListResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            NotificationsListResponse notificationsListResponse = response.body();
                            mFragment.onSuccess(notificationsListResponse);
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {
            }
        }, mFragment.getActivity(), mMainLayout, Boolean.TRUE);
    }
}
