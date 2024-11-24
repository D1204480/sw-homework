package org.example;

import java.util.ArrayList;
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
    List<Integer> findMinIndexArr = new ArrayList<>();  // 儲存最小打擊率的區塊
    int maxRateIndex = 0;   // 初始化最大值的索引
    int minRateIndex = 0;   // 最小值索引
    float maxValue = rates[0];  // 假設第一個數值是打擊率最高值
    float minValue = rates[0];  // 初始最小值, 好球區

    // 好球帶打擊率, 找出最高最低值
    for (int i = 0; i < 9; i++) {
      if (rates[i] > maxValue) {
        maxValue = rates[i];   // 更新最大值
        maxRateIndex = i;   // 更新最大值的索引
      }

      if (rates[i] < minValue) {
        minValue = rates[i];   // 更新最小值
        findMinIndexArr.clear(); // 清除之前存的索引
        minRateIndex = i;  // 更新最小值的索引
      }
    }

    // 把所有最小值存入陣列
    for (int i = 0; i < 9; i++) {
      if (rates[i] == minValue) {
        findMinIndexArr.add(i);
      }
    }

    // 比較包含壞球帶之打擊率
    if (ballIsOK.equals("true")) {
      for (int i = 9; i < 13; i++) {
        if (rates[i] < minValue) {
          minValue = rates[i];   // 更新最小值
          minRateIndex = i;  // 更新最小值的索引
          findMinIndexArr.remove(0);  // 移除前一筆資料
          findMinIndexArr.add(minRateIndex);

        } else if (rates[i] == minValue) {
          findMinIndexArr.add(minRateIndex);
        }
      }
    }


    // 轉換索引為顯示文字
    String[] minRateArr = convertIndexToDisplay(findMinIndexArr);

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
          minIndexes[i] = String.valueOf(index + 1);
          break;
      }
    }
    return minIndexes;
  } // ens of convertIndexToDisplay

} // end of Main