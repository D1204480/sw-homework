package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;

class BatterTest {
  private static final int COLUMN_COUNT = 13;
  private static final String TEST_DIR = "src/test/resources/";


  @Test
  @DisplayName("測試正常CSV檔案解析")
  void testParseValidFile() throws IOException {
    // 準備測試檔案
    String filename = "valid_test.csv";

    // 執行測試
    int[] result = Batter.parseFile(filename);

    // 驗證結果
    assertNotNull(result);
    assertEquals(COLUMN_COUNT, result.length);
    for (int i = 0; i < COLUMN_COUNT; i++) {
      assertEquals(i + 1, result[i]);
    }
  }

  @Test
  @DisplayName("測試檔案不存在的情況")
  void testParseNonexistentFile() {
    int[] result = Batter.parseFile("nonexistent.csv");
    assertNull(result);
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/invalid_test.csv", numLinesToSkip = 1)
  @DisplayName("測試不同的錯誤欄位數量")
  void testParseInvalidColumnCount(String data) throws IOException {
    String filename = "invalid_test.csv";

    int[] result = Batter.parseFile(filename);
    assertEquals(COLUMN_COUNT, result.length);
    for (int value : result) {
      assertEquals(0, value);
    }
  }

  @Test
  @DisplayName("測試非數字資料")
  void testParseInvalidNumber() throws IOException {
    String filename = "invalid_number_test.csv";

    int[] result = Batter.parseFile(filename);
    assertNull(result);
  }




  @Test
  void calHitsRate() {
  }

  // 輔助方法：建立測試檔案
  private void createTestFile(String filename, String content) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_DIR + filename))) {
      writer.write(content);
    }
  }

}