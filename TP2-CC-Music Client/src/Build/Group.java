package Build;

import Exception.MissingPieciesException;
import java.util.ArrayList;
import java.util.HashMap;

public class Group {

    private HashMap<Integer, PDU> listaPDU;
    private int nPacotes;

    public Group() {
        listaPDU = new HashMap<>();
        nPacotes = -1;
    }

    public void addPiece(byte[] dados) {

        PDU pdu = new PDU(dados);
        ListaCampos lc = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        
        
        //System.out.println("Não faz parte deste PDU!");
        Campo c = lc.getCampoByTag((byte) 17);
        int a = c.byteToInt(c.getDados());

        c = lc.getCampoByTag((byte) 254);
        if (c == null) {
            //é o último
            nPacotes = a;
        }

        c = lc.getCampoByTag((byte) 19);
        if (c == null) {// se nao tiver o campo 19
            System.out.println("Não faz parte deste PDU!");
            return;
        } else {
            listaPDU.put(a, pdu);
        }
    }

    public boolean isComplete() {
        if (nPacotes == -1) {
            return false;
        }

        if (listaPDU.size() != nPacotes) {
            return false;
        }
        return true;
    }

    public PDU generate() throws MissingPieciesException {
        if (!isComplete()) {
            throw new MissingPieciesException();
        }

        int i = 1, j = 0;
        ListaCampos lc;
        Campo c;
        byte[] data = new byte[5 * 1024 * 1024]; // 4 MB
        PDU resp;

        while (i <= nPacotes) {

            lc = new ListaCampos(listaPDU.get(i).getLista(), listaPDU.get(i).getnCampos());
            c = lc.getCampoByTag((byte) 19);

            System.arraycopy(c.getDados(), 0, data, j, c.getSize());
            j += c.getSize();
            i++;
        }

        resp = new PDU(data);
        return resp;
    }

    /**
     * Devolve uma lista com os número dos pacotes que faltam, se não se souber
     * quantos pacotes são, devolve apenas o primeiro que se sabe que falta.
     *
     * @return Lista com o número das peças que faltam.
     */
    public ArrayList<Integer> getMissingPieces() {
        ArrayList<Integer> resp = new ArrayList<>();
        int i = 0;

        if (isComplete()) {
            return null;
        }

        if (nPacotes == -1) {
            while (true) {
                if (listaPDU.get(i) == null) {
                    resp.add(i);
                    break;
                }
                i++;
            }
        } else {
            while (i < nPacotes) {
                if (listaPDU.get(i) == null) {
                    resp.add(i);
                }
                i++;
            }
        }

        return resp;
    }

}
