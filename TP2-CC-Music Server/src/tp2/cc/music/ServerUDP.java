/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2.cc.music;

import DataBase.Desafio;
import DataBase.User;
import DataBase.UserDB;
import CalculadoresResposta.Register;
import CalculadoresResposta.Hello;
import CalculadoresResposta.Login;
import CalculadoresResposta.Logout;
import CalculadoresResposta.MakeChallenge;
import CalculadoresResposta.Split;
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
    private ArrayList<Desafio> desafios;

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
        desafios=new ArrayList<>();
        //inicialize();//para testes
    }

    public void start() {
        int i = 0, j = 0;
        short nPacotes;
        byte[][] aux;

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
            if (sendData.length > 48 * 1024) { //alterar para mandar 5 de cada vez, e esperar confirmação
                Split s = new Split(sendData);
                nPacotes = s.getNPacotes();
                aux = s.generate();
                j = 0;
                while (j < nPacotes) {
                    send(aux[j], IPAddress, portSend);
                    j++;
                }

            } else {
                send(sendData, IPAddress, portSend);
            }
            System.out.println("Resposta enviada!");
            System.out.println();
            System.out.println("**Pacote de dados nº" + i + " tratado **");
            i++;
        }
    }

    private void send(byte[] sendData, InetAddress ip, int portSend) {
        //criar datagramPacket
        sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, portSend);
        if (sendPacket == null) {
            return;
        }
        //enviar resposta
        try {
            serverSocketSend.send(sendPacket);
        } catch (IOException ex) {
            System.out.println("Socket em utilização!");
            System.exit(0);
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
                Hello h = new Hello();
                return h.getResposta();
            case (2):
                //Register
                System.out.println("Tipo: REGISTER");
                Register r = new Register(receiveData, db, IPAddress, portSend);
                return r.getResposta();
            case (3):
                //Login
                System.out.println("Tipo: LOGIN");
                Login l = new Login(receiveData, db ,IPAddress, portSend);
                return l.getResposta();
            case (4):
                //Logout
                System.out.println("Tipo: LOGOUT");
                Logout logout = new Logout(receiveData, db, IPAddress, portSend);
                return logout.getResposta();
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
                MakeChallenge mkC = new MakeChallenge(receiveData, db, desafios, IPAddress, portSend);
                return mkC.getResposta();
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
            case (14):
                //Info
                System.out.println("Tipo: INFO");
                break;
            default:
                System.out.println("Tipo irreconhecível. Pacote ignorado: " + receiveData[4]);
                return null;
        }
        return null;
    }

    private void inicialize() {
        User c = new User("Nome5", "rafa", "123");
        c.setPontuacao(5);
        db.addClient(c);
        c = new User("Nome20", "nuno", "123");
        c.setPontuacao(20);
        db.addClient(c);
        c = new User("Nome15", "rui", "123");
        c.setPontuacao(15);
        db.addClient(c);

        ArrayList<User> users = db.topRanked();
        Iterator<User> ite = users.iterator();
        while (ite.hasNext()) {
            c = ite.next();
            System.out.println(c.getPontuacao());
        }

    }
}
