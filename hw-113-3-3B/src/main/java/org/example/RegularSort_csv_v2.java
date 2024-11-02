package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RegularSort_csv_v2 {
  private static final Logger logger = Logger.getLogger(RegularSort_csv_v2.class.getName());

  public static void main(String[] args) {
    try {
      setupLogger();
      String filePath = "data/MLB_2024_regular.txt";
      List<Team_v2> _ALtop3List = new ArrayList<>();
      List<Team_v2> _ALlist = new ArrayList<>();
      List<Team_v2> _NLtop3List = new ArrayList<>();
      List<Team_v2> _NLlist = new ArrayList<>();

      Map<String, List<Team_v2>> leagueTeams = initializeLeagueTeams();

      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        logger.info("Opening file: " + filePath);
        processFile(br, leagueTeams);
        sortTeamsInDivisions(leagueTeams);
        prepareTop3AndLeagueLists(_ALtop3List, _ALlist, _NLtop3List, _NLlist, leagueTeams);
        printSchedule(_ALtop3List, _ALlist, _NLtop3List, _NLlist);
        logger.info("Program completed successfully.");
      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "File not found: " + e.getMessage());
      } catch (IOException e) {
        logger.log(Level.SEVERE, "I/O error: " + e.getMessage());
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Log error: " + e.getMessage());
    }
  }

  private static void setupLogger() throws IOException {
    FileHandler fileHandler = new FileHandler("RegularSortCsv.log", true);
    fileHandler.setFormatter(new SimpleFormatter());
    logger.addHandler(fileHandler);
  }

  private static Map<String, List<Team_v2>> initializeLeagueTeams() {
    return Map.of(
        "AL EAST", new ArrayList<>(),
        "AL CENTRAL", new ArrayList<>(),
        "AL WEST", new ArrayList<>(),
        "NL EAST", new ArrayList<>(),
        "NL CENTRAL", new ArrayList<>(),
        "NL WEST", new ArrayList<>()
    );
  }

  private static void processFile(BufferedReader br, Map<String, List<Team_v2>> leagueTeams) throws IOException {
    String line;
    while ((line = br.readLine()) != null) {
      line = line.replace("\"", "");
      String[] data = line.split(",");
      if (data.length < 4) continue;
      String league = data[0];
      if (leagueTeams.containsKey(league)) {
        List<Team_v2> teamList = leagueTeams.get(league);
        processTeamData(br, teamList, league);
      }
    }
  }

  private static void processTeamData(BufferedReader br, List<Team_v2> teamList, String league) throws IOException {
    for (int i = 0; i < 5; i++) {
      String[] data = br.readLine().replace("\"", "").split(",");
      if (data.length < 4) continue;
      Team_v2 team = new Team_v2(league, data[0], Integer.parseInt(data[1]), Integer.parseInt(data[2]), Float.parseFloat(data[3]));
      teamList.add(team);
    }
  }

  private static void sortTeamsInDivisions(Map<String, List<Team_v2>> leagueTeams) {
    leagueTeams.values().forEach(Collections::sort);
  }

  private static void prepareTop3AndLeagueLists(List<Team_v2> _ALtop3List, List<Team_v2> _ALlist,
                                                List<Team_v2> _NLtop3List, List<Team_v2> _NLlist,
                                                Map<String, List<Team_v2>> leagueTeams) {
    leagueTeams.forEach((league, teams) -> {
      if (league.startsWith("AL")) {
        _ALtop3List.add(teams.get(0));
        _ALlist.addAll(teams.subList(1, teams.size()));
      } else if (league.startsWith("NL")) {
        _NLtop3List.add(teams.get(0));
        _NLlist.addAll(teams.subList(1, teams.size()));
      }
    });
  }

  private static void printSchedule(List<Team_v2> _ALtop3List, List<Team_v2> _ALlist,
                                    List<Team_v2> _NLtop3List, List<Team_v2> _NLlist) {
    System.out.println("(AMERICAN LEAGUE)");
    printTeamRanks(_ALtop3List, _ALlist);
    System.out.println("(NATIONAL LEAGUE)");
    printTeamRanks(_NLtop3List, _NLlist);
  }

  private static void printTeamRanks(List<Team_v2> top3List, List<Team_v2> leagueList) {
    System.out.printf("%-21s 6 ----- \n", leagueList.get(2).getTeamName());
    System.out.printf("%-21s 3 ----- ? -----\n", top3List.get(2).getTeamName());
    System.out.printf("%-29s 2 ----- ?\n", top3List.get(1).getTeamName());
    System.out.printf("%-21s 5 -----\n", leagueList.get(1).getTeamName());
    System.out.printf("%-21s 4 ----- ? -----\n", leagueList.get(0).getTeamName());
    System.out.printf("%-29s 1 ----- ? ----- ?\n", top3List.get(0).getTeamName());
  }
} // end of RegularSort
