package Build;

public class NextPackage {

    private PDU pdu;
    private ListaCampos lc;

    public NextPackage(int piece) {
        this.lc = new ListaCampos();
        Campo c = new Campo((byte) 30);
        c.setDados(c.IntToByte(piece));
        lc.addCampo(c);

        this.pdu = new PDU((byte) 0, (byte) 0, (byte) 0, (byte) 30, lc.getNCampos(), lc.getTotalSize(), lc.generate());
    }

    public byte[] generatePDU() {
        return this.pdu.generatePDU();
    }
}
