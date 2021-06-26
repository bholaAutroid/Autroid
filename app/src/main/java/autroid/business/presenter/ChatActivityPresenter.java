package autroid.business.presenter;

import android.content.Intent;
import android.net.Uri;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.BusinessProfileResponse;
import autroid.business.view.activity.ChatActivity;
import retrofit2.Response;

public class ChatActivityPresenter {

    private ChatActivity mChatActivity;

    public ChatActivityPresenter(ChatActivity activity){
        mChatActivity=activity;
    }

    public void getContact(String Id) {

        ApiRequest apiRequest = ApiFactory.createService(mChatActivity,ApiRequest.class);
        ApiCallback.MyCall<BusinessProfileResponse> myCall = apiRequest.getShowroomProfile(Id);

        myCall.enqueue(new ApiCallback.MyCallback<BusinessProfileResponse>() {
            @Override
            public void success(final Response<BusinessProfileResponse> response) {
                mChatActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            BusinessProfileResponse showroomProfileResponse = response.body();
                            mChatActivity.startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+showroomProfileResponse.getResponseData().getContact_no())));
                        } else {
                            //App.getInstance().showErroroBar(response.body().getResponseMessage());
                        }
                    }
                });
            }
            @Override
            public void error(String errorMessage) {}
        },mChatActivity,null,Boolean.TRUE);
    }
}
