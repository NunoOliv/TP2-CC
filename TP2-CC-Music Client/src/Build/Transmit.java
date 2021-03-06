package Build;

public class Transmit {

    private PDU pdu;
    private ListaCampos lc;

    private short label;
    private int pergunta;
    private String nome;

    public Transmit(short label, int pergunta, String nomeDesafio) {
        this.pdu = new PDU();
        this.lc = new ListaCampos();

        this.label = label;
        this.pergunta = pergunta;
        this.nome = nomeDesafio;

        inicia();
    }

    private void inicia() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(label);
        pdu.setTipo((byte) 12);

        Campo c = new Campo((byte) 7);//nome do desafio
        c.setDados(nome.getBytes());
        lc.addCampo(c);

        c = new Campo((byte) 10); //numero da pergunta a enviar
        c.setDados(c.IntToByte(pergunta));
        lc.addCampo(c);

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
    }

    public byte[] generate() {
        return pdu.generatePDU();
    }
}
