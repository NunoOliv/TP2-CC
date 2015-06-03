package tp2.cc.music;

import CalculadoresResposta.AcceptChallenge;
import CalculadoresResposta.Campo;
import DataBase.Desafio;
import DataBase.User;
import DataBase.UserDB;
import CalculadoresResposta.Register;
import CalculadoresResposta.Hello;
import CalculadoresResposta.ListChallenge;
import CalculadoresResposta.ListaCampos;
import CalculadoresResposta.Login;
import CalculadoresResposta.Logout;
import CalculadoresResposta.MakeChallenge;
import CalculadoresResposta.PDU;
import CalculadoresResposta.Split;
import CalculadoresResposta.Transmit;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 *
 * @author Rafael
 */
public class ServerUDP {

    private UserDB db;
    private ArrayList<Desafio> desafios;

    private int portSend = 9876;
    private int portReceive = 9877;
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
        desafios = new ArrayList<>();
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

            receiveData = receive();

            System.out.println("**Pacote de dados nº" + i + " recebido do IP: " + IPAddress + " **");
            System.out.println();

            sendData = buildPDU(receiveData);

            if (sendData.length > 48 * 1024) {
                System.out.println("A dividir em PDU's mais pequenos...");
                sendDivided();
            } else {
                send(sendData, IPAddress, portSend);
            }
            System.out.println("Resposta enviada!");
            System.out.println();
            System.out.println("**Pacote de dados nº" + i + " tratado **");
            i++;
        }
    }

    private byte[] receive() {
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
        return receivePacket.getData();
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
                Login l = new Login(receiveData, db, IPAddress, portSend);
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
                ListChallenge lstC = new ListChallenge(receiveData, db, desafios, IPAddress, portSend);
                return lstC.generatePDU();
            case (8):
                //Make chalenge
                System.out.println("Tipo: MAKE_CHALLENGE");
                MakeChallenge mkC = new MakeChallenge(receiveData, db, desafios, IPAddress, portSend);
                return mkC.getResposta();
            case (9):
                //Accept Chalenge
                System.out.println("Tipo: ACCEPT_CHALLENGE");
                AcceptChallenge ac = new AcceptChallenge(receiveData, db, desafios, IPAddress, portSend);
                return ac.getResposta();
            case (10):
                //Delete chalenge
                System.out.println("Tipo: DELETE_CHALLENGE");
                break;
            case (11):
                //Answer
                System.out.println("Tipo: ANSWER");
                break;
            case (12):
                //transmit
                System.out.println("Tipo: TRANSMIT");
                Transmit t = new Transmit(receiveData, desafios);
                return t.getResposta();
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

        /*ArrayList<User> users = db.topRanked();
         Iterator<User> ite = users.iterator();
         while (ite.hasNext()) {
         c = ite.next();
         System.out.println(c.getPontuacao());
         }*/
    }

    private void sendDivided() {
        int j;
        short nPacotes, nextPackage;
        byte[][] aux;
        byte[] next;
        PDU pdu;
        ListaCampos lc;
        Campo c;

        Split s = new Split(sendData);
        nPacotes = s.getNPacotes();
        aux = s.generate();
        j = 1;

        while (j <= nPacotes) {
            send(aux[j - 1], IPAddress, portSend);
            j++;
            next = receive();
            pdu = new PDU(next);
            lc = new ListaCampos(pdu.getLista(), pdu.getnCampos());
            c = lc.getCampoByTag((byte) 30);
            nextPackage = c.byteToShort(c.getDados());
            /*if (j >= nPacotes) {
             //foram todos enviados
             //send all is good
             lc = new ListaCampos();
             c = new Campo((byte) 30);
                
             pdu = new PDU((byte) 0, (byte) 0, 0, (byte) 0, nCampos, nPacotes, next);
             break;
             }*/
            if (j == nextPackage) {
                //all is good
                System.out.println("Pacote " + j + " sincronizado!");
            } else {
                System.out.println("Pacote " + j + " enviado, pacote " + nextPackage + " esperado!");
                System.exit(0);
            }

        }
    }
}
