package autroid.business.view.fragment.leads;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.qiscus.nirmana.Nirmana;

import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.leads.LeadRemarkAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.model.bean.AdvisorBE;
import autroid.business.model.bean.RemarkBE;
import autroid.business.model.bean.UserBE;
import autroid.business.model.realm.LeadsRealm;
import autroid.business.model.request.LeadAdvisorRequest;
import autroid.business.model.request.LeadPriorityRequest;
import autroid.business.model.request.LeadUpdateRequest;
import autroid.business.model.response.AdvisorResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.LeadDetailsResponse;
import autroid.business.model.response.CreateLeadResponse;
import autroid.business.model.response.LeadStatusResponse;
import autroid.business.presenter.LeadDetailPresenter;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.activity.HomeScreen;
import autroid.business.view.customviews.CustomSpinner;
import autroid.business.view.fragment.booking.ManualBookingFragment;
import autroid.business.view.fragment.carsales.AddCarFragment;
import autroid.business.view.fragment.jobcard.JobCardCarSelectionFragment;
import autroid.business.view.fragment.search.MainSearchBookingDetailsFragment;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeadDetailFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.spn_status)
    CustomSpinner mSpnStatus;
    @BindView(R.id.remark)
    EditText mRemark;
    @BindView(R.id.update_status)
    Button mStatus;
    @BindView(R.id.main_layout)
    LinearLayout mMainLayout;
    @BindView(R.id.call_btn)
    ImageButton callBtn;
    @BindView(R.id.chat_btn)
    ImageButton chatBtn;

    TextView status, source, name, email, assignee, lastActive, advisor, contact, followUpDate, followUpTime, mViewOption;

    PopupMenu popup;

    RelativeLayout followUpLayout, recordingLayout;

    ImageView userUpdate, playback;

    SeekBar seekBar;

    LinearLayout advisorSpinnerLayout, reasonSpinnerLayout, advisorLayout;

    AppCompatSpinner prioritySpinner, advisorSpinner, reasonSpinner;

    LeadDetailPresenter mPresenter;

    LeadDetailsResponse detailsResponse;

    LeadRemarkAdapter remarkAdapter;

    String leadId, currentStatus = "", advisorId = "", advisorFromResponse = "", stage = "", bookingId = "", userId = "";

    ArrayList<AdvisorBE> advisorsList;

    ArrayList<RemarkBE> remarkList;

    boolean fromMainSearch, fromAssignedLead, fromLeadsList, mutexPriority, mutexAdvisor, allUpdated, nameUpdated, contactUpdated, prepared, isPaused, isPsf;

    Realm realm;

    RealmController realmController;

    LeadsRealm leadsRealm;

    RecyclerView recList;

    //String strRemark;

    int currentDay, currentMonth, currentYear, currentHour, currentMin, priority;

    Calendar calendar;

    DatePickerDialog datePickerDialog;

    TimePickerDialog timePickerDialog;

    MediaPlayer mediaPlayer;

    Runnable runnable;

    Handler handler;

    public LeadDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (detailsResponse != null && isPaused) {
            setUpPlayBack(detailsResponse.getLeadDetail().getRemark().getResource());
            seekBar.setProgress(0);
//            Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(playback);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (prepared && mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            isPaused = true;
        }

        if (handler != null) handler.removeCallbacks(runnable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lead_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this, view);
        GlobalBus.getBus().register(this);

        this.realm = RealmController.with(getActivity()).getRealm();
        realmController = new RealmController(getActivity().getApplication());

        advisorsList = new ArrayList<>();
        remarkList = new ArrayList<>();
        remarkAdapter = new LeadRemarkAdapter(getActivity(), remarkList);

        setUpDateData();
        setUpTimeData();
        findViews(view);
        getBundleData();

        recList.setLayoutManager(new LinearLayoutManager(getActivity()));
        recList.setAdapter(remarkAdapter);

        mPresenter = new LeadDetailPresenter(this, mMainLayout);
        mPresenter.getAdvisor();
        mPresenter.getLeadDetail(leadId);


        mStatus.setOnClickListener(this);
        //updateDetails.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        chatBtn.setOnClickListener(this);
        //chatBtn.setOnClickListener(this);

//        date.setOnClickListener(v -> {
//            datePickerDialog.show();
//        });

    }

    private void setUpDateData() {
        calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentMonth = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(getActivity(), this, currentYear, currentMonth, currentDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
    }

    private void setUpTimeData() {
        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        currentMin = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(getActivity(), this, currentHour, currentMin, false);
    }


    private void findViews(View view) {
        status = view.findViewById(R.id.status);
        source = view.findViewById(R.id.source);
        userUpdate = view.findViewById(R.id.user_update);
        name = view.findViewById(R.id.name);
        contact = view.findViewById(R.id.contact);
        email = view.findViewById(R.id.email);
        recList = view.findViewById(R.id.remark_list);
        assignee = view.findViewById(R.id.assignee);
        advisor = view.findViewById(R.id.advisor);
        prioritySpinner = view.findViewById(R.id.priority_spinner);
        followUpLayout = view.findViewById(R.id.follow_up_layout);
        recordingLayout = view.findViewById(R.id.recording_layout);
        advisorSpinnerLayout = view.findViewById(R.id.advisor_spinner_layout);
        reasonSpinnerLayout = view.findViewById(R.id.reason_spinner_layout);
        followUpDate = view.findViewById(R.id.follow_up_date);
        followUpTime = view.findViewById(R.id.follow_up_time);
        advisorSpinner = view.findViewById(R.id.advisor_spinner);
        reasonSpinner = view.findViewById(R.id.reason_spinner);
        advisorLayout = view.findViewById(R.id.advisor_layout);
        mViewOption = view.findViewById(R.id.textViewOptions);
        lastActive = view.findViewById(R.id.last_active);
        playback = view.findViewById(R.id.playback);
        seekBar = view.findViewById(R.id.seekbar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (prepared && fromUser) {
                    seekBar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                    startPlayBack();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        userUpdate.setOnClickListener(this);
        followUpDate.setOnClickListener(this);
        followUpTime.setOnClickListener(this);
        mViewOption.setOnClickListener(this);
        playback.setOnClickListener(this);
    }

    public void onSuccessDetails(LeadDetailsResponse detailsResponse) {

        this.detailsResponse = detailsResponse;

        if (detailsResponse.getLeadDetail().getRemark() != null)
            setAvailableStatus(detailsResponse.getLeadDetail().getRemark().getStatus());

        if (!fromMainSearch && (detailsResponse.getLeadDetail().getCategory().equalsIgnoreCase("Booking") || detailsResponse.getLeadDetail().getCategory().equalsIgnoreCase("Insurance")))
            setUpMenu();

        setUpPrimaryData(detailsResponse);

        bookingId = detailsResponse.getLeadDetail().getBooking();

        if (detailsResponse.getLeadDetail().getUser() != null)
            userId = detailsResponse.getLeadDetail().getUser();

        if (detailsResponse.getLeadDetail().getRemark() != null && !detailsResponse.getLeadDetail().getRemark().getResource().equals("")) {
            recordingLayout.setVisibility(View.VISIBLE);
            setUpPlayBack(detailsResponse.getLeadDetail().getRemark().getResource());
        }

        remarkList.addAll(detailsResponse.getLeadDetail().getLogs());
        remarkAdapter.notifyDataSetChanged();

        setUpPrioritySpinner(detailsResponse.getLeadDetail().getPriority());

        if (detailsResponse.getLeadDetail().getFollow_up().getDate() != null) {
            followUpDate.setText(detailsResponse.getLeadDetail().getFollow_up().getDate().substring(0, 10));
            followUpTime.setText(detailsResponse.getLeadDetail().getFollow_up().getTime());
        }

        //if(detailsResponse.getLeadDetail().getAdvisor()==null)advisorSpinnerLayout.setVisibility(View.VISIBLE);

    }

    public void onSuccessAdvisors(AdvisorResponse response) {

        LeadAdvisorRequest leadAdvisorRequest = new LeadAdvisorRequest();
        leadAdvisorRequest.setLead(leadId);

        AdvisorBE advisor = new AdvisorBE();
        advisor.setId("");
        advisor.setName("Select Advisor");
        advisor.setContact_no("");
        advisor.setEmail("");

        advisorsList.add(advisor);
        advisorsList.addAll(response.getAdvisorsList());

        ArrayList<String> advisorNames = new ArrayList<>();
        for (AdvisorBE data : advisorsList) advisorNames.add(data.getName());

        ArrayAdapter<String> advisorAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, advisorNames);
        advisorAdapter.setDropDownViewResource(R.layout.layout_spinner_remark);
        advisorSpinner.setAdapter(advisorAdapter);

        advisorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                leadAdvisorRequest.setAdvisor(advisorsList.get(position).getId());
                if (mutexAdvisor) mPresenter.addAdvisor(leadAdvisorRequest);
                mutexAdvisor = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onSuccessPriorityUpdate(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
    }

    public void onSuccessAddAdvisor(BaseResponse response) {
        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
        advisorSpinnerLayout.setVisibility(View.GONE);
    }

    public void onStatusSuccess(LeadStatusResponse leadStatusResponse) {

        reasonSpinnerLayout.setVisibility(View.VISIBLE);

        ArrayList<String> reasonList = new ArrayList<>();
        reasonList.add("Select Reason");

        for (String status : leadStatusResponse.getStatusList()) reasonList.add(status);

        ArrayAdapter<String> reasonListAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, reasonList);
        reasonListAdapter.setDropDownViewResource(R.layout.layout_spinner_remark);
        reasonSpinner.setAdapter(reasonListAdapter);
    }

    private void setUpPrioritySpinner(int responsePriority) {

        int selection;

        LeadPriorityRequest leadPriorityRequest = new LeadPriorityRequest();
        leadPriorityRequest.setLead(leadId);

        if (responsePriority == 3) selection = 0;
        else if (responsePriority == 2) selection = 1;
        else selection = 2;

        ArrayList<String> list = new ArrayList<>();
        list.add("High");
        list.add("Medium");
        list.add("Low");

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, list);
        adapterStatus.setDropDownViewResource(R.layout.layout_spinner_remark);
        prioritySpinner.setAdapter(adapterStatus);

        prioritySpinner.setSelection(selection);

        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0) priority = 3;
                else if (position == 1) priority = 2;
                else priority = 1;

                leadPriorityRequest.setPriority(priority);
                if (mutexPriority) mPresenter.updatePriority(leadPriorityRequest);
                mutexPriority = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setUpPrimaryData(LeadDetailsResponse detailsResponse) {

        int counter = 0;

        status.setText(detailsResponse.getLeadDetail().getRemark().getStatus());
        source.setText(detailsResponse.getLeadDetail().getSource());

        if (!detailsResponse.getLeadDetail().getName().equals("")) {
            name.setText(detailsResponse.getLeadDetail().getName());
            counter += 1;
            nameUpdated = true;
        } else name.setText("N/A");

        if (!detailsResponse.getLeadDetail().getContact_no().equals("")) {
            contact.setText(detailsResponse.getLeadDetail().getContact_no());
            counter += 1;
            contactUpdated = true;
            callBtn.setVisibility(View.VISIBLE);
        } else contact.setText("N/A");

        if (!detailsResponse.getLeadDetail().getEmail().equals("")) {
            email.setText(detailsResponse.getLeadDetail().getEmail());
            counter += 1;
        } else email.setText("N/A");

        if (detailsResponse.getLeadDetail().getAssignee() != null)
            assignee.setText(detailsResponse.getLeadDetail().getAssignee().getName());
        else assignee.setVisibility(View.GONE);

        if (detailsResponse.getLeadDetail().getAdvisor() != null) {
            advisor.setText(detailsResponse.getLeadDetail().getAdvisor().getName());
            advisorFromResponse = detailsResponse.getLeadDetail().getAdvisor().getId();
        } else advisorLayout.setVisibility(View.GONE);

        if (!detailsResponse.getLeadDetail().getLast_active().equals(""))
            lastActive.setText(detailsResponse.getLeadDetail().getLast_active());
        else lastActive.setText("N/A");

        if (counter == 3) userUpdate.setVisibility(View.GONE);

    }

    private void setAvailableStatus(String status) {

        ArrayList<String> list = new ArrayList<>();

        if (status.equals(Constant.OPEN)) {
            list.add(status);
            list.add(Constant.FOLLOW_UP);
            list.add(Constant.LOST);
            list.add(Constant.CLOSED);
        } else if (status.equals(Constant.FOLLOW_UP)) {
            list.add(status);
            list.add(Constant.LOST);
            list.add(Constant.CLOSED);
        } else {
            list.add(Constant.PSF);
            isPsf = true;
        }


        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(getActivity(), R.layout.layout_spinner_remark, list);
        adapterStatus.setDropDownViewResource(R.layout.layout_spinner_remark);
        mSpnStatus.setAdapter(adapterStatus);

        mSpnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (mSpnStatus.getSelectedItem().equals(Constant.OPEN)) {
                    followUpLayout.setVisibility(View.GONE);
                    reasonSpinnerLayout.setVisibility(View.GONE);
                } else if (mSpnStatus.getSelectedItem().equals(Constant.FOLLOW_UP)) {
                    followUpLayout.setVisibility(View.VISIBLE);
                    reasonSpinnerLayout.setVisibility(View.GONE);
                } else if (mSpnStatus.getSelectedItem().equals(Constant.LOST)) {
                    mPresenter.getLeadsStatus(Constant.LOST);
                    followUpLayout.setVisibility(View.GONE);
                } else if (mSpnStatus.getSelectedItem().equals(Constant.CLOSED)) {
                    followUpLayout.setVisibility(View.GONE);
                    reasonSpinnerLayout.setVisibility(View.GONE);
                } else if (mSpnStatus.getSelectedItem().equals(Constant.PSF)) {
                    followUpLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_status:
                if (validate()) {
                    LeadUpdateRequest leadUpdateRequest = new LeadUpdateRequest();
                    leadUpdateRequest.setId(leadId);
                    leadUpdateRequest.setStatus(mSpnStatus.getSelectedItem().toString().trim());
                    leadUpdateRequest.setRemark(mRemark.getText().toString().trim());
                    leadUpdateRequest.setDate(followUpDate.getText().toString().trim());
                    leadUpdateRequest.setTime(followUpTime.getText().toString().trim());
                    if (mSpnStatus.getSelectedItem().equals(Constant.LOST))
                        leadUpdateRequest.setReason(reasonSpinner.getSelectedItem().toString().trim());
                    //leadUpdateRequest.setAdvisor(advisorId);
                    //leadUpdateRequest.setDate(date.getText().toString().trim());
                    mPresenter.updateLeadsStatus(leadUpdateRequest);
                }
                break;

            case R.id.user_update:
                LeadUserUpdate leadUserUpdate = new LeadUserUpdate();
                Bundle bundle = new Bundle();
                UserBE user = new UserBE();
                bundle.putString(Constant.KEY_ID, leadId);
                bundle.putSerializable(Constant.DETAILS_TYPE, user);
                user.setName(name.getText().toString().trim());
                user.setContact_no(contact.getText().toString().trim());
                user.setEmail(email.getText().toString().trim());
                leadUserUpdate.setArguments(bundle);
                leadUserUpdate.show(getChildFragmentManager(), "LeadUserDetailsUpdate");
                break;

            case R.id.call_btn:
                if (detailsResponse != null)
                    Utility.onCallClick(detailsResponse.getLeadDetail().getContact_no(), getActivity());
                break;

            case R.id.chat_btn:
                if (detailsResponse != null)
                    Utility.whatsAppChat(detailsResponse.getLeadDetail().getContact_no(), getActivity());
                break;

            case R.id.follow_up_date:
                datePickerDialog.show();
                break;

            case R.id.follow_up_time:
                timePickerDialog.show();
                break;

            case R.id.textViewOptions:
                if (popup != null) popup.show();
                break;

            case R.id.playback:
                if (prepared && !mediaPlayer.isPlaying()) startPlayBack();
                else if (prepared && mediaPlayer.isPlaying()) stopPlayBack();
                break;

            case R.id.seekbar:
                break;

        }
    }

    private boolean validate() {

        if (mSpnStatus.getSelectedItem() == null) {
            Utility.showResponseMessage(mMainLayout, "Please refresh the screen");
            return false;
        } else if (mSpnStatus.getSelectedItem().equals(Constant.FOLLOW_UP) && followUpDate.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select Follow-Up Date");
            return false;
        } else if (mSpnStatus.getSelectedItem().equals(Constant.LOST) && reasonSpinner.getSelectedItemPosition() == 0) {
            Utility.showResponseMessage(mMainLayout, "Select reason");
            return false;
        } else if (mRemark.getText().toString().trim().length() == 0) {
            Utility.showResponseMessage(mMainLayout, "Enter Remark");
            return false;
        }

        return true;
    }


//    public void onSuccessUpdate(EditLeadResponse response) {
//        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();
//        detailsUpdated = true;
//        realmController.updatePrimaryData(leadId, response.getUser().getContact_no(), response.getUser().getEmail());
//    }

    public void onUpdateStatusSuccess(CreateLeadResponse response) {

        Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_SHORT).show();

//        if(fromAssignedLead) {
//            stage=Constant.ASSIGNED;
//            if (response.getmLeadsBE().getFollow_up().getDate() != null) realmController.updateAssignedLead(leadId, stage, response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getAssignee_remark(), response.getmLeadsBE().getUpdated_at(), response.getmLeadsBE().getFollow_up().getDate().substring(0, 10));
//            else realmController.updateAssignedLead(leadId, stage, response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getAssignee_remark(), response.getmLeadsBE().getUpdated_at(), "");
//        }

        if (!fromMainSearch) {
            if (response.getmLeadsBE().getFollow_up().getDate() != null)
                realmController.updateLead(leadId, response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getAssignee_remark(), response.getmLeadsBE().getUpdated_at(), response.getmLeadsBE().getFollow_up().getDate().substring(0, 10));
            else
                realmController.updateLead(leadId, response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getStatus(), response.getmLeadsBE().getRemark().getAssignee_remark(), response.getmLeadsBE().getUpdated_at(), "");

        } else {
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_REFRESH_LEAD);
            Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
            GlobalBus.getBus().post(sendEvent);
        }

        getActivity().onBackPressed();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date sourceDate = null;
        String targetdatevalue = null;
        try {
            sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetdatevalue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        followUpDate.setText(targetdatevalue);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfTheDay, int minute) {
        String amPm;
        amPm = hourOfTheDay >= 12 ? "PM" : "AM";
        hourOfTheDay = hourOfTheDay > 12 ? hourOfTheDay - 12 : hourOfTheDay;
        followUpTime.setText(String.format("%02d:%02d", hourOfTheDay, minute) + " " + amPm);
    }

    public String zeroPrefix(int quantity) {
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }


    private void getBundleData() {

        if (getArguments().get(Constant.VALUE).equals("LeadAssigned")) {
            leadId = getArguments().getString(Constant.KEY_ID);
            currentStatus = getArguments().getString(Constant.STATUS);
            fromAssignedLead = true;
        } else if (getArguments().getString(Constant.VALUE).equals("LeadList")) {
            leadId = getArguments().getString(Constant.KEY_ID);
            leadsRealm = realmController.getLeadById(leadId);
            currentStatus = leadsRealm.getStatus();
            fromLeadsList = true;
        } else {
            leadId = getArguments().getString(Constant.KEY_ID);
            //currentStatus = getArguments().getBundle(Constant.BUNDLE_DATA).getString(Constant.STATUS);
            fromMainSearch = true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_USER_DETAILS_UPDATE) {

            allUpdated = true;
            userId = intent.getStringExtra(Constant.KEY_ID);

        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SEND_CAR_INSURANCE) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName().equalsIgnoreCase("SelectionFragment"))fm.popBackStack();

            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_CAR_ID, intent.getStringExtra("car_id"));
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new LeadInsuranceCarDetailsFragment(), "LeadInsuranceCarDetailsFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);

        }else if(intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_SEND_INSURANCE_CAR){

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 2).getName().equalsIgnoreCase("SelectionFragment"))fm.popBackStack();

            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_VARIANT_ID, intent.getStringExtra("variant_id"));
            bundle.putString(Constant.KEY_VARIANT_NAME,intent.getStringExtra("variant_name"));
            bundle.putString(Constant.USER_ID,userId);
            bundle.putBoolean(Constant.VALUE,true);
            ((HomeScreen) getActivity()).makeDrawerVisible();
            ((HomeScreen) getActivity()).addFragment(new AddCarFragment(), "AddCarFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
        }
    }

    private void setUpMenu() {
        mViewOption.setVisibility(View.VISIBLE);
        popup = new PopupMenu(getActivity(), mViewOption);

        if (isPsf) popup.inflate(R.menu.booking_detail);
        else popup.inflate(R.menu.request_estimate);

        if (detailsResponse != null && detailsResponse.getLeadDetail().getCategory().equalsIgnoreCase("Insurance")) {
            popup.getMenu().add(0, 1, 2, "Renew Insurance");
        }

        popup.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.request_estimate:
                    if (nameUpdated && contactUpdated) {
                        Bundle bundle = new Bundle();
                        bundle.putString(Constant.KEY_ID, leadId);
                        bundle.putString(Constant.ADVISOR_ID, advisorFromResponse);
                        bundle.putBoolean(Constant.IS_LEAD, true);
                        if (fromLeadsList) bundle.putString(Constant.VALUE, "LeadList");
                        else if (fromAssignedLead) bundle.putString(Constant.VALUE, "LeadAssigned");
                        ((HomeScreen) getActivity()).addFragment(new ManualBookingFragment(), "ManualBookingFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                    } else Utility.showResponseMessage(mMainLayout, "Update user details");

                    break;

                case R.id.booking_detail:
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_ID, bookingId);
                    bundle.putString(Constant.KEY_TYPE, Constant.PSF);
                    ((HomeScreen) getActivity()).addFragment(new MainSearchBookingDetailsFragment(), "BookingDetailsFragment", true, false, bundle, ((HomeScreen) getActivity()).currentFrameId);
                    break;

                case 1:
                    if (nameUpdated && contactUpdated) {
                        Bundle bundleInsurance = new Bundle();
                        bundleInsurance.putBoolean(Constant.VALUE, true);
                        bundleInsurance.putString(Constant.KEY_ID, userId);
                        ((HomeScreen) getActivity()).makeDrawerVisible();
                        ((HomeScreen) getActivity()).addFragment(new JobCardCarSelectionFragment(), "SelectionFragment", true, false, bundleInsurance, ((HomeScreen) getActivity()).currentFrameId);
                    } else Utility.showResponseMessage(mMainLayout, "Update user details");

                    break;
            }

            return false;
        });
    }

    private void setUpPlayBack(String link) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            runnable = () -> updateSeekBar();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(link);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    prepared = true;
                    seekBar.setMax(mediaPlayer.getDuration());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void startPlayBack() {
        if (handler == null) handler = new Handler();
//        Nirmana.getInstance().get().load(R.drawable.ic_qiscus_pause_audio).into(playback);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(v -> {
//            Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(playback);
            handler.removeCallbacks(runnable);
        });

        updateSeekBar();
    }

    private void stopPlayBack() {
//        Nirmana.getInstance().get().load(R.drawable.ic_qiscus_play_audio).into(playback);
        mediaPlayer.pause();
    }


    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        handler.postDelayed(runnable, 1000);
    }
}
