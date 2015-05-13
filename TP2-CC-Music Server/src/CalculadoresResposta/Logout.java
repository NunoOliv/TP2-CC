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
        User c = users.getCliente(ip, port);
        if (c != null && c.isSessaoAtiva()) {
            c.setnMensagensRecebidas((short) 0);
            c.setSessaoAtiva(false);

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            c.incrementaMensagensEnviadas();
            pdu.setLabel(c.getnMensagensEnviadas());
            c.setnMensagensEnviadas((short) 0);
            pdu.setTipo((byte) 0);

            lc.addCampo(new Campo((byte) 0));

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

            System.out.println(c.getAlcunha() + " desconectou-se.");
        } else {
            String err;

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);

            if (c == null) {
                err = "Utilizador não está registado!";
                pdu.setLabel((short) 2);
            } else {
                err = "Utilizador não estava com sessão ativa!";
                c.setnMensagensRecebidas((short) 0);
                c.setSessaoAtiva(false);
                c.incrementaMensagensEnviadas();
                pdu.setLabel(c.getnMensagensEnviadas());
                c.setnMensagensEnviadas((short) 0);
            }
            pdu.setTipo((byte) 0);

            lc.addCampo(new Campo((byte) 255, (short) err.length(), err.getBytes()));

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
