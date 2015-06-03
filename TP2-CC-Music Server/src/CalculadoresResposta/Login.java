package CalculadoresResposta;

import DataBase.User;
import DataBase.UserDB;
import java.net.InetAddress;

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
    private InetAddress IP;
    private int port;

    public Login(byte[] pdu, UserDB users, InetAddress IP, int port) {
        this.users = users;
        this.pdu = new PDU(pdu);
        this.lc = new ListaCampos(this.pdu.getLista(), this.pdu.getnCampos());
        this.IP = IP;
        this.port = port;

        inicia();
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

    private void inicia() {

        alcunha = new String(lc.getCampoByTag((byte) 2).getDados());
        pass = new String(lc.getCampoByTag((byte) 3).getDados());

        User u = users.login(alcunha, pass);
        if (u != null) {
            System.out.println("    Login:\n      Alcunha: " + alcunha + "\n      Pass: " + pass);
            u.incrementaMensagensEnviadas();
            u.incrementaMensagensRecebidas();

            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            u.incrementaMensagensEnviadas();
            pdu.setLabel(u.getnMensagensEnviadas());
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            Campo campo = new Campo((byte) 1);
            campo.setDados(u.getNome().getBytes());
            lc.addCampo(campo);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());

            System.out.println("IP Recebido: " + IP + " Port: " + port);
            
            u.setSessaoAtiva(true);
            u.setEnderecoIP(this.IP);
            u.setPort(this.port);
        } else {
            System.out.println("    Login falhou:\n      Alcunha: " + alcunha + "\n      Pass: " + pass);
            pdu.setVersao((byte) 0);
            pdu.setSeguranca((byte) 0);
            pdu.setLabel((byte) 1);
            pdu.setTipo((byte) 0);

            lc = new ListaCampos();
            Campo campo = new Campo((byte) 255);
            String err = "Login failed!";
            campo.setDados(err.getBytes());
            lc.addCampo(campo);

            pdu.setnCampos(lc.getNCampos());
            pdu.setTamanho(lc.getTotalSize());
            pdu.setLista(lc.generate());
            //System.out.println("data[8]: " + list[0]);
        }
        //System.out.println(alcunha+"\n"+pass+"\n"+nome); all is good here

    }

}
