package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ratna.foosip.AboutUs;
import com.ratna.foosip.Feedback;
import com.ratna.foosip.History;
import com.ratna.foosip.Home;
import com.ratna.foosip.Profile;
import com.ratna.foosip.R;
import com.ratna.foosip.ReferApp;
import com.ratna.foosip.SignIn;

import SharedPreferences.UserSession;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Vikas on 11/18/2015.
 */
public class RecycleViewAdapterItemNavDrawer extends RecyclerView.Adapter<RecycleViewAdapterItemNavDrawer.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener,View.OnTouchListener {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[];
    private int mIcons[];





    private Bitmap Bitmap_profile;
    Context context;

    UserSession userSession;




    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public RecycleViewAdapterItemNavDrawer(Context context, String Titles[], int Icons[], String Email){
        mNavTitles = Titles;
        mIcons = Icons;
        this.context=context;
        userSession = new UserSession(context);



    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;
        TextView textView,txt_name,txt_email;
        ImageView imageView;
        CircleImageView profile;
        LinearLayout linearLayout;




        public ViewHolder(final View itemView,int ViewType) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                linearLayout=(LinearLayout)itemView.findViewById(R.id.linear_layout_nav_bar);
                Holderid = 1;
            }
            else{
                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_email = (TextView) itemView.findViewById(R.id.txt_email);
                profile = (CircleImageView)itemView.findViewById(R.id.circleView);

                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }


    }






    @Override
    public RecycleViewAdapterItemNavDrawer.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterItemNavDrawer.ViewHolder holder, final int position) {
        if(holder.Holderid ==1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    if (position == 1) {
//
//                        ((Home) context).onClose();
//
//                    }
                    if (position == 1) {
                        Intent intent = new Intent(context, History.class);
                        context.startActivity(intent);

                    }
//                    if (position == 3) {
//                        Intent intent = new Intent(context, LogIn.class);
//                        context.startActivity(intent);
//                    }
                    if (position == 2) {
                        Intent intent = new Intent(context, ReferApp.class);
                        context.startActivity(intent);
                    }
                    if (position == 3) {
                        Intent intent = new Intent(context, Feedback.class);
                        context.startActivity(intent);

                    }
                    if (position == 4) {
                        Intent intent = new Intent(context, AboutUs.class);
                        context.startActivity(intent);

                    }

                    if (position == 5) {
                        userSession.setUserLogIn(false);
                        Intent intent = new Intent(context, SignIn.class);
                        context.startActivity(intent);
                        ((Home) context).finishAffinity();
                    }
//                    if (position == 5) {
//                        Intent intent = new Intent(context, FeedBack.class);
//                        context.startActivity(intent);
//
//                    }
            }
        });


        }
        else{
            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Profile.class);
                    context.startActivity(intent);
                }
            });

        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}