/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import DataBase.Cliente;
import DataBase.UserDB;

/**
 *
 * @author Rafael
 */
public class Login {

    private String alcunha;
    private String pass;

    private PDU pdu;
    private UserDB users;

    public Login(byte[] pdu, UserDB users) {
        this.users = users;
        this.pdu = new PDU(pdu);

        inicia();
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

    private void inicia() {
        byte[] al = new byte[75];
        byte[] pa = new byte[75];

        byte[] lista = pdu.getLista();

        System.arraycopy(lista, 0, al, 0, 74);
        System.arraycopy(lista, 75, pa, 0, 74);

        alcunha = new String(al).trim();
        pass = new String(pa).trim();

        Cliente c = users.login(alcunha, pass);
        if (c != null) {
            //System.out.println("    Novo utilizador:\n      Alcunha: "+alcunha+"\n      Pass: "+pass+"\n      Nome: "+nome);
            c.incrementaMensagensRecebidas();

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            c.incrementaMensagensEnviadas();
            pdu.setLabel(c.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);
            pdu.setnCampos((byte) 1);
      
            byte[] list = new byte[247];
            byte[] aux = c.getNome().getBytes();
            short size = (short) aux.length;

            list[0] = 0;
            System.arraycopy(aux, 0, list, 1, size);
            
            pdu.setTamanho((short) (size + 1));
            pdu.setLista(list);
        } else {
            //System.out.println("    Registo falhou:\n      Alcunha: " + alcunha + "\n      Pass: " + pass + "\n      Nome: " + nome);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            c.incrementaMensagensEnviadas();
            pdu.setLabel(c.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);
            pdu.setnCampos((byte) 1);

            short size;
            byte[] list = new byte[247];
            byte[] aux = "Login failed!".getBytes();
            size = (short) aux.length;

            list[0] = (byte) 255;
            System.arraycopy(aux, 0, list, 1, size);

            pdu.setTamanho((short) (size + 1));
            pdu.setLista(list);
            //System.out.println("data[8]: " + list[0]);
        }
        //System.out.println(alcunha+"\n"+pass+"\n"+nome); all is good here

    }

}
