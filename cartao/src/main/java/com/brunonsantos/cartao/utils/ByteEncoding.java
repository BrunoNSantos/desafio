package com.brunonsantos.cartao.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ByteEncoding {
	private static final String CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private static final char CODEFLAG = '9';

	private static StringBuilder out = new StringBuilder();

	private static Map<Character, Integer> CODEMAP = new HashMap<Character, Integer>();

	private static void Append(int b) {
		if (b < 61) {
			out.append(CODES.charAt(b));
		} else {

			out.append(CODEFLAG);

			out.append(CODES.charAt(b - 61));
		}
	}

	public static String base62Encode(byte[] in) {

		// Reset output StringBuilder
		out.setLength(0);

		//
		int b;

		// Loop with 3 bytes as a group
		for (int i = 0; i < in.length; i += 3) {

			// #1 char
			b = (in[i] & 0xFC) >> 2;
			Append(b);

			b = (in[i] & 0x03) << 4;
			if (i + 1 < in.length) {

				// #2 char
				b |= (in[i + 1] & 0xF0) >> 4;
				Append(b);

				b = (in[i + 1] & 0x0F) << 2;
				if (i + 2 < in.length) {

					// #3 char
					b |= (in[i + 2] & 0xC0) >> 6;
					Append(b);

					// #4 char
					b = in[i + 2] & 0x3F;
					Append(b);

				} else {
					// #3 char, last char
					Append(b);
				}
			} else {
				// #2 char, last char
				Append(b);
			}
		}

		return out.toString();
	}

	public static byte[] base62Decode(char[] inChars) {

		// Map for special code followed by CODEFLAG '9' and its code index
		CODEMAP.put('A', 61);
		CODEMAP.put('B', 62);
		CODEMAP.put('C', 63);

		ArrayList<Byte> decodedList = new ArrayList<Byte>();

		// 6 bits bytes
		int[] unit = new int[4];

		int inputLen = inChars.length;

		// char counter
		int n = 0;

		// unit counter
		int m = 0;

		// regular char
		char ch1 = 0;

		// special char
		char ch2 = 0;

		Byte b = 0;

		while (n < inputLen) {
			ch1 = inChars[n];
			if (ch1 != CODEFLAG) {
				// regular code
				unit[m] = CODES.indexOf(ch1);
				m++;
				n++;
			} else {
				n++;
				if (n < inputLen) {
					ch2 = inChars[n];
					if (ch2 != CODEFLAG) {
						// special code index 61, 62, 63
						unit[m] = CODEMAP.get(ch2);
						m++;
						n++;
					}
				}
			}

			// Add regular bytes with 3 bytes group composed from 4 units with 6
			// bits.
			if (m == 4) {
				b = new Byte((byte) ((unit[0] << 2) | (unit[1] >> 4)));
				decodedList.add(b);
				b = new Byte((byte) ((unit[1] << 4) | (unit[2] >> 2)));
				decodedList.add(b);
				b = new Byte((byte) ((unit[2] << 6) | unit[3]));
				decodedList.add(b);

				// Reset unit counter
				m = 0;
			}
		}

		// Add tail bytes group less than 4 units
		if (m != 0) {
			if (m == 1) {
				b = new Byte((byte) ((unit[0] << 2)));
				decodedList.add(b);
			} else if (m == 2) {
				b = new Byte((byte) ((unit[0] << 2) | (unit[1] >> 4)));
				decodedList.add(b);
			} else if (m == 3) {
				b = new Byte((byte) ((unit[0] << 2) | (unit[1] >> 4)));
				decodedList.add(b);
				b = new Byte((byte) ((unit[1] << 4) | (unit[2] >> 2)));
				decodedList.add(b);
			}
		}

		Byte[] decodedObj = decodedList.toArray(new Byte[decodedList.size()]);

		byte[] decoded = new byte[decodedObj.length];

		// Convert object Byte array to primitive byte array
		for (int i = 0; i < decodedObj.length; i++) {
			decoded[i] = (byte) decodedObj[i];

		}

		return decoded;
	}
	
    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param   hex         the hex string
     * @return              the hex string decoded into a byte array
     */
    public static byte[] hexDecode(final String hex)
    {
    	byte[] binary;
    	if (hex.contains(":")) {
    		final String[] hexs = hex.split(":");
    		binary = new byte[hexs.length];
    		for(int i = 0; i < hexs.length; i++) {
    			binary[i] = (byte)Integer.parseInt(hexs[i], 16);
    		}
    	} else {
    		binary = new byte[hex.length() / 2];
	        for(int i = 0; i < binary.length; i++)
	        {
	            binary[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
	        }
    	}
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param   in       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    public static String hexEncode(byte[] in)
    {
        BigInteger bi = new BigInteger(1, in);
        String hex = bi.toString(16);
        int paddingLength = (in.length * 2) - hex.length();
        if(paddingLength > 0) 
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}
