package CalculadoresResposta;

public class Hello {

    PDU pdu;

    public Hello() {
        pdu = new PDU();
        incia();
    }

    private void incia() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) 0);
        pdu.setTipo((byte) 0);
        pdu.setnCampos((byte) 1);
        pdu.setTamanho((short) 1);

        byte[] list = new byte[247];
        list[0] = 0;

        pdu.setLista(list);
    }

    public byte[] getResposta() {
        return pdu.generatePDU();
    }

}
