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

/**
 *
 * @author Rafael
 */
public class TP2CCMusicClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, SocketException, IOException {
        int port = 9876;
        BufferedReader in;
        InetAddress IPAddress;
        DatagramSocket clientSocket;
        DatagramPacket receivePacket;
        DatagramPacket sendPacket;
        byte[] sendData = new byte[255];
        byte[] receiveData = new byte[255];
        short label = 1;

        in = new BufferedReader(new InputStreamReader(System.in));

        clientSocket = new DatagramSocket();
        IPAddress = InetAddress.getByName("localhost");
        System.out.println("IP: " + IPAddress.toString());

        buildHello(sendData,label);

        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        
        /*
         receivePacket = new DatagramPacket(receiveData, receiveData.length);
         clientSocket.receive(receivePacket);

         String modifiedSentence = new String(receivePacket.getData());

         System.out.println("FROM SERVER:" + modifiedSentence);*/
        clientSocket.close();
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

    private static void buildHello(byte[] sendData,short label) {
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
        fillSize(sendData, (short)0);
        
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
