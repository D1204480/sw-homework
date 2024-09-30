package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RegularSort {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(LoggingFile.class.getName());

  public static void main(String[] args) {

    try {
      // 設定 FileHandler，將日誌寫入檔案 "app.log"
      FileHandler fileHandler = new FileHandler("app.log", true); // true 表示追加到文件中
      fileHandler.setFormatter(new SimpleFormatter()); // 設定格式為簡單格式
      logger.addHandler(fileHandler);
//      logger.info("這是一條 INFO 等級的日誌訊息");

      String fileName = "MLB_2023_regular.txt";  // 要打開的檔案名稱
      List<Team> teamALlist = new ArrayList<>();  // 建立陣列存放AL球隊
      List<Team> teamALtop3list = new ArrayList<>();  // 建立陣列存放AL球隊
      List<Team> teamNLlist = new ArrayList<>();  // 建立陣列存放NL球隊
      List<Team> teamNLtop3list = new ArrayList<>();  // 建立陣列存放AL球隊
      List<Team> teamALEastlist = new ArrayList<>();  // 建立陣列存放AL East球隊
      List<Team> teamALCenterlist = new ArrayList<>();  // 建立陣列存放AL Center球隊
      List<Team> teamALWestlist = new ArrayList<>();  // 建立陣列存放AL West球隊
      List<Team> teamNLEastlist = new ArrayList<>();  // 建立陣列存放NL East球隊
      List<Team> teamNLCenterlist = new ArrayList<>();  // 建立陣列存放NL Center球隊
      List<Team> teamNLWestlist = new ArrayList<>();  // 建立陣列存放NL West球隊

      // 開檔
      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line = br.readLine();
        while (line != null) {  // 讀每一row直到沒資料
          String[] data = line.split(",");

          if (data.length < 4) {  // 跳過不足4個欄位的資料
            continue;
          }
          assert data.length >= 4: "欄位數不足4欄";

          /*
          用if/else區分League:
          AL EAST, AL CENTER, AL WEST
          NL EAST, NL CENTER, NL WEST
           */
          if (data[0].equals("AL EAST")) { // AL EAST
            assert data[0].equals("AL EAST"): "非 AL EAST 區";

            processTeams(br, teamALEastlist, "AL EAST");
            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("AL CENTER")) { // AL CENTER
            assert data[0].equals("AL CENTER"): "非 AL CENTER 區";

            processTeams(br, teamALCenterlist, "AL CENTER");
            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("AL WEST")) { // AL WEST
            assert data[0].equals("AL WEST"): "非 AL WEST 區";

            processTeams(br, teamALWestlist, "AL WEST");
            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL EAST")) { // NL EAST
            assert data[0].equals("NL EAST"): "非 NL EAST 區";

            processTeams(br, teamNLEastlist, "NL EAST");
            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL CENTER")) { // NL CENTER
            assert data[0].equals("NL CENTER"): "非 NL CENTER 區";

            processTeams(br, teamNLCenterlist, "NL CENTER");
            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL WEST")){ // NL WEST
            assert data[0].equals("NL WEST"): "非 NL WEST 區";

            processTeams(br, teamNLWestlist, "NL WEST");
            line = br.readLine(); // 跳到下一行
          } // end of if/else
        } // end of while()

        System.out.println("AL EAST 排序前: ");
        for (Team team: teamALEastlist) {
          System.out.println(team);
        }

        System.out.println("AL CENTER 排序前: ");
        for (Team team: teamALCenterlist) {
          System.out.println(team);
        }

        System.out.println("AL WEST 排序前: ");
        for (Team team: teamALWestlist) {
          System.out.println(team);
        }

        System.out.println("NL EAST 排序前: ");
        for (Team team: teamNLEastlist) {
          System.out.println(team);
        }

        System.out.println("NL CENTER 排序前: ");
        for (Team team: teamNLCenterlist) {
          System.out.println(team);
        }

        System.out.println("NL WEST 排序前: ");
        for (Team team: teamNLWestlist) {
          System.out.println(team);
        }

        //排序 (win多至少)
        Collections.sort(teamALEastlist, new Team());
        Collections.sort(teamALCenterlist, new Team());
        Collections.sort(teamALWestlist, new Team());
        Collections.sort(teamNLEastlist, new Team());
        Collections.sort(teamNLCenterlist, new Team());
        Collections.sort(teamNLWestlist, new Team());


        System.out.println("排序後: ");
        for (int i = 0; i < 5; i++) {
          System.out.println(teamALEastlist.get(i));
        }

        /*
        各區冠軍進入top3陣列, 其餘隊伍分別進入AL/NL陣列
         */
        processTop6(teamALtop3list, teamALlist, teamALEastlist);
        processTop6(teamALtop3list, teamALlist, teamALCenterlist);
        processTop6(teamALtop3list, teamALlist, teamALWestlist);
        processTop6(teamNLtop3list, teamNLlist, teamNLEastlist);
        processTop6(teamNLtop3list, teamNLlist, teamNLCenterlist);
        processTop6(teamNLtop3list, teamNLlist, teamNLWestlist);

        // 排序 (win由多而少)
        Collections.sort(teamALtop3list, new Team());
        Collections.sort(teamALlist, new Team());
        Collections.sort(teamNLtop3list, new Team());
        Collections.sort(teamNLlist, new Team());

        // 取得AL聯盟前3名
        Team ALp1 = null; Team ALp2 = null; Team ALp3 = null;
        for (int i = 0; i < 1; i++) {
          ALp1 = teamALtop3list.get(0);
          ALp2 = teamALtop3list.get(1);
          ALp3 = teamALtop3list.get(2);
        }

        // 取得AL聯盟4~6名
        Team ALp4 = null; Team ALp5 = null; Team ALp6 = null;
        for (int i = 0; i < 1; i++) {
          ALp4 = teamALlist.get(0);
          ALp5 = teamALlist.get(1);
          ALp6 = teamALlist.get(2);
        }

        // 取得NL聯盟前3名
        Team NLp1 = null; Team NLp2 = null; Team NLp3 = null;
        for (int i = 0; i < 1; i++) {
          NLp1 = teamALtop3list.get(0);
          NLp2 = teamALtop3list.get(1);
          NLp3 = teamALtop3list.get(2);
        }

        // 取得NL聯盟4~6名
        Team NLp4 = null; Team NLp5 = null; Team NLp6 = null;
        for (int i = 0; i < 1; i++) {
          NLp4 = teamALlist.get(0);
          NLp5 = teamALlist.get(1);
          NLp6 = teamALlist.get(2);
        }

//        System.out.println( ALp1);
//        System.out.println(ALp2);
//        System.out.println(ALp3);
//        System.out.println(ALp4);
//        System.out.println(ALp5);
//        System.out.println(ALp6);

        System.out.println();
        System.out.println("排程表: ");
        // 輸出格式
        System.out.println("(AMERICAN LEAGE)" );
        System.out.printf("%-17s 6 ----- \n", ALp6.getTeamName());
        System.out.printf("%-17s 3 ----- ? -----\n", ALp3.getTeamName());
        System.out.printf("%-25s 2 ----- ?\n", ALp2.getTeamName());
        System.out.printf("%-17s 5 -----\n", ALp5.getTeamName());
        System.out.printf("%-17s 4 ----- ? -----\n", ALp4.getTeamName());
        System.out.printf("%-25s 1 ----- ? ----- ?\n", ALp1.getTeamName());
        System.out.printf("%-43s---- ?\n",' ');
        System.out.printf("%-17s 6 ----- ? ----- ? ----- ?\n", NLp6.getTeamName());
        System.out.printf("%-17s 3 -----\n", NLp3.getTeamName());
        System.out.printf("%-25s 2 ----\n", NLp2.getTeamName());
        System.out.printf("%-17s 5 ----- ? ----- ?\n", NLp5.getTeamName());
        System.out.printf("%-17s 4 -----\n", NLp4.getTeamName());
        System.out.printf("%-25s 1 -----\n", NLp1.getTeamName());
        System.out.println("(NATIONAL LEAGE)");

      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生開檔IO錯誤: " + e.getMessage());
        throw new RuntimeException(e.getMessage());

      } // end of BufferReader

    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      e.printStackTrace();

    } // end of log日誌
  } // end of main()

  private static void processTeams(BufferedReader br, List<Team> teamList, String league) throws IOException {
    for (int i = 0; i < 5; i++) {
      String line = br.readLine(); // 跳到標頭(例如:NL WEST)的下一行
      String[] data = line.split(",");
      Team team = new Team();

      try {
        String teamName = data[0];
        int win = Integer.parseInt(data[1]);
        int lose = Integer.parseInt(data[2]);
        float pct = Float.parseFloat(data[3]);

        team.setLeague(league);
        team.setTeamName(teamName);
        team.setWin(win);
        team.setLose(lose);
        team.setPct(pct);

        teamList.add(team);  // 放入陣列

      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } // end of for()
  } // end of processTeams

  private static void processTop6(List<Team> top3List, List<Team> league, List<Team> teamList) {
    for (int i = 0; i < 1; i++) {
//          System.out.println(teamALEastlist.get(i));
      Team p1 = null;
      Team p2 = null;
      Team p3 = null;
      Team p4 = null;
      Team p5 = null;

      p1 = teamList.get(0);
      p2 = teamList.get(1);
      p3 = teamList.get(2);
      p4 = teamList.get(3);
      p5 = teamList.get(4);

      top3List.add(p1);  // 第一名進入top3陣列
      league.add(p2);  // 其餘進入AL/NL聯盟陣列
      league.add(p3);
      league.add(p4);
      league.add(p5);
    }
  } // end of processTop6

} // end of RegularSort
