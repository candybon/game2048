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

import java.awt.event.KeyEvent;

import com.chen.game.kernel.Configuration;
import com.chen.game.kernel.GameEngine;
import com.chen.game.kernel.GameListener;

public class Game2048App implements GameListener {
    private Configuration conf = new Configuration();
    private SwingDisplay gameBoard = new SwingDisplay(conf);
    
    private GameEngine engine = new GameEngine(conf, this);
    
    private String gameStatus = "";

    public Game2048App() {
	gameBoard.addKeyListener((e) -> {
	    switch (e.getKeyCode()) {
	    case KeyEvent.VK_LEFT:
		engine.left();
		break;
	    case KeyEvent.VK_RIGHT:
		engine.right();
		break;
	    case KeyEvent.VK_DOWN:
		engine.down();
		break;
	    case KeyEvent.VK_UP:
		engine.up();
		break;
	    }
	    gameBoard.render(engine.getData(), "Score: " + engine.getScore() + gameStatus);
	});
    }

    public void start() {
	engine.reset();
	gameBoard.render(engine.getData(), "Score: " + engine.getScore() + gameStatus);
    }

    @Override
    public void win() {
	gameStatus = " You Won!";
	gameBoard.render(engine.getData(), "Score: " + engine.getScore() + gameStatus);
    }

    @Override
    public void lose() {
	gameStatus = " Game Over!";
	gameBoard.render(engine.getData(), "Score: " + engine.getScore() + gameStatus);
    }
    
    public static void main(String[] args) {
	Game2048App game = new Game2048App();
	game.start();
    }
}
