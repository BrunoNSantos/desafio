package com.brunonsantos.cartao.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;

public class AuthUtils {

	private static final int TOKEN_BYTES_SIZE = 32;

	public static final HashMap<RenderingHints.Key, Object> RenderingProperties = new HashMap<>();

	static {
		RenderingProperties.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		RenderingProperties.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		RenderingProperties.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	private AuthUtils() {
	}

	public static String createRandomToken() {
		// Generate a random token
		SecureRandom random = new SecureRandom();
		byte[] tokenBytes = new byte[TOKEN_BYTES_SIZE];
		random.nextBytes(tokenBytes);

		return ByteEncoding.base62Encode(tokenBytes);
	}

	public static String encodeToken(String token) {
		String result = null;
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
			result = ByteEncoding.hexEncode(hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String encodePassword(String password) {
		try {
			return PasswordHash.createHash(password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isPasswordValid(String encodedPassword, String rawPassword) {
		if (encodedPassword == null) {
			return false;
		}
		try {
			return PasswordHash.validatePassword(rawPassword, encodedPassword);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String generateRandomPassword() {
		return generateRandomPassword(8);
	}

	public static String generateRandomPassword(int size) {
		// create a password generator
		PasswordGenerator generator = new PasswordGenerator();

		// create character rules to generate passwords with
		List<CharacterRule> rules = new ArrayList<CharacterRule>();
		rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
		rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
		rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));

		return generator.generatePassword(size, rules).toLowerCase();
	}

	public static boolean validatePasswordStrength(String password) {
		List<Rule> rules = new ArrayList<Rule>();
		rules.add(new LengthRule(6, 32));
		rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
		rules.add(new CharacterRule(EnglishCharacterData.Alphabetical, 1));

		PasswordValidator validator = new PasswordValidator(rules);
		PasswordData passwordData = new PasswordData(password);
		RuleResult result = validator.validate(passwordData);
		return result.isValid();
	}

	public static byte[] textToDataImageUrl(String text) {
		return textToDataImageUrl(text, new Font("Arial", Font.PLAIN, 16));
	}

	public static byte[] textToDataImageUrl(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(null, true, true);

		// Calculate size of buffered image.
		LineMetrics lineMetrics = font.getLineMetrics(text, frc);

		Rectangle2D rect2d = font.getStringBounds(text, frc);

		BufferedImage bufferedImage = new BufferedImage((int) Math.ceil(rect2d.getWidth()),
				(int) Math.ceil(rect2d.getHeight()), BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics2d = bufferedImage.createGraphics();

		graphics2d.setRenderingHints(RenderingProperties);

		graphics2d.setBackground(Color.WHITE);
		graphics2d.setColor(Color.BLACK);

		graphics2d.clearRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

		graphics2d.setFont(font);

		graphics2d.drawString(text, 0, lineMetrics.getAscent());

		graphics2d.dispose();

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		try {
			ImageIO.write(bufferedImage, "png", byteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return byteArray.toByteArray();
	}

}
