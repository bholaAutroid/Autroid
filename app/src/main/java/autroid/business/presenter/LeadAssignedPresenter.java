package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.response.LeadsResponse;
import autroid.business.view.fragment.leads.LeadAssignedFragment;
import retrofit2.Response;

public class LeadAssignedPresenter {

        private LeadAssignedFragment mFragment;
        private ViewGroup mMainLayout;

        public LeadAssignedPresenter(LeadAssignedFragment frag, ViewGroup mainLayout) {
            mFragment = frag;
            mMainLayout = mainLayout;
        }


        public void getAssignedLeads(int pageNo) {
            ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
            ApiCallback.MyCall<LeadsResponse> myCall=apiRequest.getAllLeads("",pageNo,"N/A",0,""); //N/A implies no status is not available

            myCall.enqueue(new ApiCallback.MyCallback<LeadsResponse>() {
                @Override
                public void success(final Response<LeadsResponse> response) {
                    mFragment.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.body().getResponseCode() == 200) {
                                LeadsResponse baseResponse = response.body();
                                mFragment.onSuccess(baseResponse);
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
