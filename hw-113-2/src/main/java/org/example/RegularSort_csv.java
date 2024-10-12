package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RegularSort_csv {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(RegularSort_csv.class.getName());

  public static void main(String[] args) {

    try {
      // 設定 FileHandler，將日誌寫入檔案 "RegularSort_csv.log"
      FileHandler fileHandler = new FileHandler("RegularSort_csv.log", true); // true 表示追加到文件中
      fileHandler.setFormatter(new SimpleFormatter()); // 設定格式為簡單格式
      logger.addHandler(fileHandler);
//      logger.info("這是一條 INFO 等級的日誌訊息");

      String fileName = "MLB_2024_regular.csv";  // 要打開的檔案名稱
      String filePath = "data/" + fileName;
      String outputFile = "MLB_2024_wildCard.csv"; // 寫出去的檔名
      String outputPath = "data/" + outputFile;

      List<Team> _ALlist = new ArrayList<>();  // 建立陣列存放AL球隊
      List<Team> _ALtop3List = new ArrayList<>();  // 建立陣列存放AL top3球隊
      List<Team> _NLlist = new ArrayList<>();  // 建立陣列存放NL球隊
      List<Team> _NLtop3List = new ArrayList<>();  // 建立陣列存放AL top3球隊
      List<Team> team_ALEastList = new ArrayList<>();  // 建立陣列存放AL East球隊
      List<Team> team_ALCentralList = new ArrayList<>();  // 建立陣列存放AL Center球隊
      List<Team> team_ALWestList = new ArrayList<>();  // 建立陣列存放AL West球隊
      List<Team> team_NLEastList = new ArrayList<>();  // 建立陣列存放NL East球隊
      List<Team> team_NLCentralList = new ArrayList<>();  // 建立陣列存放NL Center球隊
      List<Team> team_NLWestList = new ArrayList<>();  // 建立陣列存放NL West球隊

      // 開檔案
      try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        logger.info("嘗試開啟檔案: " + filePath);

        int count = 0;
        String line = "";
        while ((line = br.readLine()) != null) {  // 讀每一row直到沒資料
          assert line != null : "讀不到資料";

          count++;
          if (count == 1) {  // 用來跳過第一行的表頭
            continue;
          }

          line = line.replace("\"", "");  // 去除csv檔, 每行資料前後的""
          String[] data = line.split(",");

          if (data.length < 4) {  // 跳過不足4個欄位的資料
            continue;
          }
          assert data.length >= 4 : "欄位數不足4欄，資料有誤";

          /*
          1. 用if/else區分League:
             AL EAST, AL CENTER, AL WEST
             NL EAST, NL CENTER, NL WEST
          2. 分別放入該區域的陣列
           */
          if (data[0].equals("AL EAST")) { // AL EAST
            assert data[0].equals("AL EAST") : "非 AL EAST 區";
            processTeams(br, team_ALEastList, "AL EAST");

          } else if (data[0].equals("AL CENTRAL")) { // AL CENTRAL
            assert data[0].equals("AL CENTRAL") : "非 AL CENTRAL 區";
            processTeams(br, team_ALCentralList, "AL CENTRAL");

          } else if (data[0].equals("AL WEST")) { // AL WEST
            assert data[0].equals("AL WEST") : "非 AL WEST 區";
            processTeams(br, team_ALWestList, "AL WEST");

          } else if (data[0].equals("NL EAST")) { // NL EAST
            assert data[0].equals("NL EAST") : "非 NL EAST 區";
            processTeams(br, team_NLEastList, "NL EAST");

          } else if (data[0].equals("NL CENTRAL")) { // NL CENTRAL
            assert data[0].equals("NL CENTRAL") : "非 NL CENTRAL 區";
            processTeams(br, team_NLCentralList, "NL CENTRAL");

          } else if (data[0].equals("NL WEST")) { // NL WEST
            assert data[0].equals("NL WEST") : "非 NL WEST 區";
            processTeams(br, team_NLWestList, "NL WEST");

          } // end of if/else
        } // end of while()

        System.out.println("AL EAST 排序前: ");
        for (Team team : team_ALEastList) {
          System.out.println(team);
        }

        System.out.println("AL CENTRAL 排序前: ");
        for (Team team : team_ALCentralList) {
          System.out.println(team);
        }

        System.out.println("AL WEST 排序前: ");
        for (Team team : team_ALWestList) {
          System.out.println(team);
        }

        System.out.println("NL EAST 排序前: ");
        for (Team team : team_NLEastList) {
          System.out.println(team);
        }

        System.out.println("NL CENTRAL 排序前: ");
        for (Team team : team_NLCentralList) {
          System.out.println(team);
        }

        System.out.println("NL WEST 排序前: ");
        for (Team team : team_NLWestList) {
          System.out.println(team);
        }

        // 6區各隊伍做排序
        //排序 (win多至少)
        Collections.sort(team_ALEastList, new Team());
        Collections.sort(team_ALCentralList, new Team());
        Collections.sort(team_ALWestList, new Team());
        Collections.sort(team_NLEastList, new Team());
        Collections.sort(team_NLCentralList, new Team());
        Collections.sort(team_NLWestList, new Team());


        System.out.println("排序後: ");
        for (int i = 0; i < 5; i++) {
          System.out.println(team_ALEastList.get(i));
        }

        /*
        AL/NL各區冠軍分別進入該區top3陣列, 其餘隊伍分別進入AL/NL陣列
         */
        divideTop3(_ALtop3List, _ALlist, team_ALEastList);
        divideTop3(_ALtop3List, _ALlist, team_ALCentralList);
        divideTop3(_ALtop3List, _ALlist, team_ALWestList);
        divideTop3(_NLtop3List, _NLlist, team_NLEastList);
        divideTop3(_NLtop3List, _NLlist, team_NLCentralList);
        divideTop3(_NLtop3List, _NLlist, team_NLWestList);

        // 分別把前3名隊伍做排序、第4~12名隊伍做排序
        // 排序 (win由多而少)
        Collections.sort(_ALtop3List, new Team());
        Collections.sort(_ALlist, new Team());
        Collections.sort(_NLtop3List, new Team());
        Collections.sort(_NLlist, new Team());

        // 取得AL聯盟1~3名
        Team ALp1 = null;
        Team ALp2 = null;
        Team ALp3 = null;
        for (int i = 0; i < 1; i++) {
          ALp1 = _ALtop3List.get(0);
          ALp2 = _ALtop3List.get(1);
          ALp3 = _ALtop3List.get(2);
        }

        // 取得AL聯盟4~6名
        Team ALp4 = null;
        Team ALp5 = null;
        Team ALp6 = null;
        for (int i = 0; i < 1; i++) {
          ALp4 = _ALlist.get(0);
          ALp5 = _ALlist.get(1);
          ALp6 = _ALlist.get(2);
        }

        // 取得NL聯盟1~3名
        Team NLp1 = null;
        Team NLp2 = null;
        Team NLp3 = null;
        for (int i = 0; i < 1; i++) {
          NLp1 = _NLtop3List.get(0);
          NLp2 = _NLtop3List.get(1);
          NLp3 = _NLtop3List.get(2);
        }

        // 取得NL聯盟4~6名
        Team NLp4 = null;
        Team NLp5 = null;
        Team NLp6 = null;
        for (int i = 0; i < 1; i++) {
          NLp4 = _NLlist.get(0);
          NLp5 = _NLlist.get(1);
          NLp6 = _NLlist.get(2);
        }

//        System.out.println(ALp1);
//        System.out.println(ALp2);
//        System.out.println(ALp3);
//        System.out.println(ALp4);
//        System.out.println(ALp5);
//        System.out.println(ALp6);

        System.out.println();
        System.out.println("排程表: ");
        // 輸出格式
        System.out.println("(AMERICAN LEAGUE)");
        System.out.printf("%-21s 6 ----- \n", ALp6.getTeamName());
        System.out.printf("%-21s 3 ----- ? -----\n", ALp3.getTeamName());
        System.out.printf("%-29s 2 ----- ?\n", ALp2.getTeamName());
        System.out.printf("%-21s 5 -----\n", ALp5.getTeamName());
        System.out.printf("%-21s 4 ----- ? -----\n", ALp4.getTeamName());
        System.out.printf("%-29s 1 ----- ? ----- ?\n", ALp1.getTeamName());
        System.out.printf("%-47s----- ?\n", ' ');
        System.out.printf("%-21s 6 ----- ? ----- ? ----- ?\n", NLp6.getTeamName());
        System.out.printf("%-21s 3 -----\n", NLp3.getTeamName());
        System.out.printf("%-29s 2 -----\n", NLp2.getTeamName());
        System.out.printf("%-21s 5 ----- ? ----- ?\n", NLp5.getTeamName());
        System.out.printf("%-21s 4 -----\n", NLp4.getTeamName());
        System.out.printf("%-29s 1 -----\n", NLp1.getTeamName());
        System.out.println("(NATIONAL LEAGUE)");

        // 集合AL/NL進入wildCard名單(AL/NL聯盟前6名)
        List<Team> wildCardTeams = Arrays.asList(
            _ALtop3List.get(0), _ALtop3List.get(1), _ALtop3List.get(2),
            _ALlist.get(0), _ALlist.get(1), _ALlist.get(2),
            _NLtop3List.get(0), _NLtop3List.get(1), _NLtop3List.get(2),
            _NLlist.get(0), _NLlist.get(1), _NLlist.get(2)
        );

        // 把AL/NL前6隊伍寫出檔案
        writeFile(outputPath, wildCardTeams);

        logger.info("程式正確執行結束");

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
      line = line.replace("\"", "");  // 去除每行的前後""
      String[] data = line.split(",");
      Team team = new Team();

      try {
        String teamName = data[0];
        assert data[1].matches("\\d+") : "讀入的W非數字";
        int win = Integer.parseInt(data[1]);

        assert data[2].matches("\\d+") : "讀入的L非數字";
        int lose = Integer.parseInt(data[2]);

        assert data[2].matches(".\\d+") : "讀入的PCT非浮點數";
        float pct = Float.parseFloat(data[3]);


        team.setLeague(league);
        team.setTeamName(teamName);
        team.setWin(win);
        team.setLose(lose);
        team.setPct(pct);

        teamList.add(team);  // 放入陣列

      } catch (NumberFormatException e) {
        logger.log(Level.SEVERE, "發生處理數字格式錯誤: " + e.getMessage());
        e.printStackTrace();
      }
    } // end of for()
    assert teamList.size() == 5 : "每個區應有5個隊伍";
  } // end of processTeams


  private static void divideTop3(List<Team> top3List, List<Team> league, List<Team> teamList) {
    for (int i = 0; i < 1; i++) {
//          System.out.println(teamALEastlist.get(i));
      assert teamList.size() == 5 : "每區應有5個隊伍";
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

  private static void writeFile(String outputPath, List<Team> teamList) {
    try (FileWriter fileWriter = new FileWriter(outputPath)) {
      String header = "League, Team, W, L, PCT, Position";  // 標題
      fileWriter.write(header);
      fileWriter.write("\n"); // 跳一行

      int count = 0;  // position 排名順位
      for (Team team: teamList) {
        fileWriter.write(generateOutputString(team));
        count++;
        if (team.getLeague().startsWith("AL")) {
          fileWriter.write(String.valueOf(count));
          fileWriter.write("\n");

        } else {
          fileWriter.write(String.valueOf(count - 6));  // NL聯盟排在AL聯盟後面, 所以要扣掉前6位, 才會從1開始
          fileWriter.write("\n");
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String generateOutputString(Team team) {
    String record = team.getLeague() + ","
        + team.getTeamName() + ","
        + team.getWin() + ","
        + team.getLose() + ","
        + team.getPct() + ",";
    return record;
  }

} // end of RegularSort
