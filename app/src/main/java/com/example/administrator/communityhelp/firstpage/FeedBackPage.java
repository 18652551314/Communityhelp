package com.example.administrator.communityhelp.firstpage;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.BaseActivity;
import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/4.
 */
public class FeedBackPage extends BaseActivity {
    private EditText feedBackText;
    private Button commit;
    private TextView textView;
    public static final String URL = "http://120.27.5.22:8080/services/problemService";
    public static final String NameSpace = "http://problem.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "problemFeedBackSave";//方法名

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.feedbackpage);
        textView= (TextView) findViewById(R.id.textview_back_linli);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feedBackText= (EditText) findViewById(R.id.pingtailiuyan_editext);
        commit= (Button) findViewById(R.id.pingtailiuyan_but);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(feedBackText.getText().toString().trim())){
                    Log.e("tag",feedBackText.getText().toString());
                    ProgressDialogUtils.showProgressDialog(FeedBackPage.this,"玩命处理中");
                    HashMap<String,Object> map=new HashMap<String, Object>();
                    map.put("content",feedBackText.getText().toString());
                    map.put("ptinfoType","");
                    map.put("communityCode",getUserData().getCommunityCode());
                    map.put("tempFileIds","");
                    map.put("userId",getUserData().getUserId());
                    WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
                        @Override
                        public void callBack(SoapObject result) {
                            ProgressDialogUtils.dismissProgressDialog();
                            if(result!=null){
                                String json=result.getProperty(0).toString();
                                feedBackText.setText("");
                                Toast.makeText(FeedBackPage.this,"提交成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast toast=Toast.makeText(FeedBackPage.this,"",Toast.LENGTH_SHORT);
                    View view=getLayoutInflater().inflate(R.layout.toastview,null);
                    TextView text= (TextView) view.findViewById(R.id.toast_text);
                    text.setText("请输入留言内容");
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.setView(view);
                    toast.show();
                }
            }
        });
    }
}

