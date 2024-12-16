## ER-диаграмма

```mermaid
erDiagram
    CLIENT ||--o{ INVESTMENT : "делает"
    SECURITY ||--o{ INVESTMENT : "используется в"

    CLIENT {
        int client_id PK
        string name
        string ownership_type
        string address
        string phone
    }

    SECURITY {
        int security_id PK
        string name
        string type
        decimal current_price
        decimal expected_return
    }

    INVESTMENT {
        int investment_id PK
        int client_id FK
        int security_id FK
        date investment_date
        decimal amount
        decimal expected_profit
    }
```

## Описание таблиц

1. **CLIENT (Клиент)**:
   - client_id: уникальный идентификатор клиента (первичный ключ)
   - name: название предприятия
   - ownership_type: вид собственности
   - address: адрес предприятия
   - phone: телефон предприятия

2. **SECURITY (Ценная бумага)**:
   - security_id: уникальный идентификатор ценной бумаги (первичный ключ)
   - name: название ценной бумаги
   - type: тип ценной бумаги (акция, облигация и т.д.)
   - current_price: текущая цена
   - expected_return: ожидаемая доходность

3. **INVESTMENT (Инвестиция)**:
   - investment_id: уникальный идентификатор инвестиции (первичный ключ)
   - client_id: внешний ключ, связывающий с таблицей CLIENT
   - security_id: внешний ключ, связывающий с таблицей SECURITY
   - investment_date: дата инвестиции
   - amount: сумма инвестиции
   - expected_profit: ожидаемая прибыль

## Выбранная таблица для дальнейшей работы:
**Таблица SECURITY - Ценная бумага**

## Диаграмма классов
```mermaid
classDiagram
    class ISecurity {
        <<interface>>
        +getSecurityId() int
        +getName() String
        +getType() String
        +getCurrentPrice() BigDecimal
    }

    class BriefSecurity {
        #int securityId
        #String name
        #String type
        +BriefSecurity()
        +BriefSecurity(int, String, String)
        +setSecurityId(int)
        +setName(String)
        +setType(String)
        +getCurrentPrice() BigDecimal
        +toString() String
    }

    class Security {
        -BigDecimal currentPrice
        -BigDecimal expectedReturn
        -Security(Builder)
        +getCurrentPrice() BigDecimal
        +getExpectedReturn() BigDecimal
        +static createNewSecurity(String, String, BigDecimal, BigDecimal) Security
        +static updateExistingSecurity(int, String, String, BigDecimal, BigDecimal) Security
        +static createFromString(String) Security
        +static createFromJson(String) Security
        +toJson() String
        +toString() String
        +equals(Object) boolean
        +hashCode() int
        +isSameBriefSecurity(BriefSecurity) boolean
    }

    class SecurityValidator {
        +static validateSecurityId(int)
        +static validateName(String)
        +static validateType(String)
        +static validatePrice(BigDecimal)
        +static validateExpectedReturn(BigDecimal)
        +static validateSecurity(Security)
    }

    ISecurity <|.. BriefSecurity : implements
    BriefSecurity <|-- Security : extends
    Security ..> SecurityValidator : uses
    BriefSecurity ..> SecurityValidator : uses
```

## Структура проекта
```mermaid
classDiagram
    class ISecurityRepository {
        <<interface>>
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
    }

    class AbstractSecurityRepository {
        <<abstract>>
        #Securitys: List[Security]
        #filename: str
        #serializationStrategy: SerializationStrategy
        +AbstractSecurityRepository(filename: str, strategy: SerializationStrategy)
        +readFromFile()
        +writeToFile()
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
        #generateNewId(): int
    }

    class SecurityRepository {
        +SecurityRepository(filename: str, strategy: SerializationStrategy)
    }

    class SecurityRepositoryAdapter {
        -fileRepository: AbstractSecurityRepository
        +SecurityRepositoryAdapter(fileRepository: AbstractSecurityRepository)
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
    }

    class SerializationStrategy {
        <<interface>>
        +readFromFile(filename: str): List[Security]
        +writeToFile(filename: str, Securitys: List[Security])
    }

    class AbstractSerializationStrategy {
        <<abstract>>
        #objectMapper: Any
        +readFromFile(filename: str): List[Security]
        +writeToFile(filename: str, Securitys: List[Security])
        #createObjectMapper()*: Any
    }

    class JsonSerializationStrategy {
        #read_data(file): List[dict]
        #write_data(file, data: List[dict])
    }

    class YamlSerializationStrategy {
        #read_data(file): List[dict]
        #write_data(file, data: List[dict])
    }

    class Security_rep_DB {
        -connection: PostgreSQLConnection
        +Security_rep_DB()
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
    }

    class PostgreSQLConnection {
        -instance: PostgreSQLConnection
        -connection: Connection
        -PostgreSQLConnection()
        +getInstance(): PostgreSQLConnection
        +getConnection(): Connection
        +close()
    }

    AbstractSecurityRepository <|-- SecurityRepository : extends
    ISecurityRepository <|.. SecurityRepositoryAdapter : implements
    SecurityRepositoryAdapter --> AbstractSecurityRepository : uses
    AbstractSecurityRepository --> SerializationStrategy : uses
    SerializationStrategy <|.. AbstractSerializationStrategy : implements
    AbstractSerializationStrategy <|-- JsonSerializationStrategy : extends
    AbstractSerializationStrategy <|-- YamlSerializationStrategy : extends
    ISecurityRepository <|.. Security_rep_DB : implements
    Security_rep_DB --> PostgreSQLConnection : uses
```

## Диаграмма взаимодействия компонентов
```mermaid
sequenceDiagram
    participant User
    participant Main as Main:main()
    participant MainController as MainController:controller
    participant MainWindow as MainWindow:main_window
    participant Repository as Repository:repository_adapter
    participant AddEditController as AddEditController:controller
    participant AddEditWindow as AddEditWindow:add_window/edit_window
    participant DetailsController as DetailsController:controller
    participant DetailsWindow as DetailsWindow:details_window
    
    %% Инициализация приложения
    rect rgb(200, 220, 240)
    Note over User,DetailsWindow: Инициализация приложения
    User->>+Main: main()
    Main->>+Repository: createFileRepository()
    Note right of Repository: FileRepository + Adapter<br/>или PostgreSQLRepository
    Repository-->>-Main: return repository
    Main->>+MainController: MainController(repository)
    MainController->>MainWindow: MainWindow()
    MainWindow-->>MainController: return window
    MainController->>Repository: add_observer(self)
    MainController->>MainController: initializePagination()
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: display UI
    end

    %% Добавление ценной бумаги
    rect rgb(220, 240, 220)
    Note over User,DetailsWindow: Добавление новой ценной бумаги
    User->>MainWindow: click add_button
    MainWindow->>MainController: handle_add()
    MainController->>+AddEditWindow: AddEditWindow(parent, is_edit=false)
    MainController->>+AddEditController: AddEditController(view, repository)
    AddEditWindow-->>User: display form
    User->>AddEditWindow: input data
    User->>AddEditWindow: click save_button
    AddEditWindow->>AddEditController: handle_save(data)
    AddEditController->>AddEditController: validate_data()
    Note right of AddEditController: Использование SecurityValidator
    AddEditController->>Repository: addSecurity(security)
    Repository->>Repository: notifyObservers()
    Repository->>MainController: update()
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: update UI
    AddEditWindow-->>User: destroy()
    end

    %% Редактирование ценной бумаги
    rect rgb(240, 220, 220)
    Note over User,DetailsWindow: Редактирование ценной бумаги
    User->>MainWindow: select security
    User->>MainWindow: click edit_button
    MainWindow->>MainController: handle_edit()
    MainController->>Repository: getById(security_id)
    Repository-->>MainController: return Security
    MainController->>+AddEditWindow: AddEditWindow(parent, is_edit=true)
    MainController->>+AddEditController: AddEditController(view, repository, security_id)
    AddEditWindow->>AddEditWindow: setSecurityData(security)
    AddEditWindow-->>User: display form
    User->>AddEditWindow: modify data
    User->>AddEditWindow: click save_button
    AddEditWindow->>AddEditController: handle_save(data)
    AddEditController->>AddEditController: validate_data()
    AddEditController->>Repository: replaceSecurity(security_id, security)
    Repository->>Repository: notifyObservers()
    Repository->>MainController: update()
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: update UI
    AddEditWindow-->>User: destroy()
    end

    %% Просмотр деталей
    rect rgb(240, 240, 220)
    Note over User,DetailsWindow: Просмотр деталей ценной бумаги
    User->>MainWindow: select security
    User->>MainWindow: click view_button
    MainWindow->>MainController: handle_view()
    MainController->>Repository: getById(security_id)
    Repository-->>MainController: return Security
    MainController->>+DetailsWindow: SecurityDetailsWindow(parent)
    MainController->>+DetailsController: SecurityDetailsController(view, security)
    DetailsController->>DetailsWindow: displaySecurity(security)
    DetailsWindow-->>User: display details
    User->>DetailsWindow: click close_button
    DetailsWindow-->>User: destroy()
    end

    %% Удаление ценной бумаги
    rect rgb(240, 220, 240)
    Note over User,DetailsWindow: Удаление ценной бумаги
    User->>MainWindow: select security
    User->>MainWindow: click delete_button
    MainWindow->>MainController: handle_delete()
    MainController->>MainWindow: show_confirmation("Are you sure...")
    MainWindow-->>User: messagebox.askyesno()
    User->>MainWindow: confirm
    MainWindow->>MainController: return True
    MainController->>Repository: deleteSecurity(security_id)
    Repository->>Repository: notifyObservers()
    Repository->>MainController: update()
    MainController->>MainController: checkPageBounds()
    Note right of MainController: Проверка и корректировка<br/>текущей страницы
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: update UI
    end

    %% Пагинация
    rect rgb(220, 240, 240)
    Note over User,DetailsWindow: Пагинация
    User->>MainWindow: click prev/next или<br/>изменить spinner
    MainWindow->>MainController: handle_page_change()
    MainController->>MainController: navigateToPage(new_page)
    MainController->>MainController: calculateTotalPages()
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: update UI
    end

    %% Сортировка
    rect rgb(220, 220, 240)
    Note over User,DetailsWindow: Сортировка
    User->>MainWindow: select sort_combobox
    MainWindow->>MainController: handle_sort(event)
    MainController->>Repository: get_k_n_short_list(k, n, sort_field)
    Repository-->>MainController: return securities[]
    MainController->>MainWindow: update_products(securities, total_items)
    MainController->>MainWindow: updatePaginationControls()
    MainWindow-->>User: update UI
    end
```

## Диаграмма классов
```mermaid
classDiagram
    %% Интерфейсы и абстрактные классы
    class Observer {
        <<interface>>
        +update()*
    }
    class Observable {
        <<interface>>
        +addObserver(observer: Observer)
        +removeObserver(observer: Observer)
        +notifyObservers()
    }
    class ISecurityRepository {
        <<interface>>
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: String): List~BriefSecurity~
        +addSecurity(security: Security)
        +replaceSecurity(id: int, security: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: String)
    }
    class BaseObservable {
        <<abstract>>
        -observers: List~Observer~
        +addObserver(observer: Observer)
        +removeObserver(observer: Observer)
        +notifyObservers()
    }
    
    %% Классы моделей
    class BriefSecurity {
        #securityId: int
        #name: String
        #type: String
        #currentPrice: BigDecimal
        +getters/setters()
    }
    class Security {
        -expectedReturn: BigDecimal
        +createNewSecurity()
        +updateExistingSecurity()
        +getExpectedReturn()
    }

    %% Репозитории и стратегии
    class AbstractSecurityRepository {
        <<abstract>>
        #Securitys: List[Security]
        #filename: str
        #serializationStrategy: SerializationStrategy
        +AbstractSecurityRepository(filename: str, strategy: SerializationStrategy)
        +readFromFile()
        +writeToFile()
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
        #generateNewId(): int
    }
    class SecurityRepository {
        +SecurityRepository(filename: str, strategy: SerializationStrategy)
    }
    class SecurityRepositoryAdapter {
        -fileRepository: AbstractSecurityRepository
        +SecurityRepositoryAdapter(fileRepository: AbstractSecurityRepository)
        +getById(id: int): Security
        +get_k_n_short_list(k: int, n: int, sortField: str): List[BriefSecurity]
        +addSecurity(Security: Security)
        +replaceSecurity(id: int, newSecurity: Security)
        +deleteSecurity(id: int)
        +get_count(): int
        +sort_by_field(field: str)
    }
    class SerializationStrategy {
        <<interface>>
        +readFromFile(filename: str): List[Security]
        +writeToFile(filename: str, Securitys: List[Security])
    }

    class AbstractSerializationStrategy {
        <<abstract>>
        #objectMapper: Any
        +readFromFile(filename: str): List[Security]
        +writeToFile(filename: str, Securitys: List[Security])
        #createObjectMapper()*: Any
    }

    class JsonSerializationStrategy {
        #read_data(file): List[dict]
        #write_data(file, data: List[dict])
    }

    class YamlSerializationStrategy {
        #read_data(file): List[dict]
        #write_data(file, data: List[dict])
    }
    class PostgreSQLRepository {
        -connection: PostgreSQLConnection
        +все методы ISecurityRepository
        -extractSecurityFromResultSet()
        -extractBriefSecurityFromResultSet()
    }
    class PostgreSQLConnection {
        -instance: PostgreSQLConnection
        -connection: Connection
        -PostgreSQLConnection()
        +getInstance(): PostgreSQLConnection
        +getConnection(): Connection
        +close()
    }

    %% Контроллеры
    class MainController {
        -repository: ISecurityRepository
        -mainWindow: MainWindow
        -currentSortField: String
        -currentPage: int
        +update()
        -initializeListeners()
        -initializePagination()
        -handleAdd()
        -handleEdit()
        -handleDelete()
        -handleView()
        -handleSort()
        -handlePageChange()
        -navigateToPage()
        -updateView()
    }
    class AddEditSecurityController {
        -view: AddEditSecurityWindow
        -repository: ISecurityRepository
        -editingSecurityId: Integer
        -handleSave()
        -validateInput()
    }
    class SecurityDetailsController {
        -view: SecurityDetailsWindow
        -security: Security
        -displaySecurityDetails()
    }

    %% Представления
    class MainWindow {
        -securitiesTable: JTable
        -tableModel: DefaultTableModel
        -buttons: JButton
        -sortComboBox: JComboBox
        -pageSpinner: JSpinner
        -pageSize: int
        +updateSecurities()
        +updatePaginationControls()
        +getters()
    }
    class AddEditSecurityWindow {
        -textFields: JTextField
        -buttons: JButton
        -isEditMode: boolean
        +setSecurityData()
        +getSecurityData()
        +getters()
    }
    class SecurityDetailsWindow {
        -labels: JLabel
        -closeButton: JButton
        +displaySecurity()
    }

    %% Отношения наследования
    Observer <|.. MainController : implements
    Observable <|.. BaseObservable : implements
    BaseObservable <|-- SecurityRepositoryAdapter : extends
    BaseObservable <|-- PostgreSQLRepository : extends
    ISecurityRepository <|.. AbstractSecurityRepository : implements
    ISecurityRepository <|.. SecurityRepositoryAdapter : implements
    ISecurityRepository <|.. PostgreSQLRepository : implements
    AbstractSecurityRepository <|-- SecurityRepository : extends
    BriefSecurity <|-- Security : extends
    AbstractSecurityRepository --> SerializationStrategy : uses
    SerializationStrategy <|.. AbstractSerializationStrategy : implements
    AbstractSerializationStrategy <|-- JsonSerializationStrategy : extends
    AbstractSerializationStrategy <|-- YamlSerializationStrategy : extends
    PostgreSQLRepository --> PostgreSQLConnection : uses

    %% Композиция (сильная зависимость, жизненный цикл)
    MainController *-- MainWindow : creates and manages
    AddEditSecurityController *-- AddEditSecurityWindow : creates and manages
    SecurityDetailsController *-- SecurityDetailsWindow : creates and manages

    %% Агрегация (слабая зависимость)
    MainController o-- ISecurityRepository : uses
    AddEditSecurityController o-- ISecurityRepository : uses
    SecurityRepositoryAdapter o-- AbstractSecurityRepository : adapts
    SecurityDetailsController o-- Security : displays

    %% Зависимости
    MainController ..> AddEditSecurityController : creates
    MainController ..> SecurityDetailsController : creates
    AddEditSecurityWindow ..> Security : creates/modifies
    MainWindow ..> BriefSecurity : displays
```