package com.bsu.stoninois.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bsu.stoninois.R;
import com.mikepenz.fastadapter.items.AbstractItem;

public class QuarterlyReportAdapter extends AbstractItem<QuarterlyReportAdapter, QuarterlyReportAdapter.ViewHolder> {

    int id;
    String name;
    int count;
    String title;
    int quarter;
    int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Fast Adapter methods
    @Override
    public int getType() { return R.id.item_quarterly_report; }

    @Override
    public int getLayoutRes() { return R.layout.item_quarterly_report; }

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);
        holder.name.setText(name);
        holder.count.setText(String.valueOf(count));
        holder.title.setText(title);
        holder.year.setText("Year: " + String.valueOf(year));
        holder.quarter.setText("Quarter: " + String.valueOf(quarter));
    }



    // Manually create the ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView count;
        TextView title;
        TextView year;
        TextView quarter;
        //TODO: Declare your UI widgets here

        public ViewHolder(final View itemView) {
            super(itemView);
            //TODO: init UI
            name = itemView.findViewById(R.id.name);
            count = itemView.findViewById(R.id._id);
            year = itemView.findViewById(R.id.year);
            quarter = itemView.findViewById(R.id.quarter);
            title = itemView.findViewById(R.id.title);
        }
    }
}
