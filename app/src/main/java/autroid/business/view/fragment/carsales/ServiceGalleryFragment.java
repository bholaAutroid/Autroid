package autroid.business.view.fragment.carsales;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import autroid.business.R;
import autroid.business.interfaces.ServicesGalleryCallback;
import autroid.business.presenter.carsales.ServiceGalleryPresenter;
import autroid.business.utils.Constant;
import autroid.business.view.fragment.EditPicFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceGalleryFragment extends DialogFragment implements ServicesGalleryCallback {

    @BindView(R.id.services_gallery)
    RecyclerView recList;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    private ServiceGalleryPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

    }

    public ServiceGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity60)));
        return inflater.inflate(R.layout.fragment_service_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        LinearLayoutManager llm;
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        mPresenter=new ServiceGalleryPresenter(this,mMainLayout);
        mPresenter.getServiceGallery(getArguments().getString(Constant.KEY_ID),getArguments().getString(Constant.KEY_TYPE));

        /*recList.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //start youtube player activity by passing selected video id via intent
                 Intent intent=new Intent(getActivity(), YouTubeVideoActivity.class);
                    intent.putExtra("YouTubeCode","m5_AKjDdqaU");
                    getActivity().startActivity(intent);

            }
        }));*/

    }

    @Override
    public void onImageClick(String type, String fileCode) {
        EditPicFragment imageZoomFragment =new EditPicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_IMAGES,fileCode);
        bundle.putString(Constant.KEY_TYPE,"cover");
        imageZoomFragment.setArguments(bundle);
     }

    @Override
    public void onThumbnailClick(String type, String fileCode) {
      /*  Intent intent=new Intent(getActivity(), YouTubeVideoActivity.class);
        intent.putExtra("YouTubeCode", Utility.getYouTubeId(fileCode));
        getActivity().startActivity(intent);*/
    }

//    public void onSuccess(ServiceGalleryResponse baseResponse) {
//        ServiceGalleryAdapter serviceGalleryAdapter = new ServiceGalleryAdapter(getActivity(),baseResponse.getGalleryBES(),this);
//        recList.setAdapter(serviceGalleryAdapter);
//    }
}
