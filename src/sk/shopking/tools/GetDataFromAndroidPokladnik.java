package sk.shopking.tools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetDataFromAndroidPokladnik extends Thread{
    protected DatagramSocket socket = null;
    protected boolean running;
    private final List<GetDataFromAndroidPokladnikListener> listeners = new ArrayList<>();

    public GetDataFromAndroidPokladnik() throws IOException {
        socket = new DatagramSocket(4446);
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            byte[] buf = new byte[256];
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.setSoTimeout(1000);
                try {
                    socket.receive(packet);
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    packet = new DatagramPacket(buf, buf.length, address, port);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    final String received2 = received.trim();
                    if (received2.startsWith("E")) {
                        running = false;
                    }
                    else if (!received2.startsWith("S")){
                        String received3 = received2.replaceAll("[^0-9]","");
                        for (GetDataFromAndroidPokladnikListener gdfapl : listeners) {
                            gdfapl.receivedBarcodeEvent(received3);
                        }
                        socket.send(packet);
                    }
                }catch (SocketTimeoutException ignored){}


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    public void addListener(GetDataFromAndroidPokladnikListener toAdd) {
        listeners.add(toAdd);
    }

    public void removeListener(GetDataFromAndroidPokladnikListener toRemove) {
        listeners.remove(toRemove);
    }

    public void removeListeners() {
        listeners.clear();
    }

    public void terminate(){
        running = false;
    }
}
