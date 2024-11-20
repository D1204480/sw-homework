package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

    // 新增 compareZonesTest 方法
    public static void compareZonesTest(float[] rates, List<Integer> ballIndex,
                                        int zone1, int zone2) {
      try {
        Method method = Main.class.getDeclaredMethod("compareZones",
            float[].class, List.class, int.class, int.class);
        method.setAccessible(true);
        method.invoke(null, rates, ballIndex, zone1, zone2);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  private float[] rates;
  private List<Integer> ballIndex;

  @BeforeEach
  void setUp() {
    // 初始化測試用的打擊率陣列
    rates = new float[13];
    // 設置預設打擊率
    for (int i = 0; i < rates.length; i++) {
      rates[i] = 0.300f;
    }
    ballIndex = new ArrayList<>();
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
    assertEquals(1, result.size());
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


  @Test
  @DisplayName("測試 compareZones: 第一區較低")
  void testCompareZonesFirstLower() {
    rates[0] = 0.200f;  // 較低
    rates[9] = 0.300f;  // 較高

    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);

    assertEquals(1, ballIndex.size(), "應該只有一個結果");
    assertEquals(0, ballIndex.get(0), "應該選擇打擊率較低的區域");
  }

  @Test
  @DisplayName("測試 compareZones: 第二區較低")
  void testCompareZonesSecondLower() {
    rates[0] = 0.300f;  // 較高
    rates[9] = 0.200f;  // 較低

    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);

    assertEquals(1, ballIndex.size(), "應該只有一個結果");
    assertEquals(9, ballIndex.get(0), "應該選擇打擊率較低的區域");
  }

  @Test
  @DisplayName("測試 compareZones: 打擊率相等")
  void testCompareZonesEqual() {
    rates[0] = 0.250f;
    rates[9] = 0.250f;

    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);

    assertEquals(2, ballIndex.size(), "打擊率相等時應該有兩個結果");
    assertTrue(ballIndex.contains(0), "應該包含第一個區域");
    assertTrue(ballIndex.contains(9), "應該包含第二個區域");
  }

  @Test
  @DisplayName("測試 compareZones: 所有實際使用的區域組合")
  void testCompareZonesAllCombinations() {
    // 測試 0,9 區域
    rates[0] = 0.300f;
    rates[9] = 0.200f;
    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);
    assertEquals(1, ballIndex.size());
    assertEquals(9, ballIndex.get(0));
    ballIndex.clear();

    // 測試 2,10 區域
    rates[2] = 0.300f;
    rates[10] = 0.200f;
    MainWrapper.compareZonesTest(rates, ballIndex, 2, 10);
    assertEquals(1, ballIndex.size());
    assertEquals(10, ballIndex.get(0));
    ballIndex.clear();

    // 測試 6,11 區域
    rates[6] = 0.300f;
    rates[11] = 0.200f;
    MainWrapper.compareZonesTest(rates, ballIndex, 6, 11);
    assertEquals(1, ballIndex.size());
    assertEquals(11, ballIndex.get(0));
    ballIndex.clear();

    // 測試 8,12 區域
    rates[8] = 0.300f;
    rates[12] = 0.200f;
    MainWrapper.compareZonesTest(rates, ballIndex, 8, 12);
    assertEquals(1, ballIndex.size());
    assertEquals(12, ballIndex.get(0));
  }

  @Test
  @DisplayName("測試 compareZones: 極值差異")
  void testCompareZonesExtremeValues() {
    // 測試極小值差異
    rates[0] = 0.001f;
    rates[9] = 0.002f;
    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);
    assertEquals(1, ballIndex.size());
    assertEquals(0, ballIndex.get(0));
    ballIndex.clear();

    // 測試極大值差異
    rates[0] = 0.999f;
    rates[9] = 0.998f;
    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);
    assertEquals(1, ballIndex.size());
    assertEquals(9, ballIndex.get(0));
  }

  @Test
  @DisplayName("測試 compareZones: 細微差異")
  void testCompareZonesSmallDifferences() {
    // 測試非常接近的值
    rates[0] = 0.3001f;
    rates[9] = 0.3000f;

    MainWrapper.compareZonesTest(rates, ballIndex, 0, 9);

    assertEquals(1, ballIndex.size());
    assertEquals(9, ballIndex.get(0));
  }

  @AfterEach
  void tearDown() {
    ballIndex.clear();
  }
}