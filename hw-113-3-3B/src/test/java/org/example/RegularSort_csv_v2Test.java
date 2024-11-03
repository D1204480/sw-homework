package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RegularSort_csv_v2Test {

//  to capture console output from System.out
  private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

  @BeforeEach
//  to capture output in the test
  void setUp() {
    System.setOut(new PrintStream(outputStreamCaptor));  // Redirect System.out to capture output
  }

  @Test
  void initializeLeagueTeamsTest() {
    Map<String, List<Team_v2>> leagueTeams = RegularSort_csv_v2.initializeLeagueTeams();
    assertNotNull(leagueTeams);
    assertEquals(6, leagueTeams.size());
    assertTrue(leagueTeams.containsKey("AL EAST"));
    assertTrue(leagueTeams.containsKey("NL WEST"));
    assertTrue(leagueTeams.get("AL EAST").isEmpty());
    assertTrue(leagueTeams.get("NL EAST").isEmpty());
  }

  @Test
  void sortTeamsInDivisionsTest() {
    Map<String, List<Team_v2>> leagueTeams = RegularSort_csv_v2.initializeLeagueTeams();
    List<Team_v2> alEast = leagueTeams.get("AL EAST");

    alEast.add(new Team_v2("AL EAST", "Team A", 90, 72, 0.556f));
    alEast.add(new Team_v2("AL EAST", "Team B", 95, 67, 0.586f));
    alEast.add(new Team_v2("AL EAST", "Team C", 85, 77, 0.525f));

    RegularSort_csv_v2.sortTeamsInDivisions(leagueTeams);
    assertEquals("Team B", alEast.get(0).getTeamName()); // Team B has the highest wins
    assertEquals("Team A", alEast.get(1).getTeamName()); // Team A 第2名
    assertEquals("Team C", alEast.get(2).getTeamName()); // Team C 第3名
  }

  @Test
  void processFileTest() throws IOException {
    // Mock data simulating the file input for two leagues with enough rows for processTeamData
    String testData = "\"AL EAST\", \"L\", \"W\", \"PCT\"\n"
        + "\"Team A\",90,72,0.556\n"
        + "\"Team B\",95,67,0.586\n"
        + "\"Team C\",80,82,0.494\n"
        + "\"Team D\",85,77,0.525\n"
        + "\"Team E\",88,74,0.543\n"
        + "\"NL WEST\", \"L\", \"W\", \"PCT\"\n"
        + "\"Team F\",91,71,0.562\n"
        + "\"Team G\",89,73,0.549\n"
        + "\"Team H\",87,75,0.537\n"
        + "\"Team I\",86,76,0.531\n"
        + "\"Team J\",84,78,0.519\n";

    BufferedReader br = new BufferedReader(new StringReader(testData));
    Map<String, List<Team_v2>> leagueTeams = RegularSort_csv_v2.initializeLeagueTeams();

    // Run the method under test
    RegularSort_csv_v2.processFile(br, leagueTeams);

    // Verify results
    List<Team_v2> alEastTeams = leagueTeams.get("AL EAST");
    List<Team_v2> nlWestTeams = leagueTeams.get("NL WEST");

    // Assertions
    assertEquals(5, alEastTeams.size(), "AL EAST should contain 5 teams.");
    assertEquals(5, nlWestTeams.size(), "NL WEST should contain 5 teams.");

    // Verify some specific details about the teams
    assertEquals("Team A", alEastTeams.get(0).getTeamName());
    assertEquals(90, alEastTeams.get(0).getWin());
    assertEquals(72, alEastTeams.get(0).getLose());

    assertEquals("Team F", nlWestTeams.get(0).getTeamName());
    assertEquals(91, nlWestTeams.get(0).getWin());
    assertEquals(71, nlWestTeams.get(0).getLose());
  }

  @Test
  void teamsInDivisionsTest() {
    // Create mock data for AL EAST division
    List<Team_v2> alEastTeams = new ArrayList<>();
    alEastTeams.add(new Team_v2("AL EAST", "Team C", 85, 77, 0.525f)); // lower wins
    alEastTeams.add(new Team_v2("AL EAST", "Team A", 95, 67, 0.586f)); // highest wins
    alEastTeams.add(new Team_v2("AL EAST", "Team B", 90, 72, 0.556f)); // middle wins

    // Create mock data for NL WEST division
    List<Team_v2> nlWestTeams = new ArrayList<>();
    nlWestTeams.add(new Team_v2("NL WEST", "Team X", 91, 71, 0.562f)); // highest wins
    nlWestTeams.add(new Team_v2("NL WEST", "Team Z", 87, 75, 0.537f)); // lower wins
    nlWestTeams.add(new Team_v2("NL WEST", "Team Y", 89, 73, 0.549f)); // middle wins

    // Initialize the map with these teams
    Map<String, List<Team_v2>> leagueTeams = new HashMap<>();
    leagueTeams.put("AL EAST", alEastTeams);
    leagueTeams.put("NL WEST", nlWestTeams);

    // Run the method under test
    RegularSort_csv_v2.sortTeamsInDivisions(leagueTeams);

    // Assertions to check if the teams are sorted in descending order by wins
    // AL EAST should be sorted as: Team A, Team B, Team C
    assertEquals("Team A", leagueTeams.get("AL EAST").get(0).getTeamName());
    assertEquals("Team B", leagueTeams.get("AL EAST").get(1).getTeamName());
    assertEquals("Team C", leagueTeams.get("AL EAST").get(2).getTeamName());

    // NL WEST should be sorted as: Team X, Team Y, Team Z
    assertEquals("Team X", leagueTeams.get("NL WEST").get(0).getTeamName());
    assertEquals("Team Y", leagueTeams.get("NL WEST").get(1).getTeamName());
    assertEquals("Team Z", leagueTeams.get("NL WEST").get(2).getTeamName());
  }

  @Test
  void prepareTop3AndLeagueListsTest() {
    // Set up test data for AL and NL divisions
    List<Team_v2> alEastTeams = new ArrayList<>();
    alEastTeams.add(new Team_v2("AL EAST", "Team A", 95, 67, 0.586f)); // Top 1
    alEastTeams.add(new Team_v2("AL EAST", "Team B", 90, 72, 0.556f)); // Rank 2
    alEastTeams.add(new Team_v2("AL EAST", "Team C", 85, 77, 0.525f)); // Rank 3
    alEastTeams.add(new Team_v2("AL EAST", "Team D", 80, 82, 0.494f)); // Rank 4

    List<Team_v2> nlWestTeams = new ArrayList<>();
    nlWestTeams.add(new Team_v2("NL WEST", "Team X", 91, 71, 0.562f)); // Top 1
    nlWestTeams.add(new Team_v2("NL WEST", "Team Y", 89, 73, 0.549f)); // Rank 2
    nlWestTeams.add(new Team_v2("NL WEST", "Team Z", 87, 75, 0.537f)); // Rank 3
    nlWestTeams.add(new Team_v2("NL WEST", "Team W", 84, 78, 0.519f)); // Rank 4

    // Initialize the map with these teams
    Map<String, List<Team_v2>> leagueTeams = new HashMap<>();
    leagueTeams.put("AL EAST", alEastTeams);
    leagueTeams.put("NL WEST", nlWestTeams);

    // Initialize empty lists to store results
    List<Team_v2> _ALtop3List = new ArrayList<>();
    List<Team_v2> _ALlist = new ArrayList<>();
    List<Team_v2> _NLtop3List = new ArrayList<>();
    List<Team_v2> _NLlist = new ArrayList<>();

    // Run the method under test
    RegularSort_csv_v2.prepareTop3AndLeagueLists(_ALtop3List, _ALlist, _NLtop3List, _NLlist, leagueTeams);

    // Verify that the top 3 and remaining teams are correctly added to each list
    assertEquals(1, _ALtop3List.size());
    assertEquals("Team A", _ALtop3List.get(0).getTeamName());
    assertEquals(3, _ALlist.size());
    assertEquals("Team B", _ALlist.get(0).getTeamName());
    assertEquals("Team C", _ALlist.get(1).getTeamName());
    assertEquals("Team D", _ALlist.get(2).getTeamName());

    assertEquals(1, _NLtop3List.size());
    assertEquals("Team X", _NLtop3List.get(0).getTeamName());
    assertEquals(3, _NLlist.size());
    assertEquals("Team Y", _NLlist.get(0).getTeamName());
    assertEquals("Team Z", _NLlist.get(1).getTeamName());
    assertEquals("Team W", _NLlist.get(2).getTeamName());
  }



  @Test
  void printScheduleTest() {
    // Create sample data for AL and NL leagues
    List<Team_v2> alTop3List = new ArrayList<>();
    alTop3List.add(new Team_v2("AL", "Top AL Team 1", 95, 67, 0.586f));
    alTop3List.add(new Team_v2("AL", "Top AL Team 2", 90, 72, 0.556f));
    alTop3List.add(new Team_v2("AL", "Top AL Team 3", 85, 77, 0.525f));

    List<Team_v2> alList = new ArrayList<>();
    alList.add(new Team_v2("AL", "AL Team 1", 80, 82, 0.494f));
    alList.add(new Team_v2("AL", "AL Team 2", 75, 87, 0.463f));
    alList.add(new Team_v2("AL", "AL Team 3", 70, 92, 0.432f));

    List<Team_v2> nlTop3List = new ArrayList<>();
    nlTop3List.add(new Team_v2("NL", "Top NL Team 1", 91, 71, 0.562f));
    nlTop3List.add(new Team_v2("NL", "Top NL Team 2", 89, 73, 0.549f));
    nlTop3List.add(new Team_v2("NL", "Top NL Team 3", 87, 75, 0.537f));

    List<Team_v2> nlList = new ArrayList<>();
    nlList.add(new Team_v2("NL", "NL Team 1", 84, 78, 0.519f));
    nlList.add(new Team_v2("NL", "NL Team 2", 82, 80, 0.506f));
    nlList.add(new Team_v2("NL", "NL Team 3", 80, 82, 0.494f));

    // Call the printSchedule method
    RegularSort_csv_v2.printSchedule(alTop3List, alList, nlTop3List, nlList);

    // Expected output string
    String expectedOutput = """
            (AMERICAN LEAGUE)
            AL Team 3               6 -----
            Top AL Team 3           3 ----- ? -----
            Top AL Team 2                   2 ----- ?
            AL Team 2               5 -----
            AL Team 1               4 ----- ? -----
            Top AL Team 1                   1 ----- ? ----- ?
            (NATIONAL LEAGUE)
            NL Team 3               6 -----
            Top NL Team 3           3 ----- ? -----
            Top NL Team 2                   2 ----- ?
            NL Team 2               5 -----
            NL Team 1               4 ----- ? -----
            Top NL Team 1                   1 ----- ? ----- ?
            """;

    // Compare captured output with expected output
    assertEquals(expectedOutput.trim(), outputStreamCaptor.toString().trim());
  }
}