package ca.mcmaster.cas735.acmepark.visitor_access.adapter;

import ca.mcmaster.cas735.acmepark.visitor_access.ports.provided.GateInteractionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 监听 Gate 服务相关响应的消息
 */
@Service
@Slf4j
public class AMQPGateMessageListener {

    // 注入 VisitorService 以处理业务逻辑
    private final GateInteractionHandler gateInteractionServiceImpl;

    @Autowired
    public AMQPGateMessageListener(GateInteractionHandler gateInteractionServiceImpl) {
        this.gateInteractionServiceImpl = gateInteractionServiceImpl;
    }

    // 监听 Gate 服务返回的进入请求的开门结果队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "gateEntryResponseQueue", durable = "ture"), // 定义队列名称为 gateEntryResponseQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-entry-request-exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForGateEntryRequest(String data) {
        log.debug("接收到 Gate 服务的进入响应: {}", data);
        // 调用 VisitorService 来处理 Gate 返回的结果
        gateInteractionServiceImpl.handleGateEntryRequest(data);
    }

    // 监听 Gate 服务返回的离开请求的开门结果队列
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "gateExitResponseQueue", durable = "true"), // 定义队列名称为 gateExitResponseQueue，并设置为持久化
            exchange = @Exchange(value = "${app.custom.messaging.visitor-to-gate-exit-response-exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"), // 绑定到交换机，使用配置中的名称，类型为 topic
            key = "*")) // 路由键设置为 "*"，表示匹配任意路由键
    public void listenForGateExitRequest(String data) {
        log.debug("接收到 Gate 服务的离开响应: {}", data);
        // 调用 VisitorService 来处理 Gate 返回的结果
        gateInteractionServiceImpl.handleGateExitRequest(data);
    }
}
