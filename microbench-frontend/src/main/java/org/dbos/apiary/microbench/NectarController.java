package org.dbos.apiary.microbench;

import org.dbos.apiary.client.ApiaryWorkerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.IOException;

@RestController
public class NectarController {
    private ThreadLocal<ApiaryWorkerClient> client;

    // shared counter to assign object ids
    private static AtomicInteger counter = new AtomicInteger(0);
    
    @Value("${apiary.address}")
    private String apiaryAddress;


    public NectarController() {
        this.client = new ThreadLocal<ApiaryWorkerClient>();
    }

    @PostMapping("/hashing")
    public void hashing(@RequestBody HashingArgs args) throws InvalidProtocolBufferException {
    	if (client.get() == null) {
    		client.set(new ApiaryWorkerClient(this.apiaryAddress));
    	}
      client.get().executeFunction("NectarHashing", args.getNumHashes(), args.getInputLen());
    }
    
    @PostMapping("/create")
    public int create(@RequestBody CreateArgs msg) throws IOException {
        if (client.get() == null) {
          client.set(new ApiaryWorkerClient(this.apiaryAddress));
        }
        int objectId = counter.incrementAndGet();
        client.get().executeFunction("NectarCreate", objectId, msg.getNumEntries(), msg.getEntrySize());
        return objectId;
    }

    @PostMapping("/batch-create")
    public Map<String, List<Integer>> batch_create(@RequestBody BatchCreateArgs msg) throws IOException {
        if (client.get() == null) {
          client.set(new ApiaryWorkerClient(this.apiaryAddress));
        }
        List<Integer> objectIds = new ArrayList<Integer>();
        for (int i = 0; i < msg.getNumObjects(); ++i) {
        	objectIds.add(counter.incrementAndGet());
        }
        
        client.get().executeFunction("NectarBatchCreate", objectIds, msg.getNumEntries(), msg.getEntrySize());
        
        Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
        result.put("objectIds", objectIds);
        
        return result;
    }
    
    @PostMapping("/read_write")
    public void read_write(@RequestBody ReadWriteArgs msg) throws InvalidProtocolBufferException {
    	client.get().executeFunction("NectarReadWrite", msg.getObjectIds(), msg.getOpsPerObject(), msg.getEntriesPerObject(), msg.getEntrySize());
    }
}
