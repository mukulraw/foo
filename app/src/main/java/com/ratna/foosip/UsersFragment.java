package com.ratna.foosip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.allUsersPOJO.Datum;
import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SharedPreferences.SavedParameter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UsersFragment extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<Datum> list;
    AllUsersAdapter adapter;
    SavedParameter savedParameter;
    private DatabaseReference mUsersDatabase;
    RelativeLayout relativelayout;
    LinearLayout scan;
    LinearLayout ll_chat_bg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_users_fragment , container , false);

        savedParameter = new SavedParameter(getContext());

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
//        relativelayout = view.findViewById(R.id.parent);
        manager = new GridLayoutManager(getContext() , 2);
        list = new ArrayList<>();
        scan = view.findViewById(R.id.ll_scan);
        ll_chat_bg = view.findViewById(R.id.ll_chat_bg);


        adapter = new AllUsersAdapter(getContext() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    Date c = Calendar.getInstance().getTime();
//                    System.out.println("Current time => " + c);
//
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                    String today = df.format(c);
//
//
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(df.parse(today));
//                    cal.add(Calendar.DATE, 1);
//                    String tomorrow = df.format(cal.getTime());
//
//
//                    savedParameter.setDATESET(tomorrow);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(getContext(), ScanQR2.class);
                startActivity(intent);
            }
        });

        return view;

    }



            @Override
    public void onResume() {
        super.onResume();


//                Date c = Calendar.getInstance().getTime();
//                System.out.println("Current time => " + c);
//
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                String today = df.format(c);
//
//                if(savedParameter.getDATESET().equals(today))
//                {
//                    grid.setVisibility(View.GONE);
//                    ll_chat_bg.setVisibility(View.VISIBLE);
//                    scan.setVisibility(View.VISIBLE);
//                    relativelayout.setBackground(getActivity().getResources().getDrawable(R.drawable.active_bg));
//                }

                if (savedParameter.getRID().length() > 0) {
                    grid.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.GONE);
                    ll_chat_bg.setVisibility(View.GONE);
                } else {

                    grid.setVisibility(View.GONE);
                    scan.setVisibility(View.VISIBLE);
                    ll_chat_bg.setVisibility(View.VISIBLE);

                }


                progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        allUsersRequestBean body = new allUsersRequestBean();

        body.setRid(savedParameter.getRID());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type" , "application/json");
        map.put("Authorization" , savedParameter.getTOKEN());

        Call<allUsersBean> call = cr.getAllUsers(body , map);

        call.enqueue(new Callback<allUsersBean>() {
            @Override
            public void onResponse(Call<allUsersBean> call, Response<allUsersBean> response) {

                Log.e("imageres", new Gson().toJson(response.body()));
                try {

                    adapter.setGridData(response.body().getData());

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<allUsersBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }

    class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder>
    {

        List<Datum> list = new ArrayList<>();
        Context context;

        public AllUsersAdapter(Context context , List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_active_user , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage("http://foosip.com/" + item.getProfilePic() , holder.img_icon , options);

            Log.d("image" , "http://foosip.com/" + item.getProfilePic());

            mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(item.getUid());
            mUsersDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    String image = dataSnapshot.child("image").getValue().toString();
//
//                    Picasso.with(getActivity()).load(image).placeholder(R.drawable.default_avatar).into(holder.img_icon);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.txt_name.setText(item.getFirstName());


            holder.img_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(),ActiveUserActivity.class);
//                    intent.putExtra("img", "http://foosip.com/" + item.getProfilePic());
//                    context.startActivity(intent);


                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("uid", item.getUid());
                    profileIntent.putExtra("user_id",item.getUserId());
                    context.startActivity(profileIntent);
                }
            });


//            if (position == list.size() - 1)
//            {
//
//                holder.line.setVisibility(View.GONE);
//
//            }else
//            {
//
//                holder.line.setVisibility(View.VISIBLE);
//
//            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("uid", item.getUid());
                    profileIntent.putExtra("user_id",item.getUserId());
                    context.startActivity(profileIntent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

           // CircleImageView profile;
           // TextView name , status , age , line;

            ImageView img_icon;
            TextView txt_name;

            public ViewHolder(View itemView) {
                super(itemView);

                img_icon = itemView.findViewById(R.id.icon);
                txt_name = itemView.findViewById(R.id.txt_name);
//                status = itemView.findViewById(R.id.textView28);
//                age = itemView.findViewById(R.id.textView29);
//                line = itemView.findViewById(R.id.textView30);

            }
        }

    }

}
