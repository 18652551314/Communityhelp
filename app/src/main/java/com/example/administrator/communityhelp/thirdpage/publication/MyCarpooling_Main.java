package com.example.administrator.communityhelp.thirdpage.publication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Details_1_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Details_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Fragment_1;
import com.example.administrator.communityhelp.firstpage.linliquan.carpooling.Carpooling_Fragment_2;
import com.example.administrator.communityhelp.myadapter.MyAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MyCarpooling_Main extends BaseActivity implements MyCallBack, View.OnClickListener {
    ProgressDialogUtils progress;
    View viewDialog;
    AlertDialog dialog;
    boolean isdeleting = false;
    String deleteId;
    public int startDeletecode = 0;
    LinearLayout view;
    int screenWidth;
    ViewPager viewPager;
    TextView textView1, textView2, textView3;
    Intent intent;
    MyAdapter myAdapter;
    List<Fragment> list;
    int position1;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_carpooling__main);
        view = (LinearLayout) findViewById(R.id.view_pin_1);
        textView1 = (TextView) findViewById(R.id.first_tit);
        textView2 = (TextView) findViewById(R.id.first_mass_1);
        textView3 = (TextView) findViewById(R.id.first_mass_2);
        textView1.setText("拼车");
        textView2.setText("找乘客");
        textView3.setText("找拼车");
        instart();
        setTextCorlor(textView2);
        viewPager = (ViewPager) findViewById(R.id.carpooling_vp);
        FragmentManager manager = getSupportFragmentManager();
        myAdapter = new MyAdapter(manager, list);
        viewPager.setAdapter(myAdapter);

        initTabLineWidth();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                try{
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                    lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                    view.setLayoutParams(lp);
                }catch (Exception e){
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                    lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                    view.setLayoutParams(lp);
                }
                position1 = position;
            }

            @Override
            public void onPageSelected(int position) {
                position1 = position;
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

    public void setTextCorlor(TextView text) {
        textView2.setTextColor(getResources().getColor(R.color.most_black));
        textView3.setTextColor(getResources().getColor(R.color.most_black));
        text.setTextColor(getResources().getColor(R.color.font_orange));
    }


    private void instart() {
        list = new ArrayList<>();
        list.add(new MyCarpooling_Fragment_1());
        list.add(new MyCarpooling_Fragment_2());
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
        setTextCorlor(textView2);
        viewPager.setCurrentItem(0);
    }

    public void carpool_click_2(View v) {
        setTextCorlor(textView3);
        viewPager.setCurrentItem(1);
    }

    public void iv_suppor_1(View v) {
        if (isLoad()) {
            if (position1 == 0) {
                intent = new Intent(this, Carpooling_Details_Main.class);
                intent.putExtra("user_id", getUserData().getUserId());
                intent.putExtra("CommunityCode", getUserData().getCommunityCode());
                startActivity(intent);
            } else if (position1 == 1) {
                intent = new Intent(this, Carpooling_Details_1_Main.class);
                intent.putExtra("user_id", getUserData().getUserId());
                intent.putExtra("CommunityCode", getUserData().getCommunityCode());
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

    @Override
    public void getintent(LinearLayout linearLayout, ImageView imageView) {

    }

    @Override
    public void delete(String id) {
        startDeletecode = 1;
        deleteId = id;
        setDialogView("确定要删除吗");
    }

    private void setDialogView(String message) {
        viewDialog = getLayoutInflater().inflate(R.layout.dialog_view, null);
        viewDialog.findViewById(R.id.btn_sure).setOnClickListener(this);
        viewDialog.findViewById(R.id.btn_cancel).setOnClickListener(this);
        TextView textMessage = (TextView) viewDialog .findViewById(R.id.textView_dialog_message);
        textMessage.setText(message);
        viewDialog.getBackground().setAlpha(150);
        dialog = new AlertDialog.Builder(this)
                .setView(viewDialog)
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
                while (isdeleting) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startDeletecode = 0;
                viewPager.setCurrentItem(position1);
                dialog.dismiss();
                break;
            case R.id.btn_cancel:

                dialog.dismiss();
                break;
        }
    }

    private void startDelete() {
        isdeleting = true;
        progress.show();
        Map<String, String> map = new HashMap<>();
        map.put("id", deleteId);
        map.put("userId", getUserData().getUserId());
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "carPoolingInfoRemove", "carPoolingInfoService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = web.GetStringMessage();
                    isdeleting = false;
                    progress.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyCarpooling_Main.this, "删除失败", Toast.LENGTH_SHORT).show();
                            isdeleting = false;
                            return;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      progress.dismiss();
                        Toast.makeText(MyCarpooling_Main.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}
