package autroid.business.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.adapter.SelectMultiImageAdapter;
import autroid.business.interfaces.OnImageDeleteCallback;
import autroid.business.service.UploadMultipleImagesService;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

import static autroid.business.R.id.btnSelect;

public class AddMultipleImagesActivity  extends AppCompatActivity implements View.OnClickListener,OnImageDeleteCallback {

    Button mSelectImages,mUploadImage;
    private static  int REQUEST_CODE_PICKER=100;
    RecyclerView recList;
    SelectMultiImageAdapter mSelectMultiImageAdapter;
    ArrayList<Image> images=new ArrayList<>();
    String keyValue,id;
    TextView tvMSG;
    RelativeLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multiple_images);
        mSelectImages= (Button) findViewById(btnSelect);
        mSelectImages.setOnClickListener(this);
        mUploadImage= (Button) findViewById(R.id.btnUploadImages);
        mUploadImage.setOnClickListener(this);
        tvMSG= (TextView) findViewById(R.id.select_images_text);
        mMainLayout= (RelativeLayout) findViewById(R.id.main_layout);

        recList= (RecyclerView) findViewById(R.id.images_list);

        Intent intent=getIntent();
        if(intent!=null){
            id=intent.getStringExtra(Constant.KEY_ID);
            keyValue=intent.getStringExtra(Constant.KEY_TYPE);
        }

        TextView tvTitle= (TextView) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar_title);
        tvTitle.setText("Upload Multiple Images");

        Toolbar toolbar = (Toolbar) findViewById(R.id.common_toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        checkReadPermission();
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

    private void checkReadPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
                return;
            }
            else{
                selectImages();
            }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.
                    selectImages();


                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    String permission=permissions[0];
                    boolean showRationale = shouldShowRequestPermissionRationale( permission );
                    if (! showRationale) {
                        Toast.makeText(getApplicationContext(),permission,Toast.LENGTH_SHORT).show();
                    }
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private void selectImages() {
        ImagePicker.create(this)
                // set whether pick or camera action should return immediate result or not. For pick image only work on single mode
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Select Image") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                // original selected images, used in multi mode
                .theme(R.style.ef_BaseTheme) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log// custom image loader, must be serializeable
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            this.images.addAll((ArrayList<Image>) ImagePicker.getImages(data));
            //ArrayList<Image> images = (ArrayList<Image>) ImagePicker.getImages(data);
            if(images.size()>0)
                tvMSG.setVisibility(View.GONE);
            this.images=images;
            Log.e("size",images.size()+"");

            GridLayoutManager llm;
            llm = new GridLayoutManager(getApplicationContext(),2);
            recList.setLayoutManager(llm);

            mSelectMultiImageAdapter=new SelectMultiImageAdapter(getApplicationContext(),images,this);
            recList.setAdapter(mSelectMultiImageAdapter);



        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSelect:
                checkReadPermission();
                break;
            case R.id.btnUploadImages:
                if(images!=null)
                if(images.size()>0)
                   callServiceImageUpload();
                break;
        }
    }

    private void  callServiceImageUpload(){
        Utility.showResponseMessage(mMainLayout,getString(R.string.images_uploading));
        UploadMultipleImagesService.startService(getApplicationContext(), images,id,keyValue,false);
        finish();
    }

    @Override
    public void onDeleteClick(String id) {
        if(mSelectMultiImageAdapter!=null) {
            mSelectMultiImageAdapter.images.remove(Integer.parseInt(id));
            mSelectMultiImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteImageClickId(String id) {

    }

    @Override
    public void onAddImageClick(int position) {

    }
}
