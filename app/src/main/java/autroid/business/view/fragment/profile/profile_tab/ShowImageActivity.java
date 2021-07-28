package autroid.business.view.fragment.profile.profile_tab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import autroid.business.R;

public class ShowImageActivity extends AppCompatActivity {

    ImageView ivShowFullImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_show_image );

        ivShowFullImg=findViewById( R.id.showImage );

        Intent intent = getIntent();
        String image = intent.getStringExtra("imageId");

        if (intent !=null) {
            Picasso.with( getApplicationContext() ).load( image ).fit().placeholder( R.drawable.placeholder_big ).into( ivShowFullImg );
        }
    }
}