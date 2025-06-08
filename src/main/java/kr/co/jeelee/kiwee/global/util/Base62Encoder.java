package kr.co.jeelee.kiwee.global.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class Base62Encoder {
	private static final String BASE62_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final BigInteger BASE = BigInteger.valueOf(62);

	public static String encode(UUID uuid) {
		BigInteger value = new BigInteger(1, toBytes(uuid));
		StringBuilder result = new StringBuilder();

		while (value.compareTo(BigInteger.ZERO) > 0) {
			int remainder = value.mod(BASE).intValue();
			result.insert(0, BASE62_ALPHABET.charAt(remainder));
			value = value.divide(BASE);
		}

		while (result.length() < 24) {
			result.insert(0, '0');
		}

		return result.toString();
	}

	private static byte[] toBytes(UUID uuid) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}
}
