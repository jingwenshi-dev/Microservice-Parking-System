package ca.mcmaster.cas735.acmepark.payment.adapter;

import ca.mcmaster.cas735.acmepark.payment.dto.PaymentRequest;
import ca.mcmaster.cas735.acmepark.payment.ports.provided.PaymentServicePort;
import com.fasterxml.jackson.databind.ObjectMapper; // 用于将 JSON 字符串与 Java 对象之间的转换
import lombok.extern.slf4j.Slf4j; // 使用 Lombok 提供的日志注解，简化日志记录
import org.springframework.amqp.rabbit.annotation.Exchange; // 用于声明 RabbitMQ 交换机
import org.springframework.amqp.rabbit.annotation.Queue; // 用于声明 RabbitMQ 队列
import org.springframework.amqp.rabbit.annotation.QueueBinding; // 用于绑定 RabbitMQ 交换机和队列
import org.springframework.amqp.rabbit.annotation.RabbitListener; // 用于声明 RabbitMQ 消息监听器
import org.springframework.beans.factory.annotation.Autowired; // 自动注入 Spring Bean
import org.springframework.stereotype.Service; // 将该类标记为 Spring 服务组件

// 使用 @Service 注解将该类声明为 Spring 服务，@Slf4j 启用日志功能
@Service
@Slf4j
public class PermitPaymentListener {

    // 引入 PaymentServicePort 依赖，用于支付逻辑处理
    private final PaymentServicePort paymentService;

    // 通过构造函数注入 PaymentServicePort
    @Autowired
    public PermitPaymentListener(PaymentServicePort paymentService) {
        this.paymentService = paymentService;
    }

    // RabbitMQ 消息监听器，监听特定队列中的消息
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "permit.payment.queue", durable = "true"), // 定义队列名称为 "permit.payment.queue"，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic}", ignoreDeclarationExceptions = "true", type = "topic"), // 定义交换机的名称，使用占位符从配置中读取，并设置交换机类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listen(String data) {
        // 记录日志，显示接收到的支付请求数据
        log.debug("Received payment request: {}", data);
        // 将接收到的 JSON 字符串转换为 PaymentRequest 对象
        PaymentRequest paymentRequest = translate(data);

        // 根据 userType 选择支付策略并处理支付
        switch (paymentRequest.getUserType().toLowerCase()) {
            case "student":
                log.info("Processing payment for student with license plate: {}", paymentRequest.getLicensePlate());
                paymentService.processPayment(paymentRequest);
                break;
            case "staff":
                log.info("Processing payment for faculty with license plate: {}", paymentRequest.getLicensePlate());
                paymentService.processPayment(paymentRequest);
                break;
            case "visitor":
                log.info("Processing payment for staff with license plate: {}", paymentRequest.getLicensePlate());
                paymentService.processPayment(paymentRequest);
                break;
            default:
                // 如果 userType 不是已知的类型，抛出异常
                throw new IllegalArgumentException("Unknown user type: " + paymentRequest.getUserType());
        }
    }

    // 将接收到的原始 JSON 字符串转换为 PaymentRequest 对象
    private PaymentRequest translate(String raw) {
        // 创建 Jackson 的 ObjectMapper 实例
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 使用 ObjectMapper 将 JSON 字符串解析为 PaymentRequest 对象
            return mapper.readValue(raw, PaymentRequest.class);
        } catch (Exception e) {
            // 如果转换失败，记录错误日志并抛出运行时异常
            log.error("Failed to parse payment request: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
