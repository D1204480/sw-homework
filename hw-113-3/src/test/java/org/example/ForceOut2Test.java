package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
                () -> assertEquals(Set.of("1B"), new HashSet<>(Arrays.asList(forceOut2.getForceOut(0, 0, 0).split(", ")))),
                () -> assertEquals(Set.of("1B", "2B"), new HashSet<>(Arrays.asList(forceOut2.getForceOut(1, 0, 0).split(", ")))),
                () -> assertEquals(Set.of("1B", "2B"), new HashSet<>(Arrays.asList(forceOut2.getForceOut(1, 0, 1).split(", ")))),
                () -> assertEquals(Set.of("1B"), new HashSet<>(Arrays.asList(forceOut2.getForceOut(0, 0, 1).split(", ")))),
                () -> assertEquals(Set.of("1B", "2B", "3B", "HB"), new HashSet<>(Arrays.asList(forceOut2.getForceOut(1, 1, 1).split(", "))))
        );
    }
}