package com.example.mattkim.currencyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {
    private Context context;
    private List<LiveRate> items;
    private ClickListener mClickListener;

    public EventsAdapter(List<LiveRate> items, Context context) {
        this.items = items;
        this.context = context;

    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView titleTextView;
        TextView AuthorTV;

        EventViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.tv_title);
            AuthorTV = (TextView) v.findViewById(R.id.tv_author);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (mClickListener != null) mClickListener.onLongClick(view, getAdapterPosition());
            return false;
        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_event, parent, false);
        context = parent.getContext();
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        LiveRate item = items.get(position);
        holder.titleTextView.setText(item.getExchange());
        holder.AuthorTV.setText(""+item.getRate());
        final int index = position;

/*        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                items.get(index);
                MainActivity.showActionsDialog(index);
                return true;
            }
        });
        */

    }


    @Override
    public int getItemCount() {

        return items.size();
    }
    // allows clicks events to be caught
    void setClickListener(ClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

}