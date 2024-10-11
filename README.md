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