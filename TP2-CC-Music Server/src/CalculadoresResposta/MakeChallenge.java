/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CalculadoresResposta;

import DataBase.Desafio;
import DataBase.Parser;
import DataBase.User;
import DataBase.UserDB;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rafael
 */
public class MakeChallenge {

    private PDU pdu;
    private ListaCampos lc;
    private InetAddress ipAddress;
    private int port;
    private UserDB users;
    private ArrayList<Desafio> desafios;

    private String nome;
    private GregorianCalendar data;

    public MakeChallenge(byte[] pdu, UserDB users, ArrayList<Desafio> desafios, InetAddress ip, int port) {
        this.pdu = new PDU(pdu);
        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());

        this.users = users;
        this.desafios = desafios;

        this.ipAddress = ip;
        this.port = port;

        inicia();
    }

    private void inicia() {
        String aux;
        int ano, mes, dia, hora, min, seg;
        User u = users.getCliente(ipAddress, port);
        if (u == null) {//cliente não existe
            System.out.println("Um cliente tentou criar um desafio sem estar registado!");
            return;
        }

        Campo c = lc.getCampoByTag((byte) 7);//nome
        nome = new String(c.getDados());

        c = lc.getCampoByTag((byte) 4);//dia
        if (c == null) {
            return;
        }
        aux = new String(c.getDados()); //formato AAMMDD
        ano = aux.codePointAt(0) * 10 + aux.codePointAt(1);
        mes = aux.codePointAt(2) * 10 + aux.codePointAt(3);
        dia = aux.codePointAt(4) * 10 + aux.codePointAt(5);

        c = lc.getCampoByTag((byte) 5);//hora
        if (c == null) {
            return;
        }
        aux = new String(c.getDados()); //formato HHMMSS
        hora = aux.codePointAt(0) * 10 + aux.codePointAt(1);
        min = aux.codePointAt(2) * 10 + aux.codePointAt(3);
        seg = aux.codePointAt(4) * 10 + aux.codePointAt(5);

        //teste:
        System.out.println("ano: " + ano + "mes: " + mes + "dia: " + dia + "hora: " + hora + "min: " + min + "seg: " + seg);
        data.set(ano, mes, dia, hora, min, seg);
                
        Random r=new Random();
        Parser p = new Parser();
        int max=1,minimo=1;
        int nDesafio = r.nextInt((max - minimo) + 1) + minimo;
        System.out.println("Desafio escolhido: "+nDesafio);
        
        try {
            p.parseDesafioWnum(nDesafio);
        } catch (IOException ex) {
            System.out.println("Fatal Error: Alguma coisa correu mal ao ler o ficheiro.\n"+ex.getMessage()+"\n");
            System.exit(0);
        }
        Desafio desafio = new Desafio(p.getPerguntas());
        desafio.setNome(nome);
        desafio.setHora(data);
        desafio.addJogador(u);
        
        generateResposta();
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

    private void generateResposta() {
        
    }

}
