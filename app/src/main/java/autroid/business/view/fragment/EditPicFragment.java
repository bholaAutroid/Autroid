package autroid.business.view.fragment;


import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import autroid.business.R;
import autroid.business.utils.Constant;
import me.relex.photodraweeview.PhotoDraweeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditPicFragment extends DialogFragment {

    String url,imageType;


    private PhotoDraweeView mPhotoDraweeView;

    public EditPicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getContext(),R.color.black_opacity80)));
        return inflater.inflate(R.layout.fragment_edit_pic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPhotoDraweeView = (PhotoDraweeView) view.findViewById(R.id.photo_drawee_view);

        Bundle bundle=getArguments();
        if(bundle!=null) {
            url =bundle.getString(Constant.KEY_IMAGES);
            imageType =bundle.getString(Constant.KEY_TYPE);
            mPhotoDraweeView.setPhotoUri(Uri.parse(url));
        }

    }


}
