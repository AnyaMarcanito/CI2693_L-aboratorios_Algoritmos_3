import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


public class DegreesOfSeparation {

    public static int getDegreesOfSeparation(Graph<String> graph, String person1, String person2) {
        if (!(graph.contains(person1)) || !(graph.contains(person2))) {
            System.out.println("Alguno de los nombres no se encuentra en el archivo input.txt.");
            return -1;
            }
        if (person1.equals(person2)) {
            System.out.println("Los nombres son iguales.");
            return 0;
        }

        Queue<String> cola = new LinkedList<>();
        Set<String> visitados = new HashSet<>();
        Map<String, Integer> distance = new HashMap<>();

        cola.add(person1);
        visitados.add(person1);
        distance.put(person1, 0);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            for (String sucesor : graph.getVerticesConnectedTo(actual)) {
                if (!visitados.contains(sucesor)) {
                    cola.add(sucesor);
                    visitados.add(sucesor);
                    distance.put(sucesor, distance.get(actual) + 1);

                    if (sucesor.equals(person2)) {
                        return distance.get(sucesor);
                    }
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Deben proporcionar dos nombres como argumento.");
            return;
        }

        String person1 = args[0];
        String person2 = args[1];

        Graph<String> graph = new AdjacencyListGraph<>();

        try {
            File inputFile = new File("input.txt");
            Scanner scanner = new Scanner(inputFile);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] names = line.split(" ");

                if (names.length == 2) {
                    String personA = names[0];
                    String personB = names[1];

                    graph.add(personA);
                    graph.add(personB);
                    graph.connect(personA, personB);
                    graph.connect(personB, personA);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No se pudo encontrar el archivo input.txt.");
            return;
        }
        int degree = getDegreesOfSeparation(graph, person1, person2);

        System.out.println(degree);
    }
}