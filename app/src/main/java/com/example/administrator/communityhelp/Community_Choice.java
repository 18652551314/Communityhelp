package com.example.administrator.communityhelp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.mysql.SqlHelper;
import com.example.administrator.communityhelp.thirdpage.about_us.WebSerVieceUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Community_Choice extends BaseActivity {
    String json = "";
    SqlHelper helper;
    public SQLiteDatabase dbweiter;
    public SQLiteDatabase dbReader;
    String selected;
    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6, linearLayout7;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    List<ImageView> images = new ArrayList<>();
    List<TextView> texts = new ArrayList<>();
    Button btn_selected;
    Intent intent_aa;
    String tiaojian="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community__choice);
        intent_aa=getIntent();

        tiaojian=intent_aa.getStringExtra("aa");
        linearLayout1 = (LinearLayout) findViewById(R.id.jing);
        linearLayout2 = (LinearLayout) findViewById(R.id.da);
        linearLayout3 = (LinearLayout) findViewById(R.id.dashi);
        linearLayout4 = (LinearLayout) findViewById(R.id.dashiqiao);
        linearLayout5 = (LinearLayout) findViewById(R.id.zhongheng);
        linearLayout6 = (LinearLayout) findViewById(R.id.jingba);
        linearLayout7 = (LinearLayout) findViewById(R.id.jingbalu);
        imageView1 = (ImageView) findViewById(R.id.iv_jing);
        imageView2 = (ImageView) findViewById(R.id.iv_da);
        imageView3 = (ImageView) findViewById(R.id.iv_dashi);
        imageView4 = (ImageView) findViewById(R.id.iv_dashiqiao);
        imageView5 = (ImageView) findViewById(R.id.iv_zhongheng);
        imageView6 = (ImageView) findViewById(R.id.iv_jingba);
        imageView7 = (ImageView) findViewById(R.id.iv_jingbalu);
        images.add(imageView1);
        images.add(imageView2);
        images.add(imageView3);
        images.add(imageView4);
        images.add(imageView5);
        images.add(imageView6);
        images.add(imageView7);
        textView1 = (TextView) findViewById(R.id.textView_community_01);
        textView2 = (TextView) findViewById(R.id.textView_community_02);
        textView3 = (TextView) findViewById(R.id.textView_community_03);
        textView4 = (TextView) findViewById(R.id.textView_community_04);
        textView5 = (TextView) findViewById(R.id.textView_community_05);
        textView6 = (TextView) findViewById(R.id.textView_community_06);
        textView7 = (TextView) findViewById(R.id.textView_community_07);
        texts.add(textView1);
        texts.add(textView2);
        texts.add(textView3);
        texts.add(textView4);
        texts.add(textView5);
        texts.add(textView6);
        texts.add(textView7);
        helper = new SqlHelper(this);
        dbReader = helper.getReadableDatabase();
        dbweiter = helper.getWritableDatabase();
        //获取小区列表存入数据库
        getCommunityList();
        //获取当前选择的小区
        checkSelected();
        btn_selected = (Button) findViewById(R.id.btn_decide_community_select);
        if (tiaojian!=null) {
            if (tiaojian.equals("aa")) {
                btn_selected.setText("提交关注");
            }
        }
        btn_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                dbweiter.delete("community", null, null);
                values.put("communityName", selected);
                //根据选择的社区名称在下载的数据库communityList中查找code
                String communityCode = null;
                String select = "select * from communityList where communityName ='" + selected + "'";
                Cursor cursor = dbReader.rawQuery(select, null);
                while (cursor.moveToNext()) {
                    communityCode = cursor.getString(cursor.getColumnIndex("communityCode"));
                }
                values.put("communityCode", communityCode);
                dbweiter.insert("community", null, values);
                if (isLoad()) {
                    //登录状态下修改选择的小区
                    changeCommunity(selected, communityCode);
                } else {
                    Intent intent = new Intent(Community_Choice.this,FirstMajor.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //刚进入界面时候选择的社区
        setSelected();
    }

    public void checkSelected() {
        for (TextView image :
                texts) {
            if (isLoad()) {
                selected = getUserData().getCommunityName();
                setColorte();
                for (int i = 0; i < 7; i++) {
                    if (texts.get(i).getText().equals(selected)) {
                        images.get(i).setBackgroundResource(R.mipmap.fzg);
                    }
                }
            } else if (hasData() != null && (!hasData().equals(""))) {
                selected = hasData();
                setColorte();
                for (int i = 0; i < 7; i++) {
                    if (texts.get(i).getText().equals(selected)) {
                        images.get(i).setBackgroundResource(R.mipmap.fzg);
                    }
                }
            } else {
                setColorte();
            }
        }
    }

    private void changeCommunity(final String name, String code) {
        Cursor cursor = dbReader.query("community", null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            code = cursor.getString(cursor.getColumnIndex("communityCode"));
        }
        Map<String, String> map = new HashMap<>();
        map.put("userId", getUserData().getUserId());
        map.put("communityCode", code);
        final WebSerVieceUtil web = new WebSerVieceUtil("http://community.service.zhidisoft.com", "saveCommunity", "communityService", map);
        final String finalCode = code;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String s = web.GetStringMessage();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("dog",s);
                            ContentValues values=new ContentValues();
                            values.put("userId",getUserData().getUserId());
                            values.put("communityName",name);
                            values.put("communityCode", finalCode);
                            dbweiter.update("user",values,"userId=?",new String[]{getUserData().getUserId()});
                            finish();
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(Community_Choice.this, "修改失败", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

    public void onClick_jing(View v) {
        setColorte();
        selected = textView1.getText().toString();
        imageView1.setBackgroundResource(R.mipmap.fzg);
    }


    public void onClick_da(View v) {
        setColorte();
        selected = textView2.getText().toString();
        imageView2.setBackgroundResource(R.mipmap.fzg);

    }

    public void onClick_dashi(View v) {
        setColorte();
        selected = textView3.getText().toString();
        imageView3.setBackgroundResource(R.mipmap.fzg);
    }

    public void onClick_dashiqiao(View v) {
        setColorte();
        selected = textView4.getText().toString();
        imageView4.setBackgroundResource(R.mipmap.fzg);
    }

    public void onClick_zhongheng(View v) {
        setColorte();
        selected = textView5.getText().toString();
        imageView5.setBackgroundResource(R.mipmap.fzg);
    }

    public void onClick_jingba(View v) {
        setColorte();
        selected = textView6.getText().toString();
        imageView6.setBackgroundResource(R.mipmap.fzg);
    }

    public void onClick_jingbalu(View v) {
        setColorte();
        selected = textView7.getText().toString();
        imageView7.setBackgroundResource(R.mipmap.fzg);
    }

    private void setColorte() {
        imageView1.setBackgroundResource(R.mipmap.fzf);
        imageView2.setBackgroundResource(R.mipmap.fzf);
        imageView3.setBackgroundResource(R.mipmap.fzf);
        imageView4.setBackgroundResource(R.mipmap.fzf);
        imageView5.setBackgroundResource(R.mipmap.fzf);
        imageView6.setBackgroundResource(R.mipmap.fzf);
        imageView7.setBackgroundResource(R.mipmap.fzf);

    }

    @Override
    protected void onStart() {
        super.onStart();
        setSelected();
    }

    private void setSelected() {
        if (isLoad()) {
            String comName = getUserData().getCommunityName();
            selected = comName;
            for (int i = 0; i < 7; i++) {
                String name = texts.get(i).getText().toString();
                if (comName.equals(name)) {
                    setColorte();
                    images.get(i).setBackgroundResource(R.mipmap.fzg);
                }
            }
        } else if (hasData() != null && (!hasData().equals(""))) {
            selected = hasData();
        }
    }

    private String hasData() {
        String name = "";
        Cursor cursor = dbReader.query("community", null, null, null, null, null, null, null);
        try {
            if (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("communityName"));
            }
        } catch (Exception e) {
            return name;
        }
        if (name == null) {
            return name;
        }
        if (isLoad()) {
            name = getUserData().getCommunityName();
        }
        return name;
    }

    public void getCommunityList() {
        Map map = new HashMap<>();
        map.put("pageNo", 1);
        map.put("pageSize", 10);
        map.put("condition", "");
        final WebSerVieceUtil web = new WebSerVieceUtil("http://community.service.zhidisoft.com", "communityList", "communityService", map);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    json = web.GetStringMessage();
                    Gson gson = new Gson();
                    Community_Choice.community comm = gson.fromJson(json, Community_Choice.community.class);
                    for (int i = 0; i < comm.getRows().size(); i++) {
                        String name = comm.getRows().get(i).getName();
                        String code = comm.getRows().get(i).getCode();
                        ContentValues values = new ContentValues();
                        values.put("communityName", name);
                        values.put("communityCode", code);
                        dbweiter.insert("communityList", null, values);
//                        Log.e("dog", name + "" + code);
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Community_Choice.this, "没有网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
            }
        }).start();
    }

    static class community {
        /**
         * total : 7
         * pageNo : 1
         * pageSize : 100
         * rows : [{"firstLetter":"J","name":"经八路文联社区","code":"00001"},{"firstLetter":"D","name":"大石桥城中央社区","code":"00007"}]
         */
        private int total;
        private int pageNo;
        private int pageSize;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * firstLetter : J
             * name : 经八路文联社区
             * code : 00001
             */
            private String firstLetter;
            private String name;
            private String code;

            public String getFirstLetter() {
                return firstLetter;
            }

            public void setFirstLetter(String firstLetter) {
                this.firstLetter = firstLetter;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }
        }
    }
}
