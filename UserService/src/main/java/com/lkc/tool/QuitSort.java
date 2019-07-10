package com.lkc.tool;

import com.lkc.model.industry.deviceInfo.Rule;
import com.lkc.model.industry.sensorInfo.SensorInfo;

import java.util.List;

public class QuitSort {
    public static void quickSort(List<SensorInfo> arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int i, j;
        String index;
        i = start;
        j = end - 1;
        index = arr.get(i).getSensorName();

        while (i < j) {
            while (i < j && arr.get(j).getSensorName().compareTo(index) >= 0) {
                j--;
            }
            while (i < j && arr.get(i).getSensorName().compareTo(index) < 0) {
                i++;
            }
            if (i < j) {
                SensorInfo empt = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, empt);
            }
        }
        if (arr.get(i).getSensorName().compareTo(arr.get(start).getSensorName()) < 0) {
            SensorInfo empt = arr.get(i);
            arr.set(i, arr.get(start));
            arr.set(start, empt);
        }

        quickSort(arr, start, i - 1);
        quickSort(arr, i + 1, end);

    }

    public static void quickSortByrule(List<Rule> arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int i, j;
        String index;
        i = start;
        j = end - 1;
        index = arr.get(i).getTypeName();

        while (i < j) {
            while (i < j && arr.get(j).getTypeName().compareTo(index) >= 0) {
                j--;
            }
            while (i < j && arr.get(i).getTypeName().compareTo(index) < 0) {
                i++;
            }
            if (i < j) {
                Rule empt = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, empt);
            }
        }
        if (arr.get(i).getTypeName().compareTo(arr.get(start).getTypeName()) < 0) {
            Rule empt = arr.get(i);
            arr.set(i, arr.get(start));
            arr.set(start, empt);
        }

        quickSortByrule(arr, start, i - 1);
        quickSortByrule(arr, i + 1, end);
    }
}
