package autroid.business.view.fragment.booking;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.booking.ReviewPointsAdapter;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.model.bean.RatingDataBE;
import autroid.business.model.request.ReviewRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.model.response.ReviewResponse;
import autroid.business.presenter.BookingReviewPresenter;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.aws.AwsHomeActivity;

public class BookingReviewFragment extends Fragment {

    LinearLayout mainLayout,recommendation;

    BookingReviewPresenter bookingReviewPresenter;

    AppCompatRatingBar ratingBar,recommdationRatingBar;

    Button submit;

    String bookingId,status="";

    float ratingGiven,recommendationRatingGiven;

    ArrayList<String> ratingList;

    RecyclerView recyclerView;

    ReviewPointsAdapter reviewAdapter;

    ReviewResponse reviewResponse;

    RadioGroup radioGroup;

    EditText suggestion;

    RadioButton satisfied,disSatisfied,reWork;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booking_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        findViews(view);

        bookingId=getArguments().getString(Constant.BOOKING_ID);

        ratingBar.setVisibility(View.GONE);
        recommdationRatingBar.setVisibility(View.GONE);


        bookingReviewPresenter=new BookingReviewPresenter(this,mainLayout);
        bookingReviewPresenter.getReviewQuestions(bookingId);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {

                ratingGiven=rating;

                recyclerView.setVisibility(View.VISIBLE);
                recommendation.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);
                suggestion.setVisibility(View.VISIBLE);


                if(rating<1.0){
                    recyclerView.setVisibility(View.GONE);
                    recommendation.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.GONE);
                    suggestion.setVisibility(View.GONE);
                }else {
                    recyclerView.setAdapter(null);
                    ratingList.clear();

                    for (RatingDataBE data: reviewResponse.getRatingData()) if(data.getRating()==Math.round(rating)) ratingList.addAll(data.getPointsData());

                    reviewAdapter =new ReviewPointsAdapter(ratingList);
                    recyclerView.setAdapter(reviewAdapter);
                }
            }
        });

        recommdationRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                recommendationRatingGiven=rating;
            }
        });

    }

    private void findViews(View view){

        ratingList =new ArrayList<>();

        reviewAdapter =new ReviewPointsAdapter(ratingList);
        mainLayout=view.findViewById(R.id.main_layout);
        ratingBar=view.findViewById(R.id.booking_rating);
        recommdationRatingBar=view.findViewById(R.id.recommend_rating);
        submit=view.findViewById(R.id.submit);
        recyclerView=view.findViewById(R.id.rating_points_list);
        recommendation=view.findViewById(R.id.recommend_to_friend);
        suggestion=view.findViewById(R.id.suggestion);
        radioGroup=view.findViewById(R.id.radio_group);
        satisfied=view.findViewById(R.id.satisfied);
        disSatisfied=view.findViewById(R.id.dis_satisfied);
        reWork=view.findViewById(R.id.rework);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        satisfied.setOnClickListener(v->{
            if(satisfied.isChecked())status="Satisfied";
        });

        disSatisfied.setOnClickListener(v->{
            if(disSatisfied.isChecked())status="Dissatisfied";
        });

        reWork.setOnClickListener(v->{
            if(reWork.isChecked())status=reWork.getText().toString();
        });


        submit.setOnClickListener(v->{
            if(validate())bookingReviewPresenter.setReviews(createReviewRequest());
        });
    }

    public void onSuccessReview(ReviewResponse reviewResponse) {

        submit.setVisibility(View.VISIBLE);
        this.reviewResponse=reviewResponse;
        ratingBar.setVisibility(View.VISIBLE);
        recommdationRatingBar.setVisibility(View.VISIBLE);

    }

    private ReviewRequest createReviewRequest() {

        ReviewRequest reviewRequest=new ReviewRequest();

        reviewRequest.setBooking(bookingId);
        reviewRequest.setRating(ratingGiven);
        reviewRequest.setRecommendation(recommendationRatingGiven);
        reviewRequest.setStatus(status);
        reviewRequest.setReview(suggestion.getText().toString().trim());
        reviewRequest.setPoints(setCheckPoints(ratingList, reviewAdapter));

        return reviewRequest;
    }

    private ArrayList<String> setCheckPoints(ArrayList<String> arrayList,ReviewPointsAdapter reviewPointsAdapter){

        ArrayList<String> list=new ArrayList<>();

        for (int i=0;i<arrayList.size();i++){
            if(reviewPointsAdapter.trueList.get(i)){
                list.add(arrayList.get(i));
            }
        }
        return list;
    }

    private boolean validate(){

        if(ratingGiven<1.0){
            Utility.showResponseMessage(mainLayout,"Please give a rating");
            return false;
        }else if(recommendationRatingGiven<1.0){
            Utility.showResponseMessage(mainLayout,"Give recommendation rating");
            return false;
        }else if(status.trim().length()==0){
            Utility.showResponseMessage(mainLayout,"Select satisfaction status");
            return false;
        }

        return true;
    }

    public void onSuccessReviewAdded(BaseResponse response) {
        Toast.makeText(getActivity(),response.getResponseMessage(),Toast.LENGTH_LONG).show();
        ((AwsHomeActivity)getActivity()).clearStackLocalAndShowLeads();
    }
}
