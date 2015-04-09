
package CalculadoresResposta;


public class Register extends PDU{
    
    private String nome;
    private String alcunha;
    private String pass;
    private PDU pdu;
    
    public Register(byte[] pdu) {
        super(pdu);
    }
    
    

    public byte[] getResposta() {
        return null;
    }
    
    
    
    
}
