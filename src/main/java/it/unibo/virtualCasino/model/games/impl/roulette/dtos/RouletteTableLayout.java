package it.unibo.virtualCasino.model.games.impl.roulette.dtos;

/**
 * The {@code LayoutDto} class represents layout data with top-left, top-right,
 * bottom-right, and bottom-left coordinates.
 * It is immutable, as all fields are declared {@code final}.
 */
public class RouletteTableLayout {

    /**
     * The top-left coordinate of the layout.
     */
    public final Coordinate topLeftCoordinate;

    /**
     * The top-right coordinate of the layout.
     */
    public final Coordinate topRightCoordinate;

    /**
     * The bottom-right coordinate of the layout.
     */
    public final Coordinate bottomRightCoordinate;

    /**
     * The bottom-left coordinate of the layout.
     */
    public final Coordinate bottomLeftCoordinate;

    /**
     * Copy constructor for creating a new {@code LayoutDto} object based on an
     * existing one.
     * All fields are copied from the provided {@code layoutDto} object.
     *
     * @param layoutDto the {@code LayoutDto} object to copy.
     */
    public RouletteTableLayout(RouletteTableLayout layoutDto) {
        this(
                layoutDto.topLeftCoordinate,
                layoutDto.topRightCoordinate,
                layoutDto.bottomRightCoordinate,
                layoutDto.bottomLeftCoordinate);
    }

    /**
     * Constructs a {@code LayoutDto} object with only top-left and bottom-right
     * coordinates.
     * Other coordinates are initialized based on a simple rectangular layout
     * assumption.
     *
     * @param topLeftCoordinate     the top-left coordinate of the layout.
     * @param bottomRightCoordinate the bottom-right coordinate of the layout.
     */
    public RouletteTableLayout(Coordinate topLeftCoordinate, Coordinate bottomRightCoordinate) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
        this.topRightCoordinate = new Coordinate(bottomRightCoordinate.xAxisValue, topLeftCoordinate.yAxisValue);
        this.bottomLeftCoordinate = new Coordinate(topLeftCoordinate.xAxisValue, bottomRightCoordinate.yAxisValue);
    }

    /**
     * Constructs a {@code LayoutDto} object with all four corner coordinates.
     *
     * @param topLeftCoordinate     the top-left coordinate of the layout.
     * @param topRightCoordinate    the top-right coordinate of the layout.
     * @param bottomRightCoordinate the bottom-right coordinate of the layout.
     * @param bottomLeftCoordinate  the bottom-left coordinate of the layout.
     */
    public RouletteTableLayout(
            Coordinate topLeftCoordinate,
            Coordinate topRightCoordinate,
            Coordinate bottomRightCoordinate,
            Coordinate bottomLeftCoordinate) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.topRightCoordinate = topRightCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
        this.bottomLeftCoordinate = bottomLeftCoordinate;
    }
}