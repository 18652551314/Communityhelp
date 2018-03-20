package com.example.administrator.communityhelp.thirdpage.leaving_message;

import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.SheQuActivityRecycleViewAdapter;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLeavingMessageActivity extends BaseActivity {
    int total,loadtime=2000;
    //用tkrefreshlaout装RecycleView，
//    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private MyMessageRecycleAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private TextView textView_header, textView_footer,textView_back,textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private int i = 0;
    private int num=7;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    public static final String URL = "http://120.27.5.22:8080/services/problemService";
    public static final String NameSpace = "http://problem.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "myPropertyList";//方法名
    private List<JSONObject> list;
    private ProgressDialogUtils progress;
    private int recycleImage[] = {R.drawable.poster1};
    private String gridName[] = {"活动1", "活动2", "活动3", "活动4", "活动5", "活动6"};
    private String date[] = {(String) DateFormat.format("yyyy年MM月dd日 HH:mm",new Date())};
    private String referral[] = {"介绍1", "介绍2", "介绍3", "介绍4", "介绍5", "介绍6"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        super.init();
        list=new ArrayList<>();
        setContentView(R.layout.activity_my_leaving_message);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView_myMessage);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout_myMessage);
        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        设置RecyclerView网格显示
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        getRecyclerData();

        //添加头部底部刷新加载视图
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();
        textView_title= (TextView) findViewById(R.id.textView_title);
        LinearLayout layout_back= (LinearLayout) findViewById(R.id.back_layout_myMessage);
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                                    if (i < refresh.length) {
                                        Thread.sleep(100);
                                        runOnUiThread(new Runnable() {
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
                    if(num>=total){
                        textView_footer.setText("没有了");
                    }else{
                    textView_footer.setText("松开加载更多");}
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
                        getRecyclerData();
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
                        if(num>=total){
                            loadtime=0;
                            refreshLayout.finishLoadmore();
                            return;
                        }
                        num += 7;
                        getRecyclerDataRefresh();
                        refreshLayout.finishLoadmore();
                    }
                }, loadtime);
            }
        });
    }

    private void getRecyclerData() {
        progress=new ProgressDialogUtils(this);
        progress.show();
        HashMap<String,Object> map=new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",num);
        map.put("userId",getUserData().getUserId());
        map.put("condition","");
        map.put("curTime",new Date().getTime());
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if(result!=null){
                    String json=result.getProperty(0).toString();
                    Log.e("cat0",json);
                    try {
                        list=new ArrayList<>();
                        JSONObject object=new JSONObject(json);
                        total=object.getInt("total");
                        JSONArray array=object.getJSONArray("rows");
                        for(int i=0;i<array.length();i++){
                            list.add(array.getJSONObject(i));
                        }
                        recyclerAdapter=new MyMessageRecycleAdapter(MyLeavingMessageActivity.this,list);
                        recyclerView.setAdapter(recyclerAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getRecyclerDataRefresh() {
        HashMap<String,Object> map=new HashMap();
        map.put("pageNo",0);
        map.put("pageSize",num);
        map.put("userId",getUserData().getUserId());
        map.put("condition","");
        map.put("curTime",new Date().getTime());
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if(result!=null){
                    String json=result.getProperty(0).toString();
                    Log.e("cat0",json);
                    try {
                        list=new ArrayList<>();
                        JSONObject object=new JSONObject(json);
                        JSONArray array=object.getJSONArray("rows");
                        for(int i=0;i<array.length();i++){
                            list.add(array.getJSONObject(i));
                        }
                        recyclerAdapter.AddAll(list);
                        recyclerAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
