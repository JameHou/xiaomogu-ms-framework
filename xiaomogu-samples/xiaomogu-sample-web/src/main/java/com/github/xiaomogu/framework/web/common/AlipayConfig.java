package com.github.xiaomogu.framework.web.common;

import java.io.FileWriter;
import java.io.IOException;

/**
 * @auther JameHou
 * @date 2019/2/2 16:22
 */
public class AlipayConfig {

    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016092400581907";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDIscSEKsywCViwa3T0Gn5LcGHFtz/uyOgrNpTcxhkHgNB1vCDNJMLio5xKgTyJgH2wKsZ8A/wKSWDIkAXqdNNshgp7z4M5qhvBX1iuxoiW01EvoGLlSy2zXGoEBg95IPlKMLztQmQ6e/yQdRjrNz76XnMe3GyUWTCib8b2JGBRHyxxnyWaknvjcDZjRyj4QjwyMw8tebKj4GxQjM8us7hWg6kWhD32FdFvQ/dMzhbrMcvXia1TJpNrQfO5Dl6ssb18NfjmSIDRNZ78um8LYlXqgoiFBTK+Hj9Y9ynO8kUPOLMiDVxyqFVkZktOw4cvnbas8ptiWgRHyqzdEjyHrwH1AgMBAAECggEAUbCqQPH65xym0QisTkAKcEKKSaRRjayRdCV/am3n+jngoEu3PklcWkwmbd00+vMmNfLzX79vc/BySLD293weIupckhDhZTNsoMVin6GIL3xMa4ppNtPnAj73mDOvUKqvTheQjzKbAYM+Rr6jKfsO03JmgYVPXQMA3N2Kqt4bpmjEpXgFfFqiJE8UanLpbKUHR/uXx1i75C9mZy/OQf2wt9d7aAxMBj9RH7FYriPMytokkGYnf382ykbtKQoEl2ySzE+wXj3Njb29qHWlmdGNBJFlYkKdEMBWeKzTI6o68hgPOh3NDL8EGuhOXyffbf92b5sVByajtB3N63NrUD8YcQKBgQD426beADR2GrAusLns6hT8IA7tetEWH3uvaM8mD7xppsm0t1eZ6lhq8eb6OKcIvAJrDgdlTitb3pjuxAhqXReEkoC64kCOfK8wg3RIL5/1FXCNNRlwgwNJLqM9fKEBwf3PC2nZN/cgN+Y/JNq8A9kgl7yS8u4/J12OupKxAf7zcwKBgQDOdEKRVwk9uTrv2ED2MjK9gin3+1udgaLwDLlsm0MiGtyCe7ygzAZDmxbQenyNct7s6X9JxPcCysDTDg8mxoNIwhG1fp3sxLxe1EuVOZWNzRALK0zYRVdQDpTCg43jfFcEAVMGJDJydW5DI3BZpNmKiZWl1kzXaR2tJqtk8mnq9wKBgBdG5FrLhhyt7mDPovRoCMJ0dTD9VeVN2JHVqdqTijoPobpHk+gPpWmLauFovybv/wHodASjxSZ4sbFToL3iKzr5QRU56HPrca+F/DBsU/fmo3pxxwlhF8OWmyo7KAw0Cvt8w8/PjkvzLpQlbFg9u+dyie7sziAWHIgnJ2VrHF/FAoGAQe4sx/9UiyqZILkd3cGgJJbp0jCqTD13DFPz7zQSF1UesGjVj0A4i+R+P6uuFPnnKR9UL/m/rFuVgpEN06M3j8eyIhTCWDdntJz1Wj1efj9LbVWxn6tdO8q8RPYAHPK7UyMdIWvz1EueH8C0g1w6wJw5dxFdzrjZ6k7cE0u6+XcCgYEArayoPA3YYIpnwh7kMrySe5MY7uOL2Ov5NM0TaUdLOoyjWt1qkJ4MG4Z/pjdKJDhzpqLNKfEjvVa8mL8AOR/V2yYBL7V8tgMVYkIt5GMiDyYzsCJbwV2yAKIjrqDAWifMiga/m7vq/ls6vfwQm7b9yYGgUsBIE9m1ywaUazLXaSM=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm
    // 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsPEI9z/YJtDANJ0AZvHdWOXkJwLq04m2i79RfvkIfIphhsUM3MpKxcI97HFAAZnbAKQvTPxe6RsIlnybJQCK1JvI/Nk386PXQA81sQw+S7N/rnDSkPZg3VVgHdH6LYttVrPE4t3V5Pij/rekeCzU9rHmTnyLHZFMSQuTcJmyLpKS9YsoJMTU/hxurHhCGNznVOn4dCuhVxlKqUMlG9Zy8ZpuRErsidKkwTO3b2Nsrw85Lf5Y2QaYkfW5nRvlBc9STTC4+oyB8H3ylbGSJoJrnUuieGvCAniQGfgKofLRoMIN5V0/25TgqFkiMd9W0yWOXgXWG2YgFqu3gT4KQV2DyQIDAQAB";

    // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = " http://68tifi.natappfree.cc/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = " http://68tifi.natappfree.cc/return_url";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     *
     * @param sWord
     *            要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis() + ".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
