package com.example.administrator.communityhelp.webserviceutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import com.yancy.imageselector.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * Created by Administrator on 2017/2/13.
 */

public class GetTempFileIdLunTan {
    static String str="";
    public static String upLoad(final File file, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccessed = false;
                //����һ��soap����
                SoapObject request = new SoapObject("http://file.service.zhidisoft.com", "uploadFile");
                //��soap�����е����Ը�ֵ
                String filecode = new String();
                String ele = file.getPath();
                if (null != ele) {
                    if (file.exists()) {
                        FileInputStream fis = null;
                        if (ele.substring(ele.length() - 3, ele.length()).equals("amr")) {
                            try {
                                fis = new FileInputStream(file);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                byte[] buffer = new byte[1024];
                                int count = 0;
                                while ((count = fis.read(buffer)) >= 0) {
                                    baos.write(buffer, 0, count);
                                }
                                //����Base64����
                                String uploadBuffer = new String(Base64.encode(baos.toByteArray()));
                                filecode = uploadBuffer;
                            } catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                //����Base64����
                                String uploadBuffer = new String(Base64.encode(getThumbUploadPath(ele, 480)));
                                filecode = uploadBuffer;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
                //����һ��envelope����
                request.addProperty("fileName", fileName);
                request.addProperty("file", filecode);
                SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                soapSerializationEnvelope.bodyOut = request;
                soapSerializationEnvelope.dotNet = false;
                //ע�����л���envelope
                (new MarshalBase64()).register(soapSerializationEnvelope);
                HttpTransportSE httpTransportSE = new HttpTransportSE("http://120.27.5.22:8080/services/uploadFileService");
                httpTransportSE.debug = true;
                try {
                    Log.e("damni", "prepare");
                    //��Ϣ������ϣ�׼����������
                    httpTransportSE.call(null, soapSerializationEnvelope);
                    //��ôӷ��������ص����ݲ���ʼ����
                    Log.e("damni", "request finish");
                    final SoapObject result = (SoapObject) soapSerializationEnvelope.bodyIn;
                    str = result.getProperty(0).toString();
//				Log.e("damni", str);
//				final SoapObject data=(SoapObject) result.getProperty(0);
                    isSuccessed = true;
                    JSONObject jsonobject = new JSONObject(str);

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }

            }
        }).start();
        while (str.equals("")){}
        return str;
    }

    public static byte[] getThumbUploadPath(String oldPath, int bitmapMaxWidth) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(oldPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight = 0;
        int reqWidth = bitmapMaxWidth;
        reqHeight = (reqWidth * height) / width;
        // ���ڴ��д���bitmap����������������Ŵ�С������
        options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(oldPath, options);
        Bitmap bm = compressImage(Bitmap.createScaledBitmap(bitmap, bitmapMaxWidth, reqHeight, false));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }


    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
            options -= 10;// ÿ�ζ�����10
            baos.reset();// ����baos�����baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
        return bitmap;
    }

}
