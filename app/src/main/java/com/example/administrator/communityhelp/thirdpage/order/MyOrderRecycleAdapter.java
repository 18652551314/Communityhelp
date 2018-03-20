package com.example.administrator.communityhelp.thirdpage.order;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.communityhelp.R;
import com.example.administrator.communityhelp.firstpage.taoshihui.AddShouHuoDiZhiActivity;
import com.example.administrator.communityhelp.firstpage.taoshihui.TaoShiHui_SurePayActivity;
import com.example.administrator.communityhelp.my_other.GlideLoader;
import com.example.administrator.communityhelp.myholder.RecycleViewHolder;
import com.example.administrator.communityhelp.thirdpage.leaving_message.MyMessage;
import com.example.administrator.communityhelp.thirdpage.leaving_message.MyMessageRecycleHolder;
import com.example.administrator.communityhelp.webserviceutils.ProgressDialogUtils;
import com.example.administrator.communityhelp.webserviceutils.WebServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 */
public class MyOrderRecycleAdapter extends RecyclerView.Adapter<MyOrderRecycleHolder> {
    List<MyOrder> list;
    Context context;
    Dialog dialog;
    TextView textView_dialog_message;

    public static final String NameSpace = "http://goods.service.zhidisoft.com";  //命名空间
    public static final String MethodName = "removeOrder";//方法名
    public static final String URL = "http://120.27.5.22:8080/services/goodsOrderService";//地址

    public MyOrderRecycleAdapter(Context context, List<MyOrder> list) {
        this.context = context;
        this.list = list;
    }

    public void AddAll(List list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public MyOrderRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyOrderRecycleHolder holder = new MyOrderRecycleHolder(LayoutInflater.from(context).inflate(R.layout.recycleview_myorder_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyOrderRecycleHolder holder, final int position) {
        final MyOrder order = list.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("id", list.get(position).getId());
                intent.putExtra("orderStateCode", list.get(position).getOrderStateCode());
                context.startActivity(intent);
            }
        });

        if (!isNull(order.getGoodsPic())) {
            new GlideLoader().displayImage(context, order.getGoodsPic(), holder.imageViewGoodsPic);
        }
        if (!isNull(order.getGoodsName())) {
            holder.textViewGoodsName.setText(order.getGoodsName());

        }
        if (!isNull(order.getOrderTime())) {
            holder.textViewOrderTime.setText(order.getOrderTime());

        }
        if (!isNull(order.getTotolPrice())) {
            holder.textViewTotalCost.setText("总价: " + order.getTotolPrice() + "元");

        }
        if (!isNull(order.getTotolCount())) {
            holder.textViewTotalCount.setText("数量:" + order.getTotolCount());

        }
        if (order.getOrderStateCode().equals("consumption")) {
            holder.btnPay.setVisibility(View.GONE);
        }

        if (order.getOrderStateCode().equals("evaluation")) {
            holder.btnDelete.setVisibility(View.GONE);
        }

        if (!isNull(holder.btnDelete)) {
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(context, R.layout.dialog_view, null);
                    view.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", order.getId());
                            Log.e("id", order.getId());
                            WebServiceUtils.callWebService(URL, MethodName, NameSpace, map, new WebServiceUtils.WebServiceCallBack() {
                                @Override
                                public void callBack(SoapObject result) {
                                    ProgressDialogUtils.dismissProgressDialog();
                                    if (result != null) {
                                        //获取json数据
                                        String json = result.getProperty(0).toString();
                                        //解析json数据并放入list集合中
                                        Log.e("json", "json: " + json);
                                        try {
                                            JSONArray array = new JSONArray("[" + json + "]");
                                            for (int i = 0; i < array.length(); i++) {
                                                JSONObject jo = array.getJSONObject(i);
                                                String results = jo.getString("result");
                                                if (results.equals("true")) {
                                                    Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    View view = View.inflate(context, R.layout.toastview, null);
                                                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                                    toastText.setText("删除成功");
                                                    toast.setView(view);
                                                    toast.show();
                                                    Log.e("position11", position+"");
                                                    onItemDismiss(position);
                                                } else {
                                                    Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    View view = View.inflate(context, R.layout.toastview, null);
                                                    TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                                    toastText.setText("删除失败");
                                                    toast.setView(view);
                                                    toast.show();
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        View view = View.inflate(context, R.layout.toastview, null);
                                        TextView toastText = (TextView) view.findViewById(R.id.toast_text);
                                        toastText.setText("无法获取网络数据，请检查网络连接状态！");
                                        toast.setView(view);
                                        toast.show();
                                    }
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    view.getBackground().setAlpha(150);
                    textView_dialog_message = (TextView) view.findViewById(R.id.textView_dialog_message);
                    textView_dialog_message.setText("确定要删除订单吗？");
                    dialog = new AlertDialog.Builder(context)
                            .setView(view)
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == event.KEYCODE_BACK) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    dialog.show();
                }
            });
        }
        if (!isNull(holder.btnPay)) {
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TaoShiHui_SurePayActivity.class);
                    intent.putExtra("id",order.getMerchantGoodsId());
                    intent.putExtra("name",order.getGoodsName());
                    DecimalFormat decimalFormat = new DecimalFormat("###################.###########");
                    Double price=Double.parseDouble(order.getTotolPrice())/Double.parseDouble(order.getTotolCount());
                    intent.putExtra("price",decimalFormat.format(price));
                    intent.putExtra("totalPrice",order.getTotolPrice());
                    intent.putExtra("buyNum",order.getTotolCount());
                    intent.putExtra("orderId",order.getId());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isNull(Object object) {
        return object == null;
    }

    //删除条目
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());
    }

}
