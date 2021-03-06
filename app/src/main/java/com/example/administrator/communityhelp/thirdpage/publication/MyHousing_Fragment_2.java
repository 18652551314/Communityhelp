package com.example.administrator.communityhelp.thirdpage.publication;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.administrator.communityhelp.myadapter.XunWu_2_ViewAdapter;
import com.example.administrator.communityhelp.myadapter.XunWu_One_ViewAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.mysql.UserData;
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
 * Created by Administrator on 2017/1/6.
 */
public class MyHousing_Fragment_2 extends Fragment {
    ProgressDialogUtils progress;
    int loadtime = 2000;
    int total;
    SqlHelper helper;
    SQLiteDatabase dbReader;
    MyCallBack callBack;
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private XunWu_2_ViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private TextView textView_header, textView_footer,textView_back,textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private MyCallBack myCallBack;
    private int i = 0;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    LayoutInflater inflater1;

    private int recycleImage[] = {R.drawable.poster1};
    private String gridName[] = {"活动1", "活动2", "活动3", "活动4", "活动5", "活动6"};
    private String date[] = {(String) DateFormat.format("yyyy年MM月dd日 HH:mm",new Date())};
    private String referral[] = {"介绍1", "介绍2", "介绍3", "介绍4", "介绍5", "介绍6"};

    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "myHouseRentalList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/infoExchangeService";//地址
    public int mun = 10;
    View view;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (MyCallBack) activity;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress=new ProgressDialogUtils(getActivity());
        helper = new SqlHelper(getActivity());
        dbReader = helper.getReadableDatabase();
        inflater1 = inflater;
        view = inflater.inflate(R.layout.ershou_fragment_1, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerView_ershou);
        listRecycler = new ArrayList();
        GetRequestMessageTop();
        recyclerAdapter = new XunWu_2_ViewAdapter(listRecycler, view.getContext());
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
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
     /*   recyclerAdapter.setMyCallBack(new MyCallBack() {
            @Override
            public void getintent(LinearLayout linearLayout, ImageView imageView) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(view.getContext(),Housing_Detaile_Information.class);
                        startActivity(intent);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(view.getContext(), Forum_Reply.class);
                        startActivity(intent);
                    }
                });
            }
        });*/
        begainRefresh();
        return view;
    }
    private void begainRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MyHousing_Rental activity = (MyHousing_Rental) getActivity();
                        if (activity.startDeletecode == 1) {
                            while (true) {
                                activity = (MyHousing_Rental) getActivity();
                                if (activity.startDeletecode == 0) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            GetRequestMessageTop();
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
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
                    if (mun >= total) {
                        textView_footer.setText("没有了");
                    } else {
                        textView_footer.setText("松开加载更多");
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
                        GetRequestMessageTop();
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
                            if (mun >= total) {
                                loadtime = 0;
                                refreshLayout.finishLoadmore();
                                return;
                            }
                            Thread.sleep(1000);
                            mun+=7;
                            GetRequestMessageTop();

                            recyclerAdapter.AddAll(listRecycler);
                            recyclerAdapter.notifyDataSetChanged();
                            refreshLayout.finishLoadmore();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, loadtime);
            }

        });
    }



    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        try{ progress.show();}catch (Exception e){}
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", "-1");
        map.put("pageSize", mun);
        map.put("infoType","cs");
        map.put("communityCode", getUserData().getCommunityCode());
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");
        map.put("userId", getUserData().getUserId());
        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                try {   progress.dismiss();
                }catch (Exception e){}
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
//                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    recyclerAdapter = new XunWu_2_ViewAdapter(listRecycler, view.getContext());
                    recyclerAdapter.setMyCallBack(callBack);
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
            Gson gson=new Gson();
            MyHousing_Fragment_1.MyHouseRental myHouseRental = gson.fromJson(result, MyHousing_Fragment_1.MyHouseRental.class);
            int total = myHouseRental.getTotal();
            this.total=total;
            for (int i = 0; i <total; i++) {
                Map<String, Object> map = new HashMap<>();
                String userName = myHouseRental.getRows().get(i).getUserName();
                String createTime = myHouseRental.getRows().get(i).getCreateTime();
                String content = myHouseRental.getRows().get(i).getContent();
                String headimgurl = myHouseRental.getRows().get(i).getHeadimgurl();
                String contact = myHouseRental.getRows().get(i).getContact();
                String contactPhone = myHouseRental.getRows().get(i).getContactPhone();
                int replyNum = myHouseRental.getRows().get(i).getReplyNum();
                String title = myHouseRental.getRows().get(i).getTitle();
                String picPath = myHouseRental.getRows().get(i).getPicPath();
                String id = myHouseRental.getRows().get(i).getId();
                int approvedResult= myHouseRental.getRows().get(i).getApprovedResult();
                map.put("approvedResult",approvedResult);
                map.put("headimgurl", headimgurl);//头像
                map.put("id", id);//头像
                map.put("userName", userName);//用户名
                map.put("createTime", createTime);//发布时间
                map.put("title", title);//标题
                map.put("content", content);//内容
                map.put("contact",contact);//联系人
                map.put("contactPhone", contactPhone);//联系人电话
                map.put("picPath", picPath);//主图片
                map.put("replyNum", replyNum);//回复条数
                listGridTop.add(map);
            }
        } catch (Exception e) {
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
    public UserData getUserData() {
        String select = "select * from user";
        Cursor cursor = dbReader.rawQuery(select, null);
        UserData data = new UserData();
        while (cursor.moveToNext()) {
            String communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
            String communityName = cursor.getString(cursor.getColumnIndex("communityName"));
            boolean state = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("state")));
            boolean isAdmin = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isAdmin")));
            String userName = cursor.getString(cursor.getColumnIndex("userName"));
            String userId = cursor.getString(cursor.getColumnIndex("userId"));
            String uuid = cursor.getString(cursor.getColumnIndex("uuid"));
            String msg = cursor.getString(cursor.getColumnIndex("msg"));
            data.setCommunityCode(communityCode);
            data.setCommunityName(communityName);
            data.setIsAdmin(isAdmin);
            data.setState(state);
            data.setMsg(msg);
            data.setUserId(userId);
            data.setUserName(userName);
            data.setUuid(uuid);
        }
        return data;
    }
    @Override
    public void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestMessageTop();
                    }
                });
            }
        }).start();

    }
}
