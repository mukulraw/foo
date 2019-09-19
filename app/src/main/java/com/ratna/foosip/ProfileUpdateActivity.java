package com.ratna.foosip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProfileUpdateActivity extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn;
    private Button mImageBtn;

    private EditText ed_interest,ed_first_name,ed_last_name,ed_profession,ed_dob;;

    private TextView txt_email,txt_user_name;


    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;

    private SavedParameter savedParameter;

    String base_64;

    UserSession userSession;

    ProgressBar progress;

    ImageView img_dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savedParameter = new SavedParameter(ProfileUpdateActivity.this);
        userSession = new UserSession(this);

        progress = findViewById(R.id.progress);



        mDisplayImage = (CircleImageView) findViewById(R.id.settings_image);
        mName = (TextView) findViewById(R.id.settings_name);
        mStatus = (TextView) findViewById(R.id.settings_status);

        mStatusBtn = (Button) findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) findViewById(R.id.settings_image_btn);

        ed_interest = (EditText)findViewById(R.id.ed_interest);
        ed_first_name = (EditText)findViewById(R.id.ed_first_name);
        ed_last_name = (EditText)findViewById(R.id.ed_last_name);
        ed_profession = (EditText) findViewById(R.id.ed_profession);
        ed_dob = (EditText) findViewById(R.id.ed_dob);
        img_dob = (ImageView) findViewById(R.id.img_dob);


        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_user_name = (TextView) findViewById(R.id.txt_user_name);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        setView();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                mName.setText(name);
                ed_first_name.setText(name);
                mStatus.setText(status);

                if(!image.equals("default")) {

                    //Picasso.with(ProfileUpdateActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.with(ProfileUpdateActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mDisplayImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(ProfileUpdateActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                        }
                    });

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(ProfileUpdateActivity.this, StatusActivity.class);
                status_intent.putExtra("status_value", status_value);
                startActivity(status_intent);

            }
        });


        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileUpdateActivity.this);
                        */

            }
        });

        img_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(ProfileUpdateActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                ed_dob.setText(String.format("%04d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(ProfileUpdateActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mProgressDialog = new ProgressDialog(ProfileUpdateActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload and process the image.");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                base_64 = Base64.encodeToString(thumb_byte,Base64.DEFAULT);


                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference thumb_filepath = mImageStorage.child("profile_images").child("thumbs").child(current_user_id + ".jpg");



                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {

                                    String thumb_downloadUrl = thumb_task.getResult().getDownloadUrl().toString();

                                    if(thumb_task.isSuccessful()){

                                        Map update_hashMap = new HashMap();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("thumb_image", thumb_downloadUrl);

                                        mUserDatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    mProgressDialog.dismiss();
                                                    Toast.makeText(ProfileUpdateActivity.this, "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(ProfileUpdateActivity.this, "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(ProfileUpdateActivity.this, "Error in uploading.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }

    private void setView() {

        txt_email.setText(savedParameter.getEMAIL(ProfileUpdateActivity.this));
        txt_user_name.setText(savedParameter.getUSERNAME(ProfileUpdateActivity.this));

//        Toast.makeText(ProfileUpdateActivity.this,base_64,Toast.LENGTH_SHORT).show();

        LinearLayout ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed_first_name.getText().toString().equals(""))
                {
                    if(!ed_last_name.getText().toString().equals(""))
                    {



                        progress.setVisibility(View.VISIBLE);



                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://foosip.com/")
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);

                        profileUpdateBean body = new profileUpdateBean();

                        body.setId(savedParameter.getUID());
                        body.setFirstName(ed_first_name.getText().toString());
                        body.setInterest(ed_interest.getText().toString());
                        body.setLastName(ed_last_name.getText().toString());
                        body.setProfession(ed_profession.getText().toString());
                        body.setDob(ed_dob.getText().toString());

                        Map<String, String> map = new HashMap<>();

                        map.put("Content-Type" , "application/json");
                        map.put("Authorization" , savedParameter.getTOKEN());

                        Call<profileBean> call = cr.updateProfile(body , map);


                        call.enqueue(new retrofit2.Callback<profileBean>() {
                            @Override
                            public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                                progress.setVisibility(View.GONE);
                                new ProfileEditCall2(ProfileUpdateActivity.this, SavedParameter.getEMAIL(ProfileUpdateActivity.this),
                                      ed_first_name.getText().toString(),ed_interest.getText().toString(),
                                        ed_first_name.getText().toString(),ed_last_name.getText().toString(),base_64,ed_profession.getText().toString(),ed_dob.getText().toString() );

                               // Toast.makeText(ProfileUpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                             //   finish();


                            }

                            @Override
                            public void onFailure(Call<profileBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });




                    }else{

                        Toast.makeText(ProfileUpdateActivity.this,"Please enter Last Name",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(ProfileUpdateActivity.this,"Please enter First Name",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData()
    {

        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        profileRequestBean body = new profileRequestBean();

        body.setId(savedParameter.getUID());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type" , "application/json");
        map.put("Authorization" , savedParameter.getTOKEN());

        Call<profileBean> call = cr.getProfile(body , map);


        call.enqueue(new retrofit2.Callback<profileBean>() {
            @Override
            public void onResponse(Call<profileBean> call, Response<profileBean> response) {


                mName.setText(response.body().getData().getFirstName() + " " + response.body().getData().getLastName());
                mStatus.setText(response.body().getData().getStatus());

                txt_email.setText(response.body().getData().getEmail());
                txt_user_name.setText(response.body().getData().getName());

                ed_first_name.setText(response.body().getData().getFirstName());
                ed_last_name.setText(response.body().getData().getLastName());

                ed_interest.setText(response.body().getData().getInterest());
                ed_profession.setText(response.body().getData().getProfession());
                ed_dob.setText(response.body().getData().getDob());

                if (response.body().getData().getProfilePic().length() > 0)
                {

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage("http://foosip.com/" + response.body().getData().getProfilePic() , mDisplayImage , options);

                    Log.d("iimmaaggee" , response.body().getData().getProfilePic());

                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }



    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}

