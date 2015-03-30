/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;

/**
 *
 * @author Rafael
 */
public class TP2CCMusic {

    public static HashSet<Cliente> clientes;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, IOException {
        int port = 9876;
        DatagramPacket receivePacket;
        DatagramPacket sendPacket;
        DatagramSocket serverSocket;
        byte[] receiveData = new byte[255];
        byte[] sendData = new byte[255];
        int i=0;

        InetAddress IPAddress;

        clientes = new HashSet<>();

        serverSocket = new DatagramSocket(port);

        while (true) {
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("**Pacote de dados nº"+i+" recebido**");
            receiveData = receivePacket.getData();

            menu(receiveData);
            
            System.out.println("**Pacote de dados nº"+i+" tratado**");
            i++;
            IPAddress = receivePacket.getAddress();
            port = receivePacket.getPort();
            
            /*Codigo para enviar para os clientes....
             sendData = ....
             sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
             serverSocket.send(sendPacket);
            */
        }
    }

    private static void menu(byte[] receiveData) {
        if (receiveData[0] != 0) {
            System.out.println("Versão incorreta. Pacote ignorado.");
            return;
        }
        System.out.println("Versão correta.");
        if (receiveData[1] == 0) {
            System.out.println("Sem segurança.");
        }else{
            System.out.println("Com segurança.");
        }

    }

}
