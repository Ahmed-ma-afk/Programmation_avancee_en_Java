package uf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Connectivity extends JFrame {

  private static final long serialVersionUID = 7405622711059871752L;
  private static final int step = 20;

  Connectivity(int width, int height, double prob) {
    ConnectivityWindow window = new ConnectivityWindow(width, height, prob, step);
    this.setTitle("connectivity");
    window.setPreferredSize(new Dimension(width * step + 1, height * step + 1));
    this.add(window, BorderLayout.CENTER);
    this.pack();
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.addKeyListener(window);
    this.setVisible(true);
  }

  public static void main(String[] args) {
    double prob = args.length > 0 ? Double.valueOf(args[0]) : 0.4;
    new Connectivity(40, 30, prob); 
  }
}

class ConnectivityWindow extends JPanel implements KeyListener {
  private static final long serialVersionUID = 1L;

  private final int width, height, step;
  private double prob;
  private final boolean[][] cells;
  private UnionFind uf;
  private boolean show;

  ConnectivityWindow(int width, int height, double prob, int step) {
    this.width = width;
    this.height = height;
    this.step = step;
    this.prob = prob;
    this.cells = new boolean[width][height];
    this.show = false;
    this.reset();
  }

  private void reset() {
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        cells[x][y] = Math.random() < prob;
    cells[0][0] = cells[width - 1][height - 1] = false;
    
    int n = width * height;
    uf = new UnionFind(n);
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++) {
        int i = x * height + y;
        if (x < width-1 && cells[x][y] == cells[x + 1][y])
          uf.union(i, i + height);
        if (y < height-1 && cells[x][y] == cells[x][y + 1])
          uf.union(i, i + 1);
      }
    System.out.println(uf.find(0) == uf.find(n-1) ? "YES" : "NO");
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, step*width+1, step*height+1);
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++) {
        if (this.show && uf.sameClass(0, x * height + y))
          g2d.setColor(Color.RED);
        else if (cells[x][y])
          g2d.setColor(Color.BLACK);
        else
          g2d.setColor(Color.WHITE);
        int gx = x * this.step + 1;
        int gy = y * this.step + 1;
        g2d.fillRect(gx, gy, step-1, step-1);
      }
  }

  @Override
  public void keyPressed(KeyEvent ev) {
    char key = ev.getKeyChar();
    if (key == 'q')
      System.exit(0);
    if (key == '-') {
      prob *= 0.9;
      this.show = true;
    }
    if (this.show)
      this.reset();
    this.show = !this.show;
    this.repaint();
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
  }

}
