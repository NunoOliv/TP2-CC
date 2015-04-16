package CalculadoresResposta;


public class Hello {

    PDU pdu;
    ListaCampos lc;

    public Hello() {
        pdu = new PDU();
        lc = new ListaCampos();
        incia();
    }

    private void incia() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) 0);
        pdu.setTipo((byte) 0);
        
        
        lc.addCampo(new Campo (0, 0, null));
        
        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
        
        //byte[] lc.gerate()
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
