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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.Login_Main;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Fragment_1;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Fragment_2;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Rental_Details_Main;
import com.example.administrator.communityhelp.firstpage.linliquan.housing_rental.Housing_Rental_Details_Main_1;
import com.example.administrator.communityhelp.myadapter.MyAdapter;
import com.example.administrator.communityhelp.myinterface.MyCallBack;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/5.
 */
public class MyHousing_Rental extends BaseActivity implements MyCallBack,View.OnClickListener{
    ProgressDialogUtils progress;
    boolean isdeleting=false;
    int startDeletecode = 0;
    String deleteId;
    View viewDialog;
    AlertDialog dialog;
    View view;
    int screenWidth;
    ViewPager viewPager;
    TextView textView1, textView2, textView3;
    ImageView imageView;
    Intent intent;
    MyAdapter myAdapter;
    List<Fragment> list;
    int position1;

    @Override
    public void init() {
        super.init();
        progress=new ProgressDialogUtils(this);
        setContentView(R.layout.activity_carpooling__main);
        view = findViewById(R.id.view_pin_1);
        textView1 = (TextView) findViewById(R.id.first_tit);
        textView2 = (TextView) findViewById(R.id.first_mass_1);
        textView3 = (TextView) findViewById(R.id.first_mass_2);
        imageView = (ImageView) findViewById(R.id.first_iv_1);
        textView1.setText("房屋租赁");
        textView2.setText("出租");
        textView3.setText("求租");
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
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
                lp.leftMargin = screenWidth / 2 * position + positionOffsetPixels / 2;
                view.setLayoutParams(lp);
                position1=position;
            }

            @Override
            public void onPageSelected(int position) {
                position1=position;
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
                intent = new Intent(this, Housing_Rental_Details_Main.class);
                intent.putExtra("user_id", getUserData().getUserId());
                intent.putExtra("CommunityCode", getUserData().getCommunityCode());
                startActivity(intent);
            } else {
                intent = new Intent(this, Housing_Rental_Details_Main_1.class);
                intent.putExtra("user_id", getUserData().getUserId());
                intent.putExtra("CommunityCode", getUserData().getCommunityCode());
                startActivity(intent);
            }
        } else {
            intent=new Intent(this, Login_Main.class);
            startActivity(intent);
        }
    }

    public void ll_suppor_1(View v) {
        finish();
    }

    private void instart() {
        list = new ArrayList<>();
        list.add(new MyHousing_Fragment_1());
        list.add(new MyHousing_Fragment_2());
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
        TextView textMessage = (TextView) viewDialog.findViewById(R.id.textView_dialog_message);
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
        final WebSerVieceUtil web = new WebSerVieceUtil("http://info.service.zhidisoft.com", "houseRentalRemove", "infoExchangeService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = web.GetStringMessage();
                    progress.dismiss();
                    isdeleting = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MyHousing_Rental.this, "删除失败", Toast.LENGTH_SHORT).show();
                            isdeleting = false;
                            return;
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogUtils.dismissProgressDialog();
                        Toast.makeText(MyHousing_Rental.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

}
