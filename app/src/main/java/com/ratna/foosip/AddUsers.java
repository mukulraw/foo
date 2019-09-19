package com.ratna.foosip;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.allUsersPOJO.Datum;
import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SharedPreferences.SavedParameter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddUsers extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    ProgressBar progress;
    FloatingActionButton add;
    GridLayoutManager manager;
    SavedParameter savedParameter;
    List<Datum> list;
    AllUsersAdapter adapter;

    List<checkBean> ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_users);

        savedParameter = new SavedParameter(this);
        list = new ArrayList<>();
        ll = new ArrayList<>();

        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        progress = findViewById(R.id.progressBar3);
        add = findViewById(R.id.floatingActionButton2);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Create Group");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        manager = new GridLayoutManager(this , 1);

        adapter = new AllUsersAdapter(this , list , ll);

        grid.setLayoutManager(manager);
        grid.setAdapter(adapter);



        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        allUsersRequestBean body = new allUsersRequestBean();

        body.setRid(savedParameter.getQrCode());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type" , "application/json");
        map.put("Authorization" , savedParameter.getTOKEN());

        Call<allUsersBean> call = cr.getAllUsers(body , map);

        call.enqueue(new Callback<allUsersBean>() {
            @Override
            public void onResponse(Call<allUsersBean> call, Response<allUsersBean> response) {

                try {
                    ll.clear();

                    for (int i = 0 ; i < response.body().getData().size() ; i++)
                    {
                        checkBean cb = new checkBean();
                        cb.setUserId(response.body().getData().get(i).getUserId());
                        cb.setCheck(false);
                        cb.setName(response.body().getData().get(i).getFirstName());
                        ll.add(cb);
                    }



                    adapter.setGridData(response.body().getData() , ll);
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



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<checkBean> cc = adapter.getChecked();

                final List<String> ud = new ArrayList<>();
                final List<String> nam = new ArrayList<>();

                for (int i = 0 ; i < cc.size() ; i++)
                {
                    if (cc.get(i).getCheck())
                    {
                        ud.add(cc.get(i).getUserId());
                        nam.add(cc.get(i).getName());
                    }
                }

                Log.d("size" , TextUtils.join(",", ud));


                if (ud.size() > 0)
                {

                    if (ud.size() == 1)
                    {

                        progress.setVisibility(View.VISIBLE);

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://foosip.com/")
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);

                        Call<createGroupBean> call = cr.createGroup(savedParameter.getUID() , savedParameter.getQrCode() , nam.get(0) , TextUtils.join(",", ud));

                        call.enqueue(new Callback<createGroupBean>() {
                            @Override
                            public void onResponse(Call<createGroupBean> call, Response<createGroupBean> response) {

                                progress.setVisibility(View.GONE);
//                                Toast.makeText(AddUsers.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                finish();

                            }

                            @Override
                            public void onFailure(Call<createGroupBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });

                    }
                    else
                    {


                        final Dialog dialog = new Dialog(AddUsers.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(true);
                        dialog.setContentView(R.layout.group_name_dialog);
                        dialog.show();


                        final EditText na = dialog.findViewById(R.id.editText3);
                        final ProgressBar bar = dialog.findViewById(R.id.progressBar4);
                        Button sub = dialog.findViewById(R.id.button);

                        sub.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String n = na.getText().toString();

                                if (n.length() > 0)
                                {

                                    bar.setVisibility(View.VISIBLE);

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("http://foosip.com/")
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                                    Call<createGroupBean> call = cr.createGroup(savedParameter.getUID() , savedParameter.getQrCode() , n , TextUtils.join(",", ud));

                                    call.enqueue(new Callback<createGroupBean>() {
                                        @Override
                                        public void onResponse(Call<createGroupBean> call, Response<createGroupBean> response) {

                                            bar.setVisibility(View.GONE);
                                            dialog.dismiss();
//                                            Toast.makeText(AddUsers.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                            finish();

                                        }

                                        @Override
                                        public void onFailure(Call<createGroupBean> call, Throwable t) {
                                            bar.setVisibility(View.GONE);
                                        }
                                    });

                                }
                                else
                                {
                                    Toast.makeText(AddUsers.this , "Please enter a group name" , Toast.LENGTH_SHORT).show();
                                }


                            }
                        });





                    }

                }
                else
                {
                    Toast.makeText(AddUsers.this , "Please select user" , Toast.LENGTH_SHORT).show();
                }


            }
        });



    }



    class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder>
    {

        List<Datum> list = new ArrayList<>();
        Context context;
        List<checkBean> ll = new ArrayList<>();

        public AllUsersAdapter(Context context , List<Datum> list , List<checkBean> ll)
        {
            this.context = context;
            this.list = list;
            this.ll = ll;
        }


        public void setGridData(List<Datum> list , List<checkBean> ll)
        {
            this.list = list;
            this.ll = ll;
            notifyDataSetChanged();
        }


        public List<checkBean> getChecked() {
            return ll;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.add_user_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final Datum item = list.get(position);

            checkBean it = ll.get(position);


            if (it.getCheck())
            {
                holder.check.setChecked(true);
            }
            else
            {
                holder.check.setChecked(false);
            }


            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked)
                    {
                        checkBean cb = new checkBean();
                        cb.setCheck(true);
                        cb.setUserId(item.getUserId());
                        cb.setName(item.getFirstName());
                        ll.set(position , cb);
                    }
                    else
                    {
                        checkBean cb = new checkBean();
                        cb.setCheck(false);
                        cb.setUserId(item.getUserId());
                        cb.setName(item.getFirstName());
                        ll.set(position , cb);
                    }


                }
            });


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage("http://foosip.com/" + item.getProfilePic() , holder.profile , options);

            Log.d("image" , "http://foosip.com/" + item.getProfilePic());

            holder.name.setText(item.getFirstName());


            if (position == list.size() - 1)
            {

                holder.line.setVisibility(View.GONE);

            }else
            {

                holder.line.setVisibility(View.VISIBLE);

            }

            /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("user_id", item.getUid());
                    context.startActivity(profileIntent);
                }
            });
*/
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            CircleImageView profile;
            TextView name , line;
            CheckBox check;

            public ViewHolder(View itemView) {
                super(itemView);

                profile = itemView.findViewById(R.id.view);
                name = itemView.findViewById(R.id.textView18);
                check = itemView.findViewById(R.id.checkBox2);
                line = itemView.findViewById(R.id.textView30);

            }
        }

    }

}
