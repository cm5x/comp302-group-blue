package gameMechanics;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

import gameComponents.Player;
import view.MultiplayerWaitingScreen;
import view.RunningMode;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Socket socket;

    private int hostScore = 0;
    private int clientScore = 0;
    private DataOutputStream hostOut;
    private DataOutputStream clientOut;
    private RunningMode runningMode;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(50000); // No timeout for waiting for the client
        InetAddress ip = InetAddress.getLocalHost();
        System.out.println("Server IP: " + ip.toString());
        this.run();
    }

    public void run() {
        try {
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getRemoteSocketAddress());

            hostOut = new DataOutputStream(socket.getOutputStream());


            // // Close the waiting screen if it's open
            // MultiplayerWaitingScreen waitingScreen = MultiplayerWaitingScreen.getInstance();
            // if (waitingScreen != null) {
            //     waitingScreen.close();
            // }

            startGame();

        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startGame() throws IOException {
        // Start the game for both players
        ArrayList<int[]> initialMap = generateInitialMap();
        sendMap(initialMap);

        // Start the game for the host
        Player player = new Player("uname", "pass");
        RunningMode runningMode = new RunningMode(1, player);
        runningMode.setVisible(true);
    }

    public void sendMap(ArrayList<int[]> barrierList) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeInt(barrierList.size());
        for (int[] array : barrierList) {
            out.writeInt(array.length);
            for (int value : array) {
                out.writeInt(value);
            }
        }
    }

    public void updateScore(boolean isHost, int score) {
        try {
            if (isHost) {
                hostScore = score;
            } else {
                clientScore = score;
            }
            broadcastScores();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void broadcastScores() throws IOException {
        if (hostOut != null) {
            hostOut.writeInt(hostScore);
            hostOut.writeInt(clientScore);
        }
        if (clientOut != null) {
            clientOut.writeInt(hostScore);
            clientOut.writeInt(clientScore);
        }
    }

    public void setClientOut(DataOutputStream clientOut) {
        this.clientOut = clientOut;
    }



    private ArrayList<int[]> generateInitialMap() {
        // Generate initial map for the game
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        int port = 1001;
        try {
            Server server = new Server(port);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
