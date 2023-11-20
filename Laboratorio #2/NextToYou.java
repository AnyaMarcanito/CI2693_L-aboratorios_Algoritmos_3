import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NextToYou {

    /**
     * Calcula las componentes fuertemente conexas de un grafo dado.
     * Utiliza el algoritmo de búsqueda en profundidad (DFS) para encontrar las componentes.
     * 
     * @param graph el grafo en el que se buscarán las componentes fuertemente conexas
     * @return una lista de listas que contiene las componentes fuertemente conexas del grafo
     */
    public static List<List<String>> calculoDeComponentesFuertementeConexas(Graph<String> graph) {
        //Inicializamos un conjunto de visitados, una lista de finalizados y una lista para las componentes 
        //fuertemente conexas
        Set<String> visitados = new HashSet<>();
        List<String> finalizados = new ArrayList<>();
        List<List<String>> components = new ArrayList<>();

        //Recorremos todos los vertices del grafo.
        for (String vertex : graph.getAllVertices()) {
            //Si el vertice no ha sido visitado entonces se llama al metodo dfs().
            if (!visitados.contains(vertex)) {
                dfs(graph, vertex, visitados, finalizados, true);
            }
        }

        //Vaciamos el conjunto de visitados.
        visitados.clear();
                

        //Recorremos la lista de finalizados en orden inverso.
        Collections.reverse(finalizados);

        //Creamos un grafo simetrico al grafo original.
        Graph<String> simetrico = graph.getSimetric();

        //Recorremos todos los vertices del grafo.
        for (String vertex : finalizados) {
            //Si el vertice no ha sido visitado entonces se llama al metodo dfsTranspose() 
            //y se inicializa una lista para la componenete fuertemente conexa del vertice actual.
            if (!visitados.contains(vertex)) {
                List<String> component = new ArrayList<>();
                dfs(simetrico, vertex, visitados, component, false);
                components.add(component);
            }
        }
        //Retornamos la lista de componentes.
        return components;
    }
    
    /**
     * Realiza una búsqueda en profundidad (DFS) en el grafo dado.
     * Si cualDFS es true, entonces en la busqueda DFS se busca generar la lista de finalizados.
     * Si cualDFS es false, entonces en la busqueda DFS se busca generar las componentes fuertemente conexas.
     * 
     * @param graph el grafo en el que se realizará la búsqueda
     * @param vertex el vértice actual
     * @param visitados un conjunto de vértices visitados
     * @param finalizadosOComponentes una lista de vértices finalizados o lista de vertices de una componente fuertemente 
     * conexa
     * @param cualDFS un booleano que indica si se trata del primer o segundo DFS
     */
    private static void dfs(Graph<String> graph, String vertex, Set<String> visitados, List<String> finalizadosOComponentes, boolean cualDFS) {
        //Si cualDFS es true entonces se busca generar la lista de finalizados.
        if (cualDFS == true){
            //Agregamos el vertice vertex al conjunto de visitados.
            visitados.add(vertex);
            //Recorremos todos los sucesores del vertice vertex.
            for (String vecino : graph.getOutwardEdges(vertex)) {
                //Si el sucesor no ha sido visitado entonces se llama al metodo dfs().
                if (!visitados.contains(vecino)) {
                    dfs(graph, vecino, visitados, finalizadosOComponentes, true);
                }
            }
            //Cuando ya hemos recorrido todos los sucesores de un vertice lo agregamos a la lista de finalizados.
            finalizadosOComponentes.add(vertex);
        //Si cualDFS es false entonces se busca generar las componentes fuertemente conexas.
        } else {
            //Agregamos el vertice vertex al conjunto de visitados y a la lista de componentes fuertemente conexas.
            visitados.add(vertex);
            finalizadosOComponentes.add(vertex);
            //Recorremos todos los predecesores del vertice vertex
            for (String vecino : graph.getOutwardEdges(vertex)) {
                //Si el sucesor no ha sido visitado entonces se llama al metodo dfs().
                if (!visitados.contains(vecino)) {
                    dfs(graph, vecino, visitados, finalizadosOComponentes, false);
                }
            }
        }
    }
    /**
     * Imprime las localidades encontradas.
     * 
     * @param localidades la lista de localidades a imprimir
     */
    private static void imprimirLocalidades(List<List<String>> localidades) {
        System.out.println("Localidades encontradas:");
        for (int i = 0; i < localidades.size(); i++) {
            System.out.println("Localidad " + (i + 1) + ": " + localidades.get(i));
        }
    }

    /**
     * Calcula el número total de conductores necesarios para cubrir todas las localidades.
     * 
     * @param localidades una lista de listas de cadenas que representa las localidades y sus comercios
     * @return el número total de conductores necesarios
     */
    private static int calcularTotalDeConductores(List<List<String>> localidades) {
        int totalConductores = 0;
        //Iteramos sobre las localidades.
        for (List<String> locality : localidades) {
            //Vemos la cantidad de comercios que tiene cada localidad y acumulamos el numero 
            //de conductores en totalConductores.
            int size = locality.size();
            if (size <= 2) {
                totalConductores += 10;
            } else if (size <= 5) {
                totalConductores += 20;
            } else {
                totalConductores += 30;
            }
        }
        //Retornamos el numero total de conductores.
        return totalConductores;
    }

    /**
     * El método main es el punto de entrada del programa.
     * -> Lee un archivo Caracas.txt que contiene pares de nombres de comercios.
     * -> Crea un grafo donde los vertices son los nombres de los comercios y los 
     *    arcos representan las vias con poco trafico que los conectan.
     * -> Por lo tanto el grafo resultante representa una red de localidades y sus
     *    conexiones.
     * -> Calcula las localidades para el problema como las componentes fuertemente
     *    conexas y en base a ellas calcula el número de conductores necesarios para
     *    cubrir la red de localidades.
     */
    public static void main(String[] args) {
        //Creamos un grafo de tipo AdjacencyListGraph.
        Graph<String> graph = new AdjacencyListGraph<>();
        //Leemos el archivo Caracas.txt y agregamos los nombres al grafo.
        try (BufferedReader br = new BufferedReader(new FileReader("Caracas.txt"))) {
            String line;
            //Leemos el archivo linea por linea.
            while ((line = br.readLine()) != null) {
                //Separamos los nombres de las localidades por la coma del archivo Caracas.txt.
                String[] commercePair = line.split(",");
                String commerce1 = commercePair[0].trim();
                String commerce2 = commercePair[1].trim();
                //Agregamos los nombres de las localidades al grafo.
                graph.add(commerce1);
                graph.add(commerce2);
                //Conectamos los comercios exactamente en la direccion en la que aparecen en el archivo Caracas.txt.
                graph.connect(commerce1, commerce2);
            }
            //Si no se encuentra el archivo Caracas.txt se imprime un mensaje de error.
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //Conseguimos las localidades del problema usando el metodo findcalculoDeComponentesFuertementeConexas(), 
        //pues la definicion de localidad para este problema coincide con la de componentes fuertemente conexas.
        List<List<String>> localidades = calculoDeComponentesFuertementeConexas(graph);
        //Imprimimos las localidades encontradas.
        imprimirLocalidades(localidades);
        //Calculamos el numero total de repartidores necesarios.
        int totalConductores = calcularTotalDeConductores(localidades);
        //Imprimimos el numero total de repartidores necesarios.
        System.out.println("Número total de repartidores necesarios: " + totalConductores);
    }
}
