/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Build;

import java.util.Arrays;

/**
 *
 * @author Rafael
 */
public class MakeChallenge {

    private PDU pdu;
    private ListaCampos lc;

    private String nome;
    private String dia;
    private String hora;
    private short label;

    public MakeChallenge(String nome, String dia, String hora, short label) {
        this.pdu = new PDU();
        this.lc = new ListaCampos();

        this.nome = nome;
        this.dia = dia;
        this.hora = hora;

        this.label = label;

        inicia();
    }

    private void inicia() {
        Campo c;

        c = new Campo((byte) 7);
        c.setDados(nome.getBytes(), (short) nome.getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 4);
        c.setDados(dia.getBytes(), (short) dia.getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 5);
        c.setDados(hora.getBytes(), (short) hora.getBytes().length);
        lc.addCampo(c);

        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(label);
        pdu.setTipo((byte) 8);
        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
    }

    public byte[] generate() {
        return pdu.generatePDU();
    }

}
