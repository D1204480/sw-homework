package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Batter {
  private float[] hitRate;   // 打擊率
  private static final int COLUMN_COUNT = 13;

  public float[] getHitRate() {
    return hitRate.clone(); // 防止外部直接修改陣列
  }

  // 建構子 1: 直接傳入檔案名稱
  public Batter(String pitchFile, String baseHitsFile) {
    try {
      int[] pitchArr = parseFile(pitchFile);
      int[] baseHitsArr = parseFile(baseHitsFile);
      this.hitRate = calHitsRate(baseHitsArr, pitchArr);
    } catch (Exception e) {
      // 初始化空陣列而不是 null
      this.hitRate = new float[COLUMN_COUNT];
      System.err.println("Error initializing Batter: " + e.getMessage());
    }
  }

  // 建構子2: 直接傳入數據
  public Batter(int[] baseHitsArr, int[] pitchArr) {
    this.hitRate = calHitsRate(baseHitsArr, pitchArr);
  }

  // 建構子3: 無參數建構子（如果真的需要的話）
  public Batter() {
    this.hitRate = new float[COLUMN_COUNT];  // 初始化為空陣列而不是 null
  }

  public static void main(String[] args) {

    String pitchFile = "pitch_breakdown.csv";
    String baseHitsFile = "base_hits_breakdown.csv";

    int[] pitchArr;
    int[] baseHitsArr;

    // 讀檔, 資料放入陣列
    pitchArr = parseFile(pitchFile);
    baseHitsArr = parseFile(baseHitsFile);

    // 計算打擊率
    Batter batter = new Batter();
    batter.hitRate = calHitsRate(baseHitsArr, pitchArr);

    // 測試輸出
    for (int line : pitchArr) {
      System.out.print(line + "\t");
    }
    System.out.println();
    for (int line : baseHitsArr) {
      System.out.print(line + "\t");
    }
    System.out.println();
    for (float line : batter.hitRate) {
      System.out.print(String.format("%.2f \t", line));
    }


  } // end of main

  //讀檔
  public static int[] parseFile(String filename) {
    int[] tempArr = new int[COLUMN_COUNT];   // 建立陣列

    try (BufferedReader br = new BufferedReader(new InputStreamReader(
        Batter.class.getClassLoader().getResourceAsStream(filename)))) {

      if (br == null) {
        throw new IOException("找不到檔案: " + filename);
      }

      String line = br.readLine();  // 跳過第一行表頭
      boolean hasValidData = false; // 添加標記來追蹤是否成功讀取到有效資料
      while ((line = br.readLine()) != null) {
        assert line != null : "讀不到資料";
        String[] data = line.split(",");

        if (data.length == COLUMN_COUNT) {
          try {
            // 檢查每個欄位是否都是有效數字
            for (String value : data) {
              // 嘗試解析每個值，如果有任何一個不是數字，就會拋出異常
              Integer.parseInt(value.trim());
            }

            // 所有檢查都通過後，才填充陣列
            for (int i = 0; i < data.length; i++) {
              tempArr[i] = Integer.parseInt(data[i].trim());
            }
            hasValidData = true; // 標記已成功讀取資料

          } catch (NumberFormatException e) {
            e.printStackTrace();
            return null; // 數據格式錯誤
          }
        }
      }

      // 如果沒有讀取到有效資料，返回null
      if (!hasValidData) {
        return null;
      }

    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
      return null; // 若發生錯誤，返回 null
    }

    return tempArr;

  } // end of parseFile


  public static float[] calHitsRate(int[] hitArr, int[] pitchArr) {
    float[] tempArr = new float[COLUMN_COUNT];   // 建立物件

    assert hitArr != null : "base_hits 陣列為空值";
    assert pitchArr != null : "pitch 陣列為空值";

    // 確保兩者長度一致
    if (hitArr.length != pitchArr.length) {
      throw new IllegalArgumentException("hitArr 和 pitchArr 中的數字數量不一致");
    }

    for (int i = 0; i < hitArr.length; i++) {
      // 計算擊球率，並加入 tempList
      if (pitchArr[i] != 0) {
        try {
          tempArr[i] = (float) hitArr[i] / pitchArr[i];

        } catch (NumberFormatException e) {
          e.printStackTrace();
          return null; // 非數字
        }
      }
    }

    return tempArr;

  } // end of calHitsRate

} // end of Batter
