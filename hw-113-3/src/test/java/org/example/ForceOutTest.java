package org.example;

import static org.junit.jupiter.api.Assertions.*;

class ForceOutTest {

  @org.junit.jupiter.api.Test
  void calForceOut() {
    /*
    x       -> 1B
    1B      -> 1B, 2B
    1B, 3B  -> 1B, 2B
    3B      -> 1B
     */
    ForceOut forceOut = new ForceOut();
    assertAll("test force out on calForceOut",
        () -> assertEquals("1B", forceOut.calForceOut(0,0,0)),
        () -> assertEquals("1B, 2B", forceOut.calForceOut(1,0,0)),
        () -> assertEquals("1B, 2B", forceOut.calForceOut(1,0,3)),
        () -> assertEquals("1B", forceOut.calForceOut(0,0,1))
        );

  }
}