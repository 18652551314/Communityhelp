package com.example.administrator.communityhelp.firstpage.linliquan.forum;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.LunTan_One_ViewAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiGuidCenterAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiGuidTopAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiRecycleViewAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.thirdpage.publication.MyForum_Main;
import com.example.administrator.communityhelp.thirdpage.publication.bbs.CommunityActivity;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */
public class Forum_Main extends BaseActivity {
    int total;
    TextView textView;
    Intent intent;
    int loadTime=2000;
    boolean loadState=true;
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;

    private LunTan_One_ViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private TextView textView_header, textView_footer, textView_back, textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private int i = 0;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    LayoutInflater inflater1;

    private int recycleImage[] = {R.drawable.poster1};
    private String gridName[] = {"活动1", "活动2", "活动3", "活动4", "活动5", "活动6"};
    private String date[] = {(String) DateFormat.format("yyyy年MM月dd日 HH:mm", new Date())};
    private String referral[] = {"介绍1", "介绍2", "介绍3", "介绍4", "介绍5", "介绍6"};

    ProgressDialogUtils progress;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    public static final String NameSpace = "http://bbs.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "bbsList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/bbsService";//地址
    public int mun = 10;

    //    private ImageLoader loader;
//    private DisplayImageOptions options;
    @Override
    public void init() {
        super.init();
        progress=new ProgressDialogUtils(this);
        setContentView(R.layout.activity_tourism__main);
        textView = (TextView) findViewById(R.id.first_tit_tit);
        textView.setText("社区论坛");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_forum);
        listRecycler = new ArrayList();
        //getRecyclerData();
//        loader = ImageLoader.getInstance();
//        //显示图片的配置
//        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new RoundedBitmapDisplayer(90))
//                .build();

//         recyclerAdapter = new LunTan_One_ViewAdapter(listRecycler,this);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);

        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        设置RecyclerView网格显示
//
//      recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        //     recyclerView.setAdapter(recyclerAdapter);
        //添加头部底部刷新加载视图
        GetRequestMessageTop();
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        recyclerAdapter.setMyCallBack(new MyCallBack() {
//            @Override
//            public void getintent(LinearLayout linearLayout, ImageView imageView) {
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent2=new Intent(Forum_Main.this,Forum_Detailed_Information.class);
//                        startActivity(intent2);
//                    }
//                });
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent2=new Intent(Forum_Main.this,Forum_Reply.class);
//                        startActivity(intent2);
//                    }
//                });
//            }
//        });
    }

    public void tourism_main_ll(View v) {
        if (isLoad()) {
            intent = new Intent(this, CommunityActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, Login_Main.class);
            startActivity(intent);
        }

    }

    public void tourism_main_ll_1(View v) {
        finish();
    }

    public void addHeaderBottom() {
        //添加头部布局
        refreshLayout.setHeaderView(new IHeaderView() {
            //设置视图
            @Override
            public View getView() {
                view_header = getLayoutInflater().inflate(R.layout.view_header, null);
                linearLayout = (LinearLayout) view_header.findViewById(R.id.linearLayout);
                textView_header = (TextView) view_header.findViewById(R.id.textView_header);
                imageView_header = (ImageView) view_header.findViewById(R.id.imageView_header);
                return view_header;
            }

            //下拉过程显示
            @Override
            public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
                float centerX = imageView_header.getWidth() / 2f;
                float centerY = imageView_header.getHeight() / 2f;
                imageView_header.setBackground(getResources().getDrawable(R.mipmap.xlistview_arrow));
                if (fraction < 1) {
                    imageView_header.setRotation(0);
                    textView_header.setText("下拉刷新");
                }
                if (fraction > 1) {
                    imageView_header.setRotation(180);
                    textView_header.setText("松开刷新数据");
                }
            }

            //下拉释放显示
            @Override
            public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
                if (fraction > 1) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    i++;
                                    if (i < refresh.length - 1) {
                                        Thread.sleep(100);
                                        imageView_header.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                imageView_header.setBackground(getResources().getDrawable(refresh[i]));
                                            }
                                        });


                                    } else {
                                        i = 0;
                                        break;
                                    }

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                    textView_header.setText("玩命刷新中...");
                }
                //Log.e("Cat", "onPullReleasing: 下拉释放");
            }

            //动画开始显示
            @Override
            public void startAnim(float maxHeadHeight, float headHeight) {
                //Log.e("Cat", "startAnim: 动画显示");
            }

            //动画结束
            @Override
            public void onFinish(OnAnimEndListener animEndListener) {
                //刷新完成后回弹
                animEndListener.onAnimEnd();
                //Log.e("Cat", "onFinish: 动画结束");
            }

            @Override
            public void reset() {
            }
        });

        //添加底部布局
        refreshLayout.setBottomView(new IBottomView() {
            //设置视图
            @Override
            public View getView() {
                view_footer = getLayoutInflater().inflate(R.layout.view_footer, null);
                textView_footer = (TextView) view_footer.findViewById(R.id.textView_footer);
                imageView_footer = (ImageView) view_header.findViewById(R.id.imageView_footer);
                return view_footer;
            }

            //上拉过程显示
            @Override
            public void onPullingUp(float fraction, float maxHeadHeight, float headHeight) {
                if (fraction < -1) {
                    textView_footer.setText("加载更多");
                }
                if (fraction > -1) {
                    if (loadState) {
                        textView_footer.setText("松开加载更多");
                    } else {
                        textView_footer.setText("没有了");
                        loadTime = 0;
                    }
                }
            }

            //上拉释放
            @Override
            public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
                if (fraction < 1) {
                    textView_footer.setText("加载成功");
                }
            }

            @Override
            public void onFinish() {

            }

            //动画开始
            @Override
            public void startAnim(float maxHeadHeight, float headHeight) {
                textView_footer.setText("玩命加载中...");
            }

            @Override
            public void reset() {
            }
        });
    }

    private void setRecycleViewListener() {
        //设置TwinklingRefreshLayout的监听
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            //刷新
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                }, 2000);
            }

            //加载
            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            mun += 7;
                            GetRequestMessageTop();
                            recyclerAdapter.AddAll(listRecycler);
                            recyclerAdapter.notifyDataSetChanged();
                            refreshLayout.finishLoadmore();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000);
            }

        });
    }

    private void getRecyclerData() {
        for (int i = 0; i < gridName.length; i++) {
            Map<String, Object> map = new HashMap<>();

            map.put("gridName", gridName[i]);

            listRecycler.add(map);
        }
    }

    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        try {
            progress.show();
        }catch (Exception e){e.printStackTrace();}

        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", "0");
        map.put("pageSize", mun);
        map.put("userId", getUserData().getUserId());//297ebe0e593e1bf9015944c131201242
        map.put("communityCode", "");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");

        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                try {
                    progress.dismiss();
                }catch (Exception e){}

                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                     if (total <= mun) {
                        loadState = false;
                    }
                    recyclerAdapter = new LunTan_One_ViewAdapter(listRecycler, Forum_Main.this);
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    try {
                        progress.dismiss();
                    }catch (Exception e){}
                    Toast.makeText(Forum_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //
    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            Gson  gson = new Gson();
            MyForum_Main.myForumMessage msg = gson.fromJson(result, MyForum_Main.myForumMessage.class);
            int size = msg.getPageSize();
            total=msg.getTotal();
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = new HashMap<>();
                int total = msg.getTotal();
                String id = msg.getRows().get(i).getId();
                String releaseUser = msg.getRows().get(i).getReleaseUser();
                String createTime = msg.getRows().get(i).getCreateTime();
                String content = msg.getRows().get(i).getContent();
                String title = msg.getRows().get(i).getTitle();
                String headimgurl = msg.getRows().get(i).getHeadimgurl();
                String isPraise = msg.getRows().get(i).getIsPraise();
                int replyNum=msg.getRows().get(i).getReplyNum();
                int approvedResult = msg.getRows().get(i).getApprovedResult();
                int praiseNum=msg.getRows().get(i).getPraiseNum();
                map.put("isPraise",isPraise);
                map.put("userid",getUserData().getUserId());
                map.put("praiseNum", praiseNum);
                map.put("total", total);
                map.put("id", id);
                map.put("releaseUser", releaseUser);
                map.put("createTime", createTime);
                map.put("content", content);
                map.put("title", title);
                map.put("headimgurl", headimgurl);
                map.put("approvedResult", 100);
                map.put("replyNum",replyNum);
                map.put("isload",isLoad());
                listGridTop.add(map);
            }
            // Toast.makeText(Forum_Main.this, title, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetRequestMessageTop();
    }
}
