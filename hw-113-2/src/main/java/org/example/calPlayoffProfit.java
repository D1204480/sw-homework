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
      Map<Integer, Team> _ALteamDataMap = new HashMap<>();
      Map<Integer, Team> _NLteamDataMap = new HashMap<>();


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
          int win = Integer.parseInt(wildCardRow[2].trim());
          int lose = Integer.parseInt(wildCardRow[3].trim());
          float pct = Float.parseFloat(wildCardRow[4].trim());
          int position = Integer.parseInt(wildCardRow[5].trim());

          Team team = new Team();  // 建立team物件
          team.setLeague(league);
          team.setTeamName(teamName);
          team.setWin(win);
          team.setLose(lose);
          team.setPct(pct);
          team.setPosition(position);

          // store in the Map
          teamDataMap.put(teamName, team);

          if (league.startsWith("AL")) {
            _ALteamDataMap.put(position, team);

          } else if (league.startsWith("NL")) {
            _NLteamDataMap.put(position, team);
          }


        } // end of while
      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生讀檔IO找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);
      } // end of try(read wildCard.csv)


      // read and update data to Map from court.csv
      try (BufferedReader br = new BufferedReader(new FileReader(courtFilePath))) {
        String line;
        br.readLine(); // 跳過表頭
        while ((line = br.readLine()) != null) {
          String[] courtRow = line.split(",");  // 以逗號分割

          if (courtRow.length < 5) {  // court.csv有5個欄位
            continue;
          }
          assert courtRow.length >= 5 : "row欄位數不足5欄";

          String _courtTeamName = courtRow[0].trim();
          String courtName = courtRow[1].trim();
          String courtSeats = courtRow[2].trim();
          String playoffsSOrate = courtRow[3].trim();
          String worldSeriesSOrate = courtRow[4].trim();

          // 如果有找到Map裡的key, 把court資料加入team物件裡
          for (String key : teamDataMap.keySet()) {
            if (_courtTeamName.contains(key)) {
              // 取出Map該key的value
              Team team = teamDataMap.get(key);

              // 更新team物件的欄位值
              team.setCourtName(courtName);
              team.setCourtSeats(courtSeats);
              team.setPlayoffsSOrate(playoffsSOrate);
              team.setWorldSeriesSOrate(worldSeriesSOrate);
            }
          } // end of for

          // ALMap如果有找到AL的隊伍名稱, 更新team的欄位資料
          for (Team team : _ALteamDataMap.values()) {
            if (team.getTeamName().equals(_courtTeamName)) {
              // 取出Map該key的value
              int position = team.getPosition();
              Team setTeam = teamDataMap.get(position);

              // 更新team物件的欄位值
              setTeam.setCourtName(courtName);
              setTeam.setCourtSeats(courtSeats);
              setTeam.setPlayoffsSOrate(playoffsSOrate);
              setTeam.setWorldSeriesSOrate(worldSeriesSOrate);
            }
          }

          // NLMap如果有找到NL的隊伍名稱, 更新team的欄位資料
          for (Team team : _NLteamDataMap.values()) {
            if (team.getTeamName().equals(_courtTeamName)) {
              // 取出Map該key的value
              int position = team.getPosition();
              Team setTeam = teamDataMap.get(position);

              // 更新team物件的欄位值
              setTeam.setCourtName(courtName);
              setTeam.setCourtSeats(courtSeats);
              setTeam.setPlayoffsSOrate(playoffsSOrate);
              setTeam.setWorldSeriesSOrate(worldSeriesSOrate);
            }
          }

        } // end of while

      } catch (FileNotFoundException e) {
        logger.log(Level.SEVERE, "發生找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);

      } catch (IOException e) {
        logger.log(Level.SEVERE, "發生讀檔IO找不到檔案: " + e.getMessage());
        throw new RuntimeException(e);
      } // end of try(read court.txt)


      // 印出teamDataMap查看
//      for (Map.Entry<String, Team> entry : teamDataMap.entrySet()) {
//        System.out.println("key: " + entry.getKey());
//        System.out.println("value: " + entry.getValue());
//      }

      // 印出teamDataMap查看
//      for (Map.Entry<Integer, Team> entry : _ALteamDataMap.entrySet()) {
//        System.out.println("key: " + entry.getKey());
//        System.out.println("value: " + entry.getValue());
//      }

      // 印出teamDataMap查看
//      for (Map.Entry<Integer, Team> entry : _NLteamDataMap.entrySet()) {
//        System.out.println("key: " + entry.getKey());
//        System.out.println("value: " + entry.getValue());
//      }


      // 計算分潤

      // 計算各隊分潤(hard-coding)
      calProfit(_ALteamDataMap, "New York Yankees", "AL");


    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      throw new RuntimeException(e);
    } // end of try(logger)
  } // end of main


  /* 計算分潤:
      1. P1,P2最佳: LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P1,P2最差: LDS(2主+1客)
      2. P3最佳: WILD(3主) + LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P3最差: LDS(2主)
      3. P4最佳: WILD(3主) + LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P4最差: LDS(2主)
      4. P5最佳: WILD(3客) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P5最差: LDS(2客)
      5. P6最佳: WILD(3客) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P6最差: LDS(2客)
      6. wild card: P3 vs P6, P4 vs P5
      7. 公式:(座位數) * (滿座率) * (票價450) * (客場分潤15%) * (場數)
   */
  private static void calProfit(Map<Integer, Team> teamMap, String teamName, String league) {
    int ticketPrice = 450;
    int worldTicket = 800;
    float homeShare = 0.85F;
    float awayShare = 1 - homeShare;
    int best_LDS_homeGames = 3;
    int best_LDS_awayGames = 5 - best_LDS_homeGames;
    int best_LCS_World_homeGames = 4;
    int best_LCS_World_awayGames = 7 - best_LCS_World_homeGames;
    int worst_LDS_homeGames = 2;
    int worst_LDS_awayGames = 3 - worst_LDS_homeGames;
    int total_P1earning = 0;
    int total_worst_LDS_earning = 0;

    /* P1最佳獲利:
       A.LDS: 3主+2客(P4 or P5)
       B.LCS: 4主+3客(P2 or P3 or P6)
       C.WS: (名次相同, 則以字母排序高低種子) 4主+3客(ALP1 or ALP2 or ALP3 or ALP4 or ALP5 or ALP6)
    */
    for (int key : teamMap.keySet()) {
      if (key == 1) {
        Team NLp1 = teamMap.get(key); // 取出key為1的該Map資料

        int homeSeatsInt = Integer.parseInt(NLp1.getCourtSeats().replace("k", "")) * 1000;
        float playoffSOrateFloat = Float.parseFloat(NLp1.getPlayoffsSOrate().replace("%", "")) / 100;
        float worldSeriesOrateFloat = Float.parseFloat(NLp1.getWorldSeriesSOrate().replace("%", "")) / 100;

        // A.LDS
        int LDS_homeProfit = (int) (homeSeatsInt * playoffSOrateFloat * ticketPrice * homeShare * best_LDS_homeGames);

        Team LDS_opponent = findOpponent("LDS", 1, teamMap, league);
        int LDS_awaySeats = Integer.parseInt(LDS_opponent.getCourtSeats().replace("k", "")) * 1000;
        float awayPlayoffSOrateFloat = Float.parseFloat(LDS_opponent.getPlayoffsSOrate().replace("%", "")) / 100;
        int LDS_awayProfit = (int) (LDS_awaySeats * awayPlayoffSOrateFloat * ticketPrice * awayShare * best_LDS_awayGames);

        int total_LDS_earning = (int) (LDS_homeProfit + LDS_awayProfit);

        // B.LCS
        int LCS_homeProfit = (int) (homeSeatsInt * playoffSOrateFloat * ticketPrice * homeShare * best_LCS_World_homeGames);

        Team LCS_opponent = findOpponent("LCS", 1, teamMap, league);
        int LCS_awaySeatsInt = Integer.parseInt(LCS_opponent.getCourtSeats().replace("k", "000"));
        float LCS_awayPlayoffSOrateFloat = Float.parseFloat(LCS_opponent.getPlayoffsSOrate().replace("%", "")) / 100;
        int LCS_awayProfit = (int) (LCS_awaySeatsInt * LCS_awayPlayoffSOrateFloat * ticketPrice * awayShare * best_LCS_World_awayGames);

        int total_LCS_earning = (int) (LCS_homeProfit + LCS_awayProfit);

        // C.world series
        int ws_homeProfit = (int) (homeSeatsInt * playoffSOrateFloat * worldTicket * homeShare * best_LCS_World_homeGames);
        int ws_awayProfit = (int) (56000 * 1 * worldTicket * awayShare * best_LCS_World_awayGames);  // NL最賺球場: Dodger
        int total_WS_earning = (int) (ws_homeProfit + ws_awayProfit);

        total_P1earning = total_LDS_earning + total_LCS_earning + total_WS_earning;

        // P1最差獲利: LDS (2主+1客)
        // A.LDS
        int worst_LDS_homeProfit = (int) (homeSeatsInt * playoffSOrateFloat * ticketPrice * homeShare * worst_LDS_homeGames);

        Team worst_LDS_opponent = findOpponent("LDS", 1, teamMap, league);
        int worst_LDS_awaySeats = Integer.parseInt(worst_LDS_opponent.getCourtSeats().replace("k", "")) * 1000;
        float worst_awayPlayoffSOrateFloat = Float.parseFloat(worst_LDS_opponent.getPlayoffsSOrate().replace("%", "")) / 100;
        int worst_LDS_awayProfit = (int) (worst_LDS_awaySeats * worst_awayPlayoffSOrateFloat * ticketPrice * awayShare * worst_LDS_awayGames);

        total_worst_LDS_earning = (int) (worst_LDS_homeProfit + worst_LDS_awayProfit);
      }
    }
    System.out.println(teamName + " 預估最佳獲利: " + total_P1earning);
    System.out.println(teamName + " 預估最差獲利: " + total_worst_LDS_earning);


//    for (Map.Entry<String, Team> entry : teamMap.entrySet()) {
//      if (entry.getKey().equals(teamName)) {  // 比對key
//        Team team = teamMap.get(teamName);  // 找到key, 則取出team物件
//
//        // 排行1
//        if (team.getPosition() == 1) {
//          int tickitPrice = 450;
//          int worldTickit = 800;
//          float homeShare = 0.85F;
//          float awayShare = 1 - homeShare;
//          int LDShomeGame = 3;
//          int homeSeatsInt = Integer.parseInt(team.getCourtSeats().replace("k", "000"));
//          float playoffSOrateFloat = Float.parseFloat(team.getPlayoffsSOrate().replace("%", "")) / 100;
//          float worldSeriesOrateFloat = Float.parseFloat(team.getWorldSeriesSOrate().replace("%", "")) / 100;
//
//          // A.LDS
//          double LDS_homeProfit = homeSeatsInt * playoffSOrateFloat * tickitPrice * homeShare * LDShomeGame;
////          double LDS_awayProfit =
//
//        }
//      }

  } // end of calProfit


  private static Team findOpponent(String game, int position, Map<Integer, Team> teamMap, String league) {
    // LDS
    if (game.equals("LDS")) {
      if (position == 1) {
        // 找出是P4 or P5
        Team p4 = getTeamByCourtSize(4, teamMap);
        Team p5 = getTeamByCourtSize(5, teamMap);
        return decideBetweenTeams(p4, p5);

      } else if (position == 2) {
        // 找出是P3 or P6
        Team p3 = getTeamByCourtSize(3, teamMap);
        Team p6 = getTeamByCourtSize(6, teamMap);
        return decideBetweenTeams(p3, p6);
      }
    }

    // LCS
    if (game.equals("LCS")) {
      if (position == 1 || position == 4 || position == 5) {
        // 找出是P2 or P3 or P6
        Team p2 = getTeamByCourtSize(2, teamMap);
        Team p3 = getTeamByCourtSize(3, teamMap);
        Team p6 = getTeamByCourtSize(6, teamMap);
        return decideBetweenTeams(p2, p3, p6);

      } else if (position == 2 || position == 3 || position == 6) {
        // 找出是P1 or P4 or P5
        Team p1 = getTeamByCourtSize(1, teamMap);
        Team p4 = getTeamByCourtSize(4, teamMap);
        Team p5 = getTeamByCourtSize(5, teamMap);
        return decideBetweenTeams(p1, p4, p5);
      }
    }

      /* world series
        AL: 找出(NL座位*世界大戰滿座率)最高的球場: Yankee
        NL: 找出(AL座位*世界大戰滿座率)最高的球場: Dodger
       */
    if (game.equals("world")) {
      Team p1 = getTeamByCourtSize(1, teamMap);
      Team p2 = getTeamByCourtSize(2, teamMap);
      Team p3 = getTeamByCourtSize(3, teamMap);
      Team p4 = getTeamByCourtSize(4, teamMap);
      Team p5 = getTeamByCourtSize(5, teamMap);
      Team p6 = getTeamByCourtSize(6, teamMap);
      return decideBetweenTeams(p1, p2, p3, p4, p5, p6);
    }

    return null; // If no opponent found
  }

  // 找出第position的隊伍
  private static Team getTeamByCourtSize(int position, Map<Integer, Team> teamMap) {
    for (int key : teamMap.keySet()) {
      if (key == position) {  // 找出第position的隊伍(物件)
        return teamMap.get(key);
      }
    }
    return null; // No team found with the given position
  }

  // 比較對戰隊伍的球場獲利高低, between multiple teams, teams視為一個array
  private static Team decideBetweenTeams(Team... teams) {

    Team bestTeam = teams[0];
    Float higherCourtEarning = courtEarning(bestTeam);

    for (int i = 0; i < teams.length; i++) {
      Float courtEarning = courtEarning(teams[i]);
      if (courtEarning > higherCourtEarning) {
        higherCourtEarning = courtEarning;
        bestTeam = teams[i];  // 更新bestTeam
      }
    }
    return bestTeam;
  }

  // 比較哪個球場進場人數較多
  private static float courtEarning(Team team) {
    int seats = (int) (Float.parseFloat(team.getCourtSeats().replace("k", "")) * 1000);
    float SOrate = Float.parseFloat(team.getPlayoffsSOrate().replace("%", "")) / 100;
    return seats * SOrate;
  }

} // end of calPlayoffProfit
