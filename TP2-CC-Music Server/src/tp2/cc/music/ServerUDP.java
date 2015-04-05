/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music;

import DataBase.Cliente;
import DataBase.UserDB;
import CalculadoresResposta.Register;
import CalculadoresResposta.Hello;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Rafael
 */
public class ServerUDP {

    private UserDB db;

    private int portSend = 9876;
    private final int portReceive = 9877;
    private DatagramPacket receivePacket;
    private DatagramPacket sendPacket;
    private DatagramSocket serverSocketSend = null;
    private DatagramSocket serverSocketReceive = null;
    private byte[] receiveData;
    private byte[] sendData;
    private InetAddress IPAddress;

    public ServerUDP() {
        receiveData = new byte[255];
        sendData = new byte[255];
        db = new UserDB();
        //inicialize();//para testes
    }

    public void start() {
        int i = 0;

        //iniciar os sockets 
        try {
            serverSocketReceive = new DatagramSocket(portReceive);
            serverSocketSend = new DatagramSocket(portSend);
        } catch (SocketException ex) {
            System.out.println("Socket em utilização!");
            System.exit(0);
        }

        while (true) {
            receiveData = new byte[255];
            sendData = new byte[255];
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            //fica à espera de receber um pedido
            try {
                serverSocketReceive.receive(receivePacket);
            } catch (IOException ex) {
                System.out.println("Socket em utilização!");
                System.exit(0);
            }
            IPAddress = receivePacket.getAddress();
            portSend = receivePacket.getPort();
            System.out.println("**Pacote de dados nº" + i + " recebido do IP: " + IPAddress + " **");
            System.out.println();

            receiveData = receivePacket.getData();
            sendData = buildPDU(receiveData);

            //teste resposta para o hello
            /*int j;
             byte[] aux = new byte[255];
             for (j = 0; j < 255 - 8; j++) {
             aux[j] = sendData[j + 8];
             }
             String resp = new String(aux);
             System.out.println("Resposta a enviar: " + resp);
             */
            //criar datagramPacket
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portSend);
            if (sendPacket == null) {
                continue;
            }
            //enviar resposta
            try {
                serverSocketSend.send(sendPacket);
                System.out.println("Resposta enviada!");
            } catch (IOException ex) {
                System.out.println("Socket em utilização!");
                System.exit(0);
            }

            System.out.println();
            System.out.println("**Pacote de dados nº" + i + " tratado **");
            i++;
        }
    }

    public void stop() {
        serverSocketSend.close();
        serverSocketReceive.close();
    }

    public short getLabel(byte[] data) {
        byte[] labelBytes = {data[2], data[3]};
        short label = ByteBuffer.wrap(labelBytes).order(ByteOrder.BIG_ENDIAN).getShort();
        return label;
    }

    private byte[] buildPDU(byte[] receiveData) {

        //Versão 0
        if (receiveData[0] != 0) {
            System.out.println("Versão incorreta. Pacote ignorado.");
            return null;
        }

        //Segurança 1
        System.out.println("Versão correta.");
        if (receiveData[1] == 0) {
            System.out.println("Sem segurança.");
        } else {
            System.out.println("Com segurança.");
        }

        //Label 2-3
        System.out.println("Label: " + getLabel(receiveData));

        //Tipo 4
        switch (receiveData[4]) {
            case (1):
                //Hello
                System.out.println("Tipo: HELLO");
                Hello h = new Hello(receiveData);
                return h.getResposta();
            case (2):
                //Register
                System.out.println("Tipo: REGISTER");
                Register r = new Register(receiveData);
                return r.getResposta();
            case (3):
                //Login
                System.out.println("Tipo: LOGIN");
                break;
            case (4):
                //Logout
                System.out.println("Tipo: LOGOUT");
                break;
            case (5):
                //Quit
                System.out.println("Tipo: QUIT");
                break;
            case (6):
                //End
                System.out.println("Tipo: END");
                break;
            case (7):
                //List chalenge
                System.out.println("Tipo: LIST_CHALLENGES");
                break;
            case (8):
                //Make chalenge
                System.out.println("Tipo: MAKE_CHALLENGE");
                break;
            case (9):
                //Accept Chalenge
                System.out.println("Tipo: ACCEPT_CHALLENGE");
                break;
            case (10):
                //Delete chalenge
                System.out.println("Tipo: DELETE_CHALLENGE");
                break;
            case (11):
                //Answer
                System.out.println("Tipo: ANSWER");
                break;
            case (12):
                //Retransmit
                System.out.println("Tipo: RETRANSMIT");
                break;
            case (13):
                //List Ranking
                System.out.println("Tipo: LIST_RANKING");
                break;
            default:
                System.out.println("Tipo irreconhecível. Pacote ignorado: " + receiveData[4]);
                return null;
        }
        /*
         //Nº Campos Seguintes 5
         if (receiveData[5] < 1) {
         System.out.println("Sem campos adicionais");
         return;
         } else {
         System.out.println("Numero de campos seguintes: " + receiveData[5]);
         }

         //Tamanho Lista de campos 6-7
         byte[] sizeBytes = {receiveData[6], receiveData[7]};
         short size = ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getShort();
         System.out.println("Tamanho dos restantes campos: " + size);

         //Lista de campos Seguintes 8-255
         */
        return null;
    }

    private void inicialize() {
        Cliente c = new Cliente("Nome5", "Alcunha", "123");
        c.setPontuacao(5);
        db.addClient(c);
        c = new Cliente("Nome20", "Alcunha", "123");
        c.setPontuacao(20);
        db.addClient(c);
        c = new Cliente("Nome15", "Alcunha", "123");
        c.setPontuacao(15);
        db.addClient(c);

        ArrayList<Cliente> users = db.topRanked();
        Iterator<Cliente> ite = users.iterator();
        while (ite.hasNext()) {
            c = ite.next();
            System.out.println(c.getPontuacao());
        }

    }
}
