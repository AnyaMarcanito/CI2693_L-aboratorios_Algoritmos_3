import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class NextToYou {
    public static void main(String[] args) {
        Graph<String> graph = new AdjacencyListGraph<>();
        try {
            File inputFile = new File("Caracas.txt");
            Scanner scanner = new Scanner(inputFile);

            //Iteramos sobre cada linea del archivo.
            while (scanner.hasNextLine()) {
                // Leemos la línea y la separamos por comas.
                String line = scanner.nextLine();
                String[] data = line.split(",");
                
                String comercio1 = data[0];
                String comercio2 = data[1];
                graph.add(comercio1);
                graph.add(comercio2);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //Si no se encuentra el archivo input.txt se imprime un mensaje de error.
            System.out.println("No se pudo encontrar el archivo input.txt.");
            return;
        }
    }
}
