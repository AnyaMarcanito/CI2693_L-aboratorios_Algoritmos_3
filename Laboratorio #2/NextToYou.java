import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class NextToYou {
    private static int numComponente;

    public static Map<String, Integer> componentesFuertementeConexas(Graph<String> graph) {
        Map<String, Integer> componentes = new HashMap<>();
        List<String> f = new ArrayList<>();
        Map<String, Boolean> visitados = new HashMap<>();
        dfsVisit(graph, visitados, componentes, f);
        
        Graph<String> simetricGraph = graph.getSimetric();

        Collections.reverse(f);

        List<String> vertices = f;

        visitados.clear();
        f.clear();
        componentes.clear();

        dfsVisit2(simetricGraph, visitados, componentes, f, vertices);

        return componentes;
    }

    private static void dfsVisit(Graph<String> graph, Map<String, Boolean> visitados, Map<String, Integer> componentes, List<String> f) {
        numComponente = 0;

        for (String vertex : graph.getAllVertices()) {
            visitados.put(vertex, false);
            f.add(vertex);
        }

        for (String vertex : graph.getAllVertices()) {
            if (!visitados.getOrDefault(vertex, false)) {
                dfs(graph, visitados, componentes, f, vertex);
                numComponente++;
            }
        }
    }

    private static void dfsVisit2(Graph<String> graph, Map<String, Boolean> visitados, Map<String, Integer> componentes, List<String> f, List<String> vertices) {
        numComponente = 0;

        for (String vertex : graph.getAllVertices()) {
            visitados.put(vertex, false);
            f.add(vertex);
        }

        List<String> verticesCopy = new ArrayList<>(vertices);
        for (String vertex : verticesCopy) {
            if (!visitados.getOrDefault(vertex, false)) {
                dfs(graph, visitados, componentes, f, vertex);
                numComponente++;
            }
        }
    }

    private static void dfs(Graph<String> graph, Map<String, Boolean> visitados, Map<String, Integer> componentes, List<String> f, String vertex) {
        visitados.put(vertex, true);
        componentes.put(vertex, numComponente);

        for (String vecino : graph.getOutwardEdges(vertex)) {
            if (!visitados.getOrDefault(vecino, false)) {
                dfs(graph, visitados, componentes, f, vecino);
            }
        }
        f.add(vertex);
    }

    private static int maximoValorDeMap(Map<String, Integer> localidades){
        int maxValue = Integer.MIN_VALUE;
        for (Integer value : localidades.values()) {
            if (value.compareTo(maxValue) > 0) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    private static List<List<String>> listasDeLocalidades(Map<String, Integer> localidades) {
        int maxValue = maximoValorDeMap(localidades);
        
        List<List<String>> listas = new ArrayList<>();

        for (int i = 0; i <= maxValue; i++) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : localidades.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (value == i) {
                    list.add(key);
                }
            }
            listas.add(list);
        }
        return listas;
    }

    private static int numeroDeRepartidores(Map<String, Integer> localidades) {
        int numRepartidores = 0;
        
        List<List<String>> listasDeLocalidades = listasDeLocalidades(localidades);

        for (List<String> localidad : listasDeLocalidades) {
            int numComercios = localidad.size();
            if (numComercios <= 2) {
                numRepartidores += 10;
            } else if (numComercios <= 5) {
                numRepartidores += 20;
            } else {
                numRepartidores += 30;
            }
        }
        return numRepartidores;
    }
    
    public static void main(String[] args) {
        // Creamos un grafo vacío.
        Graph<String> graph = new AdjacencyListGraph<>();
        try {
            File inputFile = new File("Caracas.txt");
            Scanner scanner = new Scanner(inputFile);

            //Iteramos sobre cada linea del archivo.
            while (scanner.hasNextLine()) {
                // Leemos la línea y la separamos por comas.
                String line = scanner.nextLine();
                String[] data = line.split(",");
                // Identificamos los dos comercios de cada linea y los agregamos como vertices al grafo.
                String comercio1 = data[0];
                String comercio2 = data[1];
                graph.add(comercio1);
                graph.add(comercio2);
                // Como existe una via de poco trafico entre comercio1 y comercio2 agregamos
                // un arco entre ellos.
                graph.connect(comercio1, comercio2);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //Si no se encuentra el archivo input.txt se imprime un mensaje de error.
            System.out.println("No se pudo encontrar el archivo Caracas.txt.");
            return;
        }
        // Calculamos las componentes fuertemente conexas del grafo, ellas seran las localidades para el problema.
        Map<String, Integer> localidades = componentesFuertementeConexas(graph);
        // Calculamos el numero de repartidores necesarios para cubrir todas las localidades.
        int numRepartidores = numeroDeRepartidores(localidades);
        // Imprimimos el resultado por la salida estandar.
        System.out.println("Cantidad de repartidores necesarios: " + numRepartidores);
    }
}
