package scau.yhq.clickhouse;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class DataCreator {

    public static void main(String[] args) {
//        for (int i = 0; i < 1024; i++) {
//            log.info(generateRandomDomain());
//        }
        // 生成随机的域名并将结果写入CSV文件
        generateRandomDomainsAndWriteToCsv("output.csv", 2 << 22);
    }

    public static void generateRandomDomainsAndWriteToCsv(String csvFilePath, int numberOfDomains) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath))) {
            // 写入CSV文件的表头
            csvWriter.writeNext(new String[]{"domain","domain_group"});

            for (int i = 1; i <= numberOfDomains; i++) {
                // 生成随机的域名
                String randomDomain = generateRandomDomain();

                // 将域名写入CSV文件
                csvWriter.writeNext(new String[]{randomDomain, "1"});
            }

            System.out.println("CSV文件生成成功：" + csvFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomDomain() {
        // 常见域名结构和字符集
        String[] domainStructures = {"example", "test", "demo", "random", "yourname"};
        String[] domainExtensions = {".com", ".net", ".org"};

        // 随机选择域名结构和扩展名
        String randomStructure = domainStructures[new Random().nextInt(domainStructures.length)];
        String randomExtension = domainExtensions[new Random().nextInt(domainExtensions.length)];

        // 生成随机的数字后缀
        String randomSuffix = UUID.randomUUID().toString().replaceAll("-", "");

        // 拼接生成的随机域名
        return randomStructure + randomSuffix + randomExtension;
    }
}
