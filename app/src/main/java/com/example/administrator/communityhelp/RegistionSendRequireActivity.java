package com.example.administrator.communityhelp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RegistionSendRequireActivity extends BaseActivity {
    EditText editText_nickName, editText_password, editText_passwordConfirm;
    TextView textView_communitySelect, textView_telNum;
    Button btn_sendRequire;
    String userName;
    String communityName = null;
    String communityCode = null;
    String nickName;
    String password;
    String passwordConform;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        textView_telNum.setText(userName);
        setCommunity();
        textView_communitySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistionSendRequireActivity.this, Community_Choice.class);
                startActivity(intent);
                finish();
            }
        });
        btn_sendRequire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequire();
            }
        });
    }

    private void sendRequire() {
        nickName = editText_nickName.getText().toString();
        password = editText_password.getText().toString();
        passwordConform = editText_passwordConfirm.toString();
        if (password.equals("") || nickName.equals("")) {
            Toast.makeText(this, "昵称密码不可以为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordConform)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("mobile", userName);
        map.put("nickName", nickName);
        map.put("password", password);
        map.put("communityCode", communityCode);
        final WebSerVieceUtil web = new WebSerVieceUtil("http://member.service.zhidisoft.com", "memberRegistered", "memberService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    json = web.GetStringMessage();
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegistionSendRequireActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (json.contains("成功")) {
                            Toast.makeText(RegistionSendRequireActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegistionSendRequireActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        }).start();

    }

    private void setCommunity() {
        Cursor cursor = dbReader.query("community", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            communityName = cursor.getString(cursor.getColumnIndex("communityName"));
            communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
        }
        if ((!communityName.equals("")) && (communityName != null)) {
            textView_communitySelect.setText(communityName);
        } else {
            Intent intent = new Intent(this, Community_Choice.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_registion_send_require);
        textView_telNum = (TextView) findViewById(R.id.textView_userName_rgiste);
        editText_nickName = (EditText) findViewById(R.id.editText_userNickName_registe);
        editText_password = (EditText) findViewById(R.id.editText_userPassword_registe);
        editText_passwordConfirm = (EditText) findViewById(R.id.editText_userPasswordConform_registe);
        textView_communitySelect = (TextView) findViewById(R.id.textView_communitySelect);
        btn_sendRequire = (Button) findViewById(R.id.btn_sendRegisteRequire);
    }
}
