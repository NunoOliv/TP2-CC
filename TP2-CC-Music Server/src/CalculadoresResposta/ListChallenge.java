package CalculadoresResposta;

import DataBase.Desafio;
import DataBase.User;
import DataBase.UserDB;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;

public class ListChallenge {

    private final PDU pdu;
    private final ListaCampos lc;
    private final ArrayList<Desafio> desafios;
    private final UserDB users;
    private final InetAddress IP;
    private final int port;

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
        int ano, mes, dia, hora, min, seg;
        String aux;

        if (u != null) {
            u.incrementaMensagensRecebidas();

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            u.incrementaMensagensEnviadas();
            pdu.setLabel(u.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);

            for (Desafio d : desafios) {
                Campo c = new Campo((byte) 7);
                c.setDados(d.getNome().getBytes(), (short) d.getNome().getBytes().length);
                lc.addCampo(c);

                c = new Campo((byte) 4); //data AAMMDD
                ano = d.getHora().get(Calendar.YEAR) - 2000;
                mes = d.getHora().get(Calendar.MONTH);
                dia = d.getHora().get(Calendar.DAY_OF_MONTH);
                aux = Integer.toString(ano);
                aux = aux.concat(Integer.toString(mes));
                aux = aux.concat(Integer.toString(dia));
                c.setDados(aux.getBytes(), (short) aux.getBytes().length);
                lc.addCampo(c);

                c = new Campo((byte) 5); //hora HHMMSS
                hora = d.getHora().get(Calendar.HOUR_OF_DAY);
                min = d.getHora().get(Calendar.MINUTE);
                seg = d.getHora().get(Calendar.SECOND);
                aux = Integer.toString(hora);
                aux = aux.concat(Integer.toString(min));
                aux = aux.concat(Integer.toString(seg));
                c.setDados(aux.getBytes(), (short) aux.getBytes().length);
                lc.addCampo(c);

                //testes:
                System.out.println("Nome: " + d.getNome());
                System.out.println("ano: " + ano + " mes: " + mes + " dia: " + dia + " hora: " + hora + " min: " + min + " seg: " + seg);
            }

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
