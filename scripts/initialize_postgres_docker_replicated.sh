#!/bin/bash
set -ex

CURR_DIR=$(dirname $(realpath $0))

# Start Postgres Docker image.
docker pull postgres:14.5-bullseye

SCRIPT_DIR=$CURR_DIR PRIMARY_PORT=5432 REPLICATION_PORT=5433 PRIMARY_USER="postgres" PRIMARY_PWD="dbos" REPLICATION_USER="replicator" REPLICATION_PWD="replicator_password" docker-compose -f $CURR_DIR/replicated-postgres-compose.yaml up -d postgres_primary postgres_replica

sleep 5
