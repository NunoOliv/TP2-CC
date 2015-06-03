package Build;

public class Hello {

    private PDU pdu;

    public Hello() {
        pdu = new PDU();
    }

    public byte[] generate() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel((short) 0);
        pdu.setTipo((byte) 1);
        pdu.setnCampos((byte) 0);
        pdu.setTamanho(0);
        pdu.setLista(null);

        return pdu.generatePDU();
    }

}
