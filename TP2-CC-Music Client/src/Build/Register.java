package Build;

public class Register {

    private PDU pdu;
    private ListaCampos lc;
    private short label;

    public Register(String alcunha, String pass, String nome, short label) {
        pdu = new PDU();
        lc = new ListaCampos();
        this.label = label;
        
        /*System.out.println("nome.length: "+nome.length());
        System.out.println("alcunha.length: "+alcunha.length());
        System.out.println("pass.length: "+pass.length());*/
        
        lc.addCampo(new Campo((byte) 1, (short) nome.length(), nome));
        lc.addCampo(new Campo((byte) 2, (short) alcunha.length(), alcunha));
        lc.addCampo(new Campo((byte) 3, (short) pass.length(), pass));
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
