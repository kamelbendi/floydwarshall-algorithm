import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FloydWarshallAlgorithm {
    private static final int INF = Integer.MAX_VALUE;

    public static void main(String[] args) {
        String inputFile = "g1000.txt";
        String outputFile = "g1000_out.txt";

        int[][] graph = readGraph(inputFile);
        int[][] distanceMatrix = performFloydWarshall(graph);

        writeOutputToFile(outputFile, graph, distanceMatrix);
    }

    public static int[][] readGraph(String inputFile) {
        int[][] graph = null;
        try (Scanner scanner = new Scanner(new File(inputFile))) {
            int nodes = scanner.nextInt();
            int edges = scanner.nextInt();

            graph = new int[nodes][nodes];
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (i == j) {
                        graph[i][j] = 0;
                    } else {
                        graph[i][j] = INF;
                    }
                }
            }

            for (int k = 0; k < edges; k++) {
                int source = scanner.nextInt() - 1;
                int destination = scanner.nextInt() - 1;
                int weight = scanner.nextInt();

                graph[source][destination] = weight;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static int[][] performFloydWarshall(int[][] graph) {
        int nodes = graph.length;
        int[][] distance = new int[nodes][nodes];

        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                distance[i][j] = graph[i][j];
            }
        }

        for (int k = 0; k < nodes; k++) {
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (distance[i][k] != INF && distance[k][j] != INF
                            && distance[i][k] + distance[k][j] < distance[i][j]) {
                        distance[i][j] = distance[i][k] + distance[k][j];
                    }
                }
            }
        }

        return distance;
    }

    public static void writeOutputToFile(String outputFile, int[][] graph, int[][] distanceMatrix) {
        try (FileWriter writer = new FileWriter(outputFile)) {
            int nodes = graph.length;
            int edges = countEdges(graph);
            int routes = nodes * (nodes - 1);

            writer.write("Number of Nodes: " + nodes + "\n");
            writer.write("Number of Edges: " + edges + "\n");
            writer.write("Number of Routes: " + routes + "\n");
            writer.write("Number of Iterations: " + nodes + "\n\n");

            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    writer.write("d[" + (i + 1) + "," + (j + 1) + "] = ");
                    if (distanceMatrix[i][j] == INF) {
                        writer.write("INF");
                    } else {
                        writer.write(Integer.toString(distanceMatrix[i][j]));
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int countEdges(int[][] graph) {
        int edges = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] != 0 && graph[i][j] != INF) {
                    edges++;
                }
            }
        }
        return edges;
    }
}
