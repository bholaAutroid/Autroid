package autroid.business.view.fragment;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.model.request.AddReviewRequest;
import autroid.business.model.response.BaseResponse;
import autroid.business.presenter.ShowroomReviewPresenter;
import autroid.business.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShowroomReviewFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.toolbar_title)
    TextView mName;
    @BindView(R.id.model_rating)
    AppCompatRatingBar mRating;
    @BindView(R.id.model_comment)
    EditText mComment;
    @BindView(R.id.btn_done)
    Button mSubmit;

    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    String strBusiness,strId,strComment;
    float ratings;
    ShowroomReviewPresenter mPresenter;

    public AddShowroomReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_showroom_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        mPresenter=new ShowroomReviewPresenter(this,mMainLayout);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().onBackPressed();
            }
        });

        Bundle bundle=getArguments();
        if(bundle!=null){
            strBusiness=bundle.getString(Constant.Key_Business_Name);
            strId=bundle.getString(Constant.KEY_ID);

            mName.setText(strBusiness);
        }

        mSubmit.setOnClickListener(this);



    }

    public void onSuccess(BaseResponse baseResponse) {
        Toast.makeText(getActivity(), baseResponse.getResponseMessage(), Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_done:
                if(validate()){
                    AddReviewRequest addReviewRequest=new AddReviewRequest();
                    addReviewRequest.setBusiness_id(strId);
                    addReviewRequest.setRating(ratings);
                    addReviewRequest.setReview(strComment);
                    addReviewRequest.setType(Constant.VALUE_SHOWROOM);
                    mPresenter.addReview(addReviewRequest);
                }
                break;
        }
    }

    private boolean validate(){
        boolean flag=true;
        strComment=mComment.getText().toString();
        ratings=mRating.getRating();

        if(ratings==0){
            flag=false;
        }

        return flag;
    }

    /*/add-model-review
{
"id": model_id,
"review": "sdsd",
"rating": 5,
}*/
}
