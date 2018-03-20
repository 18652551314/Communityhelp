package com.example.administrator.communityhelp.thirdpage.order;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/1/9.
 */
public class MyOrder {
    private String id;
    private String goodsPic;
    private String goodsName;
    private String orderTime;
    private String totolPrice;
    private String totolCount;
    private String orderStateCode;
    private String merchantGoodsId;
    private String isPayment;

    public MyOrder(String id, String goodsPic, String goodsName, String orderTime, String totolPrice, String totolCount, String orderStateCode, String merchantGoodsId, String isPayment) {
        this.id = id;
        this.goodsPic = goodsPic;
        this.goodsName = goodsName;
        this.orderTime = orderTime;
        this.totolPrice = totolPrice;
        this.totolCount = totolCount;
        this.orderStateCode = orderStateCode;
        this.merchantGoodsId = merchantGoodsId;
        this.isPayment = isPayment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTotolPrice() {
        return totolPrice;
    }

    public void setTotolPrice(String totolPrice) {
        this.totolPrice = totolPrice;
    }

    public String getTotolCount() {
        return totolCount;
    }

    public void setTotolCount(String totolCount) {
        this.totolCount = totolCount;
    }

    public String getOrderStateCode() {
        return orderStateCode;
    }

    public void setOrderStateCode(String orderStateCode) {
        this.orderStateCode = orderStateCode;
    }

    public String getMerchantGoodsId() {
        return merchantGoodsId;
    }

    public void setMerchantGoodsId(String merchantGoodsId) {
        this.merchantGoodsId = merchantGoodsId;
    }

    public String getIsPayment() {
        return isPayment;
    }

    public void setIsPayment(String isPayment) {
        this.isPayment = isPayment;
    }
}
