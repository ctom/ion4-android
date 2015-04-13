package com.sionicmobile.ion.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sionicmobile.ion.R;

import java.util.ArrayList;

/**
 * Created by TChwang on 4/3/2015.
 */
public class IonDrawerListAdapter extends BaseAdapter {

    private Context _context;
    private String[] _ionMenuList;

    public IonDrawerListAdapter(Context context, String[] list) {
        _context = context;
        _ionMenuList = list;
    }

    @Override
    public int getCount() {
        return _ionMenuList.length;
    }

    @Override
    public Object getItem(int position) {
        return _ionMenuList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_menu_list_layout, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.ion_drawer_item_id);
        txtTitle.setText(_ionMenuList[position]);

        return convertView;
    }
}
