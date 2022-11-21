package uf;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

enum Direction { Vertical, Horizontal };

class Wall {
  final int x;
  final int y;
  final Direction direction;
  boolean closed;
  
  public Wall(int x, int y, Direction direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
    this.closed = true;
  }
}

public class Maze extends JFrame {
  private static final long serialVersionUID = 2465232426222345837L;

  private static final int step = 20;
  
  Maze(int width, int height) {
    Vector<Wall> walls = new Vector<Wall>();
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++) {
        if (x < width - 1) walls.add(new Wall(x, y, Direction.Vertical));
        if (y < height - 1) walls.add(new Wall(x, y, Direction.Horizontal));
      }
    System.out.println(walls.size() + " walls");
    sorting.KnuthShuffle.shuffle(walls);

    int n = width * height;
    UnionFind uf = new UnionFind(n); // codage : x * height + y
     
    for (Wall w: walls) {
      int cell = w.x * height + w.y;
      int next = cell + (w.direction == Direction.Horizontal ? 1 : height); 
      if (uf.find(cell) == uf.find(next)) continue;
      uf.union(cell, next);
      w.closed = false;
    }
    
    MazeWindow window = new MazeWindow(walls, width, height, step);
    this.setTitle("labyrinthe");
    window.setPreferredSize(new Dimension(width * step + 1, height * step + 1));
    this.add(window, BorderLayout.CENTER);
    this.pack();
    this.add(window);
    this.addKeyListener(window);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public static void main(String[] args) {
    new Maze(40, 30);
  }
}
  
class MazeWindow extends JPanel implements KeyListener {

  private static final long serialVersionUID = 1L;

  private final int width, height, step;
  private final Vector<Wall> walls;
  
  MazeWindow(Vector<Wall> walls, int width, int height, int step) {
    this.walls = walls;
    this.width = width;
    this.height = height;
    this.step = step;
  }
  
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(3));
    int w = this.width * this.step;
    int h = this.height * this.step;
    g.drawLine(0, 0, w, 0);
    g.drawLine(0, 0, 0, h);
    g.drawLine(w, 0, w, h);
    g.drawLine(0, h, w, h);
    for (Wall wall: this.walls) {
      if (!wall.closed) continue;
      int x = wall.x * this.step;
      int y = wall.y * this.step;
      switch (wall.direction){
      case Horizontal:
        g2d.drawLine(x, y + step, x + step, y + step);
        break;
      case Vertical:
        g2d.drawLine(x + step, y, x + step, y + step);
        break;
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent ev) {    
    char key = ev.getKeyChar();
    if (key == 'q')
      System.exit(0);
  }

  @Override
  public void keyPressed(KeyEvent ev) {
  }

  @Override
  public void keyReleased(KeyEvent ev) {    
  }
  
}
