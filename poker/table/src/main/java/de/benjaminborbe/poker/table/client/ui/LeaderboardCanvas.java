package de.benjaminborbe.poker.table.client.ui;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.ArrayList;

public class LeaderboardCanvas extends DrawingArea {

    private int canvasWidth = 0;

    private int canvasHeight = 0;

    private Group leaderboardGroup = new Group();

    public LeaderboardCanvas(final int width, final int height) {
        super(width, height);
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.add(leaderboardGroup);

    }

    public void updateLeaderboard(ArrayList<String> sortedPlayerList) {
        leaderboardGroup.clear();
				int diffy = 20;
        for (String playerString : sortedPlayerList) {
            Text gameName = new Text(10, diffy, playerString);
            gameName.setFillColor("black");
						leaderboardGroup.add(gameName);
						diffy = diffy + 20;
        }
    }

}
