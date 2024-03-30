package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;

public class RouletteBet {

  private static final int MAX_VERTICAL_SPLITS = 24;
  private static final int SPLITS_IN_ROW = 2;
  private static final int NUMBERS_IN_ROW = 3;

  private int amount;
  private final RouletteBetTypes betType;
  private final int betPositionInTable;  
  private final ArrayList<Integer> winningNumbers;
  private final Map<RouletteBetTypes, Integer> betTypePayoutMap;


  public RouletteBet(Map<RouletteBetTypes, Integer> betTypePayoutMap, RouletteBetTypes betType, int betPositionInTable) {
    this.betTypePayoutMap = betTypePayoutMap;
    this.betType = betType;
    this.betPositionInTable = betPositionInTable;
    this.winningNumbers = this.getWinningNumbers(betType, betPositionInTable);
  }

  private ArrayList<Integer> getWinningNumbers(RouletteBetTypes betType, int betPositionInTable) {
    ArrayList<Integer> numbers = new ArrayList<>();
    switch (betType) {
      case STRAIGHT_UP:
        numbers.add(betPositionInTable);
        break;
      case SPLIT:
        if (betPositionInTable < MAX_VERTICAL_SPLITS) {
          int topNum = calcTopNumberBasedOnSplitPosition(betPositionInTable);
          numbers.add(topNum - 1);
          numbers.add(topNum);
        } else {
          int bottomNum = betPositionInTable - MAX_VERTICAL_SPLITS;
          numbers.add(bottomNum);
          numbers.add(bottomNum + 1);
        }
        break; 
      default:
        numbers.add(betPositionInTable);
        break;
    }

    return numbers;
  }

  private int calcTopNumberBasedOnSplitPosition(int position) {
    double number = ((double) position / SPLITS_IN_ROW) * NUMBERS_IN_ROW;

    if (number % 1 > 0) {
      int integerPart = (int) number; 
      double firstDecimal = number - integerPart;

      if (firstDecimal > 0) {
        number = Math.ceil(number); 
      }
    }

    return (int) number;
  }

}

