package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Team implements Comparable<Team> {
//  private String league;
  private String teamName;
  private int win;
  private int lose;
  private float pct;

  @Override
  public int compareTo(Team other) {
    if (this.pct > other.pct) {
      return -1;  // 由大而小做降序排列
    } else if (this.pct < other.pct) {
      return 1;
    }
    return 0;
  }
} // end of Team
