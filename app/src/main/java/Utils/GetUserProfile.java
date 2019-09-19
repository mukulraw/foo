package Utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by Vikas on 11/3/2015.
 */
public class GetUserProfile {

    Context context;
    public GetUserProfile(Context context)
    {
        this.context=context;
    }




    public String getCountry()
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.getApplicationContext().TELEPHONY_SERVICE);
        String countryCode = tm.getNetworkCountryIso();
        return countryCode;
    }

    public String getDeviceId()
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.getApplicationContext().TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    public String getOperator()
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.getApplicationContext().TELEPHONY_SERVICE);
        String operatorName = tm.getNetworkOperatorName();
        return operatorName;
    }


}
