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

        in = new BufferedReader(new InputStreamReader(System.in));

        clientSocket = new DatagramSocket();
        IPAddress = InetAddress.getByName("localhost");
        System.out.println("IP: "+IPAddress.toString());
        
        
        sendData[0] = 1; 
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);
        
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        
        String modifiedSentence = new String(receivePacket.getData());
        
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
    }

}
