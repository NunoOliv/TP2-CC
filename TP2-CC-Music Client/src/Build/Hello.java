package Build;

public class Hello {
    //teste para ver controlo de coisos no coiso

    byte[] pdu;

    public Hello() {
        pdu = new byte[255];
    }

    public byte[] generate() {
        pdu[0] = 0;
        pdu[1] = 0;
        pdu[2] = 0;
        pdu[3] = 0;
        pdu[4] = 1;
        pdu[5] = 0;
        pdu[6] = 0;
        pdu[7] = 0;
        
        return pdu;
    }

}
