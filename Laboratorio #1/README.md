Universidad Simón Bolívar

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Anya Marcano 19-10336

                                                          Laboratorio 1
                                                      Grados de separación
                                                      
Para la resolución del problema plnateado para este laboratorio, se decidío hacer uso de la clase AdjacencyListGraph creada durante el 
Proyecto #1, haciendo unas pequeñas modificaciones en ella, las cuales se basaron es sustituir los objetos de tipo List<T>, usados como valor 
del mapa que corresponde con la estructura base de los objetos de esta clase, por objetos de tipo HashSet<T> para con ello mejorar un poco la 
cota para la complejidad del peor caso de varios de los métodos de la clase.

Por otro lado, se creó la clase DegreesOfSeparation, la cual posee dos métodos estáticos, uno llamado getDegreesOfSeparation(), el cual se encarga
de calcular el grado de separación que hay entre dos vértices tipo String de un grafo dado, y el otro correspondiente al main que se encarga de 
leer el archivo "input.txt", generar un grafo usando la clase AdjacencyListGraph y usar el método getDegreesOfSeparation() para así imprimir el 
grado de separación de los vértices dados por línea de comandos.


❖ Sobre el método getDegreesOfSeparation()

Calcula los grados de separación entre dos personas en un grafo basándose en el uso de las estructuras Queue<String> con LikedList<>,
Set<String> con HashSet<> y Map<String, Integer> con HashMap<> para la implementación del algoritmo BFS, para encontrar el camino más
corto que existe entre person1 y person2 de existir un camino entre ellos en el grafo introducido.

☆ PARÁMETROS DE ENTRADA:

     * Graph<String> grafo: El grafo que contiene las conexiones entre las personas que existen en el archivo "input.txt"
     
     * String person1: El nombre de la primera persona/vértice.
     
     * String person2: El nombre de la segunda persona/vértice.
☆ SALIDA:

     * Devuelve el número del grado de separación que hay entre person1 y person2.
     
       - Si se person1 es igual a person2 entonces se retorna 0.
       
       - Si alguno de los dos Strings no pertenece al grafo introducido, se retorna -1.
       
       - Si no existe conexión entre person1 y person2 en el grafo dado, se retorna -1.
       

❖ Sobre el método main:

Este método lee un archivo que contiene pares de nombres que representan amistades, y determina el grado de separación entre dos personas dadas.
El programa toma dos nombres como argumentos de línea de comandos e imprime el grado de separación entre ellos, o un mensaje indicando que los 
nombres no se encontraron en el archivo de entrada. Utiliza la estructura creada para el Proyecto #1 para la representación de datos de grafos:
los objetos de tipo AdjacencyListGraph para representar las amistades del archivo input.txt en un grafo y calcula el grado de separación mediante 
el metodo getDegreesOfSeparation().
Así mismo, el archivo de entrada debe llamarse "input.txt" y estar en el mismo directorio que el programa.


                                        OBSERVACIONES SOBRE LA IMPLEMENTACIÓN REALIZADA

Se decició utilizar la misma clase AdjacencyListGraph creada para el Proyecto #1 dado que a pesar de que esta considera grafos dirigidos, pudo ser
fácilmente adapatada para modelar las relaciones de amistad entre personas, las cuales son simétricas, y esto se logró haciendo que, al armar el grafo
con los datos de input.txt, se generaran ambas conexiones, tanto de person1 a person2 como de person2 a person1, algo que la clase AdjacencyListGraph
permitía hacer sin inconveniente alguno. Así mismo, se deció mantenerla ya que en ella el método .add() tiene una complejidad O(1) para su peor caso, 
mientras que el metodo connect() maneja un peor caso que en la práctica es menor a O(n) habiendo implementado HashSet<T> en lugar de List<T>, y la 
complejidad de getVerticesConnectedTo() posee un peor caso que es menor a O(n^2) considerando el cambio realizado en la implementación. Con lo cual, 
la clase AdjacencyListGraph ya poseía una estructura con los métodos necesarios para armar el grafo pertienente para resolver el problema, con
complejidades que, a menos que estemos en presencia de un grafo completo de tamaño exageradamente grande, permitirán dar un resultado al problema
con cierto grado de eficiencia.

Por otro lado, se decidió hacer uso del BFS en lugar del DFS debido a que la búsqueda en amplitud permite garantizar que el camino conseguido entre 
dos vertices dados es el de distancia mínima, mientras que la búsqueda en profundida no permite asegurar nada en cuanto a la longitud de los caminos 
generados, precisamente porque ellos dependerán del criterio utilizado para seleccionar el próximo sucesor a explorar en la corrida. Por lo que, para la 
solución del problema propuesto, encontrar el grado de separación entre dos personas, lo conveniente era definitivamente crear una implementación 
para BFS que se frene una vez se consiga el camino entre person1 y person2, el cual no será otro sino el de distancia mínima.


       
