package com.management.clientinvoice.util;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.management.clientinvoice.constant.DBConstants;
import com.management.clientinvoice.domain.UserProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_TIMESTAMP = "timestamp";
    private static final String EMAIL_SECRET = "YMa2KP596mYxFHwcujQVvXbxhzTwvkq29jfkaAl";

//    @Value("${jwt.secret}")
//    private String secret;
//
//    @Value("${jwt.expiration}")
//    private Long expiration;

//    public String getUsernameFromToken(String token) throws Exception {
//        String username;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            System.out.println("claims : " + claims);
//            username = claims.getSubject();
//            System.out.println("username : " + username);
//        } catch (Exception e) {
//            username = null;
//        }
//        return username;
//    }

//    public String getRoleFromToken(String token) {
//        String role;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            role = (String) claims.get("role");
//
//        } catch (Exception e) {
//            role = null;
//        }
//        return role;
//    }

//    public ModelMap getDataFromToken(String token) throws Exception {
//        try {
//            ModelMap map = new ModelMap();
//            final Claims claims = getClaimsFromToken(token);
//            map.put("email", claims.get("email"));
//            return map;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public ModelMap getDataFromEmailToken(String token) throws Exception {
        try {
            ModelMap map = new ModelMap();
            final Claims claims = getClaimsFromEmailToken(token);
            map.put("email", claims.get("email"));
            return map;
        } catch (Exception e) {
            return null;
        }
    }
//
//    public Date getCreatedDateFromToken(String token) throws Exception {
//        Date created;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
//        } catch (Exception e) {
//            created = null;
//        }
//        return created;
//    }

//    public Date getExpirationDateFromToken(String token) throws Exception {
//        Date expiration;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            expiration = claims.getExpiration();
//        } catch (Exception e) {
//            expiration = null;
//        }
//        return expiration;
//    }

    public Date getExpirationDateFromEmailToken(String token) throws Exception {
        Date expiration;
        try {
            final Claims claims = getClaimsFromEmailToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
//
//    private Claims getClaimsFromToken(String token) throws Exception {
//        Claims claims;
//        try {
//            claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            e.printStackTrace();
//            claims = null;
//        }
//        return claims;
//    }

    private Claims getClaimsFromEmailToken(String token) throws Exception {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(EMAIL_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
            claims = null;
        }
        return claims;
    }

  //  private Date generateExpirationDate() {
//        return new Date(System.currentTimeMillis() + expiration * 1000);
//    }

    private Date generateEmailVerificationExpirationDate() {
        return new Date(System.currentTimeMillis() + DBConstants.EMAIL_EXPITY_TIME);
    }

    /**
     * Check token is expired or not for email token we have
     * @param token
     * @return
     * @throws Exception
     */
    public Boolean isEmailTokenExpired(String token) throws Exception {
        final Date expiration = getExpirationDateFromEmailToken(token);
        if(expiration==null){
            return false;
        }
        return expiration.before(new Date());

    }

    /**
     * Check token is expired or not for email token we have
     * @param token
     * @return
     * @throws Exception
     */
//    public Boolean isTokenExpired(String token) throws Exception {
//        final Date expiration = getExpirationDateFromToken(token);
//        if(expiration==null){
//            return false;
//        }
//        return expiration.before(new Date());
//
//    }

    /**
     * @param object
     * @return
     */
    public String generateToken(Object object) {
        UserProfile user = (UserProfile) object;
        String keyUsername = "";
        if (user != null) {
            if (user.getUserIdentity().getUniqueId()!= null && !user.getUserIdentity().getUniqueId().equals("")) {
                keyUsername = user.getUserIdentity().getUniqueId();
            }
        }
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put(CLAIM_KEY_USERNAME, keyUsername);
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_ROLE, "");
        claims.put(CLAIM_KEY_TIMESTAMP, new Timestamp(System.currentTimeMillis()));
        return generateToken(claims);
    }

	/*public String generateAdminUserToken(Object object)throws Exception {
		AdminUser adminUser = (AdminUser) object;
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_ROLE, adminUser.getRole());
		claims.put(CLAIM_KEY_USERNAME, adminUser.getEmail());
		claims.put(CLAIM_KEY_CREATED, new Date());
		return generateToken(claims);
	}*/

    public String generateEmailVerificationToken(ModelMap object) throws Exception {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("email", object.get("email"));
        claims.put("random", object.get("random"));
        return generateEmailVerificationToken(claims);
    }

    /**
     * TODO: need to change SignatureAlgorithm.HS512 as this is an open key. It can be decrypted without secret
     * 		use (SignatureAlgorithm.RS512, secretKey)
     * alternatively can use following:
     * https://connect2id.com/products/nimbus-jose-jwt/download
     * @param claims
     * @return
     */
//    private String generateToken(Map<String, Object> claims) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(generateExpirationDate())
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }

    /**
     * TODO: need to change SignatureAlgorithm.HS512 as this is an open key. It can be decrypted without secret
     * 		use (SignatureAlgorithm.RS512, secretKey)
     * alternatively can use following:
     * https://connect2id.com/products/nimbus-jose-jwt/download
     * @param claims
     * @return
     * @throws Exception
     */
    private String generateEmailVerificationToken(Map<String, Object> claims) throws Exception {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(null)
                .signWith(SignatureAlgorithm.HS256, EMAIL_SECRET)
                .compact();
    }

//    public Boolean canTokenBeRefreshed(String token) throws Exception {
//        return (!isTokenExpired(token));
//    }
//
//    public String refreshToken(String token)throws Exception {
//        String refreshedToken;
//        try {
//            final Claims claims = getClaimsFromToken(token);
//            claims.put(CLAIM_KEY_CREATED, new Date());
//            refreshedToken = generateToken(claims);
//        } catch (Exception e) {
//            refreshedToken = null;
//        }
//        return refreshedToken;
//    }


    /**
     * @param token
     * @return
     * @throws Exception
     */
//    public Boolean validateToken(String token, UserDetails userDetails) throws Exception {
//        UserIdentity user = (UserIdentity) userDetails;
//        System.out.println("In validateToken user : " + user);
//        final String username = getUsernameFromToken(token);
//        System.out.println("In validateToken username : " + username);
//        //final Date created = getCreatedDateFromToken(token);
//        //final Date expiration = getExpirationDateFromToken(token);
//        if (user != null) {
//            if (user.getEmail()!= null && user.getEmail().equals(username)) {
//                System.out.println("In validateToken email ok : " + username);
//                return true;
//            }
//        }
//        return false;
//    }

    public Boolean validateEmailVerificationToken(String token) throws Exception {
        return (!isEmailTokenExpired(token)) ;
    }
}

