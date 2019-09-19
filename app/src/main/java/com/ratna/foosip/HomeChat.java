package com.ratna.foosip;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import app.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by ratna on 4/6/2018.
 */

public class  HomeChat extends AppCompatActivity implements ChatsFragment.OnTextChange {

    private FirebaseAuth mAuth;
    private RelativeLayout mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;
    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private String mCurrentUserId;
    private DatabaseReference mRootRef;

    public static int CAMERA_PREVIEW_RESULT = 1;

    SavedParameter savedParameter;
    SharedPreferences fcmPref;

    ImageView img_scan;
    TextView txt_sign_out;

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        savedParameter = new SavedParameter(this);
        fcmPref = getSharedPreferences(Config.SHARED_PREF , Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

//        mToolbar = (RelativeLayout) findViewById(R.id.main_app_bar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        userSession = new UserSession(this);


        txt_sign_out = (TextView)findViewById(R.id.txt_sign_out);
        img_scan = (ImageView)findViewById(R.id.img_scan);


        txt_sign_out.setVisibility(View.GONE);


        txt_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userSession.setUserLogIn(false);
                Intent mainIntent = new Intent(HomeChat.this, SignIn.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();

            }
        });

        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent settingsIntent = new Intent(HomeChat.this, ScanQR.class);
                startActivity(settingsIntent);
            }
        });


        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.post_it_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.tounge_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.send_message_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.user_select));

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.post_it_select);
        mTabLayout.getTabAt(1).setIcon(R.drawable.tounge_select);
        mTabLayout.getTabAt(2).setIcon(R.drawable.send_message_select);
        mTabLayout.getTabAt(3).setIcon(R.drawable.user_select);



        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);



        Call<Integer> call = cr.updateFirebase(savedParameter.getUID() , fcmPref.getString("token" , ""));

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {



            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        } else {

            mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        userSession.setUserLogIn(false);
        Intent mainIntent = new Intent(HomeChat.this, SignIn.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    @Override
    public void TextChange(String str) {
        String tag = "android:switcher:" + R.id.main_tabPager+ ":" + 2;
        ChatsFragment f = (ChatsFragment) getSupportFragmentManager().findFragmentByTag(tag);
//        f.DisplayTextChange(str);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
//
//
//        if(item.getItemId() == R.id.scan){
//
//            Intent settingsIntent = new Intent(HomeChat.this, ScanQR.class);
//            startActivity(settingsIntent);
//
//        }
//
//        return true;
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
//
//            Uri imageUri = data.getData();
//
//            final String current_user_ref = "restaurant/" + "qr_03";
//
//            DatabaseReference user_message_push = mRootRef.child("restaurant")
//                    .push();
//
//            final String push_id = user_message_push.getKey();
//
//
//            StorageReference filepath = mImageStorage.child("group_message_images").child( push_id + ".jpg");
//
//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                    if(task.isSuccessful()){
//
//                        String download_url = task.getResult().getDownloadUrl().toString();
//
//
//                        Map messageMap = new HashMap();
//                        messageMap.put("message", download_url);
//                        messageMap.put("seen", false);
//                        messageMap.put("type", "image");
//                        messageMap.put("time", ServerValue.TIMESTAMP);
//                        messageMap.put("from", mCurrentUserId);
//
//                        Map messageUserMap = new HashMap();
//                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
//
//
//
//                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                                if(databaseError != null){
//
//                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
//
//                                }
//
//                            }
//                        });
//
//
//                    }
//
//                }
//            });
//
//        }
//
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
//            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
//
//            Toast.makeText(this, "Image Save on: " + path, Toast.LENGTH_LONG).show();
//
//        }
//    }


    //Important for Android 6.0 permisstion request, don't forget this!

}

