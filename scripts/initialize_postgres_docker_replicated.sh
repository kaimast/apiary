#!/bin/bash
set -ex

SCRIPT_DIR=$(dirname $(realpath $0))

# Start Postgres Docker image.
docker pull postgres:14.5-bullseye

PRIMARY_PORT=5432 REPLICATION_PORT=5433 PRIMARY_USER="postgres" PRIMARY_PWD="dbos" REPLICATION_USER="replicator" REPLICATION_PWD="replicator_password" docker-compose -f replicated-postgres-compose.yaml up -d postgres_primary postgres_replica

sleep 5
