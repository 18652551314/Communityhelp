package com.example.administrator.communityhelp.firstpage.bianmin.periphery;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_ShangPingLieBiaoActivity;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiGuidTopAdapter;
import com.example.administrator.communityhelp.myadapter.TaoShiHuiRecycleViewAdapter;
import com.example.administrator.communityhelp.myadapter.ZhouBianYouHuiRecycleViewAdapter;
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

public class ZhouBianYouHui_Main_MajorActivity extends BaseActivity implements View.OnClickListener {
    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "categoryRootList";//方法名
    public static final String MethodNameRecycler = "goodsList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/categoryService";//地址
    public static final String URLRecycler = "http://120.27.5.22:8080/services/goodsService";//地址
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private ZhouBianYouHuiRecycleViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;

    private List<Map<String, Object>> listGridTop;
    private TaoShiHuiGuidTopAdapter gridTopAdapter;
    private GridView gridViewTop;

    private TextView textView_header, textView_footer, textView_title;
    private View view_header, view_footer;
    private ImageView imageView_header, imageView_footer;
    private LinearLayout linearLayout;
    private int refresh[] = {R.drawable.common_loading3_0, R.drawable.common_loading3_1,
            R.drawable.common_loading3_2, R.drawable.common_loading3_3, R.drawable.common_loading3_4,
            R.drawable.common_loading3_5, R.drawable.common_loading3_6, R.drawable.common_loading3_7,
            R.drawable.common_loading3_8, R.drawable.common_loading3_9, R.drawable.common_loading3_10,
            R.drawable.common_loading3_11};
    private int i = 0;
    private Intent intent;
    private PopupWindow popupWindow;
    private int num = 7;
    private Map<String,Object> mapCode;
    private String communityCode;
    private EditText condition;
    private ProgressDialogUtils progress;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_zhou_bian_you_hui__main__major);
        initView();
        listGridTop = new ArrayList<>();
        GetRequestMessageTop();
        gridTopAdapter = new TaoShiHuiGuidTopAdapter(listGridTop, this);
        gridViewTop.setAdapter(gridTopAdapter);

        gridViewTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(ZhouBianYouHui_Main_MajorActivity.this, TaoShiHui_ShangPingLieBiaoActivity.class);
                intent.putExtra("id", listGridTop.get(position).get("id") + "");
                intent.putExtra("name", listGridTop.get(position).get("name") + "");
                intent.putExtra("code", listGridTop.get(position).get("code") + "");
                startActivity(intent);
            }
        });
        listRecycler = new ArrayList();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        设置RecyclerView网格显示
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        GetRequestMessageRecycler();
        //添加头部底部刷新加载视图
        addHeaderBottom();
        //设置刷新加载监听
        setRecycleViewListener();
        findViewById(R.id.ZhouBianYouHui_backMainMajor).setOnClickListener(this);
        findViewById(R.id.ZhouBianYouHui_select).setOnClickListener(this);
    }

    private void initView() {
        textView_title = (TextView) findViewById(R.id.textView_title);
        gridViewTop = (GridView) findViewById(R.id.TaoShiHui_gridViewTop);
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

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(this, "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        Log.e("communityCode", communityCode);
        map.put("communityCode",communityCode );
        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                //ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    //Log.e("json", "json: "+json);
                    listGridTop = jsonPaseTop(json);
                    gridTopAdapter = new TaoShiHuiGuidTopAdapter(listGridTop, ZhouBianYouHui_Main_MajorActivity.this);
                    gridViewTop.setAdapter(gridTopAdapter);
                } else {
                    Toast toast = Toast.makeText(ZhouBianYouHui_Main_MajorActivity.this, "", Toast.LENGTH_SHORT);
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
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String id = jo.getString("id");
                String code = jo.getString("code");
                String name = jo.getString("name");
                String columnImgPath = jo.getString("columnImgPath");
                map.put("id", id);
                map.put("code", code);
                map.put("name", name);
                map.put("columnImgPath", columnImgPath);
                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

    private void GetRequestMessageRecycler() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", 0);
        map.put("pageSize", 7);
        map.put("goodsCategoryCode", "");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis());
        map.put("type", "D");
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        map.put("communityCode",communityCode );
        WebServiceUtils.callWebService(URLRecycler, MethodNameRecycler, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    listRecycler = jsonPaseRecycler(json);
                    recyclerAdapter = new ZhouBianYouHuiRecycleViewAdapter(listRecycler, ZhouBianYouHui_Main_MajorActivity.this);
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    Toast toast = Toast.makeText(ZhouBianYouHui_Main_MajorActivity.this, "", Toast.LENGTH_SHORT);
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
        map.put("goodsCategoryCode", "");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis());
        map.put("type", "D");
        communityCode=getUserData().getCommunityCode()+"";
        mapCode=getCommunityCode();
        if(communityCode.equals("null")){
            communityCode=mapCode.get("communityCode")+"";
        }
        map.put("communityCode",communityCode );
        WebServiceUtils.callWebService(URLRecycler, MethodNameRecycler, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    listRecycler = jsonPaseRecycler(json);
                    recyclerAdapter.AddAll(listRecycler);
                    recyclerAdapter.notifyDataSetChanged();
                } else {
                    Toast toast = Toast.makeText(ZhouBianYouHui_Main_MajorActivity.this, "", Toast.LENGTH_SHORT);
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
                String goodsName = jo.getString("goodsName");
                String oldPrice = jo.getString("oldPrice");
                String discountPrice = jo.getString("discountPrice");
                String peopleBuy = jo.getString("peopleBuy");
                String summary = jo.getString("summary");
                String picPath = jo.getString("picPath");
                String merchantname = jo.getString("merchantname");
                map.put("id", id);
                map.put("goodsName", goodsName);
                map.put("oldPrice", oldPrice+"元");
                map.put("discountPrice", discountPrice+"元");
                map.put("peopleBuy", "已售 "+peopleBuy);
                map.put("summary", summary);
                map.put("picPath", picPath);
                map.put("merchantname", merchantname);
                listRecycler.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listRecycler;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ZhouBianYouHui_backMainMajor:
                finish();
                break;
            case R.id.ZhouBianYouHui_select:
                View view = View.inflate(this, R.layout.taoshihui_select, null);
                view.getBackground().setAlpha(200);
                condition = (EditText) view.findViewById(R.id.editText_condition);
                //设置键盘搜索按钮
                condition.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // 修改回车键功能
                        if (keyCode == KeyEvent.KEYCODE_ENTER  && event.getAction() == KeyEvent.ACTION_DOWN ) {
                            // 先隐藏键盘
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(ZhouBianYouHui_Main_MajorActivity.this
                                                    .getCurrentFocus().getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);
                            //接下来在这里做你自己想要做的事，实现自己的业务。
                            intent = new Intent(ZhouBianYouHui_Main_MajorActivity.this, TaoShiHui_ShangPingLieBiaoActivity.class);
                            intent.putExtra("name", "搜索结果");
                            intent.putExtra("condition", condition.getText().toString());
                            startActivity(intent);
                            popupWindow.dismiss();
                        }
                        return false;
                    }
                });
                ImageView select = (ImageView) view.findViewById(R.id.imageView_select);
                DisplayMetrics dm = new DisplayMetrics();
                //获取屏幕信息
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                int screenWidth = dm.widthPixels;
                int screenHeigh = dm.heightPixels;
                popupWindow = new PopupWindow(view, screenWidth,(int)(screenHeigh / 12), true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAtLocation(view, Gravity.TOP, 0, 0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 500);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(ZhouBianYouHui_Main_MajorActivity.this, TaoShiHui_ShangPingLieBiaoActivity.class);
                        intent.putExtra("name", "搜索结果");
                        intent.putExtra("condition", condition.getText().toString());
                        startActivity(intent);
                        popupWindow.dismiss();
                    }
                });
                break;
        }
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
