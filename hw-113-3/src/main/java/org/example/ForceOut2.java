package org.example;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ForceOut2 {

  public String getForceOut(int base1, int base2, int base3) {
     /* 封殺狀況:
    x       -> 1B
    1B      -> 1B, 2B
    1B, 3B  -> 1B, 2B
    3B      -> 1B
     */

    int[] baseArray = new int[]{1, base1, base2, base3};  // 存放壘包人數
    Set<String> forceOutSet = new HashSet<>();  // 存放壘包封殺狀況

    for (int i = 1; i < baseArray.length; i++) {  // 從1壘開始查看
      if (baseArray[i - 1] == 1) {  // 前一個壘包
        if (baseArray[i] == 1) {  // 當下的壘包有人
          forceOutSet.add(String.valueOf(i) + "B");

          if ((i + 1) == 4) {
            forceOutSet.add("HB");  // 本壘
          } else {
            forceOutSet.add(String.valueOf(i + 1) + "B");
          }

        } else {   // 當下的壘包無人
          forceOutSet.add(String.valueOf(i) + "B");
        }
      }
    }
    return forceOutSet.toString();
  } // end of getForceOut

  public static void main(String[] args) {
    ForceOut2 forceOut2 = new ForceOut2();
    System.out.println(forceOut2.getForceOut(0,0,0));  // 1B
    System.out.println(forceOut2.getForceOut(1,0,0));  // 1B, 2B
    System.out.println(forceOut2.getForceOut(1,0,1));  // 1B, 2B
    System.out.println(forceOut2.getForceOut(0,0,1));  // 1B
    System.out.println(forceOut2.getForceOut(1,1,1));  // 1B, 2B, 3B, HB

  }

} // end of ForceOut
