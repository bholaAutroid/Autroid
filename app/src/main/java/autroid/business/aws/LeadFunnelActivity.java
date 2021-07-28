package autroid.business.aws;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

import autroid.business.R;

public class LeadFunnelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_lead_funnel );


        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);
        Pie pie = AnyChart.pie();
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Justdial", 20));
        data.add(new ValueDataEntry("Walk-In", 30));
        data.add(new ValueDataEntry("Mobile App", 30));
        pie.data(data);
        anyChartView.setChart(pie);


        AnyChartView anyChartView1 = findViewById(R.id.any_chart_view1);
        APIlib.getInstance().setActiveAnyChartView(anyChartView1);
        Pie pie1 = AnyChart.pie();
        List<DataEntry> data1 = new ArrayList<>();
        data1.add(new ValueDataEntry("Justdial", 200));
        data1.add(new ValueDataEntry("Walk-In", 200));
        data1.add(new ValueDataEntry("Mobile App", 100));


        pie1.data(data1);
        anyChartView1.setChart(pie1);

    }
}