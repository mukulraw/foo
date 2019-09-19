//package com.ratna.foosip;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import SharedPreferences.SavedParameter;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.scalars.ScalarsConverterFactory;
//
//public class ChatFragment extends Fragment {
//
//    RecyclerView grid;
//    ProgressBar progress;
//    SavedParameter savedParameter;
//    GridLayoutManager manager;
//    List<groupListBean> list;
//    GroupAdapter adapter;
//    FloatingActionButton add;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.chat_fragment_layout , container , false);
//
//
//        list = new ArrayList<>();
//        savedParameter = new SavedParameter(getContext());
//        grid = view.findViewById(R.id.grid);
//        progress = view.findViewById(R.id.progressBar2);
//        manager = new GridLayoutManager(getContext() , 1);
//        adapter = new GroupAdapter(getContext() , list);
//        add = view.findViewById(R.id.floatingActionButton);
//
//        Log.d("rrrid" , savedParameter.getQrCode());
//        Log.d("userId" , savedParameter.getUID());
//
//
//        grid.setAdapter(adapter);
//        grid.setLayoutManager(manager);
//
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent intent = new Intent(getContext() , AddUsers.class);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        progress.setVisibility(View.VISIBLE);
//
//        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://foosip.com/")
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        final AllAPIs cr = retrofit.create(AllAPIs.class);
//
//        Call<List<groupListBean>> call = cr.getGroupList(savedParameter.getUID() , savedParameter.getQrCode());
//
//        call.enqueue(new Callback<List<groupListBean>>() {
//            @Override
//            public void onResponse(Call<List<groupListBean>> call, Response<List<groupListBean>> response) {
//
//                try {
//
//                    if (response.body().size() > 0)
//                    {
//                        adapter.setGridData(response.body());
//                    }
//
//                }catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//
//
//
//                progress.setVisibility(View.GONE);
//
//            }
//
//            @Override
//            public void onFailure(Call<List<groupListBean>> call, Throwable t) {
//                progress.setVisibility(View.GONE);
//            }
//        });
//
//    }
//
//    class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>
//    {
//
//        Context context;
//        List<groupListBean> list = new ArrayList<>();
//
//
//        public GroupAdapter(Context context , List<groupListBean> list)
//        {
//            this.list = list;
//            this.context = context;
//        }
//
//        public void setGridData(List<groupListBean> list)
//        {
//            this.list = list;
//            notifyDataSetChanged();
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate(R.layout.group_list_model , parent , false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//
//            final groupListBean item = list.get(position);
//
//            holder.groupName.setText(item.getGroupName());
//            holder.message.setText(item.getLastMessage());
//            holder.time.setText(item.getLastMessageTime());
//
//
//            if (position == list.size() - 1)
//            {
//                holder.line.setVisibility(View.GONE);
//            }
//            else
//            {
//                holder.line.setVisibility(View.VISIBLE);
//            }
//
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(context , Chat.class);
//                    intent.putExtra("groupId" , item.getId());
//                    intent.putExtra("name" , item.getGroupName());
//                    context.startActivity(intent);
//
//                }
//            });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//
//        class ViewHolder extends RecyclerView.ViewHolder
//        {
//
//            TextView groupName , message , time , line;
//
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//
//                groupName = itemView.findViewById(R.id.textView36);
//                message = itemView.findViewById(R.id.textView38);
//                time = itemView.findViewById(R.id.textView37);
//                line = itemView.findViewById(R.id.textView40);
//
//            }
//        }
//    }
//
//
//}
