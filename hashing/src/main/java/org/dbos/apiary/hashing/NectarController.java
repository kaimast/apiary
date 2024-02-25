package org.dbos.apiary.hashing;

import com.google.protobuf.InvalidProtocolBufferException;
import org.dbos.apiary.client.ApiaryWorkerClient;
import org.dbos.apiary.hashing.functions.NectarHashing;
import org.dbos.apiary.postgres.PostgresConnection;
import org.dbos.apiary.utilities.ApiaryConfig;
import org.dbos.apiary.utilities.Utilities;
import org.dbos.apiary.worker.ApiaryNaiveScheduler;
import org.dbos.apiary.worker.ApiaryWorker;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        ApiaryConfig.provenancePort = 5433;  // Store provenance data in Vertica.

        PostgresConnection conn = new PostgresConnection("localhost", ApiaryConfig.postgresPort, "postgres", "dbos", ApiaryConfig.vertica, "localhost");

        this.worker = new ApiaryWorker(new ApiaryNaiveScheduler(), 4, ApiaryConfig.vertica, "localhost");
        worker.registerConnection(ApiaryConfig.postgres, conn);
        worker.registerFunction("NectarHashing", ApiaryConfig.postgres, NectarHashing::new);
        worker.startServing();

        this.client = new ApiaryWorkerClient("localhost");
    }

    @GetMapping("/")
    public RedirectView index(Model model) {
        return new RedirectView("/home");
    }
}
