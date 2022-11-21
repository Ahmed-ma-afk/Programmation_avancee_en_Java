package backtracking;

// résoudre le problème de Scott (pavage avec les pentaminos)
// avec Dancing Links

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

// les 8 isométries du carré
enum Iso { Id, Rot180, Hrefl, Vrefl, Rot90, Rot270, D1refl, D2refl }

class Tile {
  final String name;
  final boolean[][] pattern;
  final int width, height;
  
  public Tile(String name, boolean[][] pattern) {
    super();
    this.name = name;
    this.pattern = pattern;
    this.width = pattern.length;
    this.height = pattern[0].length;
  }
  
  @Override
  public boolean equals(Object obj) {
    Tile that = (Tile)obj;
    if (this.width != that.width || this.height != that.height)
      return false;
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        if (this.pattern[x][y] != that.pattern[x][y])
          return false;
    return true ;
  }
  
  @Override
  public int hashCode() {
    int h = 31 * width + height;
    for (int x = 0; x < width; x++)
      for (int y = 0; y < height; y++)
        h = 31 * h + (pattern[x][y] ? 17 : 42);
    return h;
  }
  
  Set<Tile> iso() {
    HashSet<Tile> s = new HashSet<>();
    for (Iso iso: Iso.values()) {
      int w = width, h = height;
      switch (iso) {
      case Id: case Rot180: case Hrefl: case Vrefl: break;
      case Rot90: case Rot270: case D1refl: case D2refl: h = width; w = height; break;
      }
      boolean[][] pat = new boolean[w][h];
      for (int x = 0; x < w; x++)
        for (int y = 0; y < h; y++)
          switch (iso) {
          case Id:     pat[x][y] = pattern[x][y]; break;
          case Rot180: pat[x][y] = pattern[w-1-x][h-1-y]; break;
          case Hrefl:  pat[x][y] = pattern[x][h-1-y]; break;
          case Vrefl:  pat[x][y] = pattern[w-1-x][y]; break;
          case Rot90:  pat[x][y] = pattern[h-1-y][x]; break;
          case Rot270: pat[x][y] = pattern[y][w-1-x]; break;
          case D1refl: pat[x][y] = pattern[y][x];     break;
          case D2refl: pat[x][y] = pattern[h-1-y][w-1-x]; break;
          }
      s.add(new Tile(name, pat));
    }
    return s;
  }

}

public class ScottDLX {

  private DLXNode[][] grid = new DLXNode[8][8];
  private final Tile[] tiles = new Tile[] {
      new Tile("I", new boolean[][] { { true ,true ,true ,true ,true  } }),
      new Tile("V", new boolean[][] { { true ,true ,true  },
                                      { true ,false,false },
                                      { true ,false,false } }),
      new Tile("Z", new boolean[][] { { false,true ,true  },
                                      { false,true ,false },
                                      { true ,true ,false } }),
      new Tile("P", new boolean[][] { { true ,true ,true  },
                                      { true ,true ,false } }),
      new Tile("N", new boolean[][] { { true ,true ,true ,false },
                                      { false,false,true ,true  } }),
      new Tile("W", new boolean[][] { { false,true ,true  },
                                      { true ,true ,false },
                                      { true ,false,false } }),
      new Tile("Y", new boolean[][] { { true ,true ,true ,true  },
                                      { false,true ,false,false } }),
      new Tile("T", new boolean[][] { { true ,true ,true  },
                                      { false,true ,false },
                                      { false,true ,false } }),
      new Tile("F", new boolean[][] { { false,true ,true  },
                                      { true ,true ,false }, 
                                      { false,true ,false } }),
      new Tile("U", new boolean[][] { { true ,true ,true  }, 
                                      { true ,false,true  } }),
      new Tile("L", new boolean[][] { { true ,true ,true ,true },
                                      { true ,false,false,false} }),
      new Tile("X", new boolean[][] { { false,true ,false }, 
                                      { true ,true ,true  }, 
                                      { false,true ,false } }),
  };
  private DLXNode[] col = new DLXNode[12];
  
  ScottDLX() {
    LinkedList<DLXNode> cols = new LinkedList<>(); 
    for (int i = 0; i < 12; i++) {
      DLXNode n = new DLXNode(tiles[i].name);
      cols.add(n);
      col[i] = n;
    }
    for (int i = 0; i < 8; i++)
      for (int j = 0; j < 8; j++) {
        if ((i == 3 || i == 4) && (j == 3 || j == 4)) continue;
        cols.add(grid[i][j] = new DLXNode(i + ":" + j));
      }
    System.out.println(cols.size() + " colonnes");
    DancingLinks dl = new DancingLinks(cols);
    int rows = 0;
    for (int k = 0; k < 12; k++) {
      Tile t0 = tiles[k];
      System.err.print("pièce " + t0.name + ": ");
      Set<Tile> s = t0.iso();
      System.err.print(s.size() + " symétries");
      DLXNode c = col[k];
      int rows0 = rows;
      for (Tile t: s) {
        for (int i = 0; i < 8-t.width+1; i++)
          L:for (int j = 0; j < 8-t.height+1; j++) {
              LinkedList<DLXNode> row = new LinkedList<>();
              row.add(c);
              for (int x = 0; x < t.width; x++)
                for (int y = 0; y < t.height; y++)
                  if (t.pattern[x][y]) {
                    if (grid[i+x][j+y] == null) continue L;
                    row.add(grid[i+x][j+y]);
                  }
              dl.addRow(row);
              rows++;
          }
      }
      System.err.println(", " + (rows - rows0) + " placements");
    }
    System.out.println(rows + " lignes");
    System.out.println(dl.count() + " solutions");
  }
  
  public static void main(String[] args) {
    double start = System.currentTimeMillis();
    new ScottDLX();
    System.err.println((System.currentTimeMillis() - start)/1000 + " s");
  }
  
}
