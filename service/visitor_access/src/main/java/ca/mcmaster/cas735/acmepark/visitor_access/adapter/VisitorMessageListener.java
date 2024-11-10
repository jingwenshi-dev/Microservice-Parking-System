package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.domain.PaymentRequest;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VisitorMessageListener {

    // 注入 VisitorService 以处理业务逻辑
    private final VisitorService visitorService;

    @Autowired
    public VisitorMessageListener(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    // 监听访客进入的队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "visitorEntryQueue", durable = "true"), // 定义队列名称为 visitorEntryQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForVisitorEntry(String data) {
        log.debug("接收到访客进入请求: {}", data);
        String qrCode = visitorService.translate(data); // 将数据转换为 QR 码
        visitorService.handleVisitorEntry(qrCode); // 调用业务逻辑处理访客进入
    }

    // 监听访客退出的队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "visitorExitQueue", durable = "true"), // 定义队列名称为 visitorExitQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForVisitorExit(String data) {
        log.debug("接收到访客退出请求: {}", data);
        String qrCode = visitorService.translate(data); // 将数据转换为 QR 码
        visitorService.handleVisitorExit(qrCode); // 调用业务逻辑处理访客退出
    }

    // 监听支付更新的队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "visitorPaymentQueue", durable = "true"), // 定义队列名称为 visitorPaymentQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForPaymentUpdate(String data) {
        log.debug("接收到支付更新: {}", data);
        PaymentRequest paymentRequest = visitorService.translatePayment(data); // 将数据转换为 PaymentRequest 对象
        visitorService.processVisitorPayment(paymentRequest.getQrCode(), paymentRequest.isPaymentStatus()); // 调用业务逻辑处理支付更新
    }
}
