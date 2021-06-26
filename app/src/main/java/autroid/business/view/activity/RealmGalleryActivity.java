package autroid.business.view.activity;

import android.app.Application;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.Gravity;

import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import autroid.business.MyApplication;
import autroid.business.R;
import autroid.business.adapter.RealmGalleryAdapter;
import autroid.business.model.realm.CarRealm;
import autroid.business.model.realm.MediaRealm;
import autroid.business.model.realm.ProductRealm;
import autroid.business.model.realm.ShowroomRealm;
import autroid.business.realm.RealmController;
import autroid.business.utils.Constant;
import io.realm.Realm;
import io.realm.RealmList;

public class RealmGalleryActivity extends AppCompatActivity {

    RecyclerView mGallery;
    RealmList<MediaRealm> mediaRealms;
    RealmGalleryAdapter mRealmGalleryAdapter;

    private Realm realm;
    RealmController realmController;

    private String strType,strId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_gallery);

        this.realm = RealmController.with(this).getRealm();
        Application appCtx = (MyApplication) getApplication();
        realmController=new RealmController(appCtx);

        mGallery=findViewById(R.id.gallery);
        LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getApplicationContext());
        llmGallery.setOrientation(LinearLayoutManager.VERTICAL);

        // recListGallery.addItemDecoration(new GridSpacingItemDecoration(1,spacing,true));
        mGallery.setLayoutManager(llmGallery);

        SnapHelper helperCars = new GravitySnapHelper(Gravity.START);
        helperCars.attachToRecyclerView(mGallery);

        strType=getIntent().getStringExtra(Constant.KEY_TYPE);
        if(strType.equals(Constant.VALUE_PRODUCT)){
            ProductRealm productRealm=realmController.getProduct(getIntent().getStringExtra(Constant.KEY_ID));
            mediaRealms=productRealm.getMedia();

        }
        if(strType.equals(Constant.VALUE_CAR)){
            CarRealm carRealm=realmController.getCar(getIntent().getStringExtra(Constant.KEY_ID));
            mediaRealms=carRealm.getMedia();

        }
        if(strType.equals(Constant.VALUE_SHOWROOM)){
            ShowroomRealm showroomRealm=realmController.getShowroomData();
            mediaRealms=showroomRealm.getMedia();
        }
        mRealmGalleryAdapter=new RealmGalleryAdapter(mediaRealms,true);
        mGallery.setAdapter(mRealmGalleryAdapter);

    }
}
