import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HillClimbing extends Vertex{

    public static ArrayList<Vertex> start(ArrayList<Vertex> input) {
        if (input.size() < 2){
            return input;
        }
        ArrayList<Vertex> result = new ArrayList<>();
        Random random = new Random();
        while(result.size() == 0){
            for (int i = 0; i < input.size(); i++) {
                //expect the returning set to be half as long as input
                if (random.nextInt(100)>50){
                    result.add(input.get(i));
                }
            }
        }
        
        return result;
    }

    public static int getScore(ArrayList<Vertex> s,int T){
        // Max(0,T−(Total value of the vertices in S)) + sum of the cost of all edges that connect two vertices in S
        // where the cost of an edge is considered to be the value of its lower end.
        int sumVertices = 0;
        int sumEdges = 0;
        for (int i = 0; i < s.size(); i++){
            Vertex current = s.get(i);
            //Total value of the vertices in S
            sumVertices += current.value;
            //sum of the cost of all edges that connect two vertices in S
            for(int j = 0; j < current.neighbors.size(); j++){
                if(s.contains(current.neighbors.get(j))){
                    sumEdges += Math.min(current.value,current.neighbors.get(j).value);
                }
            }
        }
        return Math.max(0,T-sumVertices)+sumEdges;
    }

    public static int getSum(ArrayList<Vertex> s){
      int sum = 0;
      for (int i = 0; i < s.size(); i++){
        sum += s.get(i).value;
      }
      return sum;
    }
    
    public static ArrayList<Vertex> getNext(ArrayList<Vertex> set, ArrayList<Vertex> state, int T){
      printSet(state);
      System.out.printf(" Value=%d. Error=%d.\n",getSum(state),getScore(state, T));
      int minError = getScore(state, T);
      ArrayList<Vertex> result = state;

      int score;
      System.out.printf("Neighbors:\n");
      for (int i = 0; i < set.size(); i++){
        ArrayList<Vertex> next = new ArrayList<Vertex>();
        next.addAll(state);
        if (state.contains(set.get(i)) && state.size()>0){  //if the vertex is in the current state, try reducing it 
          next.remove(set.get(i));
        }
        else if (state.size()>0){  //if the vertex is in the current state, try adding it 
          next.add(set.get(i));
        }
        score = getScore(next,T);
        printSet(next);
        System.out.printf(" Value=%d. Error=%d.\n",getSum(next),score);
        if (score <= minError){ //calculate error and see if 
          minError = score;
          result = next;
        }
        //System.out.println(" ");
      }
      //System.out.print("result: ");
      //printSet(result);
      return result;
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

    public static void main(String[] args) throws Exception {

        ArrayList<Vertex> vertices = new ArrayList<>();
        int T = 0;
        int flag = 0;
        int restarts = 1;

        //read the set
        try {
          Scanner sc = new Scanner(new File("input.txt"));
          T = sc.nextInt();
          flag = sc.next().equals("C") ? 0 : 1;
          restarts = sc.nextInt();
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
                //singlely linked to avoid repeating edges
                //vertex2.addNeighbor(vertex1);
            }
            // System.out.print("\n"+vertex1.name+" ");
            // for (int i = 0; i < vertex1.neighbors.size();i++){
            //   System.out.print(vertex1.neighbors.get(i).name);
            // }
            // System.out.print("\n"+vertex2.name+" ");
            // for (int i = 0; i < vertex2.neighbors.size();i++){
            //   System.out.print(vertex2.neighbors.get(i).name);
            // }
          }
          sc.close();
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

        //hill climbing 4 times
        for (int i = 0; i < restarts; i++){
          //random start
          ArrayList<Vertex> start = start(vertices);
          if (flag == 1){
            System.out.print("Randomly chosen start state: ");
            printSet(start);
            System.out.printf("\n");
          }

          ArrayList<Vertex> current = start;
          //get neighboring states and calculate scores
          while(true){
            //check if answer is correct
            if (getSum(current) >= T && isIndependent(current)){
              System.out.printf("Found solution ");
              printSet(current);
              System.out.printf(" Value = "+getSum(current));
              //break;
              return;
            }
            
            ArrayList<Vertex> next = getNext(vertices,current,T);
            if(!next.equals(current)){
              current.clear();
              current.addAll(next);
              if (flag == 1){
                System.out.printf("\nMove to ");
                printSet(current);
                System.out.printf(" Value=%d. Error=%d.\n",getSum(current),getScore(current, T));
              }
            }
            else{
              System.out.printf("Search failed\n\n");
              break;
            }
          }
          System.out.print("\n");
      }
    }
}
