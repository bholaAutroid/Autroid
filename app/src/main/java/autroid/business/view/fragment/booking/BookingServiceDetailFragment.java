package autroid.business.view.fragment.booking;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.adapter.booking.BookingServiceDetailAdapter;
import autroid.business.model.bean.ServiceBE;
import autroid.business.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingServiceDetailFragment extends Fragment {

    private ServiceBE mServiceBE;
    @BindView(R.id.parts_list)
    RecyclerView mPartsList;
    @BindView(R.id.labour_list)
    RecyclerView mLabourList;
    @BindView(R.id.fitting_list)
    RecyclerView mFittingList;

    @BindView(R.id.ll_parts)
    LinearLayout mPartsLayout;
    @BindView(R.id.ll_labour)
    LinearLayout mLabourLayout;
    @BindView(R.id.ll_fitting)
    LinearLayout mFittingLayout;

    public BookingServiceDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_service_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mPartsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLabourList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFittingList.setLayoutManager(new LinearLayoutManager(getActivity()));

        mServiceBE= (ServiceBE) getArguments().getSerializable(Constant.KEY_TYPE);

        if(mServiceBE.getParts().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailPart = new BookingServiceDetailAdapter(mServiceBE.getParts(),false);
            mPartsList.setAdapter(bookingServiceDetailPart);
        }else {
            mPartsLayout.setVisibility(View.GONE);
        }

        if(mServiceBE.getLabour().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailLabour=new BookingServiceDetailAdapter(mServiceBE.getLabour(),false);
            mLabourList.setAdapter(bookingServiceDetailLabour);
        }else {
            mLabourLayout.setVisibility(View.GONE);
        }
        if(mServiceBE.getOpening_fitting().size()>0) {
            BookingServiceDetailAdapter bookingServiceDetailFitting=new BookingServiceDetailAdapter(mServiceBE.getOpening_fitting(),false);
            mFittingList.setAdapter(bookingServiceDetailFitting);
        }else {
                mFittingLayout.setVisibility(View.GONE);
        }

    }
}
