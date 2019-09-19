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

import com.ratna.foosip.HistoryDetails;
import com.ratna.foosip.PutInQueue;
import com.ratna.foosip.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ratna on 9/13/2016.
 */
public class HistoryAdapter extends  ArrayAdapter {
    private final Context context;
    private final ArrayList<HashMap<String, String>> items;

    public HistoryAdapter(Context context, ArrayList<HashMap<String, String>> items) {
        super(context,R.layout.item_history,items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_history, parent, false);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView txt_name = (TextView)rowView.findViewById(R.id.txt_name);
        TextView txt_dt = (TextView)rowView.findViewById(R.id.txt_dt);
        Picasso.with(context).load(items.get(position).get("logo"))
                .placeholder(R.drawable.ic_watermark).fit().into(imageView);


        txt_name.setText(items.get(position).get("rname"));
        txt_dt.setText(items.get(position).get("date1"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("temp_order_id", items.get(position).get("temp_order_id"));

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });



        return rowView;
    }
}
