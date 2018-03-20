package com.example.administrator.communityhelp.firstpage.wuye.notice;

import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.NoticeActivityRecycleViewAdapter;
import com.example.administrator.communityhelp.myadapter.SheQuActivityRecycleViewAdapter;
import com.example.administrator.communityhelp.myadapter.TelephoneActivityRecycleViewAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notice_Mian_MajorActivity extends BaseActivity {
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private NoticeActivityRecycleViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private TextView textView_header, textView_footer,textView_back,textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private int i = 0;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "generalInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/generalInfoService";//地址
    private int num = 7;
    private Map<String,Object> mapCode;
    private String communityCode;
    private ProgressDialogUtils progress;
    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_notice__mian__major);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        设置RecyclerView网格显示
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        //添加头部底部刷新加载视图
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();
        GetRequestMessageRecycler();
        textView_title= (TextView) findViewById(R.id.textView_title);
        textView_back= (TextView) findViewById(R.id.NoticeActivity_back);
        textView_back.setOnClickListener(new View.OnClickListener() {
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
                    textView_footer.setText("松开加载更多");
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
                        GetRequestMessageRecycler();
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
                        num += 7;
                        GetRequestMessageRefresh();
                        refreshLayout.finishLoadmore();
                    }
                }, 2000);
            }

        });
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", 0);
        map.put("pageSize", 7);
        map.put("columnCode", this.getIntent().getStringExtra("columnCode"));
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        Log.e("communityCode", communityCode);
        map.put("communityCode",communityCode );
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis());
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    listRecycler = jsonPaseRecycler(json);
                    recyclerAdapter = new NoticeActivityRecycleViewAdapter(listRecycler, Notice_Mian_MajorActivity.this);
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    Toast toast = Toast.makeText(Notice_Mian_MajorActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("无法获取网络数据，请检查网络连接状态！");
                    toast.setView(view);
                    toast.show();
                }
            }
        });
    }

    private void GetRequestMessageRefresh() {
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", 0);
        map.put("pageSize", num);
        map.put("columnCode", this.getIntent().getStringExtra("columnCode"));
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        Log.e("communityCode", communityCode);
        map.put("communityCode",communityCode );
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis());
        WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    listRecycler = jsonPaseRecycler(json);
                    recyclerAdapter.AddAll(listRecycler);
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(Notice_Mian_MajorActivity.this, "", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = getLayoutInflater().inflate(R.layout.toastview, null);
                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                    toastText.setText("无法获取网络数据，请检查网络连接状态！");
                    toast.setView(view);
                    toast.show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseRecycler(String result) {
        listRecycler = new ArrayList();
        try {
            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("rows");
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String id = jo.getString("id");
                String picPath = jo.getString("picPath");
                String content = jo.getString("content");
                String title = jo.getString("title");
                String releaseDate = jo.getString("releaseDate");
                map.put("id", id);
                map.put("picPath", picPath);
                map.put("content", content);
                map.put("title", title);
                map.put("releaseDate", releaseDate);
                listRecycler.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("listRecycler", "listRecycler: "+listRecycler.size());
        return listRecycler;
    }

    //获取当前选择的社区
    public Map<String,Object> getCommunityCode() {
        mapCode=new HashMap<>();
        String select = "select * from community";
        Cursor cursor = dbReader.rawQuery(select, null);
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            mapCode.put("communityCode",communityCode);
        }
        return mapCode;
    }
}
