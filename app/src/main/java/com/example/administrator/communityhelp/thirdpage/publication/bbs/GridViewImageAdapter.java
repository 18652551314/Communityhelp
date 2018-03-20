package com.example.administrator.communityhelp.thirdpage.publication.bbs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.my_other.PictureCut;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class GridViewImageAdapter extends BaseAdapter {
    AdapterCallBack callBack;

    Context context;
    List<String> pathlist;
    int screenWith;
    public GridViewImageAdapter(AdapterCallBack callBack,Context context, List<String> pathlist, int screenWith) {
        this.callBack = callBack;
        this.context = context;
        this.pathlist = pathlist;
        this.screenWith=screenWith;
    }

    public void setPathlist(List<String> pathlist) {
        this.pathlist = pathlist;
    }

    @Override
    public int getCount() {
        return pathlist.size();
    }

    @Override
    public Object getItem(int position) {
        return pathlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder = null;
//        if (convertView == null) {
            myHolder=new MyHolder();
            convertView=View.inflate(context, R.layout.gridview_bbs_pic_item,null);
            myHolder.imageView= (ImageView) convertView.findViewById(R.id.imagView_grideView_bbs_item);
//            convertView.setTag(myHolder);
//        }else{
//            myHolder= (MyHolder) convertView.getTag();
//        }
        if(position==pathlist.size()-1){
            Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.pic_jia);
            myHolder.imageView.setMaxWidth(screenWith*3/14);
            myHolder.imageView.setMaxHeight(screenWith*3/14);
            myHolder.imageView.setImageBitmap(PictureCut.ImageCropSquere(PictureCut.getLittleImg(bitmap,10)));
        }else{
            Bitmap bitmap=BitmapFactory.decodeFile(pathlist.get(position));
            String s=pathlist.get(position);
            myHolder.imageView.setMaxWidth(screenWith*3/14);
            myHolder.imageView.setMaxHeight(screenWith*3/14);
            new GlideLoader().displayImage(context,s,myHolder.imageView);
//            myHolder.imageView.setImageBitmap(PictureCut.ImageCropSquere(PictureCut.getLittleImg(bitmap,10)));
        }
        return convertView;
    }
    class MyHolder {
        ImageView imageView;
    }
}
