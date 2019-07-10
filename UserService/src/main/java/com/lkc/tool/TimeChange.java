package com.lkc.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeChange {

    /**
     * 将influxdb中的时间格式转换成正常的时间格式，参数String类型
     * 比如timestamp格式时间："2018-01-05T11:03:05Z534566465"
     * 转换后的时间："2018-01-05 11:03:05"
     *
     * @param timestamp 参数格式timestamp
     * @return
     */
    public static String dbtimeTonNormal(String timestamp) {
        return timestamp.substring(0, 19).replace("T", " ");
    }

    /**
     * 将timestamp格式转换成正常的时间格式，参数String类型
     * 比如timestamp格式时间："2018-01-05T03:03:05Z"
     * 转换后的DateString时间："2018-01-05 11:03:05"
     *
     * @param timestamp 参数格式timestamp
     * @return
     */
    public static String timestampToDateString(String timestamp) {
        //将时间转换成"2018-01-05T03:03:05Z"格式
        timestamp = timestamp.substring(0, 20).replace(".", "Z");
        String resultDate = "";
        try {
            SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            Object obj = sdf.parse(timestamp).getTime();
            String ooo = obj.toString();
            long stimes = Long.parseLong(ooo);

            Date da = new Date(stimes);
            resultDate = sim.format(da.getTime());
        } catch (Exception e1) {
            return "";
        }
        return resultDate;
    }

    /**
     * 将现在的正常字符串格式时间转换成距离1970的数字时间
     * 比如字符串格式时间："2017-12-15 21:49:03"
     * 转换后的数字时间："1513345743"
     *
     * @param time
     * @return
     */
    //没用到
    public static Long timeToSecond(String time) {
        //将时间转换成"2017-12-15 21:49:03"格式
        time = time.substring(0, 20).replace(".", "").replace("T", " ");
        String dateStr = "1970-1-1 08:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long aftertime = 0;
        try {
            Object d1 = sdf.parse(time).getTime();
            Date miDate = sdf.parse(dateStr);
            Object t1 = miDate.getTime();
            long d1time = Long.parseLong(d1.toString()) / 1000;
            long t1time = Long.parseLong(t1.toString()) / 1000;
            aftertime = d1time - t1time;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return aftertime;
    }

    /**
     * 将距离1970年的数字时间转换成正常的字符串格式时间；
     * 比如数字时间："1513345743"
     * 转换后："2017-12-15 21:49:03"
     *
     * @param time
     * @return
     */
    public static String secondToTime(String time) {
        String dateStr = "1970-1-1 08:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (time.equals("0")) {
            return "";
        }
        Date miDate;
        String returnstr = "";
        try {
            miDate = sdf.parse(dateStr);
            Object t1 = miDate.getTime();
            long h1 = Long.parseLong(time.toString()) * 1000 + Long.parseLong(t1.toString());
            returnstr = sdf.format(h1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnstr;
    }
}
