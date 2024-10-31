package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForceOut2Test {

  @Test
  void getForceOut() {
        /* 封殺狀況:
    x       -> 1B
    1B      -> 1B, 2B
    1B, 3B  -> 1B, 2B
    3B      -> 1B
     */
    ForceOut2 forceOut2 = new ForceOut2();  // 建立物件
    assertAll("test forceOut2 for getForceOut",
        () -> assertEquals("1B", forceOut2.getForceOut(0, 0, 0)),
        () -> assertEquals("1B, 2B", forceOut2.getForceOut(1, 0, 0)),
        () -> assertEquals("1B, 2B", forceOut2.getForceOut(1, 0, 1)),
        () -> assertEquals("1B", forceOut2.getForceOut(0, 0, 1))
    );
  }
}