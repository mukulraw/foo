package com.ratna.foosip;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.profilePOJO.picRequestBean;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;


public class RequestsFragment extends Fragment {


    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;


    //Android Layout

    private CircleImageView mDisplayImage;
    private ImageView img_profile;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn,mSignOut;
    private Button mImageBtn;

    private TextView ed_interest, ed_first_name, ed_last_name, ed_profession;

    private TextView txt_email, txt_user_name;


    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private SavedParameter savedParameter;

    String base_64;

    ProgressBar progress;

    UserSession userSession;

    ImageView img_edit;


    FoosipChat foosipChat;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        progress = view.findViewById(R.id.progress);

        savedParameter = new SavedParameter(getContext());
        userSession = new UserSession(getContext());


        boolean flag = userSession.getProfile(getContext());
        /*if (flag) {
            Intent intent = new Intent(getContext(), ScanQR.class);
            startActivity(intent);
            finish();
        }*/

        mDisplayImage = (CircleImageView) view.findViewById(R.id.settings_image);
        img_profile = view.findViewById(R.id.img_profile);
        mName = (TextView) view.findViewById(R.id.settings_name);
        mStatus = (TextView) view.findViewById(R.id.settings_status);
        ed_profession = (TextView) view.findViewById(R.id.ed_profession);

        mStatusBtn = (Button) view.findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) view.findViewById(R.id.settings_image_btn);

        ed_interest = (TextView) view.findViewById(R.id.ed_interest);
        ed_first_name = (TextView) view.findViewById(R.id.ed_first_name);
        ed_last_name = (TextView) view.findViewById(R.id.ed_last_name);

        txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_user_name = (TextView) view.findViewById(R.id.txt_user_name);
        img_edit = (ImageView) view.findViewById(R.id.img_edit);
        img_profile = (ImageView)view.findViewById(R.id.img_profile);

        mSignOut = (Button)view .findViewById(R.id.btn_sign_out);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        foosipChat = (FoosipChat)getActivity().getApplicationContext();



        setView(view);

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

                if (!image.equals("default")) {

                    //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mDisplayImage);

                    Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(img_profile, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getContext()).load(image).placeholder(R.drawable.default_avatar).into(img_profile);

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

                Intent status_intent = new Intent(getContext(), StatusActivity.class);
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
                        .start(SettingsActivity.this);
                        */

            }
        });


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userSession.setPROFILE(false);
                Intent intent = new Intent(getActivity(), ProfileUpdateActivity.class);
                startActivity(intent);

            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSession.setUserLogIn(false);
                Intent mainIntent = new Intent(getActivity(), SignIn.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {

        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        profileRequestBean body = new profileRequestBean();

        body.setId(savedParameter.getUID());

        Log.d("uusswwee", savedParameter.getUID());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type", "application/json");
        map.put("Authorization", savedParameter.getTOKEN());


        Log.d("bearer", savedParameter.getTOKEN());


        Call<profileBean> call = cr.getProfile(body, map);


        call.enqueue(new retrofit2.Callback<profileBean>() {
            @Override
            public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date birthDate = sdf.parse(response.body().getData().getDob()); //Yeh !! It's my date of birth :-)
                    Age age = calculateAge(birthDate);
                    //My age is
                    mName.setText(response.body().getData().getId()+" "+response.body().getData().getFirstName() + " " + response.body().getData().getLastName() + " ," + age);
                   foosipChat.user_id = response.body().getData().getId();
                   foosipChat.user_name = response.body().getData().getFirstName();
                    System.out.println(age);

                    mStatus.setText(response.body().getData().getStatus());

                    txt_email.setText(response.body().getData().getEmail());
                    txt_user_name.setText(response.body().getData().getFirstName() + " " +response.body().getData().getLastName() + ", " +age);

                    ed_first_name.setText(response.body().getData().getFirstName());
                    ed_last_name.setText(response.body().getData().getLastName());

                    ed_interest.setText(response.body().getData().getInterest());
                    ed_profession.setText(response.body().getData().getProfession());

                    if (response.body().getData().getProfilePic().length() > 0) {

                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        loader.displayImage("http://foosip.com/" + response.body().getData().getProfilePic(), img_profile, options);

                        Log.d("profile_pic", response.body().getData().getProfilePic());

                    }


                }catch(Exception e)
                {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(getActivity());

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(getContext())
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                base_64 = Base64.encodeToString(thumb_byte, Base64.DEFAULT);

                progress.setVisibility(View.VISIBLE);

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://foosip.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                picRequestBean body = new picRequestBean();

                body.setId(savedParameter.getUID());
                body.setProfilePic(base_64);

                Map<String, String> map = new HashMap<>();

                map.put("Content-Type", "application/json");
                map.put("Authorization", savedParameter.getTOKEN());

                Call<profileBean> call = cr.updatePic(body, map);

                call.enqueue(new retrofit2.Callback<profileBean>() {
                    @Override
                    public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                        progress.setVisibility(View.GONE);
                        loadData();

                    }

                    @Override
                    public void onFailure(Call<profileBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);
                    }
                });


                /*StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
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
                                                    Toast.makeText(getContext(), "Success Uploading.", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });


                                    } else {

                                        Toast.makeText(getContext(), "Error in uploading thumbnail.", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();

                                    }


                                }
                            });



                        } else {

                            Toast.makeText(getContext(), "Error in uploading.", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();

                        }

                    }
                });*/


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }

    private void setView(View view) {

        txt_email.setText(savedParameter.getEMAIL(getContext()));
        txt_user_name.setText(savedParameter.getUSERNAME(getContext()));

        //Toast.makeText(getContext(),base_64,Toast.LENGTH_SHORT).show();

        LinearLayout ll_submit = (LinearLayout) view.findViewById(R.id.ll_submit);
        ll_submit.setVisibility(View.GONE);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!ed_first_name.getText().toString().equals("") && !ed_first_name.getText().toString().equals(null)) {
                    if (!ed_last_name.getText().toString().equals("") && !ed_last_name.getText().toString().equals(null)) {
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

                        Map<String, String> map = new HashMap<>();

                        map.put("Content-Type", "application/json");
                        map.put("Authorization", savedParameter.getTOKEN());

                        Call<profileBean> call = cr.updateProfile(body, map);


                        call.enqueue(new retrofit2.Callback<profileBean>() {
                            @Override
                            public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                                /*userSession.setPROFILE(true);
                                Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(SettingsActivity.this, ScanQR.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                                progress.setVisibility(View.GONE);*/
                            }

                            @Override
                            public void onFailure(Call<profileBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    } else {

                        Toast.makeText(getContext(), "Please enter Last Name", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Please enter First Name", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


    private  Age calculateAge(Date birthDate) {
        int years = 0;
        int months = 0;
        int days = 0;
        //create calendar object for birth day
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTimeInMillis(birthDate.getTime());
        //create calendar object for current day
        long currentTime = System.currentTimeMillis();
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(currentTime);
        //Get difference between years
        years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        int currMonth = now.get(Calendar.MONTH) + 1;
        int birthMonth = birthDay.get(Calendar.MONTH) + 1;
        //Get difference between months
        months = currMonth - birthMonth;
        //if month difference is in negative then reduce years by one and calculate the number of months.
        if (months < 0) {
            years--;
            months = 12 - birthMonth + currMonth;
            if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
                months--;
        } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            years--;
            months = 11;
        }
        //Calculate the days
        if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
            days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
        else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)) {
            int today = now.get(Calendar.DAY_OF_MONTH);
            now.add(Calendar.MONTH, -1);
            days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
        } else {
            days = 0;
            if (months == 12) {
                years++;
                months = 0;
            }
        }
        //Create new Age object
        return new Age(days, months, years);
    }




    class Age {
        private int days;
        private int months;
        private int years;

        private Age() {
            //Prevent default constructor
        }

        public Age(int days, int months, int years) {
            this.days = days;
            this.months = months;
            this.years = years;
        }

        public int getDays() {
            return this.days;
        }

        public int getMonths() {
            return this.months;
        }

        public int getYears() {
            return this.years;
        }

        @Override
        public String toString() {
            return String.valueOf(years);
        }
    }

}


