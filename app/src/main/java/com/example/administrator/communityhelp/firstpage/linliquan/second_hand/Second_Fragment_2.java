package com.example.administrator.communityhelp.firstpage.linliquan.second_hand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.forum.Forum_Reply;
import com.example.administrator.communityhelp.myadapter.ErShou_Twe_ViewAdapter;
import com.example.administrator.communityhelp.myadapter.ErShou_ViewAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/6.
 */
public class Second_Fragment_2 extends Fragment {
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private ErShou_Twe_ViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private TextView textView_header, textView_footer, textView_back, textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private int i = 0;
    private Second_Hand_Main second_hand_main;
    ProgressDialogUtils progress;
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

    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "esInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/esInfoService";//地址
    public int mun = 10;
    private String leixing = "";
    String panduan = "", zhi = "";
    int code;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflater1 = inflater;
        view = inflater.inflate(R.layout.ershou_fragment_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ershou);
        listRecycler = new ArrayList();
        GetRequestMessageTop();
        recyclerAdapter = new ErShou_Twe_ViewAdapter(listRecycler, view.getContext());
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        second_hand_main = new Second_Hand_Main();
        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        设置RecyclerView网格显示
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
        //添加头部底部刷新加载视图
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();
       /* recyclerAdapter.setMyCallBack(new MyCallBack() {
            @Override
            public void getintent(LinearLayout linearLayout, ImageView imageView) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(view.getContext(),Second_Hand_Detaile_Information.class);
                        startActivity(intent);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(view.getContext(), Forum_Reply.class);
                    }
                });
            }

        });*/

        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zhi = ((Second_Hand_Main) activity).getTitles();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Second_Hand_Main ac = (Second_Hand_Main) getActivity();
                    try{
                    zhi = ac.getData();
                    code = ac.getCode();
                    } catch (Exception e) {
                    }

                    if (code == 2) {

                        if (zhi != "") {

                            if (zhi == "a") {
                                zhi = "";
                            }


                            if (zhi != panduan) {
                                panduan = zhi;
                                leixing = panduan;
                                recyclerView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        GetRequestMessageTop();

                                        recyclerAdapter.AddAll(listRecycler);
                                        recyclerAdapter.notifyDataSetChanged();
                                        refreshLayout.finishLoadmore();
                                    }
                                });

                            }
                        }
                    }

                }
            }
        }).start();
    }


    public void addHeaderBottom() {
        //添加头部布局
        refreshLayout.setHeaderView(new IHeaderView() {
            //设置视图
            @Override
            public View getView() {
                view_header = inflater1.inflate(R.layout.view_header, null);
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
                view_footer = inflater1.inflate(R.layout.view_footer, null);
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
                            leixing = panduan;
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

    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        try {
            progress=new ProgressDialogUtils(view.getContext());
            progress.show();
        }catch (Exception e){}

        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();

        map.put("pageNo", "-1");
        map.put("pageSize", mun);
        map.put("columnCode", leixing);
        map.put("communityCode", "00001");
        map.put("sellOrBuy", "buy");
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
                    recyclerAdapter = new ErShou_Twe_ViewAdapter(listRecycler, view.getContext());
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    Toast.makeText(view.getContext(), "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        Log.e("json", "result: " + result);

        try {

            String jjboom = "[" + result + "]";

            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("rows");

            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String userName = jo.getString("userName");
                String createTime = jo.getString("createTime");
                String content = jo.getString("content");
                String headimgurl = jo.getString("headimgurl");
                String title = jo.getString("title");
                String contactPhone = jo.getString("contactPhone");
                String replyNum = jo.getString("replyNum");
                String id = jo.getString("id");
                map.put("id", id);//头像
                map.put("headimgurl", headimgurl);//头像
                map.put("userName", userName);//用户名
                map.put("createTime", createTime);//发布时间
                map.put("title", title);//标题
                map.put("contactPhone", contactPhone);//联系人电话
                map.put("content", content);//二手物品描述
                map.put("replyNum", replyNum);//回复
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }


    private void getRecyclerData() {
        for (int i = 0; i < gridName.length; i++) {
            Map<String, Object> map = new HashMap<>();

            map.put("gridName", gridName[i]);

            listRecycler.add(map);
        }
    }
}
