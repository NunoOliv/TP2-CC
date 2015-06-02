package Build;

public class AcceptChallenge {

    private short label;
    private String nome;
    private PDU pdu;
    private ListaCampos lc;

    public AcceptChallenge(String nome, short label) {
        this.label = label;
        this.pdu = new PDU();
        this.lc = new ListaCampos();
        this.nome = nome;
    }

    public byte[] generate() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(this.label);
        pdu.setTipo((byte) 9);

        Campo c = new Campo((byte) 7);
        c.setDados(nome.getBytes(), (short) nome.getBytes().length);
        lc.addCampo(c);

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());

        return pdu.generatePDU();
    }

}
