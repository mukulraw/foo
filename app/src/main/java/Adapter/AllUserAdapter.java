package Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ratna.foosip.AllUsersActivity;
import com.ratna.foosip.ProfileActivity;
import com.ratna.foosip.R;
import com.ratna.foosip.UsersActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.UserVal;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ratna on 4/22/2018.
 */

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder>
        implements View.OnClickListener{

    private final Activity context;
    public List<UserVal> items;


    public AllUserAdapter(Activity context,List<UserVal> items) {
        this.context = context;
        this.items = items;

    }


    @Override
    public AllUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_single_layout, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final AllUserAdapter.ViewHolder holder, final int position) {

        if(!items.get(position).getProfile_pic().equals(null) && !items.get(position).getProfile_pic().equals("")) {
            Picasso.with(context).load(items.get(position).getProfile_pic()).placeholder(R.drawable.default_avatar).into(holder.userImageView);

        }

        holder.userNameView.setText(items.get(position).getFirst_name());

        holder.userStatusView.setVisibility(View.GONE);

        holder.rr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent profileIntent = new Intent(context, ProfileActivity.class);
                profileIntent.putExtra("user_id", items.get(position).getUid());
                context.startActivity(profileIntent);

            }
        });

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return items.size(); // the number of items in the list will be +1 the titles including the header view.
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userImageView;
        TextView userStatusView,userNameView;
        RelativeLayout rr;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

             rr = (RelativeLayout)itemView.findViewById(R.id.rr);
             userImageView = (CircleImageView) itemView.findViewById(R.id.user_single_image);
             userStatusView = (TextView) itemView.findViewById(R.id.user_single_status);
             userNameView = (TextView) itemView.findViewById(R.id.user_single_name);

        }


    }

}
