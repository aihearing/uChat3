package com.reapex.sv.utils;

import android.content.Context;

import com.reapex.sv.entity.Region;

import java.util.List;

/**
 * 地区工具类
 *
 * @author zhou
 */
public class RegionUtil {

    /**
     * 初始化地区信息
     */
    public static void initRegion(Context context) {
    }

    private static void initData(Context context) {
        // 获取assets目录下的json文件数据
        String jsonData = new GetJsonDataUtil().getJson(context, "region-wx.json");
    }
}
