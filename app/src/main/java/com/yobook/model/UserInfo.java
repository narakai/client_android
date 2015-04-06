package com.yobook.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class UserInfo {
    //用户唯一ID
    String mUid;
    //用户名
    String mUserName;

    //用户登录类型
    int mLoginType = 0;
    //用户登录平台ID
    String mPlatformUid;
    //用户登录平台令牌
    String mAccessToken;

    double mLatitude = 0.0;
    double mLongitude = 0.0;



    public static final int LOGIN_TYPE_QQ = 1;
    public static final String[] loginType = new String[]{"Default", "QQ"};

    public void initUserInfo(int loginType, String platformId, String token) {
        mLoginType = loginType;
        mPlatformUid = platformId;
        mAccessToken = token;
    }

    public void updateCurrentPosition(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public String toJSONString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", "Anonymous");
            obj.put("from", loginType[mLoginType]);
            obj.put("open_id", mPlatformUid);
            obj.put("access_token", mAccessToken);
            obj.put("location", new JSONArray() {}.put(mLatitude).put(mLongitude));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj.toString();
    }
}
