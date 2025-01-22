package com.nate.rpg.state.menu;

import java.awt.Color;
import java.awt.Graphics2D;

public class MainMenu extends Menu {

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.RED);
        g.drawRect(50, 50, 50, 50);
    }
}