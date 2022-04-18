<h1 align="center">Reactive Demo Webflux</h1>

## Descrição
<p align="center">Escrever uma breve descrição</p>

### Pré requisitos
- Gradle 7.4.2
- JDK 17
- Kotlin 1.6.10
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Git Flow
1. Criar uma branch a partir da master
2. Codificar o que precisa ser alterado
3. Abrir um PR e aguardar uma aprovacao para mergear
4. Após mergeado na master, gerar uma tag seguindo versionamento semantico, para saber mais acesse o [link](https://imasters.com.br/codigo/versionamento-semantico-o-que-e-e-como-usar)

### Executar o projeto

1. Execute o comando abaixo na raiz do projeto, ele criará uma instancia o banco MongoDB e do Rabbitmq no docker da máquina.
```
docker-compose up -d
```

#### Intellij
1. Adicione as seguintes váriaveis de ambiente:

```
   MONGO_STAGING_USERNAME=admin
   MONGO_STAGING_PASSWORD=admin
   RABBITMQ_STAGING_USERNAME=admin
   RABBITMQ_STAGING_PASSWORD=admin
```

2. Adicione o seguinte comando no VMOptions

```
-Dspring.profiles.active=staging
```

###URL Local
```
http://localhost:8080/
```