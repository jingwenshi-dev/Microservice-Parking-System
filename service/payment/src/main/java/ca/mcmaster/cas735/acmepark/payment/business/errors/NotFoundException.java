package ca.mcmaster.cas735.acmepark.payment.business.errors;

public class NotFoundException extends Exception {

    // 实体名称
    private final String entityName;
    // 查找失败的标识符
    private final String identifier;
    // 标识符类型
    private final String identifierType;

    // 构造函数
    public NotFoundException(String entityName, String identifier, String identifierType) {
        super(String.format("%s with %s [%s] not found.", entityName, identifierType, identifier));
        this.entityName = entityName;
        this.identifier = identifier;
        this.identifierType = identifierType;
    }

    // 获取实体名称
    public String getEntityName() {
        return entityName;
    }

    // 获取标识符
    public String getIdentifier() {
        return identifier;
    }

    // 获取标识符类型
    public String getIdentifierType() {
        return identifierType;
    }
}