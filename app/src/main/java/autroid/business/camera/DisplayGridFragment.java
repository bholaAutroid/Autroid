package autroid.business.camera;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.InspectionImageBE;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;
import autroid.business.view.fragment.EditPicFragment;

public class DisplayGridFragment extends Fragment implements RecyclerViewListener {

    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private ArrayList<InspectionImageBE> arrayList;
    private TextView title;
    LinearLayout linear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_grid,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        title=view.findViewById(R.id.toolbar_title);
        title.setText("Gallery");

        linear=view.findViewById(R.id.linear);

        arrayList= (ArrayList<InspectionImageBE>) getArguments().getSerializable("inspection_list");
        imageAdapter = new ImageAdapter(arrayList,this);
        recyclerView=view.findViewById(R.id.grid_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onItemImage(String url,int index) {

        if(index==-1){
            Utility.showResponseMessage(linear,"This image is not uploaded");
            return;
        }

        EditPicFragment editPicFragment=new EditPicFragment();
        Bundle bundle1=new Bundle();
        bundle1.putString(Constant.KEY_IMAGES,url);
        bundle1.putString(Constant.KEY_TYPE,"cover");
        editPicFragment.setArguments(bundle1);
        editPicFragment.show(getFragmentManager(),"EditPicFragment");
    }

    @Override
    public void onImageDelete(String id, int position) {

    }

    @Override
    public void onDocumentClick(String fileUrl, int position, String documentType) {

    }

    @Override
    public void onDocumentDelete(String id, int position) {

    }
}
