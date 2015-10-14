/*
 * Copyright (c) XIAOWEI CHEN, 2014.
 * All Rights Reserved. Reproduction in whole or in part is prohibited
 * without the written consent of the copyright owner.
 * 
 * XIAOWEI CHEN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. XIAOWEI CHEN SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * 
 * All rights reserved.
 */
package com.chen.game.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.chen.game.kernel.Configuration;

public class SwingDisplay extends JPanel implements KeyListener {

    private Configuration conf;
    private Consumer<KeyEvent> keyPressFunction;
    private int[][] matrix;
    private String message = "";

    public SwingDisplay(Configuration conf) {
	this.conf = conf;
	init();
    }
    
    private void init() {
	JFrame game = new JFrame();
	game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	game.setSize(conf.COLS * (Tile.MARGIN + Tile.SIZE) + Tile.MARGIN, (conf.ROWS+1)  * (Tile.MARGIN + Tile.SIZE));
	game.add(this);
	game.setVisible(true);
	this.setFocusable(true);
    }
    
    public void addKeyListener(Consumer<KeyEvent> function) {
	this.keyPressFunction = function;
	this.addKeyListener(this);
    }
    
    public void render(int[][] matrix, String status) {
	this.matrix = matrix;
	this.message = status;
	this.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if(keyPressFunction != null) {
	    keyPressFunction.accept(e);
	}
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
    @Override
    public void paint(Graphics g) {
	super.paint(g);
	g.setColor(new Color(0xbbada0));
	g.fillRect(0, 0, this.getSize().width, this.getSize().height);

	if(matrix == null) return;
	for (int i = 0; i < conf.ROWS; i++) {
	    for (int j = 0; j < conf.COLS; j++) {
		paintTile(i, j, matrix[i][j], g);
	    }
	}
	
	g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(message, Tile.MARGIN, 2 * Tile.MARGIN + (Tile.SIZE + Tile.MARGIN) * conf.ROWS);
    }

    private void paintTile(int row, int col, int val, Graphics gRaw) {
	Graphics2D g = ((Graphics2D) gRaw);
	int x = Tile.MARGIN + (Tile.SIZE + Tile.MARGIN) * col;
	int y = Tile.MARGIN + (Tile.SIZE + Tile.MARGIN) * row;

	Tile tile = new Tile(val);

	g.setColor(tile.getBackground());
	g.fillRoundRect(x, y, Tile.SIZE, Tile.SIZE, Tile.RADIUS, Tile.RADIUS);
	g.setColor(tile.getTextColor());

	final int size = val < 100 ? 36 : val < 1000 ? 32 : 24;
	final Font font = new Font("Arial", Font.BOLD, size);
	g.setFont(font);

	String s = String.valueOf(val);
	final FontMetrics fm = getFontMetrics(font);

	final int w = fm.stringWidth(s);
	final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

	if (val != 0) {
	    g.drawString(s, x + (Tile.SIZE - w) / 2, y + Tile.SIZE - (Tile.SIZE - h) / 2 - 2);
	}
    }
    
    private class Tile {
	public static final int SIZE = 64;
	public static final int MARGIN = 16;
	public static final int RADIUS = 14;

	int value;

	public Tile(int num) {
	    value = num;
	}

	public Color getTextColor() {
	    return value < 16 ? new Color(0x776e65) : new Color(0xf9f6f2);
	}

	public Color getBackground() {
	    switch (value) {
	    case 2:
		return new Color(0xeee4da);
	    case 4:
		return new Color(0xede0c8);
	    case 8:
		return new Color(0xf2b179);
	    case 16:
		return new Color(0xf59563);
	    case 32:
		return new Color(0xf67c5f);
	    case 64:
		return new Color(0xf65e3b);
	    case 128:
		return new Color(0xedcf72);
	    case 256:
		return new Color(0xedcc61);
	    case 512:
		return new Color(0xedc850);
	    case 1024:
		return new Color(0xedc53f);
	    case 2048:
		return new Color(0xedc22e);
	    case 4096:
                return new Color(0xedc22f);
	    }
	    return new Color(0xcdc1b4);
	}
    }

}
