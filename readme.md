# 📚 BiblioCanto — Sistema de Gestão de Biblioteca

O **BiblioCanto** é uma API RESTful corporativa desenvolvida para automatizar e gerenciar o fluxo operacional de bibliotecas. O sistema controla o acervo bibliográfico, o cadastro de visitantes e o ciclo de vida completo dos empréstimos de livros, aplicando regras de negócio rigorosas para garantir a integridade dos dados e do acervo físico.

A arquitetura do projeto foi desenhada sob os princípios de **separação de responsabilidades (Clean Architecture)**, dividindo a aplicação em camadas bem definidas:
* **Controllers:** Gerenciamento das requisições HTTP e validação de entrada.
* **Services:** Centralização das regras de negócio, transações e mapeamento de objetos.
* **DAOs (Data Access Objects):** Isolamento total do acesso aos dados e execução de consultas via JPA/Hibernate.
* **DTOs (Data Transfer Objects):** Blindagem do banco de dados, garantindo que apenas dados validados trafeguem entre a API e os clientes.

---

## 🛠️ Tecnologias, Frameworks e Bibliotecas

O projeto foi construído utilizando o ecossistema mais moderno e robusto do desenvolvimento Java atual. Abaixo detalhamos cada dependência e sua função específica no ecossistema do BiblioCanto:

### 1. Java 21 (LTS)
* **Para que serve:** É a linguagem de programação base e o tempo de execução (Runtime) da aplicação.
* **Por que foi utilizado:** A versão 21 é uma versão de Suporte de Longo Prazo (LTS) que oferece alta performance, segurança avançada, otimização no consumo de memória e recursos modernos da linguagem (como *Pattern Matching* e melhorias em coleções e datas com a API `java.time`).

### 2. Spring Boot (v3.x)
* **Para que serve:** Framework principal que atua como o esqueleto de toda a aplicação.
* **Por que foi utilizado:** Elimina a necessidade de configurações manuais complexas de servidores (configuração sobre convenção). Ele já embuti o servidor web (Tomcat), gerencia a injeção de dependências (`@Autowired`, `@Service`, `@Repository`) e facilita a inicialização do projeto.

### 3. Spring Web (Spring MVC)
* **Para que serve:** Módulo do Spring responsável por criar a API RESTful e gerenciar a comunicação na web.
* **Por que foi utilizado:** Permite transformar classes Java em endpoints HTTP acessíveis (usando anotações como `@RestController`, `@GetMapping`, `@PostMapping`). Ele recebe os pedidos do front-end (ou Postman), roteia para o Controller correto e devolve as respostas no formato JSON com os códigos HTTP adequados (`200 OK`, `201 Created`, `404 Not Found`).

### 4. Spring Data JPA & Hibernate
* **Para que serve:** Camada de persistência e mapeamento objeto-relacional (ORM - *Object-Relational Mapping*).
* **Por que foi utilizado:** O **Hibernate** traduz as classes Java (`@Entity`) em tabelas do banco de dados e os objetos em linhas de registro, eliminando a necessidade de escrever SQL manual para operações comuns. O **Spring Data JPA** gerencia o `EntityManager` e o ciclo de vida das transações no banco (através da anotação `@Transactional`), garantindo que se um erro ocorrer no meio de um empréstimo, nenhuma alteração parcial seja salva no banco.

### 5. Lombok
* **Para que serve:** Biblioteca de tempo de compilação que automatiza a geração de código repetitivo (*boilerplate*).
* **Por que foi utilizado:** Mantém o código das Entidades e DTOs extremamente limpo e legível. Com simples anotações como `@Data`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor` e `@Builder`, o Lombok cria internamente todos os métodos get/set, construtores, métodos de comparação (`equals` e `hashCode`) e o padrão de projeto *Builder* sem poluir os arquivos `.java`.

### 6. Jakarta Validation (Bean Validation)
* **Para que serve:** Motor de validação de dados de entrada.
* **Por que foi utilizado:** Garante que dados incorretos, maliciosos ou incompletos nunca cheguem à camada de serviço ou ao banco de dados. Utilizamos anotações diretamente nos DTOs (como `@NotNull`, `@NotBlank`, `@Positive`, `@PastOrPresent`) para validar CPFs, ISBNs, datas de devolução e campos obrigatórios de forma automática no momento em que a requisição atinge o Controller (`@Valid`).

### 7. Jackson (com.fasterxml.jackson)
* **Para que serve:** Biblioteca de serialização e desserialização de JSON.
* **Por que foi utilizado:** É o motor interno que converte o texto JSON enviado na requisição HTTP em objetos Java (DTOs) e converte as respostas Java de volta em JSON para o cliente. Utilizamos anotações como `@JsonProperty` para controlar exatamente como os nomes dos campos devem aparecer e ser lidos (tratando sensibilidade de letras maiúsculas e minúsculas).

### 8. Driver JDBC (H2 Database / PostgreSQL / MySQL)
* **Para que serve:** Driver de comunicação com o banco de dados relacional escolhido.
* **Por que foi utilizado:** Permite que a JVM (Máquina Virtual Java) e o Hibernate se conectem fisicamente ao banco de dados SQL para ler e gravar as tabelas de `livros`, `exemplares_livro`, `emprestimos` e `usuarios`.

---

## 🗄️ Modelo e Relacionamentos do Banco de Dados

O banco de dados segue regras de cardinalidade estritas para respeitar o acervo físico:

* **Livro -> Exemplares (1:N):** Um registro de catálogo de livro (identificado pelo seu ISBN único) pode possuir múltiplos exemplares físicos com códigos UUID exclusivos.
* **Exemplar -> Situação (1:1):** Cada exemplar possui estritamente um estado de situação em tempo real (`DISPONIVEL`, `EMPRESTADO`, `INDISPONIVEL`).
* **Exemplar -> Empréstimo (1:1):** Para manter o controle estrito do acervo atual, cada exemplar físico transaciona em uma relação exclusiva de 1 para 1 com seu empréstimo ativo.
* **Usuário -> Empréstimo (1:N):** Um visitante ou leitor cadastrado pode acumular um histórico de vários empréstimos realizados ao longo do tempo.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java Development Kit (JDK) 21** instalado.
* **Apache Maven** (ou o *wrapper* `./mvnw` incluso no projeto).
* Banco de dados SQL configurado (ou H2 em memória padrão no `application.properties`).

### Passos para rodar
1. Clone o repositório para a sua máquina local:
   ```bash
   git clone [https://github.com/seu-usuario/bibliocanto.git](https://github.com/seu-usuario/bibliocanto.git)