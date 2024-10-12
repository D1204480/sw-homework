package MLBcrawler.src.main.java.org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class MLB_selenium {
    public static void main(String[] args) {

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.mlb.com/standings/regular-season/division/2023");
        String title = driver.getTitle();
        System.out.println(title);


        // 抓取所有球隊名稱
        List<WebElement> teamNames = driver.findElements(By.cssSelector("a[data-type='teamLink']"));
        for (WebElement team : teamNames) {
            System.out.println("team:"+team.getText());  // 打印出球隊名稱
        }

        // 抓取勝場標題
        WebElement w_Title = driver.findElement(By.cssSelector("th[data-col='1']"));
        System.out.println(w_Title.getText());  // 這會打印出 "W"
        // 抓取所有球隊的勝場數
        List<WebElement> winElements = driver.findElements(By.cssSelector("td[data-col='1'] span[aria-hidden='true']"));
        for (WebElement win : winElements) {
            System.out.println(win.getText());
        }

        // 抓取敗場標題
        WebElement l_Title = driver.findElement(By.cssSelector("th[data-col='2']"));
        System.out.println(l_Title.getText());  // 這會打印出 "L"
        // 抓取所有球隊的敗場數
        List<WebElement> lostElements = driver.findElements(By.cssSelector("td[data-col='2'] span[aria-hidden='true']"));
        for (WebElement lost : lostElements) {
            System.out.println(lost.getText());
        }

        // 關閉 WebDriver
        driver.quit();
    }
}
