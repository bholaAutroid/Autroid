package autroid.business.adapter.leadgeneration;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import autroid.business.R;
import autroid.business.interfaces.MainSearchListener;
import autroid.business.model.realm.InsuranceDueRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class InsuranceDueAdapter extends RealmRecyclerViewAdapter<InsuranceDueRealm, InsuranceDueAdapter.MyViewHolder> {

    Context context;

    MainSearchListener insuranceDueListener;

    ConstraintLayout mainLayout;

    public InsuranceDueAdapter(@Nullable OrderedRealmCollection<InsuranceDueRealm> data, boolean autoUpdate,MainSearchListener insuranceDueListener) {
        super(data, autoUpdate);
        this.insuranceDueListener=insuranceDueListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, name, registrationNo, expire;

        InsuranceDueRealm insuranceDueRealm;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            title = itemView.findViewById(R.id.title);
            name = itemView.findViewById(R.id.name);
            registrationNo = itemView.findViewById(R.id.car_registration_no);
            expire = itemView.findViewById(R.id.expiry_date);
            mainLayout = itemView.findViewById(R.id.main_layout);

        }

        public void bind(int position) {

            insuranceDueRealm = getItem(position);

            title.setText(insuranceDueRealm.getTitle());
            name.setText(insuranceDueRealm.getUserName());
            registrationNo.setText(insuranceDueRealm.getRegistrationNo());

            if(insuranceDueRealm.getExpire()!=null) expire.setText("Expires: "+insuranceDueRealm.getExpire());

            mainLayout.setOnClickListener(v-> insuranceDueListener.onClickSearchItem(insuranceDueRealm.getId(),null,null));

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_insurance_due, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

}
