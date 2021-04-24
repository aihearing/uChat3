package com.reapex.sv;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MySP {

    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    private Object object;
    public static MySP mySP;

    public static MySP getInstance() {
        if (mySP == null) {
            synchronized (MySP.class) {
                if (mySP == null) {
                    // 使用双重同步锁
                    mySP = new MySP();
                }
            }
        }
        return mySP;
    }

    public void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    /**
     * 问题在于，这个Context哪来的我们不能确定，很大的可能性，你在某个Activity里面为了方便，直接传了个this;
     * 这样问题就来了，我们的这个类中的sInstance是一个static且强引用的，在其内部引用了一个Activity作为Context，也就是说，
     * 我们的这个Activity只要我们的项目活着，就没有办法进行内存回收。而我们的Activity的生命周期肯定没这么长，造成了内存泄漏。
     * 所以这里使用context.getApplicationContext()
     */
    private MySP() {

    }

    /**
     * 保存数据 , 所有的类型都适用
     *
     */
    public synchronized void saveParam(String key, Object object) {
        if (editor == null)
            editor = preferences.edit();
        // 得到object的类型
        String type = object.getClass().getSimpleName();
        if ("String".equals(type)) {
            // 保存String 类型
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            // 保存integer 类型
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            // 保存 boolean 类型
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            // 保存float类型
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            // 保存long类型
            editor.putLong(key, (Long) object);
        } else {
            if (!(object instanceof Serializable)) {
                throw new IllegalArgumentException(object.getClass().getName() + " 必须实现Serializable接口!");
            }

            // 不是基本类型则是保存对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(object);
                String productBase64 = Base64.encodeToString(
                        baos.toByteArray(), Base64.DEFAULT);
                editor.putString(key, productBase64);
                Log.d(this.getClass().getSimpleName(), "save object success");
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(this.getClass().getSimpleName(), "save object error");
            }
        }
        editor.apply();
    }

    /**
     * 移除信息
     */
    public synchronized void remove(String key) {
        if (editor == null)
            editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }


    /**
     * 得到保存数据的方法，所有类型都适用
     *
     */
    public Object getParam(String key, Object defaultObject) {
        if (defaultObject == null) {
            return getObject(key);
        }

        String type = defaultObject.getClass().getSimpleName();

        if ("String".equals(type)) {
            return preferences.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return preferences.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return preferences.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return preferences.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return preferences.getLong(key, (Long) defaultObject);
        }
        return getObject(key);
    }

    public void setCurrentTime(long currentTime) {    saveParam("CurrentTime", currentTime);    }
    public long getCurrentTime() {return (long)  getParam("CurrentTime", "");    }

// user in String

    public void   setUPhone(String uPhone) {    saveParam("uPhone", uPhone);    }
    public String getUPhone() {return (String)  getParam("uPhone", "");    }

    public void   setUPassword(String uPassword) {    saveParam("uPassword", uPassword);    }
    public String getUPassword() {return (String)  getParam("uPassword", "");    }

    public void   setUAvatar(String uAvatar) {    saveParam("uAvatar", uAvatar);    }
    public String getUAvatar() {return (String)  getParam("uAvatar", "");    }

    public void   setUWxId(String uWxId) {    saveParam("uWxId", uWxId);    }
    public String getUWxId() {return (String)  getParam("uWxId", "");    }

    public void   setUNickName(String uNickName) {    saveParam("uNickName", uNickName);    }
    public String getUNickName() {return (String)  getParam("uNickName", "");    }

    public void   setUSignature(String uSignature) {    saveParam("uSignature", uSignature);    }
    public String getUSignature() {return (String)  getParam("uSignature", "");    }
    public void   setURegion(String uRegion) {    saveParam("uRegion", uRegion);    }
    public String getURegion() {return (String)  getParam("uRegion", "");    }
    public void   setUGender(String uGender) {    saveParam("uGender", uGender);    }
    public String getUGender() {return (String)  getParam("uGender", "");    }
    /**
     * Whether to use for the first time
     *
     * return
     */
    public boolean isFirst() {
        return (Boolean) getParam("isFirst", true);
    }

    /**
     * set user first use is false
     *
     * return
     */
    public void setFirst(Boolean isFirst) {
        saveParam("isFirst", isFirst);
    }

    /**
     * Set up the first time login
     *
     * return
     */
    public boolean isLogin() {
        return (Boolean) getParam("isLogin", false);
    }

    /**
     * return
     */
    public void setLogin(Boolean isLogin) {
        saveParam("isLogin", isLogin);
    }

    public void setNewMsgsUnreadNumber(int newMsgsUnreadNumber) {
        saveParam("newMsgsUnreadNumber", newMsgsUnreadNumber);
    }


    public void setNewFriendsUnreadNumber(int newFriendsUnreadNumber) {
        saveParam("newFriendsUnreadNumber", newFriendsUnreadNumber);
    }

    public Integer getNewFriendsUnreadNumber() {
        return (Integer) getParam("newFriendsUnreadNumber", 0);
    }

    public Object getObject(String key) {
        String wordBase64 = preferences.getString(key, "");
        byte[] base64 = Base64.decode(wordBase64.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            ObjectInputStream bis = new ObjectInputStream(bais);
            object = bis.readObject();
            Log.d(this.getClass().getSimpleName(), "Get object success");
            return object;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.toString());
        }
        Log.e(this.getClass().getSimpleName(), "Get object is error");
        return null;
    }

    public void setDisclaimer(String Disclaimer) {saveParam("Disclaimer", Disclaimer);    }
    public String getDisclaimer() {
        return (String) getParam("Disclaimer", "");
    }

    public void setWelcome(String welcome) {saveParam("Welcome", welcome);    }
    public String getWelcome() {
        return (String) getParam("Welcome", "");
    }

    public void setPickedCity(String cityName) {
        saveParam("pickedCity", cityName);
    }

    public String getPickedCity() {
        return (String) getParam("pickedCity", "");
    }

    public void setPickedDistrict(String districtName) {
        saveParam("pickedDistrict", districtName);
    }

    public String getPickedDistrict() {
        return (String) getParam("pickedDistrict", "");
    }

    public void setPickedPostCode(String postCode) {
        saveParam("pickedPostCode", postCode);
    }
    public String getPickedPostCode() {
        return (String) getParam("pickedPostCode", "");
    }

    /**
     * 是否开启"附近的人"
     *
     * return true:是  false:否
     */
    public boolean isOpenPeopleNearby() {
        return (Boolean) getParam("isOpenPeopleNearby", false);
    }

    /**
     * 设置是否开启附近的人
     *
     * @param isOpenPeopleNearby 是否开启附近的人
     */
    public void setOpenPeopleNearby(Boolean isOpenPeopleNearby) {
        saveParam("isOpenPeopleNearby", isOpenPeopleNearby);
    }

}
