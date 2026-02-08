# Подготовка test plan для JMeter

+ Установить [yq](https://github.com/mikefarah/yq) либо другой подобный инструмент
+ Установить [OpenAPI Generator](https://openapi-generator.tech/docs/installation) (опционально)
+ Загрузить документацию OpenAPI в формате YAML: http://library.local/api/v3/api-docs.yaml Файл сохранить в каталог
[jmeter](../jmeter)
+ Удалить из документации теги:  
```shell
cd jmeter
yq 'del(.tags) | del(.paths[][].tags)' api-docs.yaml > api-docs_no_tags.yaml
```
+ Сгенерировать JMX (выполнять в каталоге [jmeter](../jmeter))
```shell
java -jar openapi-generator-cli.jar generate -i api-docs_no_tags.yaml -g jmeter
```
Либо используя плагин maven (выполнять из корневого каталога проекта либо из IDE)  
```shell
mvn openapi-generator:generate -P jmeter-test-plan
```
Результат должен быть таким:  
```shell
ls -l
итого 144
-rw-r--r-- 1 valheru valheru    468 фев  7 21:03 DefaultApi.csv
-rw-r--r-- 1 valheru valheru 140623 фев  7 21:03 DefaultApi.jmx

```

Если из yml не удалить теги, то будет сгенерировано несколько jmx по тегам.  
Можно использовать, если требуется тестировать только определённую группу эндпоинтов
(например, только security или service), в остальных случаях так делать не рекомендуется, так как один эндпоинт
с несколькими тегами окажется во всех соответствующих планах.
```shell
ls -l
итого 244
-rw-r--r-- 1 valheru valheru   169 фев  7 22:20 AuthorApi.csv
-rw-r--r-- 1 valheru valheru 34340 фев  7 22:20 AuthorApi.jmx
-rw-r--r-- 1 valheru valheru   106 фев  7 22:20 BookApi.csv
-rw-r--r-- 1 valheru valheru 31444 фев  7 22:20 BookApi.jmx
-rw-r--r-- 1 valheru valheru   133 фев  7 22:20 CycleApi.csv
-rw-r--r-- 1 valheru valheru 32749 фев  7 22:20 CycleApi.jmx
-rw-r--r-- 1 valheru valheru    82 фев  7 22:20 SecurityApi.csv
-rw-r--r-- 1 valheru valheru 26386 фев  7 22:20 SecurityApi.jmx
-rw-r--r-- 1 valheru valheru   118 фев  7 22:20 SeriesApi.csv
-rw-r--r-- 1 valheru valheru 31489 фев  7 22:20 SeriesApi.jmx
-rw-r--r-- 1 valheru valheru   251 фев  7 22:20 ServiceApi.csv
-rw-r--r-- 1 valheru valheru 60113 фев  7 22:20 ServiceApi.jmx
```
