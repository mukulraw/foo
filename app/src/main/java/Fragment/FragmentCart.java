package Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ratna.foosip.OrderScreen;
import com.ratna.foosip.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import Adapter.RecycleViewAdapterCart;
import Utils.DatabaseHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ratna on 9/23/2016.
 */
public class FragmentCart extends Fragment {

    static Context context;


    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;

    RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<HashMap<String, String>> items;
    private static DatabaseHelper databaseHelper;




    public FragmentCart() {
        // Required empty public constructor

    }

    public static FragmentCart newInstance(Context context1) {
        context = context1;
        databaseHelper = new DatabaseHelper(context);
        items = new ArrayList<>();
        FragmentCart f = new FragmentCart();
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_cart, container, false);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();

    }

    private void setView() {

        items = databaseHelper.getCartProduct();


        mRecyclerView = (RecyclerView) getView().findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecycleViewAdapterCart(getActivity(),items);
        mRecyclerView.setAdapter(mAdapter);
        // mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager =
                new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        /*
        mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        */
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Add MyItemDecoration
        //  mRecyclerView.addItemDecoration(new DividerItemDecoration(Home.this, null));

        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(context)
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.divider, R.dimen.divider)
                        .build());

        LinearLayout ll_proceed = (LinearLayout)getView().findViewById(R.id.ll_proceed);
        ll_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderScreen.class);
                startActivity(intent);

            }
        });

    }


}
