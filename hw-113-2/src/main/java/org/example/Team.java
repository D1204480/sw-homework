package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Team implements Comparator<Team> {
  private String league;
  private String teamName;
  private int win;
  private int lose;
  private float pct;

  @Override
  public int compare(Team team1, Team team2) {
    if (team1.win < team2.win) {  // 比勝場數量
      return 1;

    } else if (team1.win > team2.win) {
      return -1;

    } else {
      if (team1.lose < team2.lose) {  // 比誰輸的少
        return 1;
      } else if (team1.lose > team2.lose) {
        return -1;
      }
    }
    return team1.teamName.compareTo(team2.teamName);
  }
}
