package edu.umass.ckc.wo.login;

import edu.umass.ckc.wo.db.DbAdmin;
import edu.umass.ckc.wo.db.DbUser;
import edu.umass.ckc.wo.db.DbUtil;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by david on 12/22/2016.
 *
 * Storage and checking of passwords.  Uses a salting algorithm as follows:
 *
 * The plaintext pw is append to salt (a random string).  We run them through a hashing function and then store
 * it in the student table along with the salt.  When that user logs in we get the salt, append it to the pw he gives,
 * hash it and compare to whats in the db.
 */
public class PasswordAuthentication {
    /**
     * Each token produced by this class uses this identifier as a prefix.
     */
    public static final String ID = "$31$";

    /**
     * The minimum recommended cost, used by default
     */
    public static final int DEFAULT_COST = 16;

    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final int SIZE = 128;

    private static final Pattern layout = Pattern.compile("\\$31\\$(\\d\\d?)\\$(.{43})");

    private final SecureRandom random;
    private int cost;

    private static PasswordAuthentication instance = null;

    public static PasswordAuthentication getInstance (int cost) {
        if (instance == null)
            instance = new PasswordAuthentication(cost);
        return instance;
    }

    public static PasswordAuthentication getInstance () {
        if (instance == null)
            instance = new PasswordAuthentication();
        return instance;
    }

    public PasswordAuthentication()
    {
        this(DEFAULT_COST);
    }

    /**
     * Create a password manager with a specified cost
     *
     * @param cost the exponential computational cost of hashing a password, 0 to 30
     */
    public PasswordAuthentication(int cost)
    {
        iterations(cost); /* Validate cost */
        this.cost = cost;
        this.random = new SecureRandom();
    }

    private static int iterations(int cost)
    {
        if ((cost & ~0x1F) != 0)
            throw new IllegalArgumentException("cost: " + cost);
        return 1 << cost;
    }



    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations)
    {
        KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
            return f.generateSecret(spec).getEncoded();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Missing algorithm: " + ALGORITHM, ex);
        }
        catch (InvalidKeySpecException ex) {
            throw new IllegalStateException("Invalid SecretKeyFactory", ex);
        }
    }



    /**
     * Hash a password for storage.
     *
     * @return a secure authentication token to be stored for later authentication
     */
    public String hash(char[] password)
    {
        byte[] salt = new byte[SIZE / 8];
        random.nextBytes(salt);
        byte[] dk = pbkdf2(password, salt, 1 << cost);
        byte[] hash = new byte[salt.length + dk.length];
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(dk, 0, hash, salt.length, dk.length);
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        return ID + cost + '$' + enc.encodeToString(hash);
    }

    /**
     * Authenticate with a password and a stored password token.
     *
     * @return true if the password and token match
     */
    public boolean authenticate(char[] password, String token)
    {
        Matcher m = layout.matcher(token);
        if (!m.matches())
            throw new IllegalArgumentException("Invalid token format");
        int iterations = iterations(Integer.parseInt(m.group(1)));
        byte[] hash = Base64.getUrlDecoder().decode(m.group(2));
        byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
        byte[] check = pbkdf2(password, salt, iterations);
        int zero = 0;
        for (int idx = 0; idx < check.length; ++idx)
            zero |= hash[salt.length + idx] ^ check[idx];
        return zero == 0;
    }



    /* Hash a password in an immutable {@code String}.
         *
         * <p>Passwords should be stored in a {@code char[]} so that it can be filled
* with zeros after use instead of lingering on the heap and elsewhere.
         *
         * @deprecated Use {@link #hash(char[])} instead
*/
    @Deprecated
    public String hash(String password)
    {
        return hash(password.toCharArray());
    }

    public static void main(String[] args) {
        try {
            String u = args[0];
            String pw = args[1];
            String token = PasswordAuthentication.getInstance(0).hash(pw.toCharArray());
            System.out.println("Token for password: " + token);
            Connection conn = DbUtil.getAConnection("localhost");
            DbAdmin.setPassword(conn,u,token);
            token = DbAdmin.getPassword(conn,u);
            boolean b = PasswordAuthentication.getInstance(0).authenticate(pw.toCharArray(),token);
            System.out.println("Password for user " + u + " is " + pw);
            System.out.println("Password authentication check is " + b);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
