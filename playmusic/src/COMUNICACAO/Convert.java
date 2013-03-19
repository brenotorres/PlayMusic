package COMUNICACAO; 
import java.net.InetAddress;

public class Convert {

	
	
	/* ========================= */
	/* "primitive type --> byte[] data" Methods */
	/* ========================= */

	public static byte[] toByta(byte data) {
	    return new byte[]{data};
	}

	public static byte[] toByta(byte[] data) {
	    return data;
	}


	public static byte[] toByta(short data) {
	    return new byte[] {
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}


	public static byte[] toByta(char data) {
	    return new byte[] {
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}


	public static byte[] toByta(int data) {
	    return new byte[] {
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}


	public static byte[] toByta(long data) {
	    return new byte[] {
	        (byte)((data >> 56) & 0xff),
	        (byte)((data >> 48) & 0xff),
	        (byte)((data >> 40) & 0xff),
	        (byte)((data >> 32) & 0xff),
	        (byte)((data >> 24) & 0xff),
	        (byte)((data >> 16) & 0xff),
	        (byte)((data >> 8) & 0xff),
	        (byte)((data >> 0) & 0xff),
	    };
	}


	public static byte[] toByta(float data) {
	    return toByta(Float.floatToRawIntBits(data));
	}


	public static byte[] toByta(double data) {
	    return toByta(Double.doubleToRawLongBits(data));
	}


	public static byte[] toByta(boolean data) {
	    return new byte[]{(byte)(data ? 0x01 : 0x00)}; // bool -> {1 byte}
	}


	public static byte[] toByta(String data) {
	    int aux = 0;
		byte[] retorno = new byte[data.length()*2];
		for (int i = 0; i < data.length(); i++){
	    	byte[] charByte = toByta(data.charAt(i));
	    	for (int j = 0; j < charByte.length; j++){
	    		retorno[aux] = charByte[j];
	    		aux++;
	    	}
	    }
		return retorno;
	}

	

	/* ========================= */
	/* "byte[] data --> primitive type" Methods */
	/* ========================= */

	public static byte toByte(byte[] data) {
	    return (data == null || data.length == 0) ? 0x0 : data[0];
	}

	public static byte[] toByteA(byte[] data) {
	    return data;
	}


	public static short toShort(byte[] data) {
	    if (data == null || data.length != 2) return 0x0;
	    // ----------
	    return (short)(
	            (0xff & data[0]) << 8   |
	            (0xff & data[1]) << 0
	            );
	}


	public static char toChar(byte[] data) {
	    if (data == null || data.length != 2) return 0x0;
	    // ----------
	    return (char)(
	            (0xff & data[0]) << 8   |
	            (0xff & data[1]) << 0
	            );
	}


	public static int toInt(byte[] data) {
	    if (data == null || data.length != 4) return 0x0;
	    // ----------
	    return (int)( // NOTE: type cast not necessary for int
	            (0xff & data[0]) << 24  |
	            (0xff & data[1]) << 16  |
	            (0xff & data[2]) << 8   |
	            (0xff & data[3]) << 0
	            );
	}


	public static long toLong(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ----------
	    return (long)(
	            // (Below) convert to longs before shift because digits
	            //         are lost with ints beyond the 32-bit limit
	            (long)(0xff & data[0]) << 56  |
	            (long)(0xff & data[1]) << 48  |
	            (long)(0xff & data[2]) << 40  |
	            (long)(0xff & data[3]) << 32  |
	            (long)(0xff & data[4]) << 24  |
	            (long)(0xff & data[5]) << 16  |
	            (long)(0xff & data[6]) << 8   |
	            (long)(0xff & data[7]) << 0
	            );
	}


	public static float toFloat(byte[] data) {
	    if (data == null || data.length != 4) return 0x0;
	    // ---------- simple:
	    return Float.intBitsToFloat(toInt(data));
	}


	public static double toDouble(byte[] data) {
	    if (data == null || data.length != 8) return 0x0;
	    // ---------- simple:
	    return Double.longBitsToDouble(toLong(data));
	}


	public static boolean toBoolean(byte[] data) {
	    return (data == null || data.length == 0) ? false : data[0] != 0x00;
	}


	public static String toString(byte[] data) {
		String retorno = "";
		for (int i = 0; i < data.length; i = i+2){
			byte[] teste = new byte[2];
			for (int j = 0; j < 2; j++){
				teste[j] = data[j+i];
			}
			char c = toChar(teste);
			retorno += c;
		}
		return retorno;
	}
	
	public static int inetAddressToInt(InetAddress inetAddress) {
	    byte[] addrBytes;
	    int addr;
	    addrBytes = inetAddress.getAddress();
	    addr = ((addrBytes[0] & 0xff) << 24)
	            | ((addrBytes[1] & 0xff) << 16)
	            | ((addrBytes[2] & 0xff) << 8)
	            |  (addrBytes[3] & 0xff);
	    return addr;
	}
}
