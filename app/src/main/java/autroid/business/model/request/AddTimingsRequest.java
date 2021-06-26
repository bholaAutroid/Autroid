package autroid.business.model.request;

import java.util.ArrayList;

import autroid.business.model.bean.TimingsBE;

/**
 * Created by pranav.mittal on 06/26/17.
 */

public class AddTimingsRequest  {
    private ArrayList<TimingsBE> timing;

    public ArrayList<TimingsBE> getTiming() {
        return timing;
    }

    public void setTiming(ArrayList<TimingsBE> timing) {
        this.timing = timing;
    }
}
