package com.brunonsantos.cartao.utils;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import lombok.Getter;

/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */
public class PasswordHash {

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTE_SIZE = 32;
	public static final int PBKDF2_ITERATIONS = 64000;

	public static final int ITERATION_INDEX = 0;
	public static final int SALT_INDEX = 1;
	public static final int HASH_INDEX = 2;

	enum Encoding {
		BASE64, HEX;
	}
	
	enum Cypher {
		PBKDF2_SHA1("PBKDF2WithHmacSHA1", "sha1", 20),
		PBKDF2_SHA256("PBKDF2WithHmacSHA256", "sha256", 32),
		PBKDF2_SHA512("PBKDF2WithHmacSHA512", "sha512", 64);

		@Getter
		private final String cipher;
		@Getter
		private final String algorithm;
		@Getter
		private final int keyLength;

		Cypher(final String cipher, final String algorithm, final int keyLength) {
			this.cipher = cipher;
			this.algorithm = algorithm;
			this.keyLength = keyLength;
		}
	}

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 *
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return createHash(password.toCharArray(), Cypher.PBKDF2_SHA512, Encoding.BASE64, true, true, '$');
	}

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 *
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createMosquittoHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return createHash(password.toCharArray(), Cypher.PBKDF2_SHA512, Encoding.BASE64, true, true, '$');
	}

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 *
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	private static String createHash(final char[] password, final Cypher cypher, final Encoding encoding,
			final boolean rawSalt, final boolean includeAlgorithmInHash, final char separator)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a random salt
		final SecureRandom random = new SecureRandom();
		final byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);

		final String encodedSalt;
		switch (encoding) {
		case BASE64:
			encodedSalt = Base64.getEncoder().encodeToString(salt);
			break;
		default:
			encodedSalt = ByteEncoding.hexEncode(salt);
			break;
		}

		final StringBuilder finalHash = new StringBuilder();
		if (includeAlgorithmInHash) {
			// format PBKDF2:shaX:iterations:salt:hash
			finalHash.append("PBKDF2");
			finalHash.append(separator);
			finalHash.append(cypher.getAlgorithm());
			finalHash.append(separator);
		}

		final byte[] hash;
		// Hash the password
		if (rawSalt) {
			hash = pbkdf2(password, salt, cypher.getCipher(), PBKDF2_ITERATIONS, cypher.getKeyLength());
		} else {
			hash = pbkdf2(password, encodedSalt.getBytes(Charset.forName("UTF-8")), cypher.getCipher(), PBKDF2_ITERATIONS, cypher.getKeyLength());
		}

		finalHash.append(PBKDF2_ITERATIONS);
		finalHash.append(separator);
		finalHash.append(encodedSalt);
		finalHash.append(separator);
		switch (encoding) {
		case BASE64:
			finalHash.append(Base64.getEncoder().encodeToString(hash));
			break;
		default:
			finalHash.append(ByteEncoding.hexEncode(hash));
			break;
		}

		return finalHash.toString();
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(String password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		return validatePassword(password.toCharArray(), correctHash);
	}

	/**
	 * Validates a password using a hash.
	 *
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	private static boolean validatePassword(char[] password, String correctHash)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Decode the hash into its parameters
		int paramsOffset = 0;
		final String[] params;
		final int iterations;
		final byte[] salt;
		final byte[] hash;
		if (correctHash.contains(":")) {
			params = correctHash.trim().split(":");
		} else {
			params = correctHash.trim().split("\\$");
		}

		Cypher cypher = Cypher.PBKDF2_SHA1;
		if (params.length == 5) {
			paramsOffset = 2;
			String hashAlgorithm = params[1];
			if (Cypher.PBKDF2_SHA1.getAlgorithm().toLowerCase().equals(hashAlgorithm.toLowerCase())) {
				salt = params[SALT_INDEX + paramsOffset].getBytes(Charset.forName("UTF-8"));
			} else if (Cypher.PBKDF2_SHA256.getAlgorithm().toLowerCase().equals(hashAlgorithm.toLowerCase())) {
				cypher = Cypher.PBKDF2_SHA256;
				salt = params[SALT_INDEX + paramsOffset].getBytes(Charset.forName("UTF-8"));
			} else {
				cypher = Cypher.PBKDF2_SHA512;
				salt = Base64.getDecoder().decode(params[SALT_INDEX + paramsOffset]);
			}
			hash = Base64.getDecoder().decode(params[HASH_INDEX + paramsOffset]);
		} else {
			salt = ByteEncoding.hexDecode(params[SALT_INDEX + paramsOffset]);
			hash = ByteEncoding.hexDecode(params[HASH_INDEX + paramsOffset]);
		}
		iterations = Integer.parseInt(params[ITERATION_INDEX + paramsOffset]);

		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		byte[] testHash = pbkdf2(password, salt, cypher.getCipher(), iterations, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method is
	 * used so that password hashes cannot be extracted from an on-line system using
	 * a timing attack and then attacked off-line.
	 * 
	 * @param a
	 *            the first byte array
	 * @param b
	 *            the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++)
			diff |= a[i] ^ b[i];
		return diff == 0;
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 *
	 * @param password
	 *            the password to hash.
	 * @param salt
	 *            the salt
	 * @param iterations
	 *            the iteration count (slowness factor)
	 * @param bytes
	 *            the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */
	private static byte[] pbkdf2(final char[] password, final byte[] salt, final String algorithm, final int iterations,
			final int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
		return skf.generateSecret(spec).getEncoded();
	}

}
