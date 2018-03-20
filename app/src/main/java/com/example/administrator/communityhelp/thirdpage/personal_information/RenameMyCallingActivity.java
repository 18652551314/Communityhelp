package com.example.administrator.communityhelp.thirdpage.personal_information;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;

import java.util.HashMap;
import java.util.Map;

public class RenameMyCallingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename_my_calling);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.back_layout_myName);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final EditText editText = (EditText) findViewById(R.id.editText_myName);
        Button btn_commit = (Button) findViewById(R.id.btn_commitNewName);
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将填写的名字发送出去
                String name = editText.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(RenameMyCallingActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //预留一个发送数据的方法
                    SendNewName(name);
                }

            }
        });
    }

    private void SendNewName(String s) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("nickName", s);
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "nickNameSave", "memberService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonStr = webSerVieceUtil.GetStringMessage();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RenameMyCallingActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
//                Log.e("cat",jsonStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });
            }
        }).start();
    }
}
