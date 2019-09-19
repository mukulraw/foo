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

import com.ratna.foosip.OrderScreen;
import com.ratna.foosip.OrderSummary;
import com.ratna.foosip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.Item;
import Utils.DatabaseHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 11/21/2016.
 */

public class RecycleViewAdapterOrderSummary extends RecyclerView.Adapter<RecycleViewAdapterOrderSummary.ViewHolder>
        implements View.OnClickListener{


    private final Activity context;
//    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    HashMap<String, String> hashMap_database;
    private List<Item> item;




    public RecycleViewAdapterOrderSummary(Activity context, List<Item> item) {
        this.context = context;
        this.item = item;
        hashMap_database = new HashMap<>();
        databaseHelper = new DatabaseHelper(context);
    }




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewAdapterOrderSummary.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary, parent, false); //Inflating the layout

        RecycleViewAdapterOrderSummary.ViewHolder vhItem = new RecycleViewAdapterOrderSummary.ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterOrderSummary.ViewHolder holder, final int position) {


            holder.checkBox.setChecked(item.get(position).isChecked());
            holder.checkBox.setTag(item.get(position));

        holder.txt_prod_name.setText(item.get(position).getPrd_name());
            holder.txt_price.setText("Rs. " + item.get(position).getPrice());
            holder.txt_quan.setText(item.get(position).getQty());
            holder.img_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((OrderSummary) context).popUpWindow(view,item.get(position).getId(), item.get(position).getPrd_name(), item.get(position).getQty(), item.get(position).getUser_order_status());

                }
            });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckBox cb = (CheckBox) v;
                Item items = (Item) cb.getTag();

                items.setChecked(cb.isChecked());
                item.get(position).setChecked(cb.isChecked());

//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();

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

        TextView txt_prod_name,txt_price,txt_quan;
        ImageView img_edit;
        FrameLayout frame;
        CheckBox checkBox;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_quan = (TextView) itemView.findViewById(R.id.txt_quan);
            img_edit = (ImageView) itemView.findViewById(R.id.img_edit);
            frame =  (FrameLayout)itemView.findViewById(R.id.frame);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);


        }


    }

}





