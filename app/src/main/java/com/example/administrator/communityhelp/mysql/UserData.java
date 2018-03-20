package com.example.administrator.communityhelp.mysql;

/**
 * Created by Administrator on 2017/2/6.
 */

public class UserData {

    /**
     * communityCode : 00001
     * userId : 402881eb4fee1ef2014fee69eaf60003
     * state : true
     * userName : 15688125398
     * isAdmin : false
     * communityName : 经八路文联社区
     * uuid : 6c0fde3b-4816-4258-8582-a128b4572f8e
     * msg : 登录成功
     */
    private String communityCode;
    private String userId;
    private boolean state;
    private String userName;
    private boolean isAdmin;
    private String communityName;
    private String uuid;
    private String msg;

    public UserData() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getCommunityCode() {
        return communityCode;
    }

    public void setCommunityCode(String communityCode) {
        this.communityCode = communityCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }



}
