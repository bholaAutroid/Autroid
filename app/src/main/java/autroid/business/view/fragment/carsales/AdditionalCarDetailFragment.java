package autroid.business.view.fragment.carsales;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.utils.Constant;

public class AdditionalCarDetailFragment extends Fragment {

    TextView purchasePrice,refurbishmentPrice,sellingPrice,registrationNumber;

    ImageView backNavigation;

    public AdditionalCarDetailFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_additional_car_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        purchasePrice=view.findViewById(R.id.purchased_price);
        refurbishmentPrice=view.findViewById(R.id.refurbishment_price);
        sellingPrice=view.findViewById(R.id.selling_price);
        backNavigation=view.findViewById(R.id.back_navigation);
        registrationNumber=view.findViewById(R.id.car_registration);

        getBundleData();

        backNavigation.setOnClickListener(v-> getActivity().onBackPressed());

    }

    private void getBundleData(){

        Bundle data=getArguments();

        purchasePrice.setText("₹ "+data.getFloat(Constant.PURCHASE_PRICE));
        refurbishmentPrice.setText("₹ "+data.getFloat(Constant.REFURBISHMENT_PRICE));
        sellingPrice.setText("₹ "+data.getFloat(Constant.SELLING_PRICE));
        registrationNumber.setText(data.getString(Constant.VALUE));
    }

}
