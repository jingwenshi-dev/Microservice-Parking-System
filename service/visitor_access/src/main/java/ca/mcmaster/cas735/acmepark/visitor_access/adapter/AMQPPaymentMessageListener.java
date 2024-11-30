package ca.mcmaster.cas735.acmepark.visitor_access.adapter;


import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.PaymentInteractionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 监听用户相关请求的消息
 */
@Service
@Slf4j
public class AMQPPaymentMessageListener {

    // 注入 VisitorService 以处理业务逻辑
    private final PaymentInteractionHandler paymentInteractionService;

    @Autowired
    public AMQPPaymentMessageListener(PaymentInteractionHandler paymentInteractionService) {
        this.paymentInteractionService = paymentInteractionService;
    }


    // 监听交易返回的数据
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "PaymentToVisitorExitQueue", durable = "true"), // 定义队列名称为 visitorExitQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.payment-response-visitor-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenPaymentForVisitorExit(String data) {
        log.debug("接收到交易返回的结果: {}", data);
        paymentInteractionService.handlePaymentResult(data); // 调用业务逻辑处理访客退出
    }


}