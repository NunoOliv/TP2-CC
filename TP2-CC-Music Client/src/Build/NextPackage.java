package Build;

public class NextPackage {

    private PDU pdu;
    private ListaCampos lc;

    public NextPackage(short piece) {
        this.lc = new ListaCampos();
        Campo c = new Campo((byte) 30);
        c.setDados(c.shortToByte(piece), (short) c.shortToByte(piece).length);
        lc.addCampo(c);

        this.pdu = new PDU((byte) 0, (byte) 0, (byte) 0, (byte) 30, lc.getNCampos(), lc.getTotalSize(), lc.generate());
    }

    public byte[] generatePDU() {
        return this.pdu.generatePDU();
    }
}
