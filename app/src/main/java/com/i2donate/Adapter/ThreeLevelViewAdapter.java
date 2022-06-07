package com.i2donate.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.i2donate.Model.Expandableclass.SecondLevelExpandableListView;
import com.i2donate.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ThreeLevelViewAdapter extends BaseExpandableListAdapter {

    String[] parentHeaders;
    List<String[]> secondLevel;
    private Context context;
    List<LinkedHashMap<String, String[]>> data;

    public ThreeLevelViewAdapter(Context context, String[] parentHeader, List<String[]> secondLevel, List<LinkedHashMap<String, String[]>> data) {
        this.context = context;

        this.parentHeaders = parentHeader;

        this.secondLevel = secondLevel;

        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return parentHeaders.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {


        // no idea why this code is working

        return 1;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return groupPosition;
    }

    @Override
    public Object getChild(int group, int child) {


        return child;


    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_first, null);
        TextView text = (TextView) convertView.findViewById(R.id.rowParentText);
        text.setText(this.parentHeaders[groupPosition]);
        ImageView listview_image_add_item1 = (ImageView) convertView.findViewById(R.id.listview_image_add_item1);
        ImageView listview_image_remove_item1=(ImageView)convertView.findViewById(R.id.listview_image_remove_item1);
        if (isExpanded){
            //  text.setTextColor(R.color.colorPrimary);
            listview_image_add_item1.setVisibility(View.GONE);
            listview_image_remove_item1.setVisibility(View.VISIBLE);

        }else {
            // text.setTextColor(R.color.black);
            listview_image_remove_item1.setVisibility(View.GONE);
            listview_image_add_item1.setVisibility(View.VISIBLE);

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(context);

        String[] headers = secondLevel.get(groupPosition);
        Log.e("ThreeLevel",String.valueOf(groupPosition));

        List<String[]> childData = new ArrayList<>();
        HashMap<String, String[]> secondLevelData=data.get(groupPosition);

        for(String key : secondLevelData.keySet())
        {
            childData.add(secondLevelData.get(key));

        }


        secondLevelELV.setAdapter(new SecondLevelViewAdapter(context, headers, childData));

        secondLevelELV.setGroupIndicator(null);


        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });


        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
