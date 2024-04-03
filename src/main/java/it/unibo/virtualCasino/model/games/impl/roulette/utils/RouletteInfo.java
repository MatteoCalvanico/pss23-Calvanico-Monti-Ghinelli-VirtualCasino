package it.unibo.virtualCasino.model.games.impl.roulette.utils;

public class RouletteInfo {
  public static final int[] ROULETTE_SEQUENCE = {
      0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 
      36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 
      31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26
  };
  public static final int NUMS_TOTAL = 36;  
  public static final int TABLE_ROWS = 12;
  public static final int TABLE_COLS = 3;
  public static final int DOUBLE_STREET_W_NUMS = 6;
  public static final int SPLITS_IN_ROW = 2;
  public static final int MAX_VERTICAL_SPLITS = 24;
}
