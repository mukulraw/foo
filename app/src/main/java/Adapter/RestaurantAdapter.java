package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratna.foosip.PutInQueue;
import com.ratna.foosip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ratna on 12/5/2016.
 */
public class RestaurantAdapter extends  ArrayAdapter {
    private final Context context;
    private final ArrayList<HashMap<String, String>> items;

    public RestaurantAdapter(Context context, ArrayList<HashMap<String, String>> items) {
        super(context,R.layout.item_restaurant,items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_restaurant, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txt_name = (TextView)rowView.findViewById(R.id.txt_name);
        TextView txt_address = (TextView)rowView.findViewById(R.id.txt_address);


        txt_name.setText(items.get(position).get("name"));
        //txt_address.setText(items.get(position).get("address")+", "+items.get(position).get("res_area")+items.get(position).get("extras"));
        if(items.get(position).get("available").equals("0")) {
            txt_address.setText("Available");
        }else{
            txt_address.setText("Not Available");
        }
        Picasso.with(context).load(items.get(position).get("logo"))
                .placeholder(R.drawable.ic_watermark).fit().into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PutInQueue.class);
                Bundle bundle  = new Bundle();
                bundle.putString("rid",items.get(position).get("rid"));
                bundle.putString("seats",items.get(position).get("seats"));
                bundle.putString("name",items.get(position).get("name"));
                bundle.putString("address",items.get(position).get("address"));
                bundle.putString("available",items.get(position).get("available"));
                bundle.putString("res_area",items.get(position).get("res_area"));
                bundle.putString("rating",items.get(position).get("rating"));
                bundle.putString("waiting",items.get(position).get("waiting"));
                bundle.putString("logo",items.get(position).get("logo"));
                bundle.putString("open_time",items.get(position).get("open_time"));
                bundle.putString("close_time",items.get(position).get("close_time"));
                bundle.putString("pin",items.get(position).get("pin"));
                bundle.putString("phone",items.get(position).get("phone"));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



        return rowView;
    }
}
