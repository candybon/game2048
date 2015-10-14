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
package com.chen.game.kernel;

import com.chen.game.kernel.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameEngine {
    private int score = 0;
    private int steps = 0;
    private final Cell[][] matrix;
    private final int[][] data;
    private final Configuration conf;
    private final List<Cell> freeCells = new ArrayList<>();
    
    private final GameListener game;

    public GameEngine(Configuration conf, GameListener game) {
	this.game = game;
	this.conf = conf;
	matrix = new Cell[conf.ROWS][conf.COLS];
	data = new int[conf.ROWS][conf.COLS];
	for (int i = 0; i < conf.ROWS; i++) {
	    for (int j = 0; j < conf.COLS; j++) {
		matrix[i][j] = new Cell(i, j);
		freeCells.add(matrix[i][j]);
	    }
	}
    }

    public int[][] getData() {
	for(int i = 0; i < conf.ROWS; i++) {
	    for(int j = 0; j < conf.COLS; j++) {
		data[i][j] = matrix[i][j].val;
	    }
	}
	return data;
    }

    public void reset() {
	score = 0;
	steps = 0;
	spawn();
	spawn();
    }

    private boolean spawnable = true;

    private void spawn() {
	steps++;
	int val = new Random().nextBoolean() ? 2 : 4;
	int idx = new Random().nextInt(freeCells.size());
	Cell luckyCell = freeCells.get(idx);
	luckyCell.val = val;
	freeCells.set(idx, freeCells.get(freeCells.size() - 1));
	freeCells.remove(freeCells.size() - 1);
    }

    public void up() {
	Runnable upwards = () -> {
	    List<Cell> cells = new ArrayList<>();
	    for (int c = 0; c < conf.COLS; c++) {
		cells.clear();
		for (int r = 0; r < conf.ROWS; r++) {
		    cells.add(matrix[r][c]);
		}
		squash(cells);
	    }
	};
	align(upwards);
    }

    public void down() {
	Runnable downwards = () -> {
	    List<Cell> cells = new ArrayList<>();
	    for (int c = 0; c < conf.COLS; c++) {
		cells.clear();
		for (int r = conf.ROWS - 1; r >= 0; r--) {
		    cells.add(matrix[r][c]);
		}
		squash(cells);
	    }
	};
	align(downwards);
    }

    public void left() {
	Runnable leftwards = () -> {
	    List<Cell> cells = new ArrayList<>();
	    for (int r = 0; r < conf.ROWS; r++) {
		cells.clear();
		for (int c = 0; c < conf.COLS; c++) {
		    cells.add(matrix[r][c]);
		}
		squash(cells);
	    }
	};
	align(leftwards);
    }

    public void right() {
	Runnable rightwards = () -> {
	    List<Cell> cells = new ArrayList<>();
	    for (int r = 0; r < conf.ROWS; r++) {
		cells.clear();
		for (int c = conf.COLS - 1; c >= 0; c--) {
		    cells.add(matrix[r][c]);
		}
		squash(cells);
	    }
	};
	align(rightwards);
    }

    private void align(Runnable runnable) {
	freeCells.clear();
	spawnable = false;
	runnable.run();
	if (spawnable) {
	    steps++;
	    spawn();
	} else if (!isSolvable()) {
	    game.lose();
	}
    }

    private void squash(List<Cell> cells) {
	int baseIdx = 0;
	int i = 0;
	while (i < cells.size()) {
	    if (i == baseIdx || cells.get(i).val == 0) {
		i++;
		continue;
	    }
	    if (cells.get(baseIdx).val == 0) {
		cells.get(baseIdx).val = cells.get(i).val;
		cells.get(i).val = 0;
		i++;
		spawnable = true;
	    } else if (cells.get(baseIdx).val == cells.get(i).val) {
		cells.get(baseIdx).val *= 2;
		score += cells.get(baseIdx).val;
		if (cells.get(baseIdx).val == conf.TARGET) {
		    game.win();
		}
		cells.get(i).val = 0;
		baseIdx++;
		i++;
		spawnable = true;
	    } else {
		baseIdx++;
	    }
	}
	for (int j = baseIdx; j < cells.size(); j++) {
	    if (cells.get(j).val != 0)
		continue;
	    this.freeCells.add(cells.get(j));
	}
    }

    private boolean isSolvable() {
	for (int i = 0; i < conf.ROWS; i++) {
	    for (int j = 0; j < conf.COLS; j++) {
		if(matrix[i][j].val == 0) return true;
		if (j > 0 && matrix[i][j].val == matrix[i][j - 1].val)
		    return true;
		if (i > 0 && matrix[i][j].val == matrix[i - 1][j].val)
		    return true;
	    }
	}
	return false;
    }

    public int getScore() {
	return this.score;
    }
    
    public int getSteps() {
	return this.steps;
    }
}