package com.ratna.foosip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import SharedPreferences.SavedParameter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Wall extends Fragment {

    RecyclerView grid;
    WallAdapter adapter;
    OptionsFabLayout fab;
    List<postListbean> list;
    ProgressBar progress;
    SavedParameter savedParameter;
    BroadcastReceiver commentReceiver;
    TextView newPosts;

    TextView text;
    LinearLayout scan;
    LinearLayout ll_chat_bg;

    ConstraintLayout constraintLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_layout, container, false);


        savedParameter = new SavedParameter(getContext());
        list = new ArrayList<>();
        newPosts = view.findViewById(R.id.textView63);
        grid = view.findViewById(R.id.grid);
        adapter = new WallAdapter(getContext(), list);
        fab = view.findViewById(R.id.add);
        progress = view.findViewById(R.id.progressBar8);
        text = view.findViewById(R.id.textView62);
        scan = view.findViewById(R.id.ll_scan);
        constraintLayout = view.findViewById(R.id.parent);
        ll_chat_bg = view.findViewById(R.id.ll_chat_bg);

        fab.setMiniFabsColors(
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary,
                R.color.colorPrimary);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        grid.setAdapter(adapter);
        grid.setLayoutManager(staggeredGridLayoutManager);


        fab.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fab.isOptionsMenuOpened()) {
                    fab.closeOptionsMenu();
                }

            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Date c = Calendar.getInstance().getTime();
//                    System.out.println("Current time => " + c);
//
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                    String today = df.format(c);
//
//
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(df.parse(today));
//                    cal.add(Calendar.DATE, 1);
//                    String tomorrow = df.format(cal.getTime());
//
//
//                    savedParameter.setDATESET(tomorrow);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(getContext(), ScanQR2.class);
                startActivity(intent);
            }
        });


        fab.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {


                switch (fabItem.getItemId()) {

                    case R.id.scan:

                        Intent intent0 = new Intent(getContext(), ScanQR.class);
                        startActivity(intent0);
                        fab.closeOptionsMenu();

                        break;

                    case R.id.fab_add:

                        Intent intent = new Intent(getContext(), SharePost.class);
                        intent.putExtra("type", "ask");
                        startActivity(intent);
                        fab.closeOptionsMenu();


                        break;
                    case R.id.fab_link:

                        Intent intent1 = new Intent(getContext(), ShareMoment.class);
                        intent1.putExtra("type", "moment");
                        startActivity(intent1);
                        fab.closeOptionsMenu();

                        break;
                    case R.id.food:

                        Intent intent2 = new Intent(getContext(), ShareMoment.class);
                        intent2.putExtra("type", "food");
                        startActivity(intent2);
                        fab.closeOptionsMenu();

                        break;
                    case R.id.give:

                        Intent intent3 = new Intent(getContext(), SharePost.class);
                        intent3.putExtra("type", "give");
                        startActivity(intent3);
                        fab.closeOptionsMenu();

                        break;
                    default:
                        break;
                }


            }
        });


        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("asdasasddsa", "ASasasdad");

                // checking for type intent filter
                if (intent.getAction().equals("post")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    postListbean item = gson.fromJson(json, postListbean.class);

                    try {
                        if (!item.getUserId().equals(savedParameter.getUID())) {
                            adapter.addData(item);

                            newPosts.setVisibility(View.VISIBLE);
                            // grid.scrollToPosition(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };

        newPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grid.scrollToPosition(0);
                newPosts.setVisibility(View.GONE);

            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                new IntentFilter("post"));


        return view;
    }


    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentReceiver);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();


//        Date c = Calendar.getInstance().getTime();
//        System.out.println("Current time => " + c);
//
//        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//        String today = df.format(c);
//
//        if(savedParameter.getDATESET().equals(today))
//        {
//            text.setVisibility(View.GONE);
//            grid.setVisibility(View.GONE);
//            fab.setVisibility(View.GONE);
//            constraintLayout.setBackground(getActivity().getResources().getDrawable(R.drawable.wall_bg));
//            scan.setVisibility(View.VISIBLE);
//            ll_chat_bg.setVisibility(View.VISIBLE);
//        }


        if (savedParameter.getRID().length() > 0) {
            loadData();
            grid.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            text.setVisibility(View.GONE);
            scan.setVisibility(View.GONE);
            ll_chat_bg.setVisibility(View.GONE);
        } else {

            grid.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);

            text.setVisibility(View.VISIBLE);
            scan.setVisibility(View.VISIBLE);
            ll_chat_bg.setVisibility(View.VISIBLE);

        }


    }


    public void loadData() {
        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Log.d("qqrr", savedParameter.getRID());

        Call<List<postListbean>> call = cr.getPosts(savedParameter.getRID());

        call.enqueue(new Callback<List<postListbean>>() {
            @Override
            public void onResponse(Call<List<postListbean>> call, Response<List<postListbean>> response) {

                try {

                    Log.d("response", new Gson().toJson(response.body()).toString());

                    if (response.body().size() > 0) {
                        adapter.setGridData(response.body());
                        text.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.VISIBLE);
                        text.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<postListbean>> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }

    class WallAdapter extends RecyclerView.Adapter<WallAdapter.ViewHolder> {

        Context context;
        List<postListbean> list = new ArrayList<>();


        public WallAdapter(Context context, List<postListbean> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<postListbean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void addData(postListbean item) {
            list.add(0, item);
            notifyItemInserted(0);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.wall_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            holder.setIsRecyclable(false);

            final postListbean item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage("http://foosip.com/" + item.getSenderImage(), holder.profile, options);
            Log.d("image", "http://foosip.com/" + item.getSenderImage());

            holder.name.setText(item.getSenderName());

            if (item.getPostType().equals("ask")) {
                holder.text.setText("Asked for a");
                holder.type.setText("Recommendation");
            } else if (item.getPostType().equals("give")) {
                holder.text.setText("Shared a");
                holder.type.setText("Recommendation");
            } else if (item.getPostType().equals("moment")) {
                holder.text.setText("Shared a");
                holder.type.setText("Moment");
            } else if (item.getPostType().equals("food")) {
                holder.text.setText("Shared a");
                holder.type.setText("Food Pornography");
            }


            holder.likes.setText(String.valueOf(item.getTotalLikes()) + " likes");
            holder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    LikePost likePost = new LikePost();
//                    likePost.setPost_id( list.get(position).getId());
//                    likePost.setUser_id( savedParameter.getUID());

                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://foosip.com/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Log.d("qqrr", savedParameter.getRID());

                    //  Toast.makeText(getActivity(),savedParameter.getUID(),Toast.LENGTH_SHORT).show();

                    Call<String> call = cr.LikePost(savedParameter.getUID(), list.get(position).getId());

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                Log.d("response_like", new Gson().toJson(response.body()).toString());

                                if (response.body().equals("success")) {
                                    holder.img_like.setBackgroundResource(R.drawable.baseline_favorite_black);


                                    Toast.makeText(getActivity(), "Liked", Toast.LENGTH_SHORT).show();

                                }
                                if (response.body().equals("already liked")) {
                                    Call<String> call1 = cr.UnLikePost(savedParameter.getUID(), list.get(position).getId());

                                    call1.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {

                                            try {
                                                Log.d("response_like", new Gson().toJson(response.body()).toString());

                                                if (response.body().equals("success")) {
                                                    holder.img_like.setBackgroundResource(R.drawable.baseline_favorite_border_black);


                                                    Toast.makeText(getActivity(), "UnLiked", Toast.LENGTH_SHORT).show();

                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            progress.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                } else {

                                }

                            } catch (Exception e) {
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

            //  int like_count = item.getTotalLikes();
            holder.likes.setText(item.getTotalLikes() + " favorites");
            holder.comments.setText(item.getTotalComments() + " comments");

            holder.img_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PostSend postSend = new PostSend(list.get(position).getId(), list.get(position).getUserId(), list.get(position).getRid(),
                            list.get(position).getType(), list.get(position).getPost(), list.get(position).getTotalLikes(), list.get(position).getTotalComments(),
                            list.get(position).getPostType(), list.get(position).getCreatedDate(), list.get(position).getSenderImage(), list.get(position).getSenderName(),
                            list.get(position).getDescription(), list.get(position).getUid());

                    ArrayList<CommentSend> comments = new ArrayList<>();
                    for (Comments c : list.get(position).getComments()) {
                        comments.add(new CommentSend(c.getId(), c.getUser_id(), c.getComment(), c.getUser_image(), c.getUser_name(), c.getUid()));
                    }
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable("post",postSend);
                    intent.putExtra("list", comments);
                    intent.putExtra("post", postSend);
                    getActivity().startActivity(intent);

                }
            });

            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("uid", item.getUid());
                    profileIntent.putExtra("user_id", item.getUserId());
                    context.startActivity(profileIntent);
                }
            });

//            holder.ll.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                        PostSend postSend = new PostSend(list.get(position).getId(),list.get(position).getUserId(),list.get(position).getRid(),
//                        list.get(position).getType(),list.get(position).getPost(),list.get(position).getTotalLikes(),list.get(position).getTotalComments(),
//                        list.get(position).getPostType(),list.get(position).getCreatedDate(),list.get(position).getSenderImage(),list.get(position).getSenderName(),
//                        list.get(position).getDescription());
//
//                        ArrayList<CommentSend> comments = new ArrayList<>();
//                        for(Comments c: list.get(position).getComments()) {
//                            comments.add(new CommentSend(c.getId(),c.getUser_id(), c.getComment(),c.getUser_image(),c.getUser_name()));
//                        }
//                        Intent intent = new Intent(getActivity(),CommentActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putParcelable("post",postSend);
//                        intent.putExtra("list",comments);
//                        intent.putExtra("post",postSend);
//                        getActivity().startActivity(intent);
//                }
//            });

            if (item.getType().equals("image")) {
                loader.displayImage("http://foosip.com/uploads/posts/" + item.getPost(), holder.image, options);
                holder.image.setVisibility(View.VISIBLE);
                holder.post.setVisibility(View.VISIBLE);
                holder.post.setText(item.getDescription());
            } else {
                holder.post.setText(item.getPost());
                holder.image.setVisibility(View.GONE);
                holder.post.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profile;
            TextView name, type, post;
            ImageView image, img_chat, img_like;

            TextView likes, comments, text;
            LinearLayout ll;

            public ViewHolder(View itemView) {
                super(itemView);


                profile = itemView.findViewById(R.id.view5);
                name = itemView.findViewById(R.id.textView31);
                type = itemView.findViewById(R.id.textView58);
                post = itemView.findViewById(R.id.textView59);
                image = itemView.findViewById(R.id.imageView7);
                text = itemView.findViewById(R.id.textView57);
                ll = itemView.findViewById(R.id.ll);

                likes = itemView.findViewById(R.id.textView60);
                comments = itemView.findViewById(R.id.textView61);
                img_chat = itemView.findViewById(R.id.img_chat);
                img_like = itemView.findViewById(R.id.img_like);

            }
        }
    }

}
