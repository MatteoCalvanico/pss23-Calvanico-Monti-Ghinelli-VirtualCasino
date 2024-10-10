package it.unibo.virtualCasino.model.games.impl.roulette.dtos;

import it.unibo.virtualCasino.model.games.impl.roulette.enums.RouletteBetType;

/**
 * The {@code RouletteBetIndicatorDto} class represents a bet indicator in
 * roulette,
 * including the type of bet, the position number of the bet, and its
 * coordinates on the layout.
 * It is immutable, as all fields are declared {@code final}.
 */
public class RouletteBetIndicatorDto {

    /**
     * The type of bet placed in roulette.
     */
    public final RouletteBetType betType;

    /**
     * The position number associated with the bet.
     */
    public final int betPositionNumber;

    /**
     * The coordinates of the bet on the layout.
     */
    public final Coordinate coordinate;

    /**
     * Constructs a {@code RouletteBetIndicatorDto} object with the specified bet
     * type,
     * position number, and coordinates.
     *
     * @param betType           the type of bet placed.
     * @param betPositionNumber the position number associated with the bet.
     * @param coordinate        the coordinates of the bet on the layout.
     */
    public RouletteBetIndicatorDto(RouletteBetType betType, int betPositionNumber, Coordinate coordinate) {
        this.betType = betType;
        this.betPositionNumber = betPositionNumber;
        this.coordinate = coordinate;
    }
}
