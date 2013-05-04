import java.util.ArrayList;

import processing.core.PApplet;

public class Maze extends PApplet{

	int x;
	int y;
	int w;
	int cellsAcross;
	ArrayList cells;

	int destinationX;
	int destinationY;

	int totalCells;
	int visitedCells;
	int currentCell;
	int[] cellStack = new int[1];
	int step;

	boolean complete;

	Maze(int _x, int _y, int _w, int _ca) {
		x = _x;
		y = _y;
		w = _w;
		cellsAcross = _ca;

		step = w / cellsAcross;
		totalCells = cellsAcross * cellsAcross;
		visitedCells = 1;
		currentCell = totalCells - 1;
		cellStack[0] = currentCell;

		cells = new ArrayList();
		for (int i = 0; i < cellsAcross; i++) {
			for (int j = 0; j < cellsAcross; j++) {
				Cell c = new Cell(x + j * step, y + i * step, step);
				cells.add(c);
			}
		}
		complete = false;

		Cell lastCell = (Cell) cells.get(cells.size() - 1);
		lastCell.marked = true;

		destinationX = x + w - step;
		destinationY = y + w - step;
	}

	public void reset(int _ca) {

		cellsAcross = _ca;
		step = w / cellsAcross;

		for (int i = cells.size() - 1; i >= 0; i--) {
			cells.remove(i);
		}

		for (int i = 0; i < cellsAcross; i++) {
			for (int j = 0; j < cellsAcross; j++) {
				Cell c = new Cell(x + j * step, y + i * step, step);
				cells.add(c);
			}
		}

		Cell lastCell = (Cell) cells.get(cells.size() - 1);
		lastCell.marked = true;

		while (cellStack.length > 1) {
			cellStack = shorten(cellStack);
		}

		totalCells = cellsAcross * cellsAcross;
		visitedCells = 1;
		currentCell = totalCells - 1;
		cellStack[0] = currentCell;
		destinationX = x + w - step;
		destinationY = y + w - step;
		finished = false;
		complete = false;
		randomSeed(millis());
	}

	public void display() {
		for (int i = 0; i < cells.size(); i++) {
			Cell c = (Cell) cells.get(i);
			c.display(step / 8);
		}
	}

	boolean[] travelThrough(int _x, int _y) {

		int index = cellsAcross * ((_y - y) / step) + ((_x - x) / step);
		Cell inCell = (Cell) cells.get(index);
		inCell.visited = min(255, inCell.visited + 65);
		return inCell.walls;
	}

	public void routeStep() {

    //find the current cell's neighbors
    int numberOfPossibles=0;

    int[] neighbors = {
      currentCell-cellsAcross,currentCell+cellsAcross,currentCell-1,currentCell+1
    };

    //check for edges
    if (currentCell-cellsAcross<0) neighbors[0]=-1;
    if (currentCell+cellsAcross>=cellsAcross*cellsAcross) neighbors[1]=-1;
    if (currentCell%cellsAcross==0) neighbors[2]=-1;
    if (currentCell%cellsAcross==cellsAcross-1) neighbors[3]=-1;

    //check for previously visited cells
    for (int i = 0; i < 4; i++) {
      if (neighbors[i]!=-1) {
        Cell c = (Cell) cells.get(neighbors[i]);
        if (c.marked) neighbors[i]=-1;
        else numberOfPossibles++;
      }
    }

    if (numberOfPossibles>0) {  
      int chosenCell = (int)Math.random()*numberOfPossibles;
      for (int i = 0; i < 4; i++) {
        if (neighbors[i]!=-1) {
          if (chosenCell==0) {
            //this is the next cell
            Cell thisCell = (Cell) cells.get(currentCell);
            Cell nextCell = (Cell) cells.get(neighbors[i]);
            // thisCell.marked=true;
            nextCell.marked=true;
            //let's knock down the 2 adjoining walls
            if (i==0) {
              thisCell.walls[0]=false;
              nextCell.walls[1]=false;
            }
            if (i==1) {
              thisCell.walls[1]=false;
              nextCell.walls[0]=false;
            }
            if (i==2) {
              thisCell.walls[2]=false;
              nextCell.walls[3]=false;
            }
            if (i==3) {
              thisCell.walls[3]=false;
              nextCell.walls[2]=false;
            }
            visitedCells++;
            if (visitedCells==totalCells) {
              complete=true;
              //println(millis()-timer);
            }
            currentCell = neighbors[i];
            cellStack = append(cellStack,currentCell);
            stroke(0,0,255,5+250*thisCell.x/width+1);
            strokeWeight(step/2);
            int w = width/(cellsAcross*2);
            line(thisCell.x+step/2,thisCell.y+step/2,nextCell.x+step/2,nextCell.y+step/2);
            break;
          }
          else {
            chosenCell--;
          }
        }
      }
    }
    else {
      currentCell = cellStack[cellStack.length-1];
      cellStack = shorten(cellStack);
    }
  }
}
