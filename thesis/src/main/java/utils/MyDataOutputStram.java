package utils;

import java.io.*;

public class MyDataOutputStram extends FilterOutputStream implements DataOutput {

    protected int written;
    private byte[] bytearr = null;
    private byte[] writeBuffer = new byte[8];

    public int getWritten() {
        return written;
    }

    public void setWritten(int written) {
        this.written = written;
    }

    public byte[] getBytearr() {
        return bytearr;
    }

    public void setBytearr(byte[] bytearr) {
        this.bytearr = bytearr;
    }

    public byte[] getWriteBuffer() {
        return writeBuffer;
    }

    public void setWriteBuffer(byte[] writeBuffer) {
        this.writeBuffer = writeBuffer;
    }

    public MyDataOutputStram(OutputStream out) {
        super(out);
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {

    }

    @Override
    public void writeByte(int i) throws IOException {

    }

    @Override
    public void writeShort(int i) throws IOException {

    }

    @Override
    public void writeChar(int i) throws IOException {

    }

    @Override
    public void writeInt(int i) throws IOException {

    }

    @Override
    public void writeLong(long l) throws IOException {

    }

    @Override
    public void writeFloat(float v) throws IOException {

    }

    @Override
    public void writeDouble(double v) throws IOException {

    }

    @Override
    public void writeBytes(String s) throws IOException {

    }

    @Override
    public void writeChars(String s) throws IOException {

    }

    @Override
    public final void writeUTF(String str) throws IOException {
        writeUTF(str, this);
    }

    private int writeUTF(String str, MyDataOutputStram myDataOutputStram) throws IOException {
        int strlen = str.length();
        int utflen = 0;
        int count = 0;

        char c;
        for(int i = 0; i < strlen; ++i) {
            c = str.charAt(i);
            if (c >= 1 && c <= 127) {
                ++utflen;
            } else if (c > 2047) {
                utflen += 3;
            } else {
                utflen += 2;
            }
        }


        //byte[] bytearr = null;
        byte[] bytearr;
        if (out instanceof MyDataOutputStram) {
            MyDataOutputStram dos = (MyDataOutputStram)out;
            if (dos.bytearr == null || dos.bytearr.length < utflen + 2) {
                dos.bytearr = new byte[utflen * 2 + 2];
            }

            bytearr = dos.bytearr;
        } else {
            bytearr = new byte[utflen + 2];
        }

        int var8 = count + 1;
        bytearr[count] = (byte)(utflen >>> 8 & 255);
        bytearr[var8++] = (byte)(utflen >>> 0 & 255);
        //int i = false;

        int i;
        for(i = 0; i < strlen; ++i) {
            c = str.charAt(i);
            if (c < 1 || c > 127) {
                break;
            }

            bytearr[var8++] = (byte)c;
        }

        for(; i < strlen; ++i) {
            c = str.charAt(i);
            if (c >= 1 && c <= 127) {
                bytearr[var8++] = (byte)c;
            } else if (c > 2047) {
                bytearr[var8++] = (byte)(224 | c >> 12 & 15);
                bytearr[var8++] = (byte)(128 | c >> 6 & 63);
                bytearr[var8++] = (byte)(128 | c >> 0 & 63);
            } else {
                bytearr[var8++] = (byte)(192 | c >> 6 & 31);
                bytearr[var8++] = (byte)(128 | c >> 0 & 63);
            }
        }

        out.write(bytearr, 0, utflen + 2);
        return utflen + 2;

    }
}
