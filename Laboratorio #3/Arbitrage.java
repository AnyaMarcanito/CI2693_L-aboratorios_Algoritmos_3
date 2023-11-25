import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Set;

class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}


public class Arbitrage {
    public static void main(String[] args) {
        //Creamos un grafo de tipo AdjacencyListGraph.
        GraphV2<String> graph = new AdjacencyListGraph<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("tasas.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                String moneda1 = data[0];
                String moneda2 = data[1];
                double costo = Double.parseDouble(data[2]);
                graph.add(moneda1);
                graph.add(moneda2);
                graph.connect(moneda1, moneda2, costo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String monedaInicial : graph.getAllVertices()) {
            List<List<String>> camino = hallarCaminos(graph, monedaInicial);
            if (camino != null) {
                for (List<String> list : camino) {
                    if(list.size() > 1){
                        System.out.println(list);
                        System.out.println("DINERO FÁCIL DESDE TU CASA");
                    } else {
                        System.out.println("TODO GUAY DEL PARAGUAY");
                    }  
                }  
            } else {
                System.out.println("No se encontró arbitraje para la moneda " + monedaInicial + ".");
            }
        }
    }
    public static List<List<String>> hallarCaminos(GraphV2<String> graph, String start) {
        PriorityQueue<Pair<String, Double>> pq = new PriorityQueue<>(
            Comparator.comparingDouble((Pair<String, Double> pair) -> pair.getValue())
        );
        Map<String, Double> beneficios = new HashMap<>();
        Map<String, String> previos = new HashMap<>();
        Set<String> processed = new HashSet<>();
        List<List<String>> caminos = new ArrayList<>();
    
        for (String vertex : graph.getAllVertices()) {
            previos.put(vertex, null);
            if (!vertex.equals(start)){
                beneficios.put(vertex, 1.0);
                pq.add(new Pair<>(vertex, 1.0));
            }            
        }
                
        beneficios.put(start, 1.0);
        pq.add(new Pair<>(start, 0.0));
            
        while (!pq.isEmpty()) {
            
            Pair<String, Double> pair = pq.poll();
            String actual = pair.getKey();
            Double actualBeneficio = pair.getValue();
            processed.add(actual);
    
            List<ArcosConPesos<String>> edges = graph.getOutwardEdges(actual);
            for (ArcosConPesos<String> edge : edges) {
                String sucesor = edge.getVertex();
                double peso = edge.getPeso();
                double newBeneficio = actualBeneficio * peso;
                if (newBeneficio > beneficios.get(sucesor)) {
                    beneficios.put(sucesor, newBeneficio);
                    previos.put(sucesor, actual);
                }
            }
        }
        for (String vertex : graph.getAllVertices()) {
            List<ArcosConPesos<String>> edges = graph.getOutwardEdges(vertex);
            List<String> sucesores = new ArrayList<>();
            for (ArcosConPesos<String> edge : edges) {
                sucesores.add(edge.getVertex());
            }
            if (!vertex.equals(start) && sucesores.contains(start)) {
                List<String> camino = new ArrayList<>();
                String temp = vertex;
                while (temp != null) {
                    camino.add(temp);
                    temp = previos.get(temp);
                }
                camino.add(start);
                Collections.reverse(camino);
                camino.add(start);
                caminos.add(camino);
            }
        }
    
        return caminos;   
    }
}