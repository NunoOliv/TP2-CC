package CalculadoresResposta;

import DataBase.Desafio;
import DataBase.User;
import DataBase.UserDB;
import java.net.InetAddress;
import java.util.ArrayList;

public class AcceptChallenge {

    private PDU pdu;
    private ListaCampos lc;

    private InetAddress ipAddress;
    private int port;

    private UserDB users;
    private ArrayList<Desafio> desafios;
    private User u;

    private String nome;

    public AcceptChallenge(byte[] receiveData, UserDB db, ArrayList<Desafio> desafios, InetAddress ip, int port) {
        this.pdu = new PDU(receiveData);
        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());
        this.u = null;

        this.users = db;
        this.desafios = desafios;

        this.ipAddress = ip;
        this.port = port;

        inicia();
    }

    private void inicia() {
        boolean encontrou = false;

        u = users.getCliente(ipAddress, port);
        if (u == null) {//cliente não existe
            System.out.println("Um cliente tentou entrar num desafio sem estar registado!");
            generateError("Tentou entrar num desafio sem estar registado!");
            return;
        }
        u.incrementaMensagensRecebidas();

        Campo c = lc.getCampo(0);
        if (c.getTag() != 7) {
            generateError("Campo inesperado!");
            return;
        }
        this.nome = new String(c.getDados());
        System.out.println("Inscrição para o desafio \"" + nome + "\".");

        for (Desafio d : desafios) {

            if (d.getNome().equals(this.nome)) {

                for (User jogador : d.getJogadores()) {

                    if (jogador.getAlcunha().equals(u.getAlcunha())) {
                        System.out.println("Utilizador já estava inscrito no desafio.");
                        generateError("Já está inscrito neste desafio!");
                        return;
                    }

                }
                d.addJogador(u);
                System.out.println("Utilizador foi inscrito no desafio.");
                encontrou = true;
                break;

            }

        }

        if (!encontrou) {
            System.out.println("Desafio não existe!");
            generateError("Desafio \"" + nome + "\" não existe!");
            return;
        }

        //Generate PDU 
        lc = new ListaCampos();

        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(u.getnMensagensEnviadas());
        u.incrementaMensagensEnviadas();
        pdu.setTipo((byte) 0);

        lc.addCampo(new Campo((byte) 0));
        //System.out.println(lc.toString());

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());

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

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
