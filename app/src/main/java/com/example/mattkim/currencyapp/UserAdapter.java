package com.example.mattkim.currencyapp;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static com.example.mattkim.currencyapp.MainActivity.adapter;
import static com.example.mattkim.currencyapp.MainActivity.recyclerView;

//import static com.example.signoril.earthquakesroomrecyclerview.MainActivity.context;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context mContext;  //get the parent context for use in startActivity and to pass to deleteEQID and Room.databaseBuilders
    List<RateRecord> items;
    AppDatabase db;
    RateRecord fred;

    public UserAdapter(List<RateRecord> items) {
        this.items = items;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event,parent,false);
        mContext = parent.getContext();
        db = Room.databaseBuilder(mContext, AppDatabase.class,AppDatabase.NAME).build();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {
        holder.exchange.setText("" + items.get(position).getExchange());
        holder.rate.setText("" + items.get(position).getRate());
        //Toast toast = Toast.makeText(MainActivity.context, "Mag :" + items.get(position).getMagnitude(), Toast.LENGTH_SHORT);
        //toast.show();
        final int index = position;
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                items.get(index);
                fred = items.get(index);
                new  deleteEQID( db, recyclerView, adapter,  mContext ,  items, index).execute(items.get(index).getExchange());
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        else
            return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exchange;
        public TextView rate;

        public ViewHolder(View itemView) {
            super(itemView);
            exchange = itemView.findViewById(R.id.tv_title);
            rate = itemView.findViewById(R.id.tv_author);

        }
    }
}
