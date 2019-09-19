package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ratna.foosip.CancelQueue;
import com.ratna.foosip.OrderSummary;
import com.ratna.foosip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Item;
import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 12/6/2016.
 */
public class RecycleViewResQueue extends RecyclerView.Adapter<RecycleViewResQueue.ViewHolder>
        implements View.OnClickListener {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private final Activity context;
    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    String token_no;


    public RecycleViewResQueue(Activity context, ArrayList<HashMap<String, String>> items, String token_no) {
        this.context = context;
        this.items = items;
        this.token_no = token_no;
        databaseHelper = new DatabaseHelper(context);
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewResQueue.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_res_queue, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_res_queue, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;




    }

    @Override
    public void onBindViewHolder(final RecycleViewResQueue.ViewHolder holder, final int position) {

        if (holder.Holderid == 1) {


            Picasso.with(context).load(items.get(position-1).get("logo"))
                    .placeholder(R.drawable.ic_watermark).fit().into(holder.imageView);

            holder.img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CancelQueue(context);
                }
            });


            holder.txt_name.setText(items.get(position-1).get("rname"));

            if (items.get(position-1).get("status").equals("w")) {
                holder.txt_status.setText("Waiting-" + token_no);
            }
            if (items.get(position-1).get("status").equals("c")) {
                holder.txt_status.setText("Cancelled");
            }
            if (items.get(position-1).get("status").equals("a")) {
                holder.txt_status.setText("Available   ");
            }
        }


    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return items.size()+1 ; // the number of items in the list will be +1 the titles including the header view.
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onClick(View v) {

    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_status;
        ImageView imageView,img_cancel;
        int Holderid;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {

                txt_name = (TextView) itemView.findViewById(R.id.txt_name);
                txt_status = (TextView) itemView.findViewById(R.id.txt_status);
                imageView = (ImageView) itemView.findViewById(R.id.icon);
                img_cancel = (ImageView)itemView.findViewById(R.id.img_cancel);
                Holderid = 1;
            } else {
                Holderid = 0;
            }
        }


    }

}





