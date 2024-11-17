package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 棒球打者數據處理類別
 */
public class Batter_v2 {
  // 定義常數
  private static final int COLUMN_COUNT = 13;
  private static final String ERROR_ARRAYS_NOT_EQUAL = "hitArr 和 pitchArr 中的數量不一致";
  private static final String ERROR_ARRAY_NULL = "%s 陣列為空值";
  private static final String ERROR_READ_FILE = "無法讀取檔案: %s";

  // 成員變數
  private final float[] hitRate;    // 打擊率
  private final int[] pitchCounts;  // 投球次數
  private final int[] hitCounts;    // 安打次數

  /**
   * 從CSV檔案建立打者數據
   *
   * @param pitchFile    投球數據檔案
   * @param baseHitsFile 安打數據檔案
   * @throws BatterDataException 當數據讀取或處理出錯時
   */
  public Batter_v2(String pitchFile, String baseHitsFile) throws BatterDataException, IOException {
    try {
      this.pitchCounts = loadFile(pitchFile);
      this.hitCounts = loadFile(baseHitsFile);
      this.hitRate = calculateHitRate(hitCounts, pitchCounts);
    } catch (Exception e) {
      throw new BatterDataException("初始化打者數據失敗", e);
    }
  }

  /**
   * 從現有數據建立打者數據
   *
   * @param hitCounts   安打次數陣列
   * @param pitchCounts 投球次數陣列
   * @throws BatterDataException 當數據無效時
   */
  public Batter_v2(int[] hitCounts, int[] pitchCounts) throws BatterDataException {
    validateArrays(hitCounts, pitchCounts);
    this.hitCounts = hitCounts.clone();
    this.pitchCounts = pitchCounts.clone();
    this.hitRate = calculateHitRate(hitCounts, pitchCounts);
  }

  /**
   * 取得打擊率陣列的副本
   */
  public float[] getHitRate() {
    return hitRate.clone();
  }

  /**
   * 取得安打次數陣列的副本
   */
  public int[] getHitCounts() {
    return hitCounts.clone();
  }

  /**
   * 取得投球次數陣列的副本
   */
  public int[] getPitchCounts() {
    return pitchCounts.clone();
  }

  /**
   * 輸出打者統計數據
   */
  public void printStats() {
    System.out.println("打擊統計數據:");
    System.out.println("位置 \t投球數 \t安打數 \t打擊率");
    for (int i = 0; i < COLUMN_COUNT; i++) {
      System.out.printf("%4d\t%4d\t%4d\t%.3f%n",
          i + 1, pitchCounts[i], hitCounts[i], hitRate[i]);
    }
  }

  // 私有輔助方法

  /**
   * 從CSV檔案載入數據
   */
  private int[] loadFile(String filename) throws IOException {
    int[] data = new int[COLUMN_COUNT];
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        getClass().getClassLoader().getResourceAsStream(filename)))) {

      reader.readLine(); // 跳過標題行
      String line = reader.readLine();
      if (line == null) {
        throw new IOException("檔案為空: " + filename);
      }

      String[] values = line.split(",");
      if (values.length != COLUMN_COUNT) {
        throw new IOException("無效的數據格式: " + filename);
      }

      for (int i = 0; i < values.length; i++) {
        data[i] = Integer.parseInt(values[i].trim());
      }
    }
    return data;
  }

  /**
   * 計算打擊率
   */
  private float[] calculateHitRate(int[] hits, int[] pitches) {
    float[] rates = new float[COLUMN_COUNT];
    for (int i = 0; i < COLUMN_COUNT; i++) {
      rates[i] = pitches[i] == 0 ? 0 : (float) hits[i] / pitches[i];
    }
    return rates;
  }

  /**
   * 驗證輸入數據的有效性
   */
  private void validateArrays(int[] hits, int[] pitches) throws BatterDataException {
    if (hits == null || pitches == null) {
      throw new BatterDataException("輸入陣列不能為null");
    }
    if (hits.length != COLUMN_COUNT || pitches.length != COLUMN_COUNT) {
      throw new BatterDataException("陣列長度必須為 " + COLUMN_COUNT);
    }
  }

  /**
   * 打者數據異常類別
   */
  public static class BatterDataException extends Exception {
    public BatterDataException(String message) {
      super(message);
    }

    public BatterDataException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  // 主程式範例
  public static void main(String[] args) {
    try {
      // 從檔案建立打者數據
      Batter_v2 batter = new Batter_v2("pitch_breakdown.csv", "base_hits_breakdown.csv");
      batter.printStats();

    } catch (BatterDataException e) {
      System.err.println("錯誤: " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}