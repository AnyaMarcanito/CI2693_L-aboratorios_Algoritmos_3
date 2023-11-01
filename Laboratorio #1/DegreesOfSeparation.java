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

    /**
     * Calcula los grados de separación entre dos personas en un grafo.
     * @param grafo, el grafo que contiene las conexiones entre personas
     * @param persona1, el nombre de la primera persona
     * @param persona2, el nombre de la segunda persona
     * @devuelve el número de grados de separación entre las dos personas, o -1 si uno de los 
     * nombres no está en el grafo.
     */
    public static int getDegreesOfSeparation(Graph<String> graph, String person1, String person2) {
        //Verificamos si el grafo introducido contiene los Strings person1 y person2 como vertices:
        if (!(graph.contains(person1)) || !(graph.contains(person2))) {
            System.out.println("Alguno de los nombres no se encuentra en el archivo input.txt.");
            //De no ser asi, se retorna -1.
            return -1;
        //Verificamos si los Strings person1 y person2 son iguales:    
        } else if (person1.equals(person2)) {
            System.out.println("Los nombres son iguales.");
            //De ser asi, la distancia es 0.
            return 0;
        } else {
            /* Si no se cumple ninguna de las condiciones anteriores, se procede a calcular 
             * la distancia entre los Strings person1 y person2 usando el algoritmo de BFS.

             * Se crea una cola, un conjunto de visitados y un mapa de distancias para 
             * almacenar los datos necesarios.*/
            Queue<String> cola = new LinkedList<>();
            Set<String> visitados = new HashSet<>();
            Map<String, Integer> distance = new HashMap<>();

            /* Se agrega el primer elemento a la cola, al conjunto de visitados y al mapa 
             * de distancias.*/
            cola.add(person1);
            visitados.add(person1);
            distance.put(person1, 0);

            //Se recorre la cola hasta que esta se vacie.
            while (!cola.isEmpty()) {
                //Se toma el primer elemento de la cola.
                String actual = cola.poll();
                /*Se recorren los sucesores del elemento actual usando el metodo 
                 * .getVerticesConnectedTo() de la clase AdjacencyListGraph.*/
                for (String sucesor : graph.getOutwardEdges(actual)) {
                    //Se verifica si el sucesor no ha sido visitado.
                    if (!visitados.contains(sucesor)) {
                        /* Si no ha sido visitado, se agrega a la cola, al conjunto 
                         * de visitados y al mapa de distancias.*/
                        cola.add(sucesor);
                        visitados.add(sucesor);
                        //La distancia del sucesor es la distancia del actual + 1.
                        distance.put(sucesor, distance.get(actual) + 1);
                        //Se verifica si el sucesor es el segundo nombre introducido.
                        if (sucesor.equals(person2)) {
                            /*Si es asi, se retorna la distancia pues hemos encontrado 
                             *el camino mas corto de person1 a person2.*/
                            return distance.get(sucesor);
                        }
                    }
                }
            }
            /*Si la cola se vacia y no se encuentra el segundo nombre, se retorna -1, 
             *pues los nombres no estan conectados por ningun camino.**/
            System.out.println("No se encontro un camino entre los nombres introducidos.");
            return -1;
        }
    }

    /**
     * Este programa lee un archivo que contiene pares de nombres, que representan amistades,
     * y determina el grado de separación entre dos personas dadas.
     * El programa toma dos nombres como argumentos de línea de comandos y muestra el grado 
     * de separación entre ellos, o un mensaje indicando que los nombres no se encontraron 
     * en el archivo de entrada.
     * El archivo de entrada debe llamarse "input.txt" y estar en el mismo directorio que 
     * el programa.
     * El programa utiliza una estructura de datos de grafos para representar las amistades 
     * y calcula el grado de separación mediante una busqueda BFS usando el metodo
     * getDegreesOfSeparation().   
     */

    public static void main(String[] args) {
        //Verificamos que se hayan introducido dos nombres como argumento.
        if (args.length < 2) {
            System.out.println("Deben proporcionar dos nombres como argumento.");
            return;
        }
        //Guardamos los nombres introducidos en los Strings person1 y person2.
        String person1 = args[0];
        String person2 = args[1];

        //Creamos un grafo de tipo AdjacencyListGraph.
        Graph<String> graph = new AdjacencyListGraph<>();

        //Leemos el archivo input.txt y agregamos los nombres al grafo.
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
                    /*Conectamos los nombres en ambas direcciones esto debido a 
                     *que la amistad es reciproca y AdjacencyListGraph modela una
                     *estructura de grafo dirigida.*/
                    graph.connect(personA, personB);
                    graph.connect(personB, personA);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //Si no se encuentra el archivo input.txt se imprime un mensaje de error.
            System.out.println("No se pudo encontrar el archivo input.txt.");
            return;
        }
        /*Se calcula el grado de separacion entre los nombres introducidos 
         *usando el metodo getDegreesOfSeparation().*/
        int degree = getDegreesOfSeparation(graph, person1, person2);

        //Se imprime el grado de separacion entre los nombres introducidos.
        System.out.println("El grado de separacion entre "+person1+" y "+person2+" es: "+degree);
    }
}