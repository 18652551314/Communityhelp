package com.example.administrator.communityhelp.thirdpage.publication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Fragment_1;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Second_Fragment_2;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Seond_Hand_Details_1_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.second_hand.Seond_Hand_Details_Main;
import com.example.administrator.communityhelp.myadapter.MyAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.myinterface.ShuJu;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySecond_Hand_Main extends BaseActivity implements MyCallBack, View.OnClickListener {
//  ProgressDialogUtils  progress;
  boolean  isdeleting=false;
    String deleteId;
    AlertDialog dialog;
    View viewforPager;
    View view;
    int screenWidth;
    ViewPager viewPager;
    TextView textView1, textView2, textView3, textView_fenlei_1, textView_fenlei_2, textView_fenlei_3, textView_fenlei_4, textView_fenlei_5, textView_fenlei_6, textView_fenlei_7, textView_fenlei_8, textView_fenlei_9, textView_fenlei_10;
    LinearLayout linearLayout;
    ImageView imageView;
    RelativeLayout relativeLayout, tv_all, tv_shuma, tv_yinxiang, tv_meirong, tv_jiaju, tv_muying,
            tv_fushi, tv_jiadian, tv_bangong, tv_other;
    List<RelativeLayout> layouts;
    List<Fragment> list;
    Intent intent;
    MyAdapter myAdapter;
    FragmentManager manager = getSupportFragmentManager();
    int position1;
    View popuView;
    PopupWindow popuwindow;
    public ShuJu shuju;
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "columnList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/esInfoService";//地址

    private List<Map<String, Object>> listRecycler;

    String strValue = "";
    String strValue2="";
    String zhi2="";
    String code;

    public void setShuju(ShuJu shuju) {
        this.shuju = shuju;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_carpooling__main);
        viewforPager = findViewById(R.id.view_pin_1);
//        progress=new ProgressDialogUtils(this);
        creatPopWindow();
        GetRequestMessageTop();
        textView1 = (TextView) findViewById(R.id.first_tit);
        textView2 = (TextView) findViewById(R.id.first_mass_1);
        textView3 = (TextView) findViewById(R.id.first_mass_2);

        setTextCorlor(textView2);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout_popuwindow);
        imageView = (ImageView) findViewById(R.id.first_iv_3);
        //弹出popwindow 二手分类
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // relativeLayout= (RelativeLayout) findViewById(R.id.relativelayout_popuwindow);
//                linearLayout = (LinearLayout) popuView.findViewById(R.id.second_hand_blank);
                layouts = new ArrayList<RelativeLayout>();
                layouts.add(tv_all);
                layouts.add(tv_shuma);
                layouts.add(tv_yinxiang);
                layouts.add(tv_meirong);
                layouts.add(tv_muying);
                layouts.add(tv_jiaju);
                layouts.add(tv_fushi);
                layouts.add(tv_jiadian);
                layouts.add(tv_bangong);
                layouts.add(tv_other);
                //View v1 = manager.findFragmentById(R.id.relativelayout_popuwindow).getView();

                popuwindow.setTouchable(true);
                popuwindow.update();
                popuwindow.showAsDropDown(relativeLayout);

                tv_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_all);
                    }
                });
                tv_shuma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_shuma);
                        Second_Fragment_1 fragment1 = new Second_Fragment_1();
                        Bundle bundle = new Bundle();
                        strValue = listRecycler.get(8).get("code").toString();
                        bundle.putString("str", strValue);
                        fragment1.setArguments(bundle);
                        //如果transaction  commit（）过  那么我们要重新得到transaction
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.commit();
                        getData();
                        getTitles();

                    }
                });
                tv_yinxiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_yinxiang);
                    }
                });
                tv_meirong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                    }
                });
                tv_meirong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_meirong);
                    }
                });
                tv_jiaju.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_jiaju);
                    }
                });
                tv_muying.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_muying);
                    }
                });
                tv_fushi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_fushi);
                    }
                });
                tv_jiadian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_jiadian);
                    }
                });
                tv_bangong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_bangong);
                    }
                });
                tv_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_other);
                    }
                });
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                    }
                });
            }
        });

        imageView.setBackgroundResource(R.mipmap.oper);
        textView1.setText("二手市场");
        textView2.setText("转让");
        textView3.setText("求购");
        instart();
        viewPager = (ViewPager) findViewById(R.id.carpooling_vp);
        FragmentManager manager = getSupportFragmentManager();
        myAdapter = new MyAdapter(manager, list);
        viewPager.setAdapter(myAdapter);

        initTabLineWidth();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewforPager.getLayoutParams();
                lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                viewforPager.setLayoutParams(lp);
                position1 = position;
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTextCorlor(textView2);
                } else {
                    setTextCorlor(textView3);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void creatPopWindow() {
        popuView = getLayoutInflater().inflate(R.layout.popwindow_second_hand_classify, null);
        popuwindow = new PopupWindow(popuView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        tv_all = (RelativeLayout) popuView.findViewById(R.id.second_hand_all);
        tv_shuma = (RelativeLayout) popuView.findViewById(R.id.second_hand_shumachanping);
        tv_yinxiang = (RelativeLayout) popuView.findViewById(R.id.second_hand_MP3);
        tv_meirong = (RelativeLayout) popuView.findViewById(R.id.second_hand_meirongbaojian);
        tv_jiaju = (RelativeLayout) popuView.findViewById(R.id.second_hand_jiajuriyongpin);
        tv_muying = (RelativeLayout) popuView.findViewById(R.id.second_hand_muyingyongpin);
        tv_fushi = (RelativeLayout) popuView.findViewById(R.id.second_hand_fushiyongpin);
        tv_jiadian = (RelativeLayout) popuView.findViewById(R.id.second_hand_jiajujiadian);
        tv_bangong = (RelativeLayout) popuView.findViewById(R.id.second_hand_bangongyongpin);
        tv_other = (RelativeLayout) popuView.findViewById(R.id.second_hand_other);
        linearLayout = (LinearLayout) popuView.findViewById(R.id.second_hand_blank);
        textView_fenlei_1 = (TextView) popuView.findViewById(R.id.fenlei_1);
        textView_fenlei_2 = (TextView) popuView.findViewById(R.id.fenlei_2);
        textView_fenlei_3 = (TextView) popuView.findViewById(R.id.fenlei_3);
        textView_fenlei_4 = (TextView) popuView.findViewById(R.id.fenlei_4);
        textView_fenlei_5 = (TextView) popuView.findViewById(R.id.fenlei_5);
        textView_fenlei_6 = (TextView) popuView.findViewById(R.id.fenlei_6);
        textView_fenlei_7 = (TextView) popuView.findViewById(R.id.fenlei_7);
        textView_fenlei_8 = (TextView) popuView.findViewById(R.id.fenlei_8);
        textView_fenlei_9 = (TextView) popuView.findViewById(R.id.fenlei_9);
        textView_fenlei_10 = (TextView) popuView.findViewById(R.id.fenlei_10);
    }

    private void setLayoutColor(RelativeLayout layout) {
        for (RelativeLayout relayout : layouts) {
            relayout.setBackgroundColor(getResources().getColor(R.color.background_grey));
        }
        layout.setBackgroundColor(getResources().getColor(R.color.pressed_backgroud_black));
    }

    private void initTabLineWidth() {
        DisplayMetrics dpMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay()
                .getMetrics(dpMetrics);
        screenWidth = dpMetrics.widthPixels;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) viewforPager.getLayoutParams();
        lp.width = screenWidth / 2;
        viewforPager.setLayoutParams(lp);
    }

    public void carpool_click_1(View v) {
        viewPager.setCurrentItem(0);
        setTextCorlor(textView2);
    }

    public void carpool_click_2(View v) {
        viewPager.setCurrentItem(1);
        setTextCorlor(textView3);
    }

    public void iv_suppor_1(View v) {
        if (isLoad()) {
            if (position1 == 0) {
                intent = new Intent(this, Seond_Hand_Details_Main.class);
                startActivity(intent);
            } else if (position1 == 1) {
                intent = new Intent(this, Seond_Hand_Details_1_Main.class);
                startActivity(intent);
            }
        } else {
            intent = new Intent(this, Login_Main.class);
            startActivity(intent);
        }

    }

    public void ll_suppor_1(View v) {
        finish();
    }

    private void instart() {
        list = new ArrayList<>();
        list.add(new MySecond_Fragment_1());
        list.add(new MySecond_Fragment_2());
    }

    public void setTextCorlor(TextView text) {
        textView2.setTextColor(getResources().getColor(R.color.most_black));
        textView3.setTextColor(getResources().getColor(R.color.most_black));
        text.setTextColor(getResources().getColor(R.color.font_orange));
    }


    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
//        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();


        WebServiceUtils.callWebService(URL, MethodNameTop, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                Log.e("json", "json: " + result);
                if (result != null) {
                    //获取json数据
                    String json = result.getProperty(0).toString();
//                    progress.dismiss();
                    //解析json数据并放入list集合中
                    Log.e("json", "json: " + json);
                    listRecycler = jsonPaseTop(json);
                    textView_fenlei_2.setText(listRecycler.get(0).get("name").toString());
                    textView_fenlei_3.setText(listRecycler.get(1).get("name").toString());
                    textView_fenlei_4.setText(listRecycler.get(2).get("name").toString());
                    textView_fenlei_5.setText(listRecycler.get(3).get("name").toString());
                    textView_fenlei_6.setText(listRecycler.get(4).get("name").toString());
                    textView_fenlei_7.setText(listRecycler.get(5).get("name").toString());
                    textView_fenlei_8.setText(listRecycler.get(6).get("name").toString());
                    textView_fenlei_9.setText(listRecycler.get(7).get("name").toString());
                    textView_fenlei_10.setText(listRecycler.get(8).get("name").toString());
                } else {
                    Toast.makeText(viewforPager.getContext(), "获取WebService数据错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //解析json数据
    public List<Map<String, Object>> jsonPaseTop(String result) {
        listGridTop = new ArrayList();
        Log.e("json", "result: " + result);

        try {

//            String jjboom="["+result+"]";
            JSONArray array = new JSONArray(result);
            String jjboom = "[" + result + "]";


            for (int i = 0; i < array.length(); i++) {
                Map<String, Object> map = new HashMap<>();
                JSONObject jo = array.getJSONObject(i);
                String id = jo.getString("id");
                String name = jo.getString("name");
                String code = jo.getString("code");
                String sortNum = jo.getString("sortNum");

                map.put("id", id);
                map.put("name", name);
                map.put("code", code);
                map.put("sortNum", sortNum);
                map.put("id", id);//头像
                map.put("name", name);//用户名
                map.put("code", code);//发布时间
                map.put("sortNum", sortNum);//标题

                listGridTop.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listGridTop;
    }

    public String getTitles() {
        return strValue;
    }

    public String getData() {
        //返回的是选择后,textview的值

        return strValue;
    }
    public String getData2() {
        //返回的是选择后,textview的值

        return strValue2;
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
                while (isdeleting){
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                strValue2=strValue2+"a";
                getData2();
                viewPager.setCurrentItem(position1);
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    private void startDelete() {
        isdeleting=true;
//        progress.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", deleteId);
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "esInfoRemove", "esInfoService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = web.GetStringMessage();
//                    progress.dismiss();
                    isdeleting=false;
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MySecond_Hand_Main.this, "删除失败", Toast.LENGTH_SHORT).show();
//                            progress.dismiss();
                            isdeleting=false;
                            return;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetRequestMessageTop();
//                        progress.dismiss();
                        Toast.makeText(MySecond_Hand_Main.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        progress.dismiss();
    }

}
