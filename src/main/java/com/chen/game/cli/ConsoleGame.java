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

package com.chen.game.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.chen.game.kernel.Configuration;
import com.chen.game.kernel.GameEngine;
import com.chen.game.kernel.GameListener;

public class ConsoleGame implements GameListener {

    private final Configuration conf = new Configuration();
    private final GameEngine engine = new GameEngine(conf, this);

    public void start() throws IOException {
	engine.reset();
	paint();

	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	String cmd;
	while ((cmd = reader.readLine()) != null) {
	    switch (cmd) {
	    case "w":
		engine.up();
		break;
	    case "a":
		engine.left();
		break;
	    case "s":
		engine.down();
		break;
	    case "d":
		engine.right();
		break;
	    case "q":
		System.exit(0);
	    default:
		continue;
	    }
	    paint();
	}
    }

    private void paint() {
	int[][] data = engine.getData();
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < conf.ROWS; i++) {
	    for (int j = 0; j < conf.COLS; j++) {
		sb.append(String.format("%5d", data[i][j]));
	    }
	    sb.append('\n');
	}
	System.out.print("\033[H\033[2J");
	System.out.println("w==>UP\ns==>DOwN\na==>LEFT\nd==>RIGHT\nq==>QUIT");
	System.out.print(sb.toString());
    }

    @Override
    public void win() {
	System.out.println("Winner");
	System.exit(0);
    }

    @Override
    public void lose() {
	System.out.println("Game Over.");
	System.exit(0);
    }

    public static void main(String[] args) throws IOException {
	new ConsoleGame().start();
    }
}
