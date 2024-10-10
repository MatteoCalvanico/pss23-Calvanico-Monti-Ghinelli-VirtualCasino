package it.unibo.virtualCasino.model.games.impl.roulette.dtos;

/**
 * The {@code Coordinate} class represents a point in a two-dimensional plane,
 * with X and Y axis values.
 * It is immutable, as both fields are declared {@code final}.
 */
public class CoordinateDto {

    /**
     * The X-axis value of the coordinate.
     */
    public final double xAxisValue;

    /**
     * The Y-axis value of the coordinate.
     */
    public final double yAxisValue;

    /**
     * Constructs a {@code Coordinate} object with the specified X and Y values.
     *
     * @param xAxisValue the value of the X-axis.
     * @param yAxisValue the value of the Y-axis.
     */
    public CoordinateDto(double xAxisValue, double yAxisValue) {
        this.xAxisValue = xAxisValue;
        this.yAxisValue = yAxisValue;
    }
}
