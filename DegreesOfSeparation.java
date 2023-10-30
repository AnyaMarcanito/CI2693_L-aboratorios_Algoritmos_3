public class DegreesOfSeparation {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Deben proporcionar dos nombres como argumento.");
            return;
        }

        String person1 = args[0];
        String person2 = args[1];

        AdjacencyListGraph graph = new AdjacencyListGraph();
}