

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public abstract class CharacterEncoder {
	protected PrintStream pStream;

	abstract int bytesPerAtom();

	abstract int bytesPerLine();

	void encodeBufferPrefix(OutputStream paramOutputStream) throws IOException {
		this.pStream = new PrintStream(paramOutputStream);
	}

	void encodeBufferSuffix(OutputStream paramOutputStream) throws IOException {
	}

	void encodeLinePrefix(OutputStream paramOutputStream, int paramInt)
			throws IOException {
	}

	void encodeLineSuffix(OutputStream paramOutputStream) throws IOException {
		this.pStream.println();
	}

	abstract void encodeAtom(OutputStream paramOutputStream,
			byte[] paramArrayOfByte, int paramInt1, int paramInt2)
			throws IOException;

	protected int readFully(InputStream paramInputStream,
			byte[] paramArrayOfByte) throws IOException {
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			int j = paramInputStream.read();
			if (j == -1)
				return i;
			paramArrayOfByte[i] = (byte) j;
		}
		return paramArrayOfByte.length;
	}

	public void encodeBuffer(InputStream paramInputStream,
			OutputStream paramOutputStream) throws IOException {
		byte[] arrayOfByte = new byte[bytesPerLine()];

		encodeBufferPrefix(paramOutputStream);
		while (true) {
			int j = readFully(paramInputStream, arrayOfByte);
			if (j == -1) {
				break;
			}
			encodeLinePrefix(paramOutputStream, j);
			for (int i = 0; i < j; i += bytesPerAtom()) {
				if (i + bytesPerAtom() <= j)
					encodeAtom(paramOutputStream, arrayOfByte, i,
							bytesPerAtom());
				else {
					encodeAtom(paramOutputStream, arrayOfByte, i, j - i);
				}
			}
			encodeLineSuffix(paramOutputStream);
			if (j < bytesPerLine()) {
				break;
			}
		}
		encodeBufferSuffix(paramOutputStream);
	}

	public void encodeBuffer(byte[] paramArrayOfByte,
			OutputStream paramOutputStream) throws IOException {
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
				paramArrayOfByte);
		encodeBuffer(localByteArrayInputStream, paramOutputStream);
	}

	public String encodeBuffer(byte[] paramArrayOfByte) {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
				paramArrayOfByte);
		try {
			encodeBuffer(localByteArrayInputStream, localByteArrayOutputStream);
		} catch (Exception localException) {
			throw new Error("ChracterEncoder::encodeBuffer internal error");
		}
		String str;
		try {
			str = localByteArrayOutputStream.toString("UTF8");
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			throw new Error("CharacterEncoder::encodeBuffer internal error(2)");
		}
		return str;
	}
}