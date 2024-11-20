package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
  // 創建一個包裝器類來測試私有方法
  private static class MainWrapper extends Main {
    public static List<Integer> compareHitsRateTest(int index, float[] rates) {
      // 使用反射調用私有方法
      try {
        Method method = Main.class.getDeclaredMethod("compareHitsRate", int.class, float[].class);
        method.setAccessible(true);
        return (List<Integer>) method.invoke(null, index, rates);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private float[] rates;

  @BeforeEach
  void setUp() {
    // 初始化測試用的打擊率陣列
    rates = new float[13];
  }

  @Test
  @DisplayName("測試 case 0: 比較0,9區")
  void testCompareZoneZero() {
    // 測試 rates[9] < rates[0]
    rates[0] = 0.300f;
    rates[9] = 0.200f;
    List<Integer> result = MainWrapper.compareHitsRateTest(0, rates);
    assertEquals(1, result.size());
    assertEquals(9, result.get(0));

    // 測試 rates[9] == rates[0]
    rates[0] = 0.300f;
    rates[9] = 0.300f;
    result = MainWrapper.compareHitsRateTest(0, rates);
    assertEquals(2, result.size());
    assertTrue(result.contains(0));
    assertTrue(result.contains(9));

    // 測試 rates[9] > rates[0]
    rates[0] = 0.200f;
    rates[9] = 0.300f;
    result = MainWrapper.compareHitsRateTest(0, rates);
    assertEquals(0, result.size());
  }

  @Test
  @DisplayName("測試 case 1: 比較1,9,10區")
  void testCompareZoneOne() {
    // 測試 rates[1] 最小
    rates[1] = 0.100f;
    rates[9] = 0.200f;
    rates[10] = 0.300f;
    List<Integer> result = MainWrapper.compareHitsRateTest(1, rates);
    assertEquals(1, result.size());
    assertEquals(1, result.get(0));

    // 測試 rates[9] 最小
    rates[1] = 0.300f;
    rates[9] = 0.100f;
    rates[10] = 0.200f;
    result = MainWrapper.compareHitsRateTest(1, rates);
    assertEquals(1, result.size());
    assertEquals(9, result.get(0));

    // 測試 rates[10] 最小
    rates[1] = 0.300f;
    rates[9] = 0.200f;
    rates[10] = 0.100f;
    result = MainWrapper.compareHitsRateTest(1, rates);
    assertEquals(1, result.size());
    assertEquals(10, result.get(0));

    // 測試相等的情況
    rates[1] = 0.200f;
    rates[9] = 0.200f;
    rates[10] = 0.200f;
    result = MainWrapper.compareHitsRateTest(1, rates);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("測試 case 2: 比較2,10區")
  void testCompareZoneTwo() {
    // 測試 rates[10] < rates[2]
    rates[2] = 0.300f;
    rates[10] = 0.200f;
    List<Integer> result = MainWrapper.compareHitsRateTest(2, rates);
    assertEquals(1, result.size());
    assertEquals(10, result.get(0));

    // 測試 rates[10] == rates[2]
    rates[2] = 0.200f;
    rates[10] = 0.200f;
    result = MainWrapper.compareHitsRateTest(2, rates);
    assertEquals(2, result.size());
    assertTrue(result.contains(2));
    assertTrue(result.contains(10));
  }

  @Test
  @DisplayName("測試 case 5: 比較5,10,12區")
  void testCompareZoneFive() {
    // 測試 rates[5] 最小
    rates[5] = 0.100f;
    rates[10] = 0.200f;
    rates[12] = 0.300f;
    List<Integer> result = MainWrapper.compareHitsRateTest(5, rates);
    assertEquals(1, result.size());
    assertEquals(5, result.get(0));

    // 測試三個值相等
    rates[5] = 0.200f;
    rates[10] = 0.200f;
    rates[12] = 0.200f;
    result = MainWrapper.compareHitsRateTest(5, rates);
    assertEquals(1, result.size());
  }

  @Test
  @DisplayName("測試邊界值")
  void testBoundaryValues() {
    // 測試極小值
    rates[7] = 0.001f;
    rates[11] = 0.002f;
    rates[12] = 0.003f;
    List<Integer> result = MainWrapper.compareHitsRateTest(7, rates);
    assertEquals(1, result.size());
    assertEquals(7, result.get(0));

    // 測試極大值
    rates[7] = 0.999f;
    rates[11] = 0.998f;
    rates[12] = 0.997f;
    result = MainWrapper.compareHitsRateTest(7, rates);
    assertEquals(1, result.size());
    assertEquals(12, result.get(0));
  }

  @Test
  @DisplayName("測試無效的索引")
  void testInvalidIndex() {
    // 測試無效的索引值
    List<Integer> result = MainWrapper.compareHitsRateTest(4, rates);
    assertTrue(result.isEmpty());

    result = MainWrapper.compareHitsRateTest(-1, rates);
    assertTrue(result.isEmpty());

    result = MainWrapper.compareHitsRateTest(9, rates);
    assertTrue(result.isEmpty());
  }

}