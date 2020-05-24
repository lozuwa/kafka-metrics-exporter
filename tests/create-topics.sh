for each in {1..2500}; do
  ./kafka-topics.sh --zookeeper zookeeper:2181 --create --partitions 8 --replication-factor 1 --topic foo${each}
done;
