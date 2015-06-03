package Build;

public class Login {

    private PDU pdu;
    private ListaCampos lc;
    private short label;

    public Login(String alcunha, String pass, short label) {
        this.pdu = new PDU();
        this.lc = new ListaCampos();
        this.label = label;

        Campo c = new Campo((byte) 2);
        c.setDados(alcunha.getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 3);
        c.setDados(pass.getBytes());
        lc.addCampo(c);
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
