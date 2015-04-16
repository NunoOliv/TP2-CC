/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Build;

import java.nio.ByteBuffer;

/**
 *
 * @author Rafael
 */
public class Login {

    private PDU pdu;
    private ListaCampos lc;
    private short label;

    public Login(String alcunha, String pass, short label) {
        pdu = new PDU();
        lc = new ListaCampos();
        this.label = label;

        lc.addCampo(new Campo((byte) 2, (short) alcunha.length(), alcunha));
        lc.addCampo(new Campo((byte) 3, (short) pass.length(), pass));
    }

    public byte[] generate() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(label);
        pdu.setTipo((byte) 3);
        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());

        return pdu.generatePDU();
    }

}
