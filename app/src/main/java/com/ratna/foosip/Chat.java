package com.ratna.foosip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import SharedPreferences.SavedParameter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Chat extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progress;
    EmojiconEditText message;
    FloatingActionButton send;
    ImageButton emoji;
    RecyclerView grid;
    private EmojIconActions emojIcon;

    View root;

    String groupId;
    String groupName;

    SavedParameter savedParameter;

    List<chatBean> list;
    ChatAdapter adapter;
    LinearLayoutManager manager;

    BroadcastReceiver commentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        savedParameter = new SavedParameter(this);

        list = new ArrayList<>();

        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("name");

        toolbar = findViewById(R.id.toolbar2);
        progress = findViewById(R.id.progressBar5);
        message = findViewById(R.id.editText6);
        send = findViewById(R.id.floatingActionButton3);
        emoji = findViewById(R.id.imageButton);
        grid = findViewById(R.id.grid);
        root = findViewById(R.id.contentRoot);

        adapter = new ChatAdapter(this, list);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , true);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        emojIcon = new EmojIconActions(this, root, message, emoji);
        emojIcon.ShowEmojIcon();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle(groupName);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("asdasasddsa" , "ASasasdad");

                // checking for type intent filter
                if (intent.getAction().equals("commentData")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    chatBean item = gson.fromJson(json, chatBean.class);

                    adapter.addData(item);

                    grid.scrollToPosition(0);

                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = message.getText().toString();

                if (m.length() > 0) {


                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://foosip.com/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Call<createGroupBean> call = cr.sendMessage(groupId, savedParameter.getUID(), m);
                    call.enqueue(new Callback<createGroupBean>() {
                        @Override
                        public void onResponse(Call<createGroupBean> call, Response<createGroupBean> response) {


                            if (response.body().getStatus().equals("1")) {
                                message.setText("");
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<createGroupBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                }

            }
        });

        LocalBroadcastManager.getInstance(Chat.this).registerReceiver(commentReceiver,
                new IntentFilter("commentData"));

    }


    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(commentReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();


        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<List<chatBean>> call = cr.getMessages(groupId);

        call.enqueue(new Callback<List<chatBean>>() {
            @Override
            public void onResponse(Call<List<chatBean>> call, Response<List<chatBean>> response) {

                try {

                    if (response.body().size() > 0) {
                        adapter.setGridData(response.body());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<chatBean>> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        Context context;
        List<chatBean> list = new ArrayList<>();
        SavedParameter savedParameter;

        public ChatAdapter(Context context, List<chatBean> list) {
            this.context = context;
            this.list = list;
            savedParameter = new SavedParameter(context);
        }


        public void setGridData(List<chatBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }


        public void addData(chatBean item) {
            list.add(0, item);
            notifyItemInserted(0);
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.chat_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            chatBean item = list.get(position);

            if (item.getSenderId().equals(savedParameter.getUID())) {
                // right
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(item.getSenderImage(), holder.profileRight, options);
                holder.nameRight.setText(item.getSenderName());
                holder.messageRight.setText(item.getMessage());
                holder.timeRight.setText(item.getCreatedDate());

                holder.right.setVisibility(View.VISIBLE);
                holder.left.setVisibility(View.GONE);

            } else {
                // left
                DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(item.getSenderImage(), holder.profileLeft, options);
                holder.nameLeft.setText(item.getSenderName());
                holder.messageLeft.setText(item.getMessage());
                holder.timeLeft.setText(item.getCreatedDate());

                holder.left.setVisibility(View.VISIBLE);
                holder.right.setVisibility(View.GONE);

            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profileRight, profileLeft;
            TextView nameRight, nameLeft;
            TextView messageRight, messageLeft;
            TextView timeRight, timeLeft;
            View right, left;

            public ViewHolder(View itemView) {
                super(itemView);

                profileRight = itemView.findViewById(R.id.view3);
                nameRight = itemView.findViewById(R.id.textView48);
                messageRight = itemView.findViewById(R.id.textView50);
                timeRight = itemView.findViewById(R.id.textView51);

                profileLeft = itemView.findViewById(R.id.view4);
                nameLeft = itemView.findViewById(R.id.textView52);
                messageLeft = itemView.findViewById(R.id.textView53);
                timeLeft = itemView.findViewById(R.id.textView54);

                right = itemView.findViewById(R.id.right);
                left = itemView.findViewById(R.id.left);


            }
        }
    }


}
