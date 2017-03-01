package com.lsh.library;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BankInfo {

    //该方法用于打开assets中的binNum文档资源，获得里面的binNum数据
    private static String openBinNum(Context context) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String str = null;
        try {
            InputStream is = context.getResources().getAssets().open("binNum.txt");
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            is.close();
            outputStream.close();
            str = outputStream.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    //获得Bank card的前缀
    private static List<Long> getBinNum(Context context) {
        String binNum = openBinNum(context);
        String[] binArr = binNum.split(",");
        List<Long> lon = new ArrayList<>();
        for (int i = 0; i < binArr.length; i++) {
            if (i % 2 == 0)
                lon.add(Long.parseLong(binArr[i]));

        }
        return lon;
    }

    //获得BankName
    private static List<String> getBinName(Context context) {
        String binNum = openBinNum(context);
        String[] binArr = binNum.split(",");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < binArr.length; i++) {
            if (i % 2 != 0) list.add(binArr[i]);
        }
        return list;
    }

    //通过输入的卡号获得银行卡信息
    public static String getNameOfBank(Context context, char[] charBin) {
        long longBin = 0;

        for (int i = 0; i < 6; i++) {
            longBin = (longBin * 10) + (charBin[i] - 48);
        }
        int index = binarySearch(getBinNum(context), longBin);
        if (index == -1) {
            return "没有获取到该银行卡信息:";
        }
        return getBinName(context).get(index);
    }

    //数量有上千条，利用二分查找算法来进行快速查找法
    public static int binarySearch(List<Long> srcArray, long des) {
        int low = 0;
        int high = srcArray.size() - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (des == srcArray.get(middle)) {
                return middle;
            } else if (des < srcArray.get(middle)) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
}
