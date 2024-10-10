package it.unibo.virtualCasino.model.games.impl.roulette.dtos;

/**
 * The {@code LayoutDto} class represents layout data with top-left, top-right,
 * bottom-right, and bottom-left coordinates, along with optional horizontal
 * and vertical offsets for lines.
 * It is immutable, as all fields are declared {@code final}.
 */
public class RouletteTableLayoutDto {

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
     * The vertical offset for horizontal lines in the layout.
     */
    public final double horizontalLinesVerticalOffset;

    /**
     * The vertical offset for vertical lines in the layout.
     */
    public final double verticalLinesVerticalOffset;

    /**
     * Copy constructor for creating a new {@code LayoutDto} object based on an
     * existing one.
     * All fields are copied from the provided {@code layoutDto} object.
     *
     * @param layoutDto the {@code LayoutDto} object to copy.
     */
    public RouletteTableLayoutDto(RouletteTableLayoutDto layoutDto) {
        this(
                layoutDto.topLeftCoordinate,
                layoutDto.topRightCoordinate,
                layoutDto.bottomRightCoordinate,
                layoutDto.bottomLeftCoordinate,
                layoutDto.horizontalLinesVerticalOffset,
                layoutDto.verticalLinesVerticalOffset);
    }

    /**
     * Constructs a {@code LayoutDto} object with only top-left and bottom-right
     * coordinates.
     * Other coordinates are initialized based on a simple rectangular layout
     * assumption.
     * Offsets are initialized to zero.
     *
     * @param topLeftCoordinate     the top-left coordinate of the layout.
     * @param bottomRightCoordinate the bottom-right coordinate of the layout.
     */
    public RouletteTableLayoutDto(Coordinate topLeftCoordinate, Coordinate bottomRightCoordinate) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
        this.topRightCoordinate = new Coordinate(bottomRightCoordinate.xAxisValue, topLeftCoordinate.yAxisValue);
        this.bottomLeftCoordinate = new Coordinate(topLeftCoordinate.xAxisValue, bottomRightCoordinate.yAxisValue);
        this.horizontalLinesVerticalOffset = 0;
        this.verticalLinesVerticalOffset = 0;
    }

    /**
     * Constructs a {@code LayoutDto} object with all four corner coordinates.
     * Offsets are initialized to zero.
     *
     * @param topLeftCoordinate     the top-left coordinate of the layout.
     * @param topRightCoordinate    the top-right coordinate of the layout.
     * @param bottomRightCoordinate the bottom-right coordinate of the layout.
     * @param bottomLeftCoordinate  the bottom-left coordinate of the layout.
     */
    public RouletteTableLayoutDto(
            Coordinate topLeftCoordinate,
            Coordinate topRightCoordinate,
            Coordinate bottomRightCoordinate,
            Coordinate bottomLeftCoordinate) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.topRightCoordinate = topRightCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
        this.bottomLeftCoordinate = bottomLeftCoordinate;
        this.horizontalLinesVerticalOffset = 0;
        this.verticalLinesVerticalOffset = 0;
    }

    /**
     * Constructs a {@code LayoutDto} object with all four corner coordinates,
     * as well as vertical offsets for both horizontal and vertical lines.
     *
     * @param topLeftCoordinate             the top-left coordinate of the layout.
     * @param topRightCoordinate            the top-right coordinate of the layout.
     * @param bottomRightCoordinate         the bottom-right coordinate of the
     *                                      layout.
     * @param bottomLeftCoordinate          the bottom-left coordinate of the
     *                                      layout.
     * @param horizontalLinesVerticalOffset the vertical offset for horizontal
     *                                      lines.
     * @param verticalLinesVerticalOffset   the vertical offset for vertical lines.
     */
    public RouletteTableLayoutDto(
            Coordinate topLeftCoordinate,
            Coordinate topRightCoordinate,
            Coordinate bottomRightCoordinate,
            Coordinate bottomLeftCoordinate,
            double horizontalLinesVerticalOffset,
            double verticalLinesVerticalOffset) {
        this.topLeftCoordinate = topLeftCoordinate;
        this.topRightCoordinate = topRightCoordinate;
        this.bottomRightCoordinate = bottomRightCoordinate;
        this.bottomLeftCoordinate = bottomLeftCoordinate;
        this.horizontalLinesVerticalOffset = horizontalLinesVerticalOffset;
        this.verticalLinesVerticalOffset = verticalLinesVerticalOffset;
    }
}