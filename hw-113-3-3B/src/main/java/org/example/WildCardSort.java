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

public class WildCardSort {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(WildCardSort.class.getName());

  public static void main(String[] args) {
    try {
      // 設定 FileHandler，將日誌寫入檔案 "app.log"
      FileHandler fileHandler = new FileHandler("app.log", true); // true 表示追加到文件中
      fileHandler.setFormatter(new SimpleFormatter()); // 設定格式為簡單格式
      logger.addHandler(fileHandler);
//      logger.info("這是一條 INFO 等級的日誌訊息");


      String fileName = "MLB_2024_wildCard.txt";  // 要打開的檔案名稱
      List<Team> teamALlist = new ArrayList<>();  // 建立陣列存放AL球隊
      List<Team> teamNLlist = new ArrayList<>();  // 建立陣列存放NL球隊

      // 開啟檔案
      try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line = br.readLine();
        while (line != null) {  // row有資料時
          String[] data = line.split(",");  // 用逗點分開每一欄位
          if (data.length != 15) {
            continue;
          }
          assert (data.length == 15) : "row不等於15欄資料";  // assert 欄位數量錯誤

          /*
          用 if/else 把球隊分為AL區跟NL區
           */
          if (line.startsWith("AL")) {  // 如果是AL聯盟
            assert data[0].startsWith("AL") : "字首開頭不是AL";

            for (int i = 0; i < 3; i++) {  // 每分群(LEADER, WILD CARD)各有3個球隊
              line = br.readLine();// 跳到下一行
              data = line.split(",");  // 用逗點分開每一欄位
              Team team = new Team();  // 建立物件

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("AL");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamALlist.add(team);  // 放入陣列

              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            }
            line = br.readLine(); //跳到下一行

          } else if (line.startsWith("NL")) {  // 如果是NL聯盟
            assert data[0].startsWith("NL") : "字首開頭不是NL";

            for (int i = 0; i < 3; i++) {   // 每分群(LEADER, WILD CARD)各有3個球隊
              line = br.readLine();  // 跳到下一行
              data = line.split(",");  // 用逗點分開每一欄位
              Team team = new Team();  // 建立物件

              try {
                String teamName = data[0];
                int win = Integer.parseInt(data[1]);
                int lose = Integer.parseInt(data[2]);
                float pct = Float.parseFloat(data[3]);

                team.setLeague("NL");
                team.setTeamName(teamName);
                team.setWin(win);
                team.setLose(lose);
                team.setPct(pct);

                teamNLlist.add(team);  // 放入陣列
              } catch (NumberFormatException e) {
                e.printStackTrace();
              }
            } // end of for()

            line = br.readLine();  // 跳下一行

          } // end of else-if
        } // end of while

        assert teamALlist.size() == 6: "AL球隊非6隊";
        assert teamNLlist.size() == 6: "NL球隊非6隊";
        System.out.println("AL排序前: ");
        for (Team team: teamALlist) {
          System.out.println(team + "\t");
        }

        System.out.println("NL排序前: ");
        for (Team team: teamNLlist) {
          System.out.println(team + "\t");
        }

        Collections.sort(teamALlist, new Team());  // win由大而小排序
        Collections.sort(teamNLlist, new Team());  // win由大而小排序

        Team AL1 = null, AL2 = null, AL3 = null, AL4 = null, AL5 = null, AL6 = null;
        System.out.println("AL排序後: ");
        for (int i = 0; i < 6; i++) {
//          System.out.println(teamALlist.get(i) + "\t");
          AL1 = teamALlist.get(0);
          AL2 = teamALlist.get(1);
          AL3 = teamALlist.get(2);
          AL4 = teamALlist.get(3);
          AL5 = teamALlist.get(4);
          AL6 = teamALlist.get(5);
        }
        System.out.println(AL1.getTeamName());
        System.out.println(AL2.getTeamName());

        Team NL1 = null, NL2 = null, NL3 = null, NL4 = null, NL5 = null, NL6 = null;
        System.out.println("NL排序後: ");
        for (int i = 0; i < 6; i++) {
//          System.out.println(teamNLlist.get(i) + "\t");
          NL1 = teamNLlist.get(0);
          NL2 = teamNLlist.get(1);
          NL3 = teamNLlist.get(2);
          NL4 = teamNLlist.get(3);
          NL5 = teamNLlist.get(4);
          NL6 = teamNLlist.get(5);
        }

        System.out.println(NL1.getTeamName());
        System.out.println(NL2.getTeamName());

        System.out.println();
        System.out.println("排程表: ");
        // 輸出格式
        System.out.println("(AMERICAN LEAGE)\n" );
        System.out.println( AL6.getTeamName() + " 6 -----");
        System.out.println(AL3.getTeamName() + " 3 ----- ? -----");
        System.out.println(AL2.getTeamName() + " 2 ----- ?");
        System.out.println(AL5.getTeamName() + " 5 -----");
        System.out.println(AL4.getTeamName() + " 4 ----- ? -----");
        System.out.println(AL1.getTeamName() + " 1 ----- ? ----- ?");
        System.out.println("---- ?");
        System.out.println(NL6.getTeamName() + " 6 ----- ? ----- ? ----- ?");
        System.out.println(NL3.getTeamName() + " 3 -----");
        System.out.println(NL2.getTeamName() + " 2 ----");
        System.out.println(NL5.getTeamName() + " 5 ----- ? ----- ?");
        System.out.println(NL4.getTeamName() + " 4 -----");
        System.out.println(NL1.getTeamName() + " 1 -----");
        System.out.println("(NATIONAL LEAGE)");

      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生開檔IO錯誤: " + e.getMessage());
        throw new RuntimeException(e.getMessage());
      }

    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      e.printStackTrace();

    } // end of log日誌
  } // end of main()
} // end of WildCardSort
