package org.dbos.apiary.hashing.functions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.dbos.apiary.function.ApiaryStatelessContext;
import org.dbos.apiary.function.StatelessFunction;
import org.dbos.apiary.microbench.HashingArgs;

public class NectarHashing extends StatelessFunction {
    public static int runFunction(ApiaryStatelessContext ctxt, HashingArgs args) {
    	byte[] b = new byte[args.getInputLen()];
    	new Random().nextBytes(b);
    	
    	try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
	    	
	    	for(int i = 0; i < args.getNumHashes(); ++i) {
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
