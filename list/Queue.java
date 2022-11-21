package list;

import java.util.NoSuchElementException;

/* file implémentée avec une liste simplement chaînée
 * 
 *          head                                  tail
 *           |                                     |
 *           V                                     V
 *   pop <- [ | ] -----> [ | ] ----> [ | ] ----> [ | ] <- push
 *   top
 */

public class Queue implements intf.Queue {

  private Singly head, tail;
  private int size;

  public Queue() {
    this.head = this.tail = null;
    this.size = 0;
  }

  public boolean isEmpty() {
    return this.head == null;
  }
  
  public int size() {
    return this.size;
  }

  public void push(int x) {
    Singly e = new Singly(x, null);
    if (this.head == null)
      this.head = this.tail = e;
    else {
      this.tail.next = e;
      this.tail = e;
    }
    this.size++;
  }

  public int peek() {
    if (this.head == null)
      throw new NoSuchElementException();
    return this.head.element;
  }

  public int pop() {
    if (this.head == null)
      throw new NoSuchElementException();
    int e = this.head.element;
    this.head = this.head.next;
    if (this.head == null) this.tail = null; // for the GC to collect the element
    this.size--;
    return e;
  }

  @Override
  public String toString() {
    String s = "[";
    if (this.head != null) s = s + this.head.toString();
    return s + "]";
  }
  
}