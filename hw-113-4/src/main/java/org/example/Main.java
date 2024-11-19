package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    String pitchFile = "pitch_breakdown.csv";
    String baseHitsFile = "base_hits_breakdown.csv";

    Batter batter = new Batter(pitchFile, baseHitsFile);
    System.out.println(pitch(batter, "ball"));
    System.out.println(pitch(batter, "strike"));

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

    // 可以投壞球
    if (ballIsOK.equals("ball")) {
      if (minRateIndex == 0) {
        if (rates[ballZone1] < rates[0]) {
          minRateIndex = ballZone1;
        } else if (rates[ballZone1] == rates[0]) {
          minIndexArr.add((int) rates[ballZone1]);
          minIndexArr.add((int) rates[0]);
        }
      }

      if (minRateIndex == 1) {
        if (rates[ballZone1] < rates[1]) {
          minRateIndex = ballZone1;
        } else if (rates[ballZone1] == rates[0]) {
          minIndexArr.add((int) rates[ballZone1]);
          minIndexArr.add((int) rates[0]);
        }
      }

      if (minRateIndex == 2) {
        if (rates[ballZone2] < rates[2]) {
          minRateIndex = ballZone2;
        } else if (rates[ballZone2] == rates[2]) {
          minIndexArr.add((int) rates[ballZone2]);
          minIndexArr.add((int) rates[2]);
        }
      }

      if (minRateIndex == 6) {
        if (rates[ballZone3] < rates[6]) {
          minRateIndex = ballZone3;
        } else if (rates[ballZone3] == rates[6]) {
          minIndexArr.add((int) rates[ballZone3]);
          minIndexArr.add((int) rates[6]);
        }
      }

      if (minRateIndex == 8) {
        if (rates[ballZone4] < rates[8]) {
          minRateIndex = ballZone4;
        } else if (rates[ballZone4] == rates[8]) {
          minIndexArr.add((int) rates[ballZone4]);
          minIndexArr.add((int) rates[8]);
        }
      }
    }

    minIndexArr.add(minRateIndex);  // 加入List

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

  // 比較好,壞球區打擊率
  private static List<Integer> compareHitSRate(int index, float[] rates) {
    float min;
    int minIndex = 0;
    List<Integer> ballIndex = new ArrayList<>();

    switch (index) {
      case 0:  // 比較0,9區
        if (rates[9] < rates[0]) {
          minIndex = 9;
          ballIndex.add(minIndex);
        } else if (rates[9] == rates[0]) {
          ballIndex.add(0);
          ballIndex.add(9);
        }
//        return ballIndex;
      break;

      case 1:  // 比較1,9,10區
      break;

      case 2:  // 比較2,10
        if (rates[10] < rates[2]) {
          minIndex = 10;
          ballIndex.add(minIndex);
        } else if (rates[10] == rates[2]) {
          ballIndex.add(10);
          ballIndex.add(2);
        }
//        return ballIndex;
        break;

      case 3:  // 比較3,9,10
      case 5:  // 比較5,10,12

      case 6:  // 比較6,11
        if (rates[11] < rates[6]) {
          minIndex = 11;
          ballIndex.add(minIndex);
        } else if (rates[11] == rates[6]) {
          ballIndex.add(11);
          ballIndex.add(6);
        }
//        return ballIndex;
      break;

      case 7:  // 比較7,11,12
      case 8:  // 比較8,12
        if (rates[12] < rates[8]) {
          minIndex = 12;
          ballIndex.add(minIndex);
        } else if (rates[12] == rates[8]) {
          ballIndex.add(12);
          ballIndex.add(8);
        }
//        return ballIndex;
      break;

    }
    return ballIndex;
  }
} // end of Main