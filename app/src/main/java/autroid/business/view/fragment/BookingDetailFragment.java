package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

import autroid.business.R;
import autroid.business.adapter.BookingReviewAdapter;
import autroid.business.model.realm.SelectedBookingDataRealm;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingDetailFragment extends BottomSheetDialogFragment {

    @BindView(R.id.booking_list)
    RecyclerView mBookingList;

    String bookingId;
    RealmController mRealmController;
    Realm mRealm;

    public BookingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        this.mRealm = RealmController.with(getActivity()).getRealm();
        mRealmController=RealmController.getInstance();


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mBookingList.setLayoutManager(llm);

        bookingId=getArguments().getString(Constant.KEY_ID);

        RealmList<SelectedBookingDataRealm> selectedBookingDataRealm=mRealmController.getBookingsById(bookingId);

        BookingReviewAdapter bookingReviewAdapter=new BookingReviewAdapter(selectedBookingDataRealm,true,getActivity());
        mBookingList.setAdapter(bookingReviewAdapter);
    }
}
