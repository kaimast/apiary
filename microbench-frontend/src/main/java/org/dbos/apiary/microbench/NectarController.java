package org.dbos.apiary.microbench;

import org.dbos.apiary.client.ApiaryWorkerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.InvalidProtocolBufferException;

@RestController
public class NectarController {
    private ThreadLocal<ApiaryWorkerClient> client;
    
    @Value("${apiary.address}")
    private String apiaryAddress;


    public NectarController() {
        this.client = new ThreadLocal<ApiaryWorkerClient>();
    }

    @PostMapping("/hashing")
    public void index(@RequestBody HashingArgs args) throws InvalidProtocolBufferException {
    	if (client.get() == null) {
    		client.set(new ApiaryWorkerClient(this.apiaryAddress));
    	}
        client.get().executeFunction("NectarHashing", args.getNumHashes(), args.getInputLen());
    }
}
