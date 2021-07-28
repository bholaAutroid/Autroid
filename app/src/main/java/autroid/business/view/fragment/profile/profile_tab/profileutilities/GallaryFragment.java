package autroid.business.view.fragment.profile.profile_tab.profileutilities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.GalleryAdapter;
import autroid.business.interfaces.GalleryImageClickCallback;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.ThumbnailBE;
import autroid.business.utils.Constant;
import autroid.business.view.activity.GalleryActivity;
import autroid.business.aws.AwsHomeActivity;
import autroid.business.view.fragment.payment.PaytmPaymentFragment;

public class GallaryFragment extends Fragment implements GalleryImageClickCallback, OnRealmImageClickCallback {

    RecyclerView mGallery;

    public GallaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallary, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );

        mGallery = (RecyclerView) view.findViewById(R.id.post_images);
        final LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getActivity());
        llmGallery.setOrientation(LinearLayoutManager.VERTICAL);

        mGallery.setLayoutManager(llmGallery);
    }

    @Override
    public void onGalleryClick(ArrayList<ThumbnailBE> mImages) {
        mGallery.setAdapter(new GalleryAdapter(getActivity(),mImages,this));

    }
   public void goToact(){
//        startActivity( new Intent(getActivity(), GalleryActivity.class ) );
   }

    @Override
    public void onImageClick(int pos, String id) {

    }

    @Override
    public void onDetailClick(int pos, String des) {

    }

    @Override
    public void onDetailClick(String id, String type) {

    }

    @Override
    public void onTraveloguePagerClick(int pos, String id) {

    }
}