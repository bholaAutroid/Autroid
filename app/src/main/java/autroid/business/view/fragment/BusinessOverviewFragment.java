package autroid.business.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import autroid.business.R;
import autroid.business.adapter.BusinessOverviewAdapter;
import autroid.business.model.bean.OverviewBE;
import autroid.business.model.response.BusinessOverviewResponse;
import autroid.business.presenter.BusinessOverviewPresenter;
import autroid.business.utils.Utility;
import autroid.business.view.customviews.CustomSpinner;

public class BusinessOverviewFragment extends Fragment {

    String startDate="",endDate="";

    boolean isClicked=false;

    int fromDay,fromMonth,fromYear,toDay,toMonth,toYear;

    ArrayList<OverviewBE> overviewDataList;

    TextView period;

    RecyclerView businessRecycler;

    RelativeLayout mainLayout;

    LinearLayout calendarLayout;

    View mTransparent;

    Button buttonFrom,buttonTo,cancel,apply;

    BusinessOverviewAdapter businessOverviewAdapter;

    BusinessOverviewPresenter businessOverviewPresenter;

    CustomSpinner selectionSpinner;

    ArrayList<String> selectionKeys;

    ArrayList<Integer> selectionValues;

    CalendarView calendar;

    public BusinessOverviewFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_business_overview,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        businessRecycler=view.findViewById(R.id.overview_list);
        mainLayout=view.findViewById(R.id.main_layout);
        selectionSpinner=view.findViewById(R.id.selection_keys);
        period=view.findViewById(R.id.period);
        buttonFrom=view.findViewById(R.id.button_from);
        buttonTo=view.findViewById(R.id.button_to);
        cancel=view.findViewById(R.id.cancel);
        apply=view.findViewById(R.id.apply);
        calendarLayout=view.findViewById(R.id.calendar_layout);
        mTransparent=view.findViewById(R.id.view_transparent);
        calendar =view.findViewById(R.id.calendar);
        calendar.setMaxDate(System.currentTimeMillis()-1000);

        setUpData();

        setUpSpinner();

        businessOverviewPresenter=new BusinessOverviewPresenter(this,mainLayout);

        overviewDataList=new ArrayList();
        businessOverviewAdapter=new BusinessOverviewAdapter(getActivity(),overviewDataList);
        businessRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        businessRecycler.setAdapter(businessOverviewAdapter);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Date dateFormat = null;

                String minDate=year+"-"+(month+1)+"-"+day;
                try {
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd").parse(minDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(!isClicked){
                    isClicked=true;
                    fromDay=day;
                    fromYear=year;
                    fromMonth=month+1;
                    startDate=setUpDate(year,month,day);
                    buttonTo.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
                    buttonFrom.setBackgroundColor(getResources().getColor(R.color.matt_black));
                    calendar.setDate(System.currentTimeMillis());
                    calendarView.setMinDate(dateFormat.getTime());
                    buttonFrom.setText("FROM\n"+startDate);

                }else{
                    toDay=day;
                    toYear=year;
                    toMonth=month+1;
                    apply.setVisibility(View.VISIBLE);
                    endDate=setUpDate(year,month,day);
                    buttonTo.setText("TO\n"+endDate);

                }
            }
        });

        cancel.setOnClickListener(v->{
            isClicked=false;
            apply.setVisibility(View.GONE);
            calendarLayout.setVisibility(View.GONE);
            mTransparent.setVisibility(View.GONE);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
            calendar.setMinDate(0);
            buttonFrom.setText("FROM\n");
            buttonTo.setText("TO\n");
        });

        apply.setOnClickListener(v->{
            isClicked=false;
            calendarLayout.setVisibility(View.GONE);
            mTransparent.setVisibility(View.GONE);

            apply.setVisibility(View.GONE);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
            businessOverviewPresenter.getOverview("range",startDate+"to"+endDate);
            calendar.setMinDate(0);
            buttonFrom.setText("FROM\n");
            buttonTo.setText("TO\n");
            period.setText(Utility.getSpecificDate(fromYear,fromMonth,fromDay)+" To "+Utility.getSpecificDate(toYear,toMonth,toDay));
        });


        buttonFrom.setOnClickListener(v->{
            isClicked=false;
            startDate="";
            endDate="";
            fromYear=0;
            fromMonth=0;
            fromDay=0;
            toYear=0;
            toMonth=0;
            toDay=0;
            apply.setVisibility(View.GONE);
            calendar.setMinDate(0);
            buttonFrom.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bar));
            buttonTo.setBackgroundColor(getResources().getColor(R.color.matt_black));
            buttonTo.setText("TO\n");
        });
    }

    private void setUpData(){

        selectionKeys=new ArrayList<>();
        selectionValues=new ArrayList<>();

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

    private void setUpSpinner(){

        ArrayAdapter<String> adapterSelection = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_remark,selectionKeys);
        adapterSelection.setDropDownViewResource(R.layout.layout_spinner_text);
        selectionSpinner.setAdapter(adapterSelection);

        selectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(selectionValues.get(position)!=0){
                    String extractedDate=Utility.periodCalculator(selectionValues.get(position));
                    period.setText(extractedDate);
                    businessOverviewPresenter.getOverview("period",String.valueOf(selectionValues.get(position)));
                }else {
                    calendarLayout.setVisibility(View.VISIBLE);
                    mTransparent.setVisibility(View.VISIBLE);

                    calendar.setDate(System.currentTimeMillis());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void onSuccessOverview(BusinessOverviewResponse businessOverviewResponse) {
        overviewDataList.clear();
        overviewDataList.addAll(businessOverviewResponse.getOverViewList());
        businessOverviewAdapter.notifyDataSetChanged();
    }

    private String setUpDate(int year,int month,int day){
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
        if (quantity < 10) {
            return "0" + quantity;
        }
        return "" + quantity;
    }

}
