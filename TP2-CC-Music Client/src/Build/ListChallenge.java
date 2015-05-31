package Build;

public class ListChallenge {

    private final PDU pdu;
    private short label;

    public ListChallenge(short label) {
        this.pdu = new PDU();
        this.label = label;
    }

    public byte[] generate() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(this.label);
        pdu.setTipo((byte) 7);
        pdu.setnCampos((byte) 0);
        pdu.setTamanho((short) 0);
        pdu.setLista(null);

        return pdu.generatePDU();
    }

}
