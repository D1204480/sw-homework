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

  // Check if the sum of wins and losses equals 162
  public boolean isValidRecord() {
    return (this.win + this.lose) > 160;
  }

  @Override
  public int compareTo(Team_v2 other) {
    // Compare by wins in descending order
    int winComparison = Integer.compare(other.win, this.win);
    if (winComparison != 0) {
      return winComparison;
    }

    // If wins are the same, compare by losses in ascending order
    int lossComparison = Integer.compare(this.lose, other.lose);
    if (lossComparison != 0) {
      return lossComparison;
    }

    // If both wins and losses are the same, compare by team name alphabetically
    return this.teamName.compareTo(other.teamName);
  }

}
