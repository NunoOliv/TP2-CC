package CalculadoresResposta;

import DataBase.UserDB;
import java.net.InetAddress;

public class Register {

    private String nome;
    private String alcunha;
    private String pass;
    private InetAddress ipAddress;
    private int port;

    private PDU pdu;
    private UserDB users;

    public Register(byte[] pdu, UserDB users, InetAddress ip, int port) {
        this.users = users;
        this.pdu = new PDU(pdu);
        this.ipAddress = ip;
        this.port = port;

        inicia();
    }

    private void inicia() {
        byte[] al = new byte[75];
        byte[] no = new byte[75];
        byte[] pa = new byte[75];

        byte[] lista = pdu.getLista();

        System.arraycopy(lista, 0, al, 0, 74);
        System.arraycopy(lista, 75, pa, 0, 74);
        System.arraycopy(lista, 150, no, 0, 74);

        alcunha = new String(al).trim();
        nome = new String(no).trim();
        pass = new String(pa).trim();

        if (users.addCliente(alcunha, pass, nome, ipAddress, port)) {
            System.out.println("    Novo utilizador:\n      Alcunha: "+alcunha+"\n      Pass: "+pass+"\n      Nome: "+nome);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((short) 1);
            pdu.setTipo((byte) 0);
            pdu.setnCampos((byte) 1);
            pdu.setTamanho((short) 1);

            byte[] list = new byte[247];
            list[0] = 0;
            pdu.setLista(list);
        }else{
            System.out.println("    Registo falhou:\n      Alcunha: "+alcunha+"\n      Pass: "+pass+"\n      Nome: "+nome);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((short) 1);
            pdu.setTipo((byte) 0);
            pdu.setnCampos((byte) 1);
            
            short size;
            byte[] list = new byte[247];
            byte[] aux = "Alcunha em uso!".getBytes();
            size=(short) aux.length;
            
            list[0] = (byte)255;
            System.arraycopy(aux, 0, list, 1, size);
            
            pdu.setTamanho((short) (size+1));
            pdu.setLista(list);
            //System.out.println("data[8]: " + list[0]);
        }
        //System.out.println(alcunha+"\n"+pass+"\n"+nome); all is good here
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
