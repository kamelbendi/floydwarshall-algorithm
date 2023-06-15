import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FloydWarshallAlgorithm {

    private static int numNodes;
    private static int numEdges;
    private static int numRoutes;
    private static int numIterations;

    private static class Edge {
        int source;
        int target;
        int weight;

        Edge(int source, int target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }
    }

    private static int[][] readGraphFromFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        numNodes = scanner.nextInt();
        numEdges = scanner.nextInt();

        int[][] graph = new int[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                graph[i][j] = (i == j) ? 0 : Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < numEdges; i++) {
            int source = scanner.nextInt() - 1;
            int target = scanner.nextInt() - 1;
            int weight = scanner.nextInt();
            graph[source][target] = weight;
        }

        scanner.close();
        return graph;
    }

    private static void writeOutputToFile(String filename, int[][] graph) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(new File(filename));
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (graph[i][j] == Integer.MAX_VALUE) {
                    writer.printf("d[%d,%d] = INF PATH: No path\n", i + 1, j + 1);
                } else {
                    writer.printf("d[%d,%d] = %d PATH: %d", i + 1, j + 1, graph[i][j], i + 1);
                    printPath(j, i, graph, writer);
                    writer.println();
                }
            }
        }
        writer.close();
    }

    private static void printPath(int current, int source, int[][] graph, PrintWriter writer) {
        if (current != source) {
            int next = graph[current][source];
            printPath(next, source, graph, writer);
            writer.printf("-%d", current + 1);
        }
    }

    private static void floydWarshall(int[][] graph) {
        int[][] dist = new int[numNodes][numNodes];
        int[][] next = new int[numNodes][numNodes];

        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                dist[i][j] = graph[i][j];
                if (dist[i][j] != Integer.MAX_VALUE && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        numIterations = 0;
        for (int k = 0; k < numNodes; k++) {
            for (int i = 0; i < numNodes; i++) {
                for (int j = 0; j < numNodes; j++) {
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE
                            && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
            numIterations++;
        }

        numRoutes = countRoutes(next);
    }

    private static int countRoutes(int[][] next) {
        int count = 0;
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                if (i != j && next[i][j] != -1) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        try {
            String inputFile = "g5.txt";
            String outputFile = "g5_out.txt";

            int[][] graph = readGraphFromFile(inputFile);

            long startTime = System.currentTimeMillis();
            floydWarshall(graph);
            long endTime = System.currentTimeMillis();

            writeOutputToFile(outputFile, graph);

            System.out.println("Number of Nodes: " + numNodes);
            System.out.println("Number of Edges: " + numEdges);
            System.out.println("Number of Routes: " + numRoutes);
            System.out.println("Iterations: " + numIterations);
            System.out.println("Execution Time: " + (endTime - startTime) + " milliseconds");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
