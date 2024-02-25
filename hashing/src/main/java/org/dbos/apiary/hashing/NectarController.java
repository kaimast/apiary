package org.dbos.apiary.hashing;

import org.dbos.apiary.client.ApiaryWorkerClient;
import org.dbos.apiary.hashing.functions.NectarHashing;
import org.dbos.apiary.postgres.PostgresConnection;
import org.dbos.apiary.utilities.ApiaryConfig;
import org.dbos.apiary.worker.ApiaryNaiveScheduler;
import org.dbos.apiary.worker.ApiaryWorker;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.protobuf.InvalidProtocolBufferException;

import java.sql.*;

@Controller
@SessionAttributes("logincredentials")
public class NectarController {
    private final ApiaryWorkerClient client;
    private final ApiaryWorker worker;

    public NectarController() throws SQLException {
        ApiaryConfig.captureUpdates = true;
        ApiaryConfig.captureReads = true;
        ApiaryConfig.recordInput = true;
        ApiaryConfig.captureMetadata = true;

        PostgresConnection conn = new PostgresConnection("localhost", ApiaryConfig.postgresPort, "postgres", "dbos", ApiaryConfig.vertica, "localhost");

        this.worker = new ApiaryWorker(new ApiaryNaiveScheduler(), 4, ApiaryConfig.vertica, "localhost");
        worker.registerConnection(ApiaryConfig.postgres, conn);
        worker.registerFunction("NectarHashing", ApiaryConfig.postgres, NectarHashing::new);
        worker.startServing();

        this.client = new ApiaryWorkerClient("localhost");
    }

    @GetMapping("/")
    public void index(@RequestBody HashingArgs args) throws InvalidProtocolBufferException {
        client.executeFunction("NectarHashing", args.getNumHashes(), args.getInputLen());
    }
}
