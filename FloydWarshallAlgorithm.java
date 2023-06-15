import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FloydWarshallAlgorithm {
    private static final int INF = 99999;

    public static int[][] readGraphFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);

        int numberOfNodes = scanner.nextInt();
        int numberOfEdges = scanner.nextInt();

        int[][] graph = new int[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (i == j) {
                    graph[i][j] = 0;
                } else {
                    graph[i][j] = INF;
                }
            }
        }

        for (int k = 0; k < numberOfEdges; k++) {
            int source = scanner.nextInt() - 1; // Adjust indices to start from 0
            int target = scanner.nextInt() - 1;
            int weight = scanner.nextInt();
            graph[source][target] = weight;
        }

        scanner.close();

        return graph;
    }

    public static void floydWarshall(int[][] graph) {
        int numberOfNodes = graph.length;

        int[][] dist = new int[numberOfNodes][numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            System.arraycopy(graph[i], 0, dist[i], 0, numberOfNodes);
        }

        for (int k = 0; k < numberOfNodes; k++) {
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        printSolution(dist);
    }

    private static void printSolution(int[][] dist) {
        int numberOfNodes = dist.length;

        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (dist[i][j] == INF) {
                    System.out.println("d[" + (i + 1) + "," + (j + 1) + "] = INF");
                } else {
                    System.out.println("d[" + (i + 1) + "," + (j + 1) + "] = " + dist[i][j]);
                }
            }
        }
    }

    public static void writeOutputToFile(String filename, int[][] dist) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filename);
        int numberOfNodes = dist.length;

        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (dist[i][j] == INF) {
                    writer.println("d[" + (i + 1) + "," + (j + 1) + "] = INF");
                } else {
                    writer.println("d[" + (i + 1) + "," + (j + 1) + "] = " + dist[i][j]);
                }
            }
        }

        writer.close();
    }

    public static void main(String[] args) {
        try {
            int[][] graph = readGraphFromFile("g5.txt");
            floydWarshall(graph);
            writeOutputToFile("g5_out.txt", graph);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
