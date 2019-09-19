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
public class RecycleViewAdapterMenuItems extends RecyclerView.Adapter<RecycleViewAdapterMenuItems.ViewHolder>
        implements View.OnClickListener {

    private final Activity context;
    public ArrayList<HashMap<String, String>> items;
    DatabaseHelper databaseHelper;
    HashMap<String, String> hashMap_database;
    ImageView img_photo;


    public RecycleViewAdapterMenuItems(Activity context, ArrayList<HashMap<String, String>> items,ImageView img_photo) {
        this.context = context;
        this.items = items;
        this.img_photo = img_photo;
        hashMap_database = new HashMap<>();
        databaseHelper = new DatabaseHelper(context);
    }


    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecycleViewAdapterMenuItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_food, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecycleViewAdapterMenuItems.ViewHolder holder, final int position) {

        Picasso.with(context).load(items.get(position).get("sub_cat_photo"))
                .placeholder(R.drawable.ic_watermark).fit().into(img_photo);

        holder.txt_prod_name.setText(items.get(position).get("item_name"));
        holder.txt_price.setText("Rs. " + items.get(position).get("item_price"));
        boolean prod_exists = databaseHelper.getProductName(items.get(position).get("item_name"));
        if (prod_exists) {
            String quan = databaseHelper.getQuantity(items.get(position).get("item_name"));
            holder.txt_quan.setText(quan);
        } else {
            holder.txt_quan.setText("0");
        }

        holder.ll_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String value = holder.txt_quan.getText().toString();
                int i = Integer.parseInt(value);
                if (i > 0 && i != 0) {
                    holder.txt_quan.setText(String.valueOf(--i));
//                        Toast.makeText(context,String.valueOf(i),Toast.LENGTH_SHORT).show();
                    databaseHelper.update_byNAME(items.get(position).get("item_name"), String.valueOf(i));

                }
                if (i == 0) {
                    delete(items.get(position).get("item_name"));
                }


            }
        });


        holder.ll_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean prod_exists = databaseHelper.getProductName(holder.txt_prod_name.getText().toString());
                if (prod_exists) {
                    String value = holder.txt_quan.getText().toString();
                    int i = Integer.parseInt(value);
                    holder.txt_quan.setText(String.valueOf(++i));
                    databaseHelper.update_byNAME(items.get(position).get("item_name"), String.valueOf(i));
                } else {

                    String value = holder.txt_quan.getText().toString();
                    int i = Integer.parseInt(value);
                    holder.txt_quan.setText(String.valueOf(++i));

                    hashMap_database.put("item_id", items.get(position).get("item_id"));
                    hashMap_database.put("item_name",items.get(position).get("item_name"));
                    hashMap_database.put("item_price",items.get(position).get("item_price"));
                    hashMap_database.put("item_quantity", String.valueOf(i));
                    databaseHelper.insertCartProduct(hashMap_database);
                }

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

    public void delete(String nm) { //removes the row
        databaseHelper.deleteRecord(nm);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_prod_name, txt_price, txt_quan;
        LinearLayout ll_plus, ll_minus;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            ll_minus = (LinearLayout) itemView.findViewById(R.id.ll_minus);
            ll_plus = (LinearLayout) itemView.findViewById(R.id.ll_plus);
            txt_quan = (TextView) itemView.findViewById(R.id.txt_quan);


        }


    }

}




