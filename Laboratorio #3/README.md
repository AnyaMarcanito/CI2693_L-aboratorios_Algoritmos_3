Universidad Simón Bolívar

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Anya Marcano 19-10336

                                                Laboratorio 3
                                                  Arbitrage

Para la resolución del problema planteado para este laboratorio, se decidió hacer uso de la clase AdjacencyListGraph creada durante 
el Proyecto #1, agregando unas modificaciones adicionales, junto con la clase Arbitrage.java en la cual fueron creados los métodos 
necesarios para definir si, dado un archivo de tasas.txt, es posible coseguir algún arbitraje.

La estrategia para la resolución del problema se basó en formar un grafo dirigido con la data del archivo tasas.txt y determinar 
si en el mismo existen ciclos o no, conseguirlos, y con ellos ir calculando para cada ciclo si, de acuerdo a los costos de los arcos,
al retornar a la moneda inicial existe o no alguna ganancia, de haberla, no solo se mostrará por consola el mensaje: "DINERO FÁCIL 
DESDE TU CASA" sino que se procuró que para esta implementación sean mostrados por consola la lista de todas las posibles monedas
con las que se puede iniciar  algún arbitraje, enumerándolas, mostrando el circuito que debe ser seguido para lograrlo, junto con el 
porcentaje de ganancia que se obtiene de acuerdo a las tasas listadas en el archivo input.

Siendo así, a continuacion se mencionan todos los métodos que fueron implementados para esta solución:

❖ Modificaciones en la clase AdjacencyListGraph: 

   1. Creación de una interfaz grafo nueva llamada: GraphV2

      Con la necesidad de adaptar la AdjacencyListGraph original para que pudiese ser usada para almacenar los pesos de los distintos
      arcos, se decidió crear una nueva interfase de grafos, en la cual solamente estan definidos los métodos que serán de utilidad para
      la solución planteada para este laboratorio, así tenemos que la interfase GraphV2 contiene los siguientes métodos:
      
                         - boolean add(T vertex);
                         - boolean connect(T from, T to, double peso);
                         - boolean contains(T vertex);
                         - List<ArcosConPesos<T>> getOutwardEdges(T from);
                         - List<T> getAllVertices();
                         - Double getPeso(T from, T to);

      Donde cabe acotar que, además de haber sido omitidos algunos métodos que estaban definidos originalmente en la interfaz Graph,
      tenemos un método nuevo denominado getPeso(), el cual básicamente permite saber el peso de un arco específico que sale de un vértice a
      otro siempre y cuando el mismo exista, y además, podemos mencionar que otro cambio notorio tiene que ver con el método getOutwardEdges
      el cual se vale de una nueva clase llamada ArcosConPesos, para así, poder devolver no solo los vertices relacionados a los arcos salientes,
      sino también poder tener acceso a los pesos de tales arcos.
      
   2. Creación de la clase ArcosConPesos:

      Como se mencionó en el punto anterior, esta clase surgió como parte de la necesidad de poder tener una estructura de datos que permitiera
      devolver, en una misma lista, dos elementos asociados a cada arco presente en el grafo: el vértice y el costo. Siendo así, esta clase
      sencillamente usando dos variables miembro, una de tipo T para los vértices y la otra de tipo Double para los costos, implementa dos métodos
      adicionales al constructor de la clase, que funcionan como métodos getter para cada uno de estos atributos:

                             - getVertex: Que devuelve el vértice asociado al arco.
                             - getPeso: Que devuelve el costo asociado al arco.

   3. AdjacencyListGraph como una clase que implementa la interfase GraphV2:

      Siendo así, se creó la clase AdjacencyListGraph, esta vez como una implementación de la interfase GraphV2, pero siguiéndose la misma idea de
      la AdjacencyListGraph original, agregándole el método getPeso, que teniendo como input dos vertices, se encarga de verificar si ambos pertenecen
      al grafo, para luego calcular la lista de sucesores del vértice from, e iterar hasta verificar si alguno de esos arcos coincide con ser el que
      se está buscando (from, to) de conseguirlo se encarga de retornar su peso ayudándose del método getPeso definido en la clase ArcosConPesos.


❖ Clase Arbitrage:

  ➱ Sobre el método main:

  Siendo el punto de entrada del programa y basándose en el resto de métodos implementados, se encarga de leer un archivo tasas.txt y crear con esa data
  un grafo dirigido con AdjacencyListGraph, sobre el cual va a correrse el algoritmo modificado de Dijkstra para conseguir caminos de costo máximo 
  desde todos los vértices hasta todos los alcanzables de cada uno de ellos, iterando sobre getAllVertices del grafo construido, y haciendo que en cada una de
  esas iteraciones se haga un llamado al método dijkstra, con estos caminos se hace un llamado al método encontrarCircuitos, para ver si en esos caminos 
  existe algún circuito, luego de lo cual, si la lista resultante no es vacía se procede a iterar sobre ella para calcular los costos de cada uno de los 
  circuitos encontrados, y revisar si existe un porcentaje de ganancia en ellos usando el método calcularGanancia.
  
  De ir encontrando circuitos con una ganancia mayor a 0, por consola van a ir enumerándose las posibilidades de arbitraje conseguidas, especificándose la 
  moneda inicial, la ruta del circuito que permite obtener un arbitraje con tal moneda, y el porcentaje de ganancia conseguido en el mismo. Finalmente, una 
  vez listadas todas las posibilidades encontradas es imprimido por consola: "DINERO FACIL DESDE TU CASA". En caso de no haber encontrado ningún 
  arbitraje luego de finalizar las iteraciones, se procede a imprimir por la salida estándar "TODO GUAY PARAGUAY", con lo cual finaliza el programa.
  
  ➱ Sobre el método dijkstra:

  Se trata de una variación del algoritmo de Dijkstra que permite calcular caminos de costo máximo, para con ellos garantizar que al multiplicar los costos de 
  los caminos encontrados, tengamos la garantia de poder verificar que en efecto es posible un arbitraje, ya que con esto no estamos buscando minimizar sino 
  maximizar los costos de los arcos del grafo, por lo que la idea del algoritmo de Dijkstra bajo este cambio es perfecta para nuestra intención, pues nos permite 
  retornar, dado un grafo y un vértice inicial, la lista de los caminos de costo máximo hasta todos los alcanzables, con lo cual podemos despues verificar si 
  existen o circuitos, que es parte de la estrategia que planteamos para resolver el problema.
  
  ➱ Sobre el método reconstruirCaminos:

  Usado al final del método de Dijkstra para poder devolver los caminos encontrados en una lista de listas de Strings, este método se vale del HashMap de 
  predecesores que es armado durante la corrida de Dijkstra, junto con el vértice inicial que es introducido en Dijkstra, para así ir iterando sobre todos 
  los vertices del grafo y reconstruir los caminos que existen en predecesores, esto, viendo primero que el vertice que tomamos al iterar no es el vértice 
  inicial de Dijkstra para luego generar una lista de caminos vacía en la que agregamos el vértice de la iteración actual, para luego entrar en un bucle 
  que va reconstruyendo los caminos hasta que consigue un predecesor nulo, al conseguirlo agrega el camino formado hasta el momento a una lista, para 
  continuar iterando y al finalizar retornando una lista con las listas de los caminos formados.
  
  ➱ Sobre el método encontrarCircuitos:

  Básicamente, este método a partir de un grafo y una lista de caminos introducida se encarga de iterar sobre todos los caminos introducidos y revisar si 
  en el grafo existe un arco que permite ir desde el vértice que fue agregado último a la lista hasta el vértice que fue agregado primero, con lo cual se
  verifica la existencia de un circuito, de ser esto posible, agrega el vértice inicial al final del camino y va agregando los caminos que son circuitos 
  a una lista adicional que será retornada al finalizar todas las iteraciones.
     
   ➱ Sobre el método calcularGanancia:

   Finalmente, calcularGanancia recibe el grafo junto con un circuito encontrado, y se encarga de iterar sobre los vertices de la lista para ir encontrando
   los arcos que conforman ese circuito e ir acumulando el producto de los costos de todos los arcos que conforman el circuito, para al finalizar restarle 1 
   a la ganancia obtenida, esto ya que el arbitraje comienza con una unidad de cualquier moneda, y al hacer esto, es posible evidenciar si en efecto hay o no 
   alguna ganacia, para finalmente calcular el procentaje de ganancia multiplicando el resultado obtenido por cien y retornar el valor encontrado, el cual 
   permite determinar si hay o no un arbitraje en el circuito introducido.
   

