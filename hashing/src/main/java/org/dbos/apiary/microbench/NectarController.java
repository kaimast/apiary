package org.dbos.apiary.microbench;

import org.dbos.apiary.client.ApiaryWorkerClient;
import org.dbos.apiary.hashing.functions.NectarHashing;
import org.dbos.apiary.postgres.PostgresConnection;
import org.dbos.apiary.utilities.ApiaryConfig;
import org.dbos.apiary.worker.ApiaryNaiveScheduler;
import org.dbos.apiary.worker.ApiaryWorker;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.InvalidProtocolBufferException;

import java.sql.*;

@RestController
public class NectarController {
    private final ApiaryWorkerClient client;
    private final ApiaryWorker worker;

    public NectarController() throws SQLException {
        ApiaryConfig.captureUpdates = true;
        ApiaryConfig.captureReads = true;
        ApiaryConfig.provenancePort = 5432;  // Store provenance data in the same database.

        PostgresConnection conn = new PostgresConnection("localhost", ApiaryConfig.postgresPort, "postgres", "dbos");
        
        this.worker = new ApiaryWorker(new ApiaryNaiveScheduler(), 4, ApiaryConfig.postgres, ApiaryConfig.provenanceDefaultAddress);
        worker.registerConnection(ApiaryConfig.postgres, conn);
        worker.registerFunction("NectarHashing", ApiaryConfig.postgres, NectarHashing::new);
        worker.startServing();

        this.client = new ApiaryWorkerClient("localhost");
    }

    @PostMapping("/hashing")
    public void index(@RequestBody HashingArgs args) throws InvalidProtocolBufferException {
        client.executeFunction("NectarHashing", args.getNumHashes(), args.getInputLen());
    }
}
