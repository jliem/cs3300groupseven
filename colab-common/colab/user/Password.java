package colab.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public final class Password implements Serializable {

	public static final long serialVersionUID = 1L;
	
	private final byte[] hash;
	
	private static MessageDigest digest;
	
	static {
		try {
			digest = java.security.MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public Password(final String pass) {
		this.hash = doHash(pass);
	}

	public Password(final byte[] hash) {
		this.hash = hash;
	}

	public boolean checkPassword(final String pass) {
		return Arrays.equals(doHash(pass), hash);
	}

	private static byte[] doHash(final String str) {
		byte[] result;
		digest.update(str.getBytes());
		result = digest.digest();
		digest.reset();
		return result;
	}
	
}
