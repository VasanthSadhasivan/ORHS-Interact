package com.teacupofcode.ved.interactapp;

/**
 * Created by Admin on 9/28/2016.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private HashMap<String, List<String>> subDataHash;
    private List<String> subDataList;

    public ExpandableListAdapter(Context ctx, HashMap<String, List<String>> subDataHash, List<String> subDataList){
        this.ctx = ctx;
        this.subDataHash = subDataHash;
        this.subDataList = subDataList;
    }

    @Override
    public int getGroupCount() {
        return subDataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subDataHash.get(subDataList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return subDataList.get(groupPosition);
    }

    @Override
    public Object getChild(int parent, int child) {
        return subDataHash.get(subDataList.get(parent)).get(child);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int parent, int child) {
        return child;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, parent, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.parent);
        parentTextView.setTypeface(null, Typeface.BOLD);
        parentTextView.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int parent, int child, boolean isLastChild, View convertView, ViewGroup parentView) {
        String eventTitle = (String) getChild(parent,child);
        if(convertView == null)
        {
            LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.child_layout, parentView, false);
        }
        TextView child_textview = (TextView) convertView.findViewById(R.id.desc);
        child_textview.setText(eventTitle);
            return null;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}