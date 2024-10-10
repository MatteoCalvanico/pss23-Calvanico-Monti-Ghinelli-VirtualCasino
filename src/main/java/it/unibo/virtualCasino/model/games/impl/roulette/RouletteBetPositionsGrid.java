package it.unibo.virtualCasino.model.games.impl.roulette;

import java.util.ArrayList;

import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteTableLayoutDto;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.Coordinate;
import it.unibo.virtualCasino.model.games.impl.roulette.dtos.RouletteBetIndicatorDto;
import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;
import it.unibo.virtualCasino.view.roulette.utils.RouletteViewInfo;

/**
 * The {@code RouletteBetPositionsGrid} class manages the layout of bets in a
 * roulette game. It prepares and stores indicators for various bet types based
 * on the provided table layout data.
 */
public class RouletteBetPositionsGrid extends RouletteBase {

    /**
     * The layout of the roulette table.
     */
    private RouletteTableLayoutDto tableLayout;

    /**
     * The list of bet position indicators.
     */
    private ArrayList<RouletteBetIndicatorDto> betPositionIdicatorsList;

    /**
     * Constructs a {@code RouletteBetPositionsGrid} with the specified table
     * layout.
     *
     * @param tableLayout the layout of the roulette table
     */
    public RouletteBetPositionsGrid(RouletteTableLayoutDto tableLayout) {
        this.tableLayout = new RouletteTableLayoutDto(tableLayout);
        prepareBetPositionIdicatorsLayoutData();
    }

    /**
     * Returns a copy of the list of bet position indicators.
     *
     * @return a copy of the bet position indicators list
     */
    public ArrayList<RouletteBetIndicatorDto> getBetPositionIdicatorsList() {
        return new ArrayList<>(betPositionIdicatorsList);
    }

    /**
     * Prepares the layout data for bet position indicators by creating various
     * types of bet indicators.
     */
    private void prepareBetPositionIdicatorsLayoutData() {

        // Bet type: Split
        createSplitBetsPositionsInidicators();

        // Bet type: Street
        createStreetBetsPositionsInidicators();

        // Bet type: Double
        createDoubleStreetBetsPositionsInidicators();

        // Bet type: Corner
        createCornerBetsPositionsInidicators();

        // Bet type: Column
        createColumnBetsPositionsInidicators();

        // Bet type: Dozen
        createDozenBetsPositionsInidicators();

        // Bet type: Half
        createHalfBetsPositionsInidicators();

        // Bet type: Even
        createEvenOddBetsPositionsInidicators();

        // Bet type: Red or Black
        createRedBlackBetsPositionsInidicators();
    }

    /**
     * Creates position indicators for split bets and adds them to the list.
     */
    private void createSplitBetsPositionsInidicators() {
        // Creates horizontal split bets indicators layout data items
        int splitBetsCounter = 1;
        for (int i = 1; i < this.TABLE_COLS; i++) {
            double yAxisValue = tableLayout.topLeftCoordinate.yAxisValue
                    + (tableLayout.horizontalLinesVerticalOffset * i);

            for (int j = 0; j < this.MAX_VERTICAL_SPLITS; j += 2) {
                double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                        + (tableLayout.verticalLinesVerticalOffset * (j + 1) / 2);

                betPositionIdicatorsList.add(
                        new RouletteBetIndicatorDto(
                                RouletteBetType.SPLIT,
                                splitBetsCounter++,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }

        // Creates vertical split bets positions indicators
        for (int i = 0; i < this.TABLE_COLS; i++) {
            double yAxisValue = tableLayout.topLeftCoordinate.yAxisValue
                    + (tableLayout.horizontalLinesVerticalOffset * i)
                    + tableLayout.horizontalLinesVerticalOffset / 2;

            for (int j = 1; j <= this.MAX_HORIZONTAL_SPLITS / 3; j++) {
                double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                        + (tableLayout.verticalLinesVerticalOffset * j);

                betPositionIdicatorsList.add(
                        new RouletteBetIndicatorDto(
                                RouletteBetType.SPLIT,
                                splitBetsCounter++,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }
    }

    /**
     * Creates position indicators for street bets and adds them to the list.
     */
    private void createStreetBetsPositionsInidicators() {
        for (int i = 0; i < RouletteViewInfo.V_SPLIT_BETS; i += 2) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (tableLayout.verticalLinesVerticalOffset * (i + 1) / 2);

            betPositionIdicatorsList.add(
                    new RouletteBetIndicatorDto(
                            RouletteBetType.STREET,
                            i + 1,
                            new Coordinate(xAxisValue, tableLayout.topLeftCoordinate.yAxisValue)));
        }
    }

    /**
     * Creates position indicators for double street bets and adds them to the list.
     */
    private void createDoubleStreetBetsPositionsInidicators() {
        for (int i = 1; i < RouletteViewInfo.V_SPLIT_BETS / 2; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                    + (tableLayout.verticalLinesVerticalOffset * (i));

            betPositionIdicatorsList.add(
                    new RouletteBetIndicatorDto(
                            RouletteBetType.DOUBLE_STREET,
                            i + 1,
                            new Coordinate(xAxisValue, tableLayout.bottomRightCoordinate.yAxisValue)));
        }
    }

    /**
     * Creates position indicators for corner bets and adds them to the list.
     */
    private void createCornerBetsPositionsInidicators() {
        for (int i = 1; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double yAxisValue = tableLayout.topLeftCoordinate.yAxisValue
                    + (tableLayout.horizontalLinesVerticalOffset * i);

            for (int j = 0; j < (RouletteViewInfo.V_SPLIT_BETS / 2) - 1; j++) {
                double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue
                        + (tableLayout.verticalLinesVerticalOffset * (j + 1));

                betPositionIdicatorsList.add(
                        new RouletteBetIndicatorDto(
                                RouletteBetType.CORNER,
                                i,
                                new Coordinate(xAxisValue, yAxisValue)));
            }
        }
    }

    /**
     * Creates position indicators for column bets and adds them to the list.
     */
    private void createColumnBetsPositionsInidicators() {
        double halfHorizontalLinesVerticalOffset = tableLayout.horizontalLinesVerticalOffset / 2;
        double xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue
                - tableLayout.verticalLinesVerticalOffset / 2;

        for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double yAxisValue = tableLayout.topLeftCoordinate.yAxisValue
                    + (tableLayout.horizontalLinesVerticalOffset * i)
                    + halfHorizontalLinesVerticalOffset;

            betPositionIdicatorsList.add(
                    new RouletteBetIndicatorDto(
                            RouletteBetType.COLUMN,
                            i,
                            new Coordinate(xAxisValue, yAxisValue)));
        }
    }

    /**
     * Creates position indicators for dozen bets and adds them to the list.
     */
    // TODO cos'è quel 4 e quel 3
    private void createDozenBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomRightCoordinate.yAxisValue
                + (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / 4);
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / 3;
        double halfOffset = offset / 2;

        for (int i = 0; i < RouletteViewInfo.H_LINES_COUNT; i++) {
            double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + ((offset * i) + (halfOffset));

            betPositionIdicatorsList.add(
                    new RouletteBetIndicatorDto(
                            RouletteBetType.DOZEN,
                            i,
                            new Coordinate(xAxisValue, yAxisValue)));
        }
    }

    /**
     * Creates position indicators for half bets and adds them to the list.
     */
    // TODO cos'è quel 4 e quel 6
    private void createHalfBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / 4);
        double offset = ((tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / 6)
                / 2;

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + (offset / 2);
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.HALF,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue
                - tableLayout.verticalLinesVerticalOffset
                - (offset / 2);
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.HALF,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));

    }

    /**
     * Creates position indicators for even or odd bets and adds them to the list.
     */
    // TODO cos'è quel 4 e quel 6
    private void createEvenOddBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / 4);
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / 6;

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + (offset / 2) + offset;
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.ODD_EVEN,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - (offset / 2) - offset;
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.ODD_EVEN,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));
    }

    /**
     * Creates position indicators for red or black bets and adds them to the list.
     */
    // TODO cos'è quel 4 e quel 6
    private void createRedBlackBetsPositionsInidicators() {
        double yAxisValue = tableLayout.bottomLeftCoordinate.yAxisValue
                - (Math.abs(tableLayout.bottomRightCoordinate.yAxisValue - tableLayout.bottomLeftCoordinate.yAxisValue)
                        / 4);
        double offset = (tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - tableLayout.topLeftCoordinate.xAxisValue) / 6;

        double xAxisValue = tableLayout.topLeftCoordinate.xAxisValue + (offset / 2) + offset * 2;
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.RED_BLACK,
                        1,
                        new Coordinate(xAxisValue, yAxisValue)));

        xAxisValue = tableLayout.bottomRightCoordinate.xAxisValue - tableLayout.verticalLinesVerticalOffset
                - (offset / 2) - offset * 2;
        betPositionIdicatorsList.add(
                new RouletteBetIndicatorDto(
                        RouletteBetType.RED_BLACK,
                        2,
                        new Coordinate(xAxisValue, yAxisValue)));
    }
}
