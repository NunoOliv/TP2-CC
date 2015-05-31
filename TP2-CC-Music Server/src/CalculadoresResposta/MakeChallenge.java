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

public class MakeChallenge {

    private final PDU pdu;
    private ListaCampos lc;
    private final InetAddress ipAddress;
    private final int port;
    private final UserDB users;
    private final ArrayList<Desafio> desafios;
    private User u;

    private String nome;
    private GregorianCalendar data;

    public MakeChallenge(byte[] pdu, UserDB users, ArrayList<Desafio> desafios, InetAddress ip, int port) {
        this.pdu = new PDU(pdu);

        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());
        this.u = null;

        this.users = users;
        this.desafios = desafios;

        this.ipAddress = ip;
        this.port = port;

        this.data = new GregorianCalendar();

        inicia();
    }

    private void inicia() {
        String aux;
        int ano, mes, dia, hora, min, seg;
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
        aux = new String(c.getDados()); //formato AAMMDD
        ano = 2000 + Integer.parseInt(aux.substring(0, 2));
        mes = Integer.parseInt(aux.substring(2, 4));
        dia = Integer.parseInt(aux.substring(4));

        c = lc.getCampoByTag((byte) 5);//hora
        if (c == null) {
            return;
        }
        aux = new String(c.getDados()); //formato HHMMSS
        hora = Integer.parseInt(aux.substring(0, 2));
        min = Integer.parseInt(aux.substring(2, 4));
        seg = Integer.parseInt(aux.substring(4));

        //teste:
        //System.out.println("ano: " + ano + " mes: " + mes + " dia: " + dia + " hora: " + hora + " min: " + min + " seg: " + seg);
        data.clear();
        data.set(ano, mes, dia, hora, min, seg);

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
        desafio.setHora(data);
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
