package tp2.cc.music.client;

import Build.Hello;
import Exception.NotOkException;
import Exception.UnknownTypeException;
import Exception.VersionMissmatchException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Rafael
 */
public class Comunicador {

    private final int portSend = 9877;
    private Scanner in;
    private InetAddress IPAddress;
    private DatagramSocket clientSocket;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private byte[] sendData = new byte[255];
    private byte[] receiveData = new byte[255];
    private Interpretador inter;

    public Comunicador(Interpretador i) {
        inter = i;
    }

    public void start() {
        short label = 1;
        int i;

        in = new Scanner(System.in);

        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(10000);//5 segundos
        } catch (SocketException ex) {
            System.out.println("Fatal Error: " + ex.toString());
            System.exit(0);
        }

        try {
            IPAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            System.out.println("Fatal Error!");
            System.exit(0);
        }
        System.out.println("IP: " + IPAddress.toString());

        Hello h = new Hello();
        sendData = h.generate();

        receiveData = send(sendData);

        try {
            inter.checkOK(receiveData);
        } catch (UnknownTypeException ex) {
            System.out.println("Fatal Error: UnknownTypeException");
            System.exit(0);
        } catch (VersionMissmatchException ex) {
            System.out.println("Fatal Error: VersionMissmatchException");
            System.exit(0);
        } catch (NotOkException ex) {
            System.out.println("Fatal Error: NotOkException");
            System.exit(0);
        }
    }

    public byte[] send(byte[] send) {
        sendData = send;
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portSend);
        while (true) {
            try {
                clientSocket.send(sendPacket);
                System.out.println("Enviado!");

                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                System.out.println("Resposta recebida!");
                break;
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout!\nTentar novamente? (Y|N)");
                char c = in.nextLine().charAt(0);
                if (c == 'N' || c == 'n') {
                    System.exit(0);
                }
                if (c != 'Y' && c != 'y') {
                    System.out.println("Opção Introduzida inválida!");
                    System.exit(0);
                }
            } catch (IOException ex) {
                System.out.println("Fatal Error!");
                System.exit(0);
            }
        }

        return receivePacket.getData();
    }

    public void end() {
        clientSocket.close();
        System.exit(0);
    }
}
