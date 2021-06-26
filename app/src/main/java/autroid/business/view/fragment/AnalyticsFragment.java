package autroid.business.view.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import autroid.business.R;
import autroid.business.adapter.AnalyticsAdapter;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.bean.AnalyticsBE;
import autroid.business.model.response.AnalyticsResponse;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.AnalyticsPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.customviews.CustomSpinner;
import autroid.business.view.customviews.CustomViewPager;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFragment extends Fragment implements MainSearchListener /*implements DatePickerDialog.OnDateSetListener */ {

    String startDate = "", endDate = "";

    FloatingActionButton fab;

    ImageView mBackNavigation;

    RelativeLayout mMainLayout;

    TextView period;;

    CustomViewPager mRecyclerView;

    AnalyticsPresenter mPresenter;

    FirebaseAnalytics mFirebaseAnalytics;

    AnalyticsAdapter analyticsAdapter;

    //boolean startDate,endDate;

    boolean isClicked = false;

    int fromDay, fromMonth, fromYear, toDay, toMonth, toYear,memberRemovalPosition;

    ArrayList<AnalyticsBE> analyticsList;

    LinearLayoutManager linearLayoutManager;

    String department = Constant.OPERATIONS;

    CustomSpinner selectionSpinner;

    ArrayList<String> selectionKeys;

    ArrayList<Integer> selectionValues;

    CalendarView calendar;

    LinearLayout calendarLayout, noMembers;

    Button buttonFrom, buttonTo, cancel, apply;

    public AnalyticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analytics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GlobalBus.getBus().register(this);

        //selectionSpinner=new CustomSpinner(getActivity());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "Analytics", null);

        mMainLayout = view.findViewById(R.id.main_layout);
        mRecyclerView = view.findViewById(R.id.list);
        fab = view.findViewById(R.id.fab_add);
        mBackNavigation = view.findViewById(R.id.back_navigation);
        noMembers = view.findViewById(R.id.no_member);
        period = view.findViewById(R.id.period);
        selectionSpinner = view.findViewById(R.id.selection_keys);
        buttonFrom = view.findViewById(R.id.button_from);
        buttonTo = view.findViewById(R.id.button_to);
        cancel = view.findViewById(R.id.cancel);
        apply = view.findViewById(R.id.apply);
        calendarLayout = view.findViewById(R.id.calendar_layout);
        calendar = view.findViewById(R.id.calendar);
        calendar.setMaxDate(System.currentTimeMillis() - 1000);

        setUpData();

        setUpSpinner();

       /* linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);*/

        analyticsList = new ArrayList<>();
        mPresenter = new AnalyticsPresenter(this, mMainLayout);
        analyticsAdapter = new AnalyticsAdapter(getActivity(), analyticsList,this);
        mRecyclerView.setAdapter(analyticsAdapter);
        mRecyclerView.setPadding(40, 0, 40, 0);


        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(mRecyclerView);

        //mRecyclerView.setNestedScrollingEnabled(false);

//        mStartDate.setOnClickListener(v -> {
//           startDate=true;
//           endDate=false;
//           setDate();
//        });

//        mEndDate.setOnClickListener(v -> {
//            startDate=false;
//            endDate=true;
//            setDate();
//        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Date dateFormat = null;

                String minDate = year + "-" + (month + 1) + "-" + day;

                try {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(minDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!isClicked) {
                    isClicked = true;
                    fromDay = day;
                    fromYear = year;
                    fromMonth = month + 1;
                    startDate = setUpDate(year, month, day);
                    buttonTo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                    buttonFrom.setBackgroundColor(getResources().getColor(R.color.matt_black));
                    calendar.setDate(System.currentTimeMillis());
                    calendarView.setMinDate(dateFormat.getTime());
                    buttonFrom.setText("FROM\n" + startDate);
                } else {
                    toDay = day;
                    toYear = year;
                    toMonth = month + 1;
                    apply.setVisibility(View.VISIBLE);
                    endDate = setUpDate(year, month, day);
                    buttonTo.setText("TO\n" + endDate);
                }
            }
        });


        cancel.setOnClickListener(v -> {
            isClicked = false;
            apply.setVisibility(View.GONE);
            calendarLayout.setVisibility(View.GONE);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
            calendar.setMinDate(0);
            buttonFrom.setText("FROM");
            buttonTo.setText("TO");
        });

        apply.setOnClickListener(v -> {
            isClicked = false;
            calendarLayout.setVisibility(View.GONE);
            apply.setVisibility(View.GONE);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
            mPresenter.getShowroom(Constant.OPERATIONS, "range", startDate + "to" + endDate);
            calendar.setMinDate(0);
            buttonFrom.setText("FROM");
            buttonTo.setText("TO");
            period.setText(Utility.getSpecificDate(fromYear, fromMonth, fromDay) + " To " + Utility.getSpecificDate(toYear, toMonth, toDay));
        });

        buttonFrom.setOnClickListener(v -> {
            isClicked = false;
            startDate = "";
            endDate = "";
            fromYear = 0;
            fromMonth = 0;
            fromDay = 0;
            toYear = 0;
            toMonth = 0;
            toDay = 0;
            apply.setVisibility(View.GONE);
            calendar.setMinDate(0);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
        });


       /* if (PreferenceManager.getInstance().getStringPreference(getActivity(), Constant.SP_CRE).equalsIgnoreCase("admin")) fab.show();
        else*/

       fab.hide();

        fab.setOnClickListener(v -> {
            AddTeamMember teamMember = new AddTeamMember();
            teamMember.show(getChildFragmentManager(), "AddTeamMember");
        });

        mBackNavigation.setOnClickListener(v -> getActivity().onBackPressed());
    }

//    public void setDate() {
//
//        final Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int day = c.get(Calendar.DAY_OF_MONTH);
//        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
//
//        final Calendar later = Calendar.getInstance();
//        later.add(Calendar.MONTH, 1);
//
//        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {
//
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//
//                if (startDate) mStartDate.setText(year + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(dayOfMonth));
//
//                if (endDate) mEndDate.setText(year + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(dayOfMonth));
//
//                if(mStartDate.getText().toString().trim().length()!=0 && mEndDate.getText().toString().trim().length()!=0)
//                    getData(department, mStartDate.getText().toString().trim(), mEndDate.getText().toString().trim());
//
//                datePickerDialog.dismiss();
//
//            }
//        });
//
//        datePickerDialog.show();
//    }

//    @Override
//    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//        if (startDate) mStartDate.setText(year + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(dayOfMonth));
//
//        if (endDate) mEndDate.setText(year + "-" + zeroPrefix(monthOfYear + 1) + "-" + zeroPrefix(dayOfMonth));
//
//        if(mStartDate.getText().toString().trim().length()!=0 && mEndDate.getText().toString().trim().length()!=0)
//            getData(department, mStartDate.getText().toString().trim(), mEndDate.getText().toString().trim());
//    }

    public void onSuccess(AnalyticsResponse analyticsResponse) {

        if (analyticsList.size() > 0) analyticsList.clear();

        if (analyticsResponse.getAnalyticsData().size() == 0) noMembers.setVisibility(View.VISIBLE);
        else noMembers.setVisibility(View.GONE);

        analyticsList.addAll(analyticsResponse.getAnalyticsData());

        analyticsAdapter.notifyDataSetChanged();
    }

    private void setUpData() {

        selectionKeys = new ArrayList<>();
        selectionValues = new ArrayList<>();

        selectionKeys.add("Last 7 Days");
        selectionKeys.add("Last 15 Days");
        selectionKeys.add("Last 1 Month");
        selectionKeys.add("Last 3 Months");
        selectionKeys.add("Last 6 Months");
        selectionKeys.add("Last 12 Months");
        selectionKeys.add("Custom");

        selectionValues.add(7);
        selectionValues.add(15);
        selectionValues.add(30);
        selectionValues.add(90);
        selectionValues.add(180);
        selectionValues.add(360);
        selectionValues.add(0);

    }

    private void setUpSpinner() {

        ArrayAdapter<String> adapterSelection = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark, selectionKeys);
        adapterSelection.setDropDownViewResource(R.layout.layout_spinner_text);
        selectionSpinner.setAdapter(adapterSelection);

        selectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (selectionValues.get(position) != 0) {
                    String extractedDate = Utility.periodCalculator(selectionValues.get(position));
                    period.setText(extractedDate);
                    mPresenter.getShowroom(department, "period", String.valueOf(selectionValues.get(position)));
                } else {
                    calendarLayout.setVisibility(View.VISIBLE);
                    calendar.setDate(System.currentTimeMillis());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe
    public void getEvent(Events.SendEvent sendEvent) {
        Intent intent = sendEvent.getEvent();
        if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_CHANGE_ACCOUNT) {
//            mStartDate.setText("");
//            mEndDate.setText("");
//            getData(department, "", "");
        } else if (intent.getIntExtra(Constant.KEY_EVENT_ID, -1) == Constant.EVENT_MEMBER_ADDED) {

            if (selectionSpinner.getSelectedItemPosition() == selectionKeys.size() - 1 && (startDate.trim().length() == 0 || endDate.trim().length() == 0)) {
                mPresenter.getShowroom(department, "range", "");
            } else if (selectionSpinner.getSelectedItemPosition() == selectionKeys.size() - 1 && startDate.trim().length() > 0 && endDate.trim().length() > 0) {
                mPresenter.getShowroom(department, "range", startDate + "to" + endDate);
            } else {
                mPresenter.getShowroom(department, "period", String.valueOf(selectionValues.get(selectionSpinner.getSelectedItemPosition())));
            }
        }
    }

    private void getData(String department, String from, String to) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.START_DATE, from);
        params.putString(FirebaseAnalytics.Param.END_DATE, to);
        mFirebaseAnalytics.logEvent("analytics", params);
    }

    private String setUpDate(int year, int month, int day) {
        month += 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String targetDateValue = null;
        try {
            Date sourceDate = dateFormat.parse(zeroPrefix(day) + "-" + zeroPrefix(month) + "-" + zeroPrefix(year));
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            targetDateValue = targetFormat.format(sourceDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return targetDateValue;
    }

    public String zeroPrefix(int quantity) {

        if (quantity < 10) return "0" + quantity;

        return "" + quantity;
    }

    @Override
    public void onClickSearchItem(String id, String status, Bundle data) {
        memberRemovalPosition=Integer.parseInt(status);
        mPresenter.removeMember(id);
    }

    public void onSuccessRemoveMember(BaseResponse response) {

        analyticsList.remove(memberRemovalPosition);
        analyticsAdapter.notifyDataSetChanged();

        if (analyticsList.size() == 0) noMembers.setVisibility(View.VISIBLE);
        else noMembers.setVisibility(View.GONE);
    }
}
