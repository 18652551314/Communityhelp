package com.example.administrator.communityhelp.secondpage;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.administrator.communityhelp.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2017/1/3.
 */
public class SecondPage extends Fragment implements View.OnClickListener{
    private ImageView image;
    ImageView imageWeixinErwei;
    ImageView imageViewBianMingErwei;
    int screenWidth;
    private ImageView friendImage,weixinImage,qqImage,messageImage;

    int imageErwei[]={R.drawable.wxerweima,R.drawable.blerweima};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_sencse_main, container,false);
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        init(view);
        setImage();
        ShareSDK.initSDK(getActivity());
        return view;
    }

    private void setImage() {
        int imageSize=(int) (screenWidth*0.43);
        imageWeixinErwei.setMaxHeight(imageSize);
        imageWeixinErwei.setMaxWidth(imageSize);
        imageViewBianMingErwei.setMaxWidth(imageSize);
        imageViewBianMingErwei.setMaxHeight(imageSize);
        imageWeixinErwei.setImageBitmap(BitmapFactory.decodeResource(getResources(),imageErwei[0]));
        imageViewBianMingErwei.setImageBitmap(BitmapFactory.decodeResource(getResources(),imageErwei[1]));
    }

    private void init(View view) {
        imageWeixinErwei= (ImageView) view.findViewById(R.id.weixingerwei);
        imageViewBianMingErwei= (ImageView) view.findViewById(R.id.imageView_bianliErwei);
        friendImage= (ImageView) view.findViewById(R.id.friendImage);
        weixinImage= (ImageView) view.findViewById(R.id.weixinImage);
        qqImage= (ImageView) view.findViewById(R.id.qqImage);
        messageImage= (ImageView) view.findViewById(R.id.messageImage);
        friendImage.setOnClickListener(this);
        weixinImage.setOnClickListener(this);
        qqImage.setOnClickListener(this);
        messageImage.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        //oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://120.27.5.22:8080/mo/client/download.mo");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("社区帮帮手机客户端下载地址：http://120.27.5.22:8080/mo/client/download.mo");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://img.wdjimg.com/mms/icon/v1/f/e2/2f5f22bdd6581dcf0c3930dabf344e2f_256_256.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://120.27.5.22:8080/mo/client/download.mo");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite("客户端下载");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://120.27.5.22:8080/mo/client/download.mo");

        //启动分享
        oks.show(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.friendImage:
                Platform platpyq = ShareSDK.getPlatform(WechatMoments.NAME);
                showShare(platpyq.getName());
                break;
            case R.id.weixinImage:
                Platform platwx = ShareSDK.getPlatform(Wechat.NAME);
                showShare(platwx.getName());
                break;
            case R.id.qqImage:
                Platform platqq = ShareSDK.getPlatform(QQ.NAME);
                showShare(platqq.getName());
                break;
            case R.id.messageImage:
//                Intent messageIntent=new Intent(Intent.ACTION_VIEW);
//                messageIntent.putExtra("sms_body","社区帮帮手机客户端下载地址：http://120.27.5.22.8080/mo/client/download.mo");
//                messageIntent.setType("vnd.android-dir/mms-sms");
//                startActivity(messageIntent);
                Platform platdx = ShareSDK.getPlatform(ShortMessage.NAME);
                showShare(platdx.getName());
                break;
        }
    }
}
