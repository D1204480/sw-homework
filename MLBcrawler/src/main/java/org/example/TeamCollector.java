package org.example;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamCollector {

  public void collect() {
    // 建立List放Team物件
    List<Team> teamList = new ArrayList<>();

    // 抓取網頁
    WebDriver driver = new ChromeDriver();
    driver.get("https://www.mlb.com/standings/regular-season/division");  // 打開網頁
    System.out.println(driver.getTitle()); // 印出網頁視窗title

    // 檔案名稱
    String fileName = "MLB_2024_regular.csv";
    // 寫檔到指定路徑
    String filePath = "D:\\fcu_post\\1131軟體品質測試\\homework\\sw-homework\\hw-113-1\\data\\" + fileName;

    // 兩個 BufferedWriter，用來寫入不同的路徑 (指定路徑、本資料夾)
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
    BufferedWriter localBw = new BufferedWriter(new FileWriter(fileName))) {

      // 製作csv檔printer
      CSVPrinter printer = new CSVPrinter(bw, CSVFormat.DEFAULT.withHeader("Team", "W", "L", "PCT"));
      CSVPrinter printerLocal = new CSVPrinter(localBw, CSVFormat.DEFAULT.withHeader("Team", "W", "L", "PCT"));
      /*
       網頁架構:
       <table>
         <thead>
           <tr> (表頭)
         </thead>
         <tbody>
           <tr> <tr> <tr> <tr> <tr> (5球隊)
         </tbody>
         <tbody>
           <tr> (表頭)
         </tbody>
       </table>
       */

      // 抓table-thead區塊
      List<WebElement> heads = driver.findElements(By.cssSelector("section > div.StandingsTablestyle__StandingsTableWrapper-sc-1l6jbjt-0.kGMpgP > div > div > table thead"));

      for (WebElement head: heads) {
        WebElement theadLeague = head.findElement(By.cssSelector("tr > th > span"));
        WebElement theadWin = head.findElement(By.cssSelector("tr > th:nth-child(2) > span > span > span"));
        WebElement theadLose = head.findElement(By.cssSelector("tr > th:nth-child(3) > span > span > span"));
        WebElement theadPct = head.findElement(By.cssSelector("tr > th:nth-child(4) > span > span > span"));

        System.out.print(theadLeague.getText() + "\t");
        System.out.print(theadWin.getText() + "\t");
        System.out.print(theadLose.getText() + "\t");
        System.out.println(theadPct.getText());

        // 寫入csv檔案 (指定路徑)
        printer.printRecord(theadLeague.getText() + ","
            + theadWin.getText() + ","
            + theadLose.getText() + ","
            + theadPct.getText());

        // 寫入csv檔案 (本資料夾)
        printerLocal.printRecord(theadLeague.getText() + ","
            + theadWin.getText() + ","
            + theadLose.getText() + ","
            + theadPct.getText());

      } // end of (heads)

      // 抓table-tbody區塊 (AL EAST)
//      List<WebElement> elements = driver.findElements(By.cssSelector("section > div.StandingsTablestyle__StandingsTableWrapper-sc-1l6jbjt-0.kGMpgP > div > div > table tbody tr"));
//      for (WebElement element: elements) {
//
//        WebElement name = element.findElement(By.cssSelector("th > div > div > a.StandingsCustomCellstyle__CellLinkWrapper-sc-1c94dhk-6.jnhStu"));
//        WebElement win = element.findElement(By.cssSelector("td > span > span"));
//        WebElement lose = element.findElement(By.cssSelector("td+td > span > span"));  // +找隔壁一位鄰居
//        WebElement pct = element.findElement(By.cssSelector("td+td+td > span > span"));
//
//        System.out.print(name.getText() +"\t");
//        System.out.print(win.getText() +"\t");
//        System.out.print(lose.getText() + "\t");
//        System.out.print(pct.getText() + "\t");
//
//        int winInt = Integer.parseInt(win.getText());
//        int loseInt = Integer.parseInt(lose.getText());
//        float pctFloat = Float.parseFloat(pct.getText());
//
//        // 建立team物件, 存入temaList裡
//        Team team = new Team(name.getText(), winInt, loseInt, pctFloat);
//      }

      // 抓table-tbody區塊
      List<WebElement> tbodyList = driver.findElements(By.cssSelector("table tbody"));

      // 遍歷每個 tbody，根據奇偶數tbody選擇不同的結構
      for (int i = 0; i < tbodyList.size(); i++) {
        WebElement tbody = tbodyList.get(i);

        // 找出該 tbody 中所有的 tr
        List<WebElement> rows = tbody.findElements(By.tagName("tr"));

        // 假如是奇數的 tbody
        if ((i + 1) % 2 != 0) {
          for (WebElement row : rows) {
            // 針對奇數 tbody 的架構 (球隊區)
            WebElement name = row.findElement(By.cssSelector("th > div > div > a.StandingsCustomCellstyle__CellLinkWrapper-sc-1c94dhk-6.jnhStu"));
            WebElement win = row.findElement(By.cssSelector("td > span > span"));
            WebElement lose = row.findElement(By.cssSelector("td[data-col='2'] span[aria-hidden='true']"));
            WebElement pct = row.findElement(By.cssSelector("td[data-col='3'] span[aria-hidden='true']"));

            System.out.print(name.getText() + "\t");
            System.out.print(win.getText() + "\t");
            System.out.print(lose.getText() + "\t");
            System.out.println(pct.getText());

            int winInt = Integer.parseInt(win.getText());
            int loseInt = Integer.parseInt(lose.getText());
            float pctFloat = Float.parseFloat(pct.getText());

            Team team = new Team(name.getText(), winInt, loseInt, pctFloat);  // 建立物件
            teamList.add(team);  // 放任陣列

            // 寫入csv檔案
            printer.printRecord(name.getText() + ","
                + win.getText() + ","
                + lose.getText() + ","
                + pct.getText());

            // 寫入csv檔案 (本資料夾)
            printerLocal.printRecord(name.getText() + ","
                + win.getText() + ","
                + lose.getText() + ","
                + pct.getText());
          }
        }
        // 假如是偶數的 tbody (表頭區)
        else {
          for (WebElement row: rows) {
            WebElement name = row.findElement(By.cssSelector("th > div"));
            WebElement win = row.findElement(By.cssSelector("td[data-col='1'] span[aria-hidden='true']"));
            WebElement lose = row.findElement(By.cssSelector("td[data-col='2'] span[aria-hidden='true']"));
            WebElement pct = row.findElement(By.cssSelector("td[data-col='3'] span[aria-hidden='true']"));

            System.out.print(name.getText() + "\t");
            System.out.print(win.getText() + "\t");
            System.out.print(lose.getText() + "\t");
            System.out.println(pct.getText());

            // 寫入csv檔案
            printer.printRecord(name.getText() + ","
                + win.getText() + ","
                + lose.getText() + ","
                + pct.getText());

            // 寫入csv檔案 (本資料夾)
            printerLocal.printRecord(name.getText() + ","
                + win.getText() + ","
                + lose.getText() + ","
                + pct.getText());
          }
        } // end of if/else

      } // end of for(tbody)
    } catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      driver.quit();
    }

  } // end of collect()
} // end of TeamCollector
