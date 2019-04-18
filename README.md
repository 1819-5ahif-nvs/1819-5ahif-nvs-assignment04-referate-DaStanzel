# Basic Auth & OAuth2
## Basic Auth
Bei Basic Auth wird bei jeder request im Header der Username und das Passwort mitgegeben. Diese Security-Methode wurde vom HTTP-Protokoll bereitgestellt ist jedoch nicht sehr sicher. 
Der Ablauf der Requests läuft wie in der Graphik beschrieben.

![Requests](https://github.com/1819-5ahif-nvs/1819-5ahif-nvs-assignment04-referate-DaStanzel/tree/master/IMG/basicauth1.png)

Falls dem Zugriff nicht stattgegeben wird muss bekommt man als Responsecode den Code 401 ansonsten 200. Dies sieht man auch nochmals in der Graphik.

![Response](https://github.com/1819-5ahif-nvs/1819-5ahif-nvs-assignment04-referate-DaStanzel/tree/master/IMG/basicauth.png)

## OAuth2
Bei OAuth gibt es einen Resource Owner, welcher für die Verwaltung der Rechte verfügbar ist. Anhand des Beispiels meiner Diplomarbeit wäre das ich als Person, da ich den Zugriffen auf die Streampy API stattgebe oder nicht.
Der Resource Owner vergibt einen Client_Key und ein Client_Secret, welches eindeutig sein muss. Der Client (App, Web-Platform,...) holt sich vom Authorization Server einen Access Token. Mit diesem Token kann der User die Ressourcen abgreifen, die er sich vom Resource Server mit REST-Requests holt.

In der Graphik wird beschrieben wie OAuth funktioniert.
![OAUTH](https://github.com/1819-5ahif-nvs/1819-5ahif-nvs-assignment04-referate-DaStanzel/tree/master/IMG/Oauth.png)

In unserem Fall nehmen wir als Authorization Server Keycloak, welches einen fertigen Authorization Server bietet.
In diesem Fall zeigt die Graphik den Weg wie wir als Client und Server mit Keycloak kommunizieren.
![Communication](https://github.com/1819-5ahif-nvs/1819-5ahif-nvs-assignment04-referate-DaStanzel/tree/master/IMG/oauth1.png)

### Wie arbeitet man mit Keycloak in Java
Im File ![docker_turtorial_keycloak.md](https://github.com/1819-5ahif-nvs/1819-5ahif-nvs-assignment04-referate-DaStanzel/blob/master/docker_turtorial_keycloak.md) wird beschrieben wie man den Keycloak Server mit Hilfe von Docker aufsetzt.

In Java muss man in der web.xml einige dinge eintragen:
```xml
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Application</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>

    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK</auth-method>
        <realm-name>user</realm-name>
    </login-config>

    <security-role>
        <role-name>user</role-name>
    </security-role>
```

Und mann muss sich eine keycloak.json Datei schreiben in der man folgende Dinge einträgt.

```
{
  "realm": "user",
  "bearer-only": true,
  "auth-server-url": "http://localhost:8180/auth",
  "ssl-required": "external",
  "resource": "DEINE RESSOURCE",
  "principal-attribute": "USERNAME"
}
```
Das letzte was zu tun ist ist, uns einen AccessTokenProducer zu Schreiben. Dieser ist sehr simpel. Im unten gezeigten Codesample findet ihr einen fertigen TokenProducer.


```
@RequestScoped
public class AccessTokenProducer {

    @Inject
    private HttpServletRequest request;

    @Produces
    public AccessToken getAccessToken() {
        return ((KeycloakPrincipal) request
                .getUserPrincipal())
                .getKeycloakSecurityContext()
                .getToken();
    }
}

```
