package Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by online on 10/26/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private final String TAG = "DatabaseHelperClass";
    private static final int databaseVersion = 1;
    private static final String databaseName = "db_foosip";
    private static final String TABLE_MENU = "menutable";
    private static final String TABLE_ITEM = "itemtable";


    // Image Table Columns names


    private static final String MENU_NAME = "menu_nm";
    private static final String MENU_ID = "menu_id";
    private static final String SUB_CAT_ID = "sub_cat_id";
    private static final String SUB_CAT_NAME="sub_cat_name";
    private static final String SUB_CAT_PHOTO = "sub_cat_photo";
    private static final String SUB_CAT_STATUS="sub_cat_status";
    private static final String SUB_CAT_CATEGORY="sub_cat_category";
    private static final String ITEMS_NAME="item_name";
    private static final String ITEMS_PRICE = "item_price";
    private static final String ITEMS_ID = "item_id";
    private static final String ITEMS_QUANTITY = "item_quantity";





    public DatabaseHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_MENU + "("
                + MENU_NAME + " TEXT ,"
                + MENU_ID  + " TEXT ,"
                + SUB_CAT_ID  + " TEXT ,"
                + SUB_CAT_NAME  + " TEXT ,"
                + SUB_CAT_PHOTO  + " TEXT ,"
                + SUB_CAT_STATUS  + " TEXT ,"
                + SUB_CAT_CATEGORY  + " TEXT ,"
                + ITEMS_ID  + " TEXT ,"
                + ITEMS_NAME  + " TEXT ,"
                + ITEMS_PRICE +" TEXT )";

        String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_ITEM + "("
                + ITEMS_ID + " TEXT ,"
                + ITEMS_NAME  + " TEXT ,"
                + ITEMS_QUANTITY  + " TEXT ,"
                + ITEMS_PRICE +" TEXT )";


        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(sqLiteDatabase);
    }

    public void insertProduct(String menu_name,String menu_id,String sub_cat_id,String sub_cat_name,String sub_cat_photo,String sub_cat_status,
                              String sub_cat_category,String item_id,String item_name,String item_price)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(MENU_NAME, menu_name);
        contentValues.put(MENU_ID,  menu_id);
        contentValues.put(SUB_CAT_ID,       sub_cat_id);
        contentValues.put(SUB_CAT_NAME,      sub_cat_name);
        contentValues.put(SUB_CAT_PHOTO,     sub_cat_photo);
        contentValues.put(SUB_CAT_STATUS,      sub_cat_status);
        contentValues.put(SUB_CAT_CATEGORY,   sub_cat_category);
        contentValues.put(ITEMS_ID,      item_id);
        contentValues.put(ITEMS_NAME,      item_name);
        contentValues.put(ITEMS_PRICE,      item_price);



        database.insert(TABLE_MENU, null, contentValues);
        System.out.println("insert data in displaylog");

    }
    public  ArrayList<HashMap<String,String>>  getProduct(String menu_nm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
   //     Cursor cursor = db.rawQuery("select menu_nm,menu_id,sub_cat_id,sub_cat_name,sub_cat_photo,sub_cat_status,sub_cat_category,item_name,item_price from menutable", null);
        Cursor cursor = db.rawQuery("SELECT DISTINCT "+SUB_CAT_NAME+" from " + TABLE_MENU + " where menu_nm = '" +menu_nm + "'" , null);
        ArrayList<HashMap<String,String>> arrayList= new ArrayList<HashMap<String, String>>();

        int i = 0;
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put("menu_name",(cursor.getString(cursor.getColumnIndex(MENU_NAME))));
//                hashMap.put("menu_id",(cursor.getString(cursor.getColumnIndex(MENU_ID))));
//                hashMap.put("sub_cat_id",(cursor.getString(cursor.getColumnIndex(SUB_CAT_ID))));
                  hashMap.put("sub_cat_name",(cursor.getString(cursor.getColumnIndex(SUB_CAT_NAME))));
//                hashMap.put("sub_cat_photo",(cursor.getString(cursor.getColumnIndex(SUB_CAT_PHOTO))));
//                hashMap.put("sub_cat_status",(cursor.getString(cursor.getColumnIndex(SUB_CAT_STATUS))));
//                hashMap.put("sub_cat_category",(cursor.getString(cursor.getColumnIndex(SUB_CAT_CATEGORY))));
//                hashMap.put("item_name",(cursor.getString(cursor.getColumnIndex(ITEMS_NAME))));
//                hashMap.put("item_price",(cursor.getString(cursor.getColumnIndex(ITEMS_PRICE))));

                arrayList.add(i,hashMap);
                i++;
            }while (cursor.moveToNext());
        }
    //    Toast.makeText(context,"inside get",Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
        return arrayList;
    }


    public  ArrayList<HashMap<String,String>>  getItems(String sub_cat_nm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //     Cursor cursor = db.rawQuery("select menu_nm,menu_id,sub_cat_id,sub_cat_name,sub_cat_photo,sub_cat_status,sub_cat_category,item_name,item_price from menutable", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU + " where sub_cat_name = '" +sub_cat_nm + "'" , null);
        ArrayList<HashMap<String,String>> arrayList= new ArrayList<HashMap<String, String>>();

        int i = 0;
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("menu_name",(cursor.getString(cursor.getColumnIndex(MENU_NAME))));
                hashMap.put("menu_id",(cursor.getString(cursor.getColumnIndex(MENU_ID))));
                hashMap.put("sub_cat_id",(cursor.getString(cursor.getColumnIndex(SUB_CAT_ID))));
                hashMap.put("sub_cat_name",(cursor.getString(cursor.getColumnIndex(SUB_CAT_NAME))));
                hashMap.put("sub_cat_photo",(cursor.getString(cursor.getColumnIndex(SUB_CAT_PHOTO))));
                hashMap.put("sub_cat_status",(cursor.getString(cursor.getColumnIndex(SUB_CAT_STATUS))));
                hashMap.put("sub_cat_category",(cursor.getString(cursor.getColumnIndex(SUB_CAT_CATEGORY))));
                hashMap.put("item_id",(cursor.getString(cursor.getColumnIndex(ITEMS_ID))));
                hashMap.put("item_name",(cursor.getString(cursor.getColumnIndex(ITEMS_NAME))));
                hashMap.put("item_price",(cursor.getString(cursor.getColumnIndex(ITEMS_PRICE))));

                arrayList.add(i,hashMap);
                i++;
            }while (cursor.moveToNext());
        }
        //    Toast.makeText(context,"inside get",Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
        return arrayList;
    }
    public int getProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MENU;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    public void clearTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU, null,null);
    }


    public void insertCartProduct(HashMap<String ,String>hashMap)
    {
        String prod_name =   hashMap.get("item_name").toString();
        prod_name = prod_name.replaceAll("'", "");

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        contentValues.put(ITEMS_ID,hashMap.get("item_id").toString());
        contentValues.put(ITEMS_NAME, prod_name);
        contentValues.put(ITEMS_QUANTITY,    hashMap.get("item_quantity").toString());
        contentValues.put(ITEMS_PRICE,  hashMap.get("item_price").toString());


        database.insert(TABLE_ITEM, null, contentValues);
        System.out.println("insert data in displaylog");

    }
    public  ArrayList<HashMap<String,String>>  getCartProduct()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select item_name,item_id,item_quantity,item_price from itemtable", null);

        ArrayList<HashMap<String,String>> arrayList= new ArrayList<HashMap<String, String>>();

        int i = 0;
        if(cursor.moveToFirst())
        {
            do{
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("item_id",(cursor.getString(cursor.getColumnIndex(ITEMS_ID))));
                hashMap.put("item_name",(cursor.getString(cursor.getColumnIndex(ITEMS_NAME))));
                hashMap.put("item_quantity",(cursor.getString(cursor.getColumnIndex(ITEMS_QUANTITY))));
                hashMap.put("item_price",(cursor.getString(cursor.getColumnIndex(ITEMS_PRICE))));
                arrayList.add(i,hashMap);
                i++;
            }while (cursor.moveToNext());
        }
        //    Toast.makeText(context,"inside get",Toast.LENGTH_SHORT).show();
        cursor.close();
        db.close();
        return arrayList;
    }
    public int getCartProductCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEM;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    public void clearCartTable()   {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEM, null,null);
    }

    public  String  getQuantity(String prod_name)
    {
        if(!prod_name.equals("") && !prod_name.equals(null)) {
            prod_name = prod_name.replaceAll("'", "");
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT item_quantity FROM " + TABLE_ITEM + " WHERE item_name='" + prod_name + "'", null);
//        Toast.makeText(context,"inside quantity",Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return cursor.getString(cursor.getColumnIndex(ITEMS_QUANTITY));
            }
            return "0";
        }else{
            return "0";
        }

    }

    public  boolean  getProductName(String prod_name)
    {
        if(!prod_name.equals("") && !prod_name.equals(null)) {
            prod_name = prod_name.replaceAll("'", "");

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT item_name FROM " + TABLE_ITEM + " WHERE item_name='" + prod_name + "'", null);
//        Toast.makeText(context,"inside quantity",Toast.LENGTH_SHORT).show();
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return true;
            } else {
                return false;
            }
        }else{
            return  false;
        }


    }

    public void update_byNAME(String name, String q){
        name = name.replaceAll("'", "");
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEMS_QUANTITY, q);
        database.update(TABLE_ITEM, values, ITEMS_NAME + " = ?", new String[]{name});
        //  database.update(TABLE_PRODUCT, values, PRODUCT_NAME + " = ?", new String[]{name});

    }



    public void deleteRecord(String prod_name)
    {
        prod_name = prod_name.replaceAll("'","");
        try {
            SQLiteDatabase database = this.getWritableDatabase();
            //int row = getProductCount();

            database.delete(TABLE_ITEM, ITEMS_NAME + "=?", new String[]{prod_name});

        }catch(Exception e)
        {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
            //return -1;
        }
    }
    public boolean check(String str) {
        String decimalPattern = "([0-9]*)\\.([0-9]*)";
        boolean match = Pattern.matches(decimalPattern, str);
        return  match;
    }
    public boolean checkInteger(String str)
    {
        String regex = "\\d+";
        boolean match = Pattern.matches(regex,str);
        return  match;
    }



}