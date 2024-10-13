package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class calProfit {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(calProfit.class.getName());

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

      // 讀取後的資料放入陣列
      List<String[]> dataCourt = readFile(courtFilePath);
      assert dataCourt.size() == 12: "dataCourt檔案, 資料不足11個球場";
      List<String[]> dataWildCard = readFile(wildCardTeamPath);
      assert dataWildCard.size() == 13: "wildCard檔案, 資料不足12球隊";

      // 合併wildCard, court資料
      Map<String, String[]> teamDataMap = mergeTeamData(dataWildCard, dataCourt);

      // 印出teamData結果查看
      for (Map.Entry<String, String[]> entry: teamDataMap.entrySet()) {
        System.out.print("Team: " + entry.getKey() + "\t");
        System.out.println("Data: " + String.join(",", entry.getValue()));
      }

      /* 計算分潤:
      1. P1,P2最佳: LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P1,P2最差: LDS(2主+1客)
      2. P3,P4最佳: WILD(3主) + LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P3,P4最差: LDS(2主)
      3. P5,P6最佳: WILD(3客) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P5,P6最差: LDS(2客)
      4. wild card: P3 vs P6, P4 vs P5
       */






    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      e.printStackTrace();
    } // end of log日誌
  } // end of main()


  private static List<String[]> readFile(String filePath) {
    List<String[]> rowList = new ArrayList<>();  // 建立陣列物件

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//        logger.info("嘗試開啟檔案: " + filePath);

      int count = 0;
      String line = "";
      while ((line = br.readLine()) != null) {
        assert line != null : "讀不到檔案資料";

        count++;
        if (count == 1) {  // 為了跳過表頭
          continue;
        }

        String[] data = line.split(",");  // 使用逗點切割欄位
        if (data.length < 5) {  // 欄位不足5欄的資料不要
          continue;
        }

        rowList.add(data);  // 讀取的檔案放入rowList陣列裡
      }
    } catch (FileNotFoundException e) {
      logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
      throw new RuntimeException(e);

    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀檔IO找不到檔案: " + e.getMessage());
      throw new RuntimeException(e);
    }

    return rowList;  // 回傳String陣列

    } // end of try(readFile)


  // 合併兩個陣列資料, 回傳HashMap
  private static Map<String, String[]> mergeTeamData(List<String[]> dataWildCard, List<String[]> dataCourt) {
    // 建立hashMap物件, 合併court, wildCardTeam資料
    Map<String, String[]> teamDataMap = new HashMap<>();

    /* 讀取wildCardTeam檔案,
      欄位依序為: League, Team, W, L, PCT, Position
       */
    for (String[] row: dataWildCard) {
      String teamName = row[1];  // 取得球隊名稱 (key)
      String[] teamInfo = new String[]{row[0], row[2], row[3], row[4], row[5]};
      teamDataMap.put(teamName, teamInfo); // 球隊名字當作key, 其他欄位為value
    }

      /* 比對court中的球隊名稱，將court資料加入wildCard該球隊資料,
      欄位依序為: 球隊,球場名稱,座位數,季後賽滿座率,世界大賽滿座率
       */
    for (String[] row: dataCourt) {
      String courtTeamName = row[0];  // 取得球隊名稱
      String[] courtTeamInfo = new String[]{row[1], row[2], row[3], row[4]};

      for (String key: teamDataMap.keySet()) {
        if (courtTeamName.contains(key)) {  // 查看court的球隊名稱有沒有在teamData的keySet裡
          // 合併wildCard和court資料
          String[] wildCardInfo = teamDataMap.get(key);  // 取出Map裡的value

          // 複製wildCard陣列, court陣列到新陣列裡
          String[] combinedInfo = new String[wildCardInfo.length + courtTeamInfo.length];  // 建立新陣列, 存放合併後的全部資料

          // System.arraycopy(來源陣列, 起始位置, 目標陣列, 要複製的數量)：用來合併兩個陣列
          System.arraycopy(wildCardInfo, 0, combinedInfo, 0, wildCardInfo.length);
          System.arraycopy(courtTeamInfo, 0, combinedInfo, wildCardInfo.length, courtTeamInfo.length);

          // 將合併後的資料放到teamData
          teamDataMap.put(key, combinedInfo);
        }
      }
    } // end of for(court)

    return teamDataMap;
  }




  } // end of Profit
