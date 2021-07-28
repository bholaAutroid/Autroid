package autroid.business.aws.navigation.adapter;

import android.content.Context;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import autroid.business.R;

public class ExpandableListDataSource {

    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        List<String> filmGenres = Arrays.asList(context.getResources().getStringArray( R.array.enginee));

        List<String> management = Arrays.asList(context.getResources().getStringArray(R.array.Management));
        List<String> crm = Arrays.asList(context.getResources().getStringArray(R.array.CRM));
        List<String> wms= Arrays.asList(context.getResources().getStringArray(R.array.WMS));
        List<String> purchase = Arrays.asList(context.getResources().getStringArray(R.array.Purchase));
        List<String> master = Arrays.asList(context.getResources().getStringArray(R.array.Master));
        List<String> sales = Arrays.asList(context.getResources().getStringArray(R.array.Sale));
        List<String> accounts = Arrays.asList(context.getResources().getStringArray(R.array.Accounts));

        expandableListData.put(filmGenres.get(0), management);
        expandableListData.put(filmGenres.get(1), crm);
        expandableListData.put(filmGenres.get(2), wms);
        expandableListData.put(filmGenres.get(3), purchase);
        expandableListData.put(filmGenres.get(4), master);
        expandableListData.put(filmGenres.get(5), sales);
        expandableListData.put(filmGenres.get(6), accounts);

        return expandableListData;
    }
}
