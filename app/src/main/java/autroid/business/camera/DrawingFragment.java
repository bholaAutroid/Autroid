package autroid.business.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

//import com.qiscus.nirmana.Nirmana;

import autroid.business.R;
import autroid.business.eventbus.Events;
import autroid.business.eventbus.GlobalBus;
import autroid.business.utils.Constant;
import autroid.business.utils.Utility;

public class DrawingFragment extends Fragment implements View.OnTouchListener {

    private DrawingView still_shot_container;

    private ImageButton undo;

    private ImageView send;

    private boolean imageSelectedCarSales, imageCapturedCarSales;

    private byte bytes[];

    private Bitmap image_shot;

    private LinearLayout linearLayout;

    private RelativeLayout mainLayout;

    private EditText fileName;

    private Uri imageUri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawing_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        still_shot_container = view.findViewById(R.id.still_shot_container);
        undo = view.findViewById(R.id.undo_icon);
        send = view.findViewById(R.id.send);
        linearLayout = view.findViewById(R.id.field_message_container);
        fileName = view.findViewById(R.id.file_name);
        mainLayout = view.findViewById(R.id.main_layout);


        imageSelectedCarSales = getArguments().getBoolean(Constant.IS_IMAGE_SELECTED_CAR_SALES);
        imageCapturedCarSales = getArguments().getBoolean(Constant.IS_IMAGE_CAPTURED_CAR_SALES);

//        if (imageSelectedCarSales) {
//            if(getArguments().getString(Constant.IMAGE_URI).contains("image")) Nirmana.getInstance().get().load(Uri.parse(getArguments().getString(Constant.IMAGE_URI))).into(still_shot_container);
//            else Nirmana.getInstance().get().load(R.drawable.ic_document).into(still_shot_container);
//            still_shot_container.setShotAvailable(false);
//            linearLayout.setVisibility(View.VISIBLE);
//            undo.setVisibility(View.GONE);
//        } else if (imageCapturedCarSales) {
//            Nirmana.getInstance().get().load(getArguments().getString(Constant.VALUE)).into(still_shot_container);
//            still_shot_container.setShotAvailable(false);
//            linearLayout.setVisibility(View.VISIBLE);
//            undo.setVisibility(View.GONE);
//        } else {
//            bytes = getArguments().getByteArray("byte_array");
//            image_shot = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            Nirmana.getInstance().get().load(image_shot).into(still_shot_container);
//            still_shot_container.setShotAvailable(true);
//        }

        undo.setOnClickListener(v -> still_shot_container.undo());

        send.setOnClickListener(v -> {

            if ((imageSelectedCarSales || imageCapturedCarSales) && fileName.getText().toString().trim().length() == 0) {
                Utility.showResponseMessage(mainLayout, "Please enter file name");
            } else if (imageSelectedCarSales) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_IMAGE_SELECTED_CARSALE);
                broadcastIntent.putExtra(Constant.VALUE, fileName.getText().toString().trim());
                Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                GlobalBus.getBus().post(sendEvent);
                getActivity().onBackPressed();
            }else if (imageCapturedCarSales) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_IMAGE_CAPTURED_CARSALE);
                broadcastIntent.putExtra(Constant.VALUE, fileName.getText().toString().trim());
                Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                GlobalBus.getBus().post(sendEvent);
                getActivity().onBackPressed();
            }  else {
                Bitmap bitmap = Bitmap.createBitmap(still_shot_container.getDrawingCache());
                Intent broadcastIntent = new Intent();
                broadcastIntent.putExtra(Constant.KEY_EVENT_ID, Constant.EVENT_GET_IMAGE);
                broadcastIntent.putExtra("bitmap", bitmap);
                Events.SendEvent sendEvent = new Events.SendEvent(broadcastIntent);
                GlobalBus.getBus().post(sendEvent);
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        still_shot_container.onTouchEvent(motionEvent);
        return false;
    }
}
