package Build;

public class Transmit {

    private PDU pdu;
    private ListaCampos lc;

    private short label;
    private short pergunta;
    private String nome;
    private short piece;

    public Transmit(short label, short pergunta, String nomeDesafio) {
        this.pdu = new PDU();
        this.lc = new ListaCampos();

        this.label = label;
        this.pergunta = pergunta;
        this.nome = nomeDesafio;
        this.piece = piece;

        inicia();
    }

    private void inicia() {
        pdu.setVersao((byte) 0);
        pdu.setSeguranca((byte) 0);
        pdu.setLabel(label);
        pdu.setTipo((byte) 12);

        Campo c = new Campo((byte) 7);//nome do desafio
        c.setDados(nome.getBytes(), (short) nome.getBytes().length);
        lc.addCampo(c);

        c = new Campo((byte) 10); //numero da pergunta a enviar
        c.setDados(c.shortToByte(pergunta), (short) c.shortToByte(pergunta).length);

        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());
    }

    public byte[] generate() {
        return pdu.generatePDU();
    }
}
