package com.bsu.stoninois.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bsu.stoninois.R;
import com.mikepenz.fastadapter.items.AbstractItem;

public class BasicAdapter extends AbstractItem<BasicAdapter, BasicAdapter.ViewHolder> {

    int id;
    int index;
    String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Fast Adapter methods
    @Override
    public int getType() { return R.id.item_basic; }

    @Override
    public int getLayoutRes() { return R.layout.item_basic; }

    @Override
    public void bindView(ViewHolder holder) {
        super.bindView(holder);
        holder.index.setText(String.valueOf(index));
        holder.title.setText(title);
    }



    // Manually create the ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView title;
        //TODO: Declare your UI widgets here

        public ViewHolder(final View itemView) {
            super(itemView);
            //TODO: init UI
            index = itemView.findViewById(R.id.index);
            title = itemView.findViewById(R.id.title);

        }
    }
}
