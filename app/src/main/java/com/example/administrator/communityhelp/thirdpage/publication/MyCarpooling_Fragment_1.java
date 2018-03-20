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
import com.example.administrator.communityhelp.myadapter.BianMin_One_ViewAdapter;
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
public class MyCarpooling_Fragment_1 extends Fragment implements MyCallBack {
    ProgressDialogUtils progress;
    int loadtime = 2000;
    int total;
    String deleteId;
    SqlHelper helper;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    MyCallBack callBack;
    Thread thread;
    private List<Map<String, Object>> listRecycler;
    private RecyclerView recyclerView;
    private BianMin_One_ViewAdapter recyclerAdapter;
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
    MyCallBack myCallBack;
    private int recycleImage[] = {R.drawable.poster1};
    private String gridName[] = {"活动1", "活动2", "活动3", "活动4", "活动5", "活动6"};
    private String date[] = {(String) DateFormat.format("yyyy年MM月dd日 HH:mm", new Date())};
    private String referral[] = {"介绍1", "介绍2", "介绍3", "介绍4", "介绍5", "介绍6"};
    View view;

    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "myCarPoolingInfoList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/carPoolingInfoService";//地址
    public int mun = 10;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBack = (MyCallBack) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progress = new ProgressDialogUtils(getActivity());
        helper = new SqlHelper(getActivity());
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        inflater1 = inflater;
        view = inflater.inflate(R.layout.carpooling_fragment_1, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_pinche);
        listRecycler = new ArrayList();
        GetRequestMessageTop();
        recyclerAdapter = new BianMin_One_ViewAdapter(listRecycler, view.getContext());
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
//        recyclerAdapter.setMyCallBack(new MyCallBack() {
//            @Override
//            public void getintent(LinearLayout linearLayout, ImageView imageView) {
//                linearLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(view.getContext(),Carpooling_Detaile_Information.class);
//                        startActivity(intent);
//                    }
//                });
//
//            }
//
//        });
        begainRefresh();
        return view;
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
        try {
            progress.show();
        } catch (Exception e) {
        }

        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNo", "0");
        map.put("pageSize", mun);
        map.put("userId", getUserData().getUserId());
        map.put("type", "zck");
        map.put("communityCode", getUserData().getCommunityCode());
        map.put("condition", "");
        map.put("curTime", System.currentTimeMillis() + "");

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
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    recyclerAdapter = new BianMin_One_ViewAdapter(listRecycler, view.getContext());
                    recyclerAdapter.setMyCallBack(callBack);
                    recyclerAdapter.setMyCallBack2(MyCarpooling_Fragment_1.this);
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
            Gson gson = new Gson();
            myCarpooling myCarpool = gson.fromJson(result, myCarpooling.class);
            int total = myCarpool.getTotal();
            this.total = total;
            for (int i = 0; i < total; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("total", total);
                String userName = myCarpool.getRows().get(i).getUserName();
                String creatTime = myCarpool.getRows().get(i).getCreatTime();
                String content = myCarpool.getRows().get(i).getContent();
                String headimgurl = myCarpool.getRows().get(i).getHeadimgurl();
                String end = myCarpool.getRows().get(i).getEnd();
                String id = myCarpool.getRows().get(i).getId();
                String start = myCarpool.getRows().get(i).getStart();
                String startTime = myCarpool.getRows().get(i).getStartTime();
                String contact = myCarpool.getRows().get(i).getContact();
                String contactPhone = myCarpool.getRows().get(i).getContactPhone();
                String carInfo = myCarpool.getRows().get(i).getCarInfo();
                int approvedResult = myCarpool.getRows().get(i).getApprovedResult();
                map.put("approvedResult", approvedResult);
                map.put("headimgurl", headimgurl);//头像
                map.put("id", id);//头像
                map.put("userName", userName);//用户名
                map.put("creatTime", creatTime);//发布时间
                map.put("end", end);//终点
                map.put("start", start);//起点
                map.put("startTime", startTime);//出发时间
                map.put("contact", contact);//联系人
                map.put("contactPhone", contactPhone);//联系人电话
                map.put("carInfo", carInfo);//车辆信息（类型）
                map.put("content", content);//车辆描述
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
    }

    private void begainRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        MyCarpooling_Main activity = (MyCarpooling_Main) getActivity();
                        if (activity.startDeletecode == 1) {
                            while (true) {
                                activity = (MyCarpooling_Main) getActivity();
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

    public class myCarpooling {


        /**
         * total : 1
         * pageNo : 1
         * curTime : 1443256415409
         * pageSize : 100
         * rows : [{"approve":true,"contactPhone":"13938510190","infoType":"zck","creatTime":"2014-11-13 09:53:45","contact":"李先生","id":"fdaeb24549a6978c0149a6db3b5a0014","content":"1月5号凡最早班车到新郑机场，市区环内管接送，安全，准时","startTime":"2015-01-05 07:30:00","picPath":"http://192.168.1.110:9999/upl544686_thumbnail","start":"郑州","userName":"sqgl","headimgurl":"","carInfo":" 轿车","approvedResult":1,"end":"新郑机场"}]
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
             * approve : true
             * contactPhone : 13938510190
             * infoType : zck
             * creatTime : 2014-11-13 09:53:45
             * contact : 李先生
             * id : fdaeb24549a6978c0149a6db3b5a0014
             * content : 1月5号凡最早班车到新郑机场，市区环内管接送，安全，准时
             * startTime : 2015-01-05 07:30:00
             * picPath : http://192.168.1.110:9999/upl544686_thumbnail
             * start : 郑州
             * userName : sqgl
             * headimgurl :
             * carInfo :  轿车
             * approvedResult : 1
             * end : 新郑机场
             */

            private boolean approve;
            private String contactPhone;
            private String infoType;
            private String creatTime;
            private String contact;
            private String id;
            private String content;
            private String startTime;
            private String picPath;
            private String start;
            private String userName;
            private String headimgurl;
            private String carInfo;
            private int approvedResult;
            private String end;

            public boolean isApprove() {
                return approve;
            }

            public void setApprove(boolean approve) {
                this.approve = approve;
            }

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public String getInfoType() {
                return infoType;
            }

            public void setInfoType(String infoType) {
                this.infoType = infoType;
            }

            public String getCreatTime() {
                return creatTime;
            }

            public void setCreatTime(String creatTime) {
                this.creatTime = creatTime;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
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

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getPicPath() {
                return picPath;
            }

            public void setPicPath(String picPath) {
                this.picPath = picPath;
            }

            public String getStart() {
                return start;
            }

            public void setStart(String start) {
                this.start = start;
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

            public String getCarInfo() {
                return carInfo;
            }

            public void setCarInfo(String carInfo) {
                this.carInfo = carInfo;
            }

            public int getApprovedResult() {
                return approvedResult;
            }

            public void setApprovedResult(int approvedResult) {
                this.approvedResult = approvedResult;
            }

            public String getEnd() {
                return end;
            }

            public void setEnd(String end) {
                this.end = end;
            }
        }
    }
}
