import java.util.ArrayList;

public class Vertex {
    final String name;
    final int value;
    ArrayList<Vertex> neighbors = new ArrayList<>();

    public Vertex(){
        this.name = null;
        this.value = 0;
    }

    public Vertex(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public void addNeighbor(Vertex s){
        neighbors.add(s);
    } 
}
