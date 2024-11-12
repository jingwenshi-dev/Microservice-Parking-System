package ca.mcmaster.cas735.acmepark.visitor_access.adapter;


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
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "visitorEntryQueue", durable = "true"), // 定义队列名称为 visitorEntryQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-entry-request-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForVisitorEntry(String data) {
        log.debug("接收到访客进入请求: {}", data);
        visitorService.handleVisitorEntry(data); // 调用业务逻辑处理访客进入
    }

    // 监听访客退出的队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "visitorExitQueue", durable = "true"), // 定义队列名称为 visitorExitQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-exit-request-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForVisitorExit(String data) {
        log.debug("接收到访客退出请求: {}", data);
        visitorService.handleVisitorExit(data); // 调用业务逻辑处理访客退出
    }

    // 监听 Gate 服务返回的进入请求的开门结果队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gateEntryResponseQueue", durable = "true"), // 定义队列名称为 gateEntryResponseQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-to-gate-entry-response-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForGateEntryResponse(String data) {
        log.debug("接收到 Gate 服务的进入响应: {}", data);
        // 调用 VisitorService 来处理 Gate 返回的结果
        visitorService.handleGateEntryResponse(data);
    }

    // 监听 Gate 服务返回的离开请求的开门结果队列
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gateExitResponseQueue", durable = "true"), // 定义队列名称为 gateExitResponseQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-to-gate-exit-response-exchange}", ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForGateExitResponse(String data) {
        log.debug("接收到 Gate 服务的离开响应: {}", data);
        // 调用 VisitorService 来处理 Gate 返回的结果
        visitorService.handleGateExitResponse(data);
    }
}
