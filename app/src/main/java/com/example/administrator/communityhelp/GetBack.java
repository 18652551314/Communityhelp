package com.example.administrator.communityhelp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GetBack extends BaseActivity {
    EditText editText_telNum, editText_checkCode;
    TextView textView_getCheckCode;
    Button btn_next_getBack;
    int i = -1, y = -1;
    private int recLen = 60;
    Timer timer;
    String mobile, verCode;
    boolean isRightTel = false;
    boolean isReady = false;
    String telNum = null;
    String checkCode = null;
    String ures;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        textView_getCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNum=editText_telNum.getText().toString();
                if(telNum.length()!=11){
                    Toast.makeText(GetBack.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!telNum.startsWith("1")){
                    Toast.makeText(GetBack.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(telNum.startsWith("11")||telNum.startsWith("12")||telNum.equals("19")){
                    Toast.makeText(GetBack.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (i == 1 && y == -1) {
                    y = 1;
                    ures = editText_telNum.getText().toString();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", editText_telNum.getText().toString());
                    final WebSerVieceUtil web01 = new WebSerVieceUtil("http://member.service.zhidisoft.com",
                            "pwdSendVer", "memberService", map);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = "";
                                json = web01.GetStringMessage();
                                Log.e("cat", json);
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GetBack.this, "没有网络", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (json.contains("false")) {
                                        y = -1;
                                        Toast.makeText(GetBack.this, "操作失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (json.contains("频繁")) {
                                        y = -1;
                                        Toast.makeText(GetBack.this, "请不要频繁进行验证", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(GetBack.this, "操作成功", Toast.LENGTH_SHORT).show();
                                    textView_getCheckCode.setBackgroundResource(R.color.item_line_gray);
                                    timer = new Timer();
                                    //tiemer三个参数，进行点击事件的规划
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {      // UI thread
                                                @Override
                                                public void run() {
                                                    recLen--;
                                                    textView_getCheckCode.setText("" + recLen + "秒后重置");
                                                    if (recLen < 0) {
                                                        timer.cancel();
                                                        textView_getCheckCode.setText("重新获取验证码");
                                                        if (i == 1) {
                                                            textView_getCheckCode.setBackgroundResource(R.color.telv);
                                                        }
                                                        y = -1;
                                                        recLen = 60;
                                                    }
                                                }
                                            });
                                        }
                                    }, 1000, 1000);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        editText_telNum.addTextChangedListener(mTextWatcher);
        btn_next_getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (isReady) {
                    mobile = editText_telNum.getText().toString();
                    verCode = editText_checkCode.getText().toString();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", mobile);
                    map.put("verCode", verCode);
                    final WebSerVieceUtil web02 =  new WebSerVieceUtil("http://member.service.zhidisoft.com",
                            "pwdVerification", "memberService", map);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = "";
                                json = web02.GetStringMessage();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (json.contains("false")) {
                                            Toast.makeText(GetBack.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                            return;
                                        } else {
                                            Intent intent = new Intent(GetBack.this, GetBackPassword.class);
                                            Gson gson=new Gson();
                                            GetBackData data=gson.fromJson(json,GetBackData.class);
                                            String userId=data.getValue();
                                            Log.e("cat",userId);
                                            intent.putExtra("userId",userId);
                                            startActivity(intent);
                                        }
                                    }
                                });

                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GetBack.this, "没有网络", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_get_back);
        editText_telNum = (EditText) findViewById(R.id.editText_telNum);
        editText_checkCode = (EditText) findViewById(R.id.editText_checkCode);
        textView_getCheckCode = (TextView) findViewById(R.id.textView_getCheckCode);
        btn_next_getBack = (Button) findViewById(R.id.btn_next_getBack);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            telNum = editText_telNum.getText().toString();
                            if (telNum.length() == 11) {
                                textView_getCheckCode.setBackgroundColor(getResources().getColor(R.color.title_green));
                                isRightTel = true;
                            } else {
                                textView_getCheckCode.setBackgroundColor(getResources().getColor(R.color.font_gray));
                                isRightTel = false;
                            }
                        }
                    });

                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkCode = editText_checkCode.getText().toString();
                            if (isRightTel && !checkCode.equals("")) {
                                isReady = true;
                                btn_next_getBack.setBackgroundColor(getResources().getColor(R.color.title_green));
                            } else {
                                btn_next_getBack.setBackgroundColor(getResources().getColor(R.color.font_gray));
                                isReady = false;
                            }
                        }
                    });

                }
            }
        }).start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
            if ((start - before) == 10) {
                Toast.makeText(GetBack.this,
                        "" + (start - before), Toast.LENGTH_SHORT)
                        .show();
                textView_getCheckCode.setBackgroundResource(R.color.telv);
                i = 1;
            } else if (!((start - before) == 10)) {
                textView_getCheckCode.setBackgroundResource(R.color.item_line_gray);
                i = -1;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = editText_telNum.getSelectionStart();
            editEnd = editText_telNum.getSelectionEnd();
        }
    };


    public static class GetBackData{

        /**
         * result : true
         * value : abf45678dadf658eee8acea
         * msg : 操作成功
         */

        private boolean result;
        private String value;
        private String msg;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
