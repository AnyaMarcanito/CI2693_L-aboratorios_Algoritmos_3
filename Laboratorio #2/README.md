Universidad Simón Bolívar

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Anya Marcano 19-10336

                                                    Laboratorio 2
                                                   App de Delivery

Para la resolución del problema plnateado para este laboratorio, se decidío hacer uso de la clase AdjacencyListGraph creada durante 
el Proyecto #1, y modificada en el Laboratorio #1, junto con la implementacion de la clase NextToYou.java con los metodos necesarios para 
implementar el algoritmo para calcular las componentes fuertemente conexas por medio del algoritmo de Visita, el cual esta basado en
el algoritmo de busqueda DFS recursiva, esto ya que la parte inicial del problema consiste en conseguir las localidades o conjuntos de 
comercios que son mutuamente alcanzables a traves de vias de poco trafico, y como el grafo generado del archivo Caracas.txt tiene
como vertices comercios y los arcos son las vias de poco trafico entre ellos, una de las posibles formas para modelar el problema consiste
en ver las localidades como las componentes fuertemente conexas, calcularlas y luego ver cuantos comercios hay en cada una de esas componentes
para poder realizar la sumatoria para saber cuantos conductores seran necesarios segun las localidades presentes en Caracas.


Siendo asi, a continuacion se mencionan todos los metodos implementados para esta solucion:


❖ Modificaciones en la clase AdjacencyListGraph: método getSimetric()


  Como el algoritmo para calcular las componentes fuertemente conexas requiere poder calcular el grafo simetrico, dentro de la clase 
  AdjacencyListGraph() (y en consecuencia dentro de la interfase Graph) fue agregado el metodo getSimetric() el cual se encarga de, 
  dado un grafo de tipo T, iterar sobre todos sus vertices usando el metodo getAllVertices(), agregar cada uno de ellos a un nuevo grafo 
  el cual sera el simetrico, para luego iterar sobre los sucesores usando un for anidado y el metodo getOutwarEdges(), para asi conectar los arcos 
  en el grafo simetrico desde el sucesor hacia el predecesor, con lo cual, al finalizar se obtiene el grafo simetrico del grafo original el
  es retornado por el metodo.
  Asi mismo en cuanto a complejidad, podemos decir que el mismo como itera sobre todos los vertices del grafo original y luego sobre los sucesores
  de cada uno de los vertices del grafo, y como en el peor de los casos podriamos estar hablando de un grafo donde todos los vertices se encuentran
  conectados por un arco entre ellos, y como los metodos getAllVertices() y getOutwardEdges() tienen complejidades O(1) en ambos casos, y el metodo 
  connect() es O(|V|) para el peor caso entonces tenemos que getSimetric() posee una complejidad para el peor caso de O(|V|^3).


❖ Clase NextToYou: 

  ➱ Sobre el metodo calculoDeComponentesFuertementeConexas():

  Se trata de la implementacion del pseudocodigo visto en teoria para el calculo de las componentes fuertemente conexas como una aplicacion del algorimo
  de busqueda recursiva de DFS, en el se tiene como parametro de entrada el grafo en el que se buscarán las componentes fuertemente conexas y se 
  retorna una lista de listas que contiene las componentes fuertemente conexas del grafo. 

  En este metodo se realiza una corrida de VisitaDFS sobre el grafo original, marcando los vertices visitados con un conjunto de visitados y conservando
  el orden en el que fueron finalizados los vertices en la busqueda con una lista de finalizados, luego de lo cual se vacia el conjunto de visitados, se
  invierte el orden de finalizados, se calcula el grafo simetrico del grafo introducido como parametro y se procede a correr nuevamente una visitaDFS en la
  cual el orden para recorrer los vertices  coincide con el orden presente en finalizados y del cual se retornaran las componentes fuertemente conexas 
  conseguidas.

  ➱ Sobre el metodo dfs():

  Se trata de la implementacion de la funcion recursiva de DFS presente en el algoritmo de VisitaDFS, en este caso la implementacion tiene como parametros:
  el grafo en el que se realizará la búsqueda, el vértice actual, el conjunto de vértices visitados, una lista de vértices , que bien puede ser de los que 
  ya fueron finalizados durante la busqueda o de los presentes en una componente fuertemente conexa y un booleano que indica que DFS se queire implementar, 
  el que permite mantener los vertices que van finalizando o el que necesaitamos usar para calcular las componentes fuertemente conexas.

  El metodo comienza revisando el booleano del parametro, si es true, se va a ejecutar la fucnion recursiva de DFS que mantiene los vertices que van siendo
  finalizados, necesaria para la primera corrida de VisitaDFS de calculoDeComponentesFuertementeConexas(), mientras que si el booleano es false, el metodo de
  dfs va a ir almacenando las componentes fuertemente conexas, lo cual es necesario durante la segunda corrida de VisitaDFS en el metodo de 
  calculoDeComponentesFuertementeConexas() una vez que se tiene el orden inverso de los finalizados y ya fue calculado el grafo simetrico.

  ➱ Sobre el metodo imprimirLocalidades():

  Se trata de un metodo auxiliar que fue creado para poder imprimir por la salida estandar las componentes fuertemente conexas que se generan luego de la 
  ejecucion de la llamada a calculoDeComponentesFuertementeConexas(). Este metodo recibe como parametro de entrada una lista de listas de Strings y se encarga
  de ir iterando sobre las listas para ir imprimiendolas por la salida estandar.

  ➱ Sobre el metodo calcularTotalDeConductores():

  Es un metodo que permite realizar la sumatoria de los conductores necesarios en la zona de Caracas de acuerdo a las localidades conseguidas con el metodo
  calculoDeComponentesFuertementeConexas(). Este metodo recibe como entrada una lista de listas de Strings que representa las distintas localidades con los
  comercios que las conforman, y retorna en base a ello el numero entero exacto de conductores necesarios segun las indicaciones del problema, las cuales son:

        -> Localidades con 2 o menos comercios: 10 repartidores.
        -> Localidades con 5 o menos comercios: 20 repartidores.
        -> Localidades con 6 comercios o mas: 30 repartidores.

  Para esto, itera sobre cada lista de las listas de localidades, y revisa el tamano de la lista en cuestion, para ver si tiene 2 o menos, 5 o menos, o si tiene
  6 o mas para ir acumulando en una variable la sumatoria de cada una de las listas y ser retornada por el metodo.


  ➱ Sobre el metodo main:

  Siendo el punto de entrada del programa y basandose en el resto de metodos implementados, se encarga de leer un archivo Caracas.txt que contiene pares de 
  nombres de comercios y con ello crear un grafo donde los vertices son los nombres de los comercios y los arcos son las vias con poco trafico que los conectan.
  Luego de lo cual con el metodo calculoDeComponentesFuertementeConexas() calcula las localidades para el problema, y en base a ellas, usando 
  calcularTotalDeConductores() calcula el número de conductores necesarios para cubrir la red de localidades.
  

                                                  OBSERVACIONES 

  Por un lado, se mantuvo el uso de la clase AdjacencyListGraph, ya que la misma poseia los metodos base necesarios para realizar lo que se queria para la solucion
  del problema, es decir, formas para conectar y agregar vertices, asi como metodos para conseguir los sucesores y todos los vertices existentes en el grafo con 
  complejidades realtivamente eficientes. Ademas de que, pese a que getSimetric tuvo que ser creada, se tenian todos los compoenentes necesarios para que su 
  implementacion fuese sencilla, sin tener que incurrir en la creacion o modificacion de algun otro metodo de la estructura.

  Por otro lado, podemos agregar que otro enfoque para la resolucion del problema puede venir desde el calculo de la matriz de alcance del grafo creado luego de la
  lectura del archivo Caracas.txt, ya que tambien se tiene conocimiento de un algoritmo visto en teoria que en base a la matriz de alcance del grafo, es posible 
  calcular tanto componentes conexas como fuertemente conexas usando dos conjuntos distintos, uno en el que se introduzcan todos los vertices y otro que se inicialice
  como vacio, para entrar en un while que se mantenga hasta que el conjunto con todos los vertices se vuelva vacio, en el cual se tome cada vez un elemento del conjunto
  que inicia con todos los vertices y se itere sobre sus alcanzables, verificando la mutua alcanzabilidad, que de haberla se pueden ir almacenando los vertices adecuados 
  en un conjunto nuevo que al finalizar la exploracion de alcanzables, debe ser agregado al conjunto que se inicializo como vacio, el cual mantendra las componentes
  fuertemente conexas y restado del conjunto que inicializa con todos los vertices, para asi eventualmente conseguir todas las componentes conexas del grafo.

  Sin embargo, como esta solucion involucra recorrer una matriz |V|x|V| para poder verificar la mutua alcanzabilidad de los vertices del grafo, se prefirio optar por la 
  implementacion del calculo de componentes fuertemente conexas usando el algoritmo visto en la teoria, tambien conocido como el algoritmo de Kosaraju, el cual da
  una solucion que se ahorra el calculo de la matriz de alcance para en su lugar aprovechar la naturaleza de la busqueda DFS.

  
  Compilación: javac AdjacencyListGraph.java NextToYou.java
  
  Ejecucion: java NextToYou
  
  Salida esperada: 
  
  Localidades encontradas:
  Localidad 1: [Sucy's Cookies, Arepas Amanda, Kagari Sushi]
  Localidad 2: [Farmanada, MacDonas]
  Número total de repartidores necesarios: 30

  


