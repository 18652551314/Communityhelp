package com.example.administrator.communityhelp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.mysql.UserData;
import com.example.administrator.communityhelp.thirdpage.UserMessage;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.example.administrator.communityhelp.thirdpage.personal_information.PersonnalMessageActivity;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Login_Main extends BaseActivity {
    Button btn_login;
    EditText editText_username;
    EditText editText_userpassword;
    TextView textView_rigester, textView_getback_tv;
    String userJason;
    String jasonStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__main);
        btn_login = (Button) findViewById(R.id.btn_login);
        editText_username = (EditText) findViewById(R.id.editText_userName_login);
        editText_userpassword = (EditText) findViewById(R.id.editText_userPassword_login);
        textView_getback_tv = (TextView) findViewById(R.id.getback_tv);
        textView_rigester = (TextView) findViewById(R.id.registration_tv);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s1 = editText_username.getText().toString();
                if (editText_username.getText() == null || editText_username.getText().equals("")
                        || editText_userpassword.getText() == null || editText_userpassword.getText().equals("")) {
                    Toast.makeText(Login_Main.this, "用户名或密码不可以为空", Toast.LENGTH_LONG).show();
                }
                if (editText_username.getText().toString().length() != 11) {
                    Toast.makeText(Login_Main.this, "请输入正确的用户名", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!s1.startsWith("1")){
                    Toast.makeText(Login_Main.this, "请输入正确的用户名", Toast.LENGTH_LONG).show();
                    return;}
                if(s1.startsWith("11")){
                    Toast.makeText(Login_Main.this, "请输入正确的用户名", Toast.LENGTH_LONG).show();
                    return;}
                if(s1.startsWith("12")||s1.startsWith("19")){
                    Toast.makeText(Login_Main.this, "请输入正确的用户名", Toast.LENGTH_LONG).show();
                }
                Map<String, String> map = new HashMap();
                map.put("name", editText_username.getText().toString());
                map.put("pwd", editText_userpassword.getText().toString());
                final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://service.zhidisoft.com", "login", "loginService", map);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            userJason = webSerVieceUtil.GetStringMessage();
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login_Main.this, "没有网络", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }
                        //解析jason
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userJason.contains("失败")) {
                                    Toast.makeText(Login_Main.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (userJason.contains("不存在")) {
                                    Toast.makeText(Login_Main.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                ContentValues values = new ContentValues();
                                Gson gson = new Gson();
                                //用Gson解析json数据获取UserData对象
                                UserData user = gson.fromJson(userJason, UserData.class);
                                values.put("communityCode", user.getCommunityCode());
                                values.put("userId", user.getUserId());
                                values.put("state", user.isState());
                                values.put("userName", user.getUserName());
                                values.put("isAdmin", user.isAdmin());
                                values.put("communityName", user.getCommunityName());
                                values.put("uuid", user.getUuid());
                                values.put("msg", user.getMsg());
                                dbweiter.insert("user", null, values);
                                values.clear();
                                dbweiter.delete("community", null, null);
                                values.put("communityName", user.getCommunityName());
                                values.put("communityCode", user.getCommunityCode());
                                dbweiter.insert("community", null, values);
                                //将nickname等信息存入数据库
                                Map<String, String> map = new HashMap<>();
                                map.put("userId", getUserData().getUserId());
                                final WebSerVieceUtil webSerVieceUtil = new WebSerVieceUtil("http://member.service.zhidisoft.com", "memberInfo", "memberService", map);
//设置从网络获取的头像
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            jasonStr = null;
                                            jasonStr = webSerVieceUtil.GetStringMessage();
                                        } catch (Exception e) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(Login_Main.this, "没有网络", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            return;
                                        }
                                        Gson gson = new Gson();
                                        UserMessage userMessage;
                                        userMessage = gson.fromJson(userJason, UserMessage.class);
                                        ContentValues values1=new ContentValues();
                                        values1.put("headImage",userMessage.getHeadImg());
                                        values1.put("nickName",userMessage.getNickName());
                                        values1.put("account",userMessage.getAccount());
                                        dbweiter.insert("userMessage",null,values1);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(Login_Main.this,"登陆成功",Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                }).start();
                            }
                        });
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView_rigester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Main.this, Registration_Main.class);
                startActivity(intent);
            }
        });
        textView_getback_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Main.this, GetBack.class);
                startActivity(intent);
            }
        });
    }
}
