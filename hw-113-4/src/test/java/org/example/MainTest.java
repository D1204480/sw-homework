package org.example;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class MainTest {

  private Batter batterMock;

  @Before
  public void setUp() {
    // 創建 Batter 的模擬對象
    batterMock = new MockBatter();
  }

  @Test
  public void testPitchWithValidRatesInStrikeZone() {
    String result = Main.pitch(batterMock, "false");
    assertEquals("(3, 7)", result);
  }

  @Test
  public void testPitchWithValidRatesIncludingBallZone() {
    String result = Main.pitch(batterMock, "true");
    assertEquals("(3, x2)", result);
  }

  @Test
  public void testPitchWithNullRates() {
    Batter nullBatter = new NullBatter();
    String result = Main.pitch(nullBatter, "false");
    assertEquals("Error: Cannot load hit rates, rates array is null.", result);
  }

  @Test
  public void testPitchWithMultipleMinimumRates() {
    Batter multiMinBatter = new MultiMinBatter();
    String result = Main.pitch(multiMinBatter, "false");
    assertEquals("(1, 4)(1, 7)", result);
  }

  // Mock Batter class for testing
  private static class MockBatter extends Batter {
    public MockBatter() {
      super("", ""); // 空的文件路徑
    }

    @Override
    public float[] getHitRate() {
      // 模擬打擊率數據：9個好球區 + 4個壞球區
      return new float[] {
          0.300f, 0.280f, 0.350f, // 1-3
          0.250f, 0.270f, 0.290f, // 4-6
          0.200f, 0.310f, 0.280f, // 7-9
          0.240f, 0.150f, 0.220f, 0.230f // x1-x4
      };
    }
  }

  // Mock Batter class that returns null rates
  private static class NullBatter extends Batter {
    public NullBatter() {
      super("", "");
    }

    @Override
    public float[] getHitRate() {
      return null;
    }
  }

  // Mock Batter class with multiple minimum rates
  private static class MultiMinBatter extends Batter {
    public MultiMinBatter() {
      super("", "");
    }

    @Override
    public float[] getHitRate() {
      return new float[] {
          0.350f, 0.280f, 0.300f,
          0.200f, 0.270f, 0.290f,
          0.200f, 0.310f, 0.280f,
          0.240f, 0.220f, 0.230f, 0.250f
      };
    }
  }
}