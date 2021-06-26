package autroid.business.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class AutoReadOtp extends BroadcastReceiver {

    public static String otp=null;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())){
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    otp=(String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Toast.makeText(context,"Otp is"+otp,Toast.LENGTH_SHORT).show();
                    break;
                case CommonStatusCodes.TIMEOUT:
                    Toast.makeText(context,"OTP not received...",Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    }
}
