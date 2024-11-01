package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RegularSortCSV_v2 {
    private static final Logger logger = Logger.getLogger(RegularSortCSV_v2.class.getName());

    public static void main(String[] args) {
        configureLogger();
        String filePath = "data/MLB_2024_regular.txt";

        TeamProcessor teamProcessor = new TeamProcessor();
        teamProcessor.processFile(filePath);

        displayAndSortTeams(teamProcessor);
        displaySchedule(teamProcessor);
    }

    private static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler("RegularSort_csv.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Log configuration error: " + e.getMessage());
        }
    }

    private static void displayAndSortTeams(TeamProcessor teamProcessor) {
        for (LeagueDivision division : LeagueDivision.values()) {
            List<Team> teamList = teamProcessor.getTeamList(division);
            System.out.println(division + " 排序前: " + teamList);
            Collections.sort(teamList, new TeamComparator()); // 排序
            System.out.println(division + " 排序後: " + teamList);
        }
    }

    private static void displaySchedule(TeamProcessor teamProcessor) {
        System.out.println("(AMERICAN LEAGUE)");
        teamProcessor.displayTop6Schedule(LeagueDivision.AMERICAN);
        System.out.println("(NATIONAL LEAGUE)");
        teamProcessor.displayTop6Schedule(LeagueDivision.NATIONAL);
    }
}

class TeamProcessor {
    private final List<Team> alTop3List = new ArrayList<>();
    private final List<Team> nlTop3List = new ArrayList<>();
    private final List<Team> alLeague = new ArrayList<>();
    private final List<Team> nlLeague = new ArrayList<>();

    public void processFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                processLine(line);
            }
        } catch (IOException e) {
            RegularSortCSV_v2.logger.log(Level.SEVERE, "File processing error: " + e.getMessage());
        }
    }

    private void processLine(String line) {
        line = line.replace("\"", "");
        String[] data = line.split(",");
        if (data.length < 4) return;

        String league = data[0];
        Team team = new Team(data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), Float.parseFloat(data[4]));

        switch (league) {
            case "AL EAST", "AL CENTRAL", "AL WEST" -> alLeague.add(team);
            case "NL EAST", "NL CENTRAL", "NL WEST" -> nlLeague.add(team);
        }
    }

    public List<Team> getTeamList(LeagueDivision division) {
        return switch (division) {
            case AL_EAST -> alLeague;
            case NL_EAST -> nlLeague;
            default -> new ArrayList<>();
        };
    }

    public void displayTop6Schedule(LeagueDivision division) {
        List<Team> top3List = division == LeagueDivision.AMERICAN ? alTop3List : nlTop3List;
        for (Team team : top3List) {
            System.out.printf("%-21s", team.getTeamName());
        }
    }
}

enum LeagueDivision {
    AL_EAST, AL_CENTRAL, AL_WEST, NL_EAST, NL_CENTRAL, NL_WEST, AMERICAN, NATIONAL
}

class Team {
    private String teamName;
    private int win;
    private int lose;
    private float pct;

    public Team(String teamName, int win, int lose, float pct) {
        this.teamName = teamName;
        this.win = win;
        this.lose = lose;
        this.pct = pct;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String toString() {
        return String.format("%s [W:%d, L:%d, Pct:%.3f]", teamName, win, lose, pct);
    }
}

class TeamComparator implements java.util.Comparator<Team> {
    @Override
    public int compare(Team t1, Team t2) {
        return Integer.compare(t2.getWin(), t1.getWin());
    }
}
