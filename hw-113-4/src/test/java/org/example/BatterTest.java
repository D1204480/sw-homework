package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BatterTest {

  @ParameterizedTest
  @CsvFileSource(resources = "/base_hits_breakdown.csv", numLinesToSkip = 1)
  void parseFile(int num, int num2) {
    assertNotNull(num);
    assertNotNull(num2);
  }

  @Test
  void parseFile() {
  }

  @Test
  void calHitsRate() {
  }
}