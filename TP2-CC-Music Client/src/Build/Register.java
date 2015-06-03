package Build;

public class Register {

    private PDU pdu;
    private ListaCampos lc;
    private short label;

    public Register(String alcunha, String pass, String nome, short label) {
        pdu = new PDU();
        lc = new ListaCampos();
        this.label = label;

        Campo c = new Campo((byte) 1);
        c.setDados(nome.getBytes());
        lc.addCampo(c);
        
        c = new Campo((byte) 2);
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
        pdu.setTipo((byte) 2);
        pdu.setnCampos(lc.getNCampos());
        pdu.setTamanho(lc.getTotalSize());
        pdu.setLista(lc.generate());

        //System.out.println(lc.toString());
        return pdu.generatePDU();
    }
}
