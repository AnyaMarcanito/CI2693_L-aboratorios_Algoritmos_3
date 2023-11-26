import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Arbitrage {
    //Colores para la salida estandar
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_MAGENTA = "\u001B[35m";

    /**
     * El método main es el punto de entrada del programa.
     * Lee los tipos de cambio de un archivo tasas.txt, realiza los cálculos de 
     * arbitraje e imprime los resultados en la salida estándar.
     */
    public static void main(String[] args) {
        //Creamos un graph de tipo AdjacencyListGraph e inicializamos las variables 
        //que seran necesarias para el programa.
        GraphV2<String> graph = new AdjacencyListGraph<>();
        boolean arbitrajeEncontrado = true;
        int i = 1;
        
        try {
            //Leemos el archivo tasas.txt y lo guardamos en un BufferedReader.
            BufferedReader br = new BufferedReader(new FileReader("tasas.txt"));
            String linea = br.readLine();
            //Mientras la linea no sea nula, separamos los datos de la linea y 
            //los guardamos en un arreglo.
            while (linea != null) {
                String[] data = linea.split(" ");
                String moneda1 = data[0];
                String moneda2 = data[1];
                double peso = Double.parseDouble(data[2]);
                //Agregamos los vertices al grafo y los conectamos con el peso 
                //correspondiente.
                graph.add(moneda1);
                graph.add(moneda2);
                graph.connect(moneda1, moneda2, peso);
                //Leemos la siguiente linea.
                linea = br.readLine();
            }
            //Cerramos el BufferedReader.
            br.close();
        //En caso de que no se encuentre el archivo, se imprime el error.
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Iteramos sobre todos los vertices del grafo.
        for (String vertex : graph.getAllVertices()) {
            //Por cada vertice, se ejecuta el algoritmo de Dijkstra y se obtienen 
            //los caminos hasta todos los alcanzables.
            List<List<String>> caminos = dijkstra(graph, vertex);
            //Se busca si en los caminos obtenidos hay algun circuito y de haberlos
            //el metodo encontrarCircuitos los guarda en una lista.
            List<List<String>> circuitos = encontrarCircuitos(graph, caminos);
            //Si la lista de circuitos no esta vacia, se itera sobre cada circuito
            if (!circuitos.isEmpty()) {
                for (List<String> circuito : circuitos) {
                    //Se calcula la ganancia de cada circuito con el metodo 
                    //calcularGanancia en porcentaje (%)
                    double ganancia = calcularGanancia(graph, circuito); 
                    //Si la ganancia es mayor a 0, entonces se imprime por la salida 
                    //estandar la moneda inicial del circuito, el circuito  y el 
                    //porcentaje ganancia obtenida.
                    if (ganancia > 0) {
                        //Usamos la variable arbitrajeEncontrado para imprimir una sola vez:
                        //la identificacion de los "ARBITRAJES ENCONTRADOS"
                        if (arbitrajeEncontrado) {
                            //Cambiamos arbitrajeEncontrado a false para no volver a imprimir 
                            //esta parte multiples veces
                            arbitrajeEncontrado = false;
                            System.out.println(" ");
                            System.out.println(ANSI_MAGENTA +"ARBITRAJES ENCONTRADOS-----------------------------------------------------"+ ANSI_RESET);
                        }
                        //Identificamos el numero del arbitraje conseguido e incrementamos i 
                        //para poder actualizar el contador de los arbitrajes conseguidos:
                        System.out.println(i);
                        i = i +1;
                        //Imprimimos la informacion del arbitraje conseguido por consola:
                        System.out.println(ANSI_YELLOW +"Partiendo de la moneda: "+ ANSI_RESET + circuito.get(0));
                        System.out.println(" ");
                        System.out.println(ANSI_YELLOW +"Siguiendo la siguiente secuencia de cambios: "+ ANSI_RESET+ circuito);
                        System.out.println(" ");
                        System.out.println(ANSI_YELLOW +"Obteniendo el siguiente porcentaje de ganancia: "+ ANSI_RESET + ganancia + " %");
                        System.out.println(" ");
                    }
                }
            }             
        }
        //Si no se encontro ningun arbitraje, se imprime por la salida estandar "TODO GUAY DEL PARAGUAY"
        //y esto lo podemos filtrar usando la variable arbitrajeEncontrado, pues si nunca fue cambiada a
        //false significa que no se encontro ningun arbitraje.
        if (arbitrajeEncontrado) {
            System.out.println(ANSI_CYAN + "RESULTADO OBTENIDO: " + ANSI_RESET + ANSI_RED + "TODO GUAY DEL PARAGUAY"+ ANSI_RESET);
            System.out.println(" ");
        //En caso contrario, se imprime por la salida estandar "DINERO FACIL DESDE TU CASA" luego de mostrar
        //todos los arbitrajes conseguidos en la busqueda
        } else{
            System.out.println(ANSI_MAGENTA +"FIN DE LOS ARBITRAJES ENCONTRADOS-----------------------------------------------------"+ ANSI_RESET);
            System.out.println(" ");
            System.out.println(ANSI_CYAN + "RESULTADO OBTENIDO: " + ANSI_RESET + ANSI_GREEN + "DINERO FACIL DESDE TU CASA"+ ANSI_RESET);
            System.out.println(" ");
        }
    }

    /**
     * Aplica el algoritmo de Dijkstra para encontrar caminos de costo maximo hasta los alcanzables del 
     * vertice indicado.
     * 
     * @param grafo: El grafo sobre el cual se va a aplicar el algoritmo de Dijkstra.
     * @param s: El vértice origen desde el que encontrar los caminos hasta sus alcanzales.
     * @return Una lista de listas que representan los caminos desde el vértice origen 
     * a cada vértice alcanzable desde este.
     */
    public static List<List<String>> dijkstra(GraphV2<String> graph, String s) {
        //Inicializamos las variables que seran necesarias para el algoritmo de Dijkstra.
        HashMap<String, Double> costos = new HashMap<String, Double>();
        HashMap<String, String> predecesores = new HashMap<String, String>();
        Set<String> visitados = new HashSet<String>();
        List<String> cola = new LinkedList<String>();

        //Iteramos sobre todos los vertices del grafo y asignamos los costos de cada uno 
        //de ellos a menos infinito y los predecesores a null.
        for (String vertex : graph.getAllVertices()) {
            costos.put(vertex, Double.NEGATIVE_INFINITY);
            predecesores.put(vertex, null);
        }

        //Actualizamos el costo del vertice inicial a 0 y lo agregamos a la cola.
        costos.put(s, 0.0);
        cola.add(s);
        
        //Mientras la cola no este vacia, exploramos los sucesores del vertice desencolado
        //agregamos ese vertice al conjunto de visitados:
        while (!cola.isEmpty()) {
            //Desencolamos el primer elemento de la cola y lo guardamos en la variable actual.
            String actual = cola.remove(0);
            //Si el vertice actual ya fue visitado, entonces continuamos con el siguiente vertice.
            if (visitados.contains(actual)) {
                continue;
            }
            //Si el vertice actual no ha sido visitado, lo agregamos al conjunto de visitados
            visitados.add(actual);
            //Iteramos sobre todos los sucesores del vertice actual
            for (ArcosConPesos<String> sucesor : graph.getOutwardEdges(actual)) {
                //Con los metodos getVertex y getPeso de la clase ArcosConPesos obtenemos el sucesor
                //y el peso del arco que lo conecta con el vertice actual.
                String vertex = sucesor.getVertex();
                double peso = sucesor.getPeso();
                //Si el costo del sucesor es menor al costo del vertice actual mas el peso del arco 
                //que los conecta y el sucesor no ha sido visitado, entonces actualizamos el costo
                if (costos.get(vertex) < costos.get(actual) + peso && !visitados.contains(vertex)) {
                    //Actualizamos el costo del sucesor y su predecesor y lo agregamos a la cola.
                    costos.put(vertex, costos.get(actual) + peso);
                    predecesores.put(vertex, actual);
                    cola.add(vertex);
                }
            }
        }
        //Usamos el metodo reconstruirCaminos para obtener todos los caminos desde el vertice inicial 
        //hasta cada uno de sus alcanzables:
        List<List<String>> caminos = reconstruirCaminos(graph, predecesores, s);
        return caminos;
    }

    /**
     * Reconstruye los caminos desde el vertice inicial hasta cada uno de sus alcanzables.
     * 
     * @param graph: El grafo sobre el cual se esta trabajando
     * @param predecesores: Un HashMap que contiene los predecesores de cada vertice.
     * @param s: El vértice origen desde el que encontrar los caminos hasta sus alcanzales.
     * @return Una lista de listas que representan los caminos desde el vértice origen 
     * a cada vértice alcanzable desde este.
     */
    private static List<List<String>> reconstruirCaminos(GraphV2<String> graph, HashMap<String, String> predecesores, String s) {
        //Inicializamos la lista de caminos.
        List<List<String>> caminos = new ArrayList<>();

        //Iteramos sobre todos los vertices del grafo.
        for (String destino : graph.getAllVertices()) {
            //Si el destino es diferente al origen, entonces reconstruimos el camino.
            if (!destino.equals(s)) {
                //Inicializamos una nueva lista para almacenar el camino actual
                List<String> camino = new ArrayList<>();
                //Agregamos el destino al camino y obtenemos su predecesor.
                camino.add(destino);
                String predecesor = predecesores.get(destino);

                //Mientras el predecesor no sea nulo, agregamos el predecesor al camino y
                //obtenemos el predecesor del predecesor.
                while (predecesor != null) {
                    camino.add(0, predecesor);
                    predecesor = predecesores.get(predecesor);
                }
                //Agregamos el camino conseguido a la lista de caminos.
                caminos.add(camino);
            }
        }
        //Retornamos la lista de caminos.
        return caminos;
    }

    /**
     * Encuentra los circuitos en un grafo dado, a partir de una lista de caminos.
     * Un circuito es un camino cerrado en el grafo donde el primer y último vértice son el mismo.
     *
     * @param graph: El grafo en el que se buscarán los circuitos.
     * @param caminos: La lista de caminos a partir de los cuales se buscarán los circuitos.
     * @return Una lista de circuitos encontrados en el grafo.
     */
    private static List<List<String>> encontrarCircuitos(GraphV2<String> graph, List<List<String>> caminos) {
        //Inicializamos la lista de circuitos.
        List<List<String>> circuitos = new ArrayList<>();

        //Iteramos sobre todos los caminos dentro de la lista de caminos introducida:
        for (List<String> camino : caminos) {
            //Obtenemos el primer y ultimo vertice del camino.
            String primerVertice = camino.get(0);
            String ultimoVertice = camino.get(camino.size() - 1);
            //Obtenemos los sucesores del ultimo vertice.
            List<String> sucesores = new ArrayList<>();
            for (ArcosConPesos<String> sucesor : graph.getOutwardEdges(ultimoVertice)) {
                sucesores.add(sucesor.getVertex());
            }
            //Si el primer vertice es un sucesor del ultimo vertice, entonces el camino 
            //es un circuito.
            if (sucesores.contains(primerVertice)) {
                //Completamos el circuito agregando el primer vertice al final del camino.
                camino.add(primerVertice);
                //Agregamos el camino a la lista de circuitos.
                circuitos.add(camino);
            }
        }
        //Retornamos la lista de circuitos encontrados de la lista de caminos introducida.
        return circuitos;
    }

    /**
     * Calcula la ganancia obtenida al recorrer un circuito en un grafo dado.
     * 
     * @param graph: El grafo que representa las conexiones entre las monedas.
     * @param circuito: La lista de monedas que forman el circuito.
     * @return La ganancia obtenida al recorrer el circuito en forma de porcentaje.
     */
    private static double calcularGanancia(GraphV2<String> graph, List<String> circuito) {
        //Inicializamos la ganancia en 1.0.
        double ganancia = 1.0;
        //Iteramos sobre todos los vertices del circuito.
        for (int i = 0; i < circuito.size() - 1; i++) {
            //Obtenemos el vertice actual y el siguiente.
            String monedaActual = circuito.get(i);
            String monedaSiguiente = circuito.get(i + 1);
            //Obtenemos el costo de la arista que conecta el vertice actual con el siguiente
            //y lo guardamos en la variable costo.
            double costo = graph.getPeso(monedaActual, monedaSiguiente);
            //Actualizamos la ganancia multiplicandola por el costo.
            ganancia *= costo;
        }
        //Restamos 1.0 a la ganancia y la multiplicamos por 100 para obtener el porcentaje.
        ganancia = ganancia - 1.0;
        ganancia *= 100;
        //Retornamos la ganancia obtenida.
        return ganancia;
    }
    
}
