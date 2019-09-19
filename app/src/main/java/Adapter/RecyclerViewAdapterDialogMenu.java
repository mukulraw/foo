package Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ratna.foosip.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/23/2016.
 */
public class RecyclerViewAdapterDialogMenu extends RecyclerView.Adapter<RecyclerViewAdapterDialogMenu.ViewHolder>
        implements View.OnClickListener{

    private final Activity context;
    public ArrayList<String> list_item;


    public RecyclerViewAdapterDialogMenu(Activity context, ArrayList<String> list_item) {
        this.context = context;
        this.list_item = list_item;

    }




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    @Override
    public RecyclerViewAdapterDialogMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_menu, parent, false); //Inflating the layout

        ViewHolder vhItem = new ViewHolder(v, viewType); //Creating ViewHolder and passing the object of type view

        return vhItem; // Returning the created object


    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapterDialogMenu.ViewHolder holder, final int position) {

        holder.txt_prod_name.setText(list_item.get(position));

    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return list_item.size(); // the number of items in the list will be +1 the titles including the header view.
    }


    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public void onClick(View v) {

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_prod_name;


        public ViewHolder(final View itemView, int ViewType) {
            super(itemView);

            txt_prod_name = (TextView) itemView.findViewById(R.id.txt_item);

        }


    }

}




