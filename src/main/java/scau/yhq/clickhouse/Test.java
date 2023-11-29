package scau.yhq.clickhouse;

import com.clickhouse.client.*;
import com.clickhouse.data.ClickHouseFormat;
import com.clickhouse.data.ClickHouseRecord;

import java.util.concurrent.ExecutionException;

public class Test {

    static int query(ClickHouseNode server, String table) throws ClickHouseException {
        try (ClickHouseClient client = ClickHouseClient.newInstance(server.getProtocol());
             ClickHouseResponse response = client.read(server)
                     // prefer to use RowBinaryWithNamesAndTypes as it's fully supported
                     // see details at https://github.com/ClickHouse/clickhouse-java/issues/928
                     .format(ClickHouseFormat.RowBinaryWithNamesAndTypes)
                     .query("select * from " + table).execute().get()) {
            int count = 0;
            // or use stream API via response.stream()
            for (ClickHouseRecord r : response.records()) {
                count++;
            }
            return count;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw ClickHouseException.forCancellation(e, server);
        } catch (ExecutionException e) {
            throw ClickHouseException.of(e, server);
        }
    }

    public static void main(String[] args) {
        ClickHouseNode server = ClickHouseNode.builder()
                .host(System.getProperty("chHost", "192.168.101.100"))
                .port(ClickHouseProtocol.HTTP, Integer.getInteger("chPort", 8123))
                // .port(ClickHouseProtocol.GRPC, Integer.getInteger("chPort", 9100))
                // .port(ClickHouseProtocol.TCP, Integer.getInteger("chPort", 9000))
                .database("default").credentials(ClickHouseCredentials.fromUserAndPassword(
                        System.getProperty("chUser", "default"), System.getProperty("chPassword", "Y1bMm7N#Zq")))
                .build();

        String table = "select * from domain";

        try {
            System.out.println("Query: " + query(server, table));
        } catch (ClickHouseException e) {
            e.printStackTrace();
        }
    }
}
