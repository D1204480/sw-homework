package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Team_v2 implements Comparable<Team_v2> {
  private String league;
  private String teamName;
  private int win;
  private int lose;
  private float pct;


  @Override
  public int compareTo(Team_v2 other) {
    return Integer.compare(other.win, this.win); // Sort by wins in descending order
  }
}
