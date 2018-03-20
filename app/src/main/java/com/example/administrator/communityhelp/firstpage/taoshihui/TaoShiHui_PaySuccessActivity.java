package com.example.administrator.communityhelp.firstpage.taoshihui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.thirdpage.order.MyOrderActivity;

public class TaoShiHui_PaySuccessActivity extends BaseActivity {
    private TextView textView_back;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_tao_shi_hui__pay_success);
        textView_back= (TextView) findViewById(R.id.payActivity_backOrder);
        textView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TaoShiHui_PaySuccessActivity.this,MyOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
