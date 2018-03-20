package com.example.administrator.communityhelp.firstpage.linliquan.second_hand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Details_1_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Details_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Fragment_1;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Fragment_2;
import com.example.administrator.communityhelp.firstpage.linliquan.recruit.Recruit_Details_main;
import com.example.administrator.communityhelp.myadapter.ErShou_ViewAdapter;
import com.example.administrator.communityhelp.myadapter.MyAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;

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

public class Second_Hand_Main extends BaseActivity {
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
    //以下为借口部分数据
    private List<Map<String, Object>> listGridTop, listGridCenter;
    public static final String NameSpace = "http://info.service.zhidisoft.com";  //命名空间
    public static final String MethodNameTop = "columnList";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/esInfoService";//地址
    ProgressDialogUtils progress;
    private List<Map<String, Object>> listRecycler;

    String strValue = "";
    int code;

    int ppp;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_carpooling__main);
        view = findViewById(R.id.view_pin_1);

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
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = "a";
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = "a";
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        }


                    }
                });
                tv_shuma.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_shuma);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(0).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();

                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(0).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        }

                    }
                });
                tv_yinxiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_yinxiang);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(1).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(1).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        }
                    }
                });

                tv_meirong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_meirong);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(2).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(2).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        }
                    }
                });
                tv_jiaju.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_jiaju);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(3).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(3).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        }
                    }
                });
                tv_muying.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_muying);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(4).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(4).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        }
                    }
                });
                tv_fushi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_fushi);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(5).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(5).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        }
                    }
                });
                tv_jiadian.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_jiadian);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(6).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();
                            getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(6).get("code").toString();
                            bundle.putString("str", strValue);
                            code=2;
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();

                        }
                    }
                });
                tv_bangong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_bangong);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(7).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(7).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        }
                    }
                });
                tv_other.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popuwindow.dismiss();
                        setLayoutColor(tv_other);
                        if (ppp == 0) {
                            Second_Fragment_1 fragment1 = new Second_Fragment_1();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(8).get("code").toString();
                            code=1;
                            bundle.putString("str", strValue);
                            fragment1.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        } else {
                            Second_Fragment_2 fragment2 = new Second_Fragment_2();
                            Bundle bundle = new Bundle();
                            strValue = listRecycler.get(8).get("code").toString();
                            code=2;
                            bundle.putString("str", strValue);
                            fragment2.setArguments(bundle);
                            //如果transaction  commit（）过  那么我们要重新得到transaction
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.commit();
                            getData();
                            getTitles();getCode();
                        }

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
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                view.setLayoutParams(lp);
                position1 = position;
            }

            @Override
            public void onPageSelected(int position) {
                ppp = position;
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
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        lp.width = screenWidth / 2;
        view.setLayoutParams(lp);
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
        list.add(new Second_Fragment_1());
        list.add(new Second_Fragment_2());
    }

    public void setTextCorlor(TextView text) {
        textView2.setTextColor(getResources().getColor(R.color.most_black));
        textView3.setTextColor(getResources().getColor(R.color.most_black));
        text.setTextColor(getResources().getColor(R.color.font_orange));
    }


    //以下为接口数据部分

    private void GetRequestMessageTop() {
        //访问时弹出dialog对话框
        progress=new ProgressDialogUtils(this);
        progress.show();
        //访问webservice服务需要的参数
        HashMap<String, Object> map = new HashMap<>();


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


    public int getCode() {
        //返回的是选择后,textview的值

        return code;
    }
}
