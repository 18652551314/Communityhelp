package com.example.administrator.communityhelp.myholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.R;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LunTan_ViewHolder extends RecyclerView.ViewHolder{
    public TextView dianzan_tv;
    public TextView textView_name,textView_shijian,textView_neirong,textView_tit,textView_delete,replyNum;
    public ImageView imageView,imageView_huifu,imageView_touxiang,luntan_huifu_iv,imageView_ischeck;
    public LinearLayout linearLayout,tamade_ll;
    public LunTan_ViewHolder(View itemView) {
        super(itemView);
        textView_delete= (TextView) itemView.findViewById(R.id.textView_bbsDelete);
        textView_name= (TextView) itemView.findViewById(R.id.luntan_tv_name);
        textView_shijian= (TextView) itemView.findViewById(R.id.shequ_shijian_tv);
        textView_neirong= (TextView) itemView.findViewById(R.id.shequ_neirong_tv);
        textView_tit= (TextView) itemView.findViewById(R.id.shequ_tit_tv);
        imageView= (ImageView) itemView.findViewById(R.id.luntan_iv);
        linearLayout= (LinearLayout) itemView.findViewById(R.id.luntan_ll);
        imageView_ischeck= (ImageView) itemView.findViewById(R.id.imageView_bbs_ischeck);
        imageView_huifu= (ImageView) itemView.findViewById(R.id.luntan_huifu_iv);
        imageView_touxiang= (ImageView) itemView.findViewById(R.id.luntan_iv1);
        luntan_huifu_iv= (ImageView) itemView.findViewById(R.id.luntan_huifu_iv);
        dianzan_tv= (TextView) itemView.findViewById(R.id.dianzan_tv);
        replyNum= (TextView) itemView.findViewById(R.id.luntan_huifu_replyNum);
    }
}
