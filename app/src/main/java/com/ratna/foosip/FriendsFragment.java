package com.ratna.foosip;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import SharedPreferences.SavedParameter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment  {

    private RecyclerView mFriendsList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;

    FoosipChat foosipChat;



    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    SavedParameter savedParameter;
    static TextView txt_req_new;
    LinearLayout ll_div;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mFriendsList = (RecyclerView) mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        savedParameter = new SavedParameter(getActivity());

        mFriendsList.setHasFixedSize(true);
        mFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

            txt_req_new = (TextView)mMainView.findViewById(R.id.txt_req_new);
            ll_div = (LinearLayout)mMainView.findViewById(R.id.ll_div);


        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        foosipChat = (FoosipChat)getActivity().getApplicationContext();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(

                Friends.class,
                R.layout.users_single_layout,
                FriendsViewHolder.class,
                mFriendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder,final Friends friends, int i) {




                final String list_user_id = getRef(i).getKey();


//                boolean ischat = savedParameter.getIsChat(list_user_id,getActivity());
//                Toast.makeText(getActivity(),list_user_id + String.valueOf(i),Toast.LENGTH_LONG).show();

//                if(ischat)
//                {
//                    friendsViewHolder.mView.setVisibility(View.GONE);
//                }else{
//                    foosipChat.count_friends = 1;
//                }

                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();

                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        if(friends.getRequest_type().equals("received")) {
                            mFriendsList.setVisibility(View.VISIBLE);
                            txt_req_new.setVisibility(View.VISIBLE);
                            ll_div.setVisibility(View.VISIBLE);
                            txt_req_new.setText("New Friend Request");
                            friendsViewHolder.setDate(friends.getRequest_type());
                            friendsViewHolder.setName(userName,getContext(),list_user_id);
                            friendsViewHolder.setUserImage(userThumb, getContext(),list_user_id);
                        }else{
                            mFriendsList.setVisibility(View.GONE);
                            txt_req_new.setVisibility(View.GONE);
                            ll_div.setVisibility(View.GONE);
                        }
//                        friendsViewHolder.setName(userName,getContext(),list_user_id);
//                        friendsViewHolder.setUserImage(userThumb, getContext(),list_user_id);

                        friendsViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent chatIntent = new Intent(getContext(), ProfileActivity.class);
                                chatIntent.putExtra("uid", list_user_id);
                                chatIntent.putExtra("user_id",friends.getUser_id_serv());
                                startActivity(chatIntent);


//                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};
//
//                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//                                builder.setTitle("Select Options");
//                                builder.setItems(options, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                                        //Click Event for each item.
//                                        if(i == 0){

//                                            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
//                                            profileIntent.putExtra("user_id", list_user_id);
//                                            startActivity(profileIntent);

//                                        }
//
//                                        if(i == 1){

//                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
//                                            chatIntent.putExtra("user_id", list_user_id);
//                                            chatIntent.putExtra("user_name", userName);
//                                            startActivity(chatIntent);

//                                        }
//
//                                    }
//                                });
//
//                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        mFriendsList.setAdapter(friendsRecyclerViewAdapter);


    }


    public void DisplayTextChange(String str) {
        txt_req_new.setText(str);
    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDate(String date){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(date);

        }

        public void setName(final String name,final Context ctx, final String userid){

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

            userNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent chatIntent = new Intent(ctx, ChatActivity.class);
//                    chatIntent.putExtra("user_id", userid);
//                    chatIntent.putExtra("user_name", name);
//                    ctx.startActivity(chatIntent);
                }
            });

        }

        public void setUserImage(String thumb_image, final Context ctx, final String userid){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);
            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

            userImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent profileIntent = new Intent(ctx, ProfileActivity.class);
//                    profileIntent.putExtra("user_id", userid);
//                    ctx.startActivity(profileIntent);
                }
            });

        }

        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) mView.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }


}
