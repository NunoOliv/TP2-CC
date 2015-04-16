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
    private ListaCampos lc;

    public Register(byte[] pdu, UserDB users, InetAddress ip, int port) {
        this.users = users;
        this.pdu = new PDU(pdu);
        
        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());
        System.out.println(lc.toString());
        this.ipAddress = ip;
        this.port = port;

        inicia();
    }

    private void inicia() {

        nome = new String(lc.getCampoByTag((byte) 1).getDados());
        alcunha = new String(lc.getCampoByTag((byte) 2).getDados());
        pass = new String(lc.getCampoByTag((byte) 3).getDados());

        if (users.addCliente(alcunha, pass, nome, ipAddress, port)) {
            System.out.println("    Novo utilizador:\n      Alcunha: " + alcunha + "\n      Pass: " + pass + "\n      Nome: " + nome);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((short) 1);
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            lc.addCampo(new Campo((byte) 0));

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());
        } else {
            System.out.println("    Registo falhou:\n      Alcunha: " + alcunha + "\n      Pass: " + pass + "\n      Nome: " + nome);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((short) 1);
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            Campo c = new Campo((byte) 255);
            String err = "Alcunha em uso!";
            c.setSize((short) err.length());
            c.setDados(err.getBytes(), c.getSize());
            lc.addCampo(c);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());
        }
        //System.out.println(alcunha+"\n"+pass+"\n"+nome); //all is good here
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
