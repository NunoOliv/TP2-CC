package Build;

import Exception.MissingPieciesException;
import java.util.ArrayList;

public class Group {

    private ArrayList<PDU> listaPDU;
    private int nPacotes;

    public Group() {
        listaPDU = new ArrayList<>();
        nPacotes = -1;
    }

    private void addPiece(byte[] dados) {
        PDU pdu = new PDU(dados);
        ListaCampos lc = new ListaCampos(pdu.getLista(), pdu.getnCampos());

        Campo c = lc.getCampo(0);
        if (c.getTag() != 17) { //se nao tiver o campo 17
            System.out.println("Não faz parte deste PDU!");
            return;
        }
        short a = c.byteToShort(c.getDados());

        c = lc.getCampo(1);
        if (c.getTag() != 254) {
            nPacotes = a + 1;
        } else {
            c = lc.getCampo(2);
        }
        if (c.getTag() != 19) {// se nao tiver o campo 19
            System.out.println("Não faz parte deste PDU!");
            return;
        } else {
            listaPDU.add(a, pdu);
        }
    }

    public boolean isComplete() {
        if (nPacotes == -1) {
            return false;
        }

        int i = 0;
        while (i < nPacotes) {
            if (listaPDU.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    public PDU generate() throws MissingPieciesException {
        if (!isComplete()) {
            throw new MissingPieciesException();
        }

        int i = 0;
        ListaCampos lc;
        Campo c;
        byte[] data = null;
        PDU resp;

        while (i < nPacotes) {
            lc = new ListaCampos(listaPDU.get(i).getLista(), listaPDU.get(i).getnCampos());

            if (i == nPacotes - 1) {//se for o último
                c = lc.getCampo(1);
            } else { // se não for o último
                c = lc.getCampo(2);
            }

            System.arraycopy(c.getDados(), 0, data, i * c.getSize(), c.getSize());
        }

        resp = new PDU(data);
        return resp;
    }
    
    /**
     * Devolve uma lista com os número dos pacotes que faltam, se não se souber
     * quantos pacotes são, devolve apenas o primeiro que se sabe que falta.
     * @return 
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
