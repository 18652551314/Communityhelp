package com.example.administrator.communityhelp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.myadapter.TaoShiHuiGuidTopAdapter;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
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
import java.util.Timer;
import java.util.TimerTask;

public class Registration_Main extends BaseActivity {
    Button btn_next;
    ImageView imageView_selected;
    EditText editText_telNum, editText_checkCode;
    TextView textView_checkCode;
    private int recLen = 60;
    Timer timer;
    private List<Map<String, Object>> listGridTop, listGridCenter;
    String ures;
    int i = -1, y = -1;
    String json;
    boolean isSelected = true;
    boolean isReady = false;

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_registration__main);
        imageView_selected = (ImageView) findViewById(R.id.imageView_select_communityRules);
        editText_telNum = (EditText) findViewById(R.id.shoujihao_et);
        btn_next = (Button) findViewById(R.id.btn_registe_next);
        editText_checkCode = (EditText) findViewById(R.id.edittext_checkCode);
        textView_checkCode = (TextView) findViewById(R.id.yanzhengma_tv);
        textView_checkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNum=editText_telNum.getText().toString();
                if(telNum.length()!=11){
                    Toast.makeText(Registration_Main.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!telNum.startsWith("1")){
                    Toast.makeText(Registration_Main.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(telNum.startsWith("11")||telNum.startsWith("12")||telNum.equals("19")){
                    Toast.makeText(Registration_Main.this,"请输入正确的用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (i == 1 && y == -1) {
                    y = 1;
                    ures = editText_telNum.getText().toString();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile", editText_telNum.getText().toString());
                    final WebSerVieceUtil web = new WebSerVieceUtil("http://member.service.zhidisoft.com", "sendVer", "memberService", map);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                json = "";
                                json = web.GetStringMessage();
                                Log.e("cat", json);
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Registration_Main.this, "没有网络", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (json.contains("已经注册")) {
                                        y = -1;
                                        showDialogWindow();
                                        return;
                                    }
                                    if (json.contains("操作失败")) {
                                        y = -1;
                                        Toast.makeText(Registration_Main.this, "操作失败", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    if (json.contains("频繁")) {
                                        y = -1;
                                        Toast.makeText(Registration_Main.this, "请不要频繁进行验证", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Toast.makeText(Registration_Main.this, "操作成功", Toast.LENGTH_SHORT).show();
                                    textView_checkCode.setBackgroundResource(R.color.item_line_gray);
                                    timer = new Timer();
                                    //tiemer三个参数，进行点击事件的规划
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {      // UI thread
                                                @Override
                                                public void run() {
                                                    recLen--;
                                                    textView_checkCode.setText("" + recLen + "秒后重置");
                                                    if (recLen < 0) {
                                                        timer.cancel();
                                                        textView_checkCode.setText("获取验证码");
                                                        if (i == 1) {
                                                            textView_checkCode.setBackgroundResource(R.color.telv);
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
        setSelected();
        imageView_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelected) {
                    isSelected = false;
                    setSelected();
                } else {
                    isSelected = true;
                    setSelected();
                }
            }
        });
    }

    private void setSelected() {
        if (isSelected) {
            imageView_selected.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sq_zfcg));
        } else {
            imageView_selected.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.image_indicator));
        }
    }

    private void showDialogWindow() {
        Toast.makeText(this, "已经注册", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Registration_Main.this,
                        "" + (start - before), Toast.LENGTH_SHORT)
                        .show();
                textView_checkCode.setBackgroundResource(R.color.telv);
                i = 1;
            } else if (!((start - before) == 10)) {
                textView_checkCode.setBackgroundResource(R.color.item_line_gray);
                i = -1;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            editStart = editText_telNum.getSelectionStart();
            editEnd = editText_telNum.getSelectionEnd();
        }
    };

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
                            String code = editText_checkCode.getText().toString();
                            if (isSelected && (!code.equals("")) && (code != null)) {
                                isReady = true;
                                btn_next.setBackgroundColor(getResources().getColor(R.color.title_green));
                            } else {
                                btn_next.setBackgroundColor(getResources().getColor(R.color.font_gray));
                            }
                        }
                    });
                }
            }
        }).start();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isReady) {
                    Map<String, String> map01 = new HashMap<String, String>();
                    map01.put("mobile", editText_telNum.getText().toString());
                    map01.put("verCode", editText_checkCode.getText().toString());
                    final WebSerVieceUtil web01 = new WebSerVieceUtil("http://member.service.zhidisoft.com", "verification", "memberService", map01);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            json = "";
                            try {
                                json = web01.GetStringMessage();
                            } catch (Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Registration_Main.this,"没有网络",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (json.contains("操作成功")) {
                                        Intent intent = new Intent(Registration_Main.this, RegistionSendRequireActivity.class);
                                        intent.putExtra("userName", editText_telNum.getText().toString());
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Registration_Main.this, "验证码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}
