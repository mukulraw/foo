package com.ratna.foosip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.RecycleViewAdapterHistoryItemDetails;
import Adapter.RecyclerAdapterComment;
import SharedPreferences.SavedParameter;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CommentActivity extends AppCompatActivity {

    private ImageView btSendMessage, btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    ProgressBar progress;
    FrameLayout ff;

    CircleImageView profile;
    TextView name , type , post;
    ImageView image;

    TextView likes , comments , text;
    LinearLayout ll,ll_add_comment;
    private SavedParameter savedParameter;

    FoosipChat foosipChat;

//    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
//    RecyclerView.LayoutManager mLayoutManager;
//    RecyclerView mRecyclerView_items;

    ArrayList<CommentSend> list;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

//        Toolbar mToolbar = (Toolbar) findViewById(R.id.users_appBar);
//        setSupportActionBar(mToolbar);
//
//        getSupportActionBar().setTitle("Post");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mRecyclerView_items = (RecyclerView) findViewById(R.id.recycler_view);
//
//
//
//        mRecyclerView_items.setLayoutManager(mLayoutManager);

        foosipChat = (FoosipChat)getApplicationContext();

        progress = (ProgressBar)findViewById(R.id.progressBar8);

        savedParameter = new SavedParameter(this);
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText) findViewById(R.id.editTextMessage);
        btSendMessage = (ImageView) findViewById(R.id.buttonMessage);
        btEmoji = (ImageView) findViewById(R.id.buttonEmoji);
        emojIcon = new EmojIconActions(this, contentRoot, edMessage, btEmoji);
        emojIcon.ShowEmojIcon();

        profile = (CircleImageView) findViewById(R.id.view5);
        name = (TextView)findViewById(R.id.textView31);
        type =  (TextView)findViewById(R.id.textView58);
        post =  (TextView)findViewById(R.id.textView59);
        image =  (ImageView) findViewById(R.id.imageView7);
        text =  (TextView)findViewById(R.id.textView57);

        ff = (FrameLayout)findViewById(R.id.ff);
        ll =  (LinearLayout) findViewById(R.id.ll);
        ll_add_comment =  (LinearLayout) findViewById(R.id.ll_add_comment);

        likes =  (TextView)findViewById(R.id.textView60);
        comments =  (TextView)findViewById(R.id.textView61);

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        Bundle bundle = getIntent().getExtras();
        list =  (ArrayList<CommentSend>)getIntent().getSerializableExtra("list");
        final PostSend postSend = (PostSend) bundle.getParcelable("post");
      //  Toast.makeText(this,postSend.getId(),Toast.LENGTH_SHORT).show();





        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.displayImage("http://foosip.com/" + postSend.getSenderImage() , profile , options);

        name.setText(postSend.getSenderName());

        if (postSend.getPostType().equals("ask"))
        {
            text.setText("Asked for a");
            type.setText("Recommendation");
        }
        else if (postSend.getPostType().equals("give"))
        {
            text.setText("Shared a");
            type.setText("Recommendation");
        }
        else if (postSend.getPostType().equals("moment"))
        {
            text.setText("Shared a");
            type.setText("Moment");
        }
        else if (postSend.getPostType().equals("food"))
        {
            text.setText("Shared a");
            type.setText("Food Pornography");
        }

        if (postSend.getType().equals("image"))
        {
            loader.displayImage("http://foosip.com/uploads/posts/" + postSend.getPost() , image , options);
            ff.setVisibility(View.VISIBLE);
            image.setVisibility(View.VISIBLE);
            post.setVisibility(View.VISIBLE);
            post.setText(postSend.getDescription());
        }
        else
        {
            ff.setVisibility(View.GONE);
            post.setText(postSend.getPost());
            image.setVisibility(View.GONE);
            post.setVisibility(View.VISIBLE);
        }


        for(CommentSend c : list)
        {
            String str =    c.getComment();
            String username = c.getUser_name();
            AddText(str,username);

         //   Toast.makeText(this,"name"+username +"  "+ str ,Toast.LENGTH_SHORT).show();
        }


//        mRecyclerView_items.setHasFixedSize(true);
//
//        mAdapter_items = new RecyclerAdapterComment(this, list);
//        mRecyclerView_items.setAdapter(mAdapter_items);
//        mLayoutManager =
//                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);


        likes.setText(String.valueOf(postSend.getTotalLikes())+ " Favorites");
        comments.setText(String.valueOf(postSend.getTotalComments())+" Comments");

        


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(CommentActivity.this, ProfileActivity.class);
                profileIntent.putExtra("uid", postSend.getUid());
                profileIntent.putExtra("user_id",postSend.getUserId());
                startActivity(profileIntent);
            }
        });

        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String  message = edMessage.getText().toString();
                AddText(message,foosipChat.user_name);
                edMessage.getText().clear();



                progress.setVisibility(View.VISIBLE);

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://foosip.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

//                AddComment addComment = new AddComment();
//                addComment.setUser_id(savedParameter.getUID());
//                addComment.setPost_id(postSend.getId());
//                addComment.setComment(message);

//                Toast.makeText(CommentActivity.this,postSend.getUserId()+" "+postSend.getId(),Toast.LENGTH_SHORT).show();


                Call<String> call = cr.add_comment(savedParameter.getUID(),postSend.getId(),message);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                        try {
                            Log.d("response_add" , new Gson().toJson(response.body()).toString());

                            if (response.body().equals("success"))
                            {
                                Toast.makeText(CommentActivity.this,"Add",Toast.LENGTH_SHORT).show();



                            }
                            else
                            {

                            }

                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });



            }
        });


    }
    private void AddText(String message,String name)
    {
        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1.setText(message);
        textView1.setTextSize(15);
        textView1.setTextColor(getResources().getColor(R.color.dimgrey));
        textView1.setPadding(20, 4, 20, 20); // in pixels (left, top, right, bottom)

        final TextView textView2 = new TextView(this);
        textView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView2.setText(name);
        textView2.setTextSize(17);
        textView2.setTextColor(getResources().getColor(R.color.black));
        textView2.setPadding(10, 20, 10, 5); // in pixels (left, top, right, bottom)

        ll_add_comment.addView(textView2);
        ll_add_comment.addView(textView1);


    }

//    public void loadData()
//    {
//        progress.setVisibility(View.VISIBLE);
//
//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://foosip.com/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        final AllAPIs cr = retrofit.create(AllAPIs.class);
//
//        Log.d("qqrr" , savedParameter.getRID());
//
//        Call<List<postListbean>> call = cr.getPosts(savedParameter.getRID());
//
//        call.enqueue(new Callback<List<postListbean>>() {
//            @Override
//            public void onResponse(Call<List<postListbean>> call, Response<List<postListbean>> response) {
//
//                try {
//
//                    Log.d("response" , new Gson().toJson(response.body()).toString());
//
//                    if (response.body().size() > 0)
//                    {
//                        List<postListbean> listbeans = response.body();
//
//                    }
//                    else
//                    {
//
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//                progress.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onFailure(Call<List<postListbean>> call, Throwable t) {
//                progress.setVisibility(View.GONE);
//            }
//        });
//
//    }



}
