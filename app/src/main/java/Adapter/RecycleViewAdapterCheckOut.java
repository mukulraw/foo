package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ratna.foosip.CheckOut;
import com.ratna.foosip.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/24/2016.
 */
public class RecycleViewAdapterCheckOut extends RecyclerView.Adapter<RecycleViewAdapterCheckOut.ViewHolder>
        implements View.OnClickListener{

    private final Activity context;
    public ArrayList<HashMap<String, String>> items;


    public RecycleViewAdapterCheckOut(Activity context,ArrayList<HashMap<String, String>> items) {
        this.context = context;
        this.items = items;

    }




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewAdapterCheckOut.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_out, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterCheckOut.ViewHolder holder, final int position) {

        holder.txt_prod_name.setText(items.get(position).get("item_name"));
        holder.txt_quan.setText(items.get(position).get("item_qty")+" x "+items.get(position).get("item_price"));
        float quan = Float.parseFloat(items.get(position).get("item_qty"));
        float price = Float.parseFloat(items.get(position).get("item_price"));
        float total_price = quan*price;
        holder.txt_price.setText("Rs. "+String.valueOf(total_price));

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

        TextView txt_prod_name,txt_quan,txt_price;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_quan = (TextView) itemView.findViewById(R.id.txt_quan);

        }


    }

}




