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
    private ListaCampos lc;
    private UserDB users;

    public Login(byte[] pdu, UserDB users) {
        this.users = users;
        this.pdu = new PDU(pdu);
        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());

        inicia();
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

    private void inicia() {

        alcunha = new String(lc.getCampoByTag((byte) 2).getDados());
        pass = new String(lc.getCampoByTag((byte) 3).getDados());

        Cliente c = users.login(alcunha, pass);
        if (c != null) {
            System.out.println("    Login:\n      Alcunha: " + alcunha + "\n      Pass: " + pass);
            c.incrementaMensagensEnviadas();
            c.incrementaMensagensRecebidas();

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            c.incrementaMensagensEnviadas();
            pdu.setLabel(c.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            Campo campo = new Campo((byte) 1);
            campo.setSize((short) c.getNome().length());
            campo.setDados(c.getNome().getBytes(), campo.getSize());
            lc.addCampo(campo);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());
            
            c.setSessaoAtiva(true);
        } else {
            System.out.println("    Login falhou:\n      Alcunha: " + alcunha + "\n      Pass: " + pass);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((byte) 1);
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            Campo campo = new Campo((byte) 255);
            String err = "Login failed!";
            campo.setSize((short) err.length());
            campo.setDados(err.getBytes(), campo.getSize());
            lc.addCampo(campo);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());
            //System.out.println("data[8]: " + list[0]);
        }
        //System.out.println(alcunha+"\n"+pass+"\n"+nome); all is good here

    }

}
