package org.dbos.apiary.microbench.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.dbos.apiary.postgres.PostgresContext;
import org.dbos.apiary.postgres.PostgresFunction;

public class NectarHashing extends PostgresFunction {
    public static int runFunction(PostgresContext ctxt, int numHashes, int inputLen) {
    	byte[] b = new byte[inputLen];
    	new Random().nextBytes(b);
    	
    	try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	    	
	    	for(int i = 0; i < numHashes; ++i) {
	    		md.update(b);
	    		md.digest();
	    		
	    		md.reset();
	    	}
	    	
	    	return 0;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		}
    }
}
