# Guia de Laboratório 3: Testes Unitários com Mocking de Dependências

## Lab 3.1: Gestão de Funcionários

### a) Exemplos de Asserções
```java
// Verificar que o funcionário encontrado não é nulo e tem o nome esperado
assertThat(found).isNotNull().extracting(Employee::getName).isEqualTo(persistedAlex.getName());

// Verificar que a lista de todos os funcionários tem 3 itens e contém os nomes esperados
assertThat(allEmployees).hasSize(3).extracting(Employee::getName)
    .contains(alex.getName(), john.getName(), bob.getName());
```

### b) Componentes da Anotação DataJpaTest
A anotação `@DataJpaTest` é composta pelas seguintes anotações:
```java
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@BootstrapWith(org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTestContextBootstrapper.class)
@ExtendWith(org.springframework.test.context.junit.jupiter.SpringExtension.class)
@OverrideAutoConfiguration(enabled=false)
@TypeExcludeFilters(DataJpaTypeExcludeFilter.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
```

### c) Declaração de Repositório Mock
Quando declaramos:
```java
@Mock
private EmployeeRepository employeeRepository;
```
Estamos a criar uma versão simulada do `EmployeeRepository` que nos permite controlar o seu comportamento no teste sem conectar a uma base de dados real.

### d) Diferença Entre @Mock e @MockBean
- **@Mock**: Utilizado em testes unitários puros para simular dependências sem carregar o contexto do Spring. Ideal para testes rápidos e isolados.
- **@MockBean**: Utilizado em testes de integração quando precisamos substituir beans específicos no contexto do Spring. Comummente usado com `@SpringBootTest` ou `@WebMvcTest`.

### e) Configuração de Conexão à Base de Dados
O ficheiro contém detalhes de conexão à base de dados, mas a linha que permite esta conexão está comentada:
```java
// @TestPropertySource(locations = "application-integrationtest.properties")
```

### f) Diferenças nas Abordagens de Teste
Nas diferentes abordagens de teste:

| Abordagem | Carregamento da Aplicação | Método de Conexão |
|-----------|---------------------------|-------------------|
| C | Parcial (apenas Controlador) | MockMvc simula outros componentes |
| D | Completo | MockMvc com servlet de teste especial |
| E | Completo | TestRestTemplate (cliente REST) |

## Lab 3.3: Considerações sobre Testes com Base de Dados Real

### Vantagens de Usar uma Base de Dados Real nos Testes
1. **Testes Mais Realistas** - Testa o comportamento da aplicação num ambiente mais próximo do real
2. **Deteção de Problemas de Integração** - Identifica erros que só surgem ao interagir com a base de dados
3. **Validação de Consultas Complexas** - Testa SQLs complexos, índices e otimizações de performance
4. **Verificação da Consistência dos Dados** - Valida a integridade e armazenamento dos dados

### Desvantagens de Usar uma Base de Dados Real nos Testes
1. **Execução Mais Lenta** - O acesso a uma base de dados real é mais lento do que usar mocks ou bases de dados em memória
2. **Maior Complexidade na Configuração** - Requer mais trabalho de configuração para testes
3. **Dependência de Dados Externos** - Os testes podem falhar devido a alterações nos dados ou na estrutura
4. **Necessidade de Limpeza dos Dados** - É preciso garantir que os testes não deixam resíduos na base de dados