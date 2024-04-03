package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.ArrayList;
import java.util.Map;

import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteBetTypes;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteColors;
import it.unibo.virtualCasino.model.games.impl.roulette.utils.RouletteInfo;

public class RouletteBet {

  private int amount;
  private final RouletteBetTypes betType;
  private final int betPositionInTable;
  private final ArrayList<Integer> winningNumbers;
  private final Map<RouletteBetTypes, Integer> betTypePayoutMap;
  private final Map<Integer, RouletteColors> colorNumberMap;


  public RouletteBet(
    Map<RouletteBetTypes, Integer> betTypePayoutMap,
    Map<Integer, RouletteColors> colorNumberMap,
    RouletteBetTypes betType,
    int betPositionInTable
  ) {
    this.betTypePayoutMap = betTypePayoutMap;
    this.colorNumberMap = colorNumberMap;
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
        {
          if (betPositionInTable < RouletteInfo.MAX_VERTICAL_SPLITS) {
            int topNum = calcTopNumberBasedOnPosition(betPositionInTable);
            numbers.add(topNum - 1);
            numbers.add(topNum);
          } else {
            int bottomNum = betPositionInTable - RouletteInfo.MAX_VERTICAL_SPLITS;
            numbers.add(bottomNum);
            numbers.add(bottomNum + RouletteInfo.TABLE_COLS);
          }
        }
        break;
      case STREET:
        {
          int topNum = betPositionInTable * RouletteInfo.TABLE_COLS;
          numbers.add(topNum++);
          numbers.add(topNum++);
          numbers.add(topNum);
        }
        break;
      case CORNER:
        {
          int topNum = calcTopNumberBasedOnPosition(betPositionInTable);
          numbers.add(topNum);
          numbers.add(topNum - 1);
          numbers.add(topNum + 2);
          numbers.add(topNum + 3);
        }
        break;
      case DOUBLE_STREET:
        {
          int startingNum = (betPositionInTable * RouletteInfo.TABLE_COLS) - 2;
          for(int i = 0; i < RouletteInfo.DOUBLE_STREET_W_NUMS; i++) {
            numbers.add(startingNum + i);
          }
        }
        break;
      case COLUMN:
        {
          int startingNum = betPositionInTable;
          for(int i = 0; i < RouletteInfo.TABLE_ROWS; i++) {
            numbers.add(startingNum);
            startingNum += RouletteInfo.TABLE_COLS;
          }
        }
        break;
      case DOZEN:
        {
          int startingNum = betPositionInTable * RouletteInfo.TABLE_ROWS;
          for(int i = startingNum; i > startingNum - RouletteInfo.TABLE_ROWS; i--) {
            numbers.add(i);
          }
        }
        break;
      case ODD_EVEN:
        {
          if (betPositionInTable == 1) {
            for (int i = 1; i <= 36; i += 2) {
              numbers.add(i);
            }
          } else {
            for (int i = 0; i <= 36; i += 2) {
              numbers.add(i);
            }
          }
        }
        break;
      case RED_BLACK:
        {         
          for (Map.Entry<Integer, RouletteColors> entry : colorNumberMap.entrySet()) {
            if (betPositionInTable == 1 && entry.getValue() == RouletteColors.RED) {
              numbers.add(entry.getKey());
            } else if (betPositionInTable == 2 && entry.getValue() == RouletteColors.BLACK) {
              numbers.add(entry.getKey());
            }
          }
        }
        break;
      case HALF:
        {
          int startingNum = betPositionInTable * (RouletteInfo.NUMS_TOTAL / 2);
          for(int i = startingNum; i > startingNum - (RouletteInfo.NUMS_TOTAL / 2); i--) {
            numbers.add(i);
          }
        }
        break;
      default:
        break;
    }

    return numbers;
  }

  private int calcTopNumberBasedOnPosition(int position) {
    double number = ((double) position / RouletteInfo.SPLITS_IN_ROW) * RouletteInfo.TABLE_COLS;

    //Ceil If First Decimal Greater Than Zero
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

