package com.example.administrator.communityhelp.thirdpage.publication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.tourism.Tourism_Details_Main;
import com.example.administrator.communityhelp.myadapter.LvYou_One_ViewAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
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

public class MyTourism_Main extends BaseActivity implements MyCallBack, View.OnClickListener {
  ProgressDialogUtils  progress;
    int loadtime = 2000;
    int total;
    View view;
    AlertDialog dialog;
    String deleteId;
    TextView textView;
    Intent intent;
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private LvYou_One_ViewAdapter recyclerAdapter;
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


    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "myLyInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/lyInfoService";//地址
    public int mun = 10;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_tourism__main);
        progress=new ProgressDialogUtils(this);
        textView = (TextView) findViewById(R.id.first_tit_tit);
        textView.setText("自助旅游");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_forum);
        listRecycler = new ArrayList();
        GetRequestMessageTop();
        recyclerAdapter = new LvYou_One_ViewAdapter(listRecycler, this);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        //添加条目线条
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
//        设置RecyclerView水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                        Intent intent2=new Intent(Tourism_Main.this,Tourism_Detaile_Information.class);
                        startActivity(intent2);
                    }
                });
            }
        });*/


    }

    public void tourism_main_ll(View v) {
        intent = new Intent(this, Tourism_Details_Main.class);
        intent.putExtra("user_id", getUserData().getUserId());
        intent.putExtra("CommunityCode", getUserData().getCommunityCode());
        startActivity(intent);
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
                            mun += 7;
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
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", "-1");
        map.put("pageSize", mun);
        map.put("userId", getUserData().getUserId());
        map.put("communityCode", "00001");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");

        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                progress.dismiss();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    recyclerAdapter = new LvYou_One_ViewAdapter(listRecycler, MyTourism_Main.this);
                    recyclerAdapter.setMyCallBack(MyTourism_Main.this);
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    Toast.makeText(MyTourism_Main.this, "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        Log.e("json", "result: " + result);

        try {
            Gson gson = new Gson();
            myTravel mytrav = gson.fromJson(result, myTravel.class);
            int total = mytrav.getTotal();
            this.total = total;
            for (int i = 0; i < total; i++) {
                Map<String, Object> map = new HashMap<>();
                String picPath = mytrav.getRows().get(i).getPicPath();
                String id = mytrav.getRows().get(i).getId();
                String title = mytrav.getRows().get(i).getTitle();
                String contact = mytrav.getRows().get(i).getContact();
                String contactPhone = mytrav.getRows().get(i).getContactPhone();
                int approvedResult = mytrav.getRows().get(i).getApprovedResult();
                map.put("approvedResult", approvedResult);
                map.put("total", total);
                map.put("picPath", picPath);//主图片
                map.put("title", title);
                map.put("contact", contact);//联系人
                map.put("contactPhone", contactPhone);//联系人电话
                map.put("id", id);
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

    @Override
    public void getintent(LinearLayout linearLayout, ImageView imageView) {

    }

    @Override
    public void delete(String id) {
        deleteId = id;
        setDialogView("确定要删除吗");
    }

    private void setDialogView(String message) {
        view = getLayoutInflater().inflate(R.layout.dialog_view, null);
        view.findViewById(R.id.btn_sure).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView textMessage = (TextView) view.findViewById(R.id.textView_dialog_message);
        textMessage.setText(message);
        view.getBackground().setAlpha(150);
        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                startDelete();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    private void startDelete() {
        progress.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", deleteId);
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "lyInfoRemove", "lyInfoService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = web.GetStringMessage();
                    progress.dismiss();
                    Log.e("isdelete", s);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                            Toast.makeText(MyTourism_Main.this, "删除失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestMessageTop();
                        progress.dismiss();
                        Toast.makeText(MyTourism_Main.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    public class myTravel {

        /**
         * total : 1
         * pageNo : 1
         * curTime : 1443241800465
         * pageSize : 10
         * rows : [{"id":"402881ee4ffd8c79014ffe27c5fe001c","picPath":"http://192.168.1.110:9999/upl0150924150103286","title":"十一自驾游","contactPhone":"联系电话：15688125390","approve":true,"userName":"小一一","headimgurl":"","contact":"联系人：卢","approvedResult":1}]
         */

        private int total;
        private int pageNo;
        private long curTime;
        private int pageSize;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public long getCurTime() {
            return curTime;
        }

        public void setCurTime(long curTime) {
            this.curTime = curTime;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public class RowsBean {
            /**
             * id : 402881ee4ffd8c79014ffe27c5fe001c
             * picPath : http://192.168.1.110:9999/upl0150924150103286
             * title : 十一自驾游
             * contactPhone : 联系电话：15688125390
             * approve : true
             * userName : 小一一
             * headimgurl :
             * contact : 联系人：卢
             * approvedResult : 1
             */

            private String id;
            private String picPath;
            private String title;
            private String contactPhone;
            private boolean approve;
            private String userName;
            private String headimgurl;
            private String contact;
            private int approvedResult;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPicPath() {
                return picPath;
            }

            public void setPicPath(String picPath) {
                this.picPath = picPath;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public boolean isApprove() {
                return approve;
            }

            public void setApprove(boolean approve) {
                this.approve = approve;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getHeadimgurl() {
                return headimgurl;
            }

            public void setHeadimgurl(String headimgurl) {
                this.headimgurl = headimgurl;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public int getApprovedResult() {
                return approvedResult;
            }

            public void setApprovedResult(int approvedResult) {
                this.approvedResult = approvedResult;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetRequestMessageTop();
    }
}
