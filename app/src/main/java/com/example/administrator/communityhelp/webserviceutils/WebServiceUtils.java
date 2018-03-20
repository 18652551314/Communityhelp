package com.example.administrator.communityhelp.webserviceutils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 访问WebService的工具类,
 */
public class WebServiceUtils {
    // 含有3个线程的线程池
    private static final ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * @param url                WebService服务器地址
     * @param methodName         WebService的调用方法名
     * @param nameSpace          WebService的命名空间
     * @param properties         WebService的参数
     * @param webServiceCallBack 回调接口
     */
    public static void callWebService(String url, final String methodName, String nameSpace, HashMap<String, Object> properties,
                                      final WebServiceCallBack webServiceCallBack) {
        // 创建HttpTransportSE对象，传递WebService服务器地址
        final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
        // 创建SoapObject对象
        SoapObject soapObject = new SoapObject(nameSpace, methodName);

        // SoapObject添加参数
        if (properties != null) {
            Iterator<Map.Entry<String, Object>> it = properties.entrySet().iterator();
//			for (; it.hasNext();) {
//				Map.Entry<String, String> entry = it.next();
//				soapObject.addProperty(entry.getKey(), entry.getValue());
//			}
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                soapObject.addProperty(entry.getKey(), entry.getValue());
            }
        }

        // 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
        final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
        // 设置是否调用的是.Net开发的WebService
        //soapEnvelope.setOutputSoapObject(soapObject);
        soapEnvelope.bodyOut = soapObject;
        soapEnvelope.dotNet = true;
        httpTransportSE.debug = true;

        // 用于子线程与主线程通信的Handler
        final Handler mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 将返回值回调到callBack的参数中
                webServiceCallBack.callBack((SoapObject) msg.obj);
            }

        };

        // 开启线程去访问WebService
        executorService.submit(new Runnable() {

            @Override
            public void run() {
                SoapObject resultSoapObject = null;
                try {
                    httpTransportSE.call(null, soapEnvelope);
                    if (soapEnvelope.getResponse() != null) {
                        // 获取服务器响应返回的SoapObject
                        resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } finally {
                    // 将获取的消息利用Handler发送到主线程
//					mHandler.sendMessage(mHandler.obtainMessage(0, resultSoapObject));

                    Message mess = mHandler.obtainMessage(0, resultSoapObject);
                    mHandler.sendMessage(mess);
                }
            }
        });
    }

    /**
     * @author xiaanming
     */
    public interface WebServiceCallBack {
        public void callBack(SoapObject result);
    }

}