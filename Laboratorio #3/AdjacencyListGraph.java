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
    Double getPeso(T from, T to);
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
}

