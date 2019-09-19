package SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.ratna.foosip.SignIn;

/**
 * Created by Vikas on 12/22/2015.
 */
public class UserSession {
    public static final String MyPREFERENCES_SAVE = "SaveUserSession" ;
    public static final String MyPREFERENCES = "SaveUserQuesSession" ;
    public static String LOGIN = "login";
    public static String SIGNUP = "signup";
    public static String PROFILE = "profile";
    public static String QR = "qr";

    Context context;
    public UserSession(Context context)
    {
        this.context=context;
    }

    public void setUserLogIn(boolean flag)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LOGIN,flag);
        editor.commit();
    }

    public void setUserSignUp(boolean flag)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SIGNUP,flag);
        editor.commit();
    }

    public void setPROFILE(boolean flag)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(PROFILE,flag);
        editor.commit();
    }

    public void setQR(boolean flag)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(QR,flag);
        editor.commit();
    }

    public static boolean getUserLogIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(LOGIN, false);
    }

    public static boolean getUserSignUp(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(SIGNUP, false);
    }

    public static boolean getProfile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(PROFILE, false);
    }

    public static boolean getQR(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(QR, false);
    }

    public void setTimeIn(String ques_id,boolean flag)
    {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(ques_id,flag);
        editor.commit();
    }

    public static boolean getTimeIn(String ques_id,Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(ques_id,false);
    }




}
