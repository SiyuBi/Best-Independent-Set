import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class IterativeDeepening extends Vertex{

    public static int getSum(ArrayList<Vertex> s){
      int sum = 0;
      for (int i = 0; i < s.size(); i++){
        sum += s.get(i).value;
      }
      return sum;
    }

    public static void printSet(ArrayList<Vertex> set){
      if (set.size() <= 0){
        System.out.print("{}");
        return;
      }
      for (Vertex v : set) {
          System.out.print(v.name + " ");
      }
    }

    public static boolean isIndependent(ArrayList<Vertex> state){
      for (int i = 0; i < state.size(); i++){
        for (int j = 0; j < state.size(); j++){
          Vertex vertex1 = state.get(i);
          Vertex vertex2 = state.get(j);
          if (vertex1.neighbors.contains(vertex2) && !vertex1.equals(vertex2)){
            //System.out.println("\n"+vertex1.name+" "+vertex2.name+" are connected");
            return false;
          }
        }
      }
      return true;
    }

    //state as input
    public static boolean dfs(int index, ArrayList<Vertex> vertices, ArrayList<Vertex> state, int limit, int T) {
      //add to state
      Vertex vertex = vertices.get(index);
      
      if (state.size() >= limit && isIndependent(state)) {
        printSet(state);
        System.out.printf(" value = %d\n",getSum(state));
        if (getSum(state)>= T){
          System.out.printf("Found solution ");printSet(state);System.out.printf(" value = %d\n",getSum(state));
          return true;
        }
        return false;
      }
      if (!state.contains(vertex)){
        state.add(vertex);
      }
      if (state.size() >= limit && isIndependent(state)) {
        printSet(state);
        System.out.printf(" value = %d\n",getSum(state));
        if (getSum(state)>= T){
          System.out.printf("Found solution ");printSet(state);System.out.printf(" value = %d\n",getSum(state));
          return true;
        }
        return false;
      }
      for (int i = index; i < vertices.size(); i++) {
        //if not a neighbor of v, not v itself, and not already in state
        if (!vertex.neighbors.contains(vertices.get(i))&& !vertex.equals(vertices.get(i)) && !state.contains(vertices.get(i))){
          //System.out.printf("not neighbor of %s: %s\n",vertex.name,vertices.get(i).name);
          dfs(i, vertices, state, limit,T);
          //update state with result
          state.remove(state.get(state.size()-1));
        }
      }
      return false;
    }

    public static void main(String[] args) throws Exception {

        ArrayList<Vertex> vertices = new ArrayList<>();
        int T = 0;
        int flag = 0;

        //read the set
        try {
          Scanner sc = new Scanner(new File("input.txt"));
          T = sc.nextInt();
          flag = sc.next().equals("C") ? 0 : 1;
          String line = sc.nextLine().trim();
          while (sc.hasNextLine()) {
            line = sc.nextLine().trim();
            if (line.isEmpty()) {
              break;
            }
            String[] parts = line.split("\\s+");
            String name = parts[0];
            int value = Integer.parseInt(parts[1]);
            Vertex v = new Vertex(name, value);
            vertices.add(v);
          }
          while (sc.hasNextLine()) {
            line = sc.nextLine().trim();
            String[] parts = line.split("\\s+");
            String name1 = parts[0];
            String name2 = parts[1];
            Vertex vertex1 = null;
            Vertex vertex2 = null;
            for (Vertex v : vertices) {
              if (v.name.equals(name1)) {
                vertex1 = v;
              }
              if (v.name.equals(name2)) {
                vertex2 = v;
              }
            }
            //add each other as neighbors
            if (vertex1 != null && vertex2 != null){
                vertex1.addNeighbor(vertex2);
            }
          }
          sc.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

        //from 0 to deepest
        for (int i = 0; i < vertices.size(); i++){
          if (flag == 1){
            System.out.printf("Depth: %d\n",i+1);
          }
          for (int j = 0; j < vertices.size(); j++){
            ArrayList<Vertex> state = new ArrayList<Vertex>();
            state.add(vertices.get(j));
            dfs(j,vertices,state,i+1,T);
          }
          System.out.print("\n");

      }
    }
}
