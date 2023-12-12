package pt.isec.pd.server.rest.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class DatabaseHeardbeat implements Runnable {
    public DatabaseHeardbeat() {
    }

    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName("230.44.44.44");
            int port = 4444;
            MulticastSocket multicastSocket = new MulticastSocket();
            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                DatagramPacket datagramPacket;

                @Override
                public void run() {
                    try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                         ObjectOutputStream out = new ObjectOutputStream(byteStream)) {
                        out.writeObject("Base de dados sofreu atualizacoes!");

                        // Create a UDP packet with the serialized data
                        datagramPacket = new DatagramPacket(byteStream.toByteArray(), byteStream.size(), group, port);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Send the heartbeat packet
                    try {
                        multicastSocket.send(datagramPacket);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println("Heartbeat sent: RMI Port = " + datagramPacket.getData());
                }
            }, 0, 1000);

            Thread.sleep(Long.MAX_VALUE);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
