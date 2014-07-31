package yuanye.hadoop.core;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.Checksum;

/**
 * Created by Kali on 2014/6/28.
 */
public class ChecksumFileSystem {

    public boolean mkdir(Path path) throws IOException {
        File parent = path.getParent().toFile();
        if (!parent.exists()) {
            if (!parent.mkdirs()) {
                return false;
            }
        }
        File file = path.toFile();
        if (file.isDirectory()) {
            return file.mkdir();
        }
        return file.createNewFile();
    }

    public boolean copy(Path src, Path dst) {
        File from = src.toFile();
        if (!from.exists()) {
            return false;
        }

        if (!dst.getParent().toFile().exists()) {
            dst.getParent().toFile().mkdirs();
        }

        if (from.isFile()) {
            src.toFile().renameTo(dst.toFile());
        } else {
            for (File f : dst.toFile().listFiles()) {
                //TODO
                //copy()
            }
        }
        return false;
    }

    public boolean rename(Path src, Path dst) {
        //TODO
        return false;
    }

    public InputStream open(Path file) {
        //TODO
        return null;
    }

    public OutputStream create(Path file) {
        //TODO
        return null;
    }

    public OutputStream append(Path file) {
        //TODO
        return null;
    }

    public static File getChecksumFile(Path file) throws IOException {
        File sumFile = new File(file.toString() + ".crc");
        if (!sumFile.exists()) {
            throw new IOException(new FileNotFoundException(sumFile.toString()));
        }
        return sumFile;
    }

    public boolean isChechsumFile(Path file) {
        //TODO
        return false;
    }

    public static class ChecksumInputChecker extends InputStream {

        private Path file;
        private long fileLen;
        private byte[] buf;
        private byte[] sum;
        private FileInputStream datas;
        private FileInputStream sums;
        private Checksum checksum;
        private int pos = 0;            //position of next byte to read in buf
        private int count = 0;          //available bytes in buff
        private long chunkpos = 0;      //position of next chunk to read in file
        private int bytesCerChunksum;
        private long filepos = 0;        //position in file
        private long sumpos = 0;

        public ChecksumInputChecker(Path file, Checksum checksum) throws IOException {
            if (!file.toFile().isFile()) {
                throw new IOException("path:" + file.toAbsolutePath() + " is not a file");
            }
            File df = file.toFile();
            fileLen = df.length();
            File sf = ChecksumFileSystem.getChecksumFile(file);
            datas = new FileInputStream(df);
            sums = new FileInputStream(sf);
            buf = new byte[512];
            sum = new byte[4];
            this.checksum = checksum;
            bytesCerChunksum = 512;
        }


        @Override
        public int read() throws IOException {
            if (pos >= count) {
                fill();
                if (pos >= count) {
                    return -1;
                }
            }
            return buf[pos++] & 0x0F;
        }


        /**
         * Read at most <code>len</code> data into <code>b</code> from <code>off</code>.
         *
         * @param b   byte buffer to read into
         * @param off offset to read into to buffer
         * @param len length to read
         * @return count of bytes actually read.
         */
        public int read(byte[] b, int off, int len) throws IOException{
            if ((off | len | (len - b.length)) < 0) {
                throw new IllegalArgumentException();
            }
            int read = 0;
            try {
                for (; read < len && read >= 0; read += read1(b, off + read, len - read)) {
                }
            } catch (ChecksumException e) {
                throw new IOException(e);
            }
            return read;
        }

        ;

        private int read1(byte[] b, int off, int len) throws ChecksumException, IOException {
            int aval = count - pos;
            if (aval <= 0) {
                //read data out directly
                if (len > buf.length) {
                    pos = count = 0;
                    int read = readChecksum(b, off, len);
                    return read;
                } else {
                    fill();
                    if (pos >= count) {
                        //EOF
                        return -1;
                    }
                    aval = count;
                }
            }
            int read = Math.min(aval, len);
            System.arraycopy(buf, pos, b, off, read);
            pos += read;
            return read;

        }

        //read one chunk to local buffer
        private void fill() {
            //TODO
        }

        /**
         * Read up to one chunk in to <code>b</code> at position <code>off</code>.
         *
         * @param pos      position in file
         * @param b        byte buffer to read into
         * @param off      offset to read into to buffer
         * @param len      length to read
         * @param checksum checksum buffer
         * @return
         */
        private int readChunk(long pos, byte[] b, int off, int len, byte[] checksum) throws IOException {
            long dpos = getChunkpos(pos);
            long spos = getChecksumpos(pos);

            try{
                long skip = spos - sumpos;
                if (skip != 0){
                    sums.skip(skip);
                };
                sums.read(b,0,b.length);
            } catch (IOException e) {
                throw e;
            }

            int read;
            try {
                long skip = dpos - filepos;
                if (skip!=0){
                    datas.skip(skip);
                }
                read = datas.read(b,off,len);
            } catch (IOException e) {
                throw e;
            }
            if ( read!= -1 ){
                filepos += read;
                sumpos += sum.length;
            }
            return read;
        }

        private int readChecksum(byte[] b, int off, int len) throws ChecksumException, IOException {
            int read = readChunk(chunkpos,b,off,len,sum);
            int act = Math.max(read,len);
            checksum.update(b,off,act);
            verifyChecksum(sum);
            chunkpos += bytesCerChunksum;
            pos = 0;
            count = act;
            return act;
        }

        private void verifyChecksum(byte[] checksum) throws ChecksumException {
        }

        private long checksum2long(byte[] checksum) {
            long result = 0;
            for (int i = 0; i < checksum.length; i++) {
                result |= (checksum[i] << (checksum.length - i - 1) * 8);
            }
            return result;
        }
        private long getChunkpos(long pos){
            return (pos/bytesCerChunksum)*bytesCerChunksum;
        }

        private long getChecksumpos(long pos){
            return (pos/bytesCerChunksum)*4;
        }

    public static class ChecksumOutputSummer extends OutputStream {

        @Override
        public void write(int b) throws IOException {

        }
    }
    }
}
