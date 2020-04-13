package com.driven.quakes;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


class QuakeCardAdapter extends RecyclerView.Adapter<QuakeCardAdapter.ViewHolder> {


    private String[] names;
    private String[] location;
    private float[] magnitude;
    private String[] depth;
    private Date[] date;
    private Listener listener;

    interface Listener {
        void onClick(int position);
    }

    public QuakeCardAdapter(String[] names, String[] location, float[] magnitude, String[] depth, Date[] date) {
        this.names = names;
        this.location = location;
        this.magnitude = magnitude;
        this.date = date;
        this.depth = depth;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    @Override
    public QuakeCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.quake_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView magnitudeText = (TextView) cardView.findViewById(R.id.magnitude);
        TextView locationText = (TextView) cardView.findViewById(R.id.location);
        TextView deptText = (TextView) cardView.findViewById(R.id.depth);
        TextView dateText = (TextView) cardView.findViewById(R.id.date);
        magnitudeText.setText(magnitude[position] + " Richter");
        locationText.setText(location[position]);
        deptText.setText("Depth " + depth[position] + "KM");
        String pattern = "EEE, d MMM yyyy";
        String datee = new SimpleDateFormat(pattern).format(date[position]);
        dateText.setText(datee);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

    }
    @Override
    public int getItemCount() {
        return names.length;
    }


}
