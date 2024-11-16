package org.example;

public class Main {

  public static void main(String[] args) {
    Batter batter = new Batter("pitch_breakdown.csv", "base_hits_breakdown.csv");
    System.out.println(pitch(batter, "ball"));
    System.out.println(pitch(batter, "strike"));

  } // end of main


  public static String pitch(Batter batter, String ballIsOK) {
    float[] rates = batter.getHitRate();  // 取得打擊率

    if (rates == null) {
      return "Error: Cannot load hit rates, rates array is null.";
    }

    StringBuilder str = new StringBuilder();
    int maxRateIndex = 0;   // 初始化最大值的索引
    int minRateIndex = 0;   // 最小值索引
    float maxValue = rates[0];  // 假設第一個數值是打擊率最高值
    float minValue = rates[0];  // 初始最小值
    int ballZone1 = 10 - 1;  // x1 壞球區
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
    for (int k = 0; k < rates.length; k++) {
      if (ballIsOK.equals("ball")) {
        if (rates[ballZone1] <= rates[0]) {
          minRateIndex = ballZone1;
        } else if (rates[ballZone2] <= rates[2]) {
          minRateIndex = ballZone2;
        } else if (rates[ballZone3] <= rates[6]) {
          minRateIndex = ballZone3;
        } else if (rates[ballZone4] <= rates[8]) {
          minRateIndex = ballZone4;
        }
      }
    }

    // 轉換索引為顯示文字
    String minRateDisplay = convertIndexToDisplay(minRateIndex);

    // 將最大值和最小值的索引加到結果中
//    str.append("Max Rate Index: ").append((maxRateIndex) + 1).append("\t");
//    str.append("Min Rate Index: ").append((minRateDisplay)).append("\t");;
    str.append("(" + (maxRateIndex + 1) + ", " + minRateDisplay + ")");

    return str.toString();
  } // end of pitch


  // 將索引轉換為顯示文字的輔助方法
  private static String convertIndexToDisplay(int index) {
    switch (index) {
      case 9:  // ballZone1
        return "x1";
      case 10:  // ballZone2
        return "x2";
      case 11:  // ballZone3
        return "x3";
      case 12:  // ballZone4
        return "x4";
      default:
        return String.valueOf(index + 1);
    }
  } // ens of convertIndexToDisplay

} // end of Main