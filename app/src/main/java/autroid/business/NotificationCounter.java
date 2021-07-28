package autroid.business;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationCounter {

    private TextView notificationNumber;

    private final int MAx_NUMBER=99;

    private int notification_number_counter=1;

    public NotificationCounter(View view){

        notificationNumber=view.findViewById( R.id.tv_countNotification );
    }

    public void increaseNumber(){
        notification_number_counter++;

        if (notification_number_counter>=1){
            notificationNumber.setText( ""+notification_number_counter );
            notificationNumber.setVisibility( View.VISIBLE );
        }

    }

    public void resetNumber(){
        notification_number_counter=0;
        notificationNumber.setText( ""+notification_number_counter );
        notificationNumber.setVisibility( View.GONE );
    }


}
