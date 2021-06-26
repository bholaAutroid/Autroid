package autroid.business.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OverviewBE {

    private String group;

    @SerializedName("data")
    private ArrayList<OverviewDataBE> overviewData;

    public String getGroup() {
        return group;
    }

    public ArrayList<OverviewDataBE> getOverviewData() {
        return overviewData;
    }
}
