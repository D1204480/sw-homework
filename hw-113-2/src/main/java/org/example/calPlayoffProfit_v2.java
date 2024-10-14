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

public class calPlayoffProfit_v2 {
  // 一般都是用 class name 來作為 logger 的名字
  private static final Logger logger = Logger.getLogger(calPlayoffProfit_v2.class.getName());

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

      // 印出_ALteamDataMap查看
//      for (Map.Entry<Integer, Team> entry : _ALteamDataMap.entrySet()) {
//        System.out.println("key: " + entry.getKey());
//        System.out.println("value: " + entry.getValue());
//      }

      // 印出_NLteamDataMap查看
//      for (Map.Entry<Integer, Team> entry : _NLteamDataMap.entrySet()) {
//        System.out.println("key: " + entry.getKey());
//        System.out.println("value: " + entry.getValue());
//      }


      // 計算分潤

      // 計算各隊分潤(hard-coding)
      // AL聯盟
      calProfit(_ALteamDataMap, _NLteamDataMap, "New York Yankees", "AL");
      calProfit(_ALteamDataMap, _NLteamDataMap, "Cleveland Guardians", "AL");
      calProfit(_ALteamDataMap, _NLteamDataMap, "Houston Astros", "AL");
      calProfit(_ALteamDataMap, _NLteamDataMap, "Baltimore Orioles", "AL");
      calProfit(_ALteamDataMap, _NLteamDataMap, "Detroit Tigers", "AL");
      calProfit(_ALteamDataMap, _NLteamDataMap, "Kansas City Royals", "AL");

      // NL聯盟
      calProfit(_NLteamDataMap, _ALteamDataMap, "Los Angeles Dodgers", "NL");
      calProfit(_NLteamDataMap, _ALteamDataMap, "Philadelphia Phillies", "NL");
      calProfit(_NLteamDataMap, _ALteamDataMap, "Milwaukee Brewers", "NL");
      calProfit(_NLteamDataMap, _ALteamDataMap, "San Diego Padres", "NL");
      calProfit(_NLteamDataMap, _ALteamDataMap, "Arizona Diamondbacks", "NL");
      calProfit(_NLteamDataMap, _ALteamDataMap, "Atlanta Braves", "NL");


    } catch (IOException e) {
      logger.log(Level.SEVERE, "發生讀寫log錯誤: " + e.getMessage());
      throw new RuntimeException(e);
    } // end of try(logger)
  } // end of main


  /* 計算分潤:
      1. P1,P2最佳: LDS(3主+2客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P1,P2最差: LDS(2主+1客)
      2. P3最佳: WILD(3主) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P3最差: WILD(2主)
      3. P4最佳: WILD(3主) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P4最差: WILD(2主)
      4. P5最佳: WILD(3客) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P5最差: WILD(2客)
      5. P6最佳: WILD(3客) + LDS(2主+3客) + LCS(高種子:4主+3客, 低種子: 3主+4客) + WS(高種子:4主+3客, 低種子: 3主+4客)
         P6最差: WILD(2客)
      6. wild card: P3 vs P6, P4 vs P5
      7. 公式:(座位數) * (滿座率) * (票價450) * (客場分潤15%) * (場數)
   */

  // 計算座位數, 轉換格式為32k -> 32000
  private static int calculateSeats(Team team) {
    String courtSeats = team.getCourtSeats();
    if (courtSeats == null || courtSeats.isEmpty()) {  // 如果沒有資料
      System.err.println("Court seats data is missing for team: " + team.getTeamName());
//      logger.log(Level.WARNING,"Court seats data is missing for team: \" + team.getTeamName()");
      return 0;
    }
    return (int) (Float.parseFloat(team.getCourtSeats().replace("k", "")) * 1000);
  }

  // 百分比轉為數字: 100% -> 1
  private static float calculateRate(String rate) {
    if (rate == null || rate.isEmpty()) {  // 如果沒有值
      System.err.println("Rate data is missing");
      return 0.0F; // Default to 0 if rate is null or empty
    }
    return Float.parseFloat(rate.replace("%", "")) / 100;
  }

  // 計算分潤公式: 座位數 * 滿座率 * 票價 * 主客場分潤 * 場數
  private static int calculateProfit(int seats, float rate, int ticketPrice, float share, int games) {
    return (int) (seats * rate * ticketPrice * share * games);
  }

  // 計算LDS獲利
  private static int calculateLDSEarnings(Team team, Team opponent, int ticketPrice, float homeShare, float awayShare, int homeGames, int awayGames) {
    int homeSeats = calculateSeats(team);
    float homeRate = calculateRate(team.getPlayoffsSOrate());

    int awaySeats = calculateSeats(opponent);
    float awayRate = calculateRate(opponent.getPlayoffsSOrate());

    int homeProfit = calculateProfit(homeSeats, homeRate, ticketPrice, homeShare, homeGames);
    int awayProfit = calculateProfit(awaySeats, awayRate, ticketPrice, awayShare, awayGames);

    return homeProfit + awayProfit;
  }

  // 計算LCS獲利
  private static int calculateLCSEarnings(Team team, Team opponent, int ticketPrice, float homeShare, float awayShare, int homeGames, int awayGames) {
    // Same logic as LDS, reused
    return calculateLDSEarnings(team, opponent, ticketPrice, homeShare, awayShare, homeGames, awayGames);
  }

  // 計算世界大賽獲利
  private static int calculateWorldSeriesEarnings(Team team, Team opponent, int worldTicket, float homeShare, float awayShare, int homeGames, int awayGames) {
    int homeSeats = calculateSeats(team);
    float homeRate = calculateRate(team.getWorldSeriesSOrate());

    int awaySeats = calculateSeats(opponent);
    float awayRate = calculateRate(opponent.getWorldSeriesSOrate());

    int homeProfit = calculateProfit(homeSeats, homeRate, worldTicket, homeShare, homeGames);
    int awayProfit = calculateProfit(awaySeats, awayRate, worldTicket, awayShare, awayGames);

    return homeProfit + awayProfit;
  }

  // 計算每隊獲利
  private static void calProfit(Map<Integer, Team> teamMap, Map<Integer, Team> opponentTeamMap, String teamName, String league) {
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
    int best_wild_Games = 3;
    int worst_wild_Games = 2;
    int team_best_earning = 0;
    int team_worst_earning = 0;

    for (int key : teamMap.keySet()) {
      if (key == 1 || key == 2) {
        Team team = teamMap.get(key);

        // A.LDS Best and Worst
        Team LDS_opponent = findOpponent("LDS", key, teamMap, opponentTeamMap, league);
        int total_LDS_earning = calculateLDSEarnings(team, LDS_opponent, ticketPrice, homeShare, awayShare, best_LDS_homeGames, best_LDS_awayGames);
        int total_worst_LDS_earning = calculateLDSEarnings(team, LDS_opponent, ticketPrice, homeShare, awayShare, worst_LDS_homeGames, worst_LDS_awayGames);

        // B.LCS
        Team LCS_opponent = findOpponent("LCS", key, teamMap, opponentTeamMap, league);
        int total_LCS_earning = calculateLCSEarnings(team, LCS_opponent, ticketPrice, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // C.World Series
        Team WS_opponent = findOpponent("WS", key, teamMap, opponentTeamMap, league);
        int total_WS_earning = calculateWorldSeriesEarnings(team, WS_opponent, worldTicket, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // Total Best Earning
        team_best_earning = total_LDS_earning + total_LCS_earning + total_WS_earning;

        // Total Worst Earning (LDS)
        team_worst_earning = total_worst_LDS_earning;

      } else if (key == 3) {
        Team team = teamMap.get(key);
        Team p6team = teamMap.get(6);  // wild-card對手
        Team p2team = teamMap.get(2);  // LDS對手

        // wild-card Best (3戰, P3 vs P6)
        int total_wild_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_wild_Games);

        // wild-card Worst (2戰, P3 vs P6)
        int total_worst_wild_earning = (int) (calculateSeats(p6team) * calculateRate(p6team.getPlayoffsSOrate()) * ticketPrice * homeShare * worst_wild_Games);

        // A.LDS Best (2主場+3客場, P3 vs P2)
        int total_LDS_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_LDS_awayGames);
        int total_LDS_away_earning = (int) (calculateSeats(p2team) * calculateRate(p2team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_LDS_homeGames);

        // B.LCS
        Team LCS_opponent = findOpponent("LCS", key, teamMap, opponentTeamMap, league);
        int total_LCS_earning = calculateLCSEarnings(team, LCS_opponent, ticketPrice, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // C.World Series
        Team WS_opponent = findOpponent("WS", key, teamMap, opponentTeamMap, league);
        int total_WS_earning = calculateWorldSeriesEarnings(team, WS_opponent, worldTicket, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // Total Best Earning
        team_best_earning = total_wild_earning + total_LDS_away_earning + total_LDS_earning + total_LCS_earning + total_WS_earning;

        // Total Worst Earning (WILD 2戰)
        team_worst_earning = total_worst_wild_earning;

      } else if (key == 4) {
        Team team = teamMap.get(key);
        Team p5team = teamMap.get(5);  // wild-card對手
        Team p1team = teamMap.get(1);  // LDS對手

        // wild-card Best (3戰, P4 vs P5)
        int total_wild_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_wild_Games);

        // wild-card Worst (2戰, P4 vs P5)
        int total_worst_wild_earning = (int) (calculateSeats(p5team) * calculateRate(p5team.getPlayoffsSOrate()) * ticketPrice * homeShare * worst_wild_Games);

        // A.LDS Best (2主場+3客場, P4 vs P1)
        int total_LDS_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_LDS_awayGames);
        int total_LDS_away_earning = (int) (calculateSeats(p1team) * calculateRate(p1team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_LDS_homeGames);

        // B.LCS
        Team LCS_opponent = findOpponent("LCS", key, teamMap, opponentTeamMap, league);
        int total_LCS_earning = calculateLCSEarnings(team, LCS_opponent, ticketPrice, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // C.World Series
        Team WS_opponent = findOpponent("WS", key, teamMap, opponentTeamMap, league);
        int total_WS_earning = calculateWorldSeriesEarnings(team, WS_opponent, worldTicket, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // Total Best Earning
        team_best_earning = total_wild_earning + total_LDS_away_earning + total_LDS_earning + total_LCS_earning + total_WS_earning;

        // Total Worst Earning (WILD 2戰)
        team_worst_earning = total_worst_wild_earning;

      } else if (key == 5) {
        Team team = teamMap.get(key);
        Team p5team = teamMap.get(4);  // wild-card對手
        Team p1team = teamMap.get(1);  // LDS對手

        // wild-card Best (3戰, P5 vs P4)
        int total_wild_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_wild_Games);

        // wild-card Worst (2戰, P5 vs P4)
        int total_worst_wild_earning = (int) (calculateSeats(p5team) * calculateRate(p5team.getPlayoffsSOrate()) * ticketPrice * awayShare * worst_wild_Games);

        // A.LDS Best (2主場+3客場, P5 vs P1)
        int total_LDS_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_LDS_awayGames);
        int total_LDS_away_earning = (int) (calculateSeats(p1team) * calculateRate(p1team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_LDS_homeGames);

        // B.LCS
        Team LCS_opponent = findOpponent("LCS", key, teamMap, opponentTeamMap, league);
        int total_LCS_earning = calculateLCSEarnings(team, LCS_opponent, ticketPrice, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // C.World Series
        Team WS_opponent = findOpponent("WS", key, teamMap, opponentTeamMap, league);
        int total_WS_earning = calculateWorldSeriesEarnings(team, WS_opponent, worldTicket, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // Total Best Earning
        team_best_earning = total_wild_earning + total_LDS_away_earning + total_LDS_earning + total_LCS_earning + total_WS_earning;

        // Total Worst Earning (WILD 2戰)
        team_worst_earning = total_worst_wild_earning;

      } else if (key == 6) {
        Team team = teamMap.get(key);
        Team p5team = teamMap.get(3);  // wild-card對手
        Team p1team = teamMap.get(2);  // LDS對手

        // wild-card Best (3戰, P6 vs P3)
        int total_wild_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_wild_Games);

        // wild-card Worst (2戰, P6 vs P3)
        int total_worst_wild_earning = (int) (calculateSeats(p5team) * calculateRate(p5team.getPlayoffsSOrate()) * ticketPrice * awayShare * worst_wild_Games);

        // A.LDS Best (2主場+3客場, P6 vs P2)
        int total_LDS_earning = (int) (calculateSeats(team) * calculateRate(team.getPlayoffsSOrate()) * ticketPrice * homeShare * best_LDS_awayGames);
        int total_LDS_away_earning = (int) (calculateSeats(p1team) * calculateRate(p1team.getPlayoffsSOrate()) * ticketPrice * awayShare * best_LDS_homeGames);

        // B.LCS
        Team LCS_opponent = findOpponent("LCS", key, teamMap, opponentTeamMap, league);
        int total_LCS_earning = calculateLCSEarnings(team, LCS_opponent, ticketPrice, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // C.World Series
        Team WS_opponent = findOpponent("WS", key, teamMap, opponentTeamMap, league);
        int total_WS_earning = calculateWorldSeriesEarnings(team, WS_opponent, worldTicket, homeShare, awayShare, best_LCS_World_homeGames, best_LCS_World_awayGames);

        // Total Best Earning
        team_best_earning = total_wild_earning + total_LDS_away_earning + total_LDS_earning + total_LCS_earning + total_WS_earning;

        // Total Worst Earning (WILD 2戰)
        team_worst_earning = total_worst_wild_earning;
      }
    }
    System.out.println(teamName + " 預估最佳獲利: " + String.format("%,d", team_best_earning) + " USD");
    System.out.println(teamName + " 預估最差獲利: " + String.format("%,d", team_worst_earning) + " USD");
  }
  // end of calProfit


  private static Team findOpponent(String game, int position, Map<Integer, Team> teamMap, Map<Integer, Team> opponentTeamMap, String league) {
    // LDS
    if (game.equals("LDS")) {
      if (position == 1) {
        // 找出是P4 or P5
        Team p4 = getTeamByCourtSize(4, teamMap, opponentTeamMap);
        Team p5 = getTeamByCourtSize(5, teamMap, opponentTeamMap);
        return decideBetweenTeams(p4, p5);

      } else if (position == 2) {
        // 找出是P3 or P6
        Team p3 = getTeamByCourtSize(3, teamMap, opponentTeamMap);
        Team p6 = getTeamByCourtSize(6, teamMap, opponentTeamMap);
        return decideBetweenTeams(p3, p6);
      }
    }

    // LCS
    if (game.equals("LCS")) {
      if (position == 1 || position == 4 || position == 5) {
        // 找出是P2 or P3 or P6
        Team p2 = getTeamByCourtSize(2, teamMap, opponentTeamMap);
        Team p3 = getTeamByCourtSize(3, teamMap, opponentTeamMap);
        Team p6 = getTeamByCourtSize(6, teamMap, opponentTeamMap);
        return decideBetweenTeams(p2, p3, p6);

      } else if (position == 2 || position == 3 || position == 6) {
        // 找出是P1 or P4 or P5
        Team p1 = getTeamByCourtSize(1, teamMap, opponentTeamMap);
        Team p4 = getTeamByCourtSize(4, teamMap, opponentTeamMap);
        Team p5 = getTeamByCourtSize(5, teamMap, opponentTeamMap);
        return decideBetweenTeams(p1, p4, p5);
      }
    }

      /* world series
        AL: 找出(NL座位*世界大戰滿座率)最高的球場: Yankee
        NL: 找出(AL座位*世界大戰滿座率)最高的球場: Dodger
       */
    if (game.equals("WS")) {
      if (league.startsWith("AL")) {
        // We are in AL league, so find the best NL team
        Team bestOpponent = null;
        float highestEarning = -1;

        for (Team team : opponentTeamMap.values()) {  // 找對方聯盟Map的value物件
          if (team.getLeague().startsWith("NL")) {
            // Calculate the court earning for this NL team
            float earning = worldSeriesEarning(team);

            // Check if this team has the highest earning potential
            if (earning > highestEarning) {
              highestEarning = earning;
              bestOpponent = team;
            }
          }
        }
        return bestOpponent;  // Return the best NL team for the AL league

      } else if (league.startsWith("NL")) {
        // Similarly, handle the NL league by finding the best AL team
        Team bestOpponent = null;
        float highestEarning = -1;

        // Loop through the AL league teams
        for (Team team : opponentTeamMap.values()) {
          if (team.getLeague().startsWith("AL")) {  // Ensure the team is from the AL league

            // Calculate the court earning for this AL team
            float earning = worldSeriesEarning(team);

            // Check if this team has the highest earning potential
            if (earning > highestEarning) {
              highestEarning = earning;
              bestOpponent = team;
            }
          }
        }
        return bestOpponent;  // Return the best AL team for the NL league
      }
    }

    return null; // If no opponent found
  }

  // 找出第position的隊伍
  private static Team getTeamByCourtSize(int position, Map<Integer, Team> teamMap, Map<Integer, Team> opponentTeamMap) {
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
      if (teams[i] != null && courtEarning(teams[i]) > higherCourtEarning) {
        higherCourtEarning = courtEarning(teams[i]);
        bestTeam = teams[i];   // 更新bestTeam
      }
    }
    return bestTeam;
  }

  // 比較哪個球場進場人數較多
  private static float courtEarning(Team team) {
    String courtSeats = team.getCourtSeats();

    // Check if courtSeats is null
    if (courtSeats == null || courtSeats.isEmpty()) {
      System.err.println("Error: Court seats information is missing for team " + team.getTeamName());
      return 0; // Return 0 or any default value in case of missing data
    }

    int seats = (int) (Float.parseFloat(courtSeats.replace("k", "")) * 1000);
    String playoffSOrate = team.getPlayoffsSOrate();

    // Check if PlayoffSOrate is null or empty
    if (playoffSOrate == null || playoffSOrate.isEmpty()) {
      System.err.println("Error: Playoff sell-out rate information is missing for team " + team.getTeamName());
      return 0; // Return 0 or any default value in case of missing data
    }

    float SOrate = Float.parseFloat(playoffSOrate.replace("%", "")) / 100;
    return seats * SOrate;
  }


  private static float worldSeriesEarning(Team team) {
    // Check if courtSeats and worldSeriesSOrate are not null before proceeding
    if (team.getCourtSeats() != null && team.getWorldSeriesSOrate() != null) {
      // Parse the court seats and world series sell-out rate
      int seats = (int) (Float.parseFloat(team.getCourtSeats().replace("k", "")) * 1000);
      float wsSOrate = Float.parseFloat(team.getWorldSeriesSOrate().replace("%", "")) / 100;
      return seats * wsSOrate;

    } else {
      return 0;  // Returning 0 if any value is missing
    }
  }
}
