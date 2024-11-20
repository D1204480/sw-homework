package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    String pitchFile = "pitch_breakdown.csv";
    String baseHitsFile = "base_hits_breakdown.csv";

    Batter batter = new Batter(pitchFile, baseHitsFile);
    System.out.println(pitch(batter, "true"));
    System.out.println(pitch(batter, "false"));

  } // end of main


  // 找出由打擊率高飄移至打擊率低的區域
  public static String pitch(Batter batter, String ballIsOK) {
    float[] rates = batter.getHitRate();  // 取得打擊率

    if (rates == null) {
      return "Error: Cannot load hit rates, rates array is null.";
    }

    StringBuilder str = new StringBuilder();
    List<Integer> minIndexArr = new ArrayList<>();  // 儲存最小打擊率的區塊
    int maxRateIndex = 0;   // 初始化最大值的索引
    int minRateIndex = 0;   // 最小值索引
    float maxValue = rates[0];  // 假設第一個數值是打擊率最高值
    float minValue = rates[0];  // 初始最小值
    int ballZone1 = 10 - 1;  // x1 壞球區,因陣列從0算起,所以減1
    int ballZone2 = 11 - 1;  // x2 壞球區
    int ballZone3 = 12 - 1;  // x3 壞球區
    int ballZone4 = 13 - 1;  // x4 壞球區


    for (int i = 0; i < 9; i++) {
      if (rates[i] > maxValue) {
        maxValue = rates[i];   // 更新最大值
        maxRateIndex = i;   // 更新最大值的索引

      } else if (rates[i] < minValue) {
        minValue = rates[i];   // 更新最小值
        minRateIndex = i;  // 更新最小值的索引
      }
    }
    minIndexArr.add(minRateIndex);

    // 可以投壞球
    if (ballIsOK.equals("true")) {
      if (minRateIndex == 0) {
        minIndexArr = compareHitsRate(0, rates);
      } else if (minRateIndex == 1) {
        minIndexArr = compareHitsRate(1, rates);
      } else if (minRateIndex == 2) {
        minIndexArr = compareHitsRate(2, rates);
      } else if (minRateIndex == 3) {
        minIndexArr = compareHitsRate(3, rates);
      } else if (minRateIndex == 4) {
        minIndexArr = compareHitsRate(4, rates);
      } else if (minRateIndex == 5) {
        minIndexArr = compareHitsRate(5, rates);
      } else if (minRateIndex == 6) {
        minIndexArr = compareHitsRate(6, rates);
      } else if (minRateIndex == 7) {
        minIndexArr = compareHitsRate(7, rates);
      } else if (minRateIndex == 8) {
        minIndexArr = compareHitsRate(8, rates);
      }
    }

    // 轉換索引為顯示文字
    String[] minRateArr = convertIndexToDisplay(minIndexArr);

    // 印出看結果
//    str.append("Max Rate Index: ").append((maxRateIndex) + 1).append("\t");
//    str.append("Min Rate Index: ").append((minRateDisplay)).append("\t");;

    // 將最大值和最小值的索引加到結果中
    for (String s : minRateArr) {
      str.append("(").append(maxRateIndex + 1).append(", ").append(s).append(")");
    }

    return str.toString();
  } // end of pitch

  // 將索引轉換為顯示文字的輔助方法
  private static String[] convertIndexToDisplay(List<Integer> indexes) {
    // 創建與輸入List相同大小的數組
    String[] minIndexes = new String[indexes.size()];

    for (int i = 0; i < indexes.size(); i++) {
      // 獲取實際的索引值
      int index = indexes.get(i);

      switch (index) {
        case 9:  // ballZone1
          minIndexes[i] = "x1";
          break;
        case 10:  // ballZone2
          minIndexes[i] = "x2";
          break;
        case 11:  // ballZone3
          minIndexes[i] = "x3";
          break;
        case 12:  // ballZone4
          minIndexes[i] = "x4";
          break;
        default:
          minIndexes[i] = String.valueOf(i + 1);
          break;
      }
    }
    return minIndexes;
  } // ens of convertIndexToDisplay

  // 比較"好.壞球區"打擊率
//  private static List<Integer> compareHitsRate(int index, float[] rates) {
//    float min = 0;
//    float temp;
//    int minIndex = 0;
//    float[] compare = new float[3];
//    List<Integer> ballIndex = new ArrayList<>();
//
//    switch (index) {
//      case 0:  // 比較0,9區
//        if (rates[9] < rates[0]) {
//          minIndex = 9;
//          ballIndex.add(minIndex);
//        } else if (rates[9] == rates[0]) {
//          ballIndex.add(0);
//          ballIndex.add(9);
//        }
//      break;
//
//      case 1:  // 比較1,9,10區
//        compare[0] = rates[1];
//        compare[1] = rates[9];
//        compare[2] = rates[10];
//
//        // 找出最小值
//        for (int i = 0; i < compare.length; i++) {
//          if (compare[i] > compare[i + 1]) {
//            temp = compare[i];
//            compare[i] = compare[i + 1];
//            compare[i + 1] = temp;
//            min = compare[i];
//          }
//        }
//
//        if (rates[1] == min) {
//          ballIndex.add(1);
//        } else if (rates[9] == min) {
//          ballIndex.add(9);
//        } else {
//          ballIndex.add(10);
//        }
//        break;
//
//      case 2:  // 比較2,10
//        if (rates[10] < rates[2]) {
//          minIndex = 10;
//          ballIndex.add(minIndex);
//        } else if (rates[10] == rates[2]) {
//          ballIndex.add(10);
//          ballIndex.add(2);
//        }
//        break;
//
//      case 3:  // 比較3,9,10
//        compare[0] = rates[3];
//        compare[1] = rates[9];
//        compare[2] = rates[10];
//
//        // 找出最小值
//        for (int i = 0; i < compare.length; i++) {
//          if (compare[i] > compare[i + 1]) {
//            temp = compare[i];
//            compare[i] = compare[i + 1];
//            compare[i + 1] = temp;
//            min = compare[i];
//          }
//        }
//
//        if (rates[3] == min) {
//          ballIndex.add(3);
//        } else if (rates[9] == min) {
//          ballIndex.add(9);
//        } else {
//          ballIndex.add(10);
//        }
//        break;
//
//      case 5:  // 比較5,10,12
//        compare[0] = rates[5];
//        compare[1] = rates[10];
//        compare[2] = rates[12];
//
//        // 找出最小值
//        for (int i = 0; i < compare.length; i++) {
//          if (compare[i] > compare[i + 1]) {
//            temp = compare[i];
//            compare[i] = compare[i + 1];
//            compare[i + 1] = temp;
//            min = compare[i];
//          }
//        }
//
//        if (rates[5] == min) {
//          ballIndex.add(5);
//        } else if (rates[10] == min) {
//          ballIndex.add(10);
//        } else {
//          ballIndex.add(12);
//        }
//        break;
//
//      case 6:  // 比較6,11
//        if (rates[11] < rates[6]) {
//          minIndex = 11;
//          ballIndex.add(minIndex);
//        } else if (rates[11] == rates[6]) {
//          ballIndex.add(11);
//          ballIndex.add(6);
//        }
//      break;
//
//      case 7:  // 比較7,11,12
//        compare[0] = rates[7];
//        compare[1] = rates[11];
//        compare[2] = rates[12];
//
//        // 找出最小值
//        for (int i = 0; i < compare.length; i++) {
//          if (compare[i] > compare[i + 1]) {
//            temp = compare[i];
//            compare[i] = compare[i + 1];
//            compare[i + 1] = temp;
//            min = compare[i];
//          }
//        }
//
//        if (rates[7] == min) {
//          ballIndex.add(7);
//        } else if (rates[11] == min) {
//          ballIndex.add(11);
//        } else {
//          ballIndex.add(12);
//        }
//        break;
//
//      case 8:  // 比較8,12
//        if (rates[12] < rates[8]) {
//          minIndex = 12;
//          ballIndex.add(minIndex);
//        } else if (rates[12] == rates[8]) {
//          ballIndex.add(12);
//          ballIndex.add(8);
//        }
//      break;
//
//    }
//    return ballIndex;
//  }
  private static List<Integer> compareHitsRate(int index, float[] rates) {
    List<Integer> ballIndex = new ArrayList<>();
    if (rates == null || index < 0 || index >= rates.length) {
      return ballIndex;
    }

    switch (index) {
      case 0 -> compareZones(rates, ballIndex, 0, 9);
      case 1 -> findMinZone(rates, ballIndex, 1, 9, 10);
      case 2 -> compareZones(rates, ballIndex, 2, 10);
      case 3 -> findMinZone(rates, ballIndex, 3, 9, 10);
      case 5 -> findMinZone(rates, ballIndex, 5, 10, 12);
      case 6 -> compareZones(rates, ballIndex, 6, 11);
      case 7 -> findMinZone(rates, ballIndex, 7, 11, 12);
      case 8 -> compareZones(rates, ballIndex, 8, 12);
    }
    return ballIndex;
  }

  // 比較兩個區域
  private static void compareZones(float[] rates, List<Integer> ballIndex, int zone1, int zone2) {
    // 參數檢查
    if (rates == null || ballIndex == null ||
        zone1 < 0 || zone2 < 0 ||
        zone1 >= rates.length || zone2 >= rates.length) {
      return;
    }

    // 比較打擊率
    if (rates[zone1] < rates[zone2]) {
      ballIndex.add(zone1);  // zone1 打擊率較低
    } else if (rates[zone2] < rates[zone1]) {
      ballIndex.add(zone2);  // zone2 打擊率較低
    } else {
      // 打擊率相等時，都加入
      ballIndex.add(zone1);
      ballIndex.add(zone2);
    }
  }

  // 找出三個區域中的最小值
  private static void findMinZone(float[] rates, List<Integer> ballIndex, int zone1, int zone2, int zone3) {
    float min = Math.min(Math.min(rates[zone1], rates[zone2]), rates[zone3]);
    if (rates[zone1] == min) {
      ballIndex.add(zone1);
    } else if (rates[zone2] == min) {
      ballIndex.add(zone2);
    } else {
      ballIndex.add(zone3);
    }
  }
} // end of Main