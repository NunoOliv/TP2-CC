package CalculadoresResposta;

import DataBase.User;
import DataBase.UserDB;
import java.net.InetAddress;

public class Logout {

    private PDU pdu;
    private ListaCampos lc;
    private UserDB users;
    private InetAddress ip;
    private int port;

    public Logout(byte[] receiveData, UserDB db, InetAddress IPAddress, int portSend) {
        this.pdu = new PDU();
        this.lc = new ListaCampos();
        this.users = db;
        this.ip = IPAddress;
        this.port = portSend;

        incia();
    }

    private void incia() {
        User u = users.getCliente(ip, port);
        if (u != null && u.isSessaoAtiva()) {
            u.setnMensagensRecebidas((short) 0);
            u.setSessaoAtiva(false);

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            u.incrementaMensagensEnviadas();
            pdu.setLabel(u.getnMensagensEnviadas());
            u.setnMensagensEnviadas((short) 0);
            pdu.setTipo((byte) 0);

            lc.addCampo(new Campo((byte) 0));

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

            System.out.println(u.getAlcunha() + " desconectou-se.");
        } else {
            String err;

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);

            if (u == null) {
                err = "Utilizador não está registado!";
                pdu.setLabel((short) 0);
            } else {
                err = "Utilizador não estava com sessão ativa!";
                u.setnMensagensRecebidas((short) 0);
                u.setSessaoAtiva(false);
                u.incrementaMensagensEnviadas();
                pdu.setLabel(u.getnMensagensEnviadas());
                u.setnMensagensEnviadas((short) 0);
            }
            pdu.setTipo((byte) 0);

            Campo c = new Campo((byte) 255);
            c.setDados(err.getBytes());
            lc.addCampo(c);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

            System.out.println("Um utilizador desconectou-se com erros.");
        }

    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
