package Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratna.foosip.CommentSend;
import com.ratna.foosip.Comments;
import com.ratna.foosip.OrderSummary;
import com.ratna.foosip.ProfileActivity;
import com.ratna.foosip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Item;
import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapterComment extends RecyclerView.Adapter<RecyclerAdapterComment.ViewHolder>
        implements View.OnClickListener{


    private final Activity context;
    //    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    HashMap<String, String> hashMap_database;
    private ArrayList<CommentSend> item;




    public RecyclerAdapterComment(Activity context, ArrayList<CommentSend> item) {
        this.context = context;
        this.item = item;
        hashMap_database = new HashMap<>();
        databaseHelper = new DatabaseHelper(context);
    }




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecyclerAdapterComment.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false); //Inflating the layout

        RecyclerAdapterComment.ViewHolder vhItem = new RecyclerAdapterComment.ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecyclerAdapterComment.ViewHolder holder, final int position) {


       holder.txt_username.setText(item.get(position).getUser_name());
       holder.txt_message.setText(item.get(position).getComment());
       holder.txt_username.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent profileIntent = new Intent(context, ProfileActivity.class);
               profileIntent.putExtra("uid", item.get(position).getUid());
               profileIntent.putExtra("user_id",item.get(position).getUser_id());
               context.startActivity(profileIntent);
           }
       });




    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return item.size(); // the number of items in the list will be +1 the titles including the header view.
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_username,txt_message;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_username = (TextView) itemView.findViewById(R.id.txt_user_name);
            txt_message = (TextView) itemView.findViewById(R.id.txt_message);



        }


    }

}





