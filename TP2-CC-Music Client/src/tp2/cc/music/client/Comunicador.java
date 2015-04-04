/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class Comunicador {

    private final int portReceive = 9876;
    private final int portSend = 9877;
    private BufferedReader in;
    private InetAddress IPAddress;
    private DatagramSocket clientSocketSend;
    private DatagramSocket clientSocketReceive;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private byte[] sendData = new byte[255];
    private byte[] receiveData = new byte[255];

    public Comunicador() {

    }

    public void start() {
        short label = 1;
        int i;

        in = new BufferedReader(new InputStreamReader(System.in));

        try {
            clientSocketSend = new DatagramSocket();
            clientSocketReceive = new DatagramSocket();
        } catch (SocketException ex) {
            System.out.println("Fatal Error: "+ex.toString());
            System.exit(0);
        }
        try {
            IPAddress = InetAddress.getByName("localhost");
        } catch (UnknownHostException ex) {
            System.out.println("Fatal Error!");
            System.exit(0);
        }
        System.out.println("IP: " + IPAddress.toString());

        buildHello(sendData, label);

        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portSend);

        try {
            clientSocketSend.send(sendPacket);
            System.out.println("Hello Enviado!");

            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocketReceive.receive(receivePacket);
            System.out.println("Resposta recebida!");
        } catch (IOException ex) {
            System.out.println("Fatal Error!");
            System.exit(0);
        }

        //get String
        byte[] aux = new byte[255];
        receiveData = receivePacket.getData();
        for (i = 0; i < 255 - 8; i++) {
            aux[i] = receiveData[i + 8];
        }

        String resp = new String(aux);

        System.out.println("Resposta: " + resp);
        clientSocketReceive.close();
        clientSocketSend.close();
    }

    private static void buildHello(byte[] sendData, short label) {
        //Versão
        sendData[0] = 0;
        //Segurança
        sendData[1] = 0;
        //Label
        fillLabel(sendData, label);
        //Tipo
        sendData[4] = 1;
        //N campos seguintes
        sendData[5] = 0;
        //Tamanho campos Segintes
        fillSize(sendData, (short) 0);

    }

    private static void fillLabel(byte[] sendData, short label) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(label).array();
        sendData[2] = bytes[0];
        sendData[3] = bytes[1];
    }

    private static void fillSize(byte[] sendData, short size) {
        byte[] bytes = ByteBuffer.allocate(2).putShort(size).array();
        sendData[6] = bytes[0];
        sendData[7] = bytes[1];
    }

    private static void buildLogin(byte[] sendData) {
        short label = 1;
        short size = 0;
        //Versão
        sendData[0] = 0;
        //Segurança
        sendData[1] = 0;
        //Label
        fillLabel(sendData, label);
        //Tipo
        sendData[4] = 1;
        //N campos seguintes
        sendData[5] = 1;
        //Tamanho campos Segintes
        fillSize(sendData, size);
        //Campos seguintes
    }
}
