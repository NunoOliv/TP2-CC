package Build;

public class Logout {

    private PDU pdu;
    private short label;

    public Logout(short label) {
        pdu = new PDU();
        this.label = label;
    }

    public byte[] generate() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(label);
        pdu.setTipo((byte) 4);
        pdu.setnCampos((byte) 0);
        pdu.setTamanho((short) 0);
        pdu.setLista(null);

        return pdu.generatePDU();
    }
}
