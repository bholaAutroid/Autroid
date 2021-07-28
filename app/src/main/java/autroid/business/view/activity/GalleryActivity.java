package autroid.business.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import autroid.business.adapter.GalleryAdapter;
import autroid.business.R;
import autroid.business.interfaces.GalleryImageClickCallback;
import autroid.business.interfaces.OnRealmImageClickCallback;
import autroid.business.model.bean.ThumbnailBE;
import autroid.business.utils.Constant;

public class GalleryActivity extends AppCompatActivity implements OnRealmImageClickCallback, GalleryImageClickCallback {

    RecyclerView mGallery;
    ArrayList<ThumbnailBE> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mGallery = (RecyclerView) findViewById(R.id.post_images);
        final LinearLayoutManager llmGallery;
        llmGallery = new LinearLayoutManager(getApplicationContext());
        llmGallery.setOrientation(LinearLayoutManager.VERTICAL);

        mGallery.setLayoutManager(llmGallery);

////    mImages= (ArrayList<ThumbnailBE>) getIntent().getSerializableExtra(Constant.KEY_IMAGES);



        TextView tvTitle= (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Gallery");

        Toolbar toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action l item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public void onGalleryClick(ArrayList<ThumbnailBE> mImages) {
//        mGallery.setAdapter(new GalleryAdapter(getApplicationContext(),mImages,this));

//        this.mImages=mImages;
    }
}
