package com.example.administrator.communityhelp.thirdpage.about_us;

import android.graphics.Bitmap;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/1/11.
 */
public class WebSerVieceUtil {
    public String NameSpace;  //命名空间
    public String MethodName;//方法名
    public String URL = "http://120.27.5.22:8080/services/";//地址
    public String resultStr = "";
    public Bitmap resultBitmap = null;
    //获取SoapObject 对象
    public SoapObject mSoapObject;

    public WebSerVieceUtil(String nameSpace, String methodName, String newURL, Map<String, String> map) {
        NameSpace = nameSpace;
        MethodName = methodName;
        URL = URL + newURL;
        mSoapObject = new SoapObject(NameSpace, MethodName);
        Set<String> set = map.keySet();
        for (String s :
                set) {
            mSoapObject.addProperty(s, map.get(s));
        }
    }

    public String GetStringMessage() {

        //获取HttpTransportSE
        HttpTransportSE mHttpTransportSE = new HttpTransportSE(URL);
        //允许Debg
        mHttpTransportSE.debug = true;
        //指定版本号
        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        envelope.bodyOut = mSoapObject;
        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        try {
            //做耗时操作，没有办法设置连接超时，如果网不好
            mHttpTransportSE.call(null, envelope);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        // 获取返回的数据  对象
        // 获取返回的结果
        try {
            SoapObject object = (SoapObject) envelope.bodyIn;
            resultStr = object.getProperty(0).toString();
        } catch (Exception e) {
            Object object = null;
            object = envelope.bodyIn;
//            SoapObject  obj= (SoapObject) object;
            resultStr = object.toString();
            Log.e("object",object.toString());
        }
        return resultStr;
    }


}
