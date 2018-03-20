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

public class ChangePasswordActivity extends BaseActivity {
    String jsonStr;
    EditText editText_oldPassword;
    EditText editText_newPassword;
    EditText editText_conformPassword;
    Button btn_commitPassword;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_commitPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //预留判断旧密码是否输入正确的方法
                String oldpwd = editText_newPassword.getText().toString();
                String confpwd = editText_conformPassword.getText().toString();
                if (!IsTruePassword(editText_oldPassword.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText_conformPassword.getText().toString().trim().equals("") || editText_newPassword.getText().toString().trim().equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!oldpwd.equals(confpwd)) {
                    Toast.makeText(ChangePasswordActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //预留发送新的密码
                    sendNewPassword(editText_oldPassword.getText().toString(), editText_newPassword.getText().toString());
                }
            }
        });
    }

    private void sendNewPassword(String oldpwd, String newpwd) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("oldpassword", oldpwd);
        map.put("password", newpwd);
        final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "passwordVal", "memberService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    jsonStr = webSerVieceUtil.GetStringMessage();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ChangePasswordActivity.this,"没有网络",Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
//                Log.e("cat",jsonStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jsonStr.contains("成功")) {
                            Toast.makeText(ChangePasswordActivity.this, "修改成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "修改失败", Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });
            }
        }).start();
    }

    private boolean IsTruePassword(String text) {
        return true;
    }

    public void init() {
        editText_oldPassword = (EditText) findViewById(R.id.edittext_oldPassword);
        editText_newPassword = (EditText) findViewById(R.id.edittext_newPassword);
        editText_conformPassword = (EditText) findViewById(R.id.edittext_conformPassword);
        linearLayout = (LinearLayout) findViewById(R.id.back_layout_myPassword);
        btn_commitPassword = (Button) findViewById(R.id.btn_commitNewPassword);
    }
}
