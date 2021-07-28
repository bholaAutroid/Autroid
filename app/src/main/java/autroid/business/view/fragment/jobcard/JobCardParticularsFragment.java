package autroid.business.view.fragment.jobcard;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.aws.AwsHomeActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.jobcard.AssetAdapter;
import autroid.business.interfaces.JobParticularsCallback;
import autroid.business.model.bean.ParticularsDataBE;
import autroid.business.model.request.UpdateRequest;
import autroid.business.model.response.BookingAddressResponse;
import autroid.business.model.response.GetParticularsResponse;
import autroid.business.presenter.jobcard.JobCardParticularsPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;

public class JobCardParticularsFragment extends Fragment implements JobParticularsCallback {

    TextView title;

    Button proceed_btn;

    RecyclerView recyclerView;

    @BindView(R.id.search)
    AutoCompleteTextView mSearch;

    ArrayList<ParticularsDataBE> assetList;

    EditText other_comments;

    RelativeLayout mainLayout;

    JobCardParticularsPresenter particularsPresenter;

    String bookingId,addressId,strConvenience="",strCharges;
    private AssetAdapter assetAdapter;

    public JobCardParticularsFragment(){}

    @Override
    public void onResume() {
        super.onResume();
        Utility.hideSoftKeyboard(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_card_particulars, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this,view);

        title = view.findViewById(R.id.toolbar_title);
        title.setText("Particulars Present");

        Utility.hideSoftKeyboard(getActivity());

        bookingId=getArguments().getString(Constant.BOOKING_ID);
        addressId=getArguments().getString(Constant.ADDRESS_ID);
        strConvenience=getArguments().getString(Constant.KEY_TYPE);
        strCharges=getArguments().getString(Constant.KEY_CHARGES);

        assetList=new ArrayList<>();
        other_comments=view.findViewById(R.id.other_comments);
        mainLayout=view.findViewById(R.id.main_layout);

        recyclerView=view.findViewById(R.id.jobcard_recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        particularsPresenter=new JobCardParticularsPresenter(this,mainLayout);
        particularsPresenter.getParticulars(bookingId);

        proceed_btn = view.findViewById(R.id.jobcard_proceed);

        /*others.setOnClickListener(v->{
            if(others.isChecked()){
                other_comments.setVisibility(View.VISIBLE);
                other_comments.requestFocus();
                Utility.showSoftKeyboard(getActivity(),other_comments);
            }else{
                other_comments.setVisibility(View.GONE);
                Utility.hideSoftKeyboard(getActivity());
            }
        });*/

        proceed_btn.setOnClickListener(v -> {
            ((AwsHomeActivity)getActivity()).disableButton(proceed_btn);
            if(other_comments.getVisibility()==View.VISIBLE && other_comments.getText().toString().equals("")){
                Utility.showResponseMessage(mainLayout,"Particulars are empty...");
            }else {
                particularsPresenter.updateParticulars(createRequest());
                //particularsPresenter.getParticulars(bookingId);
            }
        });

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(assetAdapter!=null)
                    assetAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void onSuccessResponse(GetParticularsResponse particularsResponse){

        for (ParticularsDataBE details:particularsResponse.getGetParticulars())assetList.add(details);

        ParticularsDataBE particularsDataBE=new ParticularsDataBE();
        particularsDataBE.setValue("Others");
        particularsDataBE.setChecked(false);
        assetList.add(particularsDataBE);
        assetAdapter=new AssetAdapter(assetList,this);
        recyclerView.setAdapter(assetAdapter);
    }

    public void onSuccessUpdate(GetParticularsResponse particularsResponse){
        Bundle bundle=new Bundle();
        bundle.putString(Constant.BOOKING_ID,bookingId);
        ((AwsHomeActivity) getActivity()).addFragment(new JobCardRequirementFragment(), "RequirementFragment", true, false, bundle, ((AwsHomeActivity) getActivity()).currentFrameId);
    }

    public UpdateRequest createRequest(){
        UpdateRequest updateRequest=new UpdateRequest();
        updateRequest.setBookingId(bookingId);
        assetList.remove(assetList.size()-1);
        updateRequest.setArrayList(assetList);
        updateRequest.setAddress(addressId);
        updateRequest.setConvenience(strConvenience);
        updateRequest.setCharges(strCharges);

        if(other_comments.getVisibility()==View.VISIBLE){
            updateRequest.setOtherDetails(other_comments.getText().toString().trim());
        }else{
            updateRequest.setOtherDetails("");
        }
        return updateRequest;
    }

    public void onAddressSuccess(BookingAddressResponse bookingAddressResponse) {}

    @Override
    public void onCheckChange(Boolean isChecked) {
        if(isChecked){
            other_comments.setVisibility(View.VISIBLE);
        }
        else {
            other_comments.setVisibility(View.GONE);
        }
    }
}
