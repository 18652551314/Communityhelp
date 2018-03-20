package com.example.administrator.communityhelp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/9.
 */
public class GetBackPassword extends BaseActivity {
    LinearLayout linearLayout_back;
    EditText editTextPassword, editTextConformPassword;
    Button btn_commit;
    String userId = "";
    String password = "";
    String passwordConform = "";
    String jsonStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getback_password);
        init();
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        linearLayout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitResetRequire();
            }
        });

    }

    private void commitResetRequire() {
        password = editTextPassword.getText().toString();
        passwordConform = editTextConformPassword.getText().toString();
        if (password.equals("") || passwordConform.equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordConform)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        final WebSerVieceUtil web = new WebSerVieceUtil("http://member.service.zhidisoft.com", "passwordSave", "memberService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonStr = web.GetStringMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(jsonStr.contains("true")){
                                Toast.makeText(GetBackPassword.this,"修改成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GetBackPassword.this,"没有网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();


    }

    @Override
    public void init() {
        super.init();
        linearLayout_back = (LinearLayout) findViewById(R.id.back_layout_resetPassword);
        editTextPassword = (EditText) findViewById(R.id.edittext_newPassword_reset);
        editTextConformPassword = (EditText) findViewById(R.id.edittext_conformPassword_reset);
        btn_commit = (Button) findViewById(R.id.btn_commitNewPassword_reset);

    }
}
