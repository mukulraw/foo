package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ratna.foosip.OrderScreen;
import com.ratna.foosip.R;

import java.util.ArrayList;
import java.util.HashMap;

import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/23/2016.
 */
public class RecyclerViewAdapterOrder extends RecyclerView.Adapter<RecyclerViewAdapterOrder.ViewHolder>
        implements View.OnClickListener{


    private final Activity context;
    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    HashMap<String, String> hashMap_database;


    public RecyclerViewAdapterOrder(Activity context, ArrayList<HashMap<String, String>> items) {
        this.context = context;
        this.items = items;
        hashMap_database = new HashMap<>();
        databaseHelper = new DatabaseHelper(context);
    }




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecyclerViewAdapterOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_order, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterOrder.ViewHolder holder, final int position) {

//        if(items.get(position).get("user_order_status").equals("p")) {
//            ((OrderScreen)context).HasItems();
            holder.txt_prod_name.setText(items.get(position).get("item_name"));
            holder.txt_price.setText("Rs. " + items.get(position).get("item_price"));
            holder.txt_quan.setText(items.get(position).get("item_qty"));
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderScreen) context).popUpWindow(view,items.get(position).get("item_id"), items.get(position).get("item_name"), items.get(position).get("item_qty"), items.get(position).get("user_order_status"));

                }
});
//        }else{
//        ((OrderScreen)context).NoItems();
//        }

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

        TextView txt_prod_name,txt_price,txt_quan;
        ImageView img_edit;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_quan = (TextView) itemView.findViewById(R.id.txt_quan);
            img_edit = (ImageView) itemView.findViewById(R.id.img_edit);

        }


    }

}




