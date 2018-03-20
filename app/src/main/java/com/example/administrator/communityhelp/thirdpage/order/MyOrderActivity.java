package com.example.administrator.communityhelp.thirdpage.order;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiRecycleViewAdapter;
import com.example.administrator.communityhelp.thirdpage.leaving_message.MyMessage;
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
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderActivity extends BaseActivity {
    TextView text_all;
    TextView text_noPay;
    TextView text_noJudge;
    TextView text_finish;
    //屏幕宽高
    int screenWidth;
    int screenHeight;
    View progress_line;
    //引导线的布局
    FrameLayout.LayoutParams params;
    LinearLayout linearLayout;
    //订单对象
    MyOrder order;
    RecyclerView recyclerView;
    MyOrderRecycleAdapter recyclerAdapter;
    List<MyOrder> listRecycler;
    //刷新动画
    int i;
    private TextView textView_header, textView_footer,textView_back,textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    TwinklingRefreshLayout refreshLayout;

    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "myOrderList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/goodsOrderService";//地址
    private int num = 7;
    private Map<String,Object> mapCode;
    private String orderState;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_my_order);
        initView();
        orderState="";
        GetRequestMessageRecycler();
        text_all.setTextColor(getResources().getColor(R.color.font_orange));
        //获取屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        //设置引导线长度
        params = new FrameLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.MATCH_PARENT);
        progress_line.setLayoutParams(params);
        setTextColor(text_all);
        recyclerView.setAdapter(recyclerAdapter);
        //添加头部底部刷新加载视图
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();
        //设置四个监听事件
        text_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //变颜色，变引导线位置
                params.setMargins(0, 0, 0, 0);
                progress_line.setLayoutParams(params);
                setTextColor(text_all);
                orderState="";
                GetRequestMessageRecycler();
                //预留切换Fragment
            }
        });
        text_noPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.setMargins(screenWidth / 4, 0, 0, 0);
                progress_line.setLayoutParams(params);
                setTextColor(text_noPay);
                orderState="submit_order";
                GetRequestMessageRecycler();
                //预留切换Fragment
            }
        });
        text_noJudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.setMargins(screenWidth / 2, 0, 0, 0);
                progress_line.setLayoutParams(params);
                setTextColor(text_noJudge);
                orderState="consumption";
                GetRequestMessageRecycler();
                //预留切换Fragment
            }
        });
        text_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.setMargins(screenWidth * 3 / 4, 0, 0, 0);
                progress_line.setLayoutParams(params);
                setTextColor(text_finish);
                orderState="evaluation";
                GetRequestMessageRecycler();
                //预留切换Fragment
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        text_all = (TextView) findViewById(R.id.textView_myOrder_all);
        text_noPay = (TextView) findViewById(R.id.textView_myOrder_noPay);
        text_noJudge = (TextView) findViewById(R.id.textView_myOrder_noJudge);
        text_finish = (TextView) findViewById(R.id.textView_myOrder_finish);
        progress_line = findViewById(R.id.myOrder_progress_line);
        linearLayout = (LinearLayout) findViewById(R.id.back_layout_myOrder);
        refreshLayout= (TwinklingRefreshLayout) findViewById(R.id.tkRefreshLayout_myOrder);
        recyclerView= (RecyclerView) findViewById(R.id.recycleView_myOrder);
    }
    public void setTextColor(TextView textColor) {
        text_all.setTextColor(getResources().getColor(R.color.most_black));
        text_noPay.setTextColor(getResources().getColor(R.color.most_black));
        text_noJudge.setTextColor(getResources().getColor(R.color.most_black));
        text_finish.setTextColor(getResources().getColor(R.color.most_black));
        textColor.setTextColor(getResources().getColor(R.color.font_orange));
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
        map.put("userId", getUserData().getUserId());
        map.put("merchantUserId", "");
        map.put("createOrderTimeStart", "");
        map.put("createOrderTimeEnd", "");
        map.put("orderState", orderState);
        map.put("orderNo", "");
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
                    recyclerAdapter=new MyOrderRecycleAdapter(MyOrderActivity.this,listRecycler);
                    recyclerAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    Toast toast = Toast.makeText(MyOrderActivity.this, "", Toast.LENGTH_SHORT);
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
        map.put("userId", getUserData().getUserId());
        map.put("merchantUserId", "");
        map.put("createOrderTimeStart", "");
        map.put("createOrderTimeEnd", "");
        map.put("orderState", orderState);
        map.put("orderNo", "");
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
                    Toast toast = Toast.makeText(MyOrderActivity.this, "", Toast.LENGTH_SHORT);
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
    public List<MyOrder> jsonPaseRecycler(String result) {
        try {
            listRecycler=new ArrayList<>();
            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("rows");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jo = array.getJSONObject(i);
                String id = jo.getString("id");
                String goodsName = jo.getString("goodsName");
                String createOrderTime = jo.getString("createOrderTime");
                String money = jo.getString("money");
                String goodsCount = jo.getString("goodsCount");
                String picPath = jo.getString("picPath");
                String orderStateCode = jo.getString("orderStateCode");
                String merchantGoodsId = jo.getString("merchantGoodsId");
                String isPayment = jo.getString("isPayment");
                MyOrder order=new MyOrder(id,picPath,goodsName,createOrderTime,money,goodsCount,orderStateCode,merchantGoodsId,isPayment);
                listRecycler.add(order);
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}
