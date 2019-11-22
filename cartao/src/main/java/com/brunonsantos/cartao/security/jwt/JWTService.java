package com.brunonsantos.cartao.security.jwt;

import java.security.Key;
import java.security.Permission;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.convert.Jsr310Converters.LocalDateTimeToDateConverter;

import com.brunonsantos.cartao.security.AuthenticatedUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

public class JWTService {

	private static final byte[] SECRET = { 127, -49, 52, -110, -31, -120, 23, -36, -42, -21, 95, -44, -23, 102, 86,
			-125, 9, -96, -104, -125, 71, -84, 85, 38, -45, 23, 105, -61, -41, -24, 101, 45, -99, -23, 11, 112, 30, 120,
			13, 49, -110, 123, -36, -88, 37, -25, -50, 112, -30, -126, 92, -41, -50, -85, 91, 16, 112, -10, -32, 75,
			-86, -57, -121, -21 };
	private static final String AUTH_TYPE = "Bearer ";

	private static final String ISSUER = "sensorkey";
	private static final String AUDIENCE = "sensorkey_mobile";

	private static final String SCOPES_CLAIM = "scopes";
	private static final String USER_ID_CLAIM = "uid";

	private static final ExpiringMap<Long, Boolean> revokedAccessTokens = ExpiringMap.builder().variableExpiration().build();

	public static AuthenticatedUser parseToken(final String authorizationHeader) {
		AuthenticatedUser authenticatedUser = null;
		if (authorizationHeader != null) {
			if (authorizationHeader.indexOf(AUTH_TYPE) == -1) {
				throw new MalformedJwtException("Malformed Authorization Header.");
			}
			final String token = authorizationHeader.substring(AUTH_TYPE.length());

			// Parse the token
			JwtParser parser = Jwts.parser().setSigningKey(SECRET).requireIssuer(ISSUER).requireAudience(AUDIENCE);
			@SuppressWarnings("rawtypes")
			Jwt jwt = parser.parse(token);
			Claims claims = parser.parseClaimsJws(token).getBody();

			if (claims.getSubject() == null) {
				throw new MissingClaimException(jwt.getHeader(), claims,
						String.format("Expected %s claim, but was not present in the JWT claims.", "sub"));
			}

			if (!claims.containsKey(SCOPES_CLAIM)) {
				throw new MissingClaimException(jwt.getHeader(), claims,
						String.format("Expected %s claim, but was not present in the JWT claims.", SCOPES_CLAIM));
			}

			authenticatedUser = new AuthenticatedUser();
			final String tokenId = claims.get(Claims.ID, String.class);
			if (tokenId != null) {
				if (StringUtils.isNumeric(tokenId)) {
					authenticatedUser.setAccessTokenId(Long.parseLong(tokenId));

					if (revokedAccessTokens.containsKey(authenticatedUser.getAccessTokenId())) {
						throw new ExpiredJwtException(jwt.getHeader(), claims, "This Token is revoked.");
					}
				} else {
					authenticatedUser.setRefreshTokenId(tokenId);
				}
			}

			authenticatedUser.setEmail(claims.getSubject());

			if (claims.containsKey(USER_ID_CLAIM)) {
				authenticatedUser.setId(claims.get(USER_ID_CLAIM, Long.class));
			}
		}
		return authenticatedUser;
	}

	public static String createJWTToken(final Long userId, final String tokenId, final String subject, final List<Long> siteIds,
			final LocalDateTime expirationDate, Long tenantId, final Permission... permissions) {
		final Date issueDate = GregorianCalendar.getInstance().getTime();
		final Key key = Keys.hmacShaKeyFor(SECRET);
		final Set<String> permissionNames = new HashSet<String>();
		final JwtBuilder jwtBuilder = Jwts.builder().setSubject(subject).setIssuer(ISSUER).setAudience(AUDIENCE)
				.setIssuedAt(issueDate).setExpiration(LocalDateTimeToDateConverter.INSTANCE.convert(expirationDate))
				.claim(SCOPES_CLAIM, permissionNames.toArray(new String[] {}));

		if(userId != null) {
			jwtBuilder.claim(USER_ID_CLAIM, userId);
		}

		return jwtBuilder.signWith(key, SignatureAlgorithm.HS512).compact();
	}

	public static void markAsRevoked(Long tokenId, LocalDateTime expirationDate) {
		final long duration = LocalDateTime.now().until(expirationDate, ChronoUnit.MINUTES);
		revokedAccessTokens.put(tokenId, Boolean.TRUE, ExpirationPolicy.CREATED, duration, TimeUnit.MINUTES);
	}
}
