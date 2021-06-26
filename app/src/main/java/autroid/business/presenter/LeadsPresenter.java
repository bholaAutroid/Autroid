package autroid.business.presenter;

import android.view.ViewGroup;

import autroid.business.api.ApiCallback;
import autroid.business.api.ApiFactory;
import autroid.business.api.ApiRequest;
import autroid.business.model.request.LeadUpdateRequest;
import autroid.business.model.response.LeadImportantResponse;
import autroid.business.model.response.LeadsResponse;
import autroid.business.view.fragment.leads.LeadsListFragment;
import retrofit2.Response;

public class LeadsPresenter {

    private LeadsListFragment mFragment;
    private ViewGroup mMainLayout;

    public LeadsPresenter(LeadsListFragment frag, ViewGroup mainLayout) {
        mFragment = frag;
        mMainLayout = mainLayout;
    }


    public void getLeads(int page,String status,int priority,String date,boolean byFilter) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<LeadsResponse> myCall;
        if(byFilter)myCall = apiRequest.getAllLeads("filter",page,status,priority,date);
        else myCall = apiRequest.getAllLeads("",page,status,priority,date);

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

    public void setImportant(final LeadUpdateRequest id) {
        ApiRequest apiRequest = ApiFactory.createService(mFragment.getActivity(), ApiRequest.class);
        ApiCallback.MyCall<LeadImportantResponse> myCall = apiRequest.setImportant(id);
        myCall.enqueue(new ApiCallback.MyCallback<LeadImportantResponse>() {
            @Override
            public void success(final Response<LeadImportantResponse> response) {
                mFragment.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.body().getResponseCode() == 200) {
                            LeadImportantResponse leadImportantResponse = response.body();
                           // mFragment.onImpSuccess(leadImportantResponse,id.getId());
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
