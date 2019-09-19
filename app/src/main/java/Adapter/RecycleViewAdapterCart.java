package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ratna.foosip.R;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/22/2016.
 */
public class RecycleViewAdapterCart extends RecyclerView.Adapter<RecycleViewAdapterCart.ViewHolder>
        implements View.OnClickListener {

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;
    private final Activity context;
    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;


    public RecycleViewAdapterCart(Activity context, ArrayList<HashMap<String, String>> items) {
        this.context = context;
        this.items = items;
        databaseHelper = new DatabaseHelper(context);
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewAdapterCart.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_out, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_cart, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created
        }
        return null;

    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterCart.ViewHolder holder, final int position) {
        if (holder.Holderid == 1) {
            holder.txt_prod_name.setText(items.get(position-1).get("item_name"));
            holder.txt_price.setText("Rs. " + items.get(position-1).get("item_price"));
            holder.txt_quan.setText(items.get(position-1).get("item_quantity"));


        } else {

        }

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return items.size() + 1; // the number of items in the list will be +1 the titles including the header view.
    }


    @Override
    public void onClick(View v) {

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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_prod_name, txt_price, txt_quan;
        int Holderid;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {

                txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
                txt_price = (TextView) itemView.findViewById(R.id.txt_price);
                txt_quan = (TextView) itemView.findViewById(R.id.txt_quan);
                Holderid = 1;
            } else {
                Holderid = 0;
            }
        }


    }


}



