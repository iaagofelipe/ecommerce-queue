#!/usr/bin/env bash
set -euo pipefail

# Fila principal FIFO
awslocal sqs create-queue --queue-name demo-queue.fifo \
  --attributes FifoQueue=true,ContentBasedDeduplication=true,VisibilityTimeout=30,ReceiveMessageWaitTimeSeconds=10

# DLQ opcional
awslocal sqs create-queue --queue-name demo-queue-dlq.fifo \
  --attributes FifoQueue=true,ContentBasedDeduplication=true
