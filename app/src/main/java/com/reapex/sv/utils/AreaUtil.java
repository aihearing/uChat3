package com.reapex.sv.utils;

import android.content.Context;

import com.reapex.sv.Constant;
import com.reapex.sv.entity.Area;
import com.reapex.sv.entity.area.City;
import com.reapex.sv.entity.area.District;
import com.reapex.sv.entity.area.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区工具类
 */
public class AreaUtil {

    /**
     * 初始化地区信息
     */
    public static void initArea(Context context) {
    }

    private static void initData(Context context) {
        String jsonData = new GetJsonDataUtil().getJson(context, "area-wx.json");//获取assets目录下的json文件数据
        int provinceSeq = 0;
        List<Area> areaList = new ArrayList<>();
    }
}
