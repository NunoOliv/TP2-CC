package CalculadoresResposta;

import DataBase.Desafio;
import DataBase.User;
import DataBase.UserDB;
import java.net.InetAddress;
import java.util.ArrayList;

public class ListChallenge {

    private PDU pdu;
    private ListaCampos lc;
    private ArrayList<Desafio> desafios;
    private UserDB users;
    private InetAddress IP;
    private int port;

    public ListChallenge(byte[] receiveData, UserDB db, ArrayList<Desafio> desafios, InetAddress IPAddress, int portSend) {
        this.pdu = new PDU(receiveData);
        this.lc = new ListaCampos(pdu.getLista(), pdu.getnCampos());
        this.desafios = desafios;
        this.users = db;
        this.IP = IPAddress;
        this.port = portSend;

        inicia();
    }

    public byte[] generatePDU() {
        return pdu.generatePDU();
    }

    private void inicia() {
        User u = users.getCliente(IP, port);

        lc = new ListaCampos();
        if (u != null) {
            u.incrementaMensagensRecebidas();

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            u.incrementaMensagensEnviadas();
            pdu.setLabel(u.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);

            System.out.println("Desafios:");
            for (Desafio d : desafios) {
                Campo c = new Campo((byte) 7);
                c.setDados(d.getNome().getBytes(), (short) d.getNome().getBytes().length);
                lc.addCampo(c);

                try {
                    d.dateSFtoIF();
                } catch (Exception ex) {
                    System.out.println("Fatal Error!");
                    System.exit(0);
                }

                System.out.print("Nome: \"" + d.getNome() + "\" Jogadores: ");
                for(User j:d.getJogadores()){
                    System.out.print(j.getAlcunha()+", ");
                }
                System.out.println("!");
                
                c = new Campo((byte) 4); //data AAMMDD
                c.setDados(d.getData().getBytes(), (short) d.getData().getBytes().length);
                //System.out.print("Data: " + aux);
                lc.addCampo(c);

                c = new Campo((byte) 5); //hora HHMMSS
                c.setDados(d.getHora().getBytes(), (short) d.getHora().getBytes().length);
                //System.out.print(" Hora: " + aux);
                lc.addCampo(c);

                //testes:
                //System.out.println(" Nome: " + d.getNome());
            }
            //System.out.println(" NCampos: " + lc.getNCampos() + "\nTotal size: " + lc.getTotalSize());
            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

        } else {
            String err;

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);

            err = "Tentou ver a lista de desafios sem estar registado!";
            pdu.setLabel((short) 0);
            pdu.setTipo((byte) 0);

            lc.addCampo(new Campo((byte) 255, (short) err.getBytes().length, err.getBytes()));

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

            System.out.println("Um utilizador tentou ver a lista de desafios sem estar registado!");
        }
    }

}
