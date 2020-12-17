# mtool
> 节点管理工具可以帮助节点管理质押， 治理， 投票操作， 也可以抓取数据分析生成委托人奖励分配文件并实行分配操作。
## 软件架构

- [架构文档.md](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/架构文档.md)
- [命令操作手册](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/命令操作手册.txt)
- [新版本开发与发布流程](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/新版本开发与发布流程.md)
- [快速构建区块链测试节点](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/platon-deploy/README.md)

## 使用技术

- gradle
- jcommander
- junit
- mockito
- spring,springboot
- mybatis
- logback
- client-sdk(web3j)

## 项目结构

![项目结构拆分.png](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/images/项目结构拆分.png)

- mtool-client：客户端代码
- mtool-server：服务端代码
- common：通用工具封装
- distribute：打包发布
- mbgenerator-plugin：mybatis生成器定制插件
- platon-deploy：节点快速部署工具
- doc：文档说明



## 使用说明

## mtool-client（命令行工具）

#### mtool-client流程图

![mtool-client组件图.png](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/images/mtool-client组件图.png)

#### mtool-client编译运行
```bash
./gradlew :mtool-client:run --args="--help" // args填入命令行参数
```

---

## mtool-server（服务端）

#### mtool-server奖励分配流程图

![奖励计算.png](http://192.168.9.66/Juzix-Platon/mtool/raw/develop/docs/images/奖励计算.png)

#### 修改配置 `application.yml`
```bash
...
platon:
  upload: data/upload/
  settlePeriod: 300 #结算周期块数
  increasePeriod: 15759500  #增发周期块数
  blockRewardPercent: 0.7 #增发总额中出块奖励所占比例
```

#### mtool-server服务端启动

```bash
./gradlew :mtool-server:bootRun
```

---

## 打包
> 执行打包命令后会在`./distribution/build/mtool/`下生成mtool-client和mtool-server两个zip包。
```bash
# 打包mtool-client和mtool-server
./gradlew clean release
# 在打包客户端和服务端的基础上， 合并打包mtool-all.zip汇总包（只支持linux）
./gradlew clean releaseAllInOne
```
> 解压执行：
```bash
unzip distribution/build/mtool/mtool-client-0.7.1.zip -d distribution/build/mtool/
unzip distribution/build/mtool/mtool-server-0.7.1.zip -d distribution/build/mtool/
```
> 执行解压后目录对应的 `bin/xxx` 可执行文件就能使用对应程序

## 日志
> 默认日志运行级别为`INFO`，只打印跟mtool相关的日志，当需要充分了解sdk与节点的请求情况需要配置环境变量

## 代码质量统计报告上传
> gradlew clean build jacocoTestReport sonarqube -x check -info

```bash
# linux
JAVA_OPTS="-Dlog.level=debug" ./bin/mtool-client [options] [command] [command options]
# windows
set JAVA_OPTS="-Dlog.level=debug"
bin/mtool-client.bat [options] [command] [command options]
```




## SonarQube
```bash
./gradlew clean build jacocoTestReport sonarqube -x check -info
```

## 维护
### 检查可升级依赖包
```bash
./gradlew dependencyUpdates -Drevision=release
```

### 包安全检查
```bash
./gradlew dependencyCheckAnalyze
./gradlew dependencyCheckAggregate
```
