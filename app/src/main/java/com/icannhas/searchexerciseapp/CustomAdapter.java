package com.icannhas.searchexerciseapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 9/16/2016.
 */
public class CustomAdapter extends ArrayAdapter<JSONTestObject> {

    private Context mContext;
    private List<JSONTestObject> mList;

    /**
     * CustomAdapter function until setData function are templates and utility functions that you can just copy paste everytime you create a custom adapter
     *
     */
    public CustomAdapter(Context context, List<JSONTestObject> objects) {
        super(context, R.layout.cell_item, objects);
        mContext = context;
        mList = objects;
    }

    @Override
    public int getCount() {
        if (mList != null)
            return mList.size();

        return 0;
    }

    @Override
    public JSONTestObject getItem(int position) {
        if (mList != null && mList.size() > 0 && position < mList.size()) {
            return mList.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<JSONTestObject> data) {
        mList = data;
        notifyDataSetChanged();
    }

    //The template for getView and ViewHolder usually doesn't change
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        View rowView = convertView;

        // initialize fields of the cell and stores the viewHolder in the convertView
        // if convertView is not null, then just get the viewHolder from the convertView
        if (rowView == null) {

            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            rowView = inflater.inflate(R.layout.cell_item, null);
            viewHolder = new ViewHolder();
            viewHolder.vTitle = (TextView) rowView.findViewById(R.id.cell_item_title);
            viewHolder.vBody = (TextView) rowView.findViewById(R.id.cell_item_body);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        // set properties of the view
        final JSONTestObject object = getItem(position);
        viewHolder.vTitle.setText(object.getTitle());
        viewHolder.vBody.setText(object.getBody());

        return rowView;
    }

    //store the views here
    static class ViewHolder {
        private TextView vTitle;
        private TextView vBody;
    }

}
