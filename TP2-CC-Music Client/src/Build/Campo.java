package Build;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Campo {

    private byte tag;
    private int size;
    private byte[] dados;

    public Campo() {
        this.tag = -1;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo) {
        this.tag = campo;
        this.size = 0;
        this.dados = null;
    }

    public Campo(byte campo, int size, String dados) {
        this.tag = campo;
        this.size = size;
        this.dados = new byte[size];
        System.arraycopy(dados.getBytes(), 0, this.dados, 0, size);
    }

    public Campo(byte campo, int size, byte[] dados) {
        this.tag = campo;
        this.size = size;
        this.dados = new byte[size];
        System.arraycopy(dados, 0, this.dados, 0, size);
    }

    public Campo(byte[] data) {
        this.tag = data[0];
        byte[] aux = new byte[4];
        System.arraycopy(data, 1, aux, 0, 4);
        this.size = byteToInt(aux);
        this.dados = new byte[size];
        System.arraycopy(data, 5, this.dados, 0, size);
    }

    public Campo(Campo c) {
        this.tag = c.getTag();
        this.size = c.getSize();
        this.dados = c.getDados();
    }

    public byte getTag() {
        return tag;
    }

    public void setTag(byte campo) {
        this.tag = campo;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getDados() {
        if (this.dados == null) {
            return null;
        }
        byte[] resp = new byte[this.size];
        System.arraycopy(this.dados, 0, resp, 0, this.size);
        return resp;
    }

    public void setDados(byte[] dados) {
        setSize(dados.length);
        this.dados = new byte[size];
        System.arraycopy(dados, 0, this.dados, 0, size);

    }

    public int getTotalSize() {
        return (this.size + 5);
    }

    public byte[] generate() {
        byte[] resp = new byte[this.getTotalSize()];
        resp[0] = this.tag;
        byte[] buff = IntToByte(this.size);
        resp[1] = buff[0];
        resp[2] = buff[1];
        resp[3] = buff[2];
        resp[4] = buff[3];
        if (this.dados != null) {
            System.arraycopy(this.dados, 0, resp, 5, this.size);
        }
        return resp;
    }

    public byte[] IntToByte(int size) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(size).array();
        return bytes;
    }

    public int byteToInt(byte[] data) {
        byte[] sizeBytes = {data[0], data[1], data[2], data[3]};
        return ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    public String toString() {
        return "Campo{" + "campo=" + tag + ", size=" + size + ", dados=" + dados + '}';
    }

    public Campo clone() {
        return new Campo(this);
    }

    public boolean equals(Object o) {
        if (o instanceof Campo) {
            Campo c = (Campo) o;
            if (this.tag != c.getTag()) {
                return false;
            }
            if (this.size != c.getSize()) {
                return false;
            }
            if (this.dados.equals(c.getDados())) {
                return true;
            }
        }
        return false;
    }

}
