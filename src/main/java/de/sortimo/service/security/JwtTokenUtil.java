package de.sortimo.service.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.sortimo.rest.converter.UserConverter;
import de.sortimo.rest.dto.JwtUserDto;
import de.sortimo.service.UserService;
import de.sortimo.service.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -5104791108765756775L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class); 
	
	@Autowired
	private UserService userService;
	
	private UserConverter userConverter = new UserConverter();
	
	static String CLAIM_KEY_USERNAME = "sub";
    static String CLAIM_KEY_AUDIENCE = "audience";
    static String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }
    
    public JwtUserDto getUserFromToken(String token) {
    	
    	JwtUserDto jwtUser = null;
        
        try {
            Claims claims = getClaimsFromToken(token);

            String username = (String) claims.get("username");
            
            Optional<User> tUser = userService.findByUsername(username);
            
            if (tUser.isPresent()) {
            	jwtUser = userConverter.getJwtUser(tUser.get());
            }
        } catch (Exception e) {
        	
        	LOGGER.error("Cannot read UserFromToken:", e);
        	jwtUser = null;
        }
        
        return jwtUser;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            Claims claims = getClaimsFromToken(token);
            audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(UserDetails userDetails, JwtUserDto user) {

    	Map<String, Object> claims = new HashMap<>();
    	claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
    	claims.put(CLAIM_KEY_CREATED, new Date());
    	
    	claims.put("username", user.getUsername());
		
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
    	
        String username = getUsernameFromToken(token);
        
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}