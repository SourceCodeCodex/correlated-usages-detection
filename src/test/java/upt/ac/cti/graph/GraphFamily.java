package upt.ac.cti.graph;

import java.util.List;

interface Family {

}


sealed interface GraphFamily extends Family permits Weighted, Oriented {

}


// Declare the constraint so it is not extendable. Also, implementing a sealed interface can be a
// good constraint
final class Weighted implements GraphFamily {
  private Weighted() {

  }
}


final class Oriented implements GraphFamily {
  private Oriented() {

  }
}


abstract class Graph<T extends GraphFamily> {
  private List<Vertex<T>> v;
  private List<Edge<T>> e;

  public void addEdge(Edge<T> e) {
    this.e.add(e);
    this.v.add(e.v1);
    this.v.add(e.v2);
  }
}


abstract class Vertex<T extends GraphFamily> {
  private final int id;

  public Vertex(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}


abstract class Edge<T extends GraphFamily> {
  final Vertex<T> v1, v2;

  public Edge(Vertex<T> v1, Vertex<T> v2) {
    this.v1 = v1;
    this.v2 = v2;
  }
}


class WeightedGraph extends Graph<Weighted> {

}


class WeightedVertex extends Vertex<Weighted> {

  public WeightedVertex(int id) {
    super(id);
  }

}


class WeightedEdge extends Edge<Weighted> {

  public WeightedEdge(Vertex<Weighted> v1, Vertex<Weighted> v2) {
    super(v1, v2);
  }

}


class OrientedGraph extends Graph<Oriented> {

}


class OrientedVertex extends Vertex<Oriented> {

  public OrientedVertex(int id) {
    super(id);
  }

}


class OrientedEdge extends Edge<Oriented> {

  public OrientedEdge(Vertex<Oriented> v1, Vertex<Oriented> v2) {
    super(v1, v2);
  }

}



class App {
  public static void main() {
    var wg = new WeightedGraph();
    var wv1 = new WeightedVertex(1);
    var wv2 = new WeightedVertex(2);
    var we = new WeightedEdge(wv1, wv2);

    var og = new OrientedGraph();
    var ov1 = new OrientedVertex(1);
    var ov2 = new OrientedVertex(2);
    var oe = new OrientedEdge(ov1, ov2);

    wg.addEdge(we);
    og.addEdge(oe);

    // wg.addEdge(oe);
    // new OrientedEdge(wv1, wv2);

  }
}
