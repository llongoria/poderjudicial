### PoderJudicial-21 1.0.1 20240924 Primera Version ingresada a produccion.
1. Version funcional en operacion en servidor: https://api.wcontact.loc:8443/PoderJudicial-21/

### PoderJudicial-21 1.0.2 20240925 
1. Se modifica SimpleTextForma para ser trasient.
2. Se agregan al archivo pom.xml las librerias de opensearch.
3. Se agrega la conexion de opensearch a los indeces pj-bulletin-me y pj-httpquery.
4. Se agrega la escritura de archivos en caso de falla en la ruta: /home/llongoria/PoderJudicial/BulletinError

### PoderJudicial-21 1.0.3 20240926
1. Se corrige el error:

```
2024-09-26 04:10:06,362 ERROR [mx.com.wcontact.poderjudicial.bl.HttpQueryBL] (EJB default - 1) ***** BulletinTimer|execute| Falla al enviar los datos a Opensearch *****: jakarta.json.JsonException: Jackson exception
        at deployment.PoderJudicial-21.war//mx.com.wcontact.poderjudicial.opensearch.bl.WCOpenSearchClient.createDocument(WCOpenSearchClient.java:50)
        at deployment.PoderJudicial-21.war//mx.com.wcontact.poderjudicial.opensearch.bl.HttpQueryOS.createDocument(HttpQueryOS.java:57)
        at deployment.PoderJudicial-21.war//mx.com.wcontact.poderjudicial.bl.HttpQueryBL.save(HttpQueryBL.java:44)
        at deployment.PoderJudicial-21.war//mx.com.wcontact.poderjudicial.bl.BulletinBL.runQuery(BulletinBL.java:91)
        at deployment.PoderJudicial-21.war//mx.com.wcontact.poderjudicial.timer.BulletinTimer.execute(BulletinTimer.java:60)
        at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
Caused by: com.fasterxml.jackson.databind.JsonMappingException: date must not be null (through reference chain: mx.com.wcontact.poderjudicial.entity.HttpQuery["rowCreated"])
Caused by: java.lang.NullPointerException: date must not be null
```

