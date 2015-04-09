package CalculadoresResposta;

public class Hello extends PDU {

    public Hello(byte[] pdu) {
        super(pdu);
    }
    
    public Hello(){
        super();
        super.setnCampos((byte)1);
        super.setTamanho((short)1);
        super.setLista();
    }

    
}
