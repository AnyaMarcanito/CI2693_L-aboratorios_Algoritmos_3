Universidad Simón Bolívar

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Anya Marcano 19-10336

                                                    Laboratorio 2
                                                   App de Delivery

Para la resolución del problema planteado para este laboratorio, se decidió hacer uso de la clase AdjacencyListGraph creada durante 
el Proyecto #1, y modificada en el Laboratorio #1, junto con la clase NextToYou.java con los métodos necesarios para implementar el 
algoritmo para calcular las componentes fuertemente conexas por medio del algoritmo de VisitaDFS visto en la teoría, el cual esta 
basado en una búsqueda DFS recursiva. Esto, debido a que la parte inicial del problema consiste en conseguir las localidades o conjuntos 
de comercios que son mutuamente alcanzables a través de vías de poco tráfico, y como el grafo generado del archivo Caracas.txt tiene 
como vértices los comercios presentes en la zona de Caracas y los arcos son las vías de poco tráfico entre ellos, entonces una de las 
posibles formas para modelar el problema consiste en ver las localidades como las componentes fuertemente conexas, calcularlas y luego 
ver cuántos comercios hay en cada una de esas componentes para poder realizar la sumatoria para saber cuántos reparetidores son necesarios
en Caracas según según las condiciones del problema.


Siendo así, a continuacion se mencionan todos los métodos que fueron implementados para esta solución:


❖ Modificaciones en la clase AdjacencyListGraph: método getSimetric()


  Como el algoritmo para calcular las componentes fuertemente conexas requiere poder calcular el grafo simétrico, dentro de la clase 
  AdjacencyListGraph() (y en consecuencia dentro de la interfase Graph) fue agregado el método getSimetric(), el cual se encarga de, 
  dado un grafo de tipo T, iterar sobre todos sus vértices usando el metodo getAllVertices(), agregar cada uno de ellos a un nuevo grafo, 
  el cual será el simétrico, para luego iterar sobre los sucesores usando un for anidado y el método getOutwarEdges(), para así conectar los arcos 
  en el grafo simétrico desde el sucesor hacia el predecesor, con lo cual, al finalizar se obtiene el grafo simétrico del grafo original, el
  es retornado por getSimetric().
  
  Así mismo en cuanto a complejidad, podemos decir que como en este método se itera sobre todos los vértices del grafo original y luego sobre 
  los sucesores de cada uno de los vértices del grafo, y como en el peor de los casos podríamos estar hablando de un grafo donde todos los 
  vértices se encuentran conectados por un arco entre ellos, y considerando que los métodos getAllVertices() y getOutwardEdges() tienen 
  complejidades O(1) en ambos casos, mientras que connect() es O(|V|) para el peor caso, entonces podemos conlcuir que getSimetric() posee 
  una complejidad de O(|V|^3) para el peor caso.

❖ Clase NextToYou: 

  ➱ Sobre el método calculoDeComponentesFuertementeConexas():

  Se trata de la implementación del pseudocódigo visto en teoría para el cálculo de las componentes fuertemente conexas por medio de una
  búsqueda recursiva de DFS, en el se tiene como parámetro de entrada el grafo del que se quieren las componentes fuertemente conexas y se 
  retorna una lista de listas que contiene las componentes fuertemente conexas del grafo. 

  En este método se realiza una corrida de VisitaDFS sobre el grafo original, marcando los vértices visitados con un conjunto de visitados y conservando
  el orden en el que fueron finalizados los vértices en la búsqueda con una lista de finalizados, luego de lo cual se vacía el conjunto de visitados, se
  invierte el orden de finalizados, se calcula el grafo simétrico del grafo introducido como parámetro y se procede a correr nuevamente una visitaDFS, en la
  cual el orden para recorrer los vértices  coincide con el orden presente en finalizados y del cual se retornarán las componentes fuertemente conexas 
  conseguidas.

  ➱ Sobre el metodo dfs():

  Se trata de la implementación de la funcion recursiva de DFS presente en el algoritmo de VisitaDFS, en este caso la implementación tiene como parametros:
  el grafo en el que se realizará la búsqueda, el vértice actual, el conjunto de vértices visitados, una lista de vértices , que bien puede ser de los que 
  ya fueron finalizados durante la busqueda o de los presentes en una componente fuertemente conexa y un booleano que indica qué DFS se quiere implementar, 
  el que permite mantener los vertices que van finalizando o el que permite calcular las componentes fuertemente conexas.

  El método comienza revisando el booleano del parametro, si es true, se va a ejecutar la función recursiva de DFS que mantiene los vertices que van siendo
  finalizados, necesaria para la primera corrida de VisitaDFS de calculoDeComponentesFuertementeConexas(), mientras que si el booleano es false, el método de
  dfs va a ir almacenando las componentes fuertemente conexas, lo cual es necesario durante la segunda corrida de VisitaDFS en el método de 
  calculoDeComponentesFuertementeConexas() una vez que se tiene el orden inverso de los finalizados y ya fue calculado el grafo simétrico.

  ➱ Sobre el método imprimirLocalidades():

  Se trata de un metodo auxiliar que fue creado para poder imprimir por la salida estandar las componentes fuertemente conexas que se generan luego de la 
  ejecucién de la llamada a calculoDeComponentesFuertementeConexas(). Este método recibe como parámetro de entrada una lista de listas de Strings y se encarga
  de ir iterando sobre las listas para ir imprimiendolas por la salida estandar.

  ➱ Sobre el método calcularTotalDeConductores():

  Es un método que permite realizar la sumatoria de los repartidores necesarios en la zona de Caracas de acuerdo a las localidades conseguidas con el método
  calculoDeComponentesFuertementeConexas(). Este método recibe como entrada una lista de listas de Strings, que representa las distintas localidades con los
  comercios que las conforman, y retorna en base a ello el número entero exacto de repartidores necesarios según las indicaciones del problema, las cuales son:

        -> Localidades con 2 o menos comercios: 10 repartidores.
        -> Localidades con 5 o menos comercios: 20 repartidores.
        -> Localidades con 6 comercios o mas: 30 repartidores.

  Para esto, itera sobre cada lista de las listas de localidades, y revisa el tamaño de la lista en cuestión, para ver si tiene 2 o menos, 5 o menos, o si tiene
  6 o más para ir acumulando en una variable la sumatoria de cada una de las listas y retornarla como resultado al finalizar.


  ➱ Sobre el método main:

  Siendo el punto de entrada del programa y basándose en el resto de métodos implementados, se encarga de leer un archivo Caracas.txt que contiene pares de 
  nombres de comercios y con ello crea un grafo donde los vértices son los nombres de los comercios y los arcos son las vías con poco tráfico que los conectan.
  Luego de lo cual, con el método calculoDeComponentesFuertementeConexas() calcula las localidades para el problema, y en base a ellas, usando 
  calcularTotalDeConductores() calcula el número de conductores necesarios para cubrir la red de localidades.
  

                                                   OBSERVACIONES 

  Por un lado, se mantuvo el uso de la clase AdjacencyListGraph, ya que la misma poseía los métodos base necesarios para realizar lo que se quería para la
  solución del problema, es decir, formas para conectar y agregar vértices, así como métodos para conseguir los sucesores y todos los vértices existentes en
  el grafo, manejando complejidades relativamente eficientes. Además de que, pese a que getSimetric tuvo que ser creada, se tenían todos los componentes
  necesarios para que su implementacion fuese sencilla, sin tener que incurrir en la creación o modificación adicional de algún otro método de la estructura.

  Por otro lado, podemos agregar que otro enfoque para la resolución del problema puede venir desde el cálculo de la matriz de alcance del grafo creado luego 
  de la lectura del archivo Caracas.txt, ya que también se tiene conocimiento de un algoritmo visto en teoría que en base a la matriz de alcance del grafo, 
  puede calcular tanto componentes conexas como fuertemente conexas usando dos conjuntos distintos, uno en el que se introduzcan todos los vértices y otro que
  se inicialice como vacío, para luego dentro de un while (que se ejecuta hasta que el conjunto con todos los vertices se vuelva vacío), se tome cada vez un
  elemento del conjunto que inicia con todos los vértices y se itere sobre sus alcanzables con la matriz de alcance, para así verificar cuales son los vértices
  mutuamente alcanzables, para con ello ir almacenando los vértices adecuados en un conjunto nuevo, que al finalizar la exploración de alcanzables, debe ser
  agregado al conjunto que se inicializó como vacío, el cual mantendrá las componentes fuertemente conexas, y restado del conjunto que inicializa con todos los
  vértices, para así eventualmente conseguir todas las componentes fuertemente conexas del grafo.

  Sin embargo, como esta solución involucra calcular y recorrer una matriz |V|x|V| para poder verificar la mútua alcanzabilidad de los vertices del grafo, 
  se prefirió optar por la implementación del cálculo de componentes fuertemente conexas usando el otro algoritmo visto en la teoria para esto, el cual 
  también es conocido como el algoritmo de Kosaraju, que da solución en la que se evita el cálculo de la matriz de alcance, para en su lugar aprovechar 
  naturaleza de la busqueda DFS.

  
  Compilación: javac AdjacencyListGraph.java NextToYou.java
  
  Ejecucion: java NextToYou
  
  Salida esperada: 
  
  Localidades encontradas:
  Localidad 1: [Sucy's Cookies, Arepas Amanda, Kagari Sushi]
  Localidad 2: [Farmanada, MacDonas]
  Número total de repartidores necesarios: 30

  


