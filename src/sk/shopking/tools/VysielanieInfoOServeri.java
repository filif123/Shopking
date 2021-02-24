package sk.shopking.tools;

import java.io.IOException;
import java.net.*;

public class VysielanieInfoOServeri extends Thread{
    protected DatagramSocket socket = null;
    protected boolean running;
    protected byte[] buf = new byte[256];

    public VysielanieInfoOServeri() throws IOException {
        socket = new DatagramSocket(null);
        socket.setReuseAddress(true);
        socket.bind(new InetSocketAddress(4445));
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            boolean nothing;
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.setSoTimeout(2000);
                try {
                    socket.receive(packet);
                    nothing = false;
                }catch (SocketTimeoutException ignored){ nothing = true;}
                if (!nothing){
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (received.startsWith("E")) {
                        running = false;
                    }
                    else if (received.startsWith("S")){
                        if (AppSettings.loadIPSQL().equals("localhost") || AppSettings.loadIPSQL().equals("127.0.0.1") || isAvailable(3306)){
                            socket.send(packet);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public static boolean isAvailable(int portNr) {
        boolean portFree;
        try (ServerSocket ignored = new ServerSocket(portNr)) {
            portFree = true;
        } catch (IOException e) {
            portFree = false;
        }
        return portFree;
    }

    public void terminate(){
        running = false;
    }
}
