package autroid.business.view.fragment.jobcard;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.AdvisorBE;
import autroid.business.model.bean.TechniciansDataBE;
import autroid.business.model.request.UpdateMemberRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.jobcard.JobsMemberUpdatePresenter;
import autroid.business.utils.Constant;

public class JobsMemberUpdate extends DialogFragment {

    private boolean isTechnician;

    private ArrayList<TechniciansDataBE> techniciansArrayList;

    private ArrayList<AdvisorBE> advisorsArrayList;

    private RelativeLayout mainLayout;

    private JobsMemberUpdatePresenter presenter;

    private AppCompatSpinner memberSpinner;

    private String bookingId = "";

    private ArrayList<String> arrayList;

    private Button update;

    private String memberId = "";

    public JobsMemberUpdate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimationDialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_member_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        memberSpinner = view.findViewById(R.id.member_spinner);
        update = view.findViewById(R.id.update);
        mainLayout = view.findViewById(R.id.main_layout);


        arrayList = new ArrayList<>();
        presenter = new JobsMemberUpdatePresenter(this, mainLayout);

        bookingId = getArguments().getString(Constant.BOOKING_ID);
        isTechnician = getArguments().getBoolean(Constant.KEY_TYPE);

        if (isTechnician) createTechnicianList();
        else createAdvisorList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_text, arrayList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_text);
        memberSpinner.setAdapter(adapter);

        memberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(isTechnician) memberId = techniciansArrayList.get(position).getId();
                else memberId = advisorsArrayList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        update.setOnClickListener(v -> {
            if (memberId.trim().length() > 0 && isTechnician) {
                UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest();
                updateMemberRequest.setBooking(bookingId);
                updateMemberRequest.setTechnician(memberId);
                presenter.updateTechnician(updateMemberRequest);
            }else if(memberId.trim().length() > 0){
                UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest();
                updateMemberRequest.setBooking(bookingId);
                updateMemberRequest.setAdvisor(memberId);
                presenter.updateAdvisor(updateMemberRequest);
            }
        });
    }

    private void createTechnicianList() {

        techniciansArrayList = (ArrayList<TechniciansDataBE>) getArguments().getSerializable(Constant.VALUE);

        TechniciansDataBE techniciansDataBE = new TechniciansDataBE();
        techniciansDataBE.setId("");
        techniciansDataBE.setName("Select Member");

        techniciansArrayList.add(0, techniciansDataBE);

        for (TechniciansDataBE data : techniciansArrayList) arrayList.add(data.getName());
    }

    private void createAdvisorList() {

        advisorsArrayList = (ArrayList<AdvisorBE>) getArguments().getSerializable(Constant.VALUE);

        AdvisorBE advisorBE = new AdvisorBE();
        advisorBE.setId("");
        advisorBE.setName("Select Member");

        advisorsArrayList.add(0, advisorBE);

        for (AdvisorBE data : advisorsArrayList) arrayList.add(data.getName());
    }

    public void onSuccess(BaseResponse response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(Constant.KEY_EVENT_ID,Constant.EVENT_UPDATE);
        Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
        GlobalBus.getBus().post(sendEvent);
        getDialog().dismiss();
    }
}
