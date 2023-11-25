import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface GraphV2<T> {
    boolean add(T vertex);
    boolean connect(T from, T to, double peso);
    boolean contains(T vertex);
    List<ArcosConPesos<T>> getOutwardEdges(T from);
    List<T> getAllVertices();
}

class ArcosConPesos<T> {
    private T vertex;
    private double peso;

    public ArcosConPesos(T vertex, double peso) {
        this.vertex = vertex;
        this.peso = peso;
    }

    public T getVertex() {
        return vertex;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return vertex + " " + peso;
    }
}

class AdjacencyListGraph<T> implements GraphV2<T> {
    private Map<T, List<ArcosConPesos<T>>> adjacencyMap;

    // Método Constructor
    public AdjacencyListGraph() {
        adjacencyMap = new HashMap<>();
    }

    // Método add
    public boolean add(T vertex) {
        if (!contains(vertex)) {
            adjacencyMap.put(vertex, new ArrayList<>());
            return true;
        }
        return false;
    }

    // Método connect
    public boolean connect(T from, T to, double peso) {
        if (contains(from) && contains(to)) {
            List<ArcosConPesos<T>> successors = adjacencyMap.get(from);
            for (ArcosConPesos<T> pair : successors) {
                if (pair.getVertex().equals(to)) {
                    return false; // La arista ya existe
                }
            }
            successors.add(new ArcosConPesos<>(to, peso));
            return true;
        }
        return false;
    }

    // Método contains
    public boolean contains(T vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    // Método getOutwardEdges
    public List<ArcosConPesos<T>> getOutwardEdges(T from) {
        if (contains(from)) {
            List<ArcosConPesos<T>> lista = adjacencyMap.get(from);
            return lista;
        }
        return new ArrayList<>();
    }

    // Método getAllVertices
    public List<T> getAllVertices() {
        return new ArrayList<>(adjacencyMap.keySet());
    }

    // Método getPeso
    public Double getPeso(T from, T to) {
        if (contains(from) && contains(to)) {
            List<ArcosConPesos<T>> successors = adjacencyMap.get(from);
            for (ArcosConPesos<T> pair : successors) {
                if (pair.getVertex().equals(to)) {
                    return pair.getPeso();
                }
            }
        }
        return null; // La arista no existe o los vértices no se encontraron
    }

    /*public static void main(String[] args) {
        // Creamos un grafo dirigido con pesos en los arcos
        AdjacencyListGraph<String> graph = new AdjacencyListGraph<>();

        // Agregamos vértices al grafo
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.add("D");

        // Conectamos vértices con arcos y pesos
        graph.connect("A", "B", 2.0);
        graph.connect("A", "C", 3.0);
        graph.connect("B", "C", 1.0);
        graph.connect("B", "D", 4.0);
        graph.connect("C", "D", 5.0);

        // Imprimimos los vértices del grafo
        List<String> vertices = graph.getAllVertices();
        System.out.println("Vértices: " + vertices);

        // Imprimimos los arcos salientes de un vértice
        List<ArcosConPesos<String>> successors = graph.getOutwardEdges("A");
        System.out.println("Arcos salientes de A: ");
        for (ArcosConPesos<String> arco : successors) {
            System.out.println(arco);
        }

        // Obtenemos el peso de un arco
        Double peso = graph.getPeso("A", "B");
        System.out.println("Peso de A -> B: " + peso);
    }*/
}

