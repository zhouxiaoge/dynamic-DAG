## 添加Metrics
- 新增依赖
```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
- 新增配置
```yaml
management:
  endpoint:
    health:
      show-details: always #http://127.0.0.1:8080/actuator/health
  endpoints:
    web:
      exposure:
        include: '*'
```
- 代码配置
```java

/**
 * 访问地址http://127.0.0.1:8080/actuator/health
 */
@GetMapping("/kafka")
@Timed(value = "all.kafka", longTask = true)
public void syncDag() {
    ...
}
```
- 结果
```json5
{
  "name": "all.people",
  "description": null,
  "baseUnit": "seconds",
  "measurements": [
    {
      "statistic": "ACTIVE_TASKS",
      "value": 1.0
    },
    {
      "statistic": "DURATION",
      "value": 7.015722359E11
    }
  ],
  "availableTags": [
    {
      "tag": "method",
      "values": [
        "GET"
      ]
    },
    {
      "tag": "uri",
      "values": [
        "/kafka"
      ]
    }
  ]
}
```