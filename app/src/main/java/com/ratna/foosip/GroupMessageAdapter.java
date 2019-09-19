package com.ratna.foosip;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import Adapter.ClickListenerChatFirebase;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class GroupMessageAdapter extends FirebaseRecyclerAdapter<Messages ,GroupMessageAdapter.MessageViewHolder> {


    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private DatabaseReference mUserDatabase;
    private String mCurrentUserId;



    private ClickListenerChatFirebase mClickListenerChatFirebase;

    private String nameUser;

    public GroupMessageAdapter(DatabaseReference ref) {
        super(Messages.class, R.layout.item_message_left, GroupMessageAdapter.MessageViewHolder.class, ref);
//        this.nameUser = nameUser;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
        mCurrentUserId =  FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == RIGHT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == LEFT_MSG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new MessageViewHolder(view);
        } else if (viewType == RIGHT_MSG_IMG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img, parent, false);
            return new MessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img, parent, false);
            return new MessageViewHolder(view);
        }


    }


//    @Override
//    public int getItemCount() {
//        return mMessageList.size();
//    }

    @Override
    public int getItemViewType(int position) {
        Messages model = getItem(position);
        if (model.getType().equals("text")) {
            if (model.getFrom().equals(mCurrentUserId)) {
                return RIGHT_MSG;
            } else {
                return LEFT_MSG;
            }
        }else if(model.getType().equals("image")) {
            if (model.getFrom().equals(mCurrentUserId)) {
                return RIGHT_MSG_IMG;
            } else {
                return LEFT_MSG_IMG;
            }
        }
        else {
            return RIGHT_MSG;
       }

    }




    @Override
    protected void populateViewHolder(final GroupMessageAdapter.MessageViewHolder viewHolder, Messages model, int position) {

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getFrom());


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.setIvUser(image);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(model.getType().equals("text")) {
            viewHolder.setTxtMessage(model.getMessage());
            viewHolder.setTvTimestamp(String.valueOf(model.getTime()));
        }
//        viewHolder.tvIsLocation(View.GONE);
//
        if (model.getType().equals("image")){
            viewHolder.tvIsLocation(View.GONE);
            viewHolder.setIvChatPhoto(model.getMessage());
            viewHolder.setTvTimestamp(String.valueOf(model.getTime()));
        }
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimestamp,tvLocation;
        TextView txtMessage;
        ImageView ivChatPhoto;
        CircleImageView ivUser;

        public MessageViewHolder(View view) {
            super(view);

            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            txtMessage = (TextView)itemView.findViewById(R.id.txtMessage);
            tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
            ivChatPhoto = (ImageView)itemView.findViewById(R.id.img_chat);
            ivUser = (CircleImageView)itemView.findViewById(R.id.ivUserChat);

        }


        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            txtMessage.setText(message);
        }

        public void setIvUser(String urlPhotoUser){
            if (ivUser == null)return;
           // Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40,40).into(ivUser);
            Picasso.with(ivUser.getContext()).load(urlPhotoUser)
                    .placeholder(R.drawable.default_avatar).into(ivUser);
        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url){
            if (ivChatPhoto == null)return;
            Glide.with(ivChatPhoto.getContext()).load(url).into(ivChatPhoto);
//            ivChatPhoto.setOnClickListener();
        }

        public void tvIsLocation(int visible){
            if (tvLocation == null)return;
            tvLocation.setVisibility(visible);
        }

    }

    private CharSequence converteTimestamp(String mileSegundos){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }
}
