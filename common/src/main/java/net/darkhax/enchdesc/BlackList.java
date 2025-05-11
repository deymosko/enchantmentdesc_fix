package net.darkhax.enchdesc;

import java.util.ArrayList;
import java.util.List;

public class BlackList
{
    public static String TUNNELING =          "betterarcheology:tunneling";
    public static String PENETRATING_STRIKE = "betterarcheology:penetrating_strike";
    public static String SOARING_WINDS =      "betterarcheology:soaring_winds";

    public static List<String> blacklist = List.of(
            TUNNELING,
            PENETRATING_STRIKE,
            SOARING_WINDS
    );

}
