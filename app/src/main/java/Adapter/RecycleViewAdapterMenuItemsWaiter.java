package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ratna.foosip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/22/2016.
 */
public class RecycleViewAdapterMenuItemsWaiter extends RecyclerView.Adapter<RecycleViewAdapterMenuItemsWaiter.ViewHolder>
        implements View.OnClickListener {

    private final Activity context;
    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    HashMap<String, String> hashMap_database;
    ImageView img_photo;


    public RecycleViewAdapterMenuItemsWaiter(Activity context, ArrayList<HashMap<String, String>> items,ImageView img_photo) {
        this.context = context;
        this.items = items;
        this.img_photo = img_photo;
        hashMap_database = new HashMap<>();
        databaseHelper = new DatabaseHelper(context);
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewAdapterMenuItemsWaiter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_menu, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterMenuItemsWaiter.ViewHolder holder, final int position) {

        Picasso.with(context).load(items.get(position).get("sub_cat_photo"))
                .placeholder(R.drawable.ic_watermark).fit().into(img_photo);
        holder.txt_prod_name.setText(items.get(position).get("item_name"));
        holder.txt_price.setText("Rs. " + items.get(position).get("item_price"));


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

        TextView txt_prod_name, txt_price;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);


        }


    }

}




