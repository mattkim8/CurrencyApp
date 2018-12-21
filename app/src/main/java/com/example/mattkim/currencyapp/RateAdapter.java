package com.example.mattkim.currencyapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class RateAdapter extends RecyclerView.Adapter<RateAdapter.EventViewHolder> {
    private Context context;
    private List<RateRecord2> items;
    RecyclerView recyclerView;
    private ClickListener mClickListener;

    public RateAdapter(List<RateRecord2> items, Context context) {
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
            notifyDataSetChanged();
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
        RateRecord2 item = items.get(position);
        holder.titleTextView.setText(item.getExchange());
        holder.AuthorTV.setText(""+item.getRate());
        final int index = position;



       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: fsadfafdasdf");
                items.get(index);
                new deleteTask().execute(index);
                notifyDataSetChanged();
                return true;
            }
        });


    }

    private class deleteTask extends AsyncTask<Integer, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            Log.d(TAG, "doInBackground: afsfasfd");
            Log.d(TAG, "doInBackground:" + items.get(params[0]).toString());
            EventDatabase.getEventDatabase(context).eventDao().deleteEvent(items.get(params[0]));
            return "s";
        }

        @Override
        protected void onPostExecute(String s){
            notifyDataSetChanged();
            Log.d(TAG, "onPostExecute: ddddddddd");
            Log.d(TAG, "onPostExecute: dfafasdfasd");


        }


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