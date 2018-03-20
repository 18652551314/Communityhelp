package com.example.administrator.communityhelp.thirdpage.publication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.myadapter.ErShou_ViewAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.mysql.UserData;
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

/**
 * Created by Administrator on 2017/1/6.
 */
public class MySecond_Fragment_1 extends Fragment implements MyCallBack, View.OnClickListener {
    ProgressDialogUtils progress;
    int loadtime = 2000;
    int total;
    String deleteId;
    View viewDialog;
    AlertDialog dialog;
    MyCallBack callBack;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    SqlHelper helper;
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    public ErShou_ViewAdapter recyclerAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private String leixing = "";
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
    Bundle bundle;

    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "myEsInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/esInfoService";//地址
    String panduan = "", zhi = "";
    String panduan2 = "", zhi2 = "";
    public int mun = 10;
    private MySecond_Hand_Main second_hand_main;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new ProgressDialogUtils(getActivity());
        inflater1 = inflater;
        view = inflater.inflate(R.layout.ershou_fragment_1, container, false);
        helper = new SqlHelper(getActivity());
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ershou);
        listRecycler = new ArrayList();
        GetRequestMessageTop();
        recyclerAdapter = new ErShou_ViewAdapter(listRecycler, view.getContext());
        recyclerAdapter.setMyCallBack(this);
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        second_hand_main = new MySecond_Hand_Main();
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
//        recyclerAdapter.setMyCallBack(new MyCallBack() {
//            @Override
//            public void getintent(LinearLayout linearLayout, ImageView imageView) {
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(view.getContext(),Second_Hand_Detaile_Information.class);
//                        startActivity(intent);
//                    }
//                });
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(view.getContext(), Forum_Reply.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });

        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        zhi = ((MySecond_Hand_Main) activity).getTitles();
        zhi2 = ((MySecond_Hand_Main) activity).getTitles();
        callBack = (MyCallBack) activity;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    MySecond_Hand_Main ac = (MySecond_Hand_Main) getActivity();
                    try {
                        zhi = ac.getData();
                        zhi2 = ac.getData2();
                    } catch (Exception e) {
                    }
                    if (!zhi.equals("")) {
                        if (zhi != panduan) {
                            panduan = zhi;
                            leixing = panduan;
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    GetRequestMessageTop();
                                    refreshLayout.finishLoadmore();
                                }
                            });
                        }
                    }
                    if (!zhi2.equals("")) {
                        if (zhi2 != panduan2) {
                            panduan2 = zhi2;
//                            leixing = panduan;
                            recyclerView.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        GetRequestMessageTop();
                                        recyclerAdapter.notifyDataSetChanged();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    refreshLayout.finishLoadmore();
                                }
                            });
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
                                                try {
                                                    imageView_header.setBackground(getResources().getDrawable(refresh[i]));
                                                } catch (Exception e) {
                                                }
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
                            if (total <= mun) {
                                textView_footer.setText("没有了");
                                loadtime = 0;
                                refreshLayout.finishLoadmore();
                                return;
                            } else {
                                GetRequestMessageTop();
                                Thread.sleep(1000);
                                mun += 7;
                                recyclerAdapter.AddAll(listRecycler);
                                recyclerAdapter.notifyDataSetChanged();
                                refreshLayout.finishLoadmore();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, loadtime);
            }

        });
    }

    //以下为接口数据部分
    public void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        try {
            progress.show();
        } catch (Exception e) {
        }
        //访问webservice服务需要的参数
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("pageNo", "-1");
        map.put("pageSize", mun + "");
        map.put("columnCode", leixing);
        map.put("communityCode", getUserData().getCommunityCode());
        map.put("sellOrBuy", "sell");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "myEsInfoList", "esInfoService", map);

        if (total <= mun) {
//            progress.dismiss();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String json = web.GetStringMessage();
//                    progress.dismiss();
                    Thread.sleep(500);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {   progress.dismiss();
                            }catch (Exception e){}
                            listRecycler = jsonPaseTop(json);
                            recyclerAdapter = new ErShou_ViewAdapter(listRecycler, view.getContext());
                            recyclerAdapter.setMyCallBack(callBack);
                            recyclerAdapter.setMyCallBack2(MySecond_Fragment_1.this);
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception e) {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {   progress.dismiss();
                                }catch (Exception e){}
                                Toast.makeText(getActivity(), "错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }catch (Exception e2){}
                }

            }
        }).start();

//        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
//            @Override
//            public void callBack(SoapObject result) {
//                Log.e("json", "json: " + result);
//                ProgressDialogUtils.dismissProgressDialog();
//                if (result != null) {
//                    //获取json数据
//                    String json = result.getProperty(0).toString();
//                    //解析json数据并放入list集合中
//                    Log.e("json", "json: " + json);
//                    listRecycler = jsonPaseTop(json);
//                    recyclerAdapter = new ErShou_ViewAdapter(listRecycler, view.getContext());
//                    recyclerAdapter.setMyCallBack(callBack);
//                    recyclerAdapter.setMyCallBack2(MySecond_Fragment_1.this);
//                    recyclerView.setAdapter(recyclerAdapter);
//                    recyclerAdapter.notifyDataSetChanged();
//                } else {
//                    // Toast.makeText(view.getContext(), "获取WebService数据错误", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    private void GetRequestMessageTop1() {
        //访问时弹出dialog对话框
        //ProgressDialogUtils.showProgressDialog(view.getContext(), "玩命加载中...");
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();

        map.put("pageNo", "-1");
        map.put("pageSize", mun);
        map.put("columnCode", leixing);
        map.put("communityCode", "");
        map.put("sellOrBuy", "sell");
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");


        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
//                Log.e("json", "json: " + result);
                ProgressDialogUtils.dismissProgressDialog();
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
                    //解析json数据并放入list集合中
//                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    recyclerAdapter = new ErShou_ViewAdapter(listRecycler, view.getContext());
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    // Toast.makeText(view.getContext(), "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
//        Log.e("json", "result: " + result);
        try {
            Gson gson = new Gson();
            MySecondHandMessage msg = gson.fromJson(result, MySecondHandMessage.class);
            int total = msg.getTotal();
            this.total = total;
            for (int i = 0; i < total; i++) {
                Map<String, Object> map = new HashMap<>();
                String userName = msg.getRows().get(i).getUserName();
                String createTime = msg.getRows().get(i).getCreateTime();
                String content = msg.getRows().get(i).getContent();
                String headimgurl = msg.getRows().get(i).getHeadimgurl();
                String title = msg.getRows().get(i).getTitle();
                String contactPhone = msg.getRows().get(i).getContactPhone();
                String picPath = msg.getRows().get(i).getPicPath();
                int replyNum = msg.getRows().get(i).getReplyNum();
                String id = msg.getRows().get(i).getId();
                int approvedResult = msg.getRows().get(i).getApprovedResult();
                map.put("total", total);
                map.put("approvedResult", approvedResult);
                map.put("id", id);//头像
                map.put("headimgurl", headimgurl);//头像
                map.put("userName", userName);//用户名
                map.put("createTime", createTime);//发布时间
                map.put("title", title);//标题
                map.put("contactPhone", contactPhone);//联系人电话
                map.put("content", content);//二手物品描述
                map.put("picPath", picPath);//二手物品主图片
                map.put("replyNum", replyNum);//回复条数
                listGridTop.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGridTop;
    }


    private void setRecycleViewListener_saixuan() {
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
    public void getintent(LinearLayout linearLayout, ImageView imageView) {

    }

    @Override
    public void delete(String id) {
//        GetRequestMessageTop();
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
        ProgressDialogUtils.showProgressDialog(getActivity(), "删除中");
        Map<String, String> map = new HashMap<>();
        map.put("id", deleteId);
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "esInfoRemove", "esInfoService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = web.GetStringMessage();
                    Log.e("isdelete", s);
                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestMessageTop();
                        ProgressDialogUtils.dismissProgressDialog();
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onStart() {
        super.onStart();
        GetRequestMessageTop();
//        progress.dismiss();
    }

    public class MySecondHandMessage {

        /**
         * total : 2
         * pageNo : 1
         * curTime : 1443241004946
         * pageSize : 100
         * rows : [{"createTime":"2015-09-24 14:52:09","contactPhone":"联系电话：15688125390","approve":true,"contact":"联&amp;nbsp;系&amp;nbsp;人：卢","replyNum":1,"id":"402881ee4ffd8c79014ffe1fa000000b","content":"三星note4九点五成新，无大修史，保修期，有保险，3000米出","picPath":"http://192.168.1.110:9999/upl0924145209712_thumbnail","title":"三星note4低价转让","price":3000,"userName":"小一一","headimgurl":"","approvedResult":1}]
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
             * createTime : 2015-09-24 14:52:09
             * contactPhone : 联系电话：15688125390
             * approve : true
             * contact : 联&amp;nbsp;系&amp;nbsp;人：卢
             * replyNum : 1
             * id : 402881ee4ffd8c79014ffe1fa000000b
             * content : 三星note4九点五成新，无大修史，保修期，有保险，3000米出
             * picPath : http://192.168.1.110:9999/upl0924145209712_thumbnail
             * title : 三星note4低价转让
             * price : 3000
             * userName : 小一一
             * headimgurl :
             * approvedResult : 1
             */

            private String createTime;
            private String contactPhone;
            private boolean approve;
            private String contact;
            private int replyNum;
            private String id;
            private String content;
            private String picPath;
            private String title;
            private int price;
            private String userName;
            private String headimgurl;
            private int approvedResult;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
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

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public int getReplyNum() {
                return replyNum;
            }

            public void setReplyNum(int replyNum) {
                this.replyNum = replyNum;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public int getPrice() {
                return price;
            }

            public void setPrice(int price) {
                this.price = price;
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

            public int getApprovedResult() {
                return approvedResult;
            }

            public void setApprovedResult(int approvedResult) {
                this.approvedResult = approvedResult;
            }
        }
    }


}
