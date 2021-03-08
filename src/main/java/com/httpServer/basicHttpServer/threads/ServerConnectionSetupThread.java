package com.httpServer.basicHttpServer.threads;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread class to handle a server connection separately.
 * Each server connection setup thread will be responsible for
 * creating a new TCP connection and firing up a new Server
 * Connection worker thread to take care of handling the client
 * request and generate response.
 * This way, connection setup becomes independent of connection work
 * and we can accept multiple connections quickly.
 */
@Slf4j
public class ServerConnectionSetupThread extends Thread {
    private final int port;
    private final String webRoot;
    private final ServerSocket serverSocket;

    public ServerConnectionSetupThread(int port, String webRoot) throws IOException {
        this.port = port;
        this.webRoot = webRoot;
        this.serverSocket = new ServerSocket(this.port);
    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                log.info("Server Socket connection accepted: " + socket.getInetAddress() + ":" + port);

                ServerConnectionWorkerThread workerThread = new ServerConnectionWorkerThread(socket, webRoot);
                workerThread.start();
            }
        }
        catch (IOException e) {
            log.error("Unable to accept a server socket connection ", e);
        }
        finally {
            try {
                serverSocket.close();
                log.info("Successfully closed the server socket: " + serverSocket.getInetAddress());
            }
            catch (IOException e) {
                log.debug("Exception while trying to close the server socket ", e);
            }
        }
    }
}
