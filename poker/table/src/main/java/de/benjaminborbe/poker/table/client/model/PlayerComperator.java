package de.benjaminborbe.poker.table.client.model;

import java.util.Comparator;

public class PlayerComperator implements Comparator<Player> {

    @Override
    public int compare(final Player o1, final Player o2) {
        if (o1.getScore() > o2.getScore()) {
            return -1;
        } else if (o1.getScore() < o2.getScore()) {
            return 1;
        }
        return 0;
    }
}
