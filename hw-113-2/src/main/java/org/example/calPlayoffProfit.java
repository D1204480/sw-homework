package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class calPlayoffProfit {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(calPlayoffProfit.class.getName());

  public static void main(String[] args) {
    try {
      // 設定 FileHandler，將日誌寫入檔案 "calProfit.log"
      FileHandler fileHandler = new FileHandler("calProfit.log", true); // true 表示追加到文件中
      fileHandler.setFormatter(new SimpleFormatter()); // 設定格式為簡單格式
      logger.addHandler(fileHandler);

      // 讀取的檔案路徑
      String courtFileName = "court_info.txt";  // 各隊球場資訊
      String courtFilePath = "data/" + courtFileName;
      String wildCardTeamFile = "MLB_2024_wildCard.csv";  // 季後賽名單(AL/NL前6名)
      String wildCardTeamPath = "data/" + wildCardTeamFile;

      // 建立Map合併存放court,wildCard的資料
      Map<String, Team> teamDataMap = new HashMap<>();

      // read and store data to Map from wildCard.csv
      try (BufferedReader br = new BufferedReader(new FileReader(wildCardTeamPath))) {
        String line;
        br.readLine();  // 跳過表頭
        while ((line = br.readLine()) != null) {
          String[] wildCardRow = line.split(",");  // 以逗點分割

          if (wildCardRow.length < 6) {  // wildCard的欄位有6欄
            continue;
          }
          assert wildCardRow.length >= 6 : "row的欄位數不足6欄";

          String league = wildCardRow[0];
          String teamName = wildCardRow[1];
          int win = Integer.parseInt(wildCardRow[2]);
          int lose = Integer.parseInt(wildCardRow[3]);
          float pct = Float.parseFloat(wildCardRow[4]);
          int position = Integer.parseInt(wildCardRow[5]);

          Team team = new Team();  // 建立team物件
          team.setLeague(league);
          team.setTeamName(teamName);
          team.setWin(win);
          team.setLose(lose);
          team.setPct(pct);
          team.setPosition(position);

          // store in the Map
          teamDataMap.put(teamName, team);

        }
      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生讀檔IO找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);
      } // end of try(read wildCard)


      // read and store to Map from court.csv
      try (BufferedReader br = new BufferedReader(new FileReader(courtFilePath))) {
        String line;
        br.readLine(); // 跳過表頭
        while ((line = br.readLine()) != null) {
          String[] courtRow = line.split(",");  // 以逗號分割
          
        }



      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生讀檔IO找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);
      } // end of try(read wildCard)


    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      throw new RuntimeException(e);
    } // end of try(logger)
  } // end of main
} // end of calPlayoffProfit
