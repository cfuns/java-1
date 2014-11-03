package de.benjaminborbe.poker.table.client.ui;

import de.benjaminborbe.poker.table.client.model.Player;
import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Group;
import org.vaadin.gwtgraphics.client.shape.Rectangle;
import org.vaadin.gwtgraphics.client.shape.Text;

import java.util.Collection;

public class LeaderboardCanvas extends DrawingArea {

    private Group leaderboardGroup = new Group();

    private Group leaderboardHead = new Group();

    public LeaderboardCanvas(final int width, final int height) {
        super(width, height);
        createLeaderboardHead();
        this.add(createBorder(width, height));
        this.add(leaderboardHead);
        this.add(leaderboardGroup);
    }

    public void updateLeaderboard(Collection<Player> sortedPlayerList) {
        leaderboardGroup.clear();
        int diffy = 60;
        for (Player player : sortedPlayerList) {
            Text name = new Text(10, diffy, player.getUsername());
            name.setFillColor("black");
            leaderboardGroup.add(name);

            Text score = new Text(100, diffy, player.getScore() + "");
            score.setFillColor("black");
            leaderboardGroup.add(score);
            diffy = diffy + 20;
        }
    }

    private void createLeaderboardHead() {
        leaderboardHead.clear();
        Text text = new Text(10, 20, "Leaderboard:");
        text.setFillColor("red");
        leaderboardHead.add(text);
    }

    private Rectangle createBorder(final int width, final int height) {
        final Rectangle border = new Rectangle(this.getAbsoluteLeft() + width - 2, 0, 2, height);
        border.setFillColor("black");
        return border;
    }
}
