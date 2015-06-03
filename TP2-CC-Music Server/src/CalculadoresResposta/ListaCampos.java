package CalculadoresResposta;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class ListaCampos {

    private ArrayList<Campo> lista;
    //private short totalSize;

    public ListaCampos(ArrayList<Campo> lista) {
        this.lista = lista;
    }

    public ListaCampos() {
        this.lista = new ArrayList<>();
    }

    public ListaCampos(byte[] data, short nCampos) {
        lista = new ArrayList<>();
        int i = 0, j = 0;

        byte tag;
        int size;
        byte[] dados;
        byte[] aux = new byte[4];

        while (j < nCampos) {
            tag = data[i];

            System.arraycopy(data, i + 1, aux, 0, 4);
            size = byteToInt(aux);

            if (size > 0) {
                dados = new byte[size];
                System.arraycopy(data, i + 5, dados, 0, size);

                addCampo(new Campo(tag, size, dados));
            } else {
                addCampo(new Campo(tag));
            }

            i += size + 5;
            j++;
        }
    }

    public ListaCampos(ListaCampos lc) {
        this.lista = lc.getCampos();
    }

    public ArrayList<Campo> getCampos() {
        return (ArrayList<Campo>) lista.clone();
    }

    public Campo getCampo(int posicao) throws ArrayIndexOutOfBoundsException {
        if (lista.size() < posicao) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return lista.get(posicao).clone();
    }

    public Campo getCampoByTag(byte tag) throws ArrayIndexOutOfBoundsException {
        for (Campo c : lista) {
            if (c.getTag() == tag) {
                return c;
            }
        }
        return null;
    }

    public boolean addCampo(Campo campo) {
        //totalSize += campo.getTotalSize();
        return this.lista.add(campo);
    }

    public int getTotalSize() {
        int t = 0;
        for (Campo c : lista) {
            t += c.getTotalSize();
        }
        return t;
    }

    public byte getNCampos() {
        return (byte) lista.size();
    }

    public byte[] generate() {
        byte[] ret = new byte[getTotalSize()];
        int currentSize = 0;

        for (Campo campo : lista) {
            System.arraycopy(campo.generate(), 0, ret, currentSize, campo.getTotalSize());
            currentSize = currentSize + campo.getTotalSize();
        }
        return ret;
    }

    public byte[] IntToByte(int size) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(size).array();
        return bytes;
    }

    public int byteToInt(byte[] data) {
        byte[] sizeBytes = {data[0], data[1], data[2], data[3]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    @Override
    public String toString() {
        return "ListaCampos{" + "lista=" + lista + '}';
    }

    @Override
    protected ListaCampos clone() {
        return new ListaCampos(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ListaCampos) {
            ListaCampos lc = (ListaCampos) o;

            if (lc.getNCampos() != this.getNCampos()) {
                return false;
            }

            if (lc.getTotalSize() != this.getTotalSize()) {
                return false;
            }

            for (int i = 0; i < lc.getNCampos(); i++) {
                try {
                    if (!lc.getCampo(i).equals(this.lista.get(i))) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

}
