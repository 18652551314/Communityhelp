package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

import java.util.List;

/**
 * Created by Administrator on 2017/1/7.
 */
public class AddressListViewAdapter extends BaseAdapter{
    private List list;
    private Context context;

    public AddressListViewAdapter(List list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyHolder holder=null;
        if(view==null){
            holder=new MyHolder();
            view=View.inflate(context, R.layout.row_listview_item,null);
            holder.textView_adrName= (TextView) view.findViewById(R.id.textView_adrName);
            view.setTag(holder);
        }else{
            holder= (MyHolder) view.getTag();
        }
        holder.textView_adrName.setText(list.get(position)+"");
        return view;
    }

    class MyHolder{
        TextView textView_adrName;
    }
}
