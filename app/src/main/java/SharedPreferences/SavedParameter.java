package SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import Events.MenuEvent;
import Model.Tax;


/**
 * Created by Vikas on 12/6/2015.
 */
public class SavedParameter {

    public static final String MyPREFERENCES_SAVE = "SaveParam";
    public static String OTP = "otp";
    public static String MOBILE = "mobileno";
    public static String FIRST_NAME = "first_name";
    public static String EMAIL = "email";
    public static String RID = "r_id";
    public static String TOKEN = "token_id";
    public static String UID = "user_id";
    public static String QUEUE = "queue";
    public static String TEMP_ORDER_ID = "temp_order_id";
    public static String QR_CODE = "qr_code";
    public static String MENUEVENT_OBJ = "menu_event_obj";
    public static String TAX_OBJ = "tax_obj";
    public static String SUB_USER = "sub_user";
    public static String MAIN_USER = "main_user";

    public static String USERNAME="user_name";
    public static String PASSWORD="pass";

    public static String  ISCHAT="ischat";

    public static String DATESET = "dateset";




    Context context;

    public SavedParameter(Context context) {
        this.context = context;
    }

    public static String getMobile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(MOBILE, "");
    }

    public static String getOTP(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(OTP, "");
    }

    public static String getrId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(RID, "");
    }





    public static String getUID(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(UID, "");
    }

    public static Boolean getQUEUE(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(QUEUE, false);
    }



    public static String getFirstName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(FIRST_NAME, "");
    }

    public static String getEMAIL(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(EMAIL, "");
    }

    public static String getUSERNAME(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(USERNAME, "");
    }


    public static String getPASSWORD(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(PASSWORD, "");
    }

    public static String getSaveApi(Context context,String key) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(key,"");
    }

    public static String getTempOrderId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(TEMP_ORDER_ID,"");
    }

    public static String getMenueventObj(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        String json = prefs.getString(MENUEVENT_OBJ, "");
        return json;
    }

    public static String getTaxObj(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        String json = prefs.getString(TAX_OBJ, "");
        return json;
    }

    public static boolean getSubUser (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(SUB_USER, false);
    }

    public static boolean getMainUser (Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(MAIN_USER, false);
    }


    public static boolean getIsChat (String key,Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public String getQrCode() {
        SharedPreferences prefs = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return prefs.getString(QR_CODE, "");
    }





    public void setMOBILE(String mobile) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(MOBILE, mobile);
        editor.apply();
    }

    public void setOTP(String password) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(OTP, password);
        editor.apply();
    }

    public void setrId(String rId) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RID, rId);
        editor.apply();
    }

    public String getRID() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(RID , "");
    }

    public void setTOKEN(String token) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getTOKEN() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(TOKEN , "");
    }

    public void setUID(String uid) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(UID, uid);
        editor.apply();
    }

    public String getUID() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(UID , "");
    }

    public String getDATESET() {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        return sharedpreferences.getString(DATESET , "");
    }

    public void setQUEUE(Boolean queue) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(QUEUE, queue);
        editor.apply();
    }



    public void setFirstName(String firstName) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(FIRST_NAME, firstName);
        editor.apply();
    }

    public void setEMAIL(String email) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void setSaveApi(String key,String api) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, api);
        editor.apply();
    }

    public void setTempOrderId(String tempOrderId) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(TEMP_ORDER_ID, tempOrderId);
        editor.apply();
    }

    public void setMenueventObj(MenuEvent menueventObj) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(menueventObj);
        editor.putString(MENUEVENT_OBJ, json);
        editor.apply();
    }

    public void setTaxObj(Tax taxObj) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taxObj);
        editor.putString(TAX_OBJ, json);
        editor.apply();
    }

    public void setSubUser(boolean sub_user) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(SUB_USER, sub_user);
        editor.apply();
    }

    public void setDATESET(String date) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DATESET, date);
        editor.apply();
    }


    public void setISCHAT(String key,boolean ischat) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(key, ischat);
        editor.apply();
    }


    public void setMain_user(boolean main_user) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(MAIN_USER, main_user);
        editor.apply();
    }

    public void setQrCode(String qrCode) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(QR_CODE, qrCode);
        editor.apply();
    }

    public void setUSERNAME(String username) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.apply();
    }


    public void setPASSWORD(String password) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES_SAVE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }


}
