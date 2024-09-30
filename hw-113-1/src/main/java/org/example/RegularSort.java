package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
      List<Team> teamNLlist = new ArrayList<>();  // 建立陣列存放NL球隊
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

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到AL EAST的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("AL EAST");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamALEastlist.add(team);  // 放入 AL EAST 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("AL CENTER")) { // AL CENTER
            assert data[0].equals("AL CENTER"): "非 AL CENTER 區";

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到AL CENTER的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("AL CENTER");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamALCenterlist.add(team);  // 放入 AL CENTER 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("AL WEST")) { // AL WEST
            assert data[0].equals("AL WEST"): "非 AL WEST 區";

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到AL CENTER的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("AL WEST");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamALWestlist.add(team);  // 放入 AL WEST 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL EAST")) { // NL EAST
            assert data[0].equals("NL EAST"): "非 NL EAST 區";

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到AL CENTER的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("NL EAST");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamNLEastlist.add(team);  // 放入 NL EAST 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL CENTER")) { // NL CENTER
            assert data[0].equals("NL CENTER"): "非 NL CENTER 區";

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到NL CENTER的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("NL CENTER");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamNLCenterlist.add(team);  // 放入 NL CENTER 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine(); // 跳到下一行

          } else if (data[0].equals("NL WEST")){ // NL WEST
            assert data[0].equals("NL WEST"): "非 NL WEST 區";

            for (int i = 0; i < 5; i++) {
              line = br.readLine(); // 跳到NL WEST的下一行
              data = line.split(",");
              Team team = new Team();

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("NL WEST");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamNLWestlist.add(team);  // 放入 NL WEST 陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()
          } // end of if/else
        } // end of while()

        System.out.println("AL EAST");
        for (Team team: teamALEastlist) {
          System.out.print(team + "\t");
        }

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
} // end of RegularSort
