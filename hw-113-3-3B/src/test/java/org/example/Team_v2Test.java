package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class Team_v2Test {

  @Test
  void isValidRecordTest() {
    // Case 1: Valid record, wins + losses = 162
    Team_v2 teamValid = new Team_v2("AL", "Team A", 90, 72, 0.556f);
    assertTrue(teamValid.isValidRecord(), "Expected isValidRecord to be true for 90 wins and 72 losses");

    // Case 2: Invalid record, wins + losses < 160
    Team_v2 teamInvalid = new Team_v2("NL", "Team B", 80, 75, 0.516f);
    assertFalse(teamInvalid.isValidRecord(), "Expected isValidRecord to be false for 80 wins and 75 losses");

    // Case 3: Edge case, wins + losses = 160
    Team_v2 teamEdgeCase = new Team_v2("AL", "Team C", 80, 80, 0.500f);
    assertFalse(teamEdgeCase.isValidRecord(), "Expected isValidRecord to be false for 80 wins and 80 losses");
  }

  @Test
  void compareToTest() {
    // Case 1: Different wins - Team with more wins should come first
    Team_v2 team1 = new Team_v2("AL", "Team A", 95, 67, 0.586f);
    Team_v2 team2 = new Team_v2("AL", "Team B", 90, 72, 0.556f);
    assertTrue(team1.compareTo(team2) < 0, "Expected team1 with more wins to come first");

    // Case 2: Same wins, different losses - Team with fewer losses should come first
    Team_v2 team3 = new Team_v2("AL", "Team C", 90, 70, 0.563f);
    Team_v2 team4 = new Team_v2("AL", "Team D", 90, 72, 0.556f);
    assertTrue(team3.compareTo(team4) < 0, "Expected team3 with fewer losses to come first");

    // Case 3: Same wins and losses, different names - Team with alphabetically earlier name should come first
    Team_v2 team5 = new Team_v2("AL", "Team A", 90, 72, 0.556f);
    Team_v2 team6 = new Team_v2("AL", "Team B", 90, 72, 0.556f);
    assertTrue(team5.compareTo(team6) < 0, "Expected team5 with alphabetically earlier name to come first");

  }
}