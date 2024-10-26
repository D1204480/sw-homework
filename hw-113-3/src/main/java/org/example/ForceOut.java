package org.example;

public class ForceOut {

  public String calForceOut(int oneB, int twoB, int threeB) {
    /* 封殺狀況:
    x       -> 1B
    1B      -> 1B, 2B
    1B, 3B  -> 1B, 2B
    3B      -> 1B
     */

    // 計算每個壘包有沒有人: [HB, 1B, 2B, 3B], 0 表示沒有人, 1 表示有人
    int[] baseArray = new int[]{1, oneB, twoB, threeB};

    // 儲存封殺的壘包位置
    StringBuilder forceOutBases = new StringBuilder();

    if (baseArray[1] == 1) {
      // 如果一壘有人，則一二壘會有封殺
      forceOutBases.append("1B, 2B");

    } else if (baseArray[2] == 1) { // 如果二壘有人
      if (baseArray[1] == 1) {  // 一壘也有人
        forceOutBases.append("1B, 2B, 3B");
      } else {
        forceOutBases.append("1B");
      }

    } else if (baseArray[3] == 1) {  // 三壘有人
      if (baseArray[2] == 1) {  // 二壘有人
        if (baseArray[1] == 1) {  // 一壘有人
          forceOutBases.append("1B, 2B, 3B");
        } else {
          forceOutBases.append("1B");
        }
      } else {
        if (baseArray[1] == 1) {   // 二壘無人, 一壘有人
          forceOutBases.append("1B, 2B");
        } else {
          if (baseArray[1] == 0) {
            forceOutBases.append("1B");
          }
        }
      }

    } else {  // 壘包上無人
      forceOutBases.append("1B");
    }

    // 輸出封殺壘包結果
    System.out.println(forceOutBases.toString());

    return forceOutBases.toString();
  }

  public static void main(String[] args) {
    ForceOut forceOut = new ForceOut();
     // 測試案例
    forceOut.calForceOut(0, 0, 0);  // 結果應為封殺壘包: 1B
    forceOut.calForceOut(1, 0, 0);  // 結果應為封殺壘包: 1B, 2B
    forceOut.calForceOut(1, 0, 3);  // 結果應為封殺壘包: 1B, 2B
    forceOut.calForceOut(0, 0, 1);  // 結果應為封殺壘包: 1B
  }
}


