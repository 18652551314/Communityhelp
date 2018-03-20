package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */
public class ShowAddressListViewAdapter extends BaseAdapter{
    private List<Map<String,Object>> list;
    private Context context;
    private int current;

    public ShowAddressListViewAdapter(List<Map<String,Object>> list,Context context) {
        this.list = list;
        this.context = context;
    }

    public void setCurrent(int position){
        this.current=position;
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
            view=View.inflate(context, R.layout.show_address_listview_item,null);
            holder.textView_name= (TextView) view.findViewById(R.id.textView_name);
            holder.textView_moren= (TextView) view.findViewById(R.id.textView_moren);
            holder.textView_phone= (TextView) view.findViewById(R.id.textView_phone);
            holder.textView_address= (TextView) view.findViewById(R.id.textView_address);
            holder.imageView= (ImageView) view.findViewById(R.id.image_address);
            view.setTag(holder);
        }else{
            holder= (MyHolder) view.getTag();
        }
        String name=list.get(position).get("xingming").toString();
        String phone=list.get(position).get("dianhua").toString();
        String guojia=list.get(position).get("guojia").toString();
        String sheng=list.get(position).get("sheng").toString();
        String shi=list.get(position).get("shi").toString();
        String qu=list.get(position).get("qu").toString();
        String xiangxi=list.get(position).get("xiangxi").toString();
        String flag=list.get(position).get("flag").toString();
        Log.e("flag", flag);
        String dizhi=name+phone+guojia+sheng+shi+qu+xiangxi;
        if(list.get(position).get("moren").equals("Y")){
            holder.textView_moren.setVisibility(View.VISIBLE);
           //holder.imageView.setImageResource(R.mipmap.psfs_select);
        }
        if(flag.equals("true")){
            holder.imageView.setImageResource(R.mipmap.psfs_select);
        }else{
            holder.imageView.setImageResource(R.mipmap.psfs_unselect);
        }
        holder.textView_name.setText(name);
        holder.textView_phone.setText(phone);
        holder.textView_address.setText("收货地址: "+guojia+sheng+shi+qu+xiangxi);

        return view;
    }

    class MyHolder{
        TextView textView_moren,textView_name,textView_phone,textView_address;
        ImageView imageView;
    }
}
