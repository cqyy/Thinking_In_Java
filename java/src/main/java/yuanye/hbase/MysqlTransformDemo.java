package yuanye.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by cloudera on 12/3/14.
 * <p>
 * Transform data from mysql to HBase.
 */
public class MysqlTransformDemo {

    private final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final static String DB_URL = "jdbc:mysql://192.168.2.25/gaogao";   //DataBase Name is 'gaogao';
    private final static String USER = "yuanye";
    private final static String PASSWD = "yuanye";

    private final static int BATCH_SIZE = 20;

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find proper driver for MySQL");
            System.exit(1);
        }

        Connection conn = null;
        Statement statement = null;
        HTable hTable = null;
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASSWD);
            System.out.println("Create connection to MySQL succeed.");
            statement = conn.createStatement();
            Configuration conf = HBaseConfiguration.create();
            /*
             First ,transform data in table 'taobao_2' to HBase.

             Table 'taobao_2' structure as follow:

             +------------+---------------+------+-----+---------+-------+
             | Field      | Type          | Null | Key | Default | Extra |
             +------------+---------------+------+-----+---------+-------+
             | itemId     | varchar(60)   | NO   | PRI | NULL    |       |
             | name       | varchar(100)  | YES  |     | NULL    |       |
             | itemUrl    | varchar(100)  | YES  |     | NULL    |       |
             | price      | varchar(60)   | YES  |     | NULL    |       |
             | category1  | varchar(32)   | YES  |     | NULL    |       |
             | category2  | varchar(32)   | YES  |     | NULL    |       |
             | sales      | int(10)       | YES  |     | NULL    |       |
             | shopName   | varchar(100)  | YES  |     | NULL    |       |
             | shopUrl    | varchar(100)  | YES  |     | NULL    |       |
             | shopId     | varchar(30)   | YES  |     | NULL    |       |
             | sbn        | varchar(64)   | YES  |     | NULL    |       |
             | totalPrice | decimal(16,0) | YES  |     | NULL    |       |
             | DBB        | tinytext      | YES  |     | NULL    |       |
             | monthSales | int(32)       | YES  |     | NULL    |       |
             +------------+---------------+------+-----+---------+-------+

             This table will be transform into HBase name 'taobao';
             There are three column family :'basic_info','sale_info','shop_info';
             row key:itemId
             basic_info:name,price,url,category1,category2
             sale_info:sales,totalPrice,monthSales
             shop_info:shopName,url,id
            */
            String sql = "SELECT itemId,name,price,itemUrl,category1,category2,sales," +
                    "totalPrice,monthSales,shopName,shopUrl,shopId from taobao_2";
            hTable = new HTable(conf, "taobao");
            System.out.println("Create connection to HBase succeed.");
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Put> putBatch = new ArrayList<>(BATCH_SIZE);    //batch puts
            byte[] basicInfoFam = Bytes.toBytes("basic_info");
            byte[] saleInfoFam = Bytes.toBytes("sale_info");
            byte[] shopInfoFam = Bytes.toBytes("shop_info");
            int count = 0;
            while (rs.next()) {
                String itemId = rs.getString("itemId");
                String itemName = rs.getString("name");
                String price = rs.getString("price");
                String itemURL = rs.getString("itemUrl");
                String category1 = rs.getString("category1");
                String category2 = rs.getString("category2");
                String sales = rs.getString("sales");
                String totalPrice = rs.getString("totalPrice");
                String monthSales = rs.getString("monthSales");
                String shopName = rs.getString("shopName");
                String shopId = rs.getString("shopId");
                String shopUrl = rs.getString("shopUrl");

                Put put = new Put(Bytes.toBytes(itemId));
                //basic_info family
                put.add(basicInfoFam, Bytes.toBytes("name"), Bytes.toBytes(itemName));
                put.add(basicInfoFam, Bytes.toBytes("price"), Bytes.toBytes(price));
                put.add(basicInfoFam, Bytes.toBytes("url"), Bytes.toBytes(itemURL));
                put.add(basicInfoFam, Bytes.toBytes("category1"), Bytes.toBytes(category1));
                put.add(basicInfoFam, Bytes.toBytes("category2"), Bytes.toBytes(category2));
                //sale_info family
                put.add(saleInfoFam, Bytes.toBytes("sales"), Bytes.toBytes(sales));
                put.add(saleInfoFam, Bytes.toBytes("totalPrice"), Bytes.toBytes(totalPrice));
                put.add(saleInfoFam, Bytes.toBytes("monthSales"), Bytes.toBytes(monthSales));
                //shop_info family
                put.add(shopInfoFam, Bytes.toBytes("id"), Bytes.toBytes(shopId));
                put.add(shopInfoFam, Bytes.toBytes("name"), Bytes.toBytes(shopName));
                put.add(shopInfoFam, Bytes.toBytes("url"), Bytes.toBytes(shopUrl));

                putBatch.add(put);
                count++;
                if (putBatch.size() == BATCH_SIZE) {
                    //arrive at BATCH_SIZE
                    hTable.put(putBatch);
                    putBatch.clear();
                }
            }
            if (!putBatch.isEmpty()) {
                //put rest data into HBase
                hTable.put(putBatch);
            }
            hTable.close();
            System.out.println("Transform " + count + " item information.");

            /*
            * Then ,transform table 'taobao_digital'.
            *
            * The table structure is as follow:

            +------------+---------------+------+-----+---------+-------+
            | Field      | Type          | Null | Key | Default | Extra |
            +------------+---------------+------+-----+---------+-------+
            | itemId     | varchar(60)   | NO   | PRI | NULL    |       |
            | name       | varchar(100)  | YES  |     | NULL    |       |
            | itemUrl    | varchar(100)  | YES  |     | NULL    |       |
            | price      | varchar(60)   | YES  |     | NULL    |       |
            | category1  | varchar(32)   | YES  |     | NULL    |       |
            | category2  | varchar(32)   | YES  |     | NULL    |       |
            | sales      | int(10)       | YES  |     | NULL    |       |
            | shopName   | varchar(100)  | YES  |     | NULL    |       |
            | shopUrl    | varchar(100)  | YES  |     | NULL    |       |
            | shopId     | varchar(30)   | YES  |     | NULL    |       |
            | sbn        | varchar(64)   | YES  |     | NULL    |       |
            | totalPrice | decimal(16,0) | YES  |     | NULL    |       |
            | DBB        | tinytext      | YES  |     | NULL    |       |
            | monthSales | int(32)       | YES  |     | NULL    |       |
            +------------+---------------+------+-----+---------+-------+

            *
            * This table will be transformed to HTable 'taobao_digital';
            * In this HTable,there are three column families:item_info,shop_info,sale_info;
            * Row key   : itemId.
            * item_info : name,url,price,category1,category2.
            * shop_info : name,url,id.
            * sale_info : sales,monthSales,totalPrice.
            * */
            sql = "SELECT itemId,name,price,itemUrl,category1,category2,sales," +
                    "totalPrice,monthSales,shopName,shopUrl,shopId from taobao_digital";
            hTable = new HTable(conf, "taobao_digital");
            System.out.println("Create connection to HBase succeed.");
            rs = statement.executeQuery(sql);
            count = 0;
            while (rs.next()) {
                String itemId = rs.getString("itemId");
                String itemName = rs.getString("name");
                String price = rs.getString("price");
                String itemURL = rs.getString("itemUrl");
                String category1 = rs.getString("category1");
                String category2 = rs.getString("category2");
                String sales = rs.getString("sales");
                String totalPrice = rs.getString("totalPrice");
                String monthSales = rs.getString("monthSales");
                String shopName = rs.getString("shopName");
                String shopId = rs.getString("shopId");
                String shopUrl = rs.getString("shopUrl");

                Put put = new Put(Bytes.toBytes(itemId));
                //basic_info family
                put.add(basicInfoFam, Bytes.toBytes("name"), Bytes.toBytes(itemName));
                put.add(basicInfoFam, Bytes.toBytes("price"), Bytes.toBytes(price));
                put.add(basicInfoFam, Bytes.toBytes("url"), Bytes.toBytes(itemURL));
                put.add(basicInfoFam, Bytes.toBytes("category1"), Bytes.toBytes(category1));
                put.add(basicInfoFam, Bytes.toBytes("category2"), Bytes.toBytes(category2));
                //sale_info family
                put.add(saleInfoFam, Bytes.toBytes("sales"), Bytes.toBytes(sales));
                put.add(saleInfoFam, Bytes.toBytes("totalPrice"), Bytes.toBytes(totalPrice));
                put.add(saleInfoFam, Bytes.toBytes("monthSales"), Bytes.toBytes(monthSales));
                //shop_info family
                put.add(shopInfoFam, Bytes.toBytes("id"), Bytes.toBytes(shopId));
                put.add(shopInfoFam, Bytes.toBytes("name"), Bytes.toBytes(shopName));
                put.add(shopInfoFam, Bytes.toBytes("url"), Bytes.toBytes(shopUrl));

                putBatch.add(put);
                count++;
                if (putBatch.size() == BATCH_SIZE) {
                    //arrive at BATCH_SIZE
                    hTable.put(putBatch);
                    putBatch.clear();
                }
            }
            if (!putBatch.isEmpty()) {
                //put rest data into HBase
                hTable.put(putBatch);
            }
            System.out.println("Transform " + count + " item information.");
            hTable.close();
        } catch (SQLException e) {
            System.err.println("Exception: " + e.getMessage());
        } catch (RetriesExhaustedWithDetailsException e) {
            System.err.println("Exception occurred when put data into HBase:" + e.getMessage());
        } catch (InterruptedIOException e) {
            System.err.println("Exception: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    //ignored
                }
            }

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //ignored
                }
            }

            if (hTable != null) {
                try {
                    hTable.close();
                } catch (IOException e) {
                    System.err.println("Exception when close HTable: " + e.getMessage());
                }
            }
        }
        System.out.println("Over!");

    }
}
