package CalculadoresResposta;

import DataBase.Desafio;
import DataBase.Parser;
import DataBase.User;
import DataBase.UserDB;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MakeChallenge {

    private PDU pdu;
    private ListaCampos lc;
    private InetAddress ipAddress;
    private int port;
    private UserDB users;
    private ArrayList<Desafio> desafios;
    private User u;

    private String nome;

    public MakeChallenge(byte[] pdu, UserDB users, ArrayList<Desafio> desafios, InetAddress ip, int port) {
        this.pdu = new PDU(pdu);

        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());
        this.u = null;

        this.users = users;
        this.desafios = desafios;

        this.ipAddress = ip;
        this.port = port;

        inicia();
    }

    private void inicia() {
        String data, hora;

        //System.out.println("IP Recebido: " + ipAddress + " Port: " + port);
        u = users.getCliente(ipAddress, port);
        if (u == null) {//cliente não existe
            System.out.println("Um cliente tentou criar um desafio sem estar registado!");
            generateError("Tentou criar um desafio sem estar registado!");
            return;
        }
        u.incrementaMensagensRecebidas();
        Campo c = lc.getCampoByTag((byte) 7);//nome
        nome = new String(c.getDados());

        for (Desafio d : desafios) {
            if (d.getNome().equals(nome)) {
                System.out.println("Um cliente usou um nome de um desafio já em uso!");
                generateError("Nome do desafio já está em uso!");
                return;
            }
        }

        c = lc.getCampoByTag((byte) 4);//dia
        if (c == null) {
            return;
        }
        data = new String(c.getDados()); //formato AAMMDD

        c = lc.getCampoByTag((byte) 5);//hora
        if (c == null) {
            return;
        }
        hora = new String(c.getDados()); //formato HHMMSS

        Random r = new Random();
        Parser p = new Parser();
        int max = 1, minimo = 1;
        int nDesafio = r.nextInt((max - minimo) + 1) + minimo;
        System.out.println("Desafio escolhido: " + nDesafio);

        try {
            p.parseDesafioWnum(nDesafio);
        } catch (IOException ex) {
            System.out.println("Fatal Error: Alguma coisa correu mal ao ler o ficheiro.\n" + ex.getMessage() + "\n");
            System.exit(0);
        }
        Desafio desafio = new Desafio(p.getPerguntas());
        desafio.setNome(nome);
        try {
            desafio.setData(data);
            desafio.setHora(hora);
            desafio.dateSFtoIF();

            //teste:
            //System.out.println("ano: " + ano + " mes: " + mes + " dia: " + dia + " hora: " + hora + " min: " + min + " seg: " + seg);
        } catch (Exception ex) {
            generateError("Data ou Hora mal formados!");
            return;
        }
        desafio.addJogador(u);
        desafios.add(desafio);
        System.out.println("Novo desafio criado: " + nome);

        //*****************
        //generateResposta
        //*****************
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) (u.getnMensagensEnviadas() + 1));
        u.incrementaMensagensEnviadas();
        pdu.setTipo((byte) 0);
        //mantém-se a mesma lista de campos.
        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());

    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

    private void generateError(String erro) {

        lc = new ListaCampos();

        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) 0); //visto q não se conhece o user
        pdu.setTipo((byte) 0);

        Campo campo = new Campo((byte) 255);
        campo.setDados(erro.getBytes(), (short) erro.getBytes().length);
        lc.addCampo(campo);

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
    }

}
