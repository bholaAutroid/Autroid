package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.CarDetailBE;


/**
 * Created by pranav.mittal on 01/17/18.
 */

public class SelectCarAdapter extends RecyclerView.Adapter<SelectCarAdapter.CarSelectHolder> {

    Context context;
    private ArrayList<CarDetailBE> mList;
    private RadioButton lastCheckedRB = null;
    public String carId=null,carName=null, variantId=null, regNo=null, vinNo=null, engNo=null,company=null,policy=null,expire=null,policyHolder=null;
    public int premium;

    public SelectCarAdapter(ArrayList<CarDetailBE> mList, Context context){
        this.mList=mList;
        this.context=context;
    }

    @Override
    public CarSelectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.jobcard_select_car, parent, false);
        return new CarSelectHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarSelectHolder holder, final int position) {
        holder.radio.setText(mList.get(position).getTitle()+" - "+mList.get(position).getRegistration_no());

        holder.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked_rb = (RadioButton) v;
                if(lastCheckedRB != null && lastCheckedRB!=checked_rb){
                    lastCheckedRB.setChecked(false);
                }
                lastCheckedRB = checked_rb;
                carId=mList.get(position).getId();
                variantId=mList.get(position).getVariant();
                carName=mList.get(position).getTitle();
                regNo=mList.get(position).getRegistration_no();
                vinNo=mList.get(position).getVin();
                engNo=mList.get(position).getEngine_no();
                if(mList.get(position).getInsuranceDataBE()!=null) {
                    policyHolder = mList.get(position).getInsuranceDataBE().getPolicy_holder();
                    company = mList.get(position).getInsuranceDataBE().getInsurance_company();
                    policy = mList.get(position).getInsuranceDataBE().getPolicy_no();
                    if(mList.get(position).getInsuranceDataBE().getExpire()!=null)expire = mList.get(position).getInsuranceDataBE().getExpire();
                    else expire = "";
                    premium = mList.get(position).getInsuranceDataBE().getPremium();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CarSelectHolder extends RecyclerView.ViewHolder{

       RadioButton radio;
       TextView mName;

        public CarSelectHolder(View itemView) {
            super(itemView);
            radio =itemView.findViewById(R.id.rb_car_name);
            mName=itemView.findViewById(R.id.car_name);
        }
    }
}
