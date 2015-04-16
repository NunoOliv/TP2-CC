package Build;


public class Logout {
        byte[] pdu;

    public Logout() {
        pdu = new byte[255];
    }

    public byte[] generate() {
        pdu[0] = 0;
        pdu[1] = 0;
        pdu[2] = 0;
        pdu[3] = 0;
        pdu[4] = 4;
        pdu[5] = 0;
        pdu[6] = 0;
        pdu[7] = 0;
        
        return pdu;
    }
}
