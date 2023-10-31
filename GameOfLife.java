//------CONTROLS------
//Space: Pause/Play
//Left/Right Arrow Keys: Decrease/Increase rate of iterations
//C: Clear board (this will pause)
//R: Regenerate Board (this will pause)
//CLICK: Click a dead cell to activate it and a living cell to deactivate it

import processing.core.*;

public class GameOfLife extends PApplet{
    int cols = 100;
    int rows = cols/2;

    boolean paused = false;
    boolean wipe = false;
    boolean resetRandom = false;
    int iterationDelay = 100;
    int cellSize = 10;
    int[][] plane = new int[cols][rows];
    int[][] updatedPlane = new int[cols][rows];

    int spawnProbability = 15;

    public void settings() {
        size(1000, 500);

    }

    public void setup() {
        background(255);
        initPlane();

    }

    public void draw() {

        if(!paused) {
            background(255);
            stroke(0);
            drawGrid();
            generateNewPlane();
            planeSwapper();
            generateGraphics();
            planePrinter();
            System.out.println("------------------------ NEW GRID -------------------------");
            delay(iterationDelay);
        }

        if(wipe) {
            background(255);
            wipePlane();
            drawGrid();
        }

        if(resetRandom) {
            background(255);
            wipePlane();
            initPlane();
            drawGrid();
            generateGraphics();
            resetRandom = false;
        }
    }


    public static void main(String[] args) {
        PApplet.main("GameOfLife", args);

    }

    void initPlane() {
        for(int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int cellSpawn = (int) random(100);
                if (cellSpawn > spawnProbability) {
                    plane[i][j] = 0;
                } else {
                    plane[i][j] = 1;
                }

            }
        }
    }


    void generateNewPlane() {
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                int oldPlane = plane[i][j];
                int neighborInstances = getNeighbors(plane, i, j);

                if(oldPlane == 0 && neighborInstances == 3) {
                    updatedPlane[i][j] = 1;
                } else if (oldPlane == 1 && (neighborInstances < 2 || neighborInstances > 3)) {
                    updatedPlane[i][j] = 0;
                } else {
                    updatedPlane[i][j] = oldPlane;
                }
            }
        }
    }

    int getNeighbors(int[][] plane, int cellX, int cellY) {
        int totalNeighbors = 0;

        for(int i = -1; i <=1 ; i++) {
            for(int j = -1; j <= 1; j++) {
                int col = (cellX + i + cols) % cols;
                int row = (cellY + j + rows) % rows;
                totalNeighbors += plane[col][row];
            }
        }
        totalNeighbors -= plane[cellX][cellY];
        System.out.println(totalNeighbors);
        return totalNeighbors;
    }

    void planeSwapper() {
        int[][] planeHolder = plane;
        plane = updatedPlane;
        updatedPlane = planeHolder;
    }

    void generateGraphics() {
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                int xCoor = i * cellSize;
                int yCoor = j * cellSize;

                if(plane[i][j] == 1) {
                    fill(255, 0, 0);
                    rect(xCoor, yCoor, cellSize, cellSize, 3);
                }
            }
        }
    }

    void planePrinter() {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                System.out.println(updatedPlane[i][j]);
            }
        }
    }

    void wipePlane() {
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                plane[i][j] = 0;
                wipe = false;
            }
        }
    }

    void drawGrid() {
        stroke(200);
        for (int i = 0; i <= cols; i++) {
            int x = i * cellSize;
            line(x, 0, x, height);
        }
        for (int j = 0; j <= rows; j++) {
            int y = j * cellSize;
            line(0, y, width, y);
        }
    }


    public void keyPressed() {
        if(key == ' ') {
            paused = !paused;
        } else if (keyCode == RIGHT) {
            if(iterationDelay != 10) {
                iterationDelay -= 10;
            }
        } else if (keyCode == LEFT) {
            iterationDelay += 10;
        } else if (key == 'c') {
            paused = true;
            wipe = true;
        } else if (key == 'r') {
            resetRandom = true;
            paused = true;
        }
    }

    public void mousePressed() {
        int xCoor = mouseX / cellSize;
        int yCoor = mouseY / cellSize;

        if(xCoor < cols && xCoor >= 0 && yCoor < rows && yCoor >= 0) {
            if(plane[xCoor][yCoor] == 0) {
                plane[xCoor][yCoor] = 1;
                generateGraphics();
            } else if (plane[xCoor][yCoor] == 1) {
                plane[xCoor][yCoor] = 0;
                background(255);
                generateGraphics();
                drawGrid();
            }
        }
    }
}