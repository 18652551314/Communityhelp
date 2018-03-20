package com.example.administrator.communityhelp.myadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Detailed_Information;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Rental_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Reply;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.LunTan_ViewHolder;
import com.example.administrator.communityhelp.myholder.PinChe_One_ViewHolder;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.thirdpage.publication.MyForum_Main;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/14.
 */
public class LunTan_One_ViewAdapter extends RecyclerView.Adapter {
    private List<Map<String, Object>> list;
    private Context context;
    private int i = 1, dianzan = 1;
    private MyCallBack myCallBack;
    private String id = "", isPraise = "", dierge;
    private List<String> list_zan=new ArrayList();
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures, userid;
    public static final String NameSpace = "http://bbs.service.zhidisoft.com";  //命名空间
    public static String MethodNameTop = "addPraise";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bbsService";//地址
    public int mun = 10;

    Intent intent_id;
    String Id, content1, userId1, replyUserHeadimgurl1, travelId1, replyTime1, isAnonymous1, replyUserName1, picPathSet, picPath;
    TextView textView_name, textView_time, textView_qidian, textView_con, textView_zhongdian, textView_chufa, textView_lianxiren, textView_dianhua, textView_miaoshu, textView_miaoshu_2;
    private List<Map<String, Object>> listRecycler;
    private List<Map<String, Object>> list_iv = new ArrayList();
    private ViewPager viewPager;
    private LinearLayout linearLayout;
    private int currentItem;
    private List<ImageView> point_list = new ArrayList<>();
    private Firstpageviewpager adapter1;
    private List<Map<String, Object>> point_list2 = new ArrayList();
    private List<ImageView> list_pp = new ArrayList<>();

    int images[] = {R.drawable.shtg, R.drawable.shwtg, R.drawable.shz};

//    private DisplayImageOptions options;
//    private ImageLoader loader;

    public void setMyCallBack(MyCallBack myCallBack) {
        this.myCallBack = myCallBack;
    }

    public void AddAll(List<Map<String, Object>> list) {
        this.list = list;
    }

    public LunTan_One_ViewAdapter(List<Map<String, Object>> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LunTan_ViewHolder holder = new LunTan_ViewHolder(LayoutInflater.from(context).inflate(R.layout.luntan_main_adp, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        int bbsState = 100;
        try {
            int state = (int) list.get(position).get("approvedResult");
            bbsState = state;
        } catch (Exception e) {

        }
        if (bbsState == 1) {
            ((LunTan_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setImageResource(images[0]);
        } else if (bbsState == 0) {
            ((LunTan_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setImageResource(images[1]);
        } else if (bbsState == -1) {
            ((LunTan_ViewHolder) holder).textView_delete.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setVisibility(View.VISIBLE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setImageResource(images[2]);
        } else {
            ((LunTan_ViewHolder) holder).textView_delete.setVisibility(View.GONE);
            ((LunTan_ViewHolder) holder).imageView_ischeck.setVisibility(View.GONE);
        }
        userid = list.get(position).get("userid") + "";

        ((LunTan_ViewHolder) holder).textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCallBack.delete((String) list.get(position).get("id"));
            }
        });

        ((LunTan_ViewHolder) holder).dianzan_tv.setText(list.get(position).get("praiseNum") + "");
        ((LunTan_ViewHolder) holder).textView_name.setText(list.get(position).get("releaseUser") + "");
        ((LunTan_ViewHolder) holder).textView_shijian.setText(list.get(position).get("createTime") + "");
        ((LunTan_ViewHolder) holder).textView_neirong.setText(Html.fromHtml(list.get(position).get("content") + ""));
        ((LunTan_ViewHolder) holder).textView_tit.setText(Html.fromHtml(list.get(position).get("title") + ""));
        ((LunTan_ViewHolder) holder).replyNum.setText(list.get(position).get("replyNum") + "");

        Log.e("cess", "cs " + list.get(position).get("headimgurl") + "");
        isPraise = list.get(position).get("isPraise") + "";
        list_zan.add(isPraise);
        // Bitmap headimgurl = getBitmap(list.get(position).get("headimgurl") + "");
        //         loader.displayImage(list.get(position).get("headimgurl")+"",((LunTan_ViewHolder) holder).imageView_touxiang,options);
        if (isPraise.equals("Y")) {
            ((LunTan_ViewHolder) holder).imageView.setBackgroundResource(R.mipmap.zan2);
        } else {
            ((LunTan_ViewHolder) holder).imageView.setBackgroundResource(R.mipmap.zan1);
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(list.get(position).get("headimgurl") + "");
        //((LunTan_ViewHolder) holder).imageView_touxiang.setImageBitmap(headimgurl);
        new GlideLoader().displayImage(context, list.get(position).get("headimgurl") + "", ((LunTan_ViewHolder) holder).imageView_touxiang);
        ((LunTan_ViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPraise=list_zan.get(position);
                boolean f = (boolean) list.get(position).get("isload");
                if (f) {
                } else {
                    Intent intent = new Intent(context, Login_Main.class);
                    context.startActivity(intent);
                    return;
                }
                dianzan = (Integer) (list.get(position).get("praiseNum"));


                if (isPraise.equals("Y")) {
                    dierge = ((LunTan_ViewHolder) holder).dianzan_tv.getText().toString();
                    if ((list.get(position).get("praiseNum").toString()).equals(dierge)) {
                        dianzan--;
                    }

                    ((LunTan_ViewHolder) holder).imageView.setBackgroundResource(R.mipmap.zan1);

                    id = list.get(position).get("id") + "";
                    MethodNameTop = "cancelPraise";
                    isPraise = "N";
                    list_zan.remove(position);
                    list_zan.add(position,"N");
                    GetRequestMessageTop();

                    ((LunTan_ViewHolder) holder).dianzan_tv.setText(dianzan + "");

                } else if (isPraise.equals("N")) {
                    dierge = ((LunTan_ViewHolder) holder).dianzan_tv.getText().toString();
                    if ((list.get(position).get("praiseNum").toString()).equals(dierge)) {
                        dianzan++;
                    }

                    ((LunTan_ViewHolder) holder).imageView.setBackgroundResource(R.mipmap.zan2);
                    isPraise = "Y";
                    list_zan.remove(position);
                    list_zan.add(position,"Y");
                    id = list.get(position).get("id") + "";
                    MethodNameTop = "addPraise";
                    GetRequestMessageTop();

                    ((LunTan_ViewHolder) holder).dianzan_tv.setText(dianzan + "");

                }
            }
        });
        ((LunTan_ViewHolder) holder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), Forum_Detailed_Information.class);
                intent2.putExtra("id", list.get(position).get("id") + "");
                intent2.putExtra("isPraise", list_zan.get(position) + "");
                context.startActivity(intent2);
            }
        });

        ((LunTan_ViewHolder) holder).textView_neirong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), Forum_Detailed_Information.class);
                intent2.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent2);
            }
        });

        ((LunTan_ViewHolder) holder).luntan_huifu_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(v.getContext(), Forum_Reply.class);
                intent2.putExtra("id", list.get(position).get("id") + "");
                context.startActivity(intent2);
            }
        });
        //myCallBack.getintent( ((LunTan_ViewHolder) holder).linearLayout,((LunTan_ViewHolder) holder).imageView_huifu);
        //callback.sendHolder(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        ProgressDialogUtils.showProgressDialog(context, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("userId", userid);


        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);


                } else {
                    Toast.makeText(context, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            String jjh = "[" + result + "]";
            JSONArray array = new JSONArray(jjh);
//            JSONObject object =
//            JSONArray array = object.getJSONArray("rows");new JSONObject(result);
            Log.e("json", "11: " + array);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                //String title = jo.getString("title");


                //Toast.makeText(Carpooling_Detaile_Information.this, title, Toast.LENGTH_SHORT).show();
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }


}