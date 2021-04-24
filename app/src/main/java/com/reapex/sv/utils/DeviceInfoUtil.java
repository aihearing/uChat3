package com.reapex.sv.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import com.reapex.sv.MyApp;
import com.reapex.sv.entity.DeviceInfo;

import static android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED;

public class DeviceInfoUtil extends Activity {

    private static final String TAG = "DeviceInfoUtil";
    private static DeviceInfoUtil instance;

    public DeviceInfoUtil() {

    }

    public static DeviceInfoUtil getInstance() {
        if (instance == null) {
            instance = new DeviceInfoUtil();
        }
        return instance;
    }

    /**
     * 获取手机品牌
     *
     * @return 手机品牌
     */
    public String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * 获取操作系统版本
     *
     * @return 操作系统版本
     */
    public String getOS() {
        return "Android" + Build.VERSION.RELEASE;
    }


    /**
     * 获取手机分辨率
     *
     * @param context context
     * @return 手机分辨率
     */
    public String getResolution(Context context) {
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        Log.w(TAG, "分辨率：" + screenWidth + "*" + screenHeight);
        return screenWidth + "*" + screenHeight;

    }

    /**
     * 获取运营商信息
     *
     * @param context context
     * @return 运营商信息
     */
    public String getOperator(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String simOperator = manager.getSimOperator();
        String operator;
        if (!TextUtils.isEmpty(simOperator)) {
            if (simOperator.equals("46000") || simOperator.equals("46002") || simOperator.equals("46007")) {
                // 中国移动
                operator = "中国移动";
            } else if (simOperator.equals("46003")) {
                // 中国电信
                operator = "中国电信";
            } else if (simOperator.equals("46001") || simOperator.equals("46006")) {
                // 中国联通
                operator = "中国联通";
            } else {
                // 未知
                operator = "未知";
            }
        } else {
            // 未知
            operator = "未知";
        }
        return operator;
    }

    /**
     * 判断是否联网 0 没联网 1 联网 2 mobile 3 wifi 4 2g 5 3g 6 4g
     *
     *
     * @param
     * @return
     */
    public interface NetType {
        int NET_DISCONNECTED = 0;
        int NET_CONNECTED = 1;

        int NET_MOBILE = 8;
        int NET_WIFI = 6;

        int NET_2G = 2;
        int NET_3G = 3;
        int NET_4G = 4;
        int NET_5G = 5;
        int NET_NO_KNOW = 7;
    }

    public int NetworkType(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return NetType.NET_DISCONNECTED;           //无网
        }

        NetworkCapabilities info = connManager.getNetworkCapabilities(connManager.getActiveNetwork());
        int mState = NetType.NET_DISCONNECTED;

        Network[] allNetworks = connManager.getAllNetworks();
        for (Network network : allNetworks) {
            NetworkCapabilities networkCapabilities = connManager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED))
                    mState = NetType.NET_CONNECTED;
            }
        }

        if (mState == NetType.NET_DISCONNECTED){
            return mState;
        }

        boolean mIsWifi = false;
        boolean mIsMobile = false;

        if (info != null) {
            if (info.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                mState = NetType.NET_WIFI;
            } else if (info.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                mState = NetType.NET_MOBILE;

                //1. ActivityCompat#checkSelfPermission(Context, String)}

/*                if (ActivityCompat.checkSelfPermission(.getMyContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 111);
                }else{
                    TelephonyManager mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                }
*/
//                int i = mTelephonyManager.getDataNetworkType();
                int networkType =1;
                String strNetworkType;
                switch (networkType) {
                    //  2g
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        strNetworkType = "2G";
                        break;
                    // 3g
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        strNetworkType = "3G";
                        break;

                    case TelephonyManager.NETWORK_TYPE_LTE:
                        strNetworkType = "4G";
                        break;

                    case TelephonyManager.NETWORK_TYPE_NR:
                        strNetworkType = "5G";
                        break;

                    default:
                        strNetworkType = "不详";
                        break;
                }

    }else{
                mState = NetType.NET_NO_KNOW;
            }
        }
        return mState;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 111:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //your code
                }
        }

    }


//    /**
//     * 获取唯一设备号
//     */
//    public String getDeviceNo(Context context) {
//        if (!checkReadPhoneStatePermission(context)) {
//            Log.w(TAG, "获取唯一设备号: 无权限");
//            return "";
//        }
//        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
////        @SuppressLint("HardwareIds") String szImei = TelephonyMgr.getDeviceId();
//
//        Method method = null;
//        String imei2 = "";
//        try {
//            method = TelephonyMgr.getClass().getMethod("getDeviceId", int.class);
//            String imei1 = TelephonyMgr.getDeviceId();
//            String imei0 = (String) method.invoke(TelephonyMgr, 0);
//            imei2 = (String) method.invoke(TelephonyMgr, 1);
//            String meid = (String) method.invoke(TelephonyMgr, 2);
//
//            Log.e(TAG, "唯一设备号szImei-0 is  ：" + imei0 + "  ---  imei1: " + imei1 + "  ---  imei2: " + imei2 + "   -meid is ：" + meid);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            Log.w(TAG, "唯一设备号imei-NoSuchMethodException: " + e.getMessage());
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            Log.w(TAG, "唯一设备号imei-IllegalAccessException: " + e.getMessage());
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            Log.w(TAG, "唯一设备号imei-InvocationTargetException: " + e.getMessage());
//        }
//        return imei2;
//    }
//


    /**
     * 获取联网方式
     *
    public String getNetMode(Context context) {
        String strNetworkType = "未知";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return "No NetWork";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NetworkCapabilities cap = manager.getNetworkCapabilities(manager.getActiveNetwork());
            if (cap == null) return "no";
            if (cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)){
                return "yes";
            }else{
                return "No";
            }
        } else{
            Network[] networks = manager.getAllNetworks();
            for (Network n: networks) {
                NetworkInfo nInfo = manager.getNetworkInfo(n);
                if (nInfo != null && nInfo.isConnected()) return "yes";
            }

            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                int netMode = networkInfo.getType();
                if (netMode == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "WIFI";
                    //wifi
                } else if (netMode == ConnectivityManager.TYPE_MOBILE) {
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        //  2g
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            strNetworkType = "2G";
                            break;
                        // 3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            strNetworkType = "3G";
                            break;

                        case TelephonyManager.NETWORK_TYPE_LTE:
                            strNetworkType = "4G";
                            break;

                        case TelephonyManager.NETWORK_TYPE_NR:
                            strNetworkType = "5G";
                            break;

                        default:
                            strNetworkType = "不详";
                            break;
                    }
                }
            }
            Log.w(TAG, "联网方式:" + strNetworkType);
            return strNetworkType;
        }

//    private boolean checkReadPhoneStatePermission(Context context) {
//        try {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE},
//                        10);
//                return false;
//            }
//        } catch (IllegalArgumentException e) {
//            return false;
//        }
//        return true;
//    }
//
//
//    public String getMEID(Context context) {
//        if (!checkReadPhoneStatePermission(context)) {
//            Log.w(TAG, "获取唯一设备号-getMEID: 无权限");
//            return "";
//        }
//        String meid = "";
//        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (null != mTelephonyMgr) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                meid = mTelephonyMgr.getMeid();
//                Log.i(TAG, "Android版本大于o-26-优化后的获取---meid:" + meid);
//            } else {
//                meid = mTelephonyMgr.getDeviceId();
//            }
//        }
//
//        Log.i(TAG, "优化后的获取---meid:" + meid);
//
//        return meid;
//    }
//
//    public String getIMEI(Context context) {
//        if (!checkReadPhoneStatePermission(context)) {
//            Log.w(TAG, "获取唯一设备号-getIMEI: 无权限");
//            return "";
//        }
//        String imei1 = "";
//        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (null != mTelephonyMgr) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                imei1 = mTelephonyMgr.getImei(0);
//                Log.i(TAG, "Android版本大于o-26-优化后的获取---imei-1:" + imei1);
//            } else {
//                try {
//                    imei1 = getDoubleImei(mTelephonyMgr, "getDeviceIdGemini", 0);
//                } catch (Exception e) {
//                    try {
//                        imei1 = getDoubleImei(mTelephonyMgr, "getDeviceId", 0);
//                    } catch (Exception e1) {
//                        e1.printStackTrace();
//                    }
//                    Log.e(TAG, "get device id fail: " + e.toString());
//                }
//            }
//        }
//
//        Log.i(TAG, "优化后的获取---imei1：" + imei1);
//        return imei1;
//    }
//
//    public String getIMEI2(Context context) {
//        if (!checkReadPhoneStatePermission(context)) {
//            Log.w(TAG, "获取唯一设备号-getIMEI2: 无权限");
//            return "";
//        }
//        String imei2 = "";
//        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        if (null != mTelephonyMgr) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                imei2 = mTelephonyMgr.getImei(1);
//                mTelephonyMgr.getMeid();
//                Log.i(TAG, "Android版本大于o-26-优化后的获取---imei-2:" + imei2);
//            } else {
//                try {
//                    imei2 = getDoubleImei(mTelephonyMgr, "getDeviceIdGemini", 1);
//                } catch (Exception e) {
//                    try {
//                        imei2 = getDoubleImei(mTelephonyMgr, "getDeviceId", 1);
//                    } catch (Exception ex) {
//                        Log.e(TAG, "get device id fail: " + e.toString());
//                    }
//                }
//            }
//        }
//
//        Log.i(TAG, "优化后的获取--- imei2:" + imei2);
//        return imei2;
//    }
//
//    /**
//     * 获取双卡手机的imei
//     */
//    private String getDoubleImei(TelephonyManager telephony, String predictedMethodName, int slotID) throws Exception {
//        String inumeric = null;
//
//        Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
//        Class<?>[] parameter = new Class[1];
//        parameter[0] = int.class;
//        Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
//        Object[] obParameter = new Object[1];
//        obParameter[0] = slotID;
//        Object ob_phone = getSimID.invoke(telephony, obParameter);
//        if (ob_phone != null) {
//            inumeric = ob_phone.toString();
//        }
//        return inumeric;
//    }

    /**
     * 获取设备信息
     *
     * @param context context
     * @return 设备信息
     */
    public DeviceInfo getDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setPhoneBrand(getPhoneBrand());
        deviceInfo.setPhoneModel(getPhoneModel());
        deviceInfo.setOs(getOS());
        deviceInfo.setResolution(getResolution(context));
        deviceInfo.setOperator(getOperator(context));
        return deviceInfo;
    }
}
